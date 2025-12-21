# Suggested Commands for OpenSAML-sample-code

## Backend (commands must be run in `backend/` directory)
- **Build**: `mvn clean package`
- **Run (Spring Boot)**: `mvn spring-boot:run`
- **Run (JAR)**: `java -jar target/opensaml5-webprofile-demo-1.0-SNAPSHOT.jar`
- **Docker Build**: `docker build -t opensaml5-demo .`
- **Docker Run**: `docker run -p 8080:8080 opensaml5-demo`

## Infrastructure (commands must be run in `cdk/` directory)
- **Build**: `bun run build`
- **Test**: `bun run test`
- **Synth**: `bun run synth`
- **Deploy**: `bun run deploy`
- **Deploy with Parameter**: `bun run deploy -c baseUrl=https://your-api-endpoint`
- **Destroy**: `bun run destroy --force`

## Utility Commands
- **Check Health (Local)**: `curl http://localhost:8080/opensaml5-webprofile-demo/actuator/health`
- **Check Health (AWS)**: `curl <ApiEndpoint>/opensaml5-webprofile-demo/actuator/health`
