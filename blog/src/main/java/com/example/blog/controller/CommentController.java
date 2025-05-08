package com.example.blog.controller;

import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("")
    public ResponseEntity<?> createComment(
            @RequestParam("postId") Long postId,
            @RequestParam("content") String content,
            Principal principal) {

        // Tìm bài viết
        Post post = postService.findById(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Bài viết không tồn tại!");
        }

        // Lấy thông tin User từ Principal
        String username = principal.getName();
        User user = userService.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("❌ Người dùng không hợp lệ!"));

        // Tạo và lưu bình luận mới
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user); // Gán user vào comment
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        commentService.save(comment);

        return ResponseEntity.ok("✅ Bình luận đã được đăng!");
    }


    @GetMapping("/{postId}")
    public ResponseEntity<List<Map<String, Object>>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPostIdWithUser(postId); // Gọi phương thức mới

        List<Map<String, Object>> response = comments.stream().map(comment -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comment.getId());
            map.put("username", comment.getUser().getUsername());
            map.put("avatar", comment.getUser().getAvatar()); // ✅ Avatar lấy từ User
            map.put("content", comment.getContent());
            map.put("createdAt", comment.getCreatedAt());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


}
