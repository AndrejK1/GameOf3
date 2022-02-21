package com.softkit.repository;

import com.softkit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    Optional<User> findByUsernameIgnoreCase(String username);
}
