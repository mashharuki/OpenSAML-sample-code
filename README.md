# OpenSAML 4を使用したSPとIdPの連携デモ

このコードは、SAMLを使用してSPとIdPがどのように通信するかをシミュレートするものです。

このデモのバリエーションは、[A Guide to OpenSAML V2](https://payhip.com/b/odEY?utm_source=github&utm_medium=link&utm_campaign=opensaml_samples_collection&utm_content=getting-started-authn)および[A Guide to OpenSAML V3](https://payhip.com/b/41Tw?utm_source=github&utm_medium=link&utm_campaign=opensaml_samples_collection&utm_content=getting-started-authn)の書籍で広く使用されています。

このデモコードは、SP、IdP、およびビジネスアプリケーション上の異なるエンドポイントとして機能する複数のサーブレットを設定します。

このデモは、OpenSAMLの優れた概要ガイドとして機能し、以下を実証します：

* HTTP Redirectバインディングを使用したAuthnRequestの構築、署名、送信
* AuthnRequestの署名の解析と検証
* Artifactバインディングを使用したResponseメッセージの送信
* Assertionの暗号化と復号化

## コードのビルド

```bash
mvn clean package
```

## コードの実行

コードをクローンし、このサンプルのフォルダに移動して、次のコマンドを実行します：

### Spring Boot で実行（推奨）

```bash
mvn spring-boot:run
```

または、JARファイルを直接実行：

```bash
java -jar target/opensaml5-webprofile-demo-1.0-SNAPSHOT.jar
```

その後、ブラウザを開いて http://127.0.0.1:8080/opensaml5-webprofile-demo/app/appservlet にアクセスしてください。

通信中に使用されるすべてのSAMLメッセージがコンソールにログ出力されます。

## 技術スタック

- **Java 21**
- **Spring Boot 3.3.0**
- **OpenSAML 5.1.6**
- **Jakarta EE 10**

## プロジェクト構成

```
src/main/java/no/steras/opensamlSamples/opensaml4WebprofileDemo/
├── Application.java              # Spring Boot 起動クラス
├── OpenSAMLUtils.java            # SAML ユーティリティ
├── config/
│   └── WebConfig.java            # Servlet/Filter 登録設定
├── app/
│   └── ApplicationServlet.java   # 保護リソース
├── idp/
│   ├── IDPConstants.java
│   ├── IDPCredentials.java
│   ├── SingleSignOnServlet.java  # SSO エンドポイント
│   └── ArtifactResolutionServlet.java
└── sp/
    ├── SPConstants.java
    ├── SPCredentials.java
    ├── AccessFilter.java         # 認証フィルター
    └── ConsumerServlet.java      # ACS エンドポイント
```

## エンドポイント

| パス | 説明 |
|------|------|
| `/app/appservlet` | 保護されたリソース（認証が必要） |
| `/idp/singleSignOnService` | IdP シングルサインオンエンドポイント |
| `/sp/consumer` | SP Assertion Consumer Service |
| `/idp/artifactResolutionService` | Artifact 解決エンドポイント |

## 参考文献
- [DeepWiki](https://deepwiki.com/rasmusson/OpenSAML-sample-code)