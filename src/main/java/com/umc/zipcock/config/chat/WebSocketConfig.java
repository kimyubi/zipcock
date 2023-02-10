package com.umc.zipcock.config.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // 메시지 브로커가 메시지를 처리할 수 있도록 WebSocketMessageBroker를 활성화 시킨다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // JWT 인증 처리 위한StompHandler
    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/chat");   // 메시지 브로커가 "/topic/chat" 을 prefix로 하는 경로를 구독한 구독자들에게 메시지를 전달하도록 한다.
        config.setApplicationDestinationPrefixes("/app");          //  클라이언트가 서버로 메시지를 발송할 수 있는 경로의 prefix를 지정한다.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")                         // 소켓에 연결하기 위한 엔드 포인트를 지정한다.
                .setAllowedOriginPatterns("*")                     // CORS를 적용하기 위해 AllowedOriginPatterns를 지정한다. (추후 배포시 정확한 도메인 지정해야 함.)
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);                   // jwt 토큰 인증 처리를 위해 생성한 stompHandler를 인터셉터로 지정해준다.
    }
}
