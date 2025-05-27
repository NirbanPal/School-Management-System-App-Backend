package com.example.SMSApp.exception.custom;

public class FileValidationException extends RuntimeException {
  public FileValidationException(String message) {
    super(message);
  }
}
