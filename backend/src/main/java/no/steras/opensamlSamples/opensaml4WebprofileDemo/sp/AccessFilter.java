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
 * Service Provider (SP) 側のアクセス制御フィルター。
 * 
 * 役割:
 * 1. ユーザーのセッションが認証済みかどうかをチェック。
 * 2. 未認証の場合、SAML 認証フローを開始 (AuthnRequest を作成して IdP へリダイレクト)。
 * 3. 認証後の戻り先 URL をセッションに保存。
 */
public class AccessFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

	/**
	 * フィルター初期化時に OpenSAML のランタイム環境をセットアップします。
	 * OpenSAML を使用する前に必ず `InitializationService.initialize()` を呼び出す必要があります。
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// Java の暗号化ライブラリが OpenSAML の要件を満たしているかチェック・初期化
		JavaCryptoValidationInitializer javaCryptoValidationInitializer = new JavaCryptoValidationInitializer();
		try {
			javaCryptoValidationInitializer.init();

			// デバッグ用: 利用可能なセキュリティプロバイダー（SunJCE等）をログ出力
			for (Provider jceProvider : Security.getProviders()) {
				logger.info("JCEプロバイダー情報: {}", jceProvider.getInfo());
			}

			// XMLオブジェクトの生成や解析に使用するレジストリの設定
			XMLObjectProviderRegistry registry = new XMLObjectProviderRegistry();
			ConfigurationService.register(XMLObjectProviderRegistry.class, registry);

			// セキュアに設定された XML パーサープールを登録
			registry.setParserPool(getParserPool());

			logger.info("OpenSAML 5 の初期化を開始します...");
			InitializationService.initialize();
		} catch (InitializationException e) {
			throw new RuntimeException("OpenSAML の初期化に失敗しました", e);
		}
	}

	/**
	 * XML 解析のためのパーサープールを構築します。
	 * セキュリティ上の理由から、XXE 攻撃（外部実体参照）などを無効化した設定を行います。
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

		// XXE 攻撃対策のための機能を明示的に設定
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
			logger.error("ParserPool の初期化中にエラーが発生しました: " + e.getMessage(), e);
		}

		return parserPool;
	}

	/**
	 * リクエストをインターセプトし、認証状態に応じた処理を行います。
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		logger.info("AccessFilter: リクエスト受信 - URL: {}", httpServletRequest.getRequestURL());

		// 1. セッションに認証済みフラグがあるか確認
		if (httpServletRequest.getSession().getAttribute(SPConstants.AUTHENTICATED_SESSION_ATTRIBUTE) != null) {
			logger.info("セッション認証済み。リクエストを後続の処理（サーブレット等）へ渡します。");
			chain.doFilter(request, response);
		} else {
			// 2. 未認証の場合は SAML 認証（SSO）を開始
			logger.info("未認証。SAML AuthnRequest を生成し、IdP へリダイレクトします。");
			
			// 認証成功後に戻ってくるための URL をセッションに記憶
			setGotoURLOnSession(httpServletRequest);
			
			// IdP へのリダイレクト処理を実行
			redirectUserForAuthentication(httpServletResponse);
		}
	}

	/**
	 * 認証後の戻り先 URL をセッションに保存します。
	 */
	private void setGotoURLOnSession(HttpServletRequest request) {
		String currentUrl = request.getRequestURL().toString();
		request.getSession().setAttribute(SPConstants.GOTO_URL_SESSION_ATTRIBUTE, currentUrl);
		logger.debug("戻り先URLを保存しました: {}", currentUrl);
	}

	/**
	 * AuthnRequest を構築し、IdP へリダイレクト送信します。
	 */
	private void redirectUserForAuthentication(HttpServletResponse httpServletResponse) {
		// 1. AuthnRequest オブジェクトの構築
		AuthnRequest authnRequest = buildAuthnRequest();
		
		// 2. ブラウザ経由のリダイレクト送信を実行
		redirectUserWithRequest(httpServletResponse, authnRequest);
	}

	/**
	 * HTTP Redirect Binding を使用して AuthnRequest を送信します。
	 */
	private void redirectUserWithRequest(HttpServletResponse httpServletResponse, AuthnRequest authnRequest) {
		// 送信用のメッセージコンテキストを作成
		MessageContext context = new MessageContext();
		context.setMessage(authnRequest);

		// RelayState （状態維持用の文字列）を設定。必要に応じて元のURL等を含めることもあります。
		SAMLBindingContext bindingContext = context.getSubcontext(SAMLBindingContext.class, true);
		bindingContext.setRelayState("teststate");

		// IdP のエンドポイント情報をコンテキストに追加
		SAMLPeerEntityContext peerEntityContext = context.getSubcontext(SAMLPeerEntityContext.class, true);
		SAMLEndpointContext endpointContext = peerEntityContext.getSubcontext(SAMLEndpointContext.class, true);
		endpointContext.setEndpoint(getIPDEndpoint());

		// 署名パラメータの設定（HTTP Redirect Binding の場合、クエリパラメータに署名が付与されます）
		SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
		signatureSigningParameters.setSigningCredential(SPCredentials.getCredential());
		signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);

		context.getSubcontext(SecurityParametersContext.class, true)
				.setSignatureSigningParameters(signatureSigningParameters);

		// HTTP-Redirect エンコーダーを使用してレスポンスを構成
		HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();
		encoder.setHttpServletResponseSupplier(() -> httpServletResponse);
		encoder.setMessageContext(context);

		try {
			encoder.initialize();
			
			// デバッグ用: 生成された AuthnRequest XML を表示
			logger.info("生成された AuthnRequest (未エンコード状態):");
			OpenSAMLUtils.logSAMLObject(authnRequest);

			logger.info("IdP ({}) へリダイレクトします。", IDPConstants.SSO_SERVICE);
			
			// AuthnRequest を Deflate 圧縮 + Base64 署名してリダイレクトを実行
			encoder.encode();
		} catch (ComponentInitializationException | MessageEncodingException e) {
			throw new RuntimeException("SAML リクエストのエンコードまたは送信に失敗しました", e);
		}
	}

	/**
	 * OpenSAML オブジェクトを使用して AuthnRequest を詳細に構成します。
	 */
	private AuthnRequest buildAuthnRequest() {
		AuthnRequest authnRequest = OpenSAMLUtils.buildSAMLObject(AuthnRequest.class);
		
		// 1. 各種メタデータの設定
		authnRequest.setIssueInstant(Instant.now());
		authnRequest.setDestination(getIPDSSODestination()); // 送信先(IdP)のSSOエンドポイント
		
		// 2. 認証後のレスポンスとして Artifact Binding を要求
		authnRequest.setProtocolBinding(SAMLConstants.SAML2_ARTIFACT_BINDING_URI);
		
		// 3. 認証後の戻り先（ACS）を指定
		authnRequest.setAssertionConsumerServiceURL(getAssertionConsumerEndpoint());
		
		// 4. 一意なIDと発行者（SP）の設定
		authnRequest.setID(OpenSAMLUtils.generateSecureRandomId());
		authnRequest.setIssuer(buildIssuer());
		
		// 5. ユーザー識別子(NameID)のポリシーと必須認証レベルの設定
		authnRequest.setNameIDPolicy(buildNameIdPolicy());
		authnRequest.setRequestedAuthnContext(buildRequestedAuthnContext());

		return authnRequest;
	}

	/**
	 * IdP に対して特定の認証方法（例: パスワード認証）を要求します。
	 */
	private RequestedAuthnContext buildRequestedAuthnContext() {
		RequestedAuthnContext requestedAuthnContext = OpenSAMLUtils.buildSAMLObject(RequestedAuthnContext.class);
		requestedAuthnContext.setComparison(AuthnContextComparisonTypeEnumeration.MINIMUM);

		AuthnContextClassRef passwordAuthnContextClassRef = OpenSAMLUtils.buildSAMLObject(AuthnContextClassRef.class);
		passwordAuthnContextClassRef.setURI(AuthnContext.PASSWORD_AUTHN_CTX);

		requestedAuthnContext.getAuthnContextClassRefs().add(passwordAuthnContextClassRef);

		return requestedAuthnContext;
	}

	/**
	 * NameID （ユーザー識別子）の形式を指定します。
	 * ここではセッションごとに異なる「TRANSIENT (一時的)」な ID を要求しています。
	 */
	private NameIDPolicy buildNameIdPolicy() {
		NameIDPolicy nameIDPolicy = OpenSAMLUtils.buildSAMLObject(NameIDPolicy.class);
		nameIDPolicy.setAllowCreate(true);
		nameIDPolicy.setFormat(NameIDType.TRANSIENT);
		return nameIDPolicy;
	}

	/**
	 * 発行者（Issuer）要素を構築します。通常は SP のエンティティ ID を含めます。
	 */
	private Issuer buildIssuer() {
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(getSPIssuerValue());
		return issuer;
	}

	private String getSPIssuerValue() {
		return SPConstants.SP_ENTITY_ID;
	}

	private String getAssertionConsumerEndpoint() {
		return SPConstants.ASSERTION_CONSUMER_SERVICE;
	}

	private String getIPDSSODestination() {
		return IDPConstants.SSO_SERVICE;
	}

	/**
	 * IdP の SSO サービスエンドポイント情報を定義します。
	 */
	private Endpoint getIPDEndpoint() {
		SingleSignOnService endpoint = OpenSAMLUtils.buildSAMLObject(SingleSignOnService.class);
		endpoint.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
		endpoint.setLocation(getIPDSSODestination());
		return endpoint;
	}

	public void destroy() {
		// リソースの解放が必要な場合はここに記述
	}
}