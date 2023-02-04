package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.user.UserRequest;
import com.commonplant.umc.repository.UserRepository;
import com.commonplant.umc.service.OAuthService;
import com.commonplant.umc.service.UserService;
import com.google.api.client.json.Json;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


@RestController
@RequiredArgsConstructor
public class OAuthController{

    private final OAuthService oAuthService;
    private final JwtService jwtService;
    private final UserService userService;

    private static final String CLIENT_ID = "";
    private static final String REDIRECT_URL = "";

    //front : code -> accessToken
    //back : accessToken -> userDetail
    @PostMapping("/users/login/{loginType}")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String loginType){
        System.out.println("accessToken" + accessToken);
        String token = oAuthService.oauthLogin(accessToken, loginType);
        return ResponseEntity.ok(new JsonResponse(true, 200, "login", token));

    }


    @PostMapping("/users/join")
    public ResponseEntity<JsonResponse> join(@RequestPart("user") UserRequest.join req, @RequestPart("image") MultipartFile image){
        System.out.println("join");
        String token = oAuthService.joinUser(req, image);
        return ResponseEntity.ok(new JsonResponse(true, 200, "join", token));
    }

    @GetMapping("/test/token")
    public String getUUIDByToken() {
        String uuid = jwtService.resolveToken();
        return uuid;
    }

    @GetMapping("/test/token/user")
    public User getUserTest() {
        String uuid = jwtService.resolveToken();//만료 여부 확인
        User user = userService.getUser(uuid);//Place, Plant, Memo에서는 uuid, user 사용
        return user;
    }


    private HttpEntity<MultiValueMap<String, String>> generateAuthCodeRequest(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpEntity<>(generateParam(accessToken), headers);
    }

    private MultiValueMap<String, String> generateParam(String accessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URL);
        params.add("accessToken", accessToken);
        return params;
    }
}