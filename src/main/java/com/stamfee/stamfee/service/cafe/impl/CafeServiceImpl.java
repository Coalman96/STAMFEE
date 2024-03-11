package com.stamfee.stamfee.service.cafe.impl;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.cafe.*;
import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.entity.CafeMenu;
import com.stamfee.stamfee.service.cafe.CafeMenuRepository;
import com.stamfee.stamfee.service.cafe.CafeRepository;
import com.stamfee.stamfee.service.cafe.CafeService;
import com.stamfee.stamfee.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

    private final CafeRepository cafeRepository;
    private final CafeMenuRepository cafeMenuRepository;
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


    /*
    * 카페 메뉴 로직
    * */
    @Override
    public Long addCafeMenu(String cellPhone, CafeMenuSaveDto cafeMenuSaveDto) {
        /*
         * 1. 카페 조회
         * 2. 접속한 회원과 조회된 카페의 사장 회원 아이디 일치 여부 체크
         * 3. 해당 카페에 등록된 메뉴 가지고 오기
         * 4. 메뉴 개수 초과하는 경우 불가
         * 5. 검증이 완료됐으면 메뉴 추가해주기
         * */

        // 1. 카페 조회
        Cafe cafe = cafeRepository.findById(cafeMenuSaveDto.getCafeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카페가 없습니다. "));


        // 2. 접속한 회원과 조회된 카페의 사장 회원 아이디 일치 여부 체크
        if(!cellPhone.equals(cafe.getMember().getCellphone())){
            throw new IllegalArgumentException("카페 사장님 전화번호가 일치하지 않습니다. ");
        }

        // 3. 해당 카페에 등록된 메뉴 가지고 오기
        List<CafeMenu> menuList = cafeMenuRepository.findByCafeId(cafeMenuSaveDto.getCafeId());

        // 4. 카운트 별도 메서드 만들기

        // 5. 메뉴 추가 진행
        CafeMenu cafeMenu = CafeMenu.addMenu(cafeMenuSaveDto.getName(), cafeMenuSaveDto.getContent(), cafeMenuSaveDto.getPrice(),
                cafe);

        CafeMenu savedCafeMenu = cafeMenuRepository.save(cafeMenu);

        return savedCafeMenu.getId();
    }

    @Override
    public CafeMenuDto getCafeMenu(Long cafeMenuId) {
        // 카페 메뉴 상세 조회
        CafeMenu cafeMenu = cafeMenuRepository.findById(cafeMenuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. "));


        // Entity -> Dto
        return CafeMenuDto.fromEntity(cafeMenu);
    }

    @Override
    public List<CafeMenuListDto> listCafeMenu(Long cafeId) {

        // 카페에 등록되어 있는 메뉴 가져오기
        List<CafeMenu> cafeMenus = cafeMenuRepository.findByCafeId(cafeId);

        // cafeMenus(entity) -> dto로 전환
        return cafeMenus.stream()
                .map(CafeMenuListDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCafeMenu(CafeMenuUpdateDto cafeMenuUpdateDto) {

        // CafeMenu 가져오기
        CafeMenu cafeMenu = cafeMenuRepository.findById(cafeMenuUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. "));

        cafeMenu.updateCafeMenu(cafeMenuUpdateDto.getName(), cafeMenuUpdateDto.getContent(), cafeMenuUpdateDto.getPrice());


    }


}
