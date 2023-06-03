package com.tave.connectX.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthUserInfo {

    private Map<String, Object> attributes;
    private String oAuthId;         // 유저 식별자
    private String profileImage;    // 프로필 이미지 링크
    private String nickName;        // 소셜 닉네임

    private OAuthUserInfo(Map<String, Object> attributes, String oAuthId, String profileImage, String nickName) {
        this.attributes = attributes;
        this.oAuthId = oAuthId;
        this.profileImage = profileImage;
        this.nickName = nickName;
    }

    public static OAuthUserInfo ofKakao(Map<String, Object> attributes) {

        // 유저 식별자
        String oAuthId = String.valueOf(attributes.get("id"));

        Map<String, String> properties = (Map<String, String>) attributes.get("properties");

        // 닉네임
        String nickName = properties.get("nickname");

        // Image url - thumbnail_image : 110px * 110px 또는 100px * 100px // profile_image : 640px * 640px 또는 480px * 480px
        String imageUrl = properties.get("profile_image");

        return new OAuthUserInfo(attributes, oAuthId, imageUrl, nickName);
    }


}
