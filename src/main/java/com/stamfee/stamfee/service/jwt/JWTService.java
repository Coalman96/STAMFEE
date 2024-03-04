package com.stamfee.stamfee.service.jwt;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;


public interface JWTService {

  String generateToken(UserDetails userDetails);

  String generateSocialLoginToken(UserDetails userDetails);

  String extractUserName(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

  String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);


}
