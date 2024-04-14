package live.manager.decoders.sub;

import live.manager.decoders.ReceivedMessage;

public class SetLiveStatus extends ReceivedMessage {

	public SetLiveStatus(String livePassword) {
		super(null, livePassword);
	}
	
}
