package live.chat.websocket.decoders.sub;

import live.chat.websocket.decoders.ReceivedMessage;

public class JoinChat extends ReceivedMessage {

	public JoinChat(String username, String email, String password) {
		super(username, email, password, null);
	}
}
