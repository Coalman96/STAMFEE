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

  private String cellphone;
  private String password;
  private String nickname;
  private String dongNe;
  private String picture;
  private boolean isActivate;
  private Role role;

}