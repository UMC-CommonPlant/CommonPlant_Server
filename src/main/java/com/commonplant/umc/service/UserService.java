package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.NOT_FOUND_USER;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(final User user) {
        if(user == null || user.getName() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
        final String name = user.getName();
        if(userRepository.existsByName(name)) {
            log.warn("Username already exists {}", name);
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(user);
    }
    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new BadRequestException(NOT_FOUND_USER));
        return user;
    }
}
