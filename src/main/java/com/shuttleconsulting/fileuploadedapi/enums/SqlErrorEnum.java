package com.shuttleconsulting.fileuploadedapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SqlErrorEnum {

    DUPLICATE_REGISTER(1062, "Duplicate information already exist."),
    OTHER(1, "Other");

    private final int code;
    private final String description;

    public static SqlErrorEnum getByCode(int code) {
        for (SqlErrorEnum role : SqlErrorEnum.values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
    throw new RuntimeException("SqlErrorEnum don't exist " + code);
    }


}
