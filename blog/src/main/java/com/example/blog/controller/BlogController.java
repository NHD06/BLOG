package com.example.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.blog.model.Post;
import com.example.blog.service.PostService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class BlogController {

    @Autowired
    PostService postService;

    @GetMapping("/blog/all")
    public String allBlog(Model model) {
        List<Post> posts = postService.getAllPosts();

        // Kiểm tra nếu post.thumbnail là base64, thêm prefix "data:image/jpeg;base64,"
        posts.forEach(post -> {
            if (post.getThumbnail() != null && !post.getThumbnail().startsWith("http")) {
                post.setThumbnail("data:image/jpeg;base64," + post.getThumbnail());
            }
        });

        model.addAttribute("posts", posts);
        return "blog/blog";
    }
}
