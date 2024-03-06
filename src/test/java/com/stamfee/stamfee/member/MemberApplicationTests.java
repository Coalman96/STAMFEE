package com.stamfee.stamfee.member;


import com.stamfee.stamfee.common.Role;
import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
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
//
//  @DisplayName("동네설정 테스트")
////  @Test
//  public void testGetDongNe() throws Exception {
//    String tag = "#4FzB";
//
//    // 실제 현재 위치 좌표는 Js의 geolocation 사용 예정
//    double x = 127.0292881;
//    double y = 37.4923615;
//
////    double x = 126.8110081;
////    double y = 37.5599801;
//
//    GpsDTO gpsDTO = new GpsDTO();
//    gpsDTO.setX(x);
//    gpsDTO.setY(y);
//
//    memberService.addDongNe(tag, gpsDTO);
//
//  }
//
//  @DisplayName("내프로필조회 테스트")
////  @Test
//  public void testGetMember() throws Exception{
//    String tag="#4FzB";
//    MemberDTO memberDTO = memberService.getMember(tag);
//    System.out.println(memberDTO);
//  }
//
//  @DisplayName("상대프로필조회 테스트")
////  @Test
//  public void testGetOtherMember() throws Exception{
//    String tag="#4FzB";
//    MemberDTO memberDTO = memberService.getOtherMember(tag);
//    System.out.println(memberDTO);
//  }
//
//  @DisplayName("회원목록조회 테스트")
////  @Test
//  public void testGetMemberList() throws Exception{
//    SearchDTO searchDTO = SearchDTO.builder()
//        .currentPage(0)
//        .searchKeyword("엄")
//        .pageSize(pageSize)
//        .build();
//
//    List<MemberDTO> memberDTOList = memberService.getMemberList(searchDTO);
//    System.out.println(memberDTOList);
//
//  }
//
//  @DisplayName("정지회원 등록 테스트")
////  @Test
//  public void testAddBlockMember() throws Exception{
//    String tag = "#4FzB";
//    BlockDTO blockDTO = BlockDTO.builder()
//        .id(1L)
//        .blockReason("걍 띠꺼움")
//        .blockEnd(LocalDateTime.now().plusDays(7))
//        .blockDay(7L)
//        .build();
//
//    memberService.addBlockMember(blockDTO, tag);
//    System.out.println(memberService.getMember(tag));
//  }
//
//  @DisplayName("정지회원 삭제 테스트")
////  @Test
//  public void testDeleteBlockMember() throws Exception{
//    String tag = "#4FzB";
//    memberService.deleteBlockMember(tag);
//    System.out.println(memberService.getMember(tag));
//  }
//
//  @DisplayName("회원 비활성화, 비활성화 해제 테스트")
////  @Test
//  public void testHandleMemberActivate() throws Exception{
//   String tag = "#4FzB";
//   memberService.handleMemberActivate(tag);
//
// }
//
//  @DisplayName("회원 비활성화, 비활성화 해제 테스트")
////  @Test
//  public void testHandleNadeuliDelivery() throws Exception{
//    String tag = "#4FzB";
//    memberService.handleNadeuliDelivery(tag);
//
//  }
//
//
//  @DisplayName("회원 수정 테스트")
////  @Test
//  public void testUpdateMemeber() throws Exception{
//    MemberDTO memberDTO = MemberDTO.builder()
//        .tag("#4FzB")
//        .picture("1234.jpg")
//        .nickname("롤로노아 김동헌")
//        .cellphone("01088888888")
//        .email("sex@gmail.com")
//        .build();
//
//    memberService.updateMember(memberDTO);
//  }
//
//  @DisplayName("즐겨찾기 추가 테스트")
////  @Test
//  public void testaddFavorite() throws Exception{
//    String tag = "#4FzB";
//    Long productId = 6L;
//
//    memberService.addFavorite(tag, productId);
//  }
//
//  @DisplayName("즐겨찾기 삭제 테스트")
////  @Test
//  public void testDeleteFavorite() throws Exception{
//    String tag = "#4FzB";
//    Long productId = 5L;
//
//    memberService.deleteFavorite(tag, productId);
//  }
//
//  @DisplayName("즐겨찾기 목록 조회 테스트")
////  @Test
//  public void testGetFavoriteList() throws Exception{
//    SearchDTO searchDTO = new SearchDTO();
//    searchDTO.setCurrentPage(0);
//    searchDTO.setPageSize(pageSize);
//    searchDTO.setSearchKeyword("");
//    List<OriScheMemChatFavDTO> oriScheMemChatFavDTOList = memberService.getFavoriteList("#4FzB", searchDTO);
//    System.out.println(oriScheMemChatFavDTOList);
//
//  }
//
//  @DisplayName("친화력 툴팁 테스트")
////  @Test
//  public void testGetAffinityToolTip() throws Exception {
//    System.out.println(memberService.getAffinityToolTip());
//
//  }
//
//  @DisplayName("신고 테스트")
////  @Test
//  public void testReport() throws Exception{
//    MemberDTO memberDTO = memberService.getMember("#4FzB");
//    ProductDTO productDTO = productService.getProduct(5L);
//    ReportDTO reportDTO = ReportDTO.builder()
//        .reportId(1L)
//        .content("걍 개띠꺼움ddd     ")
//        .reporter(memberDTO)
//        .product(productDTO)
//        .build();
//
//    memberService.addReport(reportDTO);
//
//  }
//
//  @DisplayName("나드리페이 계산 테스트")
////  @Test
//  public void testHandleNadeuliPayBalance() throws Exception{
//    NadeuliPayHistoryDTO nadeuliPayHistoryDTO = NadeuliPayHistoryDTO.builder()
//        .nadeuliPayHistoryId(1L)
//        .tradingMoney(10000L)
////        .tradingMoney(4000L)
////        .tradeType(TradeType.CHARGE)
////        .tradeType(TradeType.PAYMENT)
//        .tradeType(TradeType.WITHDRAW)
//        .build();
//
//    NadeuliDeliveryDTO nadeuliDeliveryDTO = NadeuliDeliveryDTO.builder()
//        .nadeuliDeliveryId(1L)
//        .deposit(10001L)
//        .deliveryState(DeliveryState.CANCEL_DELIVERY)
//        .deliveryState(DeliveryState.DELIVERY_ORDER)
//        .build();
//    String tag = "#1qZL";
//
////    memberService.handleNadeuliPayBalance(tag,null,nadeuliDeliveryDTO);
//    memberService.handleNadeuliPayBalance(tag,nadeuliPayHistoryDTO,null,null);
//
//  }
//
//  @DisplayName("친화력 점수 반영 테스트")
////  @Test
//  public void testUpdateAffinity() throws Exception{
//    String tag = "Kv4G";
//    Long affinityScore = 5L;
//    Long affinityScore1 = -5L;
//
//    memberService.updateAffinity(tag);
//
//  }
}

