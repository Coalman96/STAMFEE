package com.stamfee.stamfee.controller;

import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
