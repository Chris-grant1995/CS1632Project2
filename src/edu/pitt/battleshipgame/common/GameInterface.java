package edu.pitt.battleshipgame.common;

import edu.pitt.battleshipgame.common.board.*;
import java.util.ArrayList;

public interface GameInterface {
    int registerPlayer();
    void wait(int playerID);
    ArrayList<Board> getBoards();
    void setBoards(ArrayList<Board> boards);
    boolean isGameOver();
    void setBoard(Board board, int playerID);
    void sendMove(Coordinate coord);
    Coordinate getLastShot();
    void sendMessageToOtherPlayer(String message, int playerID);
    String checkMessages(int playerID);
    boolean checkConnection();
}