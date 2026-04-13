package asm.poly.asm_java6.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Đăng ký endpoint cho client kết nối (SockJS fallback)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // endpoint frontend sẽ kết nối
                .setAllowedOriginPatterns("*") // cho phép mọi domain, có thể giới hạn lại
                .withSockJS(); // bật SockJS fallback
    }

    // Cấu hình message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Định nghĩa prefix cho các topic mà client sẽ subscribe
        registry.enableSimpleBroker("/topic", "/queue");
        // Định nghĩa prefix cho các message gửi từ client lên server
        registry.setApplicationDestinationPrefixes("/app");
    }
}