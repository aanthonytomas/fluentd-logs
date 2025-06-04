package main

import (
	"time"
	"net/http"
	"encoding/json"
	"bytes"
	log "github.com/sirupsen/logrus"
)

const (
	fluentdURL = "http://localhost:8888"
)

func main() {
	// Initialize logrus
	log.SetFormatter(&log.JSONFormatter{})
	log.SetLevel(log.InfoLevel)

	// Function to send log via HTTP
	logViaHTTP := func(tag string, data interface{}) error {
		jsonData, err := json.Marshal(data)
		if err != nil {
			return err
		}
		
		url := fluentdURL + "/" + tag
		resp, err := http.Post(url, "application/json", bytes.NewBuffer(jsonData))
		if err != nil {
			return err
		}
		defer resp.Body.Close()
		return nil
	}

	// Simple log message
	data := map[string]interface{}{
		"message": "Starting Go Fluentd logging demo",
		"timestamp": time.Now().Format(time.RFC3339),
	}
	err := logViaHTTP("go.app.info", data)
	if err != nil {
		log.Fatal("Failed to send log:", err)
	}

	// Log with structured data
	data = map[string]interface{}{
		"message": "User action",
		"user":    "john_doe",
		"action":  "login",
		"status":  "success",
		"ip":      "192.168.1.100",
		"timestamp": time.Now().Format(time.RFC3339),
	}
	err = logViaHTTP("go.app.info", data)
	if err != nil {
		log.Error("Failed to send log:", err)
	}

	// Using logrus with structured logging
	log.Info("This is a structured log message from logrus")
	log.WithFields(log.Fields{
		"animal": "walrus",
		"size":   10,
	}).Info("A group of walrus emerges from the ocean")

	log.Warn("This is a warning message from logrus")
	log.Error("This is an error message from logrus")

	log.Info("Go Fluentd logging demo completed")
}
