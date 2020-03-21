package bullet_shot_game;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@ServerEndpoint("/point")
public class BulletShotServerEndPoint {
    private Map<Session, GameController> userSessions = Collections.synchronizedMap(new HashMap<Session, GameController>());
    @OnOpen
    public void onOpen(Session userSession) {
        GameController curController = new GameController(userSession);
        curController.initializeClient();
        userSessions.put(userSession, curController);
    }

    @OnClose
    public void onClose(Session userSession) {
        GameController controller = userSessions.get(userSession);
        controller.endGame();
        userSessions.remove(userSession);
    }
    
    @OnMessage
    public void onMessage(String message, Session userSession) {
        userSessions.get(userSession).handleMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        //Do error handling here
    }
}
