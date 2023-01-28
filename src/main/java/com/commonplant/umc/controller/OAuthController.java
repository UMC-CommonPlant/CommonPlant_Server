package com.commonplant.umc.controller;

import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
public class OAuthController{

    private final OAuthService oAuthService;

    //front : code -> accessToken
    //back : accessToken -> userDetail
    @PostMapping("/test/login/{loginType}")
    public ResponseEntity<JsonResponse> login(@RequestParam("accessToken") String accessToken, @PathVariable String loginType){
        System.out.println("accessToken" + accessToken);
        Object userInfo = oAuthService.getUserInfo(accessToken);

        return ResponseEntity.ok(new JsonResponse(true, 200, "login", userInfo));
    }
}