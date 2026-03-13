package com.example.loaplan.global.login.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "logged out"));
    }


    @GetMapping
    public ResponseEntity<?> getSession(HttpSession session, Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("authenticated", authentication != null);
        result.put("user", authentication != null ? authentication.getName() : null);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/debug")
    public ResponseEntity<?> debugSession(HttpSession session) {
        String sessionId = session.getId();
        Object user = session.getAttribute("user");

        return ResponseEntity.ok(Map.of(
                "sessionId", sessionId,
                "user", user,
                "isNew", session.isNew()
        ));
    }


}
