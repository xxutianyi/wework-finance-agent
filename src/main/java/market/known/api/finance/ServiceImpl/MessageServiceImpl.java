package market.known.api.finance.ServiceImpl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.tencent.wework.Finance;
import market.known.api.finance.Entity.Credential;
import market.known.api.finance.Exception.FinanceSDKException;
import market.known.api.finance.Service.MessageService;
import market.known.api.finance.Utils.FileTypeHelper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    private Credential credential;

    private Long financeSDK;

    @Override
    public void init(String corpId, String financeSecret, String financePrivateKey) throws Exception {

        credential = new Credential(corpId, financeSecret, financePrivateKey);

        initFinance();
    }

    private void initFinance() throws FinanceSDKException {
        int ret;
        long sdk = Finance.NewSdk();

        ret = Finance.Init(sdk, credential.getCorpId(), credential.getFinanceSecret());
        if (ret != 0) {
            Finance.DestroySdk(sdk);
            throw new FinanceSDKException(ret);
        }

        financeSDK = sdk;
    }

    @Override
    public Boolean destroySdk() {
        Finance.DestroySdk(financeSDK);
        return true;
    }

    @Override
    public List<JSONObject> getMessages(int seq, int limit) throws Exception {

        if (ObjectUtil.isNull(financeSDK)) {
            return null;
        }

        long slice = Finance.NewSlice();
        int ret = Finance.GetChatData(financeSDK, seq, limit, "", "", 30, slice);
        if (ret != 0) {
            Finance.FreeSlice(slice);
            throw new FinanceSDKException(ret);
        }
        String result = Finance.GetContentFromSlice(slice);
        Finance.FreeSlice(slice);

        JSONObject jsonObject = JSONUtil.parseObj(result);

        if (!jsonObject.get("errcode").equals(0)) {
            throw new Exception(String.valueOf(jsonObject.get("errmsg")));
        }

        List<JSONObject> encryptMessages = (new JSONArray(jsonObject.get("chatdata"))).toList(JSONObject.class);
        List<JSONObject> decryptMessages = new ArrayList<>();
        for (JSONObject object : encryptMessages) {

            JSONObject decryptMessage = decryptData(object);

            decryptMessages.add(decryptMessage);

        }
        return decryptMessages;
    }

    private JSONObject decryptData(JSONObject object) throws FinanceSDKException {

        if (ObjectUtil.isNull(financeSDK)) {
            return null;
        }

        String encryptRandomKey = (String) object.get("encrypt_random_key");
        String encryptMessage = (String) object.get("encrypt_chat_msg");
        Long seq = Long.valueOf((Integer) object.get("seq"));

        String encryptKey = decryptRandomKey(encryptRandomKey, credential.getFinancePrivateKey());

        long msg = Finance.NewSlice();
        int ret = Finance.DecryptData(financeSDK, encryptKey, encryptMessage, msg);
        if (ret != 0) {
            Finance.FreeSlice(msg);
            throw new FinanceSDKException(ret);
        }
        String result = Finance.GetContentFromSlice(msg);
        Finance.FreeSlice(msg);

        JSONObject resultJson = JSONUtil.parseObj(result);
        resultJson.append("seq", seq);

        return resultJson;

    }

    private String decryptRandomKey(String encryptRandomKey, RSAPrivateKey privateKey) {

        String decryptRandomKey = null;

        try {

            byte[] encryptRandomKeyBytes = Base64.decodeBase64(encryptRandomKey);
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            decryptRandomKey = new String(cipher.doFinal(encryptRandomKeyBytes));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptRandomKey;
    }

    @Value("${wework.storage}")
    private String filepath;

    @Override
    public String getMediaData(String sdkFileId) throws FinanceSDKException {

        String outputFileName = UUID.randomUUID().toString();

        String indexbuf = "";

        File outputFile = new File(filepath + outputFileName);

        while (true) {
            long media_data = Finance.NewMediaData();
            int ret = Finance.GetMediaData(financeSDK, indexbuf, sdkFileId, "", "", 30, media_data);
            if (ret != 0) {
                Finance.FreeMediaData(media_data);
                throw new FinanceSDKException(ret);
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(outputFile, true);
                outputStream.write(Finance.GetData(media_data));
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Finance.IsMediaDataFinish(media_data) == 1) {
                Finance.FreeMediaData(media_data);
                return setFileType(outputFile);
            } else {
                indexbuf = Finance.GetOutIndexBuf(media_data);
                Finance.FreeMediaData(media_data);
            }
        }
    }

    private String setFileType(File file) {

        try {

            String type = FileTypeHelper.getFileType(file.getPath());

            File renamedFile = new File(file.getPath() + "." + type);

            if (!file.renameTo(renamedFile)) {
                throw new IOException("Rename Failed");
            }

            return uploadToObjectStorage(renamedFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Value("${tencent-cloud.id}")
    String secretId;

    @Value("${tencent-cloud.key}")
    String secretKey;

    @Value("${tencent-cloud.bucket}")
    String bucketName;

    @Value("${tencent-cloud.region}")
    String region;

    private String uploadToObjectStorage(File file) {

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(cred, clientConfig);

        String key = credential.getCorpId() + "/" + file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        cosClient.putObject(putObjectRequest);

        URL url = cosClient.getObjectUrl(bucketName, key);

        file.delete();

        return url.toString();
    }

}
