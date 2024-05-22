package com.shuttleconsulting.fileuploadedapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponseDto {
    private String accessToken;
}
