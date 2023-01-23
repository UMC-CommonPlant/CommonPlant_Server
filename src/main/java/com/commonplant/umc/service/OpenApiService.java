package com.commonplant.umc.service;


import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenApiService {

    public class weather {
        private final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService";
        private final String apiUri = "/areaBasedList";
        private final String serviceKey = "?ServiceKey=EZjOvqj85o7yR0aFgwTuDi/ZkFZLW/yvKTiEHt7vVhu8SkJh1Nv3sqE7WnLBe1GZZO37bstjM3W//QSoNSBpnw==";
        private final String defaultQueryParam = "&MobileOS=ETC&MobileApp=AppTest&_type=json";
        private final String numOfRows = "&numOfRows=100";
        private final String baseDate = "&base_date=";
        private final String baseTime = "&base_time=";
        private final String nx = "&nx=";
        private final String ny = "&ny=";

        private String makeUrl(String x, String y) throws UnsupportedEncodingException {

            String date = "20230121";
            String time = "0600";

            return BASE_URL +
                    apiUri +
                    serviceKey +
                    defaultQueryParam +
                    numOfRows +
                    baseDate + date +
                    baseTime + time +
                    nx + x +
                    ny + y;
        }

        public ResponseEntity<?> fetch(String x, String y) throws UnsupportedEncodingException {
            System.out.println(makeUrl(x, y));
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<Map> resultMap = restTemplate.exchange(makeUrl(x, y), HttpMethod.GET, entity, Map.class);
            System.out.println(resultMap.getBody());
            return resultMap;

        }
    }
    // 주소 위도 경도 변환

        @Transactional
        public Float[] findGeoPoint(String location) {

            if (location == null)
                return null;

            // setAddress : 변환하려는 주소 (경기도 성남시 분당구 등)
            // setLanguate : 인코딩 설정
            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("ko").getGeocoderRequest();

            Geocoder geocoder = new Geocoder();
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

            if (geocoderResponse.getStatus() == GeocoderStatus.OK & !geocoderResponse.getResults().isEmpty()) {
                GeocoderResult geocoderResult = geocoderResponse.getResults().iterator().next();
                LatLng latitudeLongitude = geocoderResult.getGeometry().getLocation();

                Float[] coords = new Float[2];
                coords[0] = latitudeLongitude.getLat().floatValue();
                coords[1] = latitudeLongitude.getLng().floatValue();

                return coords;
            }

            return null;
        }


}
