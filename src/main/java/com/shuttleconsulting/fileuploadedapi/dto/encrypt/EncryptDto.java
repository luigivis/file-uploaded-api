package com.shuttleconsulting.fileuploadedapi.dto.encrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncryptDto {

  private UUID uuid;
  private String username;
  private String role;
  private LocalDateTime creationDate;
  private LocalDateTime expirationDate;

  public String toString() {
    try {
      var mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return mapper
          .writeValueAsString(new EncryptDto(uuid, username, role, creationDate, expirationDate));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error to convert to json", e);
    }
  }

  public EncryptDto toEncryptDtoFromJson(String json) {
    try {
      var mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return mapper.readValue(json, EncryptDto.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error to convert EncryptDto", e);
    }
  }

  public Boolean validateExpiration(){
    return LocalDateTime.now().isAfter(this.expirationDate);
  }
}
