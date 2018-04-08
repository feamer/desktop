package feamer.desktop;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import net.sf.jcarrierpigeon.Notification;
import net.sf.jcarrierpigeon.NotificationQueue;
import net.sf.jcarrierpigeon.WindowPosition;

public class Main {
	public static ConfigWindow configWindow = null;
	
	private static NotificationQueue queue;
	private static Notification currentNotification;
	
	
	public static void main(String[] args) {
		
		Setup.setup();
		new BackgroundService().start();

		createTrayIcon();
		
		queue = new NotificationQueue();

	}

	private static void createTrayIcon() {
		// Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("/feamer.gif", "feamer"));
		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener((ActionEvent ae)->{
			openConfigWindow();
		});
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem configItem = new MenuItem("config");
		configItem.addActionListener((ActionEvent ae)->{
			openConfigWindow();
		});
		
		MenuItem startUploadItem = new MenuItem("transfer file");
		
		startUploadItem.addActionListener((ActionEvent ae)->{
			StartUploadNotification window = new StartUploadNotification();
			Notification note = new Notification(window, WindowPosition.TOPRIGHT, 25, 25, 25000);
			queue = new NotificationQueue();
			queue.add(note);
		});
		
		MenuItem exitItem = new MenuItem("exit");
		
		exitItem.addActionListener((ActionEvent ae)->{
			System.exit(1);
		});
		
		
		// Add components to pop-up menu
		popup.add(startUploadItem);
		popup.add(configItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	private static void openConfigWindow() {
		if(configWindow != null) {
			configWindow.frmFeamer.dispose();
		}
		configWindow = new ConfigWindow();
		configWindow.frmFeamer.setVisible(true);
	}
	
	public static void requestNotification(String name, long timestamp, String endpoint) {
		RequestNotification window = new RequestNotification(name, timestamp, endpoint);
		Notification note = new Notification(window, WindowPosition.TOPRIGHT, 25, 25, 5000);
		queue = new NotificationQueue();
		queue.add(note);
	}
	
	public static void clipboardNotification(File file) {
		ClipboardNotification window = new ClipboardNotification(file);
		Notification note = new Notification(window, WindowPosition.TOPRIGHT, 25, 25, 5000);
		queue = new NotificationQueue();
		queue.add(note);
	}

	// Obtain the image URL
	protected static Image createImage(String path, String description) {
		URL imageURL = BackgroundService.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
	
	public static BufferedImage generateQRCodeImage(String text)
            throws WriterException, IOException {
		
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
