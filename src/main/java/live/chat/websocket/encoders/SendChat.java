package live.chat.websocket.encoders;

public class SendChat {
	private String message;
	private String username;
	private String email;
	private Boolean isBroadcaster;
	
	public SendChat(String message, String username, String email, Boolean isBroadcaster) {
		super();
		this.message = message;
		this.username = username;
		this.email = email;
		this.isBroadcaster = isBroadcaster;
	}
	
	public String getMessage() {
		return message;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public Boolean getIsBroadcaster() {
		return isBroadcaster;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setIsBroadcaster(Boolean isBroadcaster) {
		this.isBroadcaster = isBroadcaster;
	}
}
