package com.stamfee.stamfee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
  //액세스 토큰
  private String accessToken;
  //리프레시 토큰
  private String refreshToken;

}
