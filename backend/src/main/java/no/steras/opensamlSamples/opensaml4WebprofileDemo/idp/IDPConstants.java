package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * IDP定数クラス
 */
@Component
public class IDPConstants {
    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.idp-entity-id}")
    private String idpEntityId;

    public static String IDP_ENTITY_ID;
    public static String SSO_SERVICE;
    public static String ARTIFACT_RESOLUTION_SERVICE;

    @PostConstruct
    public void init() {
        IDP_ENTITY_ID = idpEntityId;
        SSO_SERVICE = baseUrl + "/opensaml5-webprofile-demo/idp/singleSignOnService";
        ARTIFACT_RESOLUTION_SERVICE = baseUrl + "/opensaml5-webprofile-demo/idp/artifactResolutionService";
    }
}
