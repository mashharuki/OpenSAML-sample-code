package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.xml.security.utils.EncryptionConstants;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.core.xml.schema.impl.XSStringBuilder;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.decoder.MessageDecodingException;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.saml.saml2.binding.decoding.impl.HTTPSOAP11Decoder;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPSOAP11Encoder;
import org.opensaml.saml.saml2.core.ArtifactResponse;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.Audience;
import org.opensaml.saml.saml2.core.AudienceRestriction;
import org.opensaml.saml.saml2.core.AuthnContext;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.EncryptedAssertion;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.NameIDType;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.Status;
import org.opensaml.saml.saml2.core.StatusCode;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml.saml2.encryption.Encrypter;
import org.opensaml.xmlsec.encryption.support.DataEncryptionParameters;
import org.opensaml.xmlsec.encryption.support.EncryptionException;
import org.opensaml.xmlsec.encryption.support.KeyEncryptionParameters;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.shared.component.ComponentInitializationException;
import net.shibboleth.shared.xml.impl.BasicParserPool;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.OpenSAMLUtils;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.SPConstants;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.SPCredentials;

/**
 * Identity Provider (IdP) 側の Artifact Resolution Service (ARS) エンドポイント。
 * 
 * 役割:
 * 1. SP からバックチャネル（SOAP通信）で送られてくる `ArtifactResolve` リクエストを受信。
 * 2. 送信されたアーティファクトに対応するアサーションを特定。
 * 3. アサーション（署名・暗号化済み）をカプセル化した `ArtifactResponse` を SOAP で返信。
 */
