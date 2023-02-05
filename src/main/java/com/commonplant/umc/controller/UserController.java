package com.commonplant.umc.controller;

import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseBody
    @GetMapping("/kakao")
    public ResponseEntity<JsonResponse> kakaoCallback(@RequestParam String code){
        String response = "Load Kakao API successfully!";
        return ResponseEntity.ok(new JsonResponse(true, 200, "getKakaoApi",response));
    }

}
