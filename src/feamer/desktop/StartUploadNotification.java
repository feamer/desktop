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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartUploadNotification extends JFrame {

	private JPanel contentPane;

	private File currentFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RequestNotification frame = new RequestNotification();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartUploadNotification() {

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

		StartUploadNotification self = this;
		
		lblNewLabel.setTransferHandler(new FileDropHandler(files -> {
			if (files == null || files.size() == 0) {
				return;
			}
			this.currentFile = files.get(0);
			lblNewLabel.setText(files.get(0).getName());
		}));
		
		lblAccept.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				FeamerPreferences.getInstance().transferFile(self.currentFile, null);
				self.dispose();
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

		contentPane.add(lblDecline);
	}

}
