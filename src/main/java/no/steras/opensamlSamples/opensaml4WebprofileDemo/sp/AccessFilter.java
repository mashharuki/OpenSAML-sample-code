package no.steras.opensamlSamples.opensaml4WebprofileDemo.sp;

import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.saml.common.messaging.context.SAMLBindingContext;
import org.opensaml.saml.common.messaging.context.SAMLEndpointContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPRedirectDeflateEncoder;
import org.opensaml.saml.saml2.core.AuthnContext;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnContextComparisonTypeEnumeration;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameIDPolicy;
import org.opensaml.saml.saml2.core.NameIDType;
import org.opensaml.saml.saml2.core.RequestedAuthnContext;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.config.impl.JavaCryptoValidationInitializer;
import org.opensaml.xmlsec.context.SecurityParametersContext;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.shared.component.ComponentInitializationException;
import net.shibboleth.shared.xml.impl.BasicParserPool;
import net.shibboleth.shared.xml.ParserPool;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.OpenSAMLUtils;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.idp.IDPConstants;

/**
 * アクセスフィルター
 * ユーザーをインターセプトし、未認証の場合はSAML認証を開始します。
 * このフィルターはSP（Service Provider）側で動作し、保護されたリソースへのアクセス時に
 * ユーザーがIdP（Identity Provider）で認証されているかをチェックします。
 */
