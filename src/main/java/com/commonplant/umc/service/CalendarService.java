package com.commonplant.umc.service;

import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.calendar.CalendarResponse;
import com.commonplant.umc.repository.MemoRepository;
import com.commonplant.umc.repository.PlantRepository;
import com.commonplant.umc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public CalendarResponse.calendarCardListRes getCalendar(String userEmail) {

        // loginType 인자는 로그인한 user의 정보를 가져와야 함
        // 현재는 임의로 "kakao"를 넣어줌
        User user = userRepository.findUserByEmail(userEmail,"kakao");

        // created_at, watered_date 불러오기
        // userCalendarPlantList Getter
        List<Plant> userCalendarPlantCreatedAtList = plantRepository.findAllByUser(user);

        List<CalendarResponse.plantCreatedAtRes> plantCreatedAtListDto
                = userCalendarPlantCreatedAtList.stream().
                map(plant -> new CalendarResponse.plantCreatedAtRes(
                                plant.getPlantIdx(),
                                plant.getName(),
                                plant.getNickname(),
                                plant.getPlace().getName(),
                                plant.getImgUrl(),
                                plant.getCreatedAt()
                        )
                ).collect(Collectors.toList());

        // userCalendarPlantList Setter
        // List<List> plantAllCreatedAtList = new ArrayList<>();
        List<CalendarResponse.plantCreatedAtRes> plantListByCreatedAt = new ArrayList<>();

        for(int i = 0; i < plantCreatedAtListDto.size(); i++){
            plantListByCreatedAt = new ArrayList<>();
            plantListByCreatedAt.add(plantCreatedAtListDto.get(i));

            // plantAllCreatedAtList.add(plantListByCreatedAt);
        }



        // userCalendarPlantList Getter
        List<Plant> userCalendarPlantWateredDateList = plantRepository.findAllByUser(user);

        List<CalendarResponse.plantWateredDateRes> plantWateredDateListDto
                = userCalendarPlantWateredDateList.stream().
                map(plant -> new CalendarResponse.plantWateredDateRes(
                                plant.getPlantIdx(),
                                plant.getName(),
                                plant.getNickname(),
                                plant.getPlace().getName(),
                                plant.getImgUrl(),
                                plant.getWateredDate()
                        )
                ).collect(Collectors.toList());

        // userCalendarPlantList Setter
        // List<List> plantAllWateredDateList = new ArrayList<>();
        List<CalendarResponse.plantWateredDateRes> plantListByWateredDate = new ArrayList<>();

        for(int i = 0; i < plantWateredDateListDto.size(); i++){
            plantListByWateredDate = new ArrayList<>();
            plantListByWateredDate.add(plantWateredDateListDto.get(i));

            // plantAllWateredDateList.add(plantListByWateredDate);
        }



//        CalendarResponse.calendarCardListRes calendarCardListRes =
//                new CalendarResponse.calendarCardListRes(plantAllCreatedAtList,plantAllWateredDateList);
        CalendarResponse.calendarCardListRes calendarCardListRes =
                new CalendarResponse.calendarCardListRes(plantListByCreatedAt,plantListByWateredDate);

        return calendarCardListRes;
    }
}
