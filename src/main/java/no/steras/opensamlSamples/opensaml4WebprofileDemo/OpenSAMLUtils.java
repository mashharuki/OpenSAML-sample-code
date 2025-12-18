package no.steras.opensamlSamples.opensaml4WebprofileDemo;

import javax.xml.namespace.QName;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.common.SignableSAMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import net.shibboleth.utilities.java.support.security.impl.RandomIdentifierGenerationStrategy;
import net.shibboleth.utilities.java.support.xml.SerializeSupport;

/**
 * Created by Privat on 4/6/14.
 */
public class OpenSAMLUtils {
	// ロガーインスタンス
	private static Logger logger = LoggerFactory.getLogger(OpenSAMLUtils.class);
	private static RandomIdentifierGenerationStrategy secureRandomIdGenerator;

	// 静的初期化子
	static {
		// セキュアランダムIDジェネレータの初期化
		secureRandomIdGenerator = new RandomIdentifierGenerationStrategy();

	}

	/**
	 * ユーティリティメソッド SAMLオブジェクトの生成
	 *
	 * @param clazz SAMLオブジェクトのクラス
	 * @param <T>   SAMLオブジェクトの型
	 * @return 生成されたSAMLオブジェクト
	 */
	public static <T> T buildSAMLObject(final Class<T> clazz) {
		T object = null;
		try {
			// SAMLオブジェクトのビルダーファクトリを取得
			XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
			// デフォルトの要素名を取得し、SAMLオブジェクトを生成
			QName defaultElementName = (QName) clazz.getDeclaredField("DEFAULT_ELEMENT_NAME").get(null);
			// SAMLオブジェクトのビルダーを使用してオブジェクトを構築
			object = (T) builderFactory.getBuilder(defaultElementName).buildObject(defaultElementName);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Could not create SAML object");
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("Could not create SAML object");
		}

		return object;
	}

	/**
	 * セキュアランダムIDの生成
	 *
	 * @return 生成されたセキュアランダムID
	 */
	public static String generateSecureRandomId() {
		return secureRandomIdGenerator.generateIdentifier();
	}

	/**
	 * SAMLオブジェクトのログ出力
	 *
	 * @param object ログ出力するSAMLオブジェクト
	 */
	public static void logSAMLObject(final XMLObject object) {
		Element element = null;

		if (object instanceof SignableSAMLObject && ((SignableSAMLObject) object).isSigned()
				&& object.getDOM() != null) {
			element = object.getDOM();
		} else {
			try {
				// SAMLオブジェクトのマーシャラーを取得し、DOM要素に変換
				Marshaller out = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(object);
				// マーシャリングを実行
				out.marshall(object);
				// DOM要素を取得
				element = object.getDOM();

			} catch (MarshallingException e) {
				logger.error(e.getMessage(), e);
			}
		}
		// DOM要素を整形されたXML文字列に変換
		String xmlString = SerializeSupport.prettyPrintXML(element);

		logger.info(xmlString);

	}
}
