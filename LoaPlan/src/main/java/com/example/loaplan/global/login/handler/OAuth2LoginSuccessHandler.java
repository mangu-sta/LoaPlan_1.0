package com.example.loaplan.global.login.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.InetAddress;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${frontend.port}")
    private String frontendPort;

    @Value("${frontend.ip:}")
    private String frontendIp;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // ✅ 로그인 성공 시 세션에 유저 정보 저장
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        HttpSession session = request.getSession();
        session.setAttribute("user", oAuth2User.getAttributes());

        // ✅ 하이브리드 리다이렉트: Origin → frontend.ip → localIp → localhost 순서
        String redirectUrl;
        String origin = request.getHeader("Origin");

        if (origin != null && !origin.isEmpty()) {
            // 프론트에서 요청을 보낸 Origin이 있으면 그대로 사용
            redirectUrl = origin + "/";
        } else {
            // 없으면 fallback
            String localIp = InetAddress.getLocalHost().getHostAddress();

            if (frontendIp != null && !frontendIp.isEmpty()) {
                redirectUrl = "http://" + frontendIp + ":" + frontendPort + "/";
            } else {
                redirectUrl = "http://" + localIp + ":" + frontendPort + "/";
            }
        }

        // ✅ 최종 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}
