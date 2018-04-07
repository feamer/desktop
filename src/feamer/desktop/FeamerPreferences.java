package feamer.desktop;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FeamerPreferences {

	private static FeamerPreferences instance;

	Preferences prefs;

	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String ENDPOINT = "endpoint";

	private String token;

	protected FeamerPreferences() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
		if (prefs.get(USERNAME, null) == null) {
			prefs.put(USERNAME, "tobi");
		}
		if (prefs.get(PASSWORD, null) == null) {
			try {
				prefs.put(PASSWORD, toSHA1("tobi"));
			} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if (prefs.get(ENDPOINT, null) == null) {
			prefs.put(ENDPOINT, "http://51.144.0.67:80");
		}
	}

	public static String toSHA1(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(input.getBytes("UTF-8"));
		return byteToHex(crypt.digest());
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public void setPassword(String password) {
		try {
			this.prefs.put(PASSWORD, toSHA1(password));
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUsername(String username) {
		this.prefs.put(USERNAME, username);
	}

	public void setEndpoint(String endpoint) {
		this.prefs.put(ENDPOINT, endpoint);
	}

	public String get(String key) {
		return this.prefs.get(key, null);
	}

	public static FeamerPreferences getInstance() {
		if (instance == null) {
			instance = new FeamerPreferences();
		}

		return instance;
	}

	public void checkAndUpdateToken(Consumer<Boolean> callback) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(get(ENDPOINT) + "/login");
		try {
			httpPost.setEntity(new StringEntity(
					"{ \"username\":\"" + this.get(USERNAME) + "\", \"password\":\"" + this.get(PASSWORD) + "\"}"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			CloseableHttpResponse response2 = httpclient.execute(httpPost);
			String result = IOUtils.toString(response2.getEntity().getContent());
			System.out.println("received token: " + result);
			System.out.println("status code: " + response2.getStatusLine().getStatusCode());
			token = result;
			if(response2.getStatusLine().getStatusCode() == 200) {
				callback.accept(true);
				System.out.println("success");
			}else {
				callback.accept(false);
			}
			response2.close();
			return;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		callback.accept(false);
	}
	
	public void transferFile(File file, Consumer<Float> callback) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(get(ENDPOINT) + "/rest/upload");
		httpPost.addHeader("Authorization", token);
		httpPost.addHeader("Filename", file.getName());
		httpPost.setEntity(new FileEntity(file));

		try {
			CloseableHttpResponse response2 = httpclient.execute(httpPost);
			String result = IOUtils.toString(response2.getEntity().getContent());
			System.out.println("received token: " + result);
			System.out.println("status code: " + response2.getStatusLine().getStatusCode());
			token = result;
			if(response2.getStatusLine().getStatusCode() == 200) {
				System.out.println("success");
			}else {
			}
			response2.close();
			return;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

}
