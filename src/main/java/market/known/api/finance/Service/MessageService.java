package market.known.api.finance.Service;

import cn.hutool.json.JSONObject;
import market.known.api.finance.Exception.FinanceSDKException;

import java.util.List;

public interface MessageService {

    void init(String corpId, String financeSecret, String financePrivateKey) throws Exception;

    Boolean destroySdk();

    public List<JSONObject> getMessages(int seq, int limit) throws Exception;

    public String getMediaData(String sdkFileId) throws FinanceSDKException;

}
