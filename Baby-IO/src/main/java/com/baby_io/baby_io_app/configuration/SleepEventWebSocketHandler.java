package com.baby_io.baby_io_app.configuration;

import com.baby_io.baby_io_app.dto.SleepEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SleepEventWebSocketHandler extends TextWebSocketHandler {

  private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
  private final ObjectMapper objectMapper;

  public SleepEventWebSocketHandler() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
    System.out.println("WebSocket connection established: " + session.getId());

    // Send a welcome message to confirm connection
    try {
      session.sendMessage(new TextMessage("{\"type\":\"connection\",\"message\":\"Connected to sleep events\"}"));
    } catch (Exception e) {
      System.err.println("Error sending welcome message: " + e.getMessage());
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessions.remove(session);
    System.out.println("WebSocket connection closed: " + session.getId() + " - " + status.toString());
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    System.err.println("WebSocket transport error for session " + session.getId() + ": " + exception.getMessage());
    sessions.remove(session);
  }

  /**
   * Broadcast a sleep event to all connected WebSocket clients
   */
  public void broadcastSleepEvent(SleepEventDTO sleepEvent) {
    if (sessions.isEmpty()) {
      System.out.println("No WebSocket sessions to broadcast to");
      return;
    }

    String message;
    try {
      // Wrap the sleep event in a message envelope
      var messageWrapper = new Object() {
        public final String type = "sleep_event";
        public final SleepEventDTO data = sleepEvent;
      };
      message = objectMapper.writeValueAsString(messageWrapper);
      System.out.println("Broadcasting sleep event: " + message);
    } catch (Exception e) {
      System.err.println("Error serializing sleep event: " + e.getMessage());
      return;
    }

    // Use removeIf to safely remove closed sessions while iterating
    sessions.removeIf(session -> {
      try {
        if (session.isOpen()) {
          session.sendMessage(new TextMessage(message));
          return false; // Keep the session
        } else {
          System.out.println("Removing closed WebSocket session: " + session.getId());
          return true; // Remove closed sessions
        }
      } catch (Exception e) {
        System.err.println("Error sending WebSocket message to session " + session.getId() + ": " + e.getMessage());
        return true; // Remove problematic sessions
      }
    });

    System.out.println("Sleep event broadcasted to " + sessions.size() + " active sessions");
  }

  /**
   * Get the number of active WebSocket connections
   */
  public int getActiveConnectionCount() {
    return sessions.size();
  }
}