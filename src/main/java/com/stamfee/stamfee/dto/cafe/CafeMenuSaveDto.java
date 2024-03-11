package com.stamfee.stamfee.dto.cafe;

import com.stamfee.stamfee.entity.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuSaveDto {

    private Long cafeId;
    private String name;
    private int price;
    private String content;
//    private int count;

    //Todo: 프론트에서 count를 전달해줄 수 있지 않을끼? 우선 Dto에 X


}
