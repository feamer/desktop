package feamer.desktop;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.google.zxing.WriterException;

import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class ConfigWindow {

	public JFrame frmFeamer;
	private JTextField tfUsername;
	private JPasswordField passwordField;
	private JTextField tfEndpoint;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigWindow window = new ConfigWindow();
					window.frmFeamer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConfigWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFeamer = new JFrame();
		frmFeamer.setIconImage(Main.createImage("/feamer.gif", "tray icon"));
		frmFeamer.setTitle("feamer");
		frmFeamer.setBounds(100, 100, 503, 327);
		frmFeamer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmFeamer.getContentPane().setLayout(null);
		frmFeamer.setResizable(false);

		tfUsername = new JTextField();
		tfUsername.setToolTipText("Username");
		tfUsername.setText(FeamerPreferences.getInstance().get(FeamerPreferences.USERNAME));
		tfUsername.setBounds(96, 46, 141, 20);
		frmFeamer.getContentPane().add(tfUsername);
		tfUsername.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Password");
		passwordField.setText(FeamerPreferences.getInstance().get(FeamerPreferences.PASSWORD));
		passwordField.setBounds(96, 77, 141, 20);
		frmFeamer.getContentPane().add(passwordField);

		JLabel lblUsername = new JLabel("username:");
		lblUsername.setBounds(26, 46, 69, 20);
		frmFeamer.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("password:");
		lblPassword.setBounds(26, 77, 69, 20);
		frmFeamer.getContentPane().add(lblPassword);

		JLabel lblEndpoint = new JLabel("endpoint:");
		lblEndpoint.setBounds(26, 108, 69, 20);
		frmFeamer.getContentPane().add(lblEndpoint);

		tfEndpoint = new JTextField();
		tfEndpoint.setToolTipText("Endpoint");
		tfEndpoint.setText(FeamerPreferences.getInstance().get(FeamerPreferences.ENDPOINT));
		tfEndpoint.setColumns(10);
		tfEndpoint.setBounds(96, 108, 141, 20);
		frmFeamer.getContentPane().add(tfEndpoint);

		JButton btnNewButton = new JButton("save");

		btnNewButton.setBounds(96, 139, 141, 20);
		frmFeamer.getContentPane().add(btnNewButton);

		JLabel lblStatus = new JLabel("");
		lblStatus.setVisible(false);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(80, 261, 141, 16);

		frmFeamer.getContentPane().add(lblStatus);

		FeamerPreferences.getInstance().getUserId(id -> {
			BufferedImage image;
			try {
				image = Main.generateQRCodeImage(id);
				JPanel qrPanel = new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(image, -10, -10, null);
					}
				};
				qrPanel.setBackground(new Color(0, 0, 0, 0));
				qrPanel.setBounds(270, 11, 178, 182);
				frmFeamer.getContentPane().add(qrPanel);

				JLabel lblScanThisCode = new JLabel(
						"<html>Scan this code from an <b>other device</b> to add an <b>other device</b> or <b>register a friend</b></html>");
				lblScanThisCode.setHorizontalAlignment(SwingConstants.LEFT);
				lblScanThisCode.setFont(new Font("Tahoma", Font.PLAIN, 14));
				lblScanThisCode.setUI(MultiLineLabelUI.labelUI);
				lblScanThisCode.setBounds(270, 204, 178, 73);
				frmFeamer.getContentPane().add(lblScanThisCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!FeamerPreferences.getInstance().get(FeamerPreferences.PASSWORD).equals(passwordField.getText())) {
					FeamerPreferences.getInstance().setPassword(passwordField.getText());
				}
				FeamerPreferences.getInstance().setUsername(tfUsername.getText());
				FeamerPreferences.getInstance().setEndpoint(tfEndpoint.getText());

				lblStatus.setVisible(false);
				FeamerPreferences.getInstance().checkAndUpdateToken(success -> {
					lblStatus.setVisible(true);
					if (success) {
						lblStatus.setText("login succeeded");
						lblStatus.setForeground(new Color(0, 200, 0));
					} else {
						lblStatus.setText("login failed. chack configuration");
						lblStatus.setForeground(Color.RED);
					}
				});
			}
		});

	}
}
