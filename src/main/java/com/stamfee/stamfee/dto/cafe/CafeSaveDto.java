package com.stamfee.stamfee.dto.cafe;

import lombok.Data;

@Data
public class CafeSaveDto {

    private String name;
    private String content;
    private String telNumber;
    private String dong;
    private Double latitude;
    private Double longitude;

}
