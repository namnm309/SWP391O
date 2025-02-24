package com.example.SpringBootTurialVip.config;

import com.example.SpringBootTurialVip.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;

@Configuration//init và run các public method có @Bean vào ApllicationContext(IOC Container)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {



    @Value("${jwt.signerKey}")
    private String signerKey;

    //Tạo biến để cho phép các endpoint
    private final String [] PUBLIC_ENDPOINT={
            "/auth/loginToken",
            "/auth/verifyToken",
            "/users/createUser",
            "/users/verify",
            "/users/resend",
            "home/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        //Cấu hình endpoint nào cần bảo vệ và endpoint nào ko cần
        //Cụ thể : sign up user , log in page ,...
        httpSecurity.authorizeHttpRequests(request -> //lambda function
                request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,PUBLIC_ENDPOINT)//chỉ cho phép admin truy cập vào api này
                        .permitAll()
                        .requestMatchers("/staff/**").hasAnyRole("ADMIN")

                        //.hasAuthority("ROLE_ADMIN")//chỉ cho phép admin truy cập vào api này
                        //.hasRole(Role.ADMIN.name())
                        .anyRequest()
                        .authenticated());//Bước này vân sẽ chưa truy cập đc do csrf đc auto bật lên để protect => disable
                        //Ngoài phân quyền trên endpoint thì ta có thể phân quyền trên method => EnabledMethodSecurity
                            //Phổ biến nhất trong các dự án
                            //Bỏ phần quyền trên endpoint tại đây trc
                            //Sử dụng PreAuthorize , PostAuthori    ze đặt trên method cần phân quyền tại service

        //Sử dụng token để truy cập , sử dụng o2auth
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        //.jwkSetUri() , dùng cho bên thứ 3
                                .decoder(jwtDecoder())//cần implement
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())//Nếu thêm method này thì authorize trên sẽ disabled
                        ));




        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Cho phép ReactJS gọi API
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    //Tác dụng là khai báo thông tin về role để phân quyền truy cập , cách 2 , cách 1 là dùng clasms ra scope
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    //Method này check token có hợp lệ kjo để cho phép truy cập
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec=new SecretKeySpec(signerKey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }



    //Vì mã hóa mật khẩu đc sử dụng nhiều nơi nên có thể tạo ở đây để sử dụng
    @Bean//đánh dấu là Bean sẽ làm method này đc đưa vào IOC Container để sử dụng ở các nơi khác
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
