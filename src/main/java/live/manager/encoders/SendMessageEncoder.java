package live.manager.encoders;

import jakarta.json.Json;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class SendMessageEncoder implements Encoder.Text<SendMessage> {
	@Override
	public String encode(SendMessage object) throws EncodeException {
		if (object.getBroadcasterId() != null) {
			return Json.createObjectBuilder()
				    .add("broadcaster_id", object.getBroadcasterId())
				    .build()
				    .toString();
		} else if (object.getUsersCount() != null) {
			return Json.createObjectBuilder()
				    .add("users-count", object.getUsersCount())
				    .build()
				    .toString();
		} else {
			return object.getString();
		}
	}
}
