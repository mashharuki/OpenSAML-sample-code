package no.steras.opensamlSamples.opensaml4WebprofileDemo.sp;

/**
 * SP用の定数クラス
 */
public class SPConstants {
	// ベースURL（環境変数から取得、未設定時はlocalhostをデフォルトとして使用）
	private static final String BASE_URL = System.getenv("BASE_URL") != null
		? System.getenv("BASE_URL")
		: "http://localhost:8080";

	public static final String SP_ENTITY_ID = "TestSP";
	public static final String AUTHENTICATED_SESSION_ATTRIBUTE = "authenticated";
	public static final String GOTO_URL_SESSION_ATTRIBUTE = "gotoURL";
	// ACS URL
	public static final String ASSERTION_CONSUMER_SERVICE = BASE_URL + "/opensaml5-webprofile-demo/sp/consumer";

}
