package com.shuttleconsulting.fileuploadedapi.entity;

import com.shuttleconsulting.fileuploadedapi.dto.request.UserLoginRequestDto;
import com.shuttleconsulting.fileuploadedapi.dto.request.UserRegisterDto;
import com.shuttleconsulting.fileuploadedapi.enums.RoleEnum;
import com.shuttleconsulting.fileuploadedapi.utils.Encryption;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name = "user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(columnDefinition = "ENUM('ADMIN','ANONYMOUS', 'USER') NOT NULL DEFAULT 'ANONYMOUS'")
  @Enumerated(value = EnumType.STRING)
  private RoleEnum role = RoleEnum.ANONYMOUS;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT now()")
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP ON UPDATE now()")
  private LocalDateTime updatedAt;

  @Column(columnDefinition = "BOOLEAN default true")
  private Boolean status = true;

  public UserEntity(UserRegisterDto userRegisterDto) {
    var enc = new Encryption();
    this.username = userRegisterDto.getUsername();
    this.password = enc.encrypt(userRegisterDto.getPassword());
  }

  public UserEntity(UserLoginRequestDto userLoginRequestDto) {
    var enc = new Encryption();
    this.username = userLoginRequestDto.getUsername();
    this.password = userLoginRequestDto.getPassword();
  }
}
