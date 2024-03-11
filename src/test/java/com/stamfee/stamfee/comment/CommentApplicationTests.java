package com.stamfee.stamfee.comment;

import com.stamfee.stamfee.dto.CommentDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.service.comment.CommentRepository;
import com.stamfee.stamfee.service.comment.CommentService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public void testAddcomment() throws Exception {
        //given
        CommentDTO commentDTO = CommentDTO.builder()
                .content("테스트 댓글1")
//                .timeAgo("방금 전")
//                .refComment(CommentDTO.builder().commentId(2L).build())
                .post(PostDTO.builder().postId(15L).build())
                .writer(MemberDTO.builder().cellphone("01086258914").build())
                .build();
        System.out.println(commentDTO);

        //when
        boolean result = commentService.addComment(commentDTO);

        //then
        Assertions.assertTrue(result);
    }

//    @Test
//    @Transactional
    public void testGetComment() throws Exception {
        long commentID = 2L;
        CommentDTO commentDTO =  commentService.getComment(commentID);
        System.out.println(commentDTO);
    }

//    @Test
//    @Transactional
    public void testGetCommentList() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCurrentPage(0);
        searchDTO.setPageSize(pageSize);
        searchDTO.setSearchKeyword("목");
        long postId= 9L;
        List<CommentDTO> commentList = commentService.getCommentList(postId);
        System.out.println(commentList);
    }

    //@Test
    public void testDeleteComment() throws Exception{
        long commentID = 2L;
        commentService.deleteComment(commentID);
    }




    //@Test
    public void testUpdateComment() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .commentId(2L)
                .content("있겠냐???")
                .timeAgo("방금 전")
                .post(PostDTO.builder().postId(1L).build())
                .writer(MemberDTO.builder().nickname("Bss3").build())
                .build();
        System.out.println(commentDTO);

        commentService.updateComment(commentDTO);
    }
}