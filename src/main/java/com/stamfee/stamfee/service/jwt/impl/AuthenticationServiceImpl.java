package com.stamfee.stamfee.service.jwt.impl;

import com.stamfee.stamfee.common.Role;
import com.stamfee.stamfee.dto.GpsDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.TokenDTO;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.security.CustomAuthenticationManager;
import com.stamfee.stamfee.security.CustomAuthenticationToken;
import com.stamfee.stamfee.service.auth.AuthService;
import com.stamfee.stamfee.service.jwt.AuthenticationService;
import com.stamfee.stamfee.service.jwt.JWTService;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

  private final MemberMapper memberMapper;

  private final JWTService jwtService;

  private final MemberRepository memberRepository;

  private final MemberService memberService;

  private final CustomAuthenticationManager authenticationManager;

  private final AuthService authService;

  private final PasswordEncoder passwordEncoder;

  public TokenDTO addMember(MemberDTO memberDTO) throws Exception {
    // findByCellphone로 회원을 찾음
    Optional<MemberDTO> existingMember = memberRepository.findByCellphone(memberDTO.getCellphone())
        .or(()->memberRepository.findByNickname(memberDTO.getNickname()))
        .map(memberMapper::memberToMemberDTO);
    // 회원이 이미 존재하는 경우 예외를 던짐

    if (existingMember.isPresent()) {
//      throw new IllegalArgumentException("이미 존재하는 회원입니다. 회원의 정보: " + existingMember.get());
      return null;
    }else{
      Member member = memberMapper.memberDTOToMember(memberDTO);
      member.setRole(Role.USER);
      member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
      memberRepository.save(member);
      MemberDTO existMember = memberRepository.findByCellphone(memberDTO.getCellphone()).map(memberMapper::memberToMemberDTO).orElseThrow(()-> new IllegalArgumentException("없는 회원입니다."));
      return accessToken(existMember);
    }
  }

  public TokenDTO login(MemberDTO memberDTO) throws Exception {
    // findByCellphone로 회원을 찾음
    Optional<MemberDTO> existingMember = memberRepository.findByCellphone(memberDTO.getCellphone())
        .map(memberMapper::memberToMemberDTO);
    MemberDTO existMember = existingMember.orElse(null);
    log.info("찾은 멤버는{}",existingMember);
    assert existMember != null;
    boolean login = passwordEncoder.matches(memberDTO.getPassword(), existMember.getPassword());
    log.info("로그인여부는{}",login);

    return login ? accessToken(existMember) : new TokenDTO();

  }

  @Override
  public TokenDTO accessToken(MemberDTO memberDTO) throws Exception {
    CustomAuthenticationToken authRequest = new CustomAuthenticationToken(memberDTO.getCellphone(), memberDTO.getPassword());
    // CustomAuthenticationToken을 사용하려면 CustomAuthenticationManager의 authenticate가 호출되도록 해야 합니다.
    // 따라서 여기에서는 CustomAuthenticationManager를 직접 호출하게 됩니다.
    authenticationManager.authenticate(authRequest);
    Member member = memberRepository.findByCellphone(memberDTO.getCellphone()).orElseThrow(()-> new IllegalArgumentException("없는 회원입니다."));
    TokenDTO tokenDTO = new TokenDTO();

    tokenDTO.setAccessToken(jwtService.generateToken(member));
    tokenDTO.setRefreshToken(jwtService.generateRefreshToken(new HashMap<>(), member));

    return tokenDTO;
  }

  @Override
  public TokenDTO refreshToken(TokenDTO refreshTokenDTO) throws Exception  {
    // 받은 RefreshToken을 통해 유저 정보 중 아이디값추출 -> subject
    String memberCellphone = jwtService.extractUserName(refreshTokenDTO.getRefreshToken());
    // 추출한 아이디로 데이터를가져옴
    Member member = memberRepository.findByCellphone(memberCellphone).orElseThrow(()-> new IllegalArgumentException("없는 회원입니다."));

    //
    if(jwtService.isTokenValid(refreshTokenDTO.getRefreshToken(),member)){

      TokenDTO token = new TokenDTO();
      token.setAccessToken(jwtService.generateToken(member));
      token.setRefreshToken(jwtService.generateRefreshToken(new HashMap<>(),member)); // 새로운 리프레시 토큰 설정

      return token;
    }

    return null;
  }


}
