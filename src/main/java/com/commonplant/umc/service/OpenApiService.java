package com.commonplant.umc.service;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenApiService {

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

    // 주소 위도 경도 변환

    public String getKakaoApiFromAddress(String roadFullAddr) {
        String apiKey = "a2810be90f591c5686c3d095d19e6461";
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        String jsonString = null;

        try {
            roadFullAddr = URLEncoder.encode(roadFullAddr, "UTF-8");

            String addr = apiUrl + "?query=" + roadFullAddr;

            URL url = new URL(addr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer docJson = new StringBuffer();

            String line;

            while ((line=rd.readLine()) != null) {
                docJson.append(line);
            }

            jsonString = docJson.toString();
            rd.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public HashMap<String, String> getXYMapfromJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> XYMap = new HashMap<String, String>();

        try {
            TypeReference<Map<String, Object>> typeRef
                    = new TypeReference<Map<String, Object>>(){};
            Map<String, Object> jsonMap = mapper.readValue(jsonString, typeRef);

            @SuppressWarnings("unchecked")
            List<Map<String, String>> docList
                    =  (List<Map<String, String>>) jsonMap.get("documents");

            Map<String, String> adList = (Map<String, String>) docList.get(0);
            XYMap.put("x",adList.get("x"));
            XYMap.put("y", adList.get("y"));

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return XYMap;
    }



}
