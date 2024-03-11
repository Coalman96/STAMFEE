package com.stamfee.stamfee.mapper;

import com.stamfee.stamfee.common.CalculateTimeAgo;
import com.stamfee.stamfee.dto.CommentDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.entity.Comment;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.entity.Post;
import java.time.LocalDateTime;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "post", target = "post", qualifiedByName = "postDTOToPost")
    @Mapping(source = "writer", target = "writer", qualifiedByName = "memberDTOToMember")
    @Mapping(source = "refComment", target = "refComment", qualifiedByName = "refcommentDTOToRefComment")
    @Mapping(target = "regDate", ignore = true)
    Comment commentDTOToComment(CommentDTO commentDTO);

    @Mapping(source = "post", target = "post", qualifiedByName = "postToPostDTO")
    @Mapping(source = "writer", target = "writer", qualifiedByName = "memberToMemberDTO")
    @Mapping(source = "refComment", target = "refComment", qualifiedByName = "refcommentToRefCommentDTO")
    @Mapping(source = "regDate", target = "timeAgo", qualifiedByName = "regDateToTimeAgo")
    CommentDTO commentToCommentDTO(Comment comment);

    @Named("postDTOToPost")
    default Post postDTOToPost(PostDTO postDTO){
        if(postDTO == null){
            return null;
        }
        return Post.builder().postId(postDTO.getPostId()).build();
    }

    @Named("postToPostDTO")
    default PostDTO postToPostDTO(Post post){
        if(post == null){
            return null;
        }
        return PostDTO.builder().postId(post.getPostId()).build();
    }

    @Named("memberDTOToMember")
    default Member memberDTOToMember(MemberDTO memberDTO){
        if(memberDTO == null){
            return null;
        }
        return Member.builder().nickname(memberDTO.getNickname()).build();
    }

    @Named("memberToMemberDTO")
    default MemberDTO memberToMemberDTO(Member member){
        if(member == null){
            return null;
        }
        return MemberDTO.builder().nickname(member.getNickname())
                .build();
    }

    @Named("refcommentDTOToRefComment")
    default Comment refcommentDTOToRefComment(CommentDTO commentDTO){
        if(commentDTO == null){
            return null;
        }
        return Comment.builder().commentId(commentDTO.getCommentId()).build();
    }

    @Named("refcommentToRefCommentDTO")
    default CommentDTO refcommentToRefCommentDTO(Comment comment){
        if(comment == null){
            return null;
        }
        return CommentDTO.builder().commentId(comment.getCommentId())
                .build();
    }


    @Named("regDateToTimeAgo")
    default String regDateToTimeAgo(LocalDateTime regDate){
        return CalculateTimeAgo.calculateTimeDifferenceString(regDate);
    }
}
