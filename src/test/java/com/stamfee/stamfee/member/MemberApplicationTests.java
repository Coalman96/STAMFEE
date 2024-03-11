package com.stamfee.stamfee.member;


import com.stamfee.stamfee.common.Role;
import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.dto.TokenDTO;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.mapper.MemberMapperImpl;
import com.stamfee.stamfee.service.auth.AuthRepository;
import com.stamfee.stamfee.service.auth.AuthService;
import com.stamfee.stamfee.service.auth.impl.AuthServiceImpl;
import com.stamfee.stamfee.service.jwt.AuthenticationService;
import com.stamfee.stamfee.service.jwt.impl.AuthenticationServiceImpl;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import com.stamfee.stamfee.service.member.impl.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberApplicationTests {

  /*
  * 1. 서비스에 @Mock을 사용해야하나 mapper, jwt 문제발생으로 Autowired사용
  * */
  @Autowired
  MemberService memberService;

  @Mock
  MemberRepository memberRepository;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  MemberMapper memberMapper;

  @Autowired
  AuthService authService;

  @Mock
  AuthRepository authRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // 필드 주입을 위한 설정
  }


  @Value("${pageSize}")
  private int pageSize;

  @DisplayName("회원가입 테스트 / 중복회원이 아닐경우")
//  @Test
  public void testAddMember() throws Exception {
    //given
    String nickname = "NotPussyUm";
    String password = "1234!!";
    String cellPhone = "01034431643";
    //member 객체 생성
    MemberDTO memberDTO = MemberDTO.builder()
        .cellphone(cellPhone)
        .nickname(nickname)
        .password(password)
        .isActivate(false)
        .build();

    //가짜 Repository 설정
    //휴대폰번호가 중복이아닐경우
    /*
    * 구현로직에 검증단계가있음에도 경우의 수를 설정하는 이유
    *  -> 테스트시에 외부 의존성을 제어할 수 없는 경우가 존재하기때문.
    *  -> 여기서 외부 의존성이란 DB, 다른 서비스를 뜻함.
    *  -> 구현로직에 사용되는 메소드는 가능한 테스트코드에서 설정하는게 좋다.
    *  -> 하지만 지금은 맛만보는것이기때문에 느낌만 알고가겠다.
    * */

    BDDMockito.given(memberRepository.findByCellphone(memberDTO.getCellphone())).willReturn(
        Optional.empty());

    //닉네임이 중복이 아닐경우
    BDDMockito.given(memberRepository.findByNickname(memberDTO.getNickname())).willReturn(
        Optional.empty());

    //when
    TokenDTO result = authenticationService.addMember(memberDTO);

    //then
    Assertions.assertNotNull(result);
  }

  @DisplayName("회원가입 테스트 / 중복회원일경우") // 테스트 메소드는 상황별 분기
//  @Test
  public void testAddMemberDuplicated() throws Exception {
    //given
    String nickname = "NotPussyUm";
    String password = "1234!!";
    String cellPhone = "01034431643";

    //member 객체 생성
    MemberDTO memberDTO = MemberDTO.builder()
        .cellphone(cellPhone)
        .nickname(nickname)
        .password(password)
        .isActivate(false)
        .build();

    //가짜 Repository 설정
    //휴대폰번호가 중복일경우
    BDDMockito.given(memberRepository.findByCellphone(memberDTO.getCellphone())).willReturn(
        Optional.of(memberMapper.memberDTOToMember(memberDTO)));

    //닉네임이 중복일경우
    BDDMockito.given(memberRepository.findByNickname(memberDTO.getNickname())).willReturn(
        Optional.of(memberMapper.memberDTOToMember(memberDTO)));

    //when
    TokenDTO result = authenticationService.addMember(memberDTO);

    //then
    Assertions.assertNull(result);
  }

  @DisplayName("로그인 성공")
//    @Test
  public void testLogin() throws Exception {
    //given
    //입력받은 휴대폰번호와 비밀번호 객체 생성
    String cellphone = "01034431643";
    String password = "1234!!";

    MemberDTO memberDTO = MemberDTO.builder()
        .cellphone(cellphone)
        .password(password)
        .build();

    //회원이 존재할 경우
    Member member = Member.builder()
        .cellphone(cellphone)
        .password(passwordEncoder.encode(memberDTO.getPassword()))
        .build();

    //가짜 Repository 설정
    BDDMockito.given(memberRepository.findByCellphone(memberDTO.getCellphone())).willReturn(
        Optional.of(member));

    //비밀번호가 일치할 경우
    BDDMockito.given(passwordEncoder.matches(memberDTO.getPassword(), member.getPassword())).willReturn(true);

    //when
    TokenDTO result = authenticationService.login(memberDTO);

    //then
    Assertions.assertNotNull(result.getAccessToken());

  }

  @DisplayName("로그인 실패")
