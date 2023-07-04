package com.tave.connectX.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "ConnectX API 명세서",
                description = "로그인 이후 요청마다 발급한 JWT 토큰을 담아서 보내기, 우측 Authorize 버튼에 발급한 토큰을 넣으면 매 요청마다 헤더에 자동으로 들어갑니다.",
                version = "v1"),
        security = {
                @SecurityRequirement(name = "Authorization")
        }
)
@SecuritySchemes({
        @SecurityScheme(name = "Authorization",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization")
})
@Configuration
public class SwaggerConfig {
}
