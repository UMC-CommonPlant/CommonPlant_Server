package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.calendar.CalendarResponse;
import com.commonplant.umc.service.CalendarService;
import com.commonplant.umc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/plant/myCalendar")
    public ResponseEntity<JsonResponse> getCalendar(){

        String uuid = jwtService.resolveToken();

        // user의 PK: email
        String calendarUserEmail = userService.getUser(uuid).getEmail();

        System.out.println(calendarUserEmail);

        // 리스트 2개를 담는 calendarCardListRes
        CalendarResponse.calendarCardListRes calendarCardLists = calendarService.getCalendar(calendarUserEmail);

        // calendarCardLists를 인자로 넣기
        return ResponseEntity.ok(new JsonResponse(true, 200,"myCalendar", calendarCardLists));
    }

}
