package com.tave.connectX.service;

import com.tave.connectX.dto.OAuthToken;
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
    @Value(value = "${oauth.secret.key}")
    private String RESTAPIKEY = "TAVE11TH";
    @Value(value = "${oauth.redirect.uri}")
    private String REDIRECTURI = "TAVE11TH";

    public OAuthToken getKakaoAccessToken(String authorizationCode) {

        try {

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


}
