package com.shuttleconsulting.fileuploadedapi.controller;

import com.shuttleconsulting.fileuploadedapi.dto.request.UserLoginRequestDto;
import com.shuttleconsulting.fileuploadedapi.dto.request.UserRegisterDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.dto.response.UserLoginResponseDto;
import com.shuttleconsulting.fileuploadedapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse.GenerateHttpResponse;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService service;

  @Autowired
  public AuthController(AuthService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public ResponseEntity<GenericResponse<Object>> createUser(
      @RequestBody UserRegisterDto userRegisterDto) {
    var result = service.register(userRegisterDto);
    return GenerateHttpResponse(result);
  }

  @PostMapping("/login")
  public ResponseEntity<GenericResponse<UserLoginResponseDto>> login(
      @RequestBody UserLoginRequestDto userLoginRequestDto) {
    var result = service.login(userLoginRequestDto);
    return GenerateHttpResponse(result);
  }
}
