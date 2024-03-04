package com.stamfee.stamfee.service.jwt;

import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.TokenDTO;

public interface AuthenticationService {

  public TokenDTO addMember(MemberDTO memberDTO) throws Exception;

  public TokenDTO login(MemberDTO memberDTO) throws Exception;

  public TokenDTO accessToken(MemberDTO memberDTO) throws Exception ;

  public TokenDTO refreshToken(TokenDTO refreshTokenDTO) throws Exception ;


}
