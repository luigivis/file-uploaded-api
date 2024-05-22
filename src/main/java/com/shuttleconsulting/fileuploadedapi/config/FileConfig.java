package com.shuttleconsulting.fileuploadedapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shuttleconsulting.fileuploadedapi.enums.FilesEnum;
import com.shuttleconsulting.fileuploadedapi.exception.StorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FileConfig {

  @Bean
  protected Boolean init() {
    var path = Paths.get(FilesEnum.FILE_LOCATION.getValue());
    log.info("CREATING DIRECTORY {}", path);
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
    return true;
  }

  @Bean
  protected ObjectMapper configObjectMapper(){
    var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
