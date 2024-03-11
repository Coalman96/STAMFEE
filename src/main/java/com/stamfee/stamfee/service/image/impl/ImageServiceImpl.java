package com.stamfee.stamfee.service.image.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.stamfee.stamfee.dto.ImageDTO;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.SearchDTO;
import com.stamfee.stamfee.entity.Image;
import com.stamfee.stamfee.entity.Post;
import com.stamfee.stamfee.mapper.ImageMapper;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.service.image.ImageRepository;
import com.stamfee.stamfee.service.image.ImageService;
import com.stamfee.stamfee.service.member.MemberRepository;
import com.stamfee.stamfee.service.member.MemberService;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Log4j2
@Transactional
@Service("imageServiceImpl")
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Value("${imageNum}")
    int imageNum;

    @Override
    public void addImage(List<MultipartFile> multipartFiles, ImageDTO imageDTO) {
        // forEach 구문을 통해 multipartFiles로 넘어온 파일들 하나씩 처리
        multipartFiles.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                // S3에 파일 업로드
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                                       .withCannedAcl(CannedAccessControlList.PublicRead));

                imageDTO.setImageName(generateImageUrl(fileName));
                imageRepository.save(imageMapper.imageDTOToImage(imageDTO));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
        });
    }

    @Override
    public ImageDTO getImage(long imageId) throws Exception {
        return imageRepository.findById(imageId).map(imageMapper::imageToImageDTO).orElse(null);
    }

    @Override
    public List<ImageDTO> getImageList(long id, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(0, imageNum);
        Page<Image> imagePage;
            imagePage = imageRepository.findByPost(Post.builder().postId(id).build(), pageable);
        log.info(imagePage);
        return imagePage.map(imageMapper::imageToImageDTO).toList();
    }

    @Override
    public void deletePostImage(long postId) throws Exception {
        log.info(postId);
        deleteEntityImages(Post.builder().postId(postId).build());
    }

    @Override
    public void deleteImage(long imageId) throws Exception {
        log.info(imageId);
        imageRepository.deleteById(imageId);
    }

    @Override
    public void addProfile(MultipartFile profileImage, Object dto) {
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = createFileName(profileImage.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(profileImage.getSize());
            objectMetadata.setContentType(profileImage.getContentType());

            try (InputStream inputStream = profileImage.getInputStream()) {
                // S3에 프로필 이미지 업로드
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                                       .withCannedAcl(CannedAccessControlList.PublicRead));

                // 프로필 이미지 URL 설정
                if (dto instanceof MemberDTO) {
                    MemberDTO memberDTO = (MemberDTO) dto;
                    memberDTO.setPicture(bucket+"/"+fileName);
                    memberRepository.save(memberMapper.memberDTOToMember(memberDTO));
                }

            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 업로드에 실패했습니다.");
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
        } else {
            // profileImage가 없을 경우 처리할 로직을 추가할 수 있습니다.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 이미지를 찾을 수 없습니다.");
        }
    }

    public void deleteImageByFileName(String fileName) {
        // endpoint와 bucket을 파일 경로에서 먼저 삭제
        String cleanedFileName = removeEndpointAndBucket(fileName);
        log.info("cleanedFileName,{}",cleanedFileName);
        // S3에서 파일 삭제
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, cleanedFileName));
    }

    public void deleteProfile(String fileName) {
        // 특정 조건을 만족하는 경우 파일 삭제를 수행하지 않음
        //기본 프로필사진은 삭제하면안되기때문에 조건에 기본 프로필사진을 추가해준다.
        if (!fileName.equals("기본 프로필사진 엔드포인트")) {
            // endpoint와 bucket을 파일 경로에서 먼저 삭제
            String cleanedFileName = removeEndpointForProfile(fileName);
            log.info("cleanedFileName, {}", cleanedFileName);
            // S3에서 파일 삭제
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, cleanedFileName));
        } else {
            log.info("기본프로필사진은 삭제할 수 없습니다.: {}", fileName);
        }
    }

    private void deleteEntityImages(Object entity) throws Exception {
        // 엔터티에 해당하는 이미지들을 가져옴
        Pageable pageable = PageRequest.of(0, imageNum);
        Page<Image> imagePage;

        if (entity instanceof Post) {
            imagePage = imageRepository.findByPost((Post) entity, pageable);
        } else {
            throw new IllegalArgumentException("해당하는 엔터티가없음.");
        }

        List<ImageDTO> imageDTOList = imagePage.map(imageMapper::imageToImageDTO).toList();

        // 각 이미지에 대해 Object Storage에서 삭제
        imageDTOList.forEach(imageDTO -> deleteImageByFileName(imageDTO.getImageName()));

        // 데이터베이스에서 삭제
        if (entity instanceof Post) {
            imageRepository.deleteByPost((Post) entity);
        }
    }

    private String createFileName(String fileName) {
        // 파일명에서 마지막 점을 기준으로 확장자 추출
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일명에 확장자가 없습니다.");
        }

        String fileExtension = fileName.substring(lastDotIndex);

        // 확장자를 제외한 파일명 가져오기
        String imageNameWithoutExtension = fileName.substring(0, lastDotIndex);

        // 현재 날짜 및 시간 정보 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 날짜 및 시간 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        // 이미지이름(확장자 제외) + 날짜 및 시간 + 확장자
        return imageNameWithoutExtension + now.format(formatter) + fileExtension;
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    private String removeEndpointAndBucket(String fileName) {
        // endpoint와 bucket을 파일 경로에서 삭제
        String cleanedFileName = fileName.replaceFirst(bucket + "/", "");

        return cleanedFileName;
    }

    private String removeEndpointForProfile(String fileName) {
        // endpoint와 bucket을 파일 경로에서 삭제
        String cleanedFileName = fileName.replaceFirst(bucket + "/", "");

        return cleanedFileName;
    }

    private String generateImageUrl(String fileName) {
        // 이미지 디렉터리에는 .jpg와 .png, .gif, .jpeg, heif 확장자만 허용
            return bucket + fileName;

    }

    private boolean isImageFile(String extension) {
        return ".jpg".equalsIgnoreCase(extension) || ".png".equalsIgnoreCase(extension) || ".jpeg".equalsIgnoreCase(extension) || ".gif".equalsIgnoreCase(extension) || ".heif".equalsIgnoreCase(extension);
    }

}
