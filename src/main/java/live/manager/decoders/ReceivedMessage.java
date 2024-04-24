package live.manager.decoders;

public class ReceivedMessage {
	private String peerId;
	private String livePassword;
	private String string;
	
	public ReceivedMessage() {}
	
	public ReceivedMessage(String peerId, String livePassword) {
		super();
		this.peerId = peerId;
		this.livePassword = livePassword;
	}
	public ReceivedMessage(String string) {
		super();
		this.string = string;
	}

	public String getPeerId() {
		return peerId;
	}
	public String getLivePassword() {
		return livePassword;
	}
	public String getString() {
		return string;
	}
	public void setPeerId(String jsPeerId) {
		this.peerId = jsPeerId;
	}
	public void setLivePassword(String livePassword) {
		this.livePassword = livePassword;
	}	
	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return "ReceivedMessage{peerId=" + peerId + ", livePassword=" + livePassword + ", string=" + string + "}";
	}
}
