package com.stamfee.stamfee.post;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.entity.Post;
import com.stamfee.stamfee.mapper.PostMapper;
import com.stamfee.stamfee.service.post.PostRepository;
import com.stamfee.stamfee.service.post.PostService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostApplicationTests {

    /*
    * BDDMockito 사용불가
    *  - 실제 데이터에 regDate가 주입되어야한다
    *  - CalculateTimeAgo로인해 regDate를 가져올수없다
    *  - 실제 데이터, 실제 서비스를 사용해야한다.
    * */
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostMapper postMapper;

    @Value("${pageSize}")
    private int pageSize;

    @DisplayName("게시물 작성 테스트")
//    @Test
    public void testAddPost() throws Exception {
        //given
        //이미지 추가
        List<String> imageList = new ArrayList<>();
        imageList.add("테스트1.jpg");
        imageList.add("테스트2.png");
        imageList.add("테스트3.jpg");

        //게시물 객체 생성
        PostDTO postDTO = PostDTO.builder()
                .title("이미지테스트")
                .content("엄...")
                .images(imageList)
                .writer(MemberDTO.builder().cellphone("01086258914").build())
                .build();
        //when
        PostDTO result = postService.addPost(postDTO);

        //then
        Assertions.assertNotNull(result);
    }

//    @Test
    public void testGetPost() throws Exception {
        //given
        long postId = 15;

        //when
        PostDTO postDTO = postService.getPost(postId);

        //then
        Assertions.assertNotNull(postDTO);
    }

//    @Test
    public void testDeletePost() throws Exception{
        //given
        long postId = 14;

        //when
        boolean result = postService.deletePost(postId);

        //then
        Assertions.assertTrue(result);
    }


//   @Test
    public void testGetPostList() throws Exception {
        //given
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCurrentPage(0);
        searchDTO.setPageSize(pageSize);
        searchDTO.setSearchKeyword("");

        //when
        List<PostDTO> postDTOList = postService.getPostList(searchDTO);

        //then
        Assertions.assertNotNull(postDTOList);
    }

    @Test
    public void testUpdatePost() throws Exception {
        //given
        List<String> imageList = new ArrayList<>();
        imageList.add("디비에 들어가나 테스트.jpg");
        PostDTO postDTO = PostDTO.builder()
                .postId(15L)
                .title("수정 테스트")
                .content("엄엄..")
                .writer(MemberDTO.builder().cellphone("01086258914").build())
                .build();

        //when
        Long result = postService.updatePost(postDTO);
        System.out.println(postService.getPost(postDTO.getPostId()));

        //then
        Assertions.assertNotNull(result);
    }
}