package com.stamfee.stamfee.auth;


import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.service.auth.AuthRepository;
import com.stamfee.stamfee.service.auth.AuthService;
import com.stamfee.stamfee.service.auth.impl.AuthServiceImpl;
import com.stamfee.stamfee.service.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthApplicationTests {

  /*
  * @Mock과 @InjectMocks의 차이점
  * 1. Mock은 기본적으로 가짜객체(클래스)임
  * 2. Repository, Mapper 와 같이 실제 클래스가아닌 것을 필요로할 때 적용
  * 3. InjectMocks는 Mock에 쓰일 클래스를 지정
  * 4. 즉, @Mock에 @InjectMocks를 주입함
  * */
  @Autowired // 유료서비스라 Mock객체로 주입하기 힘들기떄문에 Autowired로 변경
  AuthServiceImpl authService;

  @Autowired // 실제 authService를 사용했기때문에 Mock이아닌 Autowired로 변경
  AuthRepository authRepository;

  @Mock
  MemberRepository memberRepository;

  @Mock
  MemberMapper memberMapper;

  /*
  * 기본 BDD 구조
  * 1. given -> 특정 상황이나 조건 설정
  *  - 값 설정
  * 2. when -> 테스트 대상의 동작 수행
  *  - 메소드 실행
  * 3. then -> 결과 검증
  *  - when에서 나온 결과가 맞는지 체크
  * */
  @DisplayName("휴대폰 인증 번호 전송 테스트")
//  @Test
  public void testSendSms() throws Exception{
    // given
    String phoneNumber = "01086258914";
    // DTO 생성
    AuthDTO authDTO = AuthDTO.builder()
        .to(phoneNumber)
        .build();

    BDDMockito.given(memberRepository.findByCellphone(authDTO.getTo())).willReturn(
        Optional.empty());

    //when
    //랜덤한 5자리 인증번호를 phoneNumber로 전송
    boolean sendSms = authService.sendSms(authDTO);

    //then
    //인증번호 전송 완료 시 true
    Assertions.assertTrue(sendSms);

  }

  @DisplayName("비밀번호 찾기 테스트")
//  @Test
  public void testFindAccount() throws Exception{
    // given
    String phoneNumber = "01086258914";
    // DTO 생성
    AuthDTO authDTO = AuthDTO.builder()
        .to(phoneNumber)
        .build();
    //멤버 객체 생성
    Member member = Member.builder()
        .cellphone(phoneNumber)
        .build();

    BDDMockito.given(memberRepository.findByCellphone(authDTO.getTo())).willReturn(
        Optional.of(member));

    //when
    //랜덤한 5자리 인증번호를 phoneNumber로 전송
    boolean result = authService.findAccount(authDTO);

    //then
    //인증번호 전송 완료 시 true
    Assertions.assertTrue(result);

  }


  @DisplayName("인증 번호 확인 테스트")
//  @Test
  public void testVerifySms() throws Exception{
    //given
    String phoneNumber = "01086258914";
    //인증번호는 실제 핸드폰번호에 찍힌 번호 확인
    String authNumber = "22222";
    AuthDTO authDTO = AuthDTO.builder()
        .to(phoneNumber)
        .authNumber(authNumber)
        .build();

    //when
    //verifySms 메소드 실행시 인증번호 확인 후 삭제
    boolean verify = authService.verifySms(authDTO);

    //then
    //인증번호 확인 시 true
    Assertions.assertTrue(verify);

  }

}
