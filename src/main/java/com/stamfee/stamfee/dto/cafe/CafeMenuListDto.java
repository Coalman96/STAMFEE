package com.stamfee.stamfee.dto.cafe;

import com.stamfee.stamfee.entity.CafeMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuListDto {

    private Long id;
    private String name;
    private String content;
    private int price;

    public static CafeMenuListDto fromEntity(CafeMenu entity){
        return CafeMenuListDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .content(entity.getContent())
                .price(entity.getPrice())
                .build();
    }
}