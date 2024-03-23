package com.stamfee.stamfee.service.stamp;

import com.stamfee.stamfee.dto.stamp.StampWalletDto;

import java.util.List;

public interface StampService {

    public Long createStampWallet(String cellPhone, Long cafeId) throws Exception;

    public Long addStamp(String cellPhone, Long cafeId, int count) throws Exception;

    public StampWalletDto getStampWallet(Long stampWalletId);

    public List<StampWalletDto> listStampWallet(String cellPhone) throws Exception;

    public void addCoupon(Long stampWalletId, int count);

}
