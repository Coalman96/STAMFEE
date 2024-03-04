package com.stamfee.stamfee.service.member.impl;



import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Log4j2
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  private final MemberMapper memberMapper;

  //내 프로필 조회
  @Override
  public MemberDTO getMember(String cellphone) throws Exception {
    return memberRepository.findByCellphone(cellphone).map(memberMapper::memberToMemberDTO).orElse(null);

  }

  //회원 프로필 수정
  @Override
  public boolean updateMember(MemberDTO memberDTO) throws Exception {
    log.info("받은 member는{}",memberDTO);

    MemberDTO existMember = getMember(memberDTO.getCellphone());

    memberRepository.save(memberMapper.memberDTOToMember(existMember));

    return true;
  }


}

