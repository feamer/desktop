package feamer.desktop;

public class BackgroundService extends Thread {

	public void run() {
		FeamerPreferences.getInstance().checkAndUpdateToken(success->{});

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
