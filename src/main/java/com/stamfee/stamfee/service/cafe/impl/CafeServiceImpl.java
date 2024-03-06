package com.stamfee.stamfee.service.cafe.impl;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.cafe.CafeDto;
import com.stamfee.stamfee.dto.cafe.CafeSaveDto;
import com.stamfee.stamfee.dto.cafe.CafeUpdateDto;
import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.service.cafe.CafeRepository;
import com.stamfee.stamfee.service.cafe.CafeService;
import com.stamfee.stamfee.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

    private final CafeRepository cafeRepository;
    private final MemberService memberService;


    //Todo: addCafe Member 정보 createCafe 추가, Dto변환 작업 필요 메뉴는 어떻게?
    @Override
    public Long addCafe(String cellPhone, CafeSaveDto cafeSaveDto) throws Exception {


        // cellPhone 으로 멤버 조회 필요
        MemberDTO memberDto = memberService.getMember(cellPhone);

        // memberDto -> Member Entity 로 전환 필요

//        //Cafe create 메서드
//        Cafe.createClub(cafeSaveDto.getName(), cafeSaveDto.getContent(),
//                cafeSaveDto.getTelNumber(), cafeSaveDto.getLongitude(), cafeSaveDto.getLatitude(),
//                cafeSaveDto.getDong(), )


        //cafeRepository.save()


        return null;
    }

    @Override
    public CafeDto getCafe(Long cafeId) {

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 카페가 없습니다."));

        return CafeDto.fromEntity(cafe);
    }

    @Override
    public void updateCafe(CafeUpdateDto cafeUpdateDto) {

        //카페 정보 찾기
        Cafe cafe = cafeRepository.findById(cafeUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("등록된 카페를 찾을 수 없습니다."));

        cafe.updateCafe(cafeUpdateDto);
    }


    //Todo: 삭제 로직 후 순위
    @Override
    public void deleteCafe(Long memberId, Long cafeId) {

    }
}
