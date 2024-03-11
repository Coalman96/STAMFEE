package com.stamfee.stamfee.service.comment;


import com.stamfee.stamfee.entity.Comment;
import com.stamfee.stamfee.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);
}
