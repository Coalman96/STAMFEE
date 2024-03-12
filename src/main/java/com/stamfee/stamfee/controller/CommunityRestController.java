package com.stamfee.stamfee.controller;

import com.stamfee.stamfee.dto.CommentDTO;
import com.stamfee.stamfee.dto.ImageDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.service.comment.CommentService;
import com.stamfee.stamfee.service.image.ImageService;
import com.stamfee.stamfee.service.post.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/stamfee/community")
@RequiredArgsConstructor
@Log4j2
public class CommunityRestController {

    private final PostService postService;
    private final ImageService imageService;
    private final CommentService commentService;



    @Value("${pageSize}")
    int pageSize;

    @PostMapping("/addPost")
    public ResponseEntity<Long> addPost(@ModelAttribute PostDTO postDTO, @RequestParam(value = "image", required = false) List<MultipartFile> image) throws Exception {
        log.info("받은 post는 : {}",postDTO);
        Long postId = postService.addPost(postDTO);

        // 이미지가 제공되었는지 확인
        if (image != null && !image.isEmpty()) {
            // 이미지 업로드 및 저장을 위한 ImageDTO 생성
            ImageDTO imageDTO = ImageDTO.builder()
                    .post(PostDTO.builder().postId(postId).build())
                    .build();

            // 이미지 업로드 및 저장
            imageService.addImage(image, imageDTO);
        }
        return postId != null ? ResponseEntity.ok(postId) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/getPost/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable long postId) throws Exception {
        log.info("받은 postId는 : {}",postId);
        List<ImageDTO> imageDTOList =imageService.getImageList(postId, SearchDTO.builder()
                .build());
        PostDTO postDTO = postService.getPost(postId);
        List<String> imageNames = imageDTOList.stream()
                .map(ImageDTO::getImageName)
                .collect(Collectors.toList());
        postDTO.setImages(imageNames);
        return postDTO != null ? ResponseEntity.ok(postDTO) : ResponseEntity.badRequest().build();
    }


    @GetMapping("/getPostList/{currentPage}")
    public ResponseEntity<List<PostDTO>> getPostList(@PathVariable int currentPage, @RequestParam(value = "searchKeyword",required = false) String searchKeyword) throws Exception {
        System.out.println(searchKeyword);
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .searchKeyword(searchKeyword)
                .build();
        List<PostDTO> postDTOList = postService.getPostList(searchDTO);
        return postDTOList != null ? ResponseEntity.ok(postDTOList) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/updatePost")
    public ResponseEntity<Long> updatePost(@ModelAttribute PostDTO postDTO,@RequestParam("image") List<MultipartFile> image) throws Exception {
        Long postId = postService.updatePost(postDTO);
        imageService.deletePostImage(postId);

        // 이미지 업로드 및 저장을 위한 ImageDTO 생성
        ImageDTO imageDTO = ImageDTO.builder()
            .post(PostDTO.builder().postId(postId).build())
            .build();

        // 이미지 업로드 및 저장
        imageService.addImage(image, imageDTO);
        return postId != null ? ResponseEntity.ok(postId) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/deletePost/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable long postId) throws Exception {
        if(postService.deletePost(postId)){
            imageService.deletePostImage(postId);
            return ResponseEntity.ok(true);
        }else
            return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/addComment")
    public ResponseEntity<Boolean> addComment(@RequestBody CommentDTO commentDTO) throws Exception {
        boolean result = commentService.addComment(commentDTO);
        return result ? ResponseEntity.ok(true): ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/getComment/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long commentId) throws Exception {

        CommentDTO commentDTO = commentService.getComment(commentId);

        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/getCommentList/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentList(@PathVariable  long postId) throws Exception {
        List<CommentDTO> commentDTOList = commentService.getCommentList(postId);
        return commentDTOList != null ? ResponseEntity.ok(commentDTOList) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/updateComment")
    public ResponseEntity<Boolean> updateComment(@RequestBody CommentDTO commentDTO) throws Exception {
        boolean result = commentService.updateComment(commentDTO);
        return result ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/deleteComment/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable long commentId) throws Exception {
        boolean result = commentService.deleteComment(commentId);
        return result ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
    }

}
