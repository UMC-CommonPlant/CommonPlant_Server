package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.NOT_FOUND_USER;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String uuid) {
        return userRepository.findUserByUuid(uuid).orElseThrow(()-> new BadRequestException(NOT_FOUND_USER));
    }
}

