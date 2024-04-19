package live.chat.websocket.encoders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SendChatEncoder {
	public static String encoder(SendChat sendChat) {
		ObjectMapper mapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return mapper.writeValueAsString(sendChat);
		} catch (Exception e) {
			return "Error: SendChat";
		}
	}
}
