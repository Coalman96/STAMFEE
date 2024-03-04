package com.stamfee.stamfee.config;

import com.stamfee.stamfee.security.CustomUserDetailsService;
import com.stamfee.stamfee.service.jwt.JWTService;
import com.stamfee.stamfee.service.jwt.impl.JWTServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  //1. Jwt를 사용하여 사용자의 인증을 처리하는 필터 클래스

  ///Field
  private final JWTServiceImpl jwtService;

  private final CustomUserDetailsService customUserDetailsService;

  ///Method
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    //4. request에서 인증 헤더를 가져온다
    final String authHeader = request.getHeader("Authorization");
    log.info("인증헤더키: {}", authHeader);

    //5. 인증을 위해 JWT를 저장한다.
    final String jwt;

    //6. 사용자 이메일을 저장한다
    final String memberEmail;

    //7. 인증헤더가 비어있는지 체크
    if ((StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(
        authHeader, "Bearer "))){
      log.info("토큰이 없음");
      filterChain.doFilter(request, response);
      return;
    }

    //8. JWT토큰을 가져와서 변수에 저장
    //8-1. substring을 7번째부터 하는 이유는 Bearer 타입 다음에 오기 때문임
    jwt = authHeader.substring(7);
    log.info("파싱된 jwt: {}", jwt);

    //9. JWT토큰에서 유저 이메일을 추출
    memberEmail = jwtService.extractUserName(jwt);
    log.info("memberEmail: {}", memberEmail);

    //10. 유저 이메일이 비어있는지 체크와 보안 적용 확인
    //10-1. 현재 사용자의 인증(Authentication) 정보가 비어 있거나(즉, 사용자가 인증되지 않았을 때) null인지를 확인
    if (StringUtils.isNotEmpty(memberEmail)
        && SecurityContextHolder.getContext().getAuthentication() == null) {

      //11. userService로 이동해서 userDetailsService를 생성한다.
      if(StringUtils.isNotEmpty(memberEmail)){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberEmail);
        log.info("userDetails: {}", userDetails.toString());

        //12. 토큰 유효성을 확인하기 위해 JWTServiceImpl에서 메소드를 생성하고 온다.
        //12-1. isTokenValid 메소드를 통해 토큰의 유효성 확인
        if(jwtService.isTokenValid(jwt, userDetails)){
          //12-2. 토큰이 유효하다면 현재 사용자의 인증 정보를 저장하는데 사용되는 SecurityContext 생성
          SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

          //12-3. 사용자 정보, 암호화된 비밀번호(여기서는 null), 및 권한(authorities)을 설정
          UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()
          );
          log.info("token은 {}",token);
          //13. 웹 요청과 관련된 정보를 포함시킴.
          //14. 이 정보는 사용자의 인증 요청과 관련하여 로그인 시도에 대한 자세한 정보를 기록하는 데 사용
          token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          //15. securityContext에 앞에서 생성한 토큰을 설정
          securityContext.setAuthentication(token);
          log.info("securityContext은 {}",securityContext);
          //16. Spring Security의 SecurityContextHolder에 저장하여 현재 사용자의 인증 정보를 업데이트
          SecurityContextHolder.setContext(securityContext);
          log.info("securityContext은 {}",securityContext);
        }
      }
    }

    // 필터를 연결하고 다음 시나리오로 이동
    // 보안 구성 및 관련 저장소를 추가할 것임
    // SecurityConfig로 이동해보자
    log.info("다음");
    filterChain.doFilter(request,response);
  }
}
