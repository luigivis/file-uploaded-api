package com.shuttleconsulting.fileuploadedapi.service;

import com.shuttleconsulting.fileuploadedapi.dto.response.FileUploadResponseDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {

    GenericResponse<FileUploadResponseDto> store(MultipartFile file);

    Resource loadAsResource(String filename);

    Boolean deleteAll();

}
