package com.commonplant.umc.controller;


import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.utils.weather.Weather;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.utils.weather.OpenApiService;
import com.commonplant.umc.service.PlaceService;
import com.commonplant.umc.service.UserService;
import com.commonplant.umc.utils.weather.TransLocalPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final UserService userService;
    private final OpenApiService openApiService;
    private final TransLocalPoint transLocalPoint;

    // 장소 추가
    @PostMapping("/place/add")
    public ResponseEntity<JsonResponse> addPlace(@RequestPart("place") PlaceRequest.addPlace req, @RequestPart("image") MultipartFile file){

        User user = userService.getUser(1l);

        String placeCode = placeService.addPlace(user, req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200,"addPlace", placeCode));
    }

    // 장소 정보 조회
    @GetMapping("/place/{placeCode}")
    public ResponseEntity<JsonResponse> getPlaceInfo(@PathVariable String placeCode) throws UnsupportedEncodingException {
        //User Validation
        /*
        String userId = jwtService.resolveToken();
        User user = userService.getUser(userId);
        */

        Place place = placeService.getPlace(placeCode);
        // 날씨 정보 api 구현 필요
        try{
            String url = openApiService.makeUrl(place.getGirdX(), place.getGridY());
            System.out.println(openApiService.fetch(url));
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }


        return ResponseEntity.ok(new JsonResponse(true, 200,"getPlace", place));
    }
    // 장소 정보 수정
    @PutMapping("/place/updateInfo")
    public ResponseEntity<JsonResponse> updatePlaceInfo(@RequestPart("place") PlaceRequest.addPlace req, @RequestPart("image") MultipartFile file)
    {

        User user = userService.getUser(1l);

        String placeCode = placeService.addPlace(user, req, file);

        Place place = placeService.getPlace(placeCode);

        return ResponseEntity.ok(new JsonResponse(true, 200,"updatePlace", place));
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

        String y = xy.get("x");
        String x = xy.get("y");

        double lat = Double.parseDouble(x);
        double lng = Double.parseDouble(y);

        Weather.LatXLngY rs =transLocalPoint.convertGRID_GPS(0, lat, lng);

        double lat_x = rs.getX();
        double lat_y = rs.getY();

        System.out.println(lat_x);
        System.out.println(lat_y);

        x = Double.toString(lat_x);
        y = Double.toString(lat_y);

        try {
            String url = openApiService.makeUrl(x, y);
            openApiService.fetch(url);
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok(new JsonResponse(true, 200,"getPlace", place));
    }



    // ------------------------------- 친구 검색 / 조회 / 삭제 / 추가  --------------------------------

    @GetMapping("place/searchPeople")
    public ResponseEntity<JsonResponse> searchPeople(@RequestBody PlaceRequest.searchPeople req){

        //User Validation
        /*
        String userId = jwtService.resolveToken();
        User user = userService.getUser(userId);
        */
        String input = req.getName();
        List<User> users = placeService.searchPeople(input);

        return ResponseEntity.ok(new JsonResponse(true, 200, "searchPeople", users));
    }

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
