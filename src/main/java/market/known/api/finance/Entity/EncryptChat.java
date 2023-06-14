package market.known.api.finance.Entity;

import lombok.Data;

@Data
public class EncryptChat {

    private String messageId;

    private Long seq;

    private Integer publicKeyVersion;

    private String encryptRandomKey;

    private String encryptChatMessage;

    private Boolean decrypted = false;

}
