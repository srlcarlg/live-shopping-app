package live.chat.websocket.database;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import live.chat.websocket.encoders.SendChat;

@Table("messages")
public class ChatMessage {
	
	// The Clustering Key is responsible for data sorting within the partition.
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID id;
    
	// The Partition Key is responsible for data distribution across your nodes.
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String liveSlug;
    
	private String message;
	private String username;
	private String email;
	private Boolean isBroadcaster;
	
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private Timestamp createAt;
	
	public ChatMessage(String liveSlug, String message, String username, String email, Boolean isBroadcaster) {
		this.id = UUID.randomUUID();
		this.liveSlug = liveSlug;
		this.message = message;
		this.username = username;
		this.email = email;
		this.isBroadcaster = isBroadcaster;
		this.createAt = Timestamp.from(Instant.now());
	}
	public ChatMessage() {
		this.liveSlug = "";
		this.message = "";
		this.username = "";
		this.email = "";
		this.isBroadcaster = null;
		this.createAt = null;
	}
	public ChatMessage(String slug, SendChat sendChat) {
		this.id = UUID.randomUUID();
		this.liveSlug = slug;
		this.message = sendChat.getMessage();
		this.username = sendChat.getUsername();
		this.email = sendChat.getEmail();
		this.isBroadcaster = sendChat.getIsBroadcaster();
		this.createAt = Timestamp.from(Instant.now());
	}
	
	public static SendChat toSendChat(ChatMessage cMsg) {
		return new SendChat(cMsg.getMessage(), cMsg.getUsername(), cMsg.getEmail(), cMsg.getIsBroadcaster());
	}
	
	public String getLiveSlug() {
		return liveSlug;
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
	public Timestamp getCreateAt() {
		return createAt;
	}
	
	public void setLiveSlug(String liveSlug) {
		this.liveSlug = liveSlug;
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
	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
	
	@Override
	public String toString() {
		return "ChatMessage [liveSlug=" + liveSlug + ", message=" + message + ", username=" + username + ", email="
				+ email + ", isBroadcaster=" + isBroadcaster + ", createAt=" + createAt + "]";
	}
}