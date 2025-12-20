import * as cdk from 'aws-cdk-lib/core';
import { Construct } from 'constructs';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as apigatewayv2 from 'aws-cdk-lib/aws-apigatewayv2';
import * as apigatewayv2_integrations from 'aws-cdk-lib/aws-apigatewayv2-integrations';
import * as ecr_assets from 'aws-cdk-lib/aws-ecr-assets';
import * as path from 'path';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // BASE_URL for SAML endpoints (set via context or use placeholder for initial deploy)
    // Usage: cdk deploy -c baseUrl=https://your-api-gateway-url.execute-api.region.amazonaws.com
    const baseUrl = this.node.tryGetContext('baseUrl') || '';

    // Build Docker image for Lambda
    const dockerImage = new ecr_assets.DockerImageAsset(this, 'SamlSpringBootImage', {
      directory: path.join(__dirname, '../../backend'),
      platform: ecr_assets.Platform.LINUX_AMD64,
    });

    // Lambda function using Lambda Web Adapter
    const samlFunction = new lambda.DockerImageFunction(this, 'SamlSpringBootFunction', {
      code: lambda.DockerImageCode.fromEcr(dockerImage.repository, {
        tagOrDigest: dockerImage.imageTag,
      }),
      memorySize: 1024,
      timeout: cdk.Duration.seconds(30),
      architecture: lambda.Architecture.X86_64,
      environment: {
        // Lambda Web Adapter settings
        PORT: '8080',
        READINESS_CHECK_PATH: '/opensaml5-webprofile-demo/actuator/health',
        ASYNC_INIT: 'true',
        // Spring Boot settings
        JAVA_OPTS: '-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0',
        // SAML endpoint base URL (required for SAML redirects)
        BASE_URL: baseUrl,
      },
      description: 'SAML Spring Boot application with Lambda Web Adapter',
    });

    // HTTP API Gateway
    const httpApi = new apigatewayv2.HttpApi(this, 'SamlHttpApi', {
      apiName: 'saml-spring-boot-api',
      description: 'HTTP API for SAML Spring Boot application',
      corsPreflight: {
        allowOrigins: ['*'],
        allowMethods: [apigatewayv2.CorsHttpMethod.ANY],
        allowHeaders: ['*'],
      },
    });

    // Lambda integration
    const lambdaIntegration = new apigatewayv2_integrations.HttpLambdaIntegration(
      'SamlLambdaIntegration',
      samlFunction
    );

    // Add routes - proxy all requests to Lambda
    httpApi.addRoutes({
      path: '/{proxy+}',
      methods: [apigatewayv2.HttpMethod.ANY],
      integration: lambdaIntegration,
    });

    // Root path route
    httpApi.addRoutes({
      path: '/',
      methods: [apigatewayv2.HttpMethod.ANY],
      integration: lambdaIntegration,
    });

    // Outputs
    new cdk.CfnOutput(this, 'ApiEndpoint', {
      value: httpApi.apiEndpoint,
      description: 'HTTP API endpoint URL',
    });

    new cdk.CfnOutput(this, 'LambdaFunctionName', {
      value: samlFunction.functionName,
      description: 'Lambda function name',
    });

    new cdk.CfnOutput(this, 'LambdaFunctionArn', {
      value: samlFunction.functionArn,
      description: 'Lambda function ARN',
    });
  }
}
