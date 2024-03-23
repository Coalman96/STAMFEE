package com.stamfee.stamfee.dto.stamp;

import com.stamfee.stamfee.entity.StampWallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StampWalletDto {

    private Long id;
    private String name;
    private String color;
    private int stampCount;
    private int couponCount;

    public static StampWalletDto fromEntity(StampWallet stampWallet){
        return StampWalletDto.builder()
                .id(stampWallet.getId())
                .name(stampWallet.getName())
                .color(stampWallet.getColor())
                .stampCount(stampWallet.getStampCount())
                .couponCount(stampWallet.getCouponCount())
                .build();
    }


}
