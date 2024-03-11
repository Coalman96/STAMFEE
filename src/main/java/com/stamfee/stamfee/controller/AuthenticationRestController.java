package com.stamfee.stamfee.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.TokenDTO;
import com.stamfee.stamfee.service.auth.AuthService;
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
  private final AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/addMember")
  public ResponseEntity<?> addMember(@RequestBody MemberDTO memberDTO) throws Exception {
    log.info("/member/login : POST");
    log.info("addMember에서 받은 memberDTO는 {}", memberDTO);

    TokenDTO tokenDTO = authenticationService.addMember(memberDTO);

    if(tokenDTO != null){
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());
      headers.add("RefreshToken", "Bearer " + tokenDTO.getRefreshToken());
      return new ResponseEntity<>("{\"success\": true}", headers, HttpStatus.OK);
    }else{

      return new ResponseEntity<>("{\"success\": false}",HttpStatus.BAD_REQUEST);
    }



  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO) throws Exception {
    log.info("/member/login : POST");
    log.info("login에서 받은 memberDTO은 {}", memberDTO);

    TokenDTO tokenDTO = authenticationService.login(memberDTO);
    if(tokenDTO.getAccessToken() != null){
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());
      headers.add("RefreshToken", "Bearer " + tokenDTO.getRefreshToken());
      return new ResponseEntity<>("{\"success\": true}", headers, HttpStatus.OK);
    }else{
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 틀렸습니다.");
    }

  }


  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@RequestBody TokenDTO tokenDTO) throws Exception {

    log.info("/api/v1/auth/json/refresh : POST");
    log.info("refresh에서 받은 tokenDTO는 {}",tokenDTO);
    if(tokenDTO.getAccessToken() != null){
      TokenDTO newToken = authenticationService.refreshToken(tokenDTO);
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + newToken.getAccessToken());
      headers.add("RefreshToken", "Bearer " + newToken.getRefreshToken());
      return new ResponseEntity<>("{\"success\": true}", headers, HttpStatus.OK);}
    else{

      return new ResponseEntity<>("{\"success\": false}",HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/sendSms")
  public ResponseEntity<?> sendSms(@RequestBody AuthDTO authDTO) throws Exception{
    log.info("/sms/sendSms : POST : {}", authDTO);
    return authService.sendSms(authDTO) ? new ResponseEntity<>("{\"success\": true}", HttpStatus.OK) :
        new ResponseEntity<>("{\"success\": false}",HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/findAccount")
  public ResponseEntity<?> findAccount(@RequestBody AuthDTO authDTO) throws Exception{
    log.info("/sms/findAccount : POST : {}", authDTO);
    return authService.findAccount(authDTO) ? new ResponseEntity<>("{\"success\": true}", HttpStatus.OK) :
        new ResponseEntity<>("{\"success\": false}",HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/verifySms")
  public ResponseEntity<?> verifySms(@RequestBody AuthDTO authDTO) throws Exception{
    log.info("/member/verifySms : POST : {}", authDTO);
    return authService.verifySms(authDTO) ? new ResponseEntity<>("{\"success\": true}", HttpStatus.OK) :
        new ResponseEntity<>("{\"success\": false}",HttpStatus.BAD_REQUEST);

  }

}
