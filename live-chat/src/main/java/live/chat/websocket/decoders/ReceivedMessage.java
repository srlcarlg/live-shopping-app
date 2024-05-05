package live.chat.websocket.decoders;

public class ReceivedMessage {
	private String username;
	private String email;
	private String password;
	private String message;
	private String string;
	
	public ReceivedMessage() {}
	
	public ReceivedMessage(String username, String email, String password, String message) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.message = message;
	}
	public ReceivedMessage(String message) {
		super();
		this.message = message;
	}
	public ReceivedMessage(String string, Boolean isRawText) {
		super();
		this.string = string;
	}
	
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getMessage() {
		return message;
	}
	public String getString() {
		return string;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return "ReceivedMessage [username=" + username + ", email=" + email + ", password=" + password + ", message="
				+ message + ", string=" + string + "]";
	}
}
