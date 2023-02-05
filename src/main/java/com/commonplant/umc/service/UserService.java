package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.user.UserRequest;
import com.commonplant.umc.repository.UserRepository;
import com.commonplant.umc.utils.UuidUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.NOT_FOUND_USER;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FirebaseService firebaseService;

    public User getUser(String uuid) {
        return userRepository.findUserByUuid(uuid).orElseThrow(()-> new BadRequestException(NOT_FOUND_USER));
    }

    public String getUserProfile(UserRequest.join req, MultipartFile image){
            String uuid = UuidUtil.generateType1UUID();

            //String imageUrl = firebaseService.uploadFiles(uuid, image);
             // imgUrl Setter
             String newCode = randomCode();

             String imgUrl = null;

              if(image.getSize()>0){
                  imgUrl = firebaseService.uploadFiles("commonPlant_myProfile/" +
                           "_" + newCode,image);
                }

            User userProfile = User.builder()
                    .nickName(req.getNickName())
                    .userImgUrl(imgUrl)
                    .email(req.getEmail())
                    .platform(req.getLoginType())
                    .build();

            return jwtService.createToken(userProfile.getUuid());
        }
    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }
    }

//    public User create(final User user) {
//        if(user == null || user.getName() == null ) {
//            throw new RuntimeException("Invalid arguments");
//        }
//        final String nickName = user.getNickName();
//
//        if(userRepository.existsByName(nickName)) {
//            log.warn("nickname already exists {}", nickName);
//            throw new RuntimeException("nickname already exists");
//        }
//        return userRepository.save(user);
//    }
//
//
//    public User getUser(Long name) {
//        User user = userRepository.findById(name).orElseThrow(()->new BadRequestException(NOT_FOUND_USER));
//        return user;
//    }

