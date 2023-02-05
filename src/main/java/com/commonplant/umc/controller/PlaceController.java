
package com.commonplant.umc.controller;


import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.service.UserService;
import com.commonplant.umc.service.PlaceService;
import com.commonplant.umc.utils.weather.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final UserService userService;
    private final OpenApiService openApiService;

    private final JwtService jwtService;

     // 장소 추가
     @PostMapping("/place/add")
     public ResponseEntity<JsonResponse> addPlace(@RequestPart("place") PlaceRequest.addPlace req, @RequestPart("image") MultipartFile file){

          String uuid = jwtService.resolveToken();
          User user = userService.getUser(uuid);

         String placeCode = placeService.addPlace(user, req, file);

         return ResponseEntity.ok(new JsonResponse(true, 200,"addPlace", placeCode));
     }

    // 장소 정보 조회
    @GetMapping("/place/{placeCode}")
    public ResponseEntity<JsonResponse> getPlaceInfo(@PathVariable String placeCode) {
        //User Validation
        /*
        String userId = jwtService.resolveToken();
        User user = userService.getUser(userId);
        */

        Place place = placeService.getPlace(placeCode);
        // 날씨 정보 api 구현 필요


        return ResponseEntity.ok(new JsonResponse(true, 200,"getPlace", place));
    }

    @GetMapping("/openApiTest/{placeCode}")
    public ResponseEntity<JsonResponse> openApiTest(@PathVariable String placeCode)
    {
        //User Validation
        /*
        String userId = jwtService.resolveToken();
        User user = userService.getUser(userId);
        */

        Place place = placeService.getPlace(placeCode);

        String address = "대전광역시 유성구 궁동";
        String json = openApiService.getKakaoApiFromAddress(address);
        HashMap<String, String> xy = openApiService.getXYMapfromJson(json);

        String x = xy.get("x");
        String y = xy.get("y");

        System.out.println(x);
        System.out.println(y);

        place.setGirdX(x);
        place.setGridY(y);

        return ResponseEntity.ok(new JsonResponse(true, 200,"getPlace", place));
    }



    // ------------------------------- 친구 검색 / 조회 / 삭제 / 추가  --------------------------------

//    @GetMapping("place/searchPeople")
//    public ResponseEntity<JsonResponse> searchPeople(@RequestBody PlaceRequest.searchPeople req){
//
//        //User Validation
//        /*
//        String userId = jwtService.resolveToken();
//        User user = userService.getUser(userId);
//        */
//        String input = req.getName();
//        List<User> users = placeService.searchPeople(input);
//
//        return ResponseEntity.ok(new JsonResponse(true, 200, "searchPeople", users));
//    }

    @PutMapping("/place/addPeople")
    public ResponseEntity<JsonResponse> addPeople(@RequestBody PlaceRequest.updatePlace req){

        //User Validation
        /*
        String userId = jwtService.resolveToken();
        User user = userService.getUser(userId);
        */


        return ResponseEntity.ok(new JsonResponse(true, 200, "addPeople", null)) ;
    }





}

