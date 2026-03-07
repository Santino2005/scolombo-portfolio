package com.baby_io.baby_io_app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final SleepEventWebSocketHandler sleepEventWebSocketHandler;

  @Autowired
  public WebSocketConfig(SleepEventWebSocketHandler sleepEventWebSocketHandler) {
    this.sleepEventWebSocketHandler = sleepEventWebSocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    // Register the sleep event handler at /ws/sleep-events
    registry.addHandler(sleepEventWebSocketHandler, "/ws/sleep-events")
            .setAllowedOrigins("*") // Configure this properly for production
            .withSockJS(); // Enable SockJS fallback for older browsers
  }
}