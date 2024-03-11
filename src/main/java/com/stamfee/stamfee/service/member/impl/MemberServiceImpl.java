package com.stamfee.stamfee.service.member.impl;



import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  //상대 프로필 조회
  @Override
  public MemberDTO getOtherMember(String nickname) throws Exception {
    log.info("받은닉네임은{}", nickname);
    //상대프로필은 프로필사진, 닉네임, 태그, 동네, 친화력이있어야한다.
    Member member = memberRepository.findByNickname(nickname).orElse(null);

    if (member != null) {

      MemberDTO memberDTO = MemberDTO.builder()
          .picture(member.getPicture())
          .nickname(member.getNickname())
          .build();

      log.info("반환하는 회원은{}", memberDTO);

      return memberDTO;
    }
    return null;
  }

  //회원 목록 조회
  @Override
  public List<MemberDTO> getMemberList(SearchDTO searchDTO) throws Exception {
    log.info("SearchDTO는 {} ", searchDTO);
    Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
    Page<Member> memberPage;
    memberPage = memberRepository.findAll(pageable);
    log.info(memberPage);
    return memberPage.map(memberMapper::memberToMemberDTO).toList();
  }

  //회원 프로필 수정
  @Override
  public boolean updateNickname(MemberDTO memberDTO) throws Exception {
    log.info("받은 member는{}",memberDTO);

    if(!checkNickname(memberDTO.getNickname())){
      memberRepository.save(memberMapper.memberDTOToMember(memberDTO));
      return true;
    }else
      return false;
  }


  @Override
  public boolean checkNickname(String nickname) throws Exception {
    log.info("받은 닉네임은{}",nickname);
    
    Optional<Member> member = memberRepository.findByNickname(nickname);
    //닉네임이 존재하지 않을경우 false 반환
    return member.isPresent();

  }


}

