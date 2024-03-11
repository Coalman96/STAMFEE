package com.stamfee.stamfee.mapper;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.entity.Member;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(builder = @Builder(disableBuilder = true),componentModel = "spring")
public interface MemberMapper {


  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "posts", ignore = true)
  @Mapping(target = "comments", ignore = true)
  Member memberDTOToMember(MemberDTO memberDTO);

  @Mapping(target = "activate", ignore = true)
  MemberDTO memberToMemberDTO(Member member);

}