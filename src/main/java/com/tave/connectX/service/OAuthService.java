package com.tave.connectX.service;

import com.tave.connectX.dto.OAuthToken;
import com.tave.connectX.dto.OAuthUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OAuthService {
    static final String URI_KAKAO = "https://kauth.kakao.com";
    static final String URI_KAKAO_API = "https://kapi.kakao.com";

    @Value(value = "${oauth.secret.key}")
    private String RESTAPIKEY = "TAVE11TH";
    @Value(value = "${oauth.redirect.uri}")
    private String REDIRECTURI = "TAVE11TH";

    // 프론트에서 인가 코드를 받아 토큰 받아오는 메서드
    public OAuthToken getKakaoAccessToken(String authorizationCode) {

        try {

            /**
             * [KAKAO REST API] 요청할 정보 입력 - URI, Headers, Body( Parameter )
             */
            
            // set uri
            String requestUrl = URI_KAKAO + "/oauth/token";

            // set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // set body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", RESTAPIKEY);
            body.add("redirect_uri", REDIRECTURI);
            body.add("code", authorizationCode);

            HttpEntity httpEntity = new HttpEntity(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> exchange = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, Map.class);
            Map attributes = exchange.getBody();

            String accessToken = (String) attributes.get("access_token");
            String refreshToken = (String) attributes.get("refresh_token");

            return new OAuthToken(accessToken, refreshToken);
        } catch (RuntimeException e) {
            return null;
        }
    }

    // 토큰으로 사용자 정보 받아오는 메서드
    public OAuthUserInfo getUserInfo(OAuthToken oAuthToken) {

        /**
         * [KAKAO REST API] 요청할 정보 입력 - URI, Headers
         */

        String requestUri = URI_KAKAO_API + "/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + oAuthToken.getAccessToken());

        HttpEntity httpEntity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> attributes = restTemplate.exchange(requestUri, HttpMethod.GET, httpEntity, Map.class).getBody();

        OAuthUserInfo oAuthUserInfo = OAuthUserInfo.ofKakao(attributes);

        return oAuthUserInfo;
    }


}
