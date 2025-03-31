package com.example.blog.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        String username = principal.getName(); // Lấy username của người dùng đăng nhập
        Optional<User> optionalUser = userRepository.findByUsername(username); // Lấy dữ liệu từ DB

        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get()); // Truyền User vào model
        } else {
            model.addAttribute("user", new User()); // Tránh lỗi nếu user không tồn tại
        }

        return "user/profile/view-profile"; // Trả về trang Thymeleaf
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditProfile(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        model.addAttribute("user", user); // Truyền user vào view
        return "user/profile/edit-profile";
    }


    // Xử lý cập nhật thông tin người dùng
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam String email,
                            @RequestParam MultipartFile avatar,
                            @RequestParam String address,
                            @RequestParam LocalDate dob,
                            @RequestParam String phone_number,
                            @RequestParam(required = false) String password,
                            RedirectAttributes redirectAttributes) {  
        try {
            userService.updateUser(id, name, email, avatar, address, dob, phone_number, password);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/users/profile";
    }

}
