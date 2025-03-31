package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/add-post")
    public String showNewPostForm() {
        return "admin/posts/new-post"; 
    }

    @PostMapping("/add")
    public ResponseEntity<String> createPost(
            @RequestParam("title") String title,
            @RequestParam("slug") String slug,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam("tags") String tags,
            @RequestParam("content") String content,
            @RequestParam("description") String description,
            @RequestParam("status") String status) {

        try {
            postService.createPost(title, slug, file, category, tags, content, description, status);
            return ResponseEntity.ok("✅ Bài viết đã được lưu thành công!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Lỗi khi lưu bài viết!");
        }
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại!"));
        post.setViews(post.getViews() + 1);
        postRepository.save(post); 
        model.addAttribute("post", post);
        return "post-detail"; 
    }

    // @GetMapping("/list-post")
    // public String listPosts(Model model) {
    //     List<Post> posts = postRepository.findAll();
    //     model.addAttribute("posts", posts);
    //     return "admin/posts/post-list"; 
    // }

    @GetMapping("/post-all")
    public String BlogAll(Model model) {
        List<Post> posts = postService.getAllPosts();
        posts.forEach(post -> {
            if (post.getThumbnail() != null && !post.getThumbnail().startsWith("http")) {
                post.setThumbnail("data:image/jpeg;base64," + post.getThumbnail());
            }
            post.setCommentCount(commentService.countCommentsByPostId(post.getId()));
        });
        model.addAttribute("posts", posts);
        return "blog/blog";
    }

    @GetMapping("/edit/{id}")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại!"));
        model.addAttribute("post", post);
        return "admin/posts/edit-post";
    }


    @PostMapping("/edit/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("slug") String slug,
            @RequestParam(value = "thumbnail", required = false) MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam("tags") String tags,
            @RequestParam("content") String content,
            @RequestParam("description") String description,
            @RequestParam("status") String status) {

        try {
            System.out.println("🟢 Title: " + title);
            System.out.println("🟢 Slug: " + slug);
            System.out.println("🟢 File: " + (file != null ? file.getOriginalFilename() : "Không có ảnh"));

            postService.updatePost(id, title, slug, file, category, tags, content, description, status);
            return ResponseEntity.ok("✅ Bài viết đã được cập nhật thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Lỗi khi cập nhật bài viết: " + e.getMessage());
        }
    }


    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("✅ Bài viết đã được xóa thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Lỗi khi xóa bài viết!");
        }
    }


}

