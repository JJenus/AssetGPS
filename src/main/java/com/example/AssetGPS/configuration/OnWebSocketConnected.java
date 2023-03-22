package com.example.AssetGPS.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class OnWebSocketConnected implements ApplicationListener<SessionSubscribeEvent> {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private String data = "";

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        if (headers.containsNativeHeader("trackingData") && !headers.getNativeHeader("trackingData").isEmpty()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Flux.interval(Duration.ofSeconds(3L)).subscribe(message -> {
                String txt = new CustomFile().getTemplate("tracker.txt");
                if (!txt.equalsIgnoreCase(data)) {
                    data = txt;
                    simpMessagingTemplate.convertAndSend("/ws/gps/gps-data", data);
                }
            });
//            simpMessagingTemplate.convertAndSend("/ws/gps/gps-data", "{\"connected\": \"true\"}");
        }
    }

    public void setData(String txt){
        data = txt;
    }

}
