package com.stamfee.stamfee.cafe;

import com.stamfee.stamfee.dto.cafe.*;
import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.service.cafe.CafeService;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CafeApplicationTests {

    @Autowired
    MemberService memberService;

    @Autowired
    CafeService cafeService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 카페_등록() throws Exception{
        //given
        String nickname ="test01";
        String password = "1234";
        String cellPhone = "01012341234";

        Member member = new Member();
        member.setCellphone(cellPhone);
        member.setNickname(nickname);
        member.setPassword(password);

        memberRepository.save(member);

        CafeSaveDto cafeSaveDto = new CafeSaveDto();

        cafeSaveDto.setName("카페명");
        cafeSaveDto.setContent("카페소개");
        cafeSaveDto.setLongitude(37.533948);
        cafeSaveDto.setLongitude(126.9010796);
        cafeSaveDto.setDong("영등포동");


        //when
        Long cafeId = cafeService.addCafe(member.getCellphone(), cafeSaveDto);
        CafeDto findCafeDto = cafeService.getCafe(cafeId);

        //then
        Assertions.assertThat(cafeId).isEqualTo(findCafeDto.getId());
        Assertions.assertThat(cafeSaveDto.getContent()).isEqualTo(findCafeDto.getContent());

    }

    @Test
    public void 카페_수정() throws Exception{
        //given
        String nickname ="test01";
        String password = "1234";
        String cellPhone = "01012341234";

        Member member = new Member();
        member.setCellphone(cellPhone);
        member.setNickname(nickname);
        member.setPassword(password);

        memberRepository.save(member);

        CafeSaveDto cafeSaveDto = new CafeSaveDto();

        cafeSaveDto.setName("카페명");
        cafeSaveDto.setContent("카페소개");
        cafeSaveDto.setLongitude(37.533948);
        cafeSaveDto.setLongitude(126.9010796);
        cafeSaveDto.setDong("영등포동");

        Long cafeId = cafeService.addCafe(member.getCellphone(), cafeSaveDto);

        CafeUpdateDto cafeUpdateDto = new CafeUpdateDto();
        cafeUpdateDto.setId(cafeId);
        cafeUpdateDto.setContent("내용수정");


        //when
        cafeService.updateCafe(cafeUpdateDto);

        //then
        Assertions.assertThat(cafeUpdateDto.getContent()).isEqualTo("내용수정");
    }

    @Test
    public void 카페_리스트() throws Exception{
        //given

        //when

        //then
    }

    @Test
    public void 카페_메뉴_등록() throws Exception{
        //given
        String nickname ="test01";
        String password = "1234";
        String cellPhone = "01012341234";

        Member member = new Member();
        member.setCellphone(cellPhone);
        member.setNickname(nickname);
        member.setPassword(password);

        memberRepository.save(member);

        CafeSaveDto cafeSaveDto = new CafeSaveDto();

        cafeSaveDto.setName("카페명");
        cafeSaveDto.setContent("카페소개");
        cafeSaveDto.setLongitude(37.533948);
        cafeSaveDto.setLongitude(126.9010796);
        cafeSaveDto.setDong("영등포동");

        Long cafeId = cafeService.addCafe(member.getCellphone(), cafeSaveDto);

        CafeMenuSaveDto cafeMenuSaveDto = new CafeMenuSaveDto();
        cafeMenuSaveDto.setCafeId(cafeId);
        cafeMenuSaveDto.setName("메뉴1");
        cafeMenuSaveDto.setPrice(10000);
        cafeMenuSaveDto.setContent("");

        //when
        Long l = cafeService.addCafeMenu("01012341234", cafeMenuSaveDto);
        CafeMenuDto findCafeMenu = cafeService.getCafeMenu(cafeId);

        //then
        Assertions.assertThat(findCafeMenu.getName()).isEqualTo(cafeMenuSaveDto.getName());
    }

    @Test
    public void 카페_메뉴_리스트_및_개별조회() throws Exception{
        //given
        String nickname ="test01";
        String password = "1234";
        String cellPhone = "01012341234";

        Member member = new Member();
        member.setCellphone(cellPhone);
        member.setNickname(nickname);
        member.setPassword(password);

        memberRepository.save(member);

        CafeSaveDto cafeSaveDto = new CafeSaveDto();

        cafeSaveDto.setName("카페명");
        cafeSaveDto.setContent("카페소개");
        cafeSaveDto.setLongitude(37.533948);
        cafeSaveDto.setLongitude(126.9010796);
        cafeSaveDto.setDong("영등포동");

        Long cafeId = cafeService.addCafe(member.getCellphone(), cafeSaveDto);

        CafeMenuSaveDto cafeMenuSaveDto = new CafeMenuSaveDto();
        cafeMenuSaveDto.setCafeId(cafeId);
        cafeMenuSaveDto.setName("메뉴1");
        cafeMenuSaveDto.setPrice(10000);
        cafeMenuSaveDto.setContent("");
        cafeService.addCafeMenu("01012341234", cafeMenuSaveDto);

        //when
        List<CafeMenuListDto> cafeMenuListDto = cafeService.listCafeMenu(cafeId);
        CafeMenuDto findCafeMenu = cafeService.getCafeMenu(cafeMenuListDto.get(0).getId());

        //then
        Assertions.assertThat(cafeMenuListDto.size()).isEqualTo(1);
        Assertions.assertThat(cafeMenuSaveDto.getName()).isEqualTo(findCafeMenu.getName());
    }

    @Test
    public void 카페_메뉴_수정() throws Exception{
        //given

        //when

        //then
    }



}
