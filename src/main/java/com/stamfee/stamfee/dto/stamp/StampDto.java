package com.stamfee.stamfee.dto.stamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StampDto {

    private Long id;
    private Long cafeId;
    private int count;
}
