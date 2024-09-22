package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.service.usertracking.OnlineUserSet;
import itss.group22.bookexchangeeasy.service.usertracking.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WSConnectionController {
    private final OnlineUserSet onlineUserSet;
    private final JwtDecoder jwtDecoder;

    @EventListener
    public void connect(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String token = headerAccessor.getFirstNativeHeader("token");
        connect(token, headerAccessor);
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        onlineUserSet.removeUserBySessionId(event.getSessionId());
        log.info("Client disconnected: " + event.getSessionId());
    }

    @MessageMapping("/connect")
    public void connect(String token, StompHeaderAccessor headerAccessor) {
        if (Objects.isNull(token)) return;
        Jwt jwt = jwtDecoder.decode(token);
        Long userId = jwt.getClaim("id");
        String sessionId = headerAccessor.getSessionId();
        onlineUserSet.put(userId, UserContext.builder().userId(userId).sessionId(sessionId).build());
        log.info("Client connected: " + sessionId);
    }

    @MessageMapping("/disconnect")
    public void disconnect(String token) {
        if (Objects.isNull(token)) return;
        Jwt jwt = jwtDecoder.decode(token);
        Long userId = jwt.getClaim("id");
        onlineUserSet.remove(userId);
    }
}
