package live.chat.websocket.encoders;

public class SessionBroadcast {
	private String sessionId;

	public SessionBroadcast(String sessionId) {
		super();
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
