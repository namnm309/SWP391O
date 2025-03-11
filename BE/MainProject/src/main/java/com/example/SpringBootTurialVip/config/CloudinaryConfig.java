package com.example.SpringBootTurialVip.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "swp391image");
        config.put("api_key", "586935558113132");
        config.put("api_secret", "1BNL_fFfm13rrpEFmIq-VW1iyUo");
        return new Cloudinary(config);
    }
}
