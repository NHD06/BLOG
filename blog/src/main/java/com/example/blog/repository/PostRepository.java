package com.example.blog.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
    Optional<Post> findBySlug(String slug);
    List<Post> findAll(Sort sort);

    @Query("SELECT p FROM Post p ORDER BY p.views DESC, (SELECT COUNT(c) FROM Comment c WHERE c.post.id = p.id) DESC")
    List<Post> findTopPosts(Pageable pageable);

    @Query("SELECT SUM(p.views) FROM Post p")
    long sumViews();

    @Query("SELECT p.category, COUNT(p) FROM Post p GROUP BY p.category ORDER BY COUNT(p) DESC")
    List<Object[]> countPostsByCategory();

    @Query("SELECT MONTH(p.createdAt) AS month, COUNT(p.id) FROM Post p GROUP BY MONTH(p.createdAt) ORDER BY month")
    List<Object[]> countPostsPerMonth();

    @Query("SELECT COUNT(p) FROM Post p WHERE MONTH(p.createdAt) = MONTH(CURRENT_DATE) - 1 AND YEAR(p.createdAt) = YEAR(CURRENT_DATE)")
    long countLastMonth();

    @Query("SELECT SUM(p.views) FROM Post p WHERE MONTH(p.createdAt) = MONTH(CURRENT_DATE) - 1 AND YEAR(p.createdAt) = YEAR(CURRENT_DATE)")
    Long sumViewsLastMonth();


}
