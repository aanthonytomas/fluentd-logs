# Fluentd Logging Demo

This project demonstrates centralized logging using Fluentd with both Java and Go applications. It shows how to configure and send structured logs from different languages to a centralized Fluentd server.

## Project Structure

```
fluentd/
├── fluentd/                 # Fluentd configuration and Docker setup
├── fluentd-go-example/      # Go logging example
├── test-server/            # Simple HTTP test server
└── src/                    # Java application
```

## Prerequisites

- Java 17 or higher
- Go 1.20 or higher
- Docker (for Fluentd)
- Maven (for Java)

## Setup Instructions

### 1. Java Application Setup

1. Navigate to the project root:
```bash
cd fluentd
```

2. Build and run the Java application:
```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.example.fluentd.FluentdLoggingDemo"
```

### 2. Go Application Setup

1. Navigate to the Go example directory:
```bash
cd fluentd-go-example
```

2. Run the Go application:
```bash
go run main.go
```

### 3. Test Server Setup

The project includes a simple HTTP test server that can be used to verify logging functionality:

1. Navigate to the test server directory:
```bash
cd test-server
```

2. Run the test server:
```bash
go run main.go
```

The test server will log all incoming HTTP requests to `test-server.log`.

## Fluentd Configuration

The Fluentd configuration is located in `fluentd/conf/fluentd.conf`. It's configured to:
- Listen for HTTP input on port 8888
- Listen for TCP input on port 24224
- Output logs to console in JSON format

## Logging Examples

### Java Logging

The Java application demonstrates:
- Different log levels (INFO, WARN, ERROR)
- Structured logging with MDC
- Error handling and stack traces
- JSON formatting using Logstash encoder

### Go Logging

The Go application demonstrates:
- Structured logging with logrus
- Different log levels (INFO, WARN, ERROR)
- JSON formatting
- Error handling
- User action logging with structured data

## Log Format

### Java Logs

```json
{
    "@timestamp": "2025-06-04T16:23:50.000+08:00",
    "level": "INFO",
    "thread": "main",
    "logger": "c.example.fluentd.FluentdLoggingDemo",
    "message": "Starting Fluentd Logging Demo"
}
```

### Go Logs

```json
{
    "level": "info",
    "msg": "This is a structured log message from logrus",
    "time": "2025-06-04T16:24:33+08:00"
}
```

## Testing

To verify the logging works:

1. Start the test server:
```bash
cd test-server && go run main.go
```

2. Run the Java application:
```bash
cd .. && mvn exec:java -Dexec.mainClass="com.example.fluentd.FluentdLoggingDemo"
```

3. Run the Go application:
```bash
cd fluentd-go-example && go run main.go
```

Check `test-server/test-server.log` to see all the received logs.

## Troubleshooting

1. If logs aren't appearing in the test server:
   - Verify the test server is running
   - Check if port 8888 is being used by another process
   - Verify network connectivity

2. If Java logging fails:
   - Check the logback.xml configuration
   - Verify the Logstash encoder dependencies
   - Check for connection errors in the logs

3. If Go logging fails:
   - Verify the HTTP client configuration
   - Check for network connectivity
   - Verify the logrus configuration

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
