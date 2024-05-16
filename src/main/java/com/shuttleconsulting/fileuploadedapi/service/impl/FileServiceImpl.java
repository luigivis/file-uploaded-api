package com.shuttleconsulting.fileuploadedapi.service.impl;

import com.shuttleconsulting.fileuploadedapi.dto.response.FileUploadResponseDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.enums.FilesEnum;
import com.shuttleconsulting.fileuploadedapi.exception.StorageException;
import com.shuttleconsulting.fileuploadedapi.exception.StorageFileNotFoundException;
import com.shuttleconsulting.fileuploadedapi.service.FileService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

  private final Path rootLocation;

  public FileServiceImpl() {
    this.rootLocation = Paths.get(FilesEnum.FILE_LOCATION.getValue());
  }

  @Override
  public GenericResponse<FileUploadResponseDto> store(MultipartFile file) {
    Path destinationFile;
    try {

      if (file.isEmpty()) {
        throw new RuntimeException("Failed to store empty file.");
      }
      destinationFile =
          this.rootLocation
              .resolve(Paths.get(file.getOriginalFilename()))
              .normalize()
              .toAbsolutePath();

      if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
        throw new StorageException("Cannot store file outside current directory.");
      }

      var inputStream = file.getInputStream();
      Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new StorageException("Failed to store file.", e);
    }
    return new GenericResponse<>(
        HttpStatus.OK, new FileUploadResponseDto(destinationFile.getFileName().toString()));
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      var file = rootLocation.resolve(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      }
      throw new StorageFileNotFoundException("Could not read file: " + filename);
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public Boolean deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
    return true;
  }
}
