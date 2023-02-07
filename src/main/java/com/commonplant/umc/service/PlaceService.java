
package com.commonplant.umc.service;


import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.domain.*;
import com.commonplant.umc.dto.place.PlaceResponse;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.repository.*;
import com.commonplant.umc.utils.weather.OpenApiService;
import com.commonplant.umc.utils.weather.Weather;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.utils.weather.TransLocalPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    private final PlantRepository plantRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public String addPlace(User user, PlaceRequest.addPlace req, MultipartFile file) {
        String newCode = randomCode();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_place/" + newCode, file);
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

    public List<Place> getUserPlaces(User user)
    {
        List<Place> places = belongRepository.findAllByUserOrderByCreatedAtAsc(user);

        return places;
    }

    // ------------------------------- 친구 검색 / 추가  --------------------------------
    public List<User> searchPeople(String input) {
        List<User> users = userRepository.findBynickNameContains(input);

        return users;
    }

    public String addPeople(String name, String placeCode) {
        User newUser = userRepository.findBynickName(name);
        Place place = getPlace(placeCode);

        if(ExistUserInPlace(newUser, place))
        {
            return "이미 장소에 존재하는 유저입니다.";
        }

        Belong belong = Belong.builder().user(newUser).place(place).build();
        belongRepository.save(belong);

        return place.getCode();
    }

    // --------------------------------- 장소 참여 인원 관련 -------------------------

    public Boolean isOwner(User user, Place place) {

        if(user == place.getOwner()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean ExistUserInPlace(User user, Place place) {
        Optional<Belong> findUser = belongRepository.findByUserAndPlace(user, place);

        if (!findUser.isEmpty())
            return true;

        return false;
    }

    public List<PlaceResponse.userInfoList> getUserListOfPlace(Place place){

        List<User> users =  belongRepository.findAllByPlaceOrderByCreatedAtAsc(place);
        List<PlaceResponse.userInfoList> userInfoList = new ArrayList<>();

        for(User u: users)
        {
            boolean isOwner = isOwner(u, place);
            userInfoList.add(new PlaceResponse.userInfoList(u, isOwner));
        }

        return userInfoList;
    }

    public List<PlantResponse.plantOfPlaceRes> getPlantListOfPlace(Place place){
        List<Plant> plants = plantRepository.findAllByPlaceOrderByRemainderDate(place);
        System.out.println("Plant: " + plants);
        List<PlantResponse.plantOfPlaceRes> plantList = new ArrayList<>();

        for(Plant p : plants)
        {
            List<Memo> memoList =  memoRepository.findAllByPlantIdxOrderByCreatedAtDesc(p.getPlantIdx());
            System.out.println("memoList: " + memoList);
            if(memoList.isEmpty())
            {
                continue;
            }
            Memo recent = memoList.get(0);
            System.out.println(recent.getMemoIdx());
            String recentMemo = recent.getContent();
            plantList.add(new PlantResponse.plantOfPlaceRes(p,recentMemo));
        }

       return plantList;
    }

    // ------------------------------ 기타 -----------------------------------


    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }
}

