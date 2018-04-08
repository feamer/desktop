package feamer.desktop;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class FeamerWebsocketClient extends WebSocketClient {

	public FeamerWebsocketClient(URI uri, Map<String, String> headers) {
		super(uri, new Draft_6455(), headers, 1000);
		System.out.println(uri);
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		System.out.println("websocket closed - " + arg0 + " " + arg1);

		FeamerPreferences.getInstance().checkAndUpdateToken(val -> {});
	}

	@Override
	public void onError(Exception arg0) {
		System.out.println(arg0);
		arg0.printStackTrace();

		FeamerPreferences.getInstance().checkAndUpdateToken(val -> {});

	}

	@Override
	public void onMessage(String message) {
		System.out.println("received websocket message: " + message);
		JSONObject json = new JSONObject(message);
		String filename = json.getString("name");
		String endpoint = json.getString("endpoint");
		long timestamp = json.getLong("timestamp");
		long size = json.getLong("timestamp");

		Main.requestNotification(filename, timestamp, endpoint, size);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		System.out.println("websocket connection opened");
	}

}