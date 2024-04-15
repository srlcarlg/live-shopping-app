package live.manager.decoders;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import live.manager.decoders.sub.SetLiveStatus;
import live.manager.decoders.sub.SetBroadcaster;

public class ReceivedMessageDecoder implements Decoder.Text<ReceivedMessage> {
	@Override
	public ReceivedMessage decode(String s) throws DecodeException {
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(s));
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();
			return jsonObject.containsKey("js_peer_id")
				? new SetBroadcaster(jsonObject.getString("js_peer_id"), jsonObject.getString("live_password")) 
				: new SetLiveStatus(jsonObject.getString("live_password"));
		} catch (Exception e) {
			return new ReceivedMessage(s);
		}
	}
	@Override
	public boolean willDecode(String s) {
		return true;
	}
}