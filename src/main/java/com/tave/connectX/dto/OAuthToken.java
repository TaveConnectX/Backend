package com.tave.connectX.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthToken {

    private String accessToken;
    private String refreshToken;

    public OAuthToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
