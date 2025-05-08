package com.example.blog.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String slug;

    private String thumbnail; 

    private String category;
    private String tags; 

    @Lob
    private String content;

    private String description;
    
    private String status; 

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "views", nullable = false, columnDefinition = "int default 0")
    private int views;

    @Column(name = "likes", nullable = false, columnDefinition = "int default 0")
    private int likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public String getThumbnailBase64() {
        return (thumbnail != null && !thumbnail.isEmpty()) ? "data:image/png;base64," + thumbnail : "";
    }

    public String getFormattedCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        
        if (duration.toMinutes() < 60) { 
            return duration.toMinutes() + " phút trước";
        } else if (duration.toHours() < 24) { 
            return duration.toHours() + " giờ trước";
        } else { 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            return createdAt.format(formatter);
        }
    }
    
    @Transient  
    private Long commentCount;

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}





