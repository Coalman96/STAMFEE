package com.stamfee.stamfee.service.image;


import com.stamfee.stamfee.entity.Image;
import com.stamfee.stamfee.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image, Long> {

    Page<Image> findByPost(Post post, Pageable pageable);

    void deleteByPost(Post post);

}
