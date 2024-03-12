package com.stamfee.stamfee.service.post;

import com.stamfee.stamfee.entity.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
