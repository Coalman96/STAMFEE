package com.stamfee.stamfee.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long postId;
    private String title;
    private String content;
    private String timeAgo;
    private MemberDTO writer;
    private List<String> images;

}
