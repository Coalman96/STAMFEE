package com.stamfee.stamfee.mapper;

import com.stamfee.stamfee.common.CalculateTimeAgo;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.entity.Image;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "regDate", ignore = true)
    @Mapping(source = "writer", target = "writer", qualifiedByName = "memberDTOToMember")
    @Mapping(source = "images", target = "images", qualifiedByName = "stringToImage")
    Post postDTOToPost(PostDTO postDTO);

    @Mapping(source = "writer", target = "writer", qualifiedByName = "memberToMemberDTO")
    @Mapping(source = "images", target = "images", qualifiedByName = "imageToString")
    @Mapping(source = "regDate", target = "timeAgo", qualifiedByName = "regDateToTimeAgo")
    PostDTO postToPostDTO(Post post);

    @Named("memberDTOToMember")
    default Member memberDTOToMember(MemberDTO memberDTO){
        if(memberDTO == null){
            return null;
        }
        return Member.builder().cellphone(memberDTO.getCellphone()).nickname(memberDTO.getNickname()).build();
    }

    @Named("memberToMemberDTO")
    default MemberDTO memberToMemberDTO(Member member){
        if(member == null){
            return null;
        }
        return MemberDTO.builder().cellphone(member.getCellphone()).nickname(member.getNickname())
                .build();
    }

    @Named("stringToImage")
    default List<Image> stringToImage(List<String> images){
        if(images == null){
            return null;
        }
        return images.stream().map(imageName -> {
            return Image.builder()
                    .imageName(imageName)
                    .build();
        }).collect(Collectors.toList());
    }

    @Named("imageToString")
    default List<String> imageToString(List<Image> images){
        if(images == null){
            return null;
        }
        return images.stream().map(Image::getImageName).collect(Collectors.toList());
    }

    @Named("regDateToTimeAgo")
    default String regDateToTimeAgo(LocalDateTime regDate){
        return CalculateTimeAgo.calculateTimeDifferenceString(regDate);
    }

}
