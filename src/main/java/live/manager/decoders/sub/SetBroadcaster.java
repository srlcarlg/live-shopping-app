package live.manager.decoders.sub;

import live.manager.decoders.ReceivedMessage;

public class SetBroadcaster extends ReceivedMessage {

	public SetBroadcaster(String peerId, String livePassword) {
		super(peerId, livePassword);
	}
	
}
