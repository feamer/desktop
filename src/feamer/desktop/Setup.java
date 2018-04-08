package feamer.desktop;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;

import mslinks.ShellLink;

public class Setup {
	public static void setup() {
		File linkFile = new File("%APPDATA%\\Microsoft\\Windows\\SendTo\\Feamer.lnk");
		if (!linkFile.exists()) {
			String workingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
			System.out.println(workingDirectory + "\\feamer.ico");
			String path = Setup.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			try {
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				ShellLink sl = ShellLink.createLink(workingDirectory+"\\feamer_me.exe").setWorkingDir("")
						.setIconLocation(Paths.get(".").toAbsolutePath().normalize().toString() + "\\feamer.ico");
				// sl.getHeader().setIconIndex(128);
				sl.getConsoleData().setFont(mslinks.extra.ConsoleData.Font.Consolas).setFontSize(24).setTextColor(5);

				try {
					sl.saveTo(System.getenv("APPDATA") + "\\Microsoft\\Windows\\SendTo\\feamer (my devices).lnk");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ShellLink sl2 = ShellLink.createLink(workingDirectory+"\\feamer_friend.exe").setWorkingDir("")
						.setIconLocation(Paths.get(".").toAbsolutePath().normalize().toString() + "\\feamer.ico");
				// sl.getHeader().setIconIndex(128);
				sl2.getConsoleData().setFont(mslinks.extra.ConsoleData.Font.Consolas).setFontSize(24).setTextColor(5);

				try {
					sl2.saveTo(System.getenv("APPDATA") + "\\Microsoft\\Windows\\SendTo\\feamer (my friends).lnk");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*ShellLink sl3 = ShellLink.createLink(workingDirectory+"\\feamer_all.bat").setWorkingDir("")
						.setIconLocation(Paths.get(".").toAbsolutePath().normalize().toString() + "\\feamer.ico");
				// sl.getHeader().setIconIndex(128);
				sl3.getConsoleData().setFont(mslinks.extra.ConsoleData.Font.Consolas).setFontSize(24).setTextColor(5);

				try {
					sl3.saveTo(System.getenv("APPDATA") + "\\Microsoft\\Windows\\SendTo\\feamer (nearby devices).lnk");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
