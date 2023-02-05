
package com.commonplant.umc.service;


import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.domain.Belong;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.utils.weather.OpenApiService;
import com.commonplant.umc.utils.weather.Weather;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.repository.BelongRepository;
import com.commonplant.umc.repository.PlaceRepository;
import com.commonplant.umc.repository.UserRepository;
import com.commonplant.umc.utils.weather.TransLocalPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.PLACE_CODE_ERROR;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final BelongRepository belongRepository;
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    private final OpenApiService openApiService;

    private final TransLocalPoint transLocalPoint;


    @Transactional
    public String addPlace(User user, PlaceRequest.addPlace req, MultipartFile file) {
        String newCode = randomCode();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_"+newCode,file);
              }
        // 주소 좌표 변환
        String json = openApiService.getKakaoApiFromAddress(req.getAddress());
        HashMap<String, String> xy = openApiService.getXYMapfromJson(json);

        String y = xy.get("x");
        String x = xy.get("y");

        double lat = Double.parseDouble(x);
        double lng = Double.parseDouble(y);

        Weather.LatXLngY rs =transLocalPoint.convertGRID_GPS(0, lat, lng);


        int grid_x = (int)rs.getX();
        int grid_y = (int)rs.getY();

        x = Integer.toString(grid_x);
        y = Integer.toString(grid_y);

        Place place = Place.builder().name(req.getName()).owner(user).address(req.getAddress())
                .gridX(x).gridY(y).placeImgUrl(imgUrl).code(newCode).build();
        placeRepository.save(place);

        Belong belong = Belong.builder().user(user).place(place).build();
        belongRepository.save(belong);

        return place.getCode();
    }

    public Place getPlace(String placeCode) {
        Place place = placeRepository.findByCode(placeCode).orElseThrow(()->new BadRequestException(PLACE_CODE_ERROR));

        return place;
    }

    // ------------------------------- 친구 검색 / 조회 / 삭제 / 추가  --------------------------------
    public List<User> searchPeople(String input) {
        List<User> users = userRepository.findByNameContains(input);


        return users;
    }


    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }
}

