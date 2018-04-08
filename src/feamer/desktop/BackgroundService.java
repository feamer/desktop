package feamer.desktop;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class BackgroundService extends Thread {

	public void run() {
		FeamerPreferences.getInstance().checkAndUpdateToken(success->{});

		FeamerWebsocketClient client = null;
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", FeamerPreferences.getInstance().getToken());
		
		while (true) {
			try {
				if(client == null || !client.isOpen()) {
					try {
						client = new FeamerWebsocketClient(new URI(FeamerPreferences.getInstance().get(FeamerPreferences.ENDPOINT).replaceFirst("http", "ws")+"/ws"), headers);
						client.connect();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
