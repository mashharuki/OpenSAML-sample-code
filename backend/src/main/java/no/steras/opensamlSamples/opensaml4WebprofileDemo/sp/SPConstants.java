package no.steras.opensamlSamples.opensaml4WebprofileDemo.sp;

/**
 * SP用の定数クラス
 */
public class SPConstants {
	public static final String SP_ENTITY_ID = "TestSP";
	public static final String AUTHENTICATED_SESSION_ATTRIBUTE = "authenticated";
	public static final String GOTO_URL_SESSION_ATTRIBUTE = "gotoURL";
	// ACS URL
	public static final String ASSERTION_CONSUMER_SERVICE = "http://localhost:8080/opensaml5-webprofile-demo/sp/consumer";

}
