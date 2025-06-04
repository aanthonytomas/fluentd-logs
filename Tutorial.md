# Fluentd Logging Tutorial for Java and Go

This tutorial will guide you through setting up centralized logging using Fluentd with both Java and Go applications. We'll cover everything from basic configuration to advanced logging patterns.

## Table of Contents

1. [Introduction to Fluentd](#introduction-to-fluentd)
2. [Setting Up Your Development Environment](#setting-up-your-development-environment)
3. [Java Logging Setup](#java-logging-setup)
   - [Basic Java Logging](#basic-java-logging)
   - [Structured Logging](#structured-logging)
   - [Error Handling](#error-handling)
   - [Custom Fields](#custom-fields)
4. [Go Logging Setup](#go-logging-setup)
   - [Basic Go Logging](#basic-go-logging)
   - [Structured Data](#structured-data)
   - [Custom Log Levels](#custom-log-levels)
5. [Fluentd Configuration](#fluentd-configuration)
   - [Basic Configuration](#basic-configuration)
   - [Advanced Configuration](#advanced-configuration)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)

## Introduction to Fluentd

Fluentd is an open-source data collector that helps unify data collection and consumption for a better use and understanding of data. It's particularly useful for:

- Centralizing logs from multiple sources
- Structuring and enriching log data
- Forwarding logs to various destinations
- Handling high volumes of log data

## Setting Up Your Development Environment

### Prerequisites

1. Java Development:
   - Java JDK 17 or higher
   - Maven 3.8 or higher
   - IDE (IntelliJ IDEA, Eclipse, or VS Code)

2. Go Development:
   - Go 1.20 or higher
   - IDE (VS Code with Go extension)

3. Fluentd:
   - Docker (recommended for running Fluentd)
   - Basic understanding of JSON

## Java Logging Setup

### Basic Java Logging

1. Add Dependencies to `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.11</version>
    </dependency>
    <dependency>
        <groupId>net.logstash.logback</groupId>
        <artifactId>logstash-logback-encoder</artifactId>
        <version>7.4</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.9</version>
    </dependency>
</dependencies>
```

2. Create `logback.xml` configuration:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Fluentd Appender -->
    <appender name="FLUENT" class="com.example.fluentd.CustomHttpAppender">
        <url>http://localhost:8888/app.log</url>
    </appender>

    <!-- Root logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FLUENT" />
    </root>
</configuration>
```

3. Basic Logging in Java:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleLogger {
    private static final Logger logger = LoggerFactory.getLogger(ExampleLogger.class);

    public void logBasic() {
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        
        try {
            // Simulate error
            throw new RuntimeException("Test error");
        } catch (Exception e) {
            logger.error("Error occurred", e);
        }
    }
}
```

### Structured Logging

1. Using Mapped Diagnostic Context (MDC):
```java
import org.slf4j.MDC;

public class StructuredLogger {
    public void logWithMDC() {
        MDC.put("userId", "12345");
        MDC.put("sessionId", "session-123");
        
        logger.info("User performed an action");
        
        MDC.clear(); // Clear MDC after use
    }
}
```

2. Logging Complex Objects:
```java
public class OrderLogger {
    public void logOrder(Order order) {
        logger.info("Order processed: {}", order.toJson());
    }
}
```

### Error Handling

1. Exception Logging:
```java
public class ErrorLogger {
    public void process() {
        try {
            // Simulate error
            throw new ArithmeticException("Division by zero");
        } catch (Exception e) {
            logger.error("Error in calculation", e);
        }
    }
}
```

2. Error Context:
```java
public class ErrorContextLogger {
    public void logErrorWithContext() {
        MDC.put("transactionId", "tx-123");
        MDC.put("service", "payment-processing");
        
        try {
            // Simulate error
            throw new RuntimeException("Payment failed");
        } catch (Exception e) {
            logger.error("Payment processing failed", e);
        } finally {
            MDC.clear();
        }
    }
}
```

### Custom Fields

1. Adding Custom Fields:
```java
public class CustomFieldLogger {
    public void logWithCustomFields() {
        logger.info("{}
" +
            "{" +
            "\"userId\": \"12345\",\n" +
            "\"operation\": \"login\",\n" +
            "\"status\": \"success\"\n" +
            "}", "User logged in successfully");
    }
}
```

## Go Logging Setup

### Basic Go Logging

1. Basic Setup:
```go
package main

import (
    "github.com/sirupsen/logrus"
)

func main() {
    // Initialize logger
    logger := logrus.New()
    logger.SetFormatter(&logrus.JSONFormatter{})
    
    // Basic logging
    logger.Info("This is a basic info message")
    logger.Warn("This is a warning message")
    logger.Error("This is an error message")
}
```

2. Structured Data:
```go
func logStructuredData() {
    logger := logrus.New()
    
    // Add fields
    logger.WithFields(logrus.Fields{
        "animal": "walrus",
        "size":   10,
    }).Info("A group of walrus emerges from the ocean")
}
```

### Structured Data

1. Complex Objects:
```go
type User struct {
    ID   string
    Name string
    Email string
}

func logUser(user User) {
    logger := logrus.New()
    logger.WithFields(logrus.Fields{
        "user": user,
    }).Info("User created")
}
```

2. Error Logging:
```go
func handleError(err error) {
    logger := logrus.New()
    logger.WithFields(logrus.Fields{
        "error": err.Error(),
        "stack": string(debug.Stack()),
    }).Error("Operation failed")
}
```

### Custom Log Levels

1. Adding Custom Levels:
```go
func addCustomLevel() {
    // Add custom level
    logrus.AddHook(&CustomLevelHook{})
}

type CustomLevelHook struct {}

func (h *CustomLevelHook) Levels() []logrus.Level {
    return []logrus.Level{logrus.InfoLevel, logrus.WarnLevel}
}

func (h *CustomLevelHook) Fire(entry *logrus.Entry) error {
    // Custom processing
    return nil
}
```

## Fluentd Configuration

### Basic Configuration

1. Minimal Fluentd Configuration:
```conf
<source>
  @type http
  port 8888
  bind 0.0.0.0
  tag app.log
</source>

<match app.log>
  @type stdout
</match>
```

2. Basic TCP Configuration:
```conf
<source>
  @type tcp
  port 24224
  bind 0.0.0.0
  tag app.log
</source>

<match app.log>
  @type stdout
</match>
```

### Advanced Configuration

1. Multiple Inputs:
```conf
<source>
  @type http
  port 8888
  bind 0.0.0.0
  tag app.log
</source>

<source>
  @type tcp
  port 24224
  bind 0.0.0.0
  tag app.log
</source>

<match app.log>
  @type stdout
</match>
```

2. Output to Multiple Destinations:
```conf
<match app.log>
  @type copy
  <store>
    @type stdout
  </store>
  <store>
    @type file
    path /var/log/app.log
  </store>
</match>
```

## Best Practices

1. **Log Levels**:
   - Use INFO for normal operations
   - Use WARN for potential issues
   - Use ERROR for actual errors
   - Use DEBUG for development

2. **Structured Logging**:
   - Always use structured data
   - Include timestamps
   - Include relevant context
   - Use consistent field names

3. **Error Handling**:
   - Log full stack traces
   - Include error context
   - Use proper error codes
   - Log both cause and effect

4. **Performance**:
   - Avoid logging in hot paths
   - Use asynchronous logging
   - Implement rate limiting
   - Use batch processing

5. **Security**:
   - Never log sensitive data
   - Mask sensitive fields
   - Use proper encryption
   - Implement access controls

## Troubleshooting

### Common Issues

1. **Logs Not Appearing**:
   - Check if Fluentd is running
   - Verify port bindings
   - Check network connectivity
   - Verify log levels

2. **Performance Issues**:
   - Check buffer sizes
   - Verify batch sizes
   - Check connection pooling
   - Monitor resource usage

3. **Configuration Errors**:
   - Validate Fluentd config
   - Check plugin versions
   - Verify input/output types
   - Check permissions

### Debugging Tips

1. **Enable Debug Logging**:
```conf
<system>
  log_level debug
</system>
```

2. **Use Fluentd Debug Output**:
```conf
<match app.log>
  @type debug
</match>
```

3. **Check Log Files**:
```bash
# Check Fluentd logs
tail -f /var/log/td-agent/td-agent.log

# Check application logs
tail -f test-server.log
```

## Advanced Topics

1. **Log Rotation**:
```conf
<match app.log>
  @type file
  path /var/log/app.log
  <buffer>
    @type file
    path /var/log/td-agent/buffer/app
    timekey 3600
    timekey_wait 10m
    timekey_use_utc true
  </buffer>
</match>
```

2. **Rate Limiting**:
```conf
<source>
  @type http
  port 8888
  bind 0.0.0.0
  tag app.log
  <rate_limit>
    limit 100
    interval 1s
  </rate_limit>
</source>
```

3. **Log Enrichment**:
```conf
<filter app.log>
  @type record_modifier
  <record>
    environment ${ENVIRONMENT}
    service_name ${SERVICE_NAME}
  </record>
</filter>
```

## Conclusion

This tutorial has covered everything from basic logging setup to advanced configurations for both Java and Go applications using Fluentd. Remember:

1. Always use structured logging
2. Include proper error context
3. Follow consistent logging patterns
4. Monitor and maintain your logging infrastructure
5. Keep security in mind when logging

By following these guidelines and best practices, you'll be able to create robust and maintainable logging systems that will help you monitor and troubleshoot your applications effectively.

## Additional Resources

- [Fluentd Official Documentation](https://docs.fluentd.org/)
- [Logback Documentation](https://logback.qos.ch/documentation.html)
- [Logrus Documentation](https://github.com/sirupsen/logrus)
- [Structured Logging Best Practices](https://www.datadoghq.com/blog/structured-logging-best-practices/)
