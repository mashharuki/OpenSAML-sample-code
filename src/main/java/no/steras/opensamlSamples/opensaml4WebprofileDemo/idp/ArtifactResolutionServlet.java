package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.OpenSAMLUtils;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.SPConstants;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.SPCredentials;

/**
 * アーティファクト解決用サーブレット
 */
public class ArtifactResolutionServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(ArtifactResolutionServlet.class);

	/**
	 * POSTリクエストの処理
	 */
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("recieved artifactResolve:");
		// アーティファクト解決リクエストのデコード
		HTTPSOAP11Decoder decoder = new HTTPSOAP11Decoder();
	    // リクエストをセット
		decoder.setHttpServletRequest(req);

		try {
			BasicParserPool parserPool = new BasicParserPool();
			parserPool.initialize();
			decoder.setParserPool(parserPool);
			decoder.initialize();
			decoder.decode();
		} catch (MessageDecodingException e) {
			throw new RuntimeException(e);
		} catch (ComponentInitializationException e) {
			throw new RuntimeException(e);
		}

		ArtifactResponse artifactResponse = buildArtifactResponse();

		MessageContext context = new MessageContext();
		context.setMessage(artifactResponse);
		// アーティファクト解決レスポンスのエンコードと送信
		HTTPSOAP11Encoder encoder = new HTTPSOAP11Encoder();
		encoder.setMessageContext(context);
		encoder.setHttpServletResponse(resp);

		try {
			// エンコーダー初期化
			encoder.prepareContext();
			encoder.initialize();
			encoder.encode();
		} catch (MessageEncodingException e) {
			throw new RuntimeException(e);
		} catch (ComponentInitializationException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * アーティファクトレスポンスの構築
	 * 
	 * @return アーティファクトレスポンス
	 */
	private ArtifactResponse buildArtifactResponse() {
		// アーティファクトレスポンスの構築
		ArtifactResponse artifactResponse = OpenSAMLUtils.buildSAMLObject(ArtifactResponse.class);
		// 共通属性の設定
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(IDPConstants.IDP_ENTITY_ID);
		artifactResponse.setIssuer(issuer);
		artifactResponse.setIssueInstant(Instant.now());
		artifactResponse.setDestination(SPConstants.ASSERTION_CONSUMER_SERVICE);
		// IDを設定
		artifactResponse.setID(OpenSAMLUtils.generateSecureRandomId());
		// ステータスの設定
		Status status = OpenSAMLUtils.buildSAMLObject(Status.class);
		StatusCode statusCode = OpenSAMLUtils.buildSAMLObject(StatusCode.class);
		statusCode.setValue(StatusCode.SUCCESS);
		status.setStatusCode(statusCode);
		artifactResponse.setStatus(status);
		// レスポンスメッセージの構築(SAMLオブジェクトを作成)
		Response response = OpenSAMLUtils.buildSAMLObject(Response.class);
		response.setDestination(SPConstants.ASSERTION_CONSUMER_SERVICE);
		response.setIssueInstant(Instant.now());
		response.setID(OpenSAMLUtils.generateSecureRandomId());
		Issuer issuer2 = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer2.setValue(IDPConstants.IDP_ENTITY_ID);

		response.setIssuer(issuer2);

		Status status2 = OpenSAMLUtils.buildSAMLObject(Status.class);
		StatusCode statusCode2 = OpenSAMLUtils.buildSAMLObject(StatusCode.class);
		statusCode2.setValue(StatusCode.SUCCESS);
		status2.setStatusCode(statusCode2);

		response.setStatus(status2);

		artifactResponse.setMessage(response);
		// アサーションの構築
		Assertion assertion = buildAssertion();
		// アサーションに署名と暗号化を実施
		signAssertion(assertion);
		EncryptedAssertion encryptedAssertion = encryptAssertion(assertion);

		response.getEncryptedAssertions().add(encryptedAssertion);
		return artifactResponse;
	}

	/**
	 * アサーションの暗号化
	 */
	private EncryptedAssertion encryptAssertion(Assertion assertion) {
		// アサーションの暗号化
		DataEncryptionParameters encryptionParameters = new DataEncryptionParameters();
		// アルゴリズムを設定
		encryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);

		KeyEncryptionParameters keyEncryptionParameters = new KeyEncryptionParameters();
		keyEncryptionParameters.setEncryptionCredential(SPCredentials.getCredential());
		keyEncryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);
		// Encrypterの生成
		Encrypter encrypter = new Encrypter(encryptionParameters, keyEncryptionParameters);
		encrypter.setKeyPlacement(Encrypter.KeyPlacement.INLINE);

		try {
			// 暗号化の実行
			EncryptedAssertion encryptedAssertion = encrypter.encrypt(assertion);
			return encryptedAssertion;
		} catch (EncryptionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * アサーションへの署名付与
	 */
	private void signAssertion(Assertion assertion) {
		// 署名オブジェクトの生成
		Signature signature = OpenSAMLUtils.buildSAMLObject(Signature.class);
		signature.setSigningCredential(IDPCredentials.getCredential());
		signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
		signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

		assertion.setSignature(signature);

		try {
			// 署名対象オブジェクトのマーシャリング
			XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);
		} catch (MarshallingException e) {
			throw new RuntimeException(e);
		}

		try {
			// 署名の実行
			Signer.signObject(signature);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * アサーションの構築
	 * 
	 * @return アサーション
	 */
	private Assertion buildAssertion() {
		// アサーションの構築
		Assertion assertion = OpenSAMLUtils.buildSAMLObject(Assertion.class);
		// 共通属性の設定
		Issuer issuer = OpenSAMLUtils.buildSAMLObject(Issuer.class);
		issuer.setValue(IDPConstants.IDP_ENTITY_ID);
		assertion.setIssuer(issuer);
		assertion.setIssueInstant(Instant.now());

		assertion.setID(OpenSAMLUtils.generateSecureRandomId());

		Subject subject = OpenSAMLUtils.buildSAMLObject(Subject.class);
		assertion.setSubject(subject);
		// NameIDの設定
		NameID nameID = OpenSAMLUtils.buildSAMLObject(NameID.class);
		// NameIDの各種属性を設定
		nameID.setFormat(NameIDType.TRANSIENT);
		nameID.setValue("Some NameID value");
		nameID.setSPNameQualifier("SP name qualifier");
		nameID.setNameQualifier("Name qualifier");

		subject.setNameID(nameID);

		subject.getSubjectConfirmations().add(buildSubjectConfirmation());
		// アサーションの各種属性設定
		assertion.setConditions(buildConditions());
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
		subjectConfirmationData.setInResponseTo("Made up ID");
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
		audience.setURI(SPConstants.ASSERTION_CONSUMER_SERVICE);
		audienceRestriction.getAudiences().add(audience);
		conditions.getAudienceRestrictions().add(audienceRestriction);
		return conditions;
	}

	/**
	 * AttributeStatementの構築
	 */
	private AttributeStatement buildAttributeStatement() {
		AttributeStatement attributeStatement = OpenSAMLUtils.buildSAMLObject(AttributeStatement.class);

		Attribute attributeUserName = OpenSAMLUtils.buildSAMLObject(Attribute.class);

		XSStringBuilder stringBuilder = (XSStringBuilder) XMLObjectProviderRegistrySupport.getBuilderFactory()
				.getBuilder(XSString.TYPE_NAME);
		XSString userNameValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		userNameValue.setValue("bob");

		attributeUserName.getAttributeValues().add(userNameValue);
		attributeUserName.setName("username");
		attributeStatement.getAttributes().add(attributeUserName);

		Attribute attributeLevel = OpenSAMLUtils.buildSAMLObject(Attribute.class);
		XSString levelValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		levelValue.setValue("999999999");

		attributeLevel.getAttributeValues().add(levelValue);
		attributeLevel.setName("telephone");
		attributeStatement.getAttributes().add(attributeLevel);

		return attributeStatement;

	}
}
