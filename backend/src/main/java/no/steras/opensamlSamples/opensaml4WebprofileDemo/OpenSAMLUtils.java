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

import net.shibboleth.shared.security.impl.RandomIdentifierGenerationStrategy;
import net.shibboleth.shared.xml.SerializeSupport;

/**
 * OpenSAMLの共通操作をカプセル化するユーティリティクラス。
 * SAMLオブジェクトの構築、ID生成、およびデバッグ用のログ出力機能を提供します。
 */
public class OpenSAMLUtils {
	// ロガーインスタンス
	private static Logger logger = LoggerFactory.getLogger(OpenSAMLUtils.class);
	
	// セキュアな乱数に基づいたID生成戦略。SAMLメッセージ（AuthnRequest, Assertion等）のIDに使用します。
	private static RandomIdentifierGenerationStrategy secureRandomIdGenerator;

	// 静적初期化子
	static {
		// IDジェネレータの初期化（デフォルトの16バイトのセキュアランダム値を生成）
		secureRandomIdGenerator = new RandomIdentifierGenerationStrategy();
	}

	/**
	 * 指定されたクラス型に対応するSAMLオブジェクトを生成します。
	 * OpenSAMLではオブジェクトの生成にBuilderFactoryを使用する必要があります。
	 *
	 * @param clazz 生成したいSAMLオブジェクトのインターフェースクラス（例: AuthnRequest.class）
	 * @param <T>   SAMLオブジェクトの型
	 * @return 生成・初期化されたSAMLオブジェクト
	 * @throws IllegalArgumentException オブジェクトの生成に失敗した場合
	 */
	public static <T> T buildSAMLObject(final Class<T> clazz) {
		T object = null;
		try {
			// OpenSAMLのレジストリからビルダーファクトリを取得
			XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
			
			// 渡されたクラスから定数 DEFAULT_ELEMENT_NAME (XMLのタグ名等を表すQName) を取得
			QName defaultElementName = (QName) clazz.getDeclaredField("DEFAULT_ELEMENT_NAME").get(null);
			
			// ビルダーを取得してオブジェクトを構築
			object = (T) builderFactory.getBuilder(defaultElementName).buildObject(defaultElementName);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("SAMLオブジェクトの生成に失敗しました: アクセス権限エラー", e);
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("SAMLオブジェクトの生成に失敗しました: DEFAULT_ELEMENT_NAME が見つかりません", e);
		}

		return object;
	}

	/**
	 * SAMLメッセージの識別子として使用するためのセキュアなランダムIDを生成します。
	 * SAML仕様に基づき、IDは通常文字で始まる必要があります（ジェネレータがこれを保証）。
	 *
	 * @return 生成された識別子文字列
	 */
	public static String generateSecureRandomId() {
		return secureRandomIdGenerator.generateIdentifier();
	}

	/**
	 * SAMLオブジェクトをXML形式で整形してログに出力します。
	 * メッセージの送受信内容をデバッグ・確認するために使用します。
	 *
	 * @param object ログ出力したいSAMLオブジェクト
	 */
	public static void logSAMLObject(final XMLObject object) {
		Element element = null;

		// オブジェクトが署名済みで既にDOMが構築されている場合はそれを使用
		if (object instanceof SignableSAMLObject && ((SignableSAMLObject) object).isSigned()
				&& object.getDOM() != null) {
			element = object.getDOM();
		} else {
			try {
				// オブジェクトをXML(DOM)に変換（マーシャリング）
				Marshaller out = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(object);
				out.marshall(object);
				element = object.getDOM();

			} catch (MarshallingException e) {
				logger.error("SAMLオブジェクトのマーシャリング中にエラーが発生しました: " + e.getMessage(), e);
			}
		}
		
		// DOMをインデント付きの読みやすいXML文字列に変換
		String xmlString = SerializeSupport.prettyPrintXML(element);

		logger.info("SAMLメッセージ内容:\n{}", xmlString);
	}
}
