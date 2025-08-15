package com.example.dentalsaas.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
  private String code;
  private String message;
  private LocalDateTime timestamp;
  private List<String> details;

  public ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public ErrorResponse(String code, String message, List<String> details) {
    this.code = code;
    this.message = message;
    this.details = details;
    this.timestamp = LocalDateTime.now();
  }

  // Getters et Setters
  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }
  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
  public LocalDateTime getTimestamp() { return timestamp; }
  public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
  public List<String> getDetails() { return details; }
  public void setDetails(List<String> details) { this.details = details; }
}
