package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.user.UserRequest;
import com.commonplant.umc.repository.UserRepository;
import com.commonplant.umc.service.UserService;
import com.commonplant.umc.utils.UuidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/users/myProfile")
    public ResponseEntity<JsonResponse> getUserProfile(){
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

       User userProfile = userService.getUser(uuid);
        return ResponseEntity.ok(new JsonResponse(true, 200, "getUserProfile", userProfile));
    }
    @PutMapping("/users/myProfile/{user_idx}")
    public ResponseEntity<JsonResponse> updateUserProfile(@PathVariable Long user_idx, @RequestPart("user") UserRequest.update req){
        System.out.println("update");
        User updateuser = userService.updateUser(req);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateUser", updateuser));
    }
}
