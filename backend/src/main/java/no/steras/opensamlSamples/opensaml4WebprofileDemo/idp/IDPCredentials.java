package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.crypto.KeySupport;
import java.security.*;

/**
 * IDP用資格情報クラス
 */
public class IDPCredentials {
    private static final Credential credential;

    static {
        credential = generateCredential();
    }

    /**
     * 資格情報の生成
     */
    private static Credential generateCredential() {
        try {
            // キーペアの生成
            KeyPair keyPair = KeySupport.generateKeyPair("RSA", 1024, null);
            return CredentialSupport.getSimpleCredential(keyPair.getPublic(), keyPair.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 資格情報の取得
     */
    public static Credential getCredential() {
        return credential;
    }

}
