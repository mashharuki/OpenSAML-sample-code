package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

/**
 * IDP定数クラス
 */
public class IDPConstants {
	// ベースURL（環境変数から取得、未設定時はlocalhostをデフォルトとして使用）
	private static final String BASE_URL = System.getenv("BASE_URL") != null
		? System.getenv("BASE_URL")
		: "http://localhost:8080";

	// エンティティID
	public static final String IDP_ENTITY_ID = "TestIDP";
	// SSOサービスURL(SSOエンドポイント SAMLリクエスト受信URL ⇨ SAMLレスポンス送信URL)
	public static final String SSO_SERVICE = BASE_URL + "/opensaml5-webprofile-demo/idp/singleSignOnService";
	public static final String ARTIFACT_RESOLUTION_SERVICE = BASE_URL + "/opensaml5-webprofile-demo/idp/artifactResolutionService";
}
