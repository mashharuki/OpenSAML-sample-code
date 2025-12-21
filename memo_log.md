# AWS上で動かしたみた時の記録

```bash
2025-12-21T06:08:33.680Z
INFO lambda_web_adapter: app is not ready after 2000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:35.683Z
INFO lambda_web_adapter: app is not ready after 4000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:37.691Z
INFO lambda_web_adapter: app is not ready after 6000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:39.372Z
. ____ _ __ _ _
2025-12-21T06:08:39.372Z
/\\ / ___'_ __ _ _(_)_ __ __ _ \ \ \ \
2025-12-21T06:08:39.372Z
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
2025-12-21T06:08:39.372Z
\\/ ___)| |_)| | | | | || (_| | ) ) ) )
2025-12-21T06:08:39.372Z
' |____| .__|_| |_|_| |_\__, | / / / /
2025-12-21T06:08:39.372Z
=========|_|==============|___/=/_/_/_/
2025-12-21T06:08:39.379Z
:: Spring Boot :: (v3.3.0)
2025-12-21T06:08:39.676Z
INFO lambda_web_adapter: app is not ready after 8000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:39.973Z
06:08:39.961 [main] INFO n.s.o.o.Application - Starting Application v1.0-SNAPSHOT using Java 21.0.9 with PID 12 (/app/app.jar started by sbx_user1051 in /app)
2025-12-21T06:08:39.978Z
06:08:39.978 [main] INFO n.s.o.o.Application - No active profile set, falling back to 1 default profile: "default"
2025-12-21T06:08:41.477Z
EXTENSION Name: lambda-adapter State: Ready Events: []
2025-12-21T06:08:41.479Z
START RequestId: 24a7b71b-01be-4b4d-baa4-a21c06e902f4 Version: $LATEST
2025-12-21T06:08:43.491Z
INFO Lambda runtime invoke{requestId="24a7b71b-01be-4b4d-baa4-a21c06e902f4" xrayTraceId="Root=1-69478edf-2bff254d74cf089970b605ff;Parent=7d1638a17cc8177e;Sampled=0;Lineage=1:ad95aa34:0"}: lambda_web_adapter: app is not ready after 2000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:45.491Z
INFO Lambda runtime invoke{requestId="24a7b71b-01be-4b4d-baa4-a21c06e902f4" xrayTraceId="Root=1-69478edf-2bff254d74cf089970b605ff;Parent=7d1638a17cc8177e;Sampled=0;Lineage=1:ad95aa34:0"}: lambda_web_adapter: app is not ready after 4000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:46.376Z
06:08:46.376 [main] INFO o.s.b.w.e.t.TomcatWebServer - Tomcat initialized with port 8080 (http)
2025-12-21T06:08:46.413Z
06:08:46.413 [main] INFO o.a.c.h.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-0.0.0.0-8080"]
2025-12-21T06:08:46.416Z
06:08:46.416 [main] INFO o.a.c.c.StandardService - Starting service [Tomcat]
2025-12-21T06:08:46.432Z
06:08:46.431 [main] INFO o.a.c.c.StandardEngine - Starting Servlet engine: [Apache Tomcat/10.1.24]
2025-12-21T06:08:46.574Z
06:08:46.573 [main] INFO o.a.c.c.C.[.[.[/opensaml5-webprofile-demo] - Initializing Spring embedded WebApplicationContext
2025-12-21T06:08:46.574Z
06:08:46.574 [main] INFO o.s.b.w.s.c.ServletWebServerApplicationContext - Root WebApplicationContext: initialization completed in 6414 ms
2025-12-21T06:08:47.491Z
INFO Lambda runtime invoke{requestId="24a7b71b-01be-4b4d-baa4-a21c06e902f4" xrayTraceId="Root=1-69478edf-2bff254d74cf089970b605ff;Parent=7d1638a17cc8177e;Sampled=0;Lineage=1:ad95aa34:0"}: lambda_web_adapter: app is not ready after 6000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:47.539Z
06:08:47.537 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: SUN (DSA key/parameter generation; DSA signing; SHA-1, MD5 digests; SecureRandom; X.509 certificates; PKCS12, JKS & DKS keystores; PKIX CertPathValidator; PKIX CertPathBuilder; LDAP, Collection CertStores, JavaPolicy Policy; JavaLoginConfig Configuration)
2025-12-21T06:08:47.539Z
06:08:47.539 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Sun RSA signature provider
2025-12-21T06:08:47.552Z
06:08:47.551 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Sun Elliptic Curve provider
2025-12-21T06:08:47.552Z
06:08:47.551 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Sun JSSE provider(PKCS12, SunX509/PKIX key/trust factories, SSLv3/TLSv1/TLSv1.1/TLSv1.2/TLSv1.3/DTLSv1.0/DTLSv1.2)
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: SunJCE Provider (implements RSA, DES, Triple DES, AES, Blowfish, ARCFOUR, RC2, PBE, Diffie-Hellman, HMAC, ChaCha20)
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Sun (Kerberos v5, SPNEGO)
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Sun SASL provider(implements client mechanisms for: DIGEST-MD5, EXTERNAL, PLAIN, CRAM-MD5, NTLM; server mechanisms for: DIGEST-MD5, CRAM-MD5, NTLM)
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: XMLDSig (DOM XMLSignatureFactory; DOM KeyInfoFactory; C14N 1.0, C14N 1.1, Exclusive C14N, Base64, Enveloped, XPath, XPath2, XSLT TransformServices)
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Sun PC/SC provider
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: JdkLDAP Provider (implements LDAP CertStore)
2025-12-21T06:08:47.552Z
06:08:47.552 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: JDK SASL provider(implements client and server mechanisms for GSSAPI)
2025-12-21T06:08:47.553Z
06:08:47.553 [main] INFO n.s.o.o.s.AccessFilter - JCEプロバイダー情報: Unconfigured and unusable PKCS11 provider
2025-12-21T06:08:47.936Z
06:08:47.936 [main] INFO n.s.o.o.s.AccessFilter - OpenSAMLの初期化を開始します
2025-12-21T06:08:47.937Z
06:08:47.937 [main] INFO o.o.c.c.InitializationService - Initializing OpenSAML using the Java Services API
2025-12-21T06:08:49.491Z
INFO Lambda runtime invoke{requestId="24a7b71b-01be-4b4d-baa4-a21c06e902f4" xrayTraceId="Root=1-69478edf-2bff254d74cf089970b605ff;Parent=7d1638a17cc8177e;Sampled=0;Lineage=1:ad95aa34:0"}: lambda_web_adapter: app is not ready after 8000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:51.491Z
INFO Lambda runtime invoke{requestId="24a7b71b-01be-4b4d-baa4-a21c06e902f4" xrayTraceId="Root=1-69478edf-2bff254d74cf089970b605ff;Parent=7d1638a17cc8177e;Sampled=0;Lineage=1:ad95aa34:0"}: lambda_web_adapter: app is not ready after 10000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:52.416Z
06:08:52.416 [main] INFO o.o.x.a.AlgorithmRegistry - Algorithm failed runtime support check, will not be usable: http://www.w3.org/2001/04/xmlenc#ripemd160
2025-12-21T06:08:52.437Z
06:08:52.437 [main] INFO o.o.x.a.AlgorithmRegistry - Algorithm failed runtime support check, will not be usable: http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160
2025-12-21T06:08:52.495Z
06:08:52.494 [main] INFO o.o.x.a.AlgorithmRegistry - Algorithm failed runtime support check, will not be usable: http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160
2025-12-21T06:08:53.491Z
INFO Lambda runtime invoke{requestId="24a7b71b-01be-4b4d-baa4-a21c06e902f4" xrayTraceId="Root=1-69478edf-2bff254d74cf089970b605ff;Parent=7d1638a17cc8177e;Sampled=0;Lineage=1:ad95aa34:0"}: lambda_web_adapter: app is not ready after 12000ms url=http://127.0.0.1:8080/opensaml5-webprofile-demo/actuator/health
2025-12-21T06:08:55.135Z
06:08:55.135 [main] INFO o.s.b.a.e.w.EndpointLinksResolver - Exposing 2 endpoints beneath base path '/actuator'
2025-12-21T06:08:55.339Z
06:08:55.339 [main] INFO o.a.c.h.Http11NioProtocol - Starting ProtocolHandler ["http-nio-0.0.0.0-8080"]
2025-12-21T06:08:55.435Z
06:08:55.435 [main] INFO o.s.b.w.e.t.TomcatWebServer - Tomcat started on port 8080 (http) with context path '/opensaml5-webprofile-demo'
2025-12-21T06:08:55.538Z
06:08:55.538 [main] INFO n.s.o.o.Application - Started Application in 17.037 seconds (process running for 23.53)
2025-12-21T06:08:55.817Z
06:08:55.817 [http-nio-0.0.0.0-8080-exec-1] INFO o.a.c.c.C.[.[.[/opensaml5-webprofile-demo] - Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-12-21T06:08:55.817Z
06:08:55.817 [http-nio-0.0.0.0-8080-exec-1] INFO o.s.w.s.DispatcherServlet - Initializing Servlet 'dispatcherServlet'
2025-12-21T06:08:55.916Z
06:08:55.875 [http-nio-0.0.0.0-8080-exec-1] INFO o.s.w.s.DispatcherServlet - Completed initialization in 58 ms
2025-12-21T06:08:56.373Z
=================================================
2025-12-21T06:08:56.373Z
アプリケーションが完全に起動しました (Port: 8080)
2025-12-21T06:08:56.374Z
=================================================
2025-12-21T06:08:56.753Z
06:08:56.753 [http-nio-0.0.0.0-8080-exec-2] INFO n.s.o.o.i.SingleSignOnServlet - SingleSignOnServlet.doGet: SPからAuthnRequestを受信しました。
2025-12-21T06:08:56.773Z
END RequestId: 24a7b71b-01be-4b4d-baa4-a21c06e902f4
2025-12-21T06:08:56.773Z
REPORT RequestId: 24a7b71b-01be-4b4d-baa4-a21c06e902f4 Duration: 15294.28 ms Billed Duration: 25126 ms Memory Size: 1024 MB Max Memory Used: 246 MB Init Duration: 9831.17 ms
2025-12-21T06:09:17.116Z
START RequestId: 7258794d-87b6-45eb-9bd7-59bc67099ef1 Version: $LATEST
2025-12-21T06:09:17.119Z
06:09:17.119 [http-nio-0.0.0.0-8080-exec-3] INFO n.s.o.o.i.SingleSignOnServlet - SingleSignOnServlet.doPost: 認証ボタンがクリックされました。SAMLアーティファクトを生成します。
2025-12-21T06:09:17.120Z
06:09:17.120 [http-nio-0.0.0.0-8080-exec-3] INFO n.s.o.o.i.SingleSignOnServlet - ユーザーが認証されました。アーティファクトを含めてSPにリダイレクトします: https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/sp/consumer?SAMLart=AAQAAMFbLinlXaCM%2BFIxiDwGOLAy2T71gbpO7ZhNzAgEANlB90ECfpNEVLg%3D
2025-12-21T06:09:17.152Z
END RequestId: 7258794d-87b6-45eb-9bd7-59bc67099ef1
2025-12-21T06:09:17.152Z
REPORT RequestId: 7258794d-87b6-45eb-9bd7-59bc67099ef1 Duration: 35.81 ms Billed Duration: 36 ms Memory Size: 1024 MB Max Memory Used: 246 MB
2025-12-21T06:09:17.184Z
START RequestId: 4cb3147b-3d4d-4864-9d2c-74d6b225b7fc Version: $LATEST
2025-12-21T06:09:17.187Z
06:09:17.187 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - ConsumerServlet.doGetが呼び出されました。リクエストURL: https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/sp/consumer
2025-12-21T06:09:17.194Z
06:09:17.194 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 1. IdPからアーティファクトを受信しました: AAQAAMFbLinlXaCM+FIxiDwGOLAy2T71gbpO7ZhNzAgEANlB90ECfpNEVLg=
2025-12-21T06:09:17.216Z
06:09:17.215 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 2. アーティファクト AAQAAMFbLinlXaCM+FIxiDwGOLAy2T71gbpO7ZhNzAgEANlB90ECfpNEVLg= に対してArtifactResolveリクエストを構築しました。
2025-12-21T06:09:17.216Z
06:09:17.216 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - ArtifactResolveの内容:
2025-12-21T06:09:17.272Z
06:09:17.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.OpenSAMLUtils - <?xml version="1.0" encoding="UTF-8"?><saml2p:ArtifactResolve xmlns:saml2p="urn:oasis:names:tc:SAML:2.0:protocol" Destination="https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/idp/artifactResolutionService" ID="_93b2d043712262d455c071cde25df4b7" IssueInstant="2025-12-21T06:09:17.214Z" Version="2.0">
2025-12-21T06:09:17.272Z
<saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">TestSP</saml2:Issuer>
2025-12-21T06:09:17.272Z
<saml2p:Artifact>AAQAAMFbLinlXaCM+FIxiDwGOLAy2T71gbpO7ZhNzAgEANlB90ECfpNEVLg=</saml2p:Artifact>
2025-12-21T06:09:17.272Z
</saml2p:ArtifactResolve>
2025-12-21T06:09:17.272Z
06:09:17.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 3. IdPへArtifactResolveを送信します (バックチャネル通信)...
2025-12-21T06:09:18.114Z
06:09:18.113 [http-nio-0.0.0.0-8080-exec-5] INFO o.o.x.s.s.SignatureSupport - No KeyInfoGenerator was supplied in parameters or resolveable for credential type org.opensaml.security.x509.X509Credential, No KeyInfo will be generated for Signature
2025-12-21T06:09:37.551Z
06:09:37.539 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 4. IdPからArtifactResponseを受信しました。
2025-12-21T06:09:37.552Z
06:09:37.551 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - ArtifactResponseの内容:
2025-12-21T06:09:37.555Z
06:09:37.555 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.OpenSAMLUtils - <?xml version="1.0" encoding="UTF-8"?><saml2p:ArtifactResponse xmlns:saml2p="urn:oasis:names:tc:SAML:2.0:protocol" Destination="https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/sp/consumer" ID="_70766cb1ce1acf90caecc830132fefec" IssueInstant="2025-12-21T06:09:36.655Z" Version="2.0">
2025-12-21T06:09:37.555Z
<saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">TestIDP</saml2:Issuer>
2025-12-21T06:09:37.555Z
<saml2p:Status>
2025-12-21T06:09:37.555Z
<saml2p:StatusCode Value="urn:oasis:names:tc:SAML:2.0:status:Success"/>
2025-12-21T06:09:37.555Z
</saml2p:Status>
2025-12-21T06:09:37.555Z
<saml2p:Response Destination="https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/sp/consumer" ID="_e416131939d55698d2abc5f9b4fef9ff" IssueInstant="2025-12-21T06:09:36.658Z" Version="2.0">
2025-12-21T06:09:37.555Z
<saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">TestIDP</saml2:Issuer>
2025-12-21T06:09:37.555Z
<saml2p:Status>
2025-12-21T06:09:37.555Z
<saml2p:StatusCode Value="urn:oasis:names:tc:SAML:2.0:status:Success"/>
2025-12-21T06:09:37.555Z
</saml2p:Status>
2025-12-21T06:09:37.555Z
<saml2:EncryptedAssertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">
2025-12-21T06:09:37.555Z
<xenc:EncryptedData xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="_b60e19b9f0c188a667f548fa0125e83a" Type="http://www.w3.org/2001/04/xmlenc#Element">
2025-12-21T06:09:37.555Z
<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#aes128-cbc" xmlns:xenc="http://www.w3.org/2001/04/xmlenc#"/>
2025-12-21T06:09:37.555Z
<ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
2025-12-21T06:09:37.555Z
<xenc:EncryptedKey Id="_ad0d364c034ae6beb950c9d0dc22fcdf" xmlns:xenc="http://www.w3.org/2001/04/xmlenc#">
2025-12-21T06:09:37.555Z
<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p" xmlns:xenc="http://www.w3.org/2001/04/xmlenc#">
2025-12-21T06:09:37.555Z
<ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1" xmlns:ds="http://www.w3.org/2000/09/xmldsig#"/>
2025-12-21T06:09:37.555Z
</xenc:EncryptionMethod>
2025-12-21T06:09:37.555Z
<xenc:CipherData xmlns:xenc="http://www.w3.org/2001/04/xmlenc#">
2025-12-21T06:09:37.555Z
<xenc:CipherValue>U5D1DWtJSj4sAyC/rvGcd9NSxwZPd3pGMUYx2bEx2s2C8sA+DcnciDvSyuEHBtqA4WB9rcRXZgnI&#13;
2025-12-21T06:09:37.555Z
Gg1yCAqUS/Cy+L3mSE9f8buKjI3DQ9gObu7bE2Koq6aFiscwhWxGrPXMBAFuQLxfWbTR4NuXIMBx&#13;
2025-12-21T06:09:37.555Z
3+ArxEVphr3Q3OaeNtCCgguudQ6L7H4rcMJh2NEDQKvwotiA1vNuzsopHwDHIW/LY1DneP6/U+rJ&#13;
2025-12-21T06:09:37.555Z
WZUV6DwrFnFswSnC9s7YQEEmvvrrL/KGuaX40ckfyjEuI3/XSvJp8xp9BB/WZcR/zAIVkDlG0Tw3&#13;
2025-12-21T06:09:37.555Z
JPMGJO4q5/b8ZNRgrWH0gLYqvDtotB2SMzAaow==</xenc:CipherValue>
2025-12-21T06:09:37.555Z
</xenc:CipherData>
2025-12-21T06:09:37.555Z
</xenc:EncryptedKey>
2025-12-21T06:09:37.555Z
</ds:KeyInfo>
2025-12-21T06:09:37.555Z
<xenc:CipherData xmlns:xenc="http://www.w3.org/2001/04/xmlenc#">
2025-12-21T06:09:37.555Z
<xenc:CipherValue>+RUmBTYbHH4NOl/qBmeSrFegmxSciuQzpBA6F4J1TaJSNa3HbWjWwDIQTPWe8cmPBt1pAip+A8WG&#13;
2025-12-21T06:09:37.555Z
+iMZiu7pMItMeDiZjvy6Jtn2TWN6NmuK3Zi9GtwUXD+6kf3bYubc8ret6YX+4F+xPEHpGR2uZYJC&#13;
2025-12-21T06:09:37.555Z
Z5uiOPL8T8qQtOKPdswNG9BGCfOzxTlIx6tNn1TqHj/IlalBLKuTxXrVCWsQj/gJQmk/+XZEbeiV&#13;
2025-12-21T06:09:37.555Z
61Ak8tB5MWHNIlu89XJ1N+GP8m7wVoeNhKNXLxubc0V+/BxPlcSp//eB6NMqZSoUwjS1PtSPHaMG&#13;
2025-12-21T06:09:37.555Z
47FFohsxiLyp9TML/gSAzo0S+biF3XSuuLGxrAsDPF1mq6t/zjJOnf2k/Gmfn/YdHKgOrxPgPcxx&#13;
2025-12-21T06:09:37.555Z
kH18HBC23yIVmyND9CcWdu6Y0oZzu9xZiG55RbffAR6kdXXSmSdK3FHYdjWSUyxskbs+uToICFnk&#13;
2025-12-21T06:09:37.555Z
qJFstwNftCno7E1U/QkM99aQbesEuw8OG8GelKBA5cDahAv3zJgxkoPndlanDo/IBMdEdygL9EFL&#13;
2025-12-21T06:09:37.555Z
K5GEjZ8nA6pflVI1wuW/OHIG8Uq9VzDIoeP4upv2+WlOwyr/XcJQooNLAzIhb7uxABQdf0M0xKbE&#13;
2025-12-21T06:09:37.555Z
L1+I9hrtx/aOAyBqe2hS9U7fP4zWugGoDXtZNcYDdQlDiMriPmRh8FAz9kqx9RE5nzI31lhDGuZx&#13;
2025-12-21T06:09:37.555Z
TYHD5IGr9zEietTmTXS9rYNBc0NsPG0xlBLc609Hns9VT8VFvTk9WJP/sIlzyXQiJn57uDqhBlld&#13;
2025-12-21T06:09:37.555Z
MHqA4s4MDZBRQMFuAHmrPIug/z49rw4ACwTseKQECtPy8AmKDMhOcsiYTctWKmAsLNv4UFa458Yd&#13;
2025-12-21T06:09:37.555Z
WD5L3pSADdNARpXoFwkhRN5uNGOhlHRE3HfiCCSpysdzUUbDmO4JE33w4zHHMG8y2GdVhXkTBw51&#13;
2025-12-21T06:09:37.555Z
Gl1t8XJsJQ2PakjIabTYq9nEnJ/p6E78coPinb5gfHmLhT2DOYAbYgmJl/s7PqJs1DhqmFFa9eMh&#13;
2025-12-21T06:09:37.555Z
mH9IWiHLea7RBE7+YX816CnXDd56GcAxsTJbUSnVNqx967NlAqLI8Uk2krOaIGOCjA7QB4pMgjf2&#13;
2025-12-21T06:09:37.555Z
YOST2D9rfC9+sHhhMeV2vDPZYEUYFvaqjTKA6DgP2dinzEH2dUp9crpIZySIJojeBbsySPb6fWO4&#13;
2025-12-21T06:09:37.555Z
YdHNWKR5fhRn+08lkFpPbVo/A3eFZQE8WCwgZElN5Omj1gpG0aXDAPGvPj2T/2fWcwD182QwGh+N&#13;
2025-12-21T06:09:37.555Z
h2kXXWES8Tu18Zvw+tFY8hyspnG/lHnfPOGmE2TFVu9buzCoa71s5L9FMvr0I9CDydfG8i0E/Xn+&#13;
2025-12-21T06:09:37.555Z
oWj8ZWHL7UIIt4m1ArFs7xpaxhqvPCsHxXyzDCMqzo7GUhZ8ewOjoI5XMclCMaX1CFh/8kXqIr1h&#13;
2025-12-21T06:09:37.555Z
ZyctNINZXOG5Zq6I0ohIqK5d2LWrih/uRMsJPzWcttBowL4p8rjuzCzTfrxMPQ2eFpjFaDNK7Csj&#13;
2025-12-21T06:09:37.555Z
rs8lVva0qr82GyhsBKAT2IIco9c7hCH51tUhUHA+O6RaVgk1ZrFV5vOHTtW1uWSpdsltSXbuXEjV&#13;
2025-12-21T06:09:37.555Z
eLtRg6JQNARLx5qNEA5G/UK/rMIX4tTimpLbInOQcOlWU9kUbd/MA1698HGBUnWmIWaRcFGKhRLU&#13;
2025-12-21T06:09:37.555Z
IPIb2yZOW7oMq3+Kv64vnsKv+AAtrk1ca//EgS9JwkHpTgd5Y4PCNK2Nt6V4J7sfccvmBvNyy8wY&#13;
2025-12-21T06:09:37.555Z
wtfGqDOPi5aqlgc3VG5XqIcksc/pJsS7q23Ug+Vf5r71b+ufZnL65aN5+SOM1iSCFv5ELrgafqqo&#13;
2025-12-21T06:09:37.555Z
kYTPxrqIjK8wKXyyZfpVgpjs2x7S0HUO6iNvji7WJ8VNZ+hB84X0w28kmtS3XNwVEcYyBVzDF21r&#13;
2025-12-21T06:09:37.555Z
tkRwnVtwqi3e1/4ZM4omJulIuckACVQKVs8GcDe0+rjdFF/8Eepj1Osc1SOK5SkR/iBu0jdRlc55&#13;
2025-12-21T06:09:37.555Z
slijjw08o4sMAvTSt1hV/co9hFEdE3x+mg/tgUbIlaPtpjzP9d9z2nTXBrnZZ7rbvQXRbWrPH3fc&#13;
2025-12-21T06:09:37.555Z
HF+Gu8z+AeFkQ7bqVruFEMxNoOdjXzyVIM1w3MmvtMpOMDXIICyhVdMOT7kCg1FlIFZ+UBj/VYcV&#13;
2025-12-21T06:09:37.555Z
qVr3JyvaE3+1/SLHt8lx5zMui87Tx2QewxjxO1sRI0UOPw7eUqWKGE9QE+UCg1fPqEHAmJda/0/L&#13;
2025-12-21T06:09:37.555Z
PkHgiMAeupMt4g1jAr6LhvENJHCStLvRymZqRfVUfMRLFu46SkUieSpjFaWk4fb3gdZWraTSh1Gj&#13;
2025-12-21T06:09:37.555Z
9U37VXE0UGLfE5jlCNObsTkz9nUDycfhnq6TQyyw/NimWWnS0ipimomNUdMzdV7yd/Yi8m0OaZ+f&#13;
2025-12-21T06:09:37.555Z
KaUVtVO6wu5tjYjIYQHJWEeSysQ8CDdcMD/UhvyPL1ti9BtYRVagEvg141k9kVvUNPinBOu+Nvf/&#13;
2025-12-21T06:09:37.555Z
wawtEvdiyR6hAh6d0y6zvquZPF27tpGFaMYJVs7TryZVGnWqRLvFFjmBm9c8Dy74L86ZXyiZ6eCj&#13;
2025-12-21T06:09:37.555Z
ZtUgNjKh5P79cJwwV4cfnRqyHI+mKmfcT0Wkwx+hZZV6QaOU742gYSr8XJzGRpOGE2Gz+Y9DZe0n&#13;
2025-12-21T06:09:37.555Z
0vzOBiLjb2hhhU/GyNzoKJdIj2PeWewql6kSi5pb+gckXrqkixTT6CayEW1hb73c27lMQG4sjJOK&#13;
2025-12-21T06:09:37.555Z
M0hzLhqI6qpHMoMj9n5HEDgG5jTlXChlxTQEpMx7VgNQZgJJGEg/RfzayybTunQovRv0qTBSlc/y&#13;
2025-12-21T06:09:37.555Z
Mpa6gY1AuP6KFuX1GHq4a7Uzv9hF5YviZfXk1ximAW1hsPRa21AZedMHhg04k79AOzzqC0Wnya2T&#13;
2025-12-21T06:09:37.555Z
0Ndc0Uq6Jd5belGKelLSiGgk2pMmVmoRupltpWqQICo9/s3Dq6xxyhdNI1dXMPNCnA4pzfNWCoZg&#13;
2025-12-21T06:09:37.555Z
in0qDN8hcipPJPk/vhAANitBb+JBjMqRnsRk1js2pAFbCCn3v1ByffaLTcl0ek3Ma0SfknAt1ZSn&#13;
2025-12-21T06:09:37.555Z
VTZVoIwxKoMHo10wZE6o5ksOGSrrwSw6NRiA71q1T0JDoHc8IBLcyyhsgqcz2P/PeXJ7ggzlN5o7&#13;
2025-12-21T06:09:37.555Z
ehSUcmRp2hqgN8n7qlLvaDQ0Zreq/bqS4MV76Hr6uRtg0jGSR184kWPUXULWhdg1exPyPEfZG9ze&#13;
2025-12-21T06:09:37.555Z
CDS5j97++zokjMZ7UdlpEw3r0Viw8ZHwBLpytuxKXLtUoow5JG7tFmxshgbASatgc/FlmVsRoy1b&#13;
2025-12-21T06:09:37.555Z
vwcm6Kw8A2KdgOY6JyfL1azdMFIgC84Rc9GYzB1ZeDsPSDt/ou8RBXDkwxgI58kS6uXmRNb2HXTV&#13;
2025-12-21T06:09:37.555Z
0/H3YSx9n8I5GEAZ4i3BBMd71ReVSAIo8/s/1932uDCZXQOCIhyqdOmTU7jz2iZ6nO3gI8H8v0E6&#13;
2025-12-21T06:09:37.555Z
+FkZ/prj3RYnlU90PGNw4a7DPSzBi8OYTtKrAYx66NQO1WHk3LPjFEkve9uPgIPrjj1qZ887iGdJ&#13;
2025-12-21T06:09:37.555Z
ThCayRsTdS24SpcwhWBCbeplddChMEw6SzD7zo5wH1nJYrU4nx/VRqoRx3HH/oHkL/QFkO4gDX6p&#13;
2025-12-21T06:09:37.555Z
CgJrobJRzQH/3v4FzLemYAUYd+jZDKKv+NlzP0qG3AEbRlLJfzvkrTkZMkIynB8uUw4A0arARx5h&#13;
2025-12-21T06:09:37.555Z
ymuDd9ZBosTMmbmv2HMJ/N5HGOfhEzLbxaWNtqj2dCBfFo1IHy+JtgnJ4XaGj96zNVPF39vTKcy5&#13;
2025-12-21T06:09:37.555Z
euntSUt+ZAWn7VPjJK74Fv0BJHQJuQKi7uKScxizava+h9Obf11UPoarfLdNWyG+CUjshA5J7Tdt&#13;
2025-12-21T06:09:37.555Z
C39tnHJTDMOQGZ0b8U7RjKTQMNA9qJvc9SI0/TcQvdzcPncEjqSHjGL9E1op+oHsxgVMG48fW2UO&#13;
2025-12-21T06:09:37.555Z
vUVdz/Ybc5VWBZEAnsmr5+A8yC8gbiLEkKu6NOh32VUSB8UVQ+LPwb1igCdOGgCCxTRy1oTBm5+N&#13;
2025-12-21T06:09:37.555Z
4EQJCjcG8YEy63OSE/FI/qS4yA03WG3MvgcyODQhCh0xhLZZNZrC/zhMmbxk4d6zxXl7l0inFYAU&#13;
2025-12-21T06:09:37.555Z
YGRxOks=</xenc:CipherValue>
2025-12-21T06:09:37.555Z
</xenc:CipherData>
2025-12-21T06:09:37.555Z
</xenc:EncryptedData>
2025-12-21T06:09:37.555Z
</saml2:EncryptedAssertion>
2025-12-21T06:09:37.555Z
</saml2p:Response>
2025-12-21T06:09:37.555Z
</saml2p:ArtifactResponse>
2025-12-21T06:09:37.594Z
06:09:37.594 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 5. ArtifactResponseの検証 (Destinationおよび有効期限) が完了しました。
2025-12-21T06:09:37.797Z
06:09:37.797 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 6. アサーションの復号に成功しました。
2025-12-21T06:09:38.243Z
06:09:38.243 [http-nio-0.0.0.0-8080-exec-5] WARN o.a.x.s.s.XMLSignature - Signature verification failed.
2025-12-21T06:09:38.252Z
org.opensaml.xmlsec.signature.support.SignatureException: Signature cryptographic validation not successful
2025-12-21T06:09:38.252Z
at org.opensaml.xmlsec.signature.support.impl.provider.ApacheSantuarioSignatureValidationProviderImpl.validate(ApacheSantuarioSignatureValidationProviderImpl.java:76)
2025-12-21T06:09:38.252Z
at org.opensaml.xmlsec.signature.support.SignatureValidator.validate(SignatureValidator.java:56)
2025-12-21T06:09:38.252Z
at no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.ConsumerServlet.verifyAssertionSignature(ConsumerServlet.java:198)
2025-12-21T06:09:38.252Z
at no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.ConsumerServlet.doGet(ConsumerServlet.java:102)
2025-12-21T06:09:38.252Z
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
2025-12-21T06:09:38.252Z
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
2025-12-21T06:09:38.252Z
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)
2025-12-21T06:09:38.252Z
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
2025-12-21T06:09:38.252Z
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
2025-12-21T06:09:38.252Z
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
2025-12-21T06:09:38.252Z
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
2025-12-21T06:09:38.252Z
at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
2025-12-21T06:09:38.252Z
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
2025-12-21T06:09:38.252Z
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
2025-12-21T06:09:38.253Z
at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
2025-12-21T06:09:38.253Z
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
2025-12-21T06:09:38.253Z
at org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:109)
2025-12-21T06:09:38.253Z
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
2025-12-21T06:09:38.253Z
at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
2025-12-21T06:09:38.253Z
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
2025-12-21T06:09:38.253Z
at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)
2025-12-21T06:09:38.253Z
at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
2025-12-21T06:09:38.253Z
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
2025-12-21T06:09:38.253Z
at org.apache.catalina.valves.RemoteIpValve.invoke(RemoteIpValve.java:731)
2025-12-21T06:09:38.253Z
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)
2025-12-21T06:09:38.253Z
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:389)
2025-12-21T06:09:38.253Z
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
2025-12-21T06:09:38.253Z
at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896)
2025-12-21T06:09:38.253Z
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)
2025-12-21T06:09:38.253Z
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
2025-12-21T06:09:38.253Z
at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190)
2025-12-21T06:09:38.254Z
at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
2025-12-21T06:09:38.254Z
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)
2025-12-21T06:09:38.254Z
at java.base/java.lang.Thread.run(Unknown Source)
2025-12-21T06:09:38.254Z
06:09:38.254 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 7. アサーションの署名検証に成功しました。
2025-12-21T06:09:38.254Z
06:09:38.254 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 復号されたアサーションの内容:
2025-12-21T06:09:38.255Z
06:09:38.255 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.OpenSAMLUtils - <?xml version="1.0" encoding="UTF-8"?><saml2:Assertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion" ID="_55074d3477477e0eff7863d7b5306307" IssueInstant="2025-12-21T06:09:36.659Z" Version="2.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
2025-12-21T06:09:38.255Z
<saml2:Issuer>TestIDP</saml2:Issuer>
2025-12-21T06:09:38.255Z
<ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
2025-12-21T06:09:38.255Z
<ds:SignedInfo>
2025-12-21T06:09:38.255Z
<ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
2025-12-21T06:09:38.255Z
<ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
2025-12-21T06:09:38.255Z
<ds:Reference URI="#_55074d3477477e0eff7863d7b5306307">
2025-12-21T06:09:38.255Z
<ds:Transforms>
2025-12-21T06:09:38.255Z
<ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
2025-12-21T06:09:38.255Z
<ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#">
2025-12-21T06:09:38.255Z
<ec:InclusiveNamespaces xmlns:ec="http://www.w3.org/2001/10/xml-exc-c14n#" PrefixList="xsd"/>
2025-12-21T06:09:38.255Z
</ds:Transform>
2025-12-21T06:09:38.255Z
</ds:Transforms>
2025-12-21T06:09:38.255Z
<ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
2025-12-21T06:09:38.255Z
<ds:DigestValue>IFd1VIZdvp/jpXcB/FXwkbVAhMtvqEWKLvlOB8zKFsc=</ds:DigestValue>
2025-12-21T06:09:38.255Z
</ds:Reference>
2025-12-21T06:09:38.255Z
</ds:SignedInfo>
2025-12-21T06:09:38.255Z
<ds:SignatureValue>
2025-12-21T06:09:38.255Z
jiJxjAqG0DDB0O6shvvnAbXzLdxMtAXrFvwY0uxgKIld5hJdcWZ8KrxLTtAR5ziCevTMGw92QQe1&#13;
2025-12-21T06:09:38.255Z
BLzg3+5kG3oWshYjAYV3ur+thshmjtrE6/+67rRflXvcyWfba4JUG/CcCsHqm2KVV6TSvIBefSHl&#13;
2025-12-21T06:09:38.255Z
2A7/Z06TYz/yqiM5aMQ=
2025-12-21T06:09:38.255Z
</ds:SignatureValue>
2025-12-21T06:09:38.255Z
</ds:Signature>
2025-12-21T06:09:38.255Z
<saml2:Subject>
2025-12-21T06:09:38.255Z
<saml2:NameID Format="urn:oasis:names:tc:SAML:2.0:nameid-format:transient" NameQualifier="Name qualifier" SPNameQualifier="SP name qualifier">Some NameID value</saml2:NameID>
2025-12-21T06:09:38.255Z
<saml2:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
2025-12-21T06:09:38.255Z
<saml2:SubjectConfirmationData InResponseTo="Made up ID" NotBefore="2025-12-21T06:09:36.661Z" NotOnOrAfter="2025-12-21T06:19:36.661Z" Recipient="https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/sp/consumer"/>
2025-12-21T06:09:38.255Z
</saml2:SubjectConfirmation>
2025-12-21T06:09:38.255Z
</saml2:Subject>
2025-12-21T06:09:38.255Z
<saml2:Conditions NotBefore="2025-12-21T06:09:36.661Z" NotOnOrAfter="2025-12-21T06:19:36.661Z">
2025-12-21T06:09:38.255Z
<saml2:AudienceRestriction>
2025-12-21T06:09:38.255Z
<saml2:Audience>https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/sp/consumer</saml2:Audience>
2025-12-21T06:09:38.255Z
</saml2:AudienceRestriction>
2025-12-21T06:09:38.255Z
</saml2:Conditions>
2025-12-21T06:09:38.255Z
<saml2:AttributeStatement>
2025-12-21T06:09:38.255Z
<saml2:Attribute Name="username">
2025-12-21T06:09:38.255Z
<saml2:AttributeValue xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xsd:string">bob</saml2:AttributeValue>
2025-12-21T06:09:38.255Z
</saml2:Attribute>
2025-12-21T06:09:38.255Z
<saml2:Attribute Name="telephone">
2025-12-21T06:09:38.255Z
<saml2:AttributeValue xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xsd:string">999999999</saml2:AttributeValue>
2025-12-21T06:09:38.255Z
</saml2:Attribute>
2025-12-21T06:09:38.255Z
</saml2:AttributeStatement>
2025-12-21T06:09:38.255Z
<saml2:AuthnStatement AuthnInstant="2025-12-21T06:09:36.678Z">
2025-12-21T06:09:38.255Z
<saml2:AuthnContext>
2025-12-21T06:09:38.255Z
<saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard</saml2:AuthnContextClassRef>
2025-12-21T06:09:38.255Z
</saml2:AuthnContext>
2025-12-21T06:09:38.255Z
</saml2:AuthnStatement>
2025-12-21T06:09:38.255Z
</saml2:Assertion>
2025-12-21T06:09:38.272Z
06:09:38.271 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 属性名: username
2025-12-21T06:09:38.272Z
06:09:38.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 属性値: bob
2025-12-21T06:09:38.272Z
06:09:38.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 属性名: telephone
2025-12-21T06:09:38.272Z
06:09:38.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 属性値: 999999999
2025-12-21T06:09:38.272Z
06:09:38.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 認証時刻: 2025-12-21T06:09:36.678Z
2025-12-21T06:09:38.272Z
06:09:38.272 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 認証方式: urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard
2025-12-21T06:09:38.293Z
06:09:38.293 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 8. ユーザーセッションを認証済みとしてマークしました。
2025-12-21T06:09:38.293Z
06:09:38.293 [http-nio-0.0.0.0-8080-exec-5] WARN n.s.o.o.s.ConsumerServlet - セッションに戻り先URL(GOTO_URL)が見つかりません。デフォルトのアプリケーションページにリダイレクトします。
2025-12-21T06:09:38.293Z
06:09:38.293 [http-nio-0.0.0.0-8080-exec-5] INFO n.s.o.o.s.ConsumerServlet - 要求されたURLにリダイレクトします: /opensaml5-webprofile-demo/app/appservlet
2025-12-21T06:09:38.313Z
END RequestId: 4cb3147b-3d4d-4864-9d2c-74d6b225b7fc
2025-12-21T06:09:38.313Z
REPORT RequestId: 4cb3147b-3d4d-4864-9d2c-74d6b225b7fc Duration: 21128.79 ms Billed Duration: 21129 ms Memory Size: 1024 MB Max Memory Used: 266 MB
2025-12-21T06:09:38.382Z
START RequestId: 01c3891f-b621-491c-bccf-3a9ff584ab7c Version: $LATEST
2025-12-21T06:09:38.385Z
06:09:38.384 [http-nio-0.0.0.0-8080-exec-6] INFO n.s.o.o.s.AccessFilter - AccessFilter.doFilter: リクエストをインターセプトしました。URL: https://znkcpsr911.execute-api.ap-northeast-1.amazonaws.com/opensaml5-webprofile-demo/app/appservlet
2025-12-21T06:09:38.385Z
06:09:38.385 [http-nio-0.0.0.0-8080-exec-6] INFO n.s.o.o.s.AccessFilter - セッションは既に認証済みです。通常の処理を続行します。
2025-12-21T06:09:38.386Z
END RequestId: 01c3891f-b621-491c-bccf-3a9ff584ab7c
2025-12-21T06:09:38.386Z
REPORT RequestId: 01c3891f-b621-491c-bccf-3a9ff584ab7c Duration: 4.41 ms Billed Duration: 5 ms Memory Size: 1024 MB Max Memory Used: 266 MB
```