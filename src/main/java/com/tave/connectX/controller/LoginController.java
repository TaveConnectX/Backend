package com.tave.connectX.controller;

import com.tave.connectX.dto.OAuthToken;
import com.tave.connectX.dto.OAuthUserInfo;
import com.tave.connectX.dto.User;
import com.tave.connectX.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final OAuthService oAuthService;

    @GetMapping("/login/{code}")
    public ResponseEntity responseEntity(@PathVariable String code, HttpServletResponse response) {
        try {
            OAuthToken kakaoAccessToken = oAuthService.getKakaoAccessToken(code);
            OAuthUserInfo userInfo = oAuthService.getUserInfo(kakaoAccessToken);

            User user = new User(userInfo.getOAuthId(), userInfo.getNickName());
            oAuthService.login(user, response);

            return ResponseEntity.ok(userInfo.getProfileImage());
        } catch (Exception e) {
            log.info("", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
