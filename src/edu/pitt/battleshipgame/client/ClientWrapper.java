package edu.pitt.battleshipgame.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.util.ArrayList;

import edu.pitt.battleshipgame.common.Serializer;
import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.*;

public class ClientWrapper implements GameInterface {
    // flip this to turn debug print statements on
    public static final boolean IS_DEBUG_MODE = false;
    
    ServerInterface serverInterface = null;
    int myPlayerID;
    static String ip;
    static boolean connected = false;

    private static ServerInterface getServer() {
        URL url = null;
        try {
            //url = new URL("http://localhost:9999/battleship?wsdl");
            String urlString = "http://" + ip + ":9999/battleship?wsdl";
            url = new URL(urlString);
            if (IS_DEBUG_MODE)
            {
                 System.out.println("test2");
            }
            //url = new URL("http://192.168.0.19:9999/battleship?wsdl");

            QName qname = new QName("http://server.battleshipgame.pitt.edu/", "ServerWrapperService");
            if (IS_DEBUG_MODE)
            {
                 System.out.println("test3");
            }

            Service service = Service.create(url, qname);
            connected = true;
            if (IS_DEBUG_MODE)
            {
                System.out.println("test4");
            }
            
            return service.getPort(ServerInterface.class);
        }
        catch (Exception e){
            connected = false;
            return null;
        }

    }
    
    public ClientWrapper(String ipAddr) {
        ip = ipAddr;
        serverInterface = getServer();
    }
    @Override
    public boolean checkConnection(){
        return connected;
    }
    @Override
    public int registerPlayer() {
        return serverInterface.registerPlayer();
    }
    
    @Override
    public void wait(int playerID) {

        serverInterface.wait(playerID);
    }
    
    @Override
    public void setBoards(ArrayList<Board> boards) {
        serverInterface.setBoards(Serializer.toByteArray(boards));
    }

    @Override
    public void setBoard(Board board, int myPlayerID) {
        serverInterface.setBoard(Serializer.toByteArray(board), myPlayerID);
    }
    @Override
    public void sendMove(Coordinate coord){
        serverInterface.sendMove(Serializer.toByteArray(coord));
    }
    @Override
    public Coordinate getLastShot(){
        return (Coordinate) Serializer.fromByteArray(serverInterface.getLastShot());
    }
    /**
     * Client side wrapper around the  
     */
    @Override
    public ArrayList<Board> getBoards() {
        return (ArrayList<Board>) Serializer.fromByteArray(serverInterface.getBoards());
    }
    @Override
    public void sendMessageToOtherPlayer(String message, int myPlayerID){
        serverInterface.getMessage(Serializer.toByteArray(message), myPlayerID);
    }

    @Override
    public String checkMessages(int myPlayerID){
        String message = (String) Serializer.fromByteArray(serverInterface.checkMessage(myPlayerID));
        return message;
    }

    public boolean isGameOver() {
        return serverInterface.isGameOver();
    }
}
