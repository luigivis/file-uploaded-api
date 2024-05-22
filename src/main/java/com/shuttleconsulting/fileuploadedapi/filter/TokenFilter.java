package com.shuttleconsulting.fileuploadedapi.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuttleconsulting.fileuploadedapi.dto.encrypt.EncryptDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.exception.DecryptException;
import com.shuttleconsulting.fileuploadedapi.utils.Encryption;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    var enc = new Encryption();

    var httpRequest = (HttpServletRequest) request;
    var httpResponse = (HttpServletResponse) response;
    var requestURI = httpRequest.getRequestURI();

    if (validUri(requestURI)) {
      continueProcess(request, response, chain);
      return;
    }

    var token = httpRequest.getHeader(AUTHORIZATION);
    if (token == null || token.isBlank()) {
      launchUnauthorized(httpResponse);
      return;
    }

    // ToDo: Valid expiration
    try {
      var decrypted = enc.decrypt(token);

      var encryptDto = new EncryptDto().toEncryptDtoFromJson(decrypted);

      if (encryptDto.validateExpiration()) {
        launchUnauthorized(httpResponse);
        return;
      }

    } catch (DecryptException ex) {
      launchUnauthorized(httpResponse);
      return;
    }

    continueProcess(request, response, chain);
  }

  private void continueProcess(
      ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    filterChain.doFilter(request, response);
  }

  private void launchUnauthorized(HttpServletResponse response) throws IOException {
    var genericResponse = new GenericResponse<>(HttpStatus.UNAUTHORIZED);
    response.getWriter().write(new ObjectMapper().writeValueAsString(genericResponse));
    response.setContentType("application/json");
    response.setStatus(genericResponse.getCode());
  }

  private boolean validUri(String uri) {
    return uri.contains("/api/v1/auth")
        || uri.contains("swagger-ui")
        || uri.contains("/actuator")
        || uri.contains("/v3/api-docs")
        || uri.equals("/favicon.ico")
        || uri.contains("/api/v1/security/tokenValid");
  }
}
