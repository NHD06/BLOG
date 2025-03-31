package com.example.blog.service;

import com.example.blog.model.Share;
import com.example.blog.model.User;
import com.example.blog.model.Post;
import com.example.blog.repository.ShareRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShareService {
    private final ShareRepository shareRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ShareService(ShareRepository shareRepository, PostRepository postRepository, UserRepository userRepository) {
        this.shareRepository = shareRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 📌 Lưu thông tin chia sẻ
    public Share sharePost(Long userId, Long postId, String platform) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if (user.isPresent() && post.isPresent()) {
            Share share = new Share();
            share.setUser(user.get());
            share.setPost(post.get());
            share.setPlatform(platform);
            return shareRepository.save(share);
        }
        throw new IllegalArgumentException("User hoặc Post không tồn tại");
    }

    // 📌 Lấy danh sách chia sẻ của một bài viết
    public List<Share> getSharesByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post không tồn tại"));
        return shareRepository.findByPost(post);
    }

    // 📌 Lấy danh sách chia sẻ của một người dùng
    public List<Share> getSharesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        return shareRepository.findByUser(user);
    }
    
}
