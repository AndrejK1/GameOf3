package com.softkit.service;

import com.softkit.exception.UserNotFoundException;
import com.softkit.model.User;
import com.softkit.repository.UserRepository;
import com.softkit.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String signIn(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return userRepository.findByUsernameIgnoreCase(username).map(User::getRoles)
                    .map(roles -> jwtTokenProvider.createToken(username, roles))
                    .orElseThrow(UserNotFoundException::new);
        } catch (AuthenticationException e) {
            throw new UserNotFoundException();
        }
    }

    public String signup(User user) {
        if (!userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(user.getUsername(), user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new UserNotFoundException();
        }
    }

    public User findUser(String userName) {
        return userRepository.findByUsernameIgnoreCase(userName).orElseThrow(UserNotFoundException::new);
    }
}
