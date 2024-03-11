package com.stamfee.stamfee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.dto.TokenDTO;
import com.stamfee.stamfee.service.image.ImageService;
import com.stamfee.stamfee.service.jwt.JWTService;
import com.stamfee.stamfee.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/stamfee/member")
@RequiredArgsConstructor
@Log4j2
public class MemberRestController {

  private final MemberService memberService;

  private final JWTService jwtService;

  private final ImageService imageService;

  @Value("${pageSize}")
  private int pageSize;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    log.info("/member/logout : GET");
    // SecurityContext에서 Authentication을 가져와서 로그아웃
    SecurityContextHolder.clearContext();

    // 쿠키 삭제
    Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    // 헤더에서도 삭제
    response.setHeader(HttpHeaders.AUTHORIZATION, "");

    return "{\"success\": true}";
  }

  @GetMapping("/getMember/{cellphone}")
  public ResponseEntity<MemberDTO> getMember(@PathVariable String cellphone) throws Exception{
    log.info("/member/getMember/{} : GET : ",cellphone);

    MemberDTO existMember = memberService.getMember(cellphone);

    if(existMember !=null)
      return ResponseEntity.ok(existMember);
    else
      return ResponseEntity.badRequest().build();
  }

  @GetMapping("/getOtherMember/{nickname}")
  public ResponseEntity<MemberDTO> getOtherMember(@PathVariable String nickname) throws Exception{
    log.info("/member/getOtherMember/{} : GET : ",nickname);

    MemberDTO otherMember = memberService.getOtherMember(nickname);

    return otherMember != null ? ResponseEntity.ok(otherMember) : ResponseEntity.badRequest().build();

  }

  @PostMapping("/getMemberList")
  public ResponseEntity<List<MemberDTO>> getMemberList(@RequestBody Map<String, Object> requestData) throws Exception {
    log.info("/member/getMemberList : POST : {}",requestData);
    SearchDTO searchDTO = objectMapper.convertValue(requestData.get("searchDTO"), SearchDTO.class);
    searchDTO.setPageSize(1000);
    log.info("멤버리스트는{}",memberService.getMemberList(searchDTO));

    List<MemberDTO> memberDTOList = memberService.getMemberList(searchDTO);

    return memberDTOList != null ? ResponseEntity.ok(memberDTOList) : ResponseEntity.badRequest().build();
  }
  @PostMapping("/updateNickname")
  public ResponseEntity<MemberDTO> updateNickname(@RequestBody MemberDTO memberDTO) throws Exception{
    log.info("/member/updateMember : POST : {}", memberDTO);
    boolean result = memberService.updateNickname(memberDTO);

    return result ? ResponseEntity.ok(memberService.getMember(memberDTO.getCellphone())) : ResponseEntity.badRequest().build();

  }

  @GetMapping("/checkNickname/{nickname}")
  public ResponseEntity<String> checkNickname(@PathVariable String nickname) throws Exception{
    log.info("/member/checkNickname/{} : GET : ",nickname);

    boolean result = memberService.checkNickname(nickname);

    return result ? ResponseEntity.ok("{\"success\": true}") : ResponseEntity.badRequest().body("{\"success\": false}");

  }

  @PostMapping("/updateProfile")
  public ResponseEntity<MemberDTO> updateProfile(@ModelAttribute MemberDTO memberDTO,@RequestParam("image") MultipartFile image) throws Exception {
    log.info("/member/updateProfile : POST : {}", memberDTO);
    String fileName = memberDTO.getPicture();
    imageService.deleteProfile(fileName);

    // 이미지 업로드 및 저장
    imageService.addProfile(image, memberDTO);

    MemberDTO updateMember = memberService.getMember(memberDTO.getCellphone());

    return updateMember != null ? ResponseEntity.ok(updateMember) : ResponseEntity.badRequest().build();
  }

}
