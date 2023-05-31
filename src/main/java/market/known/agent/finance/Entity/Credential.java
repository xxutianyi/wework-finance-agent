package market.known.agent.finance.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Data
@Entity
public class Credential {

    @Id
    private String corpId;

    private String financeSecret;

    @Column(length = 2048)
    private RSAPrivateKey financePrivateKey;

    public Credential(String corpId, String financeSecret, String financePrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        this.corpId = corpId;
        this.financeSecret = financeSecret;

        financePrivateKey = financePrivateKey.replace("-----BEGIN PRIVATE KEY-----", "");
        financePrivateKey = financePrivateKey.replace("-----END PRIVATE KEY-----", "");

        byte[] decoded = Base64.decodeBase64(financePrivateKey);

        this.financePrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    public Credential() {

    }
}
