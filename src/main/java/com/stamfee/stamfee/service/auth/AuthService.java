package com.stamfee.stamfee.service.auth;

import com.stamfee.stamfee.dto.AuthDTO;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface AuthService {

  public SingleMessageSentResponse sendSms(AuthDTO authDTO);
//  public void sendOne(String cellphone);

  public boolean verifySms(AuthDTO authDTO) throws Exception;


}
