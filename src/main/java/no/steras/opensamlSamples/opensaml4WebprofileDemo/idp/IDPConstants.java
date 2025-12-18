package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

/**
 * IDP定数クラス
 */
public class IDPConstants {
	// エンティティID
	public static final String IDP_ENTITY_ID = "TestIDP";
	// SSOサービスURL(SSOエンドポイント SAMLリクエスト受信URL ⇨ SAMLレスポンス送信URL)
	public static final String SSO_SERVICE = "http://localhost:8080/opensaml4-webprofile-demo/idp/singleSignOnService";
	public static final String ARTIFACT_RESOLUTION_SERVICE = "http://localhost:8080/opensaml4-webprofile-demo/idp/artifactResolutionService";
}
