package com.example.DemoSelfTest1.config;


import com.example.DemoSelfTest1.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;//tạo object từ UserRepo để lấy findByEmail

    //inject
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //tìm user trong database
    @Bean
    UserDetailsService userDetailsService() {

        return email -> {
            System.out.println("Calling UserDetailsService " + email);
            return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }

    //mã hóa password
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //xử lí xác thực , trả về một AuthenticationManager từ AuthenticationConfiguration,Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //trả về một AuthenticationProvider dùng DaoAuthenticationProvider dùng UserDetailsService
    //lấy thông tin user và passwordEncoder
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
