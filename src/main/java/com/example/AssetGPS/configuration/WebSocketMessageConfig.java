package com.example.AssetGPS.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {
    Logger logger = LoggerFactory.getLogger(WebSocketMessageConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/ws");
        config.setApplicationDestinationPrefixes("/greensky");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(new StompSubProtocolErrorHandler());
        registry.addEndpoint("/credit-balance").setAllowedOriginPatterns("*").withSockJS().setWebSocketEnabled(true);
        registry.addEndpoint("/gps").setAllowedOriginPatterns("*").withSockJS().setWebSocketEnabled(true);
    }

//    simpMessagingTemplate.convertAndSend("/ws/gps/gps-data", "{\"connected\": \"true\"}");
}
