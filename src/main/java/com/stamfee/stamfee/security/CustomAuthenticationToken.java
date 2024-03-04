package com.stamfee.stamfee.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public CustomAuthenticationToken(Object principal, Object credential) {
    super(principal, credential);
  }
}