package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.user.UserRequest;
import com.commonplant.umc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.NOT_FOUND_USER;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User getUser(String uuid) {
        return userRepository.findUserByUuid(uuid).orElseThrow(()-> new BadRequestException(NOT_FOUND_USER));
    }

    public User updateUser(UserRequest.update req){
        User modifiedUser = userRepository.findUserBynickName(req.getNickName()).orElseThrow(() -> new BadRequestException(NOT_FOUND_USER));
        modifiedUser.update(req.getNickName(), req.getEmail());
        User user = User.builder()
                .nickName(req.getNickName())
                .email(req.getEmail())
                .build();
        userRepository.save(user);

        return user;
    }

}

