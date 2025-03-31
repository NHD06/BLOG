package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.Share;
import com.example.blog.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findByPost(Post post);
    List<Share> findByUser(User user);
    
    @Query("SELECT COUNT(s) FROM Share s WHERE s.post.id = :postId")
    long countSharesByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(s) FROM Share s WHERE MONTH(s.createdAt) = MONTH(CURRENT_DATE) - 1 AND YEAR(s.createdAt) = YEAR(CURRENT_DATE)")
    long countLastMonth();

}
