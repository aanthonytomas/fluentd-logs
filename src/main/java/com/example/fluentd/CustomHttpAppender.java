package com.example.fluentd;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class CustomHttpAppender extends AppenderBase<ILoggingEvent> {
    private String url;
    private HttpClient httpClient;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void start() {
        if (url == null) {
            addError("URL must be set");
            return;
        }
        
        httpClient = HttpClient.newBuilder()
            .build();
        
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            String json = event.getFormattedMessage();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            addError("Failed to send log to HTTP endpoint", e);
        }
    }
}
