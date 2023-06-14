package market.known.api.finance.Entity;

import lombok.Data;
import market.known.api.finance.Utils.PrivateKeyHelper;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

@Data
public class Credential {

    private String corpId;

    private String financeSecret;

    private RSAPrivateKey financePrivateKey;

    public Credential(String corpId, String financeSecret, String financePrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        this.corpId = corpId;
        this.financeSecret = financeSecret;
        this.financePrivateKey = PrivateKeyHelper.fromString(financePrivateKey);
    }
}
