package com.example.blog.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private String title;
    private String slug;
    private String thumbnail;
    private String category;
    private List<String> tags;
    private String content;
    private String description;
    private boolean allowComments;
    private String status;
}

