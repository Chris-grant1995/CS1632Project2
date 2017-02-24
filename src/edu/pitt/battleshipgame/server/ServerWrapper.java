package edu.pitt.battleshipgame.server;

import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.pitt.battleshipgame.common.board.Board;
import edu.pitt.battleshipgame.common.Serializer;
import edu.pitt.battleshipgame.common.GameTracker;
import edu.pitt.battleshipgame.common.ServerInterface;
import edu.pitt.battleshipgame.common.board.Coordinate;
import javafx.concurrent.Task;
import java.lang.Thread;

import java.net.UnknownHostException;

//Service Implementation
@WebService(endpointInterface = "edu.pitt.battleshipgame.common.ServerInterface")
/**
 * This Wrapper exists to translate network requests to API compatible requests
 */
public class ServerWrapper implements ServerInterface {
    // We have a pseudo singleton around the Server object.
    private static GameTracker tracker = null;


    public ServerWrapper() throws UnknownHostException, InterruptedException {
        tracker = getInstance();
        tracker.createThreads();

        /*Task<Void> test = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Testing");
                return null;
            }
        };
        Thread t = new Thread(test);
        t.setDaemon(true);
        t.start();

        */
    }
    

    public static GameTracker getInstance() throws UnknownHostException, InterruptedException {
        if(tracker == null) {
            tracker = new GameTracker();

        }
        return tracker;
    }

    /**
     * Server.registerPlayer
     * 
     * @return The id of the registered player.
     */
    @Override
    public int registerPlayer() {
        return tracker.registerPlayer();
    }

    /**
     * Server.waitForPlayers
     * 
     * @param playerID The ID of the player that is waiting.
     */
    @Override
    public void wait(int playerID) {
        tracker.wait(playerID);
    }
    
    /**
     * The Network version of @see Server.getBoards will convert the array list
     * to a byte array.
     * 
     * @return The serialized version of the boards array. 
     */
    @Override
    public byte [] getBoards() {
        return Serializer.toByteArray(new ArrayList<Board>(tracker.getBoards()));
    }
    
    /**
     * The Network version of @see Server.registerBoard. It will convert the
     * byte [] board to a Board object to be passed to the Server.
     * 
     * playerID The ID of the player registering a board.
     * 
     * board The serialized representation of the board the player wants
     *              to register.
     */
    @Override
    public void setBoards(byte [] boards) {
        tracker.setBoards((ArrayList<Board>)Serializer.fromByteArray(boards));
    }

    @Override
    public void setBoard(byte [] board, int myPlayerID) { tracker.setBoard((Board) Serializer.fromByteArray(board), myPlayerID);}

    @Override
    public void sendMove(byte[] coord){
        tracker.sendMove((Coordinate) Serializer.fromByteArray(coord));
    }
    public byte[] getLastShot(){
        return Serializer.toByteArray(tracker.getLastShot());
    }

    @Override
    public void getMessage(byte[] message, int playerID){
        tracker.sendMessage((String)Serializer.fromByteArray(message), playerID);
    }
    @Override
    public byte[] checkMessage(int playerID){
        return Serializer.toByteArray(tracker.checkMessage(playerID));
    }
    
    public boolean isGameOver(){
        return tracker.isGameOver();
    }
}
