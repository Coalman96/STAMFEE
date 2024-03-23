package com.stamfee.stamfee.service.image;


import com.stamfee.stamfee.dto.ImageDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    boolean addImage(List<MultipartFile> multipartFiles, ImageDTO imageDTO) throws Exception;

    ImageDTO getImage(long imageId) throws Exception;

    List<ImageDTO> getImageList(long id, SearchDTO searchDTO) throws Exception;

    boolean deletePostImage(long postId) throws Exception;

    void  deleteImageByFileName(String fileName) throws Exception;

    void deleteProfile(String fileName) throws Exception;

    void deleteImage(long imageId) throws Exception;

    void addProfile(MultipartFile profileImage, Object dto) throws Exception;
}
