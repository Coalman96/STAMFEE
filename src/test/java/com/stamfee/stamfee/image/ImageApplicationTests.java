package com.stamfee.stamfee.image;

import com.stamfee.stamfee.dto.ImageDTO;
import com.stamfee.stamfee.dto.PostDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.service.image.ImageService;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class ImageApplicationTests {
    @Autowired
    ImageService imageService;


    @Value("${pageSize}")
    private int pageSize;

    @DisplayName("이미지리스트 추가 테스트")
    @Test
    public void testAddImageList() throws Exception {
        //given
        // MockMultipartFile 객체 생성
        MockMultipartFile file1 = new MockMultipartFile("files", "testImg1.jpg", "image/jpeg", "testImg1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "testImg2.jpg", "image/jpeg", "testImg2".getBytes());

        // MultipartFile 리스트에 추가
        List<MultipartFile> multipartFiles = Arrays.asList(file1, file2);

        ImageDTO imageDTO = ImageDTO.builder()
                .imageName("testImg1")
                .post(PostDTO.builder().postId(15L).build())
                .build();

        //when
        boolean result = imageService.addImage(multipartFiles,imageDTO);

        //then
        Assertions.assertTrue(result);
    }


    @DisplayName("단일 이미지 조회")
//    @Test
    public void getImage() throws Exception {
        //given
        long imageId = 43L;

        //when
        ImageDTO imageDTO = imageService.getImage(imageId);
        System.out.println(imageDTO);

        //then
        Assertions.assertNotNull(imageDTO);

    }

    @DisplayName("이미지 리스트 조회")
//    @Test
    public void testGetImagesList() throws Exception {
        //given
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCurrentPage(0);
        searchDTO.setPageSize(pageSize);
        long postId = 15L;

        //when
        List<ImageDTO> imageDTOList = imageService.getImageList(postId, searchDTO);
        System.out.println(imageDTOList);

        //then
        Assertions.assertNotNull(imageDTOList);

    }

    @DisplayName("게시물 이미지 삭제")
//    @Test
    public void testDeletePostImage() throws Exception {
        //given
        long postId = 15L;

        //when
        boolean result = imageService.deletePostImage(postId);

        //then
        Assertions.assertTrue(result);
    }
}