public class ArtifactResolutionServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(ArtifactResolutionServlet.class);

	/**
	 * SP からの SOAP POST リクエストを処理します。
	 */
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("ArtifactResolutionServlet: SP から ArtifactResolve リクエスト（バックチャネル）を受信しました。");
		
		// 1. SOAP 1.1 デコーダーを使用して SAML メッセージを抽出
		HTTPSOAP11Decoder decoder = new HTTPSOAP11Decoder();
		decoder.setHttpServletRequestSupplier(() -> req);

		try {
			BasicParserPool parserPool = new BasicParserPool();
			parserPool.initialize();
			decoder.setParserPool(parserPool);
			decoder.initialize();
			decoder.decode();
			logger.info("ArtifactResolutionServlet: リクエスト(ArtifactResolve) のデコードに成功しました。");
		} catch (MessageDecodingException | ComponentInitializationException e) {
			throw new RuntimeException("SOAP メッセージのデコードに失敗しました", e);
		}

		// 2. 実際のアサーションを含むレスポンスを構築
		logger.info("ArtifactResolutionServlet: アサーションを生成し ArtifactResponse を構築します...");
		ArtifactResponse artifactResponse = buildArtifactResponse();

		// 3. SOAP 1.1 エンコーダーを使用してレスポンスを送信
		MessageContext context = new MessageContext();
		context.setMessage(artifactResponse);
		HTTPSOAP11Encoder encoder = new HTTPSOAP11Encoder();
		encoder.setHttpServletResponseSupplier(() -> resp);
		encoder.setMessageContext(context);

		try {
			encoder.prepareContext();
			encoder.initialize();
			encoder.encode();
			logger.info("ArtifactResolutionServlet: ArtifactResponse を SP に返送しました。");
		} catch (MessageEncodingException | ComponentInitializationException e) {
			throw new RuntimeException("SOAP レスポンスのエンコードに失敗しました", e);
		}
	}

	/**
	 * SAML レスポンスの階層構造を構築します。
	 * ArtifactResponse -> Response -> EncryptedAssertion という構造になります。
	 */
	private ArtifactResponse buildArtifactResponse() {
		// ArtifactResponse の構築（外側のコンテナ）
		ArtifactResponse artifactResponse = OpenSAMLUtils.buildSAMLObject(ArtifactResponse.class);
		
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(IDPConstants.IDP_ENTITY_ID);
		artifactResponse.setIssuer(issuer);
		artifactResponse.setIssueInstant(Instant.now());
		artifactResponse.setDestination(SPConstants.ASSERTION_CONSUMER_SERVICE);
		artifactResponse.setID(OpenSAMLUtils.generateSecureRandomId());
		
		// 成功ステータスの設定
		Status status = OpenSAMLUtils.buildSAMLObject(Status.class);
		StatusCode statusCode = OpenSAMLUtils.buildSAMLObject(StatusCode.class);
		statusCode.setValue(StatusCode.SUCCESS);
		status.setStatusCode(statusCode);
		artifactResponse.setStatus(status);

		// Response の構築（実際にアサーションを運ぶメッセージ）
		Response response = OpenSAMLUtils.buildSAMLObject(Response.class);
		response.setDestination(SPConstants.ASSERTION_CONSUMER_SERVICE);
		response.setIssueInstant(Instant.now());
		response.setID(OpenSAMLUtils.generateSecureRandomId());
		
		Issuer resIssuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		resIssuer.setValue(IDPConstants.IDP_ENTITY_ID);
		response.setIssuer(resIssuer);

		Status resStatus = OpenSAMLUtils.buildSAMLObject(Status.class);
		StatusCode resStatusCode = OpenSAMLUtils.buildSAMLObject(StatusCode.class);
		resStatusCode.setValue(StatusCode.SUCCESS);
		resStatus.setStatusCode(resStatusCode);
		response.setStatus(resStatus);

		artifactResponse.setMessage(response);

		// アサーションの生成、署名、および暗号化
		Assertion assertion = buildAssertion();
		signAssertion(assertion); // 1. 署名
		EncryptedAssertion encryptedAssertion = encryptAssertion(assertion); // 2. 暗号化

		response.getEncryptedAssertions().add(encryptedAssertion);
		
		return artifactResponse;
	}

	/**
	 * SP の公開鍵を使用してアサーションを完全に暗号化します。
	 */
	private EncryptedAssertion encryptAssertion(Assertion assertion) {
		// データ暗号化(AES-128)のパラメータ
		DataEncryptionParameters encryptionParameters = new DataEncryptionParameters();
		encryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);

		// 鍵暗号化(RSA-OAEP)のパラメータ（SP の公開鍵を使用）
		KeyEncryptionParameters keyEncryptionParameters = new KeyEncryptionParameters();
		keyEncryptionParameters.setEncryptionCredential(SPCredentials.getCredential());
		keyEncryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);

		Encrypter encrypter = new Encrypter(encryptionParameters, keyEncryptionParameters);
		encrypter.setKeyPlacement(Encrypter.KeyPlacement.INLINE);

		try {
			return encrypter.encrypt(assertion);
		} catch (EncryptionException e) {
			throw new RuntimeException("アサーションの暗号化に失敗しました", e);
		}
	}

	/**
	 * IdP の秘密鍵を使用してアサーションにデジタル署名を付与します。
	 */
	private void signAssertion(Assertion assertion) {
		Signature signature = OpenSAMLUtils.buildSAMLObject(Signature.class);
		signature.setSigningCredential(IDPCredentials.getCredential());
		signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
		signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

		assertion.setSignature(signature);

		try {
			// 署名の前に DOM 要素に変換（マーシャリング）が必要
			XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);
			Signer.signObject(signature);
		} catch (MarshallingException | SignatureException e) {
			throw new RuntimeException("アサーションの署名に失敗しました", e);
		}
	}

	/**
	 * ユーザーの認証情報や属性を含むアサーションを詳細に構築します。
	 */
	private Assertion buildAssertion() {
		Assertion assertion = OpenSAMLUtils.buildSAMLObject(Assertion.class);
		
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(IDPConstants.IDP_ENTITY_ID);
		assertion.setIssuer(issuer);
		assertion.setIssueInstant(Instant.now());
		assertion.setID(OpenSAMLUtils.generateSecureRandomId());

		// 認証対象(Subject)の設定
		Subject subject = OpenSAMLUtils.buildSAMLObject(Subject.class);
		assertion.setSubject(subject);
		
		// 名前 ID (NameID) の設定。ここではダミー値を設定。
		NameID nameID = OpenSAMLUtils.buildSAMLObject(NameID.class);
		nameID.setFormat(NameIDType.TRANSIENT);
		nameID.setValue("bob-at-idp-demo");
		nameID.setSPNameQualifier(SPConstants.SP_ENTITY_ID);
		nameID.setNameQualifier(IDPConstants.IDP_ENTITY_ID);
		subject.setNameID(nameID);

		// 認証の有効性(SubjectConfirmation)を設定
		subject.getSubjectConfirmations().add(buildSubjectConfirmation());
		
		// 有効期限や対象範囲(Conditions)の設定
		assertion.setConditions(buildConditions());
		
		// ユーザー属性(AttributeStatement)と認証情報(AuthnStatement)の設定
		assertion.getAttributeStatements().add(buildAttributeStatement());
		assertion.getAuthnStatements().add(buildAuthnStatement());

		return assertion;
	}

	/**
	 * SubjectConfirmationの構築
	 */
	private SubjectConfirmation buildSubjectConfirmation() {
		SubjectConfirmation subjectConfirmation = OpenSAMLUtils.buildSAMLObject(SubjectConfirmation.class);
		subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);

		SubjectConfirmationData subjectConfirmationData = OpenSAMLUtils.buildSAMLObject(SubjectConfirmationData.class);
		subjectConfirmationData.setInResponseTo("AuthnRequest-ID-Check-Skipped-In-Demo");
		subjectConfirmationData.setInResponseTo(null); // デモ用
		subjectConfirmationData.setNotBefore(Instant.now());
		subjectConfirmationData.setNotOnOrAfter(Instant.now().plus(10, ChronoUnit.MINUTES));
		subjectConfirmationData.setRecipient(SPConstants.ASSERTION_CONSUMER_SERVICE);

		subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);

		return subjectConfirmation;
	}

	/**
	 * AuthnStatementの構築
	 */
	private AuthnStatement buildAuthnStatement() {
		AuthnStatement authnStatement = OpenSAMLUtils.buildSAMLObject(AuthnStatement.class);
		AuthnContext authnContext = OpenSAMLUtils.buildSAMLObject(AuthnContext.class);
		AuthnContextClassRef authnContextClassRef = OpenSAMLUtils.buildSAMLObject(AuthnContextClassRef.class);
		
		// スマートカード認証相当の結果として設定
		authnContextClassRef.setURI(AuthnContext.SMARTCARD_AUTHN_CTX);
		authnContext.setAuthnContextClassRef(authnContextClassRef);
		authnStatement.setAuthnContext(authnContext);
		authnStatement.setAuthnInstant(Instant.now());

		return authnStatement;
	}

	/**
	 * Conditionsの構築
	 */
	private Conditions buildConditions() {
		Conditions conditions = OpenSAMLUtils.buildSAMLObject(Conditions.class);
		conditions.setNotBefore(Instant.now());
		conditions.setNotOnOrAfter(Instant.now().plus(10, ChronoUnit.MINUTES));
		
		AudienceRestriction audienceRestriction = OpenSAMLUtils.buildSAMLObject(AudienceRestriction.class);
		Audience audience = OpenSAMLUtils.buildSAMLObject(Audience.class);
		audience.setURI(SPConstants.SP_ENTITY_ID);
		audienceRestriction.getAudiences().add(audience);
		conditions.getAudienceRestrictions().add(audienceRestriction);
		
		return conditions;
	}

	/**
	 * ユーザーの属性（ユーザー名、役割など）を構築します。
	 */
	private AttributeStatement buildAttributeStatement() {
		AttributeStatement attributeStatement = OpenSAMLUtils.buildSAMLObject(AttributeStatement.class);

		// ユーザー名属性の追加
		Attribute attributeUserName = OpenSAMLUtils.buildSAMLObject(Attribute.class);
		XSStringBuilder stringBuilder = (XSStringBuilder) XMLObjectProviderRegistrySupport.getBuilderFactory()
				.getBuilder(XSString.TYPE_NAME);
		XSString userNameValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		userNameValue.setValue("bob");
		attributeUserName.getAttributeValues().add(userNameValue);
		attributeUserName.setName("username");
		attributeStatement.getAttributes().add(attributeUserName);

		// 役割や追加情報の追加
		Attribute attributeRole = OpenSAMLUtils.buildSAMLObject(Attribute.class);
		XSString roleValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		roleValue.setValue("Administrator");
		attributeRole.getAttributeValues().add(roleValue);
		attributeRole.setName("role");
		attributeStatement.getAttributes().add(attributeRole);

		return attributeStatement;
	}
}
