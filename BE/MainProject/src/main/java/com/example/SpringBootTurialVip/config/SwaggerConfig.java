package com.example.SpringBootTurialVip.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//Cấu hình swagger
@Configuration
public class SwaggerConfig {


//    @Bean
//    public OpenAPI openAPI(){
//        return new OpenAPI().info(new Info().title("NJS1803_API_Group6_Document").
//                version("1.0.0")
//                .description("Tài liệu đọc API !!!")
//                .license(new License().name("API License").url("http://domain.vn/license")))
//                .servers(List.of(new Server().url("http://localhost:8080").description("Server_test")))
//                .components(//cấu hình cho phép nhập token để request
//                        new Components()
//                                .addSecuritySchemes(
//                                        "beareAuth",
//                                        new SecurityScheme()
//                                                .type(SecurityScheme.Type.HTTP)
//                                                .scheme("bearer")
//                                                .bearerFormat("JWT")))
//                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
//
//    }
//
//        @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("api-service-1")
//                .pathsToMatch("/**")//vị trí scan package controller
//                .build();
//    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NJS1803_API_Group6_Document")
                        .version("1.0.0")
                        .description("Tài liệu đọc API !!!")
                        .license(new License().name("API License").url("http://domain.vn/license")))
                .servers(List.of(new Server().url("http://localhost:8080").description("Server_test")))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth", //  Đổi lại đúng tên
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth"))); //  Đổi lại đúng tên
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api-service-1")
                .pathsToMatch("/**") // Quét tất cả các API trong dự án
                .build();
    }
}

