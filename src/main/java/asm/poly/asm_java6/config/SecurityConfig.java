package asm.poly.asm_java6.config;

import asm.poly.asm_java6.config.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler; // Xử lý khi login OAuth2 thành công
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService; // Service để lấy thông tin user từ Google

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())


                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả mọi người truy cập các trang sau
                        .requestMatchers("/", "/home", "/login", "/css/**", "/js/**", "/img/**").permitAll()
                        // Chỉ ADMIN mới truy cập /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Chỉ USER mới truy cập /user/**
                        .requestMatchers("/user/**").hasRole("USER")
                        // Các request khác mặc định cho phép
                        .anyRequest().permitAll()
                )

                // Xử lý khi không đủ quyền truy cập
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Nếu user không có quyền, redirect về login với param error
                            response.sendRedirect("/login?error=forbidden");
                        })
                )

                // Cấu hình login truyền thống
                .formLogin(form -> form
                        .loginPage("/login") // Trang login tùy chỉnh
                        .defaultSuccessUrl("/home", true) // Sau login thành công, chuyển về /home
                        .permitAll() // Cho phép mọi người truy cập trang login
                )

                // Cấu hình login bằng OAuth2 (Google, FB…)
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // Trang login chung cho OAuth2
                        .successHandler(oAuth2LoginSuccessHandler) // Xử lý khi login OAuth2 thành công
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Lấy thông tin user từ Google
                        )
                )

                // Cấu hình logout
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL logout
                        .logoutSuccessUrl("/home") // Sau logout, chuyển về /home
                        .invalidateHttpSession(true) // Xóa session
                        .deleteCookies("JSESSIONID") // Xóa cookie session
                );

        return http.build(); // Build SecurityFilterChain
    }
}