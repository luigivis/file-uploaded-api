package com.shuttleconsulting.fileuploadedapi.exception;

import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse.GenerateHttpResponse;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(StorageException.class)
  public ResponseEntity<GenericResponse<Object>> handleConflict(StorageException ex) {
    return GenerateHttpResponse(
        new GenericResponse<>(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage()));
  }

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<GenericResponse<Object>> handleConflict(StorageFileNotFoundException ex) {
    return GenerateHttpResponse(new GenericResponse<>(HttpStatus.NOT_FOUND, ex.getMessage()));
  }
}
