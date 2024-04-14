package live.manager.decoders.sub;

import live.manager.decoders.ReceivedMessage;

public class SetBroadcaster extends ReceivedMessage {

	public SetBroadcaster(String jsPeerId, String livePassword) {
		super(jsPeerId, livePassword);
	}
	
}
