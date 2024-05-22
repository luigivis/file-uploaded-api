package com.shuttleconsulting.fileuploadedapi.exception;

import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.enums.SqlErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

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

  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<GenericResponse<Object>> handleConflict(
      SQLIntegrityConstraintViolationException ex) {
    return GenerateHttpResponse(
        new GenericResponse<>(HttpStatus.CONFLICT, SqlErrorEnum.getByCode(ex.getErrorCode()).getDescription()));
  }

  @ExceptionHandler(DecryptException.class)
  public ResponseEntity<GenericResponse<Object>> handleConflict(DecryptException ex) {
    return GenerateHttpResponse(new GenericResponse<>(HttpStatus.UNAUTHORIZED, ex.getMessage()));
  }
}
