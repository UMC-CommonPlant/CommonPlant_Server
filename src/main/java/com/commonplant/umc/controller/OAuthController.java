package com.commonplant.umc.controller;

import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.service.OAuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


@RestController
@RequiredArgsConstructor
public class OAuthController{

    private final OAuthService oAuthService;

    private static final String CLIENT_ID = "";
    private static final String REDIRECT_URL = "";

    //front : code -> accessToken
    //back : accessToken -> userDetail
    @PostMapping("/users/login/kakao")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String kakao){
        System.out.println("accessToken" + accessToken);
        Object userInfo = oAuthService.getUserInfo(accessToken);

        return ResponseEntity.ok(new JsonResponse(true, 200, "login", userInfo));
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