package com.stamfee.stamfee.service.auth;

import com.stamfee.stamfee.dto.AuthDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface AuthService {

  public boolean sendSms(AuthDTO authDTO);
//  public void sendOne(String cellphone);

  public boolean findAccount(AuthDTO authDTO);


  public boolean verifySms(AuthDTO authDTO) throws Exception;

  MemberDTO getAuthMember() throws Exception;


}
