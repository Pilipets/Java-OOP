package networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import model.Shape;

public class Server {
	private ServerSocket socket;

	private List<Shape> listOfShapes;
	private HashMap<String, ObjectOutputStream> outputs;
	private Logger logger = Logger.getLogger(Server.class.getName());

	private class ClientHandler implements Runnable {
		private ObjectInputStream input;

		public ClientHandler(ObjectInputStream input) {
			this.input = input;
		}

		public void run() {
			try {
				while (true) {

					Command<Server> command = (Command<Server>) input.readObject();
					command.execute(Server.this);

					if (command instanceof DisconnectCommand) {
						input.close();
						return;
					}
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE,e.getMessage());
			}
		}
	}

	private class ClientAccepter implements Runnable {
		public void run() {
			try {
				while (true) {

					Socket s = socket.accept();
					ObjectOutputStream output = new ObjectOutputStream(
							s.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(
							s.getInputStream());

					String clientName = (String) input.readObject();

					boolean sameName = true;
					while (sameName) {
						sameName = false;
						for (String key : outputs.keySet()) {
							if (clientName.equals(key)) {
								clientName = JOptionPane.showInputDialog("That user name has already been used.  Choose another one: ");
								sameName = true;
								break;
							}
						}
					}

					output.writeObject(true);
					
					new Thread(new ClientHandler(input)).start();
					
					outputs.put(clientName, output);

					updateClients();
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE,e.getMessage());
			}
		}
	}

	public Server() {
		this.listOfShapes = new ArrayList<Shape>();
		this.outputs = new HashMap<String, ObjectOutputStream>();
		try {
			socket = new ServerSocket(1111);
			logger.info("NetPaintServer started on port 1111");
			new Thread(new ClientAccepter()).start();
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	public void addShape(Shape addShape) {
		listOfShapes.add(addShape);
		updateClients();
	}

	public void updateClients() {

		UpdateClientCommand update = new UpdateClientCommand(listOfShapes);
		try {
			for (ObjectOutputStream out : outputs.values())
				out.writeObject(update);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	public void disconnect(String clientName) {
		try {
			outputs.get(clientName).close();
			outputs.remove(clientName);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}
}