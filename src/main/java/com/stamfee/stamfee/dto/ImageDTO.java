package com.stamfee.stamfee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    private Long imageId;
    private String imageName;
    private PostDTO post;
}
