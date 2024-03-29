package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.user.UserRequest;
import com.commonplant.umc.repository.UserRepository;
import com.commonplant.umc.service.OAuthService;
import com.commonplant.umc.service.UserService;
import com.google.api.client.json.Json;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class OAuthController{

    private final OAuthService oAuthService;
    private final JwtService jwtService;
    private final UserService userService;


    //front : code -> accessToken
    //back : accessToken -> userDetail
    @PostMapping("/users/login/{loginType}")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String loginType){
        System.out.println("accessToken : " + accessToken);
        String token = oAuthService.oauthLogin(accessToken, loginType);

        return ResponseEntity.ok(new JsonResponse(true, 200, "login", token));
    }


    @PostMapping("/users/join")
    public ResponseEntity<JsonResponse> join(@RequestPart("user") UserRequest.join req, @RequestPart("image") MultipartFile image){
        System.out.println("join");
        String token = oAuthService.joinUser(req, image);

        return ResponseEntity.ok(new JsonResponse(true, 200, "join", token));
    }

    @GetMapping("/users/exception")
    public void exceptionTest() throws RuntimeException{
        throw new RuntimeException("Access Denied.");
    }
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<JsonResponse> ExceptionHandler(RuntimeException e){
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.ok(new JsonResponse(false, 400, "Exception", httpStatus));
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
}