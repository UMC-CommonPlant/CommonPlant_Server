
package com.commonplant.umc.controller;


import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.dto.place.PlaceResponse;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.service.UserService;
import com.commonplant.umc.service.PlaceService;
import com.commonplant.umc.utils.weather.OpenApiService;
import com.commonplant.umc.utils.weather.TransLocalPoint;
import com.commonplant.umc.utils.weather.Weather;
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
    private final JwtService jwtService;

    private final TransLocalPoint transLocalPoint;


    // myGarden 메인페이지
    @GetMapping("/place/myGarden")
    public ResponseEntity<JsonResponse> getMyGarden()
    {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        // user Info
        String nickName = user.getNickName();

        // place List Info
        List<PlaceResponse.placeList> placeLists = placeService.getUserPlaceList(user);

        // plant List Info
        List<PlaceResponse.plantList> plantLists = placeService.getUserPlantList(user);



        return ResponseEntity.ok(new JsonResponse(true, 200,"myGarden", new PlaceResponse.getMainPage(nickName,placeLists,plantLists)));
    }

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
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        // place info
        Place place = placeService.getPlace(placeCode);

        // weather info
        Weather.weatherInfo weather = null;

        // isOwner ( 내가 이 장소의 리더인지 아닌지)
        Boolean isOwner = placeService.isOwner(user, place);

        // userInfo
        List<PlaceResponse.userInfoList> userInfoList = placeService.getUserListOfPlace(place);

        // plantInfo
        List<PlantResponse.plantOfPlaceRes> plantInfoList = placeService.getPlantListOfPlace(place);


        return ResponseEntity.ok(new JsonResponse(true, 200,"getPlace", new PlaceResponse.getPlaceInfo(place, isOwner, userInfoList, plantInfoList)));
    }

    // 사용자가 속한 장소리스트
    @GetMapping("/place/userPlace")
    public ResponseEntity<JsonResponse> getUserPlaceList() {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        List<Place> places = placeService.getUserPlaces(user);


        return ResponseEntity.ok(new JsonResponse(true, 200,"userPlace", places));
    }


    @GetMapping("/openApiTest/{placeCode}")
    public ResponseEntity<JsonResponse> openApiTest(@PathVariable String placeCode)
    {
        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Place place = placeService.getPlace(placeCode);

        String address = "대전광역시 유성구 궁동";
        String json = openApiService.getKakaoApiFromAddress(address);
        HashMap<String, String> xy = openApiService.getXYMapfromJson(json);

        String x = xy.get("x");
        String y = xy.get("y");

        System.out.println(x);
        System.out.println(y);

        double lat = Double.parseDouble(x);
        double lng = Double.parseDouble(y);

        Weather.LatXLngY rs = transLocalPoint.convertGRID_GPS(0, lat, lng);

        int grid_x = (int)rs.getX();
        int grid_y = (int)rs.getY();

        x = Integer.toString(grid_x);
        y = Integer.toString(grid_y);

        try {
            String url = openApiService.makeUrl(x, y);
            openApiService.fetch(url);
        }
        catch(UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(new JsonResponse(true, 200,"getPlace", place));
    }



    // ------------------------------- 친구 검색 / 조회 / 삭제 / 추가  --------------------------------

    // 유저 검색
    @PutMapping("/place/searchPeople")
    public ResponseEntity<JsonResponse> searchPeople(@RequestBody PlaceRequest.searchPeople req){

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String input = req.getName();
        List<User> users = placeService.searchPeople(input);

        return ResponseEntity.ok(new JsonResponse(true, 200, "searchPeople", users));
    }

    // 참여인원 추가
    @PutMapping("/place/addPeople")
    public ResponseEntity<JsonResponse> addPeople(@RequestBody PlaceRequest.addPeople req){

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String placeCode = placeService.addPeople(req.getName(), req.getPlaceCode());

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPeople", placeCode)) ;
    }



}

