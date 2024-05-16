package com.shuttleconsulting.fileuploadedapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> implements Serializable {
  @JsonIgnore private HttpStatus httpStatus;

  private int code;

  private String message;

  private T data;

  public GenericResponse(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    this.code = httpStatus.value();
    this.message = httpStatus.getReasonPhrase();
  }

  public GenericResponse(HttpStatus httpStatus, String message, T data) {
    this.httpStatus = httpStatus;
    this.code = httpStatus.value();
    this.message = message;
    this.data = data;
  }

  public GenericResponse(HttpStatus httpStatus, T data) {
    this.httpStatus = httpStatus;
    this.code = httpStatus.value();
    this.message = httpStatus.getReasonPhrase();
    this.data = data;
  }

  public static <T> ResponseEntity<GenericResponse<T>> GenerateHttpResponse(
      GenericResponse<T> dto) {
    return ResponseEntity.status(dto.getHttpStatus()).body(dto);
  }
}
