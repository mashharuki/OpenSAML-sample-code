# ============================================
# ステージ 1: ビルド
# ============================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# pom.xml をコピー
COPY pom.xml ./

# 依存関係をダウンロード (キャッシュレイヤー)
RUN mvn dependency:go-offline -B || true

# ソースコードをコピー
COPY src ./src

# アプリケーションをビルド
RUN mvn clean package -DskipTests -B

# ============================================
# ステージ 2: 実行環境 (Lambda Web Adapter)
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# ヘルスチェック用の curl をインストール
RUN apk add --no-cache curl

# Lambda Web Adapter をコピー
COPY --from=public.ecr.aws/awsguru/aws-lambda-adapter:0.9.1 /lambda-adapter /opt/extensions/lambda-adapter

# ビルドされた JAR ファイルをコピー
COPY --from=builder /app/target/*.jar app.jar

# Lambda Web Adapter 用の環境変数
ENV PORT=8080
ENV READINESS_CHECK_PATH=/opensaml5-webprofile-demo/actuator/health
ENV ASYNC_INIT=true

# Spring Boot の設定
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# ポートを開放
EXPOSE 8080

# ローカル Docker 用のヘルスチェック
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/opensaml5-webprofile-demo/actuator/health || exit 1

# アプリケーションを実行
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
