package com.shuttleconsulting.fileuploadedapi.controller;

import com.shuttleconsulting.fileuploadedapi.dto.response.FileUploadResponseDto;
import com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse;
import com.shuttleconsulting.fileuploadedapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

import static com.shuttleconsulting.fileuploadedapi.dto.response.GenericResponse.GenerateHttpResponse;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

  private final FileService service;

  @Autowired
  public FileController(FileService service) {
    this.service = service;
  }

  @PostMapping()
  //@CrossOrigin(origins = {"http://localhost:*", "https://localhost:*"})
  public ResponseEntity<GenericResponse<FileUploadResponseDto>> uploadFile(@RequestParam("file") MultipartFile file) {
    return GenerateHttpResponse(service.store(file));
  }

  @GetMapping("/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    var file = service.loadAsResource(filename);
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }
}
