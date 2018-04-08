package feamer.desktop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import javax.swing.JProgressBar;

public class RequestNotification extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RequestNotification frame = new RequestNotification("", 0, "", 0);
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
	public RequestNotification(String name, long timestamp, String endpoint, long size) {

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

		UIManager.put("ProgressBar.background", new Color(10,10,10));
		UIManager.put("ProgressBar.foreground", new Color(0, 173, 239));
		UIManager.put("ProgressBar.selectionBackground", new Color(10,10,10));
		UIManager.put("ProgressBar.selectionForeground", new Color(0, 173, 239));
		 try {
			UIManager.setLookAndFeel(
			            UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(118, 97, 183, 25);
		progressBar.setVisible(false);
		contentPane.add(progressBar);
		
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
	        pane.setBackground(new Color(0,0,0,0));
			pane.setSize(85, 93);
	        getContentPane().add(pane);
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
		
		JLabel lblHeyJrgWhat = new JLabel("user wants to share "+name+" with you!");
		lblHeyJrgWhat.setForeground(Color.WHITE);
		lblHeyJrgWhat.setFont(new Font("Arial", Font.BOLD, 16));
		lblHeyJrgWhat.setBounds(118, 29, 205, 67);
		lblHeyJrgWhat.setUI(MultiLineLabelUI.labelUI);
		contentPane.add(lblHeyJrgWhat);

		RequestNotification self = this;
		JLabel lblAccept = new JLabel("accept");
		lblAccept.setHorizontalAlignment(SwingConstants.CENTER);
		lblAccept.setForeground(new Color(0,173,239));
		lblAccept.setFont(new Font("Arial", Font.BOLD, 16));
		lblAccept.setBounds(118, 97, 79, 25);
		lblAccept.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblAccept.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new Thread(()->{
					CloseableHttpClient httpclient = HttpClients.createDefault();
					HttpGet httpGet = new HttpGet(FeamerPreferences.getInstance().get(FeamerPreferences.ENDPOINT) + ""+endpoint);
					httpGet.addHeader("Authorization", FeamerPreferences.getInstance().getToken());

					try {
						CloseableHttpResponse response2 = httpclient.execute(httpGet);
						
						System.out.println(response2);
						
						BufferedInputStream bis = new BufferedInputStream(response2.getEntity().getContent());
						String filePath = name;
						File outputFile = new File(filePath);
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
						int inByte;
						double numberOfBytes = 0;
						progressBar.setVisible(true);
						while((inByte = bis.read()) != -1) {
							bos.write(inByte);
							numberOfBytes++;
							if(true) {
								progressBar.setValue((int)(numberOfBytes/size*100));
							}
						}
						bis.close();
						bos.close();
						self.dispose();
						
						
						response2.close();
					
						copyFileToClipboard(outputFile);
						
						return;
					}catch (Exception e) {
						e.printStackTrace();
					}
				}).start();
				
				
			}
		});
		contentPane.add(lblAccept);
		
		JLabel lblDecline = new JLabel("decline");
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
		RequestNotification rn = this;
		
		contentPane.add(lblDecline);
	}

	protected void copyFileToClipboard(File outputFile) {
		List listOfFiles = new ArrayList();
		listOfFiles.add(outputFile);

		FileTransferable ft = new FileTransferable(listOfFiles);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				System.out.println("Lost ownership");
			}
		});
		
		Main.clipboardNotification(outputFile);
		this.dispose();
	}

	public static class FileTransferable implements Transferable {

		private List listOfFiles;

		public FileTransferable(List listOfFiles) {
			this.listOfFiles = listOfFiles;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.javaFileListFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return listOfFiles;
		}
	}
}
