# OpenAPI Workflow (Arazzo) Parser

[![](https://badgen.net/github/license/API-Flows/openapi-workflow-parser)](LICENSE)
[![](https://badgen.net/maven/v/maven-central/com.api-flows/openapi-workflow-parser)](https://repo1.maven.org/maven2/com/api-flows/openapi-workflow-parser/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=API-Flows_openapi-workflow-parser&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=API-Flows_openapi-workflow-parser)

Parsing API workflows.

## Overview

The OpenAPI Workflow parser is an open-source Java library designed to parse the [OAI Arazzo specification](https://https://github.com/OAI/Arazzo-Specification/) files. It reads an Arazzo file (JSON or YAML formats are supported) and creates the corresponding Java objects.  

The parser's goal is to simplify the extraction and manipulation of workflows, helping developers create applications and tools that harness the semantic structure of API flows.

## Features

- **Workflow Parsing:** Reads OAI Arazzo specification files loading the corresponding Java objects.
- **Ease of Use:** Provides a simple way for developers to parse workflows.
- **Compatibility:** Supports both JSON and YAML formats.
- **Validation:** Validates the specification according to the [Arazzo Specification v1.0.0](https://https://github.com/OAI/Arazzo-Specification/).
  
## Usage

### Add to the project

You can include this library from Maven central:
```
  <dependency>
    <groupId>com-api-flows</groupId>
    <artifactId>openapi-workflow-parser</artifactId>
    <version>0.0.2</version>
  </dependency>
```

Parse from file:
```java
  final String WORKFLOWS_SPEC = "path/pet-coupons.workflow.yaml";

  OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC);

  boolean valid = result.isValid();
  String title = result.getOpenAPIWorkflow().getInfo().getTitle();
```

Parse from URL:
```java
  final String WORKFLOWS_SPEC = "https://host/path/pet-coupons.workflow.yaml";

  OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC);

  boolean valid = result.isValid();
  String title = result.getOpenAPIWorkflow().getInfo().getTitle();
```

## Build from source

Clone from the GitHub repository

```bash
  git clone https://github.com/API-Flows/openapi-workflow-parser.git
  cd openapi-workflow-parser
  mvn package
```

## Use snapshots

Add the Maven repository for the OpenAPI workflow parser snapshots:
```xml
 <repositories>
    <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/API-Flows/openapi-workflow-parser</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>
```
Add the SNAPSHOT dependency ([check latest](pom.xml) available) in your POM file:
```xml
    <dependency>
      <groupId>com.api-flows</groupId>
      <artifactId>openapi-workflow-parser</artifactId>
      <version>0.0.3-SNAPSHOT</version>
    </dependency>
```