public class AccessFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

	/**
	 * フィルターの初期化
	 * OpenSAMLライブラリとXMLパーサーを初期化します。
	 * Java暗号化検証の初期化、セキュリティプロバイダーの登録、
	 * XMLオブジェクトプロバイダーレジストリの設定を行います。
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		JavaCryptoValidationInitializer javaCryptoValidationInitializer = new JavaCryptoValidationInitializer();
		try {
			javaCryptoValidationInitializer.init();

			for (Provider jceProvider : Security.getProviders()) {
				logger.info(jceProvider.getInfo());
			}

			XMLObjectProviderRegistry registry = new XMLObjectProviderRegistry();
			ConfigurationService.register(XMLObjectProviderRegistry.class, registry);

			registry.setParserPool(getParserPool());

			logger.info("Initializing");
			InitializationService.initialize();
		} catch (InitializationException e) {
			throw new RuntimeException("Initialization failed");
		}
	}

	/**
	 * XMLパーサープールの取得
	 * セキュアなXML処理を行うためのパーサープールを構成します。
	 * XXE（XML外部エンティティ）攻撃などを防ぐため、各種セキュリティ機能を有効化します。
	 */
	private static ParserPool getParserPool() {
		BasicParserPool parserPool = new BasicParserPool();
		parserPool.setMaxPoolSize(100);
		parserPool.setCoalescing(true);
		parserPool.setIgnoreComments(true);
		parserPool.setIgnoreElementContentWhitespace(true);
		parserPool.setNamespaceAware(true);
		parserPool.setExpandEntityReferences(false);
		parserPool.setXincludeAware(false);

		final Map<String, Boolean> features = new HashMap<String, Boolean>();
		features.put("http://xml.org/sax/features/external-general-entities", Boolean.FALSE);
		features.put("http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE);
		features.put("http://apache.org/xml/features/disallow-doctype-decl", Boolean.TRUE);
		features.put("http://apache.org/xml/features/validation/schema/normalized-value", Boolean.FALSE);
		features.put("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE);

		parserPool.setBuilderFeatures(features);

		parserPool.setBuilderAttributes(new HashMap<String, Object>());

		try {
			parserPool.initialize();
		} catch (ComponentInitializationException e) {
			logger.error(e.getMessage(), e);
		}

		return parserPool;
	}

	/**
	 * フィルター処理のメインメソッド
	 * リクエストごとに呼び出され、ユーザーの認証状態をチェックします。
	 * 認証済みの場合は次のフィルターチェーンへ進み、未認証の場合はIdPへリダイレクトします。
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// セッションに認証情報が存在するかチェック
		if (httpServletRequest.getSession().getAttribute(SPConstants.AUTHENTICATED_SESSION_ATTRIBUTE) != null) {
			// 認証済み：次のフィルターチェーンへ進む
			chain.doFilter(request, response);
		} else {
			// 未認証：認証後の戻り先URLをセッションに保存し、IdPへリダイレクト
			setGotoURLOnSession(httpServletRequest);
			redirectUserForAuthentication(httpServletResponse);
		}
	}

	/**
	 * 認証後の戻り先URLをセッションに保存
	 * ユーザーが元々アクセスしようとしていたURLを記憶しておき、
	 * IdPでの認証完了後にこのURLへリダイレクトします。
	 */
	private void setGotoURLOnSession(HttpServletRequest request) {
		request.getSession().setAttribute(SPConstants.GOTO_URL_SESSION_ATTRIBUTE, request.getRequestURL().toString());
	}

	/**
	 * ユーザーをIdPの認証ページへリダイレクト
	 * AuthnRequestを構築し、ユーザーをIdPのシングルサインオンエンドポイントへリダイレクトします。
	 */
	private void redirectUserForAuthentication(HttpServletResponse httpServletResponse) {
		AuthnRequest authnRequest = buildAuthnRequest();
		redirectUserWithRequest(httpServletResponse, authnRequest);

	}

	/**
	 * AuthnRequestを使ってユーザーをリダイレクト
	 * HTTP Redirect Bindingを使用してAuthnRequestをIdPへ送信します。
	 * メッセージコンテキストを構成し、署名パラメータを設定して、
	 * HTTPRedirectDeflateEncoderでエンコードします。
	 */
	private void redirectUserWithRequest(HttpServletResponse httpServletResponse, AuthnRequest authnRequest) {

		// メッセージコンテキストの作成と設定
		// メッセージコンテキストの作成と設定
		MessageContext context = new MessageContext();

		context.setMessage(authnRequest);

		// SAMLバインディングコンテキストの設定（RelayState付き）
		SAMLBindingContext bindingContext = context.getSubcontext(SAMLBindingContext.class, true);
		bindingContext.setRelayState("teststate");

		// IdPエンティティコンテキストの設定
		SAMLPeerEntityContext peerEntityContext = context.getSubcontext(SAMLPeerEntityContext.class, true);

		// IdPエンドポイントの設定
		SAMLEndpointContext endpointContext = peerEntityContext.getSubcontext(SAMLEndpointContext.class, true);
		endpointContext.setEndpoint(getIPDEndpoint());

		// 署名パラメータの設定（RSA-SHA256を使用）
		SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
		signatureSigningParameters.setSigningCredential(SPCredentials.getCredential());
		signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);

		context.getSubcontext(SecurityParametersContext.class, true)
				.setSignatureSigningParameters(signatureSigningParameters);

		// HTTP Redirect Bindingエンコーダーの設定
		HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();
		encoder.setHttpServletResponseSupplier(() -> httpServletResponse);
		encoder.setMessageContext(context);

		try {
			encoder.initialize();
		} catch (ComponentInitializationException e) {
			throw new RuntimeException(e);
		}

		// AuthnRequestをログに出力（デバッグ用）
		logger.info("AuthnRequest: ");
		OpenSAMLUtils.logSAMLObject(authnRequest);

		logger.info("Redirecting to IDP");
		try {
			encoder.encode();
		} catch (MessageEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * AuthnRequestの構築
	 * SAML認証リクエストを作成します。
	 * このリクエストには、発行者情報、宛先、プロトコルバインディング、
	 * NameIDポリシー、要求される認証コンテキストなどが含まれます。
	 */
	private AuthnRequest buildAuthnRequest() {
		AuthnRequest authnRequest = OpenSAMLUtils.buildSAMLObject(AuthnRequest.class);
		authnRequest.setIssueInstant(Instant.now()); // 発行時刻
		authnRequest.setDestination(getIPDSSODestination()); // IdPのSSOエンドポイント
		authnRequest.setProtocolBinding(SAMLConstants.SAML2_ARTIFACT_BINDING_URI); // Artifactバインディングを使用
		authnRequest.setAssertionConsumerServiceURL(getAssertionConsumerEndpoint()); // SPのレスポンス受信エンドポイント
		authnRequest.setID(OpenSAMLUtils.generateSecureRandomId()); // 一意のリクエストID
		authnRequest.setIssuer(buildIssuer()); // 発行者（SP）
		authnRequest.setNameIDPolicy(buildNameIdPolicy()); // NameIDポリシー
		authnRequest.setRequestedAuthnContext(buildRequestedAuthnContext()); // 要求される認証コンテキスト

		return authnRequest;
	}

	/**
	 * 要求される認証コンテキストの構築
	 * IdPに対して、パスワード認証以上のレベルを要求します。
	 */
	private RequestedAuthnContext buildRequestedAuthnContext() {
		RequestedAuthnContext requestedAuthnContext = OpenSAMLUtils.buildSAMLObject(RequestedAuthnContext.class);
		requestedAuthnContext.setComparison(AuthnContextComparisonTypeEnumeration.MINIMUM); // 最小レベルの比較

		// パスワード認証コンテキストを指定
		AuthnContextClassRef passwordAuthnContextClassRef = OpenSAMLUtils.buildSAMLObject(AuthnContextClassRef.class);
		passwordAuthnContextClassRef.setURI(AuthnContext.PASSWORD_AUTHN_CTX);

		requestedAuthnContext.getAuthnContextClassRefs().add(passwordAuthnContextClassRef);

		return requestedAuthnContext;

	}

	/**
	 * NameIDポリシーの構築
	 * IdPに対して、一時的な（TRANSIENT）NameIDフォーマットの作成を許可します。
	 */
	private NameIDPolicy buildNameIdPolicy() {
		NameIDPolicy nameIDPolicy = OpenSAMLUtils.buildSAMLObject(NameIDPolicy.class);
		nameIDPolicy.setAllowCreate(true); // 新規NameIDの作成を許可

		nameIDPolicy.setFormat(NameIDType.TRANSIENT); // 一時的なNameIDフォーマット

		return nameIDPolicy;
	}

	/**
	 * 発行者（Issuer）の構築
	 * このSPのエンティティIDを設定します。
	 */
	private Issuer buildIssuer() {
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(getSPIssuerValue());

		return issuer;
	}

	/** SPのエンティティIDを取得 */
	private String getSPIssuerValue() {
		return SPConstants.SP_ENTITY_ID;
	}

	/** Assertionを受け取るSPのエンドポイントURLを取得 */
	private String getAssertionConsumerEndpoint() {
		return SPConstants.ASSERTION_CONSUMER_SERVICE;
	}

	/** IdPのシングルサインオンサービスのURLを取得 */
	private String getIPDSSODestination() {
		return IDPConstants.SSO_SERVICE;
	}

	/**
	 * IdPエンドポイントオブジェクトの構築
	 * HTTP Redirectバインディングを使用するSingleSignOnServiceエンドポイントを作成します。
	 */
	private Endpoint getIPDEndpoint() {
		SingleSignOnService endpoint = OpenSAMLUtils.buildSAMLObject(SingleSignOnService.class);
		endpoint.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
		endpoint.setLocation(getIPDSSODestination());

		return endpoint;
	}

	/** フィルターの破棄（クリーンアップ処理） */
	public void destroy() {

	}
}