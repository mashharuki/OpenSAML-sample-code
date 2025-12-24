package no.steras.opensamlSamples.opensaml4WebprofileDemo.sp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * SP用の定数クラス
 */
@Component
public class SPConstants {
    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.sp-entity-id}")
    private String spEntityId;

    public static String SP_ENTITY_ID;
    public static final String AUTHENTICATED_SESSION_ATTRIBUTE = "authenticated";
    public static final String GOTO_URL_SESSION_ATTRIBUTE = "gotoURL";
    public static String ASSERTION_CONSUMER_SERVICE;

    @PostConstruct
    public void init() {
        SP_ENTITY_ID = spEntityId;
        ASSERTION_CONSUMER_SERVICE = baseUrl + "/opensaml5-webprofile-demo/sp/consumer";
    }
}
