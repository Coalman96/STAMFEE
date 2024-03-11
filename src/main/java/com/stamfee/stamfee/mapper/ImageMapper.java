package com.stamfee.stamfee.mapper;


import com.stamfee.stamfee.dto.ImageDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.entity.Image;
import com.stamfee.stamfee.entity.Post;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface ImageMapper {

    @Mapping(source = "post", target = "post", qualifiedByName = "postDTOToPost")
    Image imageDTOToImage(ImageDTO imageDTO);

    @Mapping(source = "post", target = "post", qualifiedByName = "postToPostDTO")
    ImageDTO imageToImageDTO(Image image);


    @Named("postDTOToPost")
    default Post postDTOToPost(PostDTO postDTO){
        if(postDTO == null){
            return null;
        }
        return Post.builder().postId(postDTO.getPostId())
                .build();
    }

    @Named("postToPostDTO")
    default PostDTO postToPostDTO(Post post){
        if(post == null){
            return null;
        }
        return PostDTO.builder().postId(post.getPostId())
                .build();
    }

}
