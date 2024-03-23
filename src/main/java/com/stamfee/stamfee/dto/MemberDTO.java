package com.stamfee.stamfee.dto;

import com.stamfee.stamfee.common.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

  //휴대폰번호
  private String cellphone;
  //비밀번호
  private String password;
  //닉네임
  private String nickname;
  //프로필사진
  private String picture;
  //활성화여부
  private boolean isActivate;
  //역할
  private Role role;

}