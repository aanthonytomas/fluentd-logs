package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
)

func handler(w http.ResponseWriter, r *http.Request) {
	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		log.Println("Error reading body:", err)
		return
	}
	
	// Write to log file
	logFile, err := os.OpenFile("test-server.log", os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0644)
	if err != nil {
		log.Println("Error opening log file:", err)
		return
	}
	defer logFile.Close()

	if _, err := logFile.WriteString(string(body) + "\n"); err != nil {
		log.Println("Error writing to log file:", err)
		return
	}

	fmt.Fprintf(w, "OK")
}

func main() {
	http.HandleFunc("/", handler)
	log.Println("Starting test server on port 8888")
	log.Fatal(http.ListenAndServe(":8888", nil))
}
