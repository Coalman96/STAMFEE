package com.stamfee.stamfee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stamfee.stamfee.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberRestController {

  private final MemberService memberService;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/sayHello")
  public ResponseEntity<String> sayHello(){
    return ResponseEntity.ok("반갑습니다 관리자님");
  }




}
