# Project Overview: OpenSAML-sample-code

## Purpose
This project is a demonstration of how a Service Provider (SP) and an Identity Provider (IdP) communicate using SAML (Security Assertion Markup Language). It specifically uses OpenSAML 5.

Key features demonstrated:
- AuthnRequest construction, signing, and sending using HTTP Redirect binding.
- Parsing and verification of AuthnRequest signatures.
- Sending Response messages using Artifact binding.
- Encryption and decryption of Assertions.

## Codebase Structure
- `backend/`: The main Java Spring Boot application.
  - `src/main/java/`: Contains the logic for SP, IdP, and a protected Application Servlet.
  - `src/main/resources/`: Configuration files and keystores.
  - `certificates/`: SSL certificates and keys.
- `cdk/`: AWS infrastructure defined using CDK (Cloud Development Kit).
  - `bin/`: CDK app entry point.
  - `lib/`: Infrastructure stack definitions.
  - `test/`: Infrastructure tests.

## Tech Stack
- Java 21
- Spring Boot 3.3.0
- OpenSAML 5.1.6
- Jakarta EE 10
- Maven (Build tool for backend)
- AWS CDK (Infrastructure)
- Bun (Package manager and runner for CDK)
