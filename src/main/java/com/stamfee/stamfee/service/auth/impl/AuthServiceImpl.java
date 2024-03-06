package com.stamfee.stamfee.service.auth.impl;

import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.service.auth.AuthRepository;
import com.stamfee.stamfee.service.auth.AuthService;
import com.stamfee.stamfee.service.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class AuthServiceImpl implements AuthService {

  @Value("${coolsms.api.key}")
  private String apiKey;
  @Value("${coolsms.api.secret}")
  private String apiSecretKey;
  @Value("${coolsms.sender.number}")
  private String senderNumber;
  @Value("${sender.text}")
  private String messageTest;

  private final AuthRepository smsCertification;

  private final MemberRepository memberRepository;
  private DefaultMessageService messageService;

  private final AuthRepository smsCertificationRepository;

  @PostConstruct
  private void init(){
    this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
  }

  // 단일 메시지 발송 예제
  @Override
  public boolean sendSms(AuthDTO authDTO) {
    Message message = new Message();
    authDTO.setAuthNumber(generateRandomCode(5));
    // 발신번호 및 수신번호는 반드시 01012345678 형태.
    message.setFrom(senderNumber);
    message.setTo(authDTO.getTo());
    message.setText(messageTest +" "+ authDTO.getAuthNumber());

    try {
      if(memberRepository.findByCellphone(authDTO.getTo()).isEmpty()){

        this.messageService.sendOne(new SingleMessageSendingRequest(message));

        // DB에 발송한 인증번호 저장
        smsCertification.addAuthNum(authDTO);
        return true;
      }else{
        System.out.println("이미 존재하는 회원입니다.");
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean findAccount(AuthDTO authDTO) {
    Message message = new Message();
    authDTO.setAuthNumber(generateRandomCode(5));
    // 발신번호 및 수신번호는 반드시 01012345678 형태.
    message.setFrom(senderNumber);
    message.setTo(authDTO.getTo());
    message.setText(messageTest +" "+ authDTO.getAuthNumber());

    try {
      if(memberRepository.findByCellphone(authDTO.getTo()).isPresent()){

        this.messageService.sendOne(new SingleMessageSendingRequest(message));

        // DB에 발송한 인증번호 저장
        smsCertification.addAuthNum(authDTO);

        return true;
      }else{
        System.out.println("없는 회원입니다.");
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private String generateRandomCode(int length) {
    StringBuilder code = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      code.append(random.nextInt(10)); // 0부터 9까지의 랜덤한 숫자
    }
    return code.toString();
  }

  //사용자가 입력한 인증번호가 Redis에 저장된 인증번호와 동일한지 확인
  @Override
  public boolean verifySms(AuthDTO authDTO) throws Exception {
    if (!(smsCertificationRepository.isAuthNum(authDTO.getTo()) &&
        smsCertificationRepository.getAuthNum(authDTO.getTo())
            .equals(authDTO.getAuthNumber()))) {
      return false;
    }

    smsCertificationRepository.deleteAuthNum(authDTO.getTo());

    return true;
  }

}