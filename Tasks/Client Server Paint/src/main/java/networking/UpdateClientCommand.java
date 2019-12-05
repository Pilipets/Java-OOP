package networking;

import java.util.ArrayList;
import java.util.List;

import model.Shape;

public class UpdateClientCommand extends Command<Client> {
	private List<Shape> listOfShapes;
	

	public UpdateClientCommand(List<Shape> listOfShapes){
		this.listOfShapes = new ArrayList<Shape>(listOfShapes);
	}

	public void execute(Client executeOn) {
		// update the client
		executeOn.update(listOfShapes);
	}
}
