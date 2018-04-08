package feamer.desktop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JProgressBar;

public class StartUploadNotification extends JFrame {

	private JPanel contentPane;

	private File currentFile;

	private String user = null;

	private boolean terminateAtEnd = false;

	/**
	 * Create the frame.
	 */
	public StartUploadNotification(String filepath, String type) {

		if (filepath != null) {
			this.terminateAtEnd = true;
		}

		setType(javax.swing.JFrame.Type.UTILITY);
		setResizable(false);
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 333, 146);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				if (g instanceof Graphics2D) {
					Paint p = new Color(0, 0, 0, 200);
					Graphics2D g2d = (Graphics2D) g;
					g2d.setPaint(p);
					g2d.fillRect(0, 0, getWidth(), getHeight());
					g2d.setStroke(new BasicStroke(2));
					g2d.setPaint(new Color(0, 0, 0, 200));
					g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				}
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		BufferedImage image;
		try {
			image = ImageIO.read(getClass().getClassLoader().getResource("feamer.gif"));
			JPanel pane = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(image, 0, 0, null);
				}
			};
			pane.setLocation(23, 29);
			pane.setBackground(new Color(0, 0, 0, 0));
			pane.setSize(85, 93);
			getContentPane().add(pane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UIManager.put("ProgressBar.background", new Color(10, 10, 10));
		UIManager.put("ProgressBar.foreground", new Color(0, 173, 239));
		UIManager.put("ProgressBar.selectionBackground", new Color(10, 10, 10));
		UIManager.put("ProgressBar.selectionForeground", new Color(0, 173, 239));
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(118, 95, 205, 27);
		contentPane.add(progressBar);
		progressBar.setVisible(false);

		StartUploadNotification self = this;

		JComboBox comboBox = new JComboBox();
		comboBox.setVisible(false);
		comboBox.setFont(new Font("Arial", Font.PLAIN, 11));
		comboBox.setBounds(118, 97, 205, 25);
		FeamerPreferences.getInstance().getFriends(friends -> {
			comboBox.addItem("select a friend ...");
			for (String friend : friends) {
				comboBox.addItem(friend);
			}
		});
		comboBox.addActionListener(al -> {
			if (comboBox.getSelectedIndex() > 0) {
				this.user = String.valueOf(comboBox.getSelectedItem());
				if (self.currentFile != null) {
					FeamerPreferences.getInstance().transferFileToFriend(self.currentFile, this.user, progress -> {
						progressBar.setValue((int) (progress));
						progressBar.setVisible(true);
					}, this.terminateAtEnd);
				} else {

				}
			}

		});
		contentPane.add(comboBox);

		JLabel lblNewLabel = new JLabel("Drop files here");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBackground(SystemColor.control);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(118, 29, 205, 62);
		contentPane.add(lblNewLabel);

		lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

		JLabel lblAccept = new JLabel("transfer");
		lblAccept.setHorizontalAlignment(SwingConstants.CENTER);
		lblAccept.setForeground(new Color(0, 173, 239));
		lblAccept.setFont(new Font("Arial", Font.BOLD, 16));
		lblAccept.setBounds(118, 97, 79, 25);
		lblAccept.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lblAccept);

		lblNewLabel.setTransferHandler(new FileDropHandler(files -> {
			if (files == null || files.size() == 0) {
				return;
			}
			this.currentFile = files.get(0);
			lblNewLabel.setText(files.get(0).getName());

			if (type != null && type.equals("friend") && this.user != null) {
				FeamerPreferences.getInstance().transferFileToFriend(self.currentFile, this.user, progress -> {
					progressBar.setValue((int) (progress));
					progressBar.setVisible(true);
					System.out.println("progress: "+progress);
					if(progress > 100) {
						self.dispose();
					}
				}, this.terminateAtEnd);
			}
		}));

		lblAccept.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				FeamerPreferences.getInstance().transferFile(self.currentFile, progress -> {
					progressBar.setValue((int) (progress));
					progressBar.setVisible(true);

					System.out.println("progress: "+progress);
					if(progress > 100) {
						self.dispose();
					}
				}, false);
			}
		});

		JLabel lblDecline = new JLabel("cancel");
		lblDecline.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				self.dispose();
			}
		});
		lblDecline.setHorizontalAlignment(SwingConstants.CENTER);
		lblDecline.setForeground(new Color(255, 106, 0));
		lblDecline.setFont(new Font("Arial", Font.BOLD, 16));
		lblDecline.setBounds(222, 97, 79, 25);
		lblDecline.setCursor(new Cursor(Cursor.HAND_CURSOR));
		StartUploadNotification rn = this;

		if (type != null) {
			if (type.equals("friend")) {
				comboBox.setVisible(true);
				lblAccept.setVisible(false);
				lblDecline.setVisible(false);
				if (filepath != null) {
					this.currentFile = new File(filepath);
					lblNewLabel.setText(this.currentFile.getName());
				}
			} else {
				if (filepath != null) {
					this.currentFile = new File(filepath);
					lblNewLabel.setText(this.currentFile.getName());
					FeamerPreferences.getInstance().transferFile(self.currentFile, progress -> {
						progressBar.setValue((int) (progress));
						progressBar.setVisible(true);
						System.out.println("progress: "+progress);
						if(progress > 100) {
							self.dispose();
						}
					}, this.terminateAtEnd);
				}
			}
		}

		contentPane.add(lblDecline);
	}

}
