package com.stamfee.stamfee.service.post;

import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.entity.Post;
import java.util.List;


public interface PostService {
    Long addPost(PostDTO postDTO) throws Exception;

    PostDTO getPost(long postId) throws Exception;

    List<PostDTO> getPostList(SearchDTO searchDTO) throws Exception;

    Long updatePost(PostDTO postDTO) throws Exception;

    boolean deletePost(long postId) throws Exception;

}
