package com.stamfee.stamfee.dto.cafe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuUpdateDto {

    private Long id;
    private String name;
    private String content;
    private int price;

}
