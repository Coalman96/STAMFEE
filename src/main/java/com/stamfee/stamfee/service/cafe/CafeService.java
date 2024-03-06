package com.stamfee.stamfee.service.cafe;

import com.stamfee.stamfee.dto.cafe.CafeDto;
import com.stamfee.stamfee.dto.cafe.CafeSaveDto;
import com.stamfee.stamfee.dto.cafe.CafeUpdateDto;

public interface CafeService {

    public Long addCafe(String cellPhone, CafeSaveDto cafeSaveDto) throws Exception;

    public CafeDto getCafe(Long cafeId);

    public void updateCafe(CafeUpdateDto cafeUpdateDto);

    public void deleteCafe(Long memberId, Long cafeId);

}
