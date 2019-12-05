package networking;

public class DisconnectCommand extends Command<Server>{
	private String clientName; // client who is disconnecting

	public DisconnectCommand(String name){
		clientName = name;
	}

	@Override
	public void execute(Server executeOn) {
		executeOn.disconnect(clientName);
	}

}
