package com.shuttleconsulting.fileuploadedapi.exception;

import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse.GenerateHttpResponse;

@Controller
public class ExceptionController implements ErrorController {

  @RequestMapping("/error")
  public ResponseEntity<GenericResponse<Object>> handleError(HttpServletRequest request) {
    var status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      var statusCode = Integer.parseInt(status.toString());
      return GenerateHttpResponse(new GenericResponse<>(HttpStatus.valueOf(statusCode)));
    }

    return GenerateHttpResponse(new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
