package com.shuttleconsulting.fileuploadedapi.service.impl;

import com.shuttleconsulting.fileuploadedapi.dto.encrypt.EncryptDto;
import com.shuttleconsulting.fileuploadedapi.dto.request.UserLoginRequestDto;
import com.shuttleconsulting.fileuploadedapi.dto.request.UserRegisterDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.dto.response.UserLoginResponseDto;
import com.shuttleconsulting.fileuploadedapi.entity.UserEntity;
import com.shuttleconsulting.fileuploadedapi.repository.UserRepository;
import com.shuttleconsulting.fileuploadedapi.service.AuthService;
import com.shuttleconsulting.fileuploadedapi.utils.Encryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepository repository;

  @Autowired
  public AuthServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public GenericResponse<Object> register(UserRegisterDto userRegisterDto) {
    var save = repository.save(new UserEntity(userRegisterDto));
    return new GenericResponse<>(HttpStatus.OK, save);
  }

  @Override
  public GenericResponse<UserLoginResponseDto> login(UserLoginRequestDto userLoginRequestDto) {
    var enc = new Encryption();
    var dto = repository.findUserEntitiesByUsername(userLoginRequestDto.getUsername());

    if (dto == null) {
      return new GenericResponse<>(HttpStatus.UNAUTHORIZED);
    }

    if (!enc.encrypt(userLoginRequestDto.getPassword()).equals(dto.getPassword())) {
      return new GenericResponse<>(HttpStatus.UNAUTHORIZED);
    }

    var encryptDto =
        new EncryptDto(
            dto.getUuid(),
            dto.getUsername(),
            dto.getRole().name(),
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1));

    var token = enc.encrypt(encryptDto);
    var response = new UserLoginResponseDto(token);

    return new GenericResponse<>(HttpStatus.OK, response);
  }
}
