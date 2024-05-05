package live.chat.websocket.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;

import live.chat.websocket.decoders.sub.JoinChat;
import live.chat.websocket.decoders.sub.MessageChat;

public class ReceivedMessageDecoder  {

	public static ReceivedMessage decode(String string) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			ReceivedMessage message = mapper.readValue(string, ReceivedMessage.class);
			if (message.getUsername() == null && message.getEmail() == null 
				&& message.getMessage() == null) {
				throw new Exception(); 
			}
			return message.getMessage() == null
				? new JoinChat(message.getUsername(), message.getEmail(), message.getPassword()) 
				: new MessageChat(message.getMessage());
		} catch (Exception e) {
			return new ReceivedMessage(string, true);
		}
	}
}