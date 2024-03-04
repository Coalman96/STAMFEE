package com.stamfee.stamfee.controller;

import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Log4j2
public class AuthRestController {

  @Autowired
  private final AuthService authService;
  @PostMapping("/sendSms")
  public String sendSms(@RequestBody AuthDTO authDTO) throws Exception{
    log.info("/member/sendOne : POST : {}", authDTO);
    authService.sendSms(authDTO);

    return "{\"success\": true}";
  }

  @PostMapping("/verifySms")
  public String verifySms(@RequestBody AuthDTO authDTO) throws Exception{
    log.info("/member/verifySms : POST : {}", authDTO);
    authService.verifySms(authDTO);

    return "{\"success\": true}";
  }

}
