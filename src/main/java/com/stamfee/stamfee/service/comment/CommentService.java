package com.stamfee.stamfee.service.comment;

import com.stamfee.stamfee.dto.CommentDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import java.util.List;


public interface CommentService {

    boolean addComment(CommentDTO commentDTO) throws Exception;

    CommentDTO getComment(long commentId) throws Exception;

    List<CommentDTO> getCommentList(long postId) throws Exception;

    boolean updateComment(CommentDTO commentDTO) throws Exception;

    boolean deleteComment(long commentId) throws Exception;
}
