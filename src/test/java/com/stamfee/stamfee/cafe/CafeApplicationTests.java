package com.stamfee.stamfee.cafe;

import com.stamfee.stamfee.dto.cafe.CafeDto;
import com.stamfee.stamfee.dto.cafe.CafeSaveDto;
import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.service.cafe.CafeService;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
