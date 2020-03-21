package networking;

import model.Shape;

public class AddShapeCommand extends Command<Server>{
	private Shape addAShape;

	public AddShapeCommand(Shape addShape){
		this.addAShape = addShape;
	}

	public void execute(Server executeOn) {
		// add message to server's chat log
		executeOn.addShape(addAShape);
	}

}
