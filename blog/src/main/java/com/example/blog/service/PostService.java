package com.example.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.model.Post;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    private final String UPLOAD_DIR = "uploads/";

    public Post createPost(String title, String slug, MultipartFile file,
                        String category, String tags, String content,
                        String description, String status) throws IOException {

        String base64Image = Base64.getEncoder().encodeToString(file.getBytes()); // 🛠 Chuyển ảnh thành chuỗi Base64

        Post post = Post.builder()
                .title(title)
                .slug(slug)
                .thumbnail(base64Image)
                .category(category)
                .tags(tags)
                .content(content)
                .description(description)
                .status(status)
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public List<Post> getTopPosts() {
        return postRepository.findTopPosts(PageRequest.of(0, 6)); // Lấy 3 bài viết nổi bật
    }

    public void updatePost(Long id, String title, String slug, MultipartFile file, 
                       String category, String tags, String content, 
                       String description, String status) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại!"));

        post.setTitle(title);
        post.setSlug(slug);
        post.setCategory(category);
        post.setTags(tags);
        post.setContent(content);
        post.setDescription(description);
        post.setStatus(status);

        // 🛑 Chuyển ảnh thành chuỗi Base64 nếu có ảnh mới
        if (file != null && !file.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes()); // 🛠 Chuyển ảnh thành chuỗi Base64
            post.setThumbnail(base64Image); // Lưu chuỗi Base64 vào DB
        }

        postRepository.save(post);
    }



    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("❌ Bài viết không tồn tại!");
        }
        postRepository.deleteById(id);
    }
}


