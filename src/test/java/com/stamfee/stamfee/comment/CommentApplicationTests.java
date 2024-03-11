package com.stamfee.stamfee.comment;

import com.stamfee.stamfee.dto.CommentDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.service.comment.CommentRepository;
import com.stamfee.stamfee.service.comment.CommentService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentApplicationTests {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Value("${pageSize}")
    private int pageSize;

    @DisplayName("댓글 작성")
    @Test
    public void testAddcomment() throws Exception {
        //given
        CommentDTO commentDTO = CommentDTO.builder()
                .content("테스트 댓글1")
//                .refComment(CommentDTO.builder().commentId(2L).build())
                .post(PostDTO.builder().postId(15L).build())
                .writer(MemberDTO.builder().cellphone("01086258914").nickname("lee").build())
                .build();
        System.out.println(commentDTO);

        //when
        boolean result = commentService.addComment(commentDTO);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("대댓글 작성")
//    @Test
    public void testAddRefcomment() throws Exception {
        //given
        CommentDTO commentDTO = CommentDTO.builder()
            .content("테스트 대댓글")
            .refComment(CommentDTO.builder().commentId(8L).build())
            .post(PostDTO.builder().postId(15L).build())
            .writer(MemberDTO.builder().cellphone("01086258914").nickname("lee").build())
            .build();
        System.out.println(commentDTO);

        //when
        boolean result = commentService.addComment(commentDTO);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("댓글 조회")
//    @Test
    public void testGetComment() throws Exception {
        //given
        long commentID = 7L;

        //when
        CommentDTO commentDTO =  commentService.getComment(commentID);
        System.out.println(commentDTO);

        //then
        Assertions.assertNotNull(commentDTO);

    }

    @DisplayName("댓글리스트 조회")
//    @Test
    public void testGetCommentList() throws Exception {
        //given
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCurrentPage(0);
        searchDTO.setPageSize(pageSize);
        searchDTO.setSearchKeyword("");
        long postId= 15L;

        //when
        List<CommentDTO> commentList = commentService.getCommentList(postId);
        System.out.println(commentList);

        //then
        Assertions.assertNotNull(commentList);
    }

    @DisplayName("댓글 삭제")
//    @Test
    public void testDeleteComment() throws Exception{
        //given
        long commentID = 7L;

        //when
        boolean result = commentService.deleteComment(commentID);

        //then
        Assertions.assertTrue(result);
    }



    @DisplayName("댓글 수정")
//    @Test
    public void testUpdateComment() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .commentId(8L)
                .content("댓글 수정1")
                .post(PostDTO.builder().postId(15L).build())
                .writer(MemberDTO.builder().cellphone("01086258914").nickname("lee").build())
                .build();
        System.out.println(commentDTO);

        commentService.updateComment(commentDTO);
    }
}