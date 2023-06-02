package com.tave.connectX.controller;

import com.tave.connectX.dto.OAuthToken;
import com.tave.connectX.dto.OAuthUserInfo;
import com.tave.connectX.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
