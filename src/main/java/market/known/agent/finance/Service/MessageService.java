package market.known.agent.finance.Service;

import cn.hutool.json.JSONObject;
import market.known.agent.finance.Exception.FinanceSDKException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface MessageService {

    void setCredential(String corpId, String secret, String rsaPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException;

    void deleteCredential(String corpId);

    void init(String corpId) throws Exception;

    Boolean destroySdk();

    public List<JSONObject> getMessages(int seq, int limit, String proxy, String passwd, int timeout) throws Exception;

    public String getMediaData(String sdkFileId) throws FinanceSDKException;

}
