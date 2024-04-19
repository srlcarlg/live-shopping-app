package live.chat.websocket;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
public class WebSocketConfig {
	
	@Bean 
	public WebSocketServer webSocketServer() {
		return new WebSocketServer();
	}
	
    @Bean
    public HandlerMapping webSocketMapping(WebSocketServer webSocketServer) {
        return new SimpleUrlHandlerMapping(Map.of("/chat/{slug}", webSocketServer), -1); 
    }
    
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
