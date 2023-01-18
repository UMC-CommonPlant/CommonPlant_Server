package com.commonplant.umc.controller;


import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.service.PlaceService;
import com.commonplant.umc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    private final UserService userService;

    @PostMapping("/place/add")
    public ResponseEntity<JsonResponse> addPlace(@RequestPart("place") PlaceRequest.addPlace req, @RequestPart("image") MultipartFile file){

        User user = userService.getUser(1l);

        String placeCode = placeService.addPlace(user, req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200,"addPlace", placeCode));
    }

    @GetMapping("/place/{placeCode}")
    public ResponseEntity<JsonResponse> getPlaceInfo(@PathVariable String placeCode)
    {
        //User Validation
        /*
        String userId = jwtService.resolveToken();
        User user = userService.getUser(userId);
        */

        Place place = placeService.getPlace(placeCode);



        return ResponseEntity.ok(new JsonResponse(true, 200,"addPlace", placeCode));
    }

    @PutMapping("/place/updatePeople")
    public ResponseEntity<JsonResponse> updatePlaceInfo(@RequestBody PlaceRequest.updatePlace req){


        return null;
    }



}
