package com.shuttleconsulting.fileuploadedapi.config;

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
}
