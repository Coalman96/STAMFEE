package com.stamfee.stamfee.service.member;


import com.stamfee.stamfee.dto.MemberDTO;

public interface MemberService {

  public MemberDTO getMember(String cellphone) throws Exception;

  public boolean updateMember(MemberDTO memberDTO) throws Exception;



}
