# Fluentd Logging with Go

This directory contains a Go example that demonstrates how to send logs to Fluentd.

## Prerequisites

- Go 1.21 or later
- Fluentd installed and running (see main project README)
- Required Go modules will be downloaded automatically

## Running the Example

1. First, ensure Fluentd is running with the provided configuration.

2. Download the dependencies:
   ```bash
   cd fluentd-go-example
   go mod tidy
   ```

3. Run the Go application:
   ```bash
   go run main.go
   ```

4. Check the Fluentd output to see the logs being processed.

## Code Explanation

The example demonstrates:

1. Basic Fluentd logging
2. Structured logging with custom fields
3. Error logging with stack traces
4. Integration with logrus for more advanced logging features

## Configuration

The Fluentd configuration is set up to handle both Java and Go logs. By default, it listens on port 24224 for forward protocol and 8888 for HTTP.

## Extending the Example

To send logs to additional destinations (like Elasticsearch), uncomment and configure the appropriate sections in `fluentd.conf`.

## Troubleshooting

- If you see connection errors, ensure Fluentd is running and accessible
- Check the Fluentd logs for any configuration errors
- Verify that the Fluentd input ports (24224, 8888) are not blocked by a firewall
