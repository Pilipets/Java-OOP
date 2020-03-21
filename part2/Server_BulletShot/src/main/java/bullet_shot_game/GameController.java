package bullet_shot_game;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.Session;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class GameConfig{
    final double gravityConstant = 7.8;
    final int timeStep = 10;
    final double ballMass = 35;
    final double ballRadius = 0.4;
    final int ballSpeed = 24;
    final String preSharedKey = "sdcfgf3245@45";

    JSONObject JSONserialize(){
        JSONObject res_obj = new JSONObject();
        res_obj.put("gravityConstant", gravityConstant);
        res_obj.put("timeStep", timeStep);
        res_obj.put("ballMass", ballMass);
        res_obj.put("ballRadius", ballRadius);
        res_obj.put("ballSpeed", ballSpeed);
        return res_obj;
    }
}
class Proto{
    static class ClientMessage{
        final static int StatsResponse = 27;
        final static int InitRequest = 16;
    }
    static class ServerMessage{
        final static int InitResponse = 26;
    }
}

class StatsHandler implements AutoCloseable {
    File file;
    FileWriter fw;
    PrintWriter printer;
    StatsHandler(String id){
        file = new File(String.format("%s%s.txt",
                "E:/Programming/University/OOP(Java)/part2/Server_BulletShot/output/",
                id));
        try {
            if(file.exists())
                file.delete();
            file.createNewFile();
            fw = new FileWriter(file, true);
            printer = new PrintWriter(fw);
        } catch (IOException e){
            e.printStackTrace();
            try{
                fw.close();
                printer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void updateStats(long innerWidth, long innerHeight, long fps){
        printer.append(String.format("innerWidth=%d, innerHeight=%d, fps=%d\n",
                innerWidth, innerHeight, fps));
    }

    @Override
    public void close() {
        try{
            printer.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
public class GameController {
    private Session userSession;
    private GameConfig config;
    private StatsHandler statsHandler;
    public GameController(Session userSession){
        this.userSession = userSession;
        this.config = new GameConfig();
        this.statsHandler = new StatsHandler(userSession.getId());
    }

    public void handleMessage(String message){
        JSONParser parser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            System.out.println("Received from user" + jsonObject.toJSONString());
            long cmd_code = (Long)jsonObject.get("command");
            switch ((int)cmd_code) {
                case Proto.ClientMessage.InitRequest:
                    startGameClientRequest(jsonObject);
                    break;
                case Proto.ClientMessage.StatsResponse:
                    statsClientResponse(jsonObject);
                    break;
                default:
                    System.out.println("Unexpected command type");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    private void statsClientResponse(JSONObject msg) {
        JSONObject content = (JSONObject) msg.get("content");
        long width = (Long)content.get("innerWidth"), height = (Long)content.get("innerHeight");
        long fps = (Long)content.get("fps");

        statsHandler.updateStats(width, height, fps);
    }

    private void startGameClientRequest(JSONObject req) {
        String secretKey = (String)req.get("preSharedKey");
        if(secretKey.compareTo(config.preSharedKey) != 0){
            System.out.println("Error, invalid secret key: " + config.preSharedKey);
        }

        JSONObject obj = new JSONObject();
        obj.put("command", Proto.ServerMessage.InitResponse);
        obj.put("content", config.JSONserialize());

        try {
            System.out.println("Sending to user" + obj.toJSONString());
            userSession.getBasicRemote().sendText(obj.toJSONString());
        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("Error while sending" + obj.toJSONString());
        }
    }
    public void endGame() {
        System.out.println("Closing the game session");
    }

    void initializeClient() {
        System.out.println("Established connection from" + userSession.toString());
    }
}
