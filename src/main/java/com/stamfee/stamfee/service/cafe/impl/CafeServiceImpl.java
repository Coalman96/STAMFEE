package com.stamfee.stamfee.service.cafe.impl;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.cafe.*;
import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.entity.CafeMenu;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.mapper.MemberMapper;
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
    private final MemberMapper memberMapper;

    //Todo: addCafe Member 정보 createCafe 추가, Dto변환 작업 필요 메뉴는 어떻게?
    @Override
    public Long addCafe(String cellPhone, CafeSaveDto cafeSaveDto) throws Exception {


        // cellPhone 으로 멤버 조회 필요
        MemberDTO memberDto = memberService.getMember(cellPhone);

        // memberDto -> Member Entity 로 전환 필요
        Member member = memberMapper.memberDTOToMember(memberDto);

        //Cafe create 메서드
        Cafe cafe = Cafe.createCafe(cafeSaveDto.getName(), cafeSaveDto.getContent(),
                cafeSaveDto.getTelNumber(), cafeSaveDto.getLongitude(), cafeSaveDto.getLatitude(),
                cafeSaveDto.getDong(), member);


        cafeRepository.save(cafe);


        return cafe.getId();
    }

    @Override
    public CafeDto getCafe(Long cafeId) {

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 카페가 없습니다."));

        return CafeDto.fromEntity(cafe);
    }

    @Override
    public boolean updateCafe(CafeUpdateDto cafeUpdateDto) {
        try {
            // 카페 정보 찾기
            Cafe cafe = cafeRepository.findById(cafeUpdateDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("등록된 카페를 찾을 수 없습니다."));

            // 카페 정보 업데이트
            cafe.updateCafe(cafeUpdateDto);

            // 변경된 내용을 저장
            cafeRepository.save(cafe);

            // 성공적으로 업데이트되었으므로 true 반환
            return true;
        } catch (IllegalArgumentException e) {
            // 등록된 카페를 찾을 수 없는 경우
            e.printStackTrace(); // 또는 원하는 다른 처리를 수행

            // 실패한 경우 false 반환
            return false;
        }
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
    public boolean updateCafeMenu(CafeMenuUpdateDto cafeMenuUpdateDto) {

        try {
            // CafeMenu 가져오기
            CafeMenu cafeMenu = cafeMenuRepository.findById(cafeMenuUpdateDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            cafeMenu.updateCafeMenu(cafeMenuUpdateDto.getName(), cafeMenuUpdateDto.getContent(), cafeMenuUpdateDto.getPrice());

            return true;
        } catch (IllegalArgumentException e) {
            // 메뉴를 찾을 수 없는 경우
            return false;
        } catch (Exception e) {
            // 그 외 예외 처리
            return false;
        }
    }


}
