package no.steras.opensamlSamples.opensaml4WebprofileDemo.sp;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.messaging.handler.MessageHandler;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.messaging.handler.impl.BasicMessageHandlerChain;
import org.opensaml.messaging.pipeline.httpclient.BasicHttpClientMessagePipeline;
import org.opensaml.messaging.pipeline.httpclient.HttpClientMessagePipeline;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.saml.common.binding.security.impl.MessageLifetimeSecurityHandler;
import org.opensaml.saml.common.binding.security.impl.ReceivedEndpointSecurityHandler;
import org.opensaml.saml.common.binding.security.impl.SAMLOutboundProtocolMessageSigningHandler;
import org.opensaml.saml.common.messaging.context.SAMLMessageInfoContext;
import org.opensaml.saml.saml2.binding.decoding.impl.HttpClientResponseSOAP11Decoder;
import org.opensaml.saml.saml2.binding.encoding.impl.HttpClientRequestSOAP11Encoder;
import org.opensaml.saml.saml2.core.Artifact;
import org.opensaml.saml.saml2.core.ArtifactResolve;
import org.opensaml.saml.saml2.core.ArtifactResponse;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.EncryptedAssertion;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.encryption.Decrypter;
import org.opensaml.saml.security.impl.SAMLSignatureProfileValidator;
import org.opensaml.soap.client.http.AbstractPipelineHttpSOAPClient;
import org.opensaml.soap.common.SOAPException;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.context.SecurityParametersContext;
import org.opensaml.xmlsec.encryption.support.DecryptionException;
import org.opensaml.xmlsec.encryption.support.InlineEncryptedKeyResolver;
import org.opensaml.xmlsec.keyinfo.impl.StaticKeyInfoCredentialResolver;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.SignatureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.shared.component.ComponentInitializationException;
import net.shibboleth.shared.httpclient.HttpClientBuilder;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.OpenSAMLUtils;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.idp.IDPConstants;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.idp.IDPCredentials;

/**
 * Service Provider (SP) 側の Assertion Consumer Service (ACS) エンドポイント。
 * 
 * 役割:
 * 1. IdP からブラウザ経由で送られてくる SAML アーティファクトを受信。
 * 2. アーティファクトを使用して IdP と直接通信（バックチャネル）し、実際のアサーションを取得。
 * 3. 取得したアサーションの復号、署名検証、有効期限チェックを実行。
 * 4. 全ての検証に成功すれば、ユーザーを認証済みとしてセッションを更新。
 */
