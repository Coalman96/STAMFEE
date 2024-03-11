package com.stamfee.stamfee.service.cafe;

import com.stamfee.stamfee.dto.cafe.*;

import java.util.List;

public interface CafeService {

    public Long addCafe(String cellPhone, CafeSaveDto cafeSaveDto) throws Exception;

    public CafeDto getCafe(Long cafeId);

    public boolean updateCafe(CafeUpdateDto cafeUpdateDto);

    public void deleteCafe(Long memberId, Long cafeId);

    public Long addCafeMenu(String cellPhone, CafeMenuSaveDto cafeMenuSaveDto);

    public CafeMenuDto getCafeMenu(Long cafeMenuId);

    public List<CafeMenuListDto> listCafeMenu(Long cafeId);

    public boolean updateCafeMenu(CafeMenuUpdateDto cafeMenuUpdateDto);

}