//  @Test
  public void testLoginFailed() throws Exception {
    //given
    //입력받은 휴대폰번호와 비밀번호 객체 생성
    String cellphone = "01034431643";
    String password = "1234!!!";

    MemberDTO memberDTO = MemberDTO.builder()
        .cellphone(cellphone)
        .password(password)
        .build();

    //회원이 존재할 경우
    Member member = Member.builder()
        .cellphone(cellphone)
        .password(passwordEncoder.encode(memberDTO.getPassword()))
        .build();

    //가짜 Repository 설정
    BDDMockito.given(memberRepository.findByCellphone(memberDTO.getCellphone())).willReturn(
        Optional.of(member));

    //비밀번호가 일치할 경우
    BDDMockito.given(passwordEncoder.matches(memberDTO.getPassword(), member.getPassword())).willReturn(false);

    //when
    TokenDTO result = authenticationService.login(memberDTO);

    //then
    Assertions.assertNull(result.getAccessToken());

  }

  @DisplayName("내프로필조회 테스트")
//  @Test
  public void testGetMember() throws Exception{
    //given
    String cellPhone = "01034431643";

    //회원객체 생성
    Member existMember = Member.builder()
        .cellphone(cellPhone)
        .build();

    MemberDTO expectedMemberDTO = MemberDTO.builder()
        .cellphone(cellPhone)
        .build();

    BDDMockito.given(memberRepository.findByCellphone(cellPhone)).willReturn(Optional.of(existMember));
    BDDMockito.given(memberMapper.memberToMemberDTO(existMember)).willReturn(expectedMemberDTO);

    //when
    MemberDTO memberDTO = memberService.getMember(cellPhone);
    System.out.println(memberDTO);

    //then
    Assertions.assertNotNull(memberDTO);
    Assertions.assertEquals(memberDTO.getNickname(),expectedMemberDTO.getNickname());
  }

  @DisplayName("상대프로필조회 테스트")
//  @Test
  public void testGetOtherMember() throws Exception{
    //given
    String nickname="NotPussyUm";

    //회원객체 생성
    Member existMember = Member.builder()
        .nickname(nickname)
        .build();

    MemberDTO expectedMemberDTO = MemberDTO.builder()
        .nickname(nickname)
        .build();

    BDDMockito.given(memberRepository.findByNickname(nickname)).willReturn(Optional.of(existMember));
    BDDMockito.given(memberMapper.memberToMemberDTO(existMember)).willReturn(expectedMemberDTO);

    //when
    MemberDTO memberDTO = memberService.getOtherMember(nickname);
    System.out.println(memberDTO);

    //then
    Assertions.assertNotNull(memberDTO);
    Assertions.assertEquals(memberDTO.getNickname(),expectedMemberDTO.getNickname());
  }

  @DisplayName("회원목록조회 테스트")
//  @Test
  public void testGetMemberList() throws Exception{
    //given
    SearchDTO searchDTO = SearchDTO.builder()
        .currentPage(0)
        .searchKeyword("")
        .pageSize(pageSize)
        .build();

    Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());

    BDDMockito.given(memberRepository.findAll(pageable));

    //when
    List<MemberDTO> memberDTOList = memberService.getMemberList(searchDTO);
    System.out.println(memberDTOList);

    //then
    Assertions.assertNotNull(memberDTOList);

  }
  @DisplayName("회원 닉네임 수정 테스트")
//  @Test
  public void testUpdateNickname() throws Exception{
    //given
    String updateNickname = "PussyUm";
    String cellphone = "01034431643";

    //회원객체 생성
    Member existMember = Member.builder()
        .cellphone(cellphone)
        .nickname("NotPussyUm")
        .build();

    MemberDTO existMemberDTO = MemberDTO.builder()
        .cellphone(cellphone)
        .nickname("NotPussyUm")
        .build();

    MemberDTO updateMember = MemberDTO.builder()
        .cellphone(cellphone)
        .nickname(updateNickname)
        .build();

    BDDMockito.given(memberRepository.findByNickname(updateMember.getNickname())).willReturn(null);
    BDDMockito.given(memberMapper.memberToMemberDTO(existMember)).willReturn(existMemberDTO);

    //when
    boolean result = memberService.updateNickname(updateMember);

    //then
    Assertions.assertTrue(result);
  }

  @DisplayName("회원 닉네임 중복 체크 테스트")
//  @Test
  public void testCheckNickname() throws Exception{
    //given
    String updateNickname = "PussyUm123";

    BDDMockito.given(memberRepository.findByNickname(updateNickname)).willReturn(null);

    //when
    boolean result = memberService.checkNickname(updateNickname);

    //then
    Assertions.assertFalse(result);
  }


}

