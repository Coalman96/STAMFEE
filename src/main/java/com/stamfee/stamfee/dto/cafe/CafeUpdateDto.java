package com.stamfee.stamfee.dto.cafe;

import lombok.Data;

@Data
public class CafeUpdateDto {

    private Long id;
    private String name;
    private String telNumber;
    private String content;
    private Double longitude;
    private Double latitude;
    private String dong;
}
