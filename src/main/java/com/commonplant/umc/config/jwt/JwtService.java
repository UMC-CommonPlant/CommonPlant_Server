package com.commonplant.umc.config.jwt;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.*;


//ACTOKEN과 별개로 REFTOKEN은 DB에서 꺼내와 검증해야하기 떄문에
//시큐리티 필터를 타는 ACTOKEN과는 별개로 REFTOKEN에 맞는 유효성 검증 객체가 필요했다.
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private String secretKey= JwtSecret.SECRET;
    private final UserRepository userRepository;


    //객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init(){
        secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    //header에서 ACTOKEN추출
    public String resolveToken(){
        String token = getJwt();
        validateToken(token);
        String uuid = getUserPk(token);
        userRepository.findUserByUuid(uuid).orElseThrow(()->new BadRequestException(FAILED_TO_LOGIN_JWT));
        return uuid;
    }

    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-AUTH-TOKEN");
        /* return request.getHeader("AUTHORIZATION_HEADER");*/
    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token){
        try{
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }catch (NullPointerException e){
            log.info(e.getMessage());
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }catch(ExpiredJwtException e){
            log.error("getUserPK : "+e.getMessage());
            return "getUserPK : Expired Token";
        }
    }

    public String createToken(String userUUID){
        Claims claims = Jwts.claims().setSubject(userUUID); // JWT payload 에 저장되는 정보단위
//        claims.put("roles", "ROLE_USER"); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        //Access Token
        String acToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtSecret.EXPIRATION_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();


        return acToken;
    }



    //토큰의 유효성 + 만료 일자 확인
    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. at validation Token");
            throw new BadRequestException(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }catch (Exception e) {
            return false;
        }
        return false;
    }


}
