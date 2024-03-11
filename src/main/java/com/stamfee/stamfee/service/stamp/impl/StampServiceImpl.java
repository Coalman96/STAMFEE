package com.stamfee.stamfee.service.stamp.impl;

import com.stamfee.stamfee.service.stamp.StampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {


    @Override
    public Long createStampWallet(String cellPhone, Long cafeId) {

        // 회원 조회


        // 스탬프 지갑 생성


        return null;
    }

    @Override
    public Long addStamp() {
        return null;
    }
}
