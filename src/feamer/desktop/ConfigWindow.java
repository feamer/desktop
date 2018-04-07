package feamer.desktop;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		frmFeamer.setBounds(100, 100, 450, 327);
		frmFeamer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmFeamer.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Drop files here");
		lblNewLabel.setBackground(SystemColor.control);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 414, 106);
		frmFeamer.getContentPane().add(lblNewLabel);
		lblNewLabel.setTransferHandler(new FileDropHandler(files ->  {}));
		lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 128, 434, 2);
		frmFeamer.getContentPane().add(separator);

		tfUsername = new JTextField();
		tfUsername.setToolTipText("Username");
		tfUsername.setText(FeamerPreferences.getInstance().get(FeamerPreferences.USERNAME));
		tfUsername.setBounds(80, 141, 141, 20);
		frmFeamer.getContentPane().add(tfUsername);
		tfUsername.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Password");
		passwordField.setText(FeamerPreferences.getInstance().get(FeamerPreferences.PASSWORD));
		passwordField.setBounds(80, 172, 141, 20);
		frmFeamer.getContentPane().add(passwordField);

		JLabel lblUsername = new JLabel("username:");
		lblUsername.setBounds(10, 141, 69, 20);
		frmFeamer.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("password:");
		lblPassword.setBounds(10, 172, 69, 20);
		frmFeamer.getContentPane().add(lblPassword);

		JLabel lblEndpoint = new JLabel("endpoint:");
		lblEndpoint.setBounds(10, 203, 69, 20);
		frmFeamer.getContentPane().add(lblEndpoint);

		tfEndpoint = new JTextField();
		tfEndpoint.setToolTipText("Endpoint");
		tfEndpoint.setText(FeamerPreferences.getInstance().get(FeamerPreferences.ENDPOINT));
		tfEndpoint.setColumns(10);
		tfEndpoint.setBounds(80, 203, 141, 20);
		frmFeamer.getContentPane().add(tfEndpoint);

		JButton btnNewButton = new JButton("save");
		
		btnNewButton.setBounds(80, 234, 141, 20);
		frmFeamer.getContentPane().add(btnNewButton);

		JLabel lblStatus = new JLabel("");
		lblStatus.setVisible(false);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(80, 261, 141, 16);
		
		frmFeamer.getContentPane().add(lblStatus);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!FeamerPreferences.getInstance().get(FeamerPreferences.PASSWORD).equals(passwordField.getText())) {
					FeamerPreferences.getInstance().setPassword(passwordField.getText());
				}
				FeamerPreferences.getInstance().setUsername(tfUsername.getText());
				FeamerPreferences.getInstance().setEndpoint(tfEndpoint.getText());

				lblStatus.setVisible(false);
				FeamerPreferences.getInstance().checkAndUpdateToken( success -> {
					lblStatus.setVisible(true);
					if (success) {
						lblStatus.setText("login succeeded");
						lblStatus.setForeground(new Color(0,200,0));
					} else {
						lblStatus.setText("login failed. chack configuration");
						lblStatus.setForeground(Color.RED);
					}
				});
			}
		});
		
	}
}
