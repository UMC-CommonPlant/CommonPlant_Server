package com.commonplant.umc.service;

import com.commonplant.umc.dto.user.KakaoProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService{
    private final ObjectMapper objectMapper;

    public Object kakaoLogin(String accessToken, String loginType){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response;
        headers.add("Authorization", "Bearer " + accessToken);

        if(loginType.equals("kakao")){
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            String redirect_uri="https://kapi.kakao.com/v2/user/me";

            Object kakaoProfile = null;
            try{
                response=restTemplate.exchange(redirect_uri, HttpMethod.POST, request, String.class);
                kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
            }catch(HttpClientErrorException e){
                log.info("[REJECT]kakao login error");

            }catch(JsonProcessingException e){
                log.info("[REJECT]kakaoMapper error");
            }
            return kakaoProfile;
        }
        return null;
    }


    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode =" + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body ="+result);

            Gson gson = new Gson();
            JsonElement element = gson.fromJson(result, JsonElement.class);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String name = properties.getAsJsonObject().get("name").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("name", name);
            userInfo.put("email", email);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
