package com.example.blog.service;

import com.example.blog.model.Comment;
import com.example.blog.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdWithUser(postId);
    }

    public Comment save(Comment comment) {
        if (comment.getPost() == null || comment.getUser() == null) {
            throw new IllegalArgumentException("Post và User không được để trống!");
        }
        return commentRepository.save(comment);
    }
    
    public Long countCommentsByPostId(Long postId) {
        return commentRepository.countCommentsByPostId(postId);
    }
}
