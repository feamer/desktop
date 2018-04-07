package feamer.desktop;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import mslinks.ShellLink;

public class Setup {
	public static void setup() {
		File linkFile = new File("%APPDATA%\\Microsoft\\Windows\\SendTo\\Feamer.lnk");
		if(!linkFile.exists()) {
			String path = Setup.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			try {
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				ShellLink sl = ShellLink.createLink("notepad.exe").setWorkingDir("")
						.setIconLocation(decodedPath.substring(1)+"feamer.ico");
				//sl.getHeader().setIconIndex(128);
				sl.getConsoleData().setFont(mslinks.extra.ConsoleData.Font.Consolas).setFontSize(24).setTextColor(5);
		
				try {
					sl.saveTo(System.getenv("APPDATA")+"\\Microsoft\\Windows\\SendTo\\Feamer.lnk");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
}
