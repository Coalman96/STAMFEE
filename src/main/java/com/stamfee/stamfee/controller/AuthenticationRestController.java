package com.stamfee.stamfee.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.TokenDTO;
import com.stamfee.stamfee.service.jwt.AuthenticationService;
import com.stamfee.stamfee.service.member.MemberService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stamfee")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationRestController {
  //1. Jwt 토큰 발급을 위한 RestController
  private final AuthenticationService authenticationService;

  private final MemberService memberService;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/addMember")
  public ResponseEntity<TokenDTO> addMember(@RequestBody MemberDTO memberDTO) throws Exception {
    log.info("/member/login : POST");
    log.info("addMember에서 받은 memberDTO는 {}", memberDTO);

    TokenDTO tokenDTO = authenticationService.addMember(memberDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());

    return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO) throws Exception {
    log.info("/member/login : POST");
    log.info("login에서 받은 memberDTO은 {}", memberDTO);

    TokenDTO tokenDTO = authenticationService.login(memberDTO);
    if(tokenDTO.getAccessToken() != null){
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());
      return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
    }else{
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
    }

  }


  @PostMapping("/refresh")
  public TokenDTO refresh(@RequestBody TokenDTO refreshTokenDTO) throws Exception {

    log.info("/api/v1/auth/json/refresh : POST");
    log.info("refresh에서 받은 tokenDTO는 {}",refreshTokenDTO);

    return authenticationService.refreshToken(refreshTokenDTO);
  }

}
