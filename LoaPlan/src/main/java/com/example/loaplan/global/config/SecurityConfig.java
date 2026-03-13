package com.example.loaplan.global.config;

import com.example.loaplan.global.login.handler.OAuth2LoginSuccessHandler;
import com.example.loaplan.global.login.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.net.InetAddress;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${frontend.port}")
    private String frontendPort;

    @Value("${frontend.ip}")
    private String frontendIp;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String localIp = InetAddress.getLocalHost().getHostAddress();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowCredentials(true);

                    // React dev 서버 : Origin 자동 감지 + fallback
                    String origin = request.getHeader("Origin");
                    if (origin != null && !origin.isEmpty()) {
                        config.addAllowedOrigin(origin);
                    } else {
                        // fallback: properties의 IP > 자동 IP > localhost 순서
                        if (frontendIp != null && !frontendIp.isEmpty()) {
                            config.addAllowedOrigin("http://" + frontendIp + ":" + frontendPort);
                        } else {
                            config.addAllowedOrigin("http://" + localIp + ":" + frontendPort);
                            config.addAllowedOrigin("http://localhost:" + frontendPort);
                        }
                    }

                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 이메일 인증 API 추가 허용
                        .requestMatchers(
                                "/",
                                "/api/user/join",
                                "/api/email/**",
                                "/api/user/email/**",
                                "/api/islands/**",
                                "/api/characters",
                                "/api/user/**",
                                "/api/session/**",
                                "/css/**", "/js/**", "/images/**",
                                "/api/board/list",
                                "/api/board/detail",
                                "/api/board/images",
                                "/api/user/profile",
                                "/api/comment/**",
                                "/api/armory/**",
                                "/api/inven/**",
                                "/api/raid/list",
                                "/api/characters/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/user/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            res.setStatus(HttpServletResponse.SC_OK);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"message\":\"로그인 성공\"}");
                        })
                        .failureHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // ✅ Origin 기반 로그아웃 리다이렉트 (같은 로직)
                            String origin = request.getHeader("Origin");
                            if (origin == null || origin.isEmpty()) {
                                if (frontendIp != null && !frontendIp.isEmpty()) {
                                    origin = "http://" + frontendIp + ":" + frontendPort;
                                } else {
                                    origin = "http://" + localIp + ":" + frontendPort;
                                }
                            }
                            response.sendRedirect(origin + "/");
                        })

                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(); // 비밀번호 암호화용
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }





}