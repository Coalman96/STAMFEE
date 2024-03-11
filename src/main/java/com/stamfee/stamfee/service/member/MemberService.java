package com.stamfee.stamfee.service.member;


import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import java.util.List;

public interface MemberService {

  public MemberDTO getMember(String cellphone) throws Exception;

  public MemberDTO getOtherMember(String nickname) throws Exception;

  public List<MemberDTO> getMemberList(SearchDTO searchDTO) throws Exception;

  public boolean updateNickname(MemberDTO memberDTO) throws Exception;

  public boolean checkNickname(String nickname) throws Exception;



}
