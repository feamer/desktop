package feamer.desktop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ClipboardNotification extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public ClipboardNotification(File file) {

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

		JLabel lblHeyJrgWhat = new JLabel("File was copied into your clipboard.");
		lblHeyJrgWhat.setForeground(Color.WHITE);
		lblHeyJrgWhat.setFont(new Font("Arial", Font.BOLD, 16));
		lblHeyJrgWhat.setBounds(118, 29, 205, 55);
		lblHeyJrgWhat.setUI(MultiLineLabelUI.labelUI);
		contentPane.add(lblHeyJrgWhat);

		ClipboardNotification self = this;
		JLabel lblAccept = new JLabel("open file");
		lblAccept.setHorizontalAlignment(SwingConstants.LEFT);
		lblAccept.setForeground(new Color(0, 173, 239));
		lblAccept.setFont(new Font("Arial", Font.BOLD, 16));
		lblAccept.setBounds(117, 82, 85, 25);
		lblAccept.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblAccept.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(file.getAbsolutePath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		contentPane.add(lblAccept);

		JLabel lblDecline = new JLabel("dismiss");
		lblDecline.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				self.dispose();
			}
		});
		lblDecline.setHorizontalAlignment(SwingConstants.CENTER);
		lblDecline.setForeground(new Color(255, 106, 0));
		lblDecline.setFont(new Font("Arial", Font.BOLD, 16));
		lblDecline.setBounds(218, 82, 79, 25);
		lblDecline.setCursor(new Cursor(Cursor.HAND_CURSOR));

		contentPane.add(lblDecline);
		
		JLabel lblShowInExplorer = new JLabel("show folder");
		lblShowInExplorer.setHorizontalAlignment(SwingConstants.LEFT);
		lblShowInExplorer.setForeground(new Color(0, 173, 239));
		lblShowInExplorer.setFont(new Font("Arial", Font.BOLD, 16));
		lblShowInExplorer.setBounds(117, 110, 183, 25);
		lblShowInExplorer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblShowInExplorer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(file.getAbsoluteFile().getParentFile().getAbsolutePath()));
					self.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
				self.dispose();
			}
		});
		contentPane.add(lblShowInExplorer);
	}
}
