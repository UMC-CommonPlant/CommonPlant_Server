package com.commonplant.umc.service;

import com.commonplant.umc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(IllegalStateException::new);
        return user;
    }
}
