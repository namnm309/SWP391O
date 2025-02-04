package com.example.DemoSelfTest1.config;

import com.example.DemoSelfTest1.model.User;
import com.example.DemoSelfTest1.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //báo ngoại lệ nếu có lỗi
    private final HandlerExceptionResolver handlerExceptionResolver;
    //xử lí JWT
    private final JwtService jwtService;
    //lấy thông tin user
    private final UserDetailsService userDetailsService;

    //inject
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;


    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,//lấy tt request
            @NonNull HttpServletResponse response,//nạp tt response
            @NonNull FilterChain filterChain //xử lí filter khác
    ) throws ServletException, IOException {
        //lấy header Authorization của request
        final String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //bỏ bearer lấy jwt token
            final String jwt = authHeader.substring(7);
//            final String jwt = authHeader;

            //lấy username trong jwt đc generate từ token tạo ra
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                System.out.println("Calling JwtAuthenticationFilter: userName " + userEmail);

                User userDetails = (User) this.userDetailsService.loadUserByUsername(userEmail);

                System.out.println("Calling JwtAuthenticationFilter: email " + userDetails.getEmail());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }

    }


}