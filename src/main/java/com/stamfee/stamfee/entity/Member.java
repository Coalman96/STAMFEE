package com.stamfee.stamfee.entity;

import com.stamfee.stamfee.common.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Table(name="member")
public class Member implements UserDetails {
  //1. Security 유저정보를 구현한 회원 Entity

  @Id
  @Column(name = "cellphone", length = 20, nullable = false)
  private String cellphone;

  @Column(name = "password")
  private String password;

  @Column(name = "nickname", length = 40, nullable = false)
  private String nickname;

  @Column(name = "picture",columnDefinition = "VARCHAR(255) DEFAULT 'empty.jpg'")
  private String picture;

  @Column(name = "role",columnDefinition = "BIGINT DEFAULT 0 CHECK (role BETWEEN 0 AND 1)")
  private Role role;

  //게시물
  @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
  private List<Post> posts;

  //댓글
  @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
  private List<Comment> comments;


  //JWT UserDetails OverRiding
  //권한을 가져올 메소드
  //단순권한부여를 리스트로 반환
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  //비밀번호는 없기때문에 null로 설정
  @Override
  public String getPassword() {
    return password;
  }

  //식별자를 가져올 메소드
  @Override
  public String getUsername() {
    return cellphone;
  }

  //만료되지않은 계정, true로해야 계정이 만료안됨
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  //계정이 잠겨있지않아야한다, true유지
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  //자격증명이 만료되지않았음을 뜻함, true유지
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  //사용가능한계정을 뜻함, true 유지
  @Override
  public boolean isEnabled() {
    return true;
  }

}
