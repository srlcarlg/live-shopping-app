package live.manager.encoders;

public class SendMessage {
	private String broadcasterId;
	private Integer usersCount;
	private String string;
	
	public SendMessage() {}
	
	public SendMessage(String broadcasterId, Integer usersCount) {
		super();
		this.broadcasterId = broadcasterId;
		this.usersCount = usersCount;
	}
	public SendMessage(String string) {
		super();
		this.string = string;
	}

	public String getBroadcasterId() {
		return broadcasterId;
	}
	public Integer getUsersCount() {
		return usersCount;
	}
	public String getString() {
		return string;
	}
	
	public void setBroadcasterId(String broadcasterId) {
		this.broadcasterId = broadcasterId;
	}
	public void setUsersCount(Integer usersCount) {
		this.usersCount = usersCount;
	}
	public void setString(String string) {
		this.string = string;
	}
}