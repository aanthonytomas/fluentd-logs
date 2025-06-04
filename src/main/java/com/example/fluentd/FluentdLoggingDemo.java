package com.example.fluentd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FluentdLoggingDemo {
    private static final Logger logger = LoggerFactory.getLogger(FluentdLoggingDemo.class);

    public static void main(String[] args) {
        // Set up MDC (Mapped Diagnostic Context) for structured logging
        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("service", "fluentd-demo");
        
        logger.info("Starting Fluentd Logging Demo");
        
        // Simple log messages
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        
        // Log with exception
        try {
            throw new ArithmeticException("Division by zero");
        } catch (Exception e) {
            logger.error("Error in calculation: {}", e.getMessage(), e);
        }
        
        // Log with structured data using MDC
        MDC.put("user", "john_doe");
        MDC.put("action", "login");
        logger.info("User action performed");
        
        // Log with additional fields
        Map<String, String> orderDetails = new HashMap<>();
        orderDetails.put("orderId", "ORD-12345");
        orderDetails.put("amount", "99.99");
        orderDetails.put("currency", "USD");
        
        logger.info("Order processed: {}", orderDetails);
        
        // Clear MDC
        MDC.clear();
        
        logger.info("Fluentd Logging Demo completed");
        
        // Keep the application running for a while to ensure logs are sent
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
