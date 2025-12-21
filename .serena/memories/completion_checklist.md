# Completion Checklist

Before finishing a task, ensure the following steps are performed:

1.  **Backend Verification**:
    - Run `mvn clean package` in the `backend/` directory to ensure the build passes.
    - (If tests are added in the future) Run `mvn test`.
2.  **Infrastructure Verification**:
    - Run `bun run build` and `bun run test` in the `cdk/` directory.
3.  **Local Execution Test**:
    - Verify that the application starts with `mvn spring-boot:run`.
    - Check the health endpoint: `curl http://localhost:8080/opensaml5-webprofile-demo/actuator/health`.
4.  **Japanese Documentation**:
    - Ensure all new methods/classes have Japanese Javadoc/comments.
5.  **Clean up**:
    - Remove any temporary files or debug logs.
