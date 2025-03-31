package com.example.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.blog.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT MONTH(u.createdAt) AS month, COUNT(u.id) FROM User u GROUP BY MONTH(u.createdAt) ORDER BY month")
    List<Object[]> countUsersPerMonth();

    @Query("SELECT COUNT(u) FROM User u WHERE MONTH(u.createdAt) = MONTH(CURRENT_DATE) - 1 AND YEAR(u.createdAt) = YEAR(CURRENT_DATE)")
    long countLastMonth();

}
