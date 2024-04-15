package live.manager.decoders;

public class ReceivedMessage {
	private String jsPeerId;
	private String livePassword;
	private String string;
	
	public ReceivedMessage() {}
	
	public ReceivedMessage(String jsPeerId, String livePassword) {
		super();
		this.jsPeerId = jsPeerId;
		this.livePassword = livePassword;
	}
	public ReceivedMessage(String string) {
		super();
		this.string = string;
	}

	public String getJsPeerId() {
		return jsPeerId;
	}
	public String getLivePassword() {
		return livePassword;
	}
	public String getString() {
		return string;
	}
	public void setJsPeerId(String jsPeerId) {
		this.jsPeerId = jsPeerId;
	}
	public void setLivePassword(String livePassword) {
		this.livePassword = livePassword;
	}	
	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return "ReceivedMessage{jsPeerId=" + jsPeerId + ", livePassword=" + livePassword + ", string=" + string + "}";
	}
}
