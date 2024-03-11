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
public class CafeMenuDto {

    private Long id;
    private String name;
    private String content;
    private int price;

    // 카페 메뉴 이미지 등록 필요

    public static CafeMenuDto fromEntity(CafeMenu cafeMenu){
        return CafeMenuDto.builder()
                .id(cafeMenu.getId())
                .name(cafeMenu.getName())
                .content(cafeMenu.getContent())
                .price(cafeMenu.getPrice())
                .build();
    }

}
