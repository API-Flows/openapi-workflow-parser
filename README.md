# OpenAPI Workflow Parser

![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)
[![](https://badgen.net/github/license/api-flows/openapi-workflow-parser)](LICENSE)
[![](https://badgen.net/maven/v/maven-central/com.api-flows/openapi-workflow-parser)](https://repo1.maven.org/maven2/com/api-flows/openapi-workflow-parser/)

Parsing OpenAPI workflows.

## Overview

The OpenAPI Workflow parser is an open-source Java library designed to parse the [OpenAPI SIG-Workflows specification](https://github.com/OAI/sig-workflows) files. It reads an OpenAPI workflow specifications file (JSON or YAML formats are supported) and creates the corresponding Java objects.  
The parser's goal is to simplifiy the extraction and manipulation of OpenAPI workflows, helping developers create applications and tools that leverage the semantics of API flows.

## Features

- **Workflow Parsing:** Reads OpenAPI specification files loading the corresponding Java objects.
- **Ease of Use:** Provides a user-friendly way for developers to parse OpenAPI workflows.
- **Compatibility:** Supports OpenAPI specifications in JSON and YAML formats.
- **Validation:** Validates the OpenAPI specification according to the [Workflows Specification v1.0.0](https://github.com/OAI/sig-workflows/blob/main/versions/1.0.0.md).
  
## Usage

### Add to the project

You can include this library from Maven central:
```
  <dependency>
    <groupId>com-api-flows</groupId>
    <artifactId>openapi-workflow-parser</artifactId>
    <version>0.0.1</version>
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
  git clone https://github.com/gcatanese/openapi-workflow-parser.git
```

