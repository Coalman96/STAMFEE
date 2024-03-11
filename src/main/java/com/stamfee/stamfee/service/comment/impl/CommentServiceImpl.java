package com.stamfee.stamfee.service.comment.impl;

import com.stamfee.stamfee.dto.CommentDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.entity.Comment;
import com.stamfee.stamfee.entity.Post;
import com.stamfee.stamfee.mapper.CommentMapper;
import com.stamfee.stamfee.service.comment.CommentRepository;
import com.stamfee.stamfee.service.comment.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Log4j2
@Transactional
@Service("commentServiceImpl")
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Value("${commentNum}")
    int commentNum;


    //댓글 작성
    @Override
    public boolean addComment(CommentDTO commentDTO) throws Exception {
        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        log.info(comment);
        try{
            commentRepository.save(comment);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //댓글 조회
    @Override
    public CommentDTO getComment(long commentId) throws Exception {
        return commentRepository.findById(commentId).map(commentMapper::commentToCommentDTO).orElse(null);
    }

    //댓글 리스트 조회
    @Override
    public List<CommentDTO> getCommentList(long postId) throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "regDate");
        Pageable pageable = PageRequest.of(0, commentNum, sort);
        Page<Comment> commentPage;
        commentPage = commentRepository.findByPost(Post.builder().postId(postId).build(), pageable);
        log.info(commentPage);
        return commentPage.map(commentMapper::commentToCommentDTO).toList();
    }

    //댓글 수정
    @Override
    public void updateComment(CommentDTO commentDTO) throws Exception {
        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        log.info(comment);
        commentRepository.save(comment);
    }

    //댓글 삭제
    @Override
    public void deleteComment(long commentId) throws Exception {
        log.info(commentId);
        commentRepository.deleteById(commentId);
    }

}
