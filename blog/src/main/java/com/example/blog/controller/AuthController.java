package com.example.blog.controller;

import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.model.Role;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.ShareRepository;
import com.example.blog.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    
    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShareRepository shareRepository;

    // Hiển thị trang login/register
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("showRegister", false); // Mặc định hiển thị đăng nhập
        return "login"; // Trả về login.html
    }

    // Xử lý đăng ký tài khoản
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String roleName,
                           Model model) {
        try {
            Role role = Role.valueOf(roleName.toUpperCase()); // Chuyển string thành Role enum
            userService.registerUser(username, email, password, role);
            model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Lỗi: Vai trò không hợp lệ!");
        }
        return "login"; // Giữ người dùng trên trang đăng nhập
    }

    // Xác định trang home dựa trên role
    @GetMapping("/home")
    public String home(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/home";
        } else {
            return "redirect:/user/home";
        }
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model, 
                            @RequestParam(defaultValue = "0") int page, 
                            @RequestParam(defaultValue = "5") int size) { 
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách bài viết
        Page<Post> postPage = postRepository.findAll(pageable);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        // Tính tổng số bài viết từ bảng Post
        long totalPosts = postRepository.count();  // Đếm tổng số bản ghi trong bảng Post
        model.addAttribute("totalPosts", totalPosts);

        // Tính tổng số lượt xem từ cột views trong bảng Post
        long totalViews = postRepository.sumViews();  // Lấy tổng số lượt xem
        model.addAttribute("totalViews", totalViews); // Tổng lượt xem

        // Tính tổng số người dùng từ bảng User
        long totalUsers = userRepository.count();  // Đếm tổng số bản ghi trong bảng User
        model.addAttribute("totalUsers", totalUsers); // Tổng số người dùng


        // Tính tổng số lượt chia sẻ từ bảng share
        long totalShares = shareRepository.count();  // Đếm tổng số bản ghi trong bảng Share

        // Lấy dữ liệu bài viết theo tháng
        List<Object[]> postStats = postRepository.countPostsPerMonth();
        Map<Integer, Integer> postsPerMonthMap = new HashMap<>();
        for (Object[] row : postStats) {
            postsPerMonthMap.put((Integer) row[0], ((Long) row[1]).intValue());
        }

        // Lấy dữ liệu người dùng theo tháng
        List<Object[]> userStats = userRepository.countUsersPerMonth();
        Map<Integer, Integer> usersPerMonthMap = new HashMap<>();
        for (Object[] row : userStats) {
            usersPerMonthMap.put((Integer) row[0], ((Long) row[1]).intValue());
        }

        // Chuẩn bị danh sách dữ liệu theo tháng (1-12)
        List<Integer> postsPerMonth = new ArrayList<>();
        List<Integer> usersPerMonth = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            postsPerMonth.add(postsPerMonthMap.getOrDefault(i, 0)); // Nếu không có dữ liệu -> 0
            usersPerMonth.add(usersPerMonthMap.getOrDefault(i, 0));
        }

        model.addAttribute("totalShares", totalShares); // Tổng số lượt chia sẻ
        model.addAttribute("postsPerMonth", postsPerMonth);
        model.addAttribute("usersPerMonth", usersPerMonth);

        // Lấy danh sách người dùng
        Page<User> userPage = userRepository.findAll(pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentUserPage", page);
        model.addAttribute("totalUserPages", userPage.getTotalPages());

        return "admin/home/home-admin";
    }

    @GetMapping("/user/home")
    public String userHome(Model model, Principal principal) {
        List<Post> topPosts = postService.getTopPosts();
        String username = principal.getName();
        // Kiểm tra nếu post.thumbnail là base64, thêm prefix "data:image/jpeg;base64,"
        topPosts.forEach(post -> {
            if (post.getThumbnail() != null && !post.getThumbnail().startsWith("http")) {
                post.setThumbnail("data:image/jpeg;base64," + post.getThumbnail());
            }

            // Gán số lượng bình luận vào Post
            post.setCommentCount(commentService.countCommentsByPostId(post.getId()));
        });
        Optional<User> optionalUser = userRepository.findByUsername(username); // Lấy dữ liệu từ DB

        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get()); // Truyền User vào model
        } else {
            model.addAttribute("user", new User()); // Tránh lỗi nếu user không tồn tại
        }

        model.addAttribute("topPosts", topPosts);
        return "user/home/home-user";
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername()));
    }


    @GetMapping("/api/post-categories")
    @ResponseBody
    public Map<String, Object> getPostCategories() {
        List<Object[]> results = postRepository.countPostsByCategory();
        Map<String, Object> response = new HashMap<>();
        
        List<String> categories = new ArrayList<>();
        List<Long> postCounts = new ArrayList<>();

        for (Object[] result : results) {
            categories.add((String) result[0]);
            postCounts.add(((Number) result[1]).longValue());
        }

        response.put("categories", categories);
        response.put("postCounts", postCounts);
        return response;
    }

}
