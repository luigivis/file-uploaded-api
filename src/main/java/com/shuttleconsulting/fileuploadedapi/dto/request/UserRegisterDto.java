package com.shuttleconsulting.fileuploadedapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String password;
}
