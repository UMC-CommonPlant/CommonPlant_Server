package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.user.KakaoProfile;
import com.commonplant.umc.dto.user.NaverProfile;
import com.commonplant.umc.dto.user.UserRequest;
import com.commonplant.umc.repository.UserRepository;
import com.commonplant.umc.utils.UuidUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService{
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FirebaseService firebaseService;


    //TODO: loginUser
    public String oauthLogin(String accessToken, String loginType){
        String email = "";
        switch (loginType){
            case "kakao":
                email = kakaoLogin(accessToken); break;
            case "naver":
                email = naverLogin(accessToken); break;
            default: throw new BadRequestException(WRONG_PLATFORM);
        }

        if(userRepository.countUserByEmail(email, loginType) > 0){
            User user = userRepository.findUserByEmail(email, loginType);
            String token = jwtService.createToken(user.getUuid());
            return token;
        }else{
            throw new IllegalArgumentException(email);
        }
    }

    //TODO: joinUser
    public String joinUser(UserRequest.join req, MultipartFile image){

        if(userRepository.countUserByEmail(req.getEmail(), req.getLoginType()) > 0){
            throw new BadRequestException(EXIST_USER);
        }else{
            //join
            String uuid = UuidUtil.generateType1UUID();
            String imageUrl = firebaseService.uploadFiles(uuid, image);

            User user = User.builder()
                    .nickName(req.getNickName())
                    .userImgUrl(imageUrl)
                    .uuid(uuid)
                    .email(req.getEmail())
                    .platform(req.getLoginType()).
                    build();
            userRepository.save(user);

            return jwtService.createToken(user.getUuid());
        }
    }

    public String kakaoLogin(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response;
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        String redirect_uri="https://kapi.kakao.com/v2/user/me";

        KakaoProfile kakaoProfile = null;
        try{
            response=restTemplate.exchange(redirect_uri, HttpMethod.POST, request, String.class);
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        }catch(HttpClientErrorException e){
            log.info("[REJECT]kakao login error");

        }catch(JsonProcessingException e){
            log.info("[REJECT]kakaoMapper error");
        }
        return kakaoProfile.getKakao_account().getEmail();
    }

    public String naverLogin(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response;
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        String redirect_uri="https://openapi.naver.com/v1/nid/me";

        NaverProfile naverProfile = null;
        try{
            response=restTemplate.exchange(redirect_uri, HttpMethod.POST, request, String.class);
            naverProfile = objectMapper.readValue(response.getBody(), NaverProfile.class);
        }catch(HttpClientErrorException e){
            log.info("[REJECT]naver login error");

        }catch(JsonProcessingException e){
            log.info("[REJECT]naverMapper error");
        }
        return naverProfile.getNaver_account().getEmail();
    }
}
