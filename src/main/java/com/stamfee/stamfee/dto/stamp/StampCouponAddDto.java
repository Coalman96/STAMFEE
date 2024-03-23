package com.stamfee.stamfee.dto.stamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StampCouponAddDto {

    private Long stampWalletId;
    private int count;
}
