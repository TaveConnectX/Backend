package com.tave.connectX.controller;

import com.tave.connectX.dto.OAuthToken;
import com.tave.connectX.dto.OAuthUserInfo;
import com.tave.connectX.dto.User;
import com.tave.connectX.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final OAuthService oAuthService;

    @GetMapping("/login/{code}")
    public ResponseEntity responseEntity(@PathVariable String code) {

        OAuthToken kakaoAccessToken = oAuthService.getKakaoAccessToken(code);
        OAuthUserInfo userInfo = oAuthService.getUserInfo(kakaoAccessToken);

        return ResponseEntity.ok(userInfo.getOAuthId() + userInfo.getNickName() +", " + userInfo.getProfileImage());
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody User user, HttpServletResponse response) {
        try {
            oAuthService.login(user, response);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info("", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
