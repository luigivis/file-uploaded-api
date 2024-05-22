package com.shuttleconsulting.fileuploadedapi.service;

import com.shuttleconsulting.fileuploadedapi.dto.request.UserLoginRequestDto;
import com.shuttleconsulting.fileuploadedapi.dto.request.UserRegisterDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.dto.response.UserLoginResponseDto;

public interface AuthService {

    GenericResponse<Object> register(UserRegisterDto userRegisterDto);

    GenericResponse<UserLoginResponseDto> login(UserLoginRequestDto userLoginRequestDto);

}
