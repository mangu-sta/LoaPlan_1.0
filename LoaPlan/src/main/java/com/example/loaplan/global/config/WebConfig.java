package com.example.loaplan.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.port}")
    private String frontendPort;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        try {
            // ✅ 현재 PC의 로컬 IP 자동 감지
            String localIp = InetAddress.getLocalHost().getHostAddress();

            // ✅ 세 가지 주소 동시 허용
            String localhost = "http://localhost:" + frontendPort;
            String loopback = "http://127.0.0.1:" + frontendPort;
            String lan = "http://" + localIp + ":" + frontendPort;

            registry.addMapping("/api/**")
                    .allowedOrigins(localhost, loopback, lan)
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowCredentials(true);

            System.out.println("✅ [CORS 허용 목록]");
            System.out.println(" - " + localhost);
            System.out.println(" - " + loopback);
            System.out.println(" - " + lan);

        } catch (Exception e) {
            System.err.println("❌ CORS 설정 중 IP 감지 실패: " + e.getMessage());
        }
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 🟡 1) 현재 프로젝트 루트 디렉토리 구하기
        String projectRoot = System.getProperty("user.dir");

        // 🟡 2) uploads 절대경로 만들기
        String absoluteUploadPath = Paths.get(projectRoot, uploadDir)
                .toAbsolutePath()
                .toString()
                + "/";

        System.out.println("📁 Static Image Path = " + absoluteUploadPath);

        // 🟡 3) /uploads/** → 실제 폴더 연결
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + absoluteUploadPath);
    }

}
