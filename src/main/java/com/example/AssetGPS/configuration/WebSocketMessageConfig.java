package com.example.AssetGPS.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    // scheduled refresh
    /*@Scheduled(fixedRate = 10001)
    public void refreshCreditBalance() {
        try{
            logger.info(destinationWebsocket);
            logger.info(destinationURL);
            logger.info(destinationURL.substring(destinationURL.indexOf("/") + 2, destinationURL.indexOf('.')));

            RefreshCreditBalance.refresh(destinationWebsocket, String.valueOf(usersDetailsRepository.getCreditBalance(destinationURL.substring(destinationURL.indexOf("/") + 2, destinationURL.indexOf('.'))).get(0)[0]));
            logger.info("refreshCreditBalance: refreshed " + destinationWebsocket);
        }catch (Exception e){
            return;
        }
    }*/

//            simpMessagingTemplate.convertAndSend("/ws/gps/gps-data", "{\"connected\": \"true\"}");
}
