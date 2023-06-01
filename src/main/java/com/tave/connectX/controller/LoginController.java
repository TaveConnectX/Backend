package com.tave.connectX.controller;

import com.tave.connectX.dto.OAuthToken;
import com.tave.connectX.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final OAuthService oAuthService;

    @GetMapping("/login/{code}")
    public ResponseEntity responseEntity(@PathVariable String code) {

        OAuthToken kakaoAccessToken = oAuthService.getKakaoAccessToken(code);



        return ResponseEntity.ok().build();
    }

}
