package com.stamfee.stamfee.service.post.impl;

import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.entity.Post;
import com.stamfee.stamfee.mapper.PostMapper;
import com.stamfee.stamfee.service.post.PostRepository;
import com.stamfee.stamfee.service.post.PostService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@RequiredArgsConstructor
@Log4j2
@Transactional
@Service("postServiceImpl")
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    //게시물 작성
    @Override
    public PostDTO addPost(PostDTO postDTO) throws Exception {
        log.info("받은 PostDTO는{}",postDTO);
        return postMapper.postToPostDTO(postRepository.save(postMapper.postDTOToPost(postDTO)));
    }

    //게시물 조회
    @Override
    public PostDTO getPost(long postId) throws Exception {
        return postRepository.findById(postId).map(postMapper::postToPostDTO).orElse(null);
    }

    //게시물 리스트 조회
    @Override
    public List<PostDTO> getPostList(SearchDTO searchDTO) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize(), sort);
        Page<Post> postPage;
        postPage = postRepository.findAll(pageable);
        log.info(postPage);
        return postPage.map(postMapper::postToPostDTO).toList();
    }

    //게시물 수정
    @Override
    public Long updatePost(PostDTO postDTO) throws Exception {
        Post post = postMapper.postDTOToPost(postDTO);
        log.info(post);
        Post savedPost = postRepository.save(post);
        return savedPost.getPostId();
    }

    //게시물 삭제
    @Override
    public boolean deletePost(long postId) throws Exception {
        log.info(postId);
        try{
            postRepository.deleteById(postId);
            return true;
        }catch (Exception e){
            log.error("게시물 삭제 중 오류 발생: {}",e.getMessage());
            return false;
        }

    }




}