public class ConsumerServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(ConsumerServlet.class);

	/**
	 * IdP からのリダイレクト（または POST）を受け取り、SAML 認証フローを完結させます。
	 */
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("ConsumerServlet: IdP からのレスポンスを受信しました。リクエストURL: {}", req.getRequestURL());
		
		// 1. クエリパラメータ 'SAMLart' からアーティファクトを取得
		Artifact artifact = buildArtifactFromRequest(req);
		logger.info("1. SAMLアーティファクトを受信しました: {}", artifact.getValue());
		
		// 2. アーティファクト解決リクエスト (ArtifactResolve) を構築
		ArtifactResolve artifactResolve = buildArtifactResolve(artifact);
		logger.info("2. ArtifactResolve リクエストを作成しました。");
		OpenSAMLUtils.logSAMLObject(artifactResolve);
		
		// 3. IdP へ SOAP 通信で ArtifactResolve を送信し、ArtifactResponse を取得（バックチャネル）
		logger.info("3. IdP ({}) へのバックチャネル通信を開始します...", IDPConstants.ARTIFACT_RESOLUTION_SERVICE);
		ArtifactResponse artifactResponse = sendAndReceiveArtifactResolve(artifactResolve, resp);
		logger.info("4. IdP から ArtifactResponse を受領しました。");
		OpenSAMLUtils.logSAMLObject(artifactResponse);

		// 4. メッセージの宛先 (Destination) と有効期限 (IssueInstant) のセキュリティ検証
		validateDestinationAndLifetime(artifactResponse, req);
		logger.info("5. メッセージの基本検証（宛先・有効期限）が完了しました。");

        // 5. アサーションの抽出と処理
		EncryptedAssertion encryptedAssertion = getEncryptedAssertion(artifactResponse);
		
		// 暗号化されているアサーションを復号
		Assertion assertion = decryptAssertion(encryptedAssertion);
		logger.info("6. アサーションの復号に成功しました。");

		// 署名の検証（IdP の正真性の確認）
		verifyAssertionSignature(assertion);
		logger.info("7. アサーションの署名検証に成功しました。");
		OpenSAMLUtils.logSAMLObject(assertion);

		// 6. ユーザー情報のログ出力（属性、認証時刻、認証方式など）
		logAssertionAttributes(assertion);
		logAuthenticationInstant(assertion);
		logAuthenticationMethod(assertion);

		// 7. セッションを認証済みに更新
		setAuthenticatedSession(req);
		logger.info("8. 認証が完了しました。セッションを更新し、元のURLへ戻ります。");
		
		// 8. 認証開始前に保存していた元の URL へリダイレクト
		redirectToGotoURL(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		// HTTP POST 経由で受け取った場合も同様に処理
		doGet(req, resp);
	}

	/**
	 * メッセージが自分宛であること、および発行から時間が経ちすぎていないことを検証します。
	 */
	private void validateDestinationAndLifetime(ArtifactResponse artifactResponse, HttpServletRequest request) {
		MessageContext context = new MessageContext();
		context.setMessage(artifactResponse);

		SAMLMessageInfoContext messageInfoContext = context.getSubcontext(SAMLMessageInfoContext.class, true);
		messageInfoContext.setMessageIssueInstant(artifactResponse.getIssueInstant());

		// メッセージ有効期限のチェック（発行時刻から指定時間内であること）
		MessageLifetimeSecurityHandler lifetimeSecurityHandler = new MessageLifetimeSecurityHandler();
		lifetimeSecurityHandler.setClockSkew(Duration.ofMillis(1000)); // クロックのずれ許容
		lifetimeSecurityHandler.setMessageLifetime(Duration.ofMillis(2000)); // 有効期間
		lifetimeSecurityHandler.setRequiredRule(true);
		
		// 宛先 URL のチェック（Destination 属性がリクエスト URL と一致すること）
		ReceivedEndpointSecurityHandler receivedEndpointSecurityHandler = new ReceivedEndpointSecurityHandler();
		receivedEndpointSecurityHandler.setHttpServletRequestSupplier(() -> request);
		
		try {
			lifetimeSecurityHandler.initialize();
			receivedEndpointSecurityHandler.initialize();
			
			// ハンドラーチェーンとして実行
			BasicMessageHandlerChain handlerChain = new BasicMessageHandlerChain();
			List<MessageHandler> handlers = new ArrayList<>();
			handlers.add(lifetimeSecurityHandler);
			handlers.add(receivedEndpointSecurityHandler);
			handlerChain.setHandlers(handlers);
			
			handlerChain.initialize();
			handlerChain.doInvoke(context);
		} catch (ComponentInitializationException | MessageHandlerException e) {
			throw new RuntimeException("メッセージのセキュリティ検証に失敗しました", e);
		}
	}

	/**
	 * SP の秘密鍵を使用して、暗号化されたアサーションを復号します。
	 */
	private Assertion decryptAssertion(EncryptedAssertion encryptedAssertion) {
		StaticKeyInfoCredentialResolver keyInfoCredentialResolver = new StaticKeyInfoCredentialResolver(
				SPCredentials.getCredential());

		// 復号機の設定
		Decrypter decrypter = new Decrypter(null, keyInfoCredentialResolver, new InlineEncryptedKeyResolver());
		decrypter.setRootInNewDocument(true);

		try {
			return decrypter.decrypt(encryptedAssertion);
		} catch (DecryptionException e) {
			throw new RuntimeException("アサーションの復号に失敗しました", e);
		}
	}

	/**
	 * アサーションのデジタル署名を検証します。
	 */
	private void verifyAssertionSignature(Assertion assertion) {
		if (!assertion.isSigned()) {
			throw new RuntimeException("SAMLアサーションに署名がありません。セキュリティリスクがあります。");
		}

		try {
			// XML署名の構文とプロファイルが正しいか検証
			SAMLSignatureProfileValidator profileValidator = new SAMLSignatureProfileValidator();
			profileValidator.validate(assertion.getSignature());

			// IdP の公開鍵を使用して署名を数学的に検証
			SignatureValidator.validate(assertion.getSignature(), IDPCredentials.getCredential());
		} catch (SignatureException e) {
			throw new RuntimeException("アサーションの署名検証に失敗しました。メッセージが改ざんされている可能性があります。", e);
		}
	}

	private void setAuthenticatedSession(HttpServletRequest req) {
		req.getSession().setAttribute(SPConstants.AUTHENTICATED_SESSION_ATTRIBUTE, true);
	}

	/**
	 * 認証フロー開始時に保存した元の URL へユーザーを引き戻します。
	 */
	private void redirectToGotoURL(HttpServletRequest req, HttpServletResponse resp) {
		String gotoURL = (String) req.getSession().getAttribute(SPConstants.GOTO_URL_SESSION_ATTRIBUTE);
		if (gotoURL == null) {
			logger.warn("セッションに戻り先URLが見つかりません。デフォルトページを表示します。");
			gotoURL = req.getContextPath() + "/app/appservlet";
		}
		try {
			resp.sendRedirect(gotoURL);
		} catch (IOException e) {
			throw new RuntimeException("リダイレクト中にエラーが発生しました", e);
		}
	}

	private void logAuthenticationMethod(Assertion assertion) {
		logger.info("認証方式: {}", 
				assertion.getAuthnStatements().get(0).getAuthnContext().getAuthnContextClassRef().getURI());
	}

	private void logAuthenticationInstant(Assertion assertion) {
		logger.info("認証時刻: {}", assertion.getAuthnStatements().get(0).getAuthnInstant());
	}

	private void logAssertionAttributes(Assertion assertion) {
		if (assertion.getAttributeStatements().isEmpty()) return;
		
		logger.info("抽出されたユーザー属性:");
		for (Attribute attribute : assertion.getAttributeStatements().get(0).getAttributes()) {
			for (XMLObject attributeValue : attribute.getAttributeValues()) {
				logger.info("  - {}: {}", attribute.getName(), ((XSString) attributeValue).getValue());
			}
		}
	}

	private EncryptedAssertion getEncryptedAssertion(ArtifactResponse artifactResponse) {
		Response response = (Response) artifactResponse.getMessage();
		return response.getEncryptedAssertions().get(0);
	}

	/**
	 * SOAP クライアントを使用して ArtifactResolve リクエストを IdP へ送信し、同期的にレスポンスを待機します。
	 * ここで SP と IdP 間の「バックチャネル」通信が発生します。
	 */
	private ArtifactResponse sendAndReceiveArtifactResolve(final ArtifactResolve artifactResolve,
			HttpServletResponse servletResponse) {
		try {
			// 送信用コンテキストの構築
			MessageContext contextout = new MessageContext();
			contextout.setMessage(artifactResolve);

			// ArtifactResolve リクエストへの署名設定
			SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
			signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
			signatureSigningParameters.setSigningCredential(SPCredentials.getCredential());
			signatureSigningParameters.setSignatureCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

			contextout.getSubcontext(SecurityParametersContext.class, true)
				.setSignatureSigningParameters(signatureSigningParameters);

			// 操作全体のコンテキスト（リクエスト/レスポンスペア）
			InOutOperationContext context = new ProfileRequestContext();
			context.setOutboundMessageContext(contextout);

			// SOAP 1.1 パイプラインを定義するクライアントの構築
			AbstractPipelineHttpSOAPClient soapClient = new AbstractPipelineHttpSOAPClient() {
				protected HttpClientMessagePipeline newPipeline() throws SOAPException {
					HttpClientRequestSOAP11Encoder encoder = new HttpClientRequestSOAP11Encoder();
					HttpClientResponseSOAP11Decoder decoder = new HttpClientResponseSOAP11Decoder();
					BasicHttpClientMessagePipeline pipeline = new BasicHttpClientMessagePipeline(encoder, decoder);

					// 送信時に署名を付与するためのハンドラーを追加
					SAMLOutboundProtocolMessageSigningHandler signingHandler = new SAMLOutboundProtocolMessageSigningHandler();
					try {
						signingHandler.initialize();
					} catch (ComponentInitializationException e) {
						throw new SOAPException(e);
					}
					pipeline.setOutboundPayloadHandler(signingHandler);
					return pipeline;
				}
			};

			// HTTP クライアントの準備と初期化
			HttpClientBuilder clientBuilder = new HttpClientBuilder();
			soapClient.setHttpClient(clientBuilder.buildClient());
			soapClient.initialize();
			
			// SOAP リクエストを IdP の Artifact Resolution Service エンドポイントに送信
			soapClient.send(IDPConstants.ARTIFACT_RESOLUTION_SERVICE, context);

			// IdP からの SOAP レスポンス内の SAML メッセージ (ArtifactResponse) を抽出
			return (ArtifactResponse) context.getInboundMessageContext().getMessage();
		} catch (Exception e) {
			throw new RuntimeException("バックチャネル SOAP 通信中にエラーが発生しました", e);
		}
	}

	private Artifact buildArtifactFromRequest(final HttpServletRequest req) {
		Artifact artifact = OpenSAMLUtils.buildSAMLObject(Artifact.class);
		// URL パラメータ ?SAMLart=... を取得
		artifact.setValue(req.getParameter("SAMLart"));
		return artifact;
	}

	/**
	 * 受信した特定のアーティファクトを引き換えるための解決リクエストを構築します。
	 */
	private ArtifactResolve buildArtifactResolve(final Artifact artifact) {
		ArtifactResolve artifactResolve = OpenSAMLUtils.buildSAMLObject(ArtifactResolve.class);

		// 発行者情報のセット
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(SPConstants.SP_ENTITY_ID);
		artifactResolve.setIssuer(issuer);

		artifactResolve.setIssueInstant(Instant.now());
		artifactResolve.setID(OpenSAMLUtils.generateSecureRandomId());
		artifactResolve.setDestination(IDPConstants.ARTIFACT_RESOLUTION_SERVICE);
		
		// 解決対象のアーティファクトをセット
		artifactResolve.setArtifact(artifact);

		return artifactResolve;
	}
}
