package com.example.blog.controller;

import com.example.blog.model.Share;
import com.example.blog.service.ShareService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import com.example.blog.model.Share;
import com.example.blog.model.User;
import com.example.blog.model.Post;
import com.example.blog.repository.ShareRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ShareController {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/share")
    public ResponseEntity<String> sharePost(
            @RequestParam Long postId,
            @RequestParam String platform,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        // Tìm User hiện tại
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng!"));

        // Tìm bài viết
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bài viết không tồn tại"));

        // Lưu vào database
        Share share = new Share();
        share.setUser(user);
        share.setPost(post);
        share.setPlatform(platform);
        shareRepository.save(share);

        return ResponseEntity.ok("Chia sẻ bài viết thành công trên " + platform);
    }
}




