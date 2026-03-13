package com.example.loaplan.global.redirectController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;

@Controller
public class RedirectController {

    @Value("${frontend.port}")
    private String frontendPort;

    @GetMapping({"", "/"})
    public String home() {

        try {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            return "redirect:http://"+localIp+":"+frontendPort+"/";

        } catch (Exception e) {
            throw new RuntimeException("❌ IP 감지 실패", e);
        }
    }
}