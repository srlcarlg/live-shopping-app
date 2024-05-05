package live.chat.websocket.encoders;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionEncoder {
	public static String encoder(SessionBroadcast sessionBroadcast) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(sessionBroadcast);
		} catch (Exception e) {
			return "Error: SessionEncoder";
		}
	}
}
