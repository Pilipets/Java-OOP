package networking;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Shape;
import view.NetPaintGUI;

public class Client extends JFrame{

	private String clientName; 
	private NetPaintGUI netPaint;
	private Logger logger = Logger.getLogger(Client.class.getName());
	private Socket server; 
	private ObjectOutputStream out; 
	private ObjectInputStream in; 

	private class ServerHandler implements Runnable{
		public void run() {
			try{
				while(true){
					Command<Client> c = (Command<Client>)in.readObject();
					c.execute(Client.this);
				}
			}
			catch(Exception e){
				logger.log(Level.SEVERE,e.getMessage());
			}
		}
	}

	public Client(){
		
		String host = JOptionPane.showInputDialog("Server address:");
		String port = JOptionPane.showInputDialog("Server port:");
		clientName = JOptionPane.showInputDialog("Client name:");
		
		if (host == null || port == null || clientName == null)
			return;
		
		try{
			
			server = new Socket(host, Integer.parseInt(port));
			out = new ObjectOutputStream(server.getOutputStream());
			in = new ObjectInputStream(server.getInputStream());
			
			out.writeObject(clientName);
			
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent arg0) {
					try {
						out.writeObject(new DisconnectCommand(clientName));
						out.close();
						in.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE,e.getMessage());
					}
				}
			});

			boolean temp = (boolean) in.readObject();
			
			setupGUI();
		
			new Thread(new ServerHandler()).start();
		}catch(Exception e){
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	private void setupGUI() {
	
		netPaint = new NetPaintGUI(out);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		this.setSize(d.width - 250, d.height - 150);
		this.setTitle("NetPaint");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(netPaint);
		
		this.setVisible(true);
	}

	public void update(List<Shape> currentShapes) {
		netPaint.update(currentShapes);
	}
}
	
