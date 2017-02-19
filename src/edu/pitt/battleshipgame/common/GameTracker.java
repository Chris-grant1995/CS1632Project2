package edu.pitt.battleshipgame.common;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.battleshipgame.common.board.Board;

public class GameTracker {
    public static final int MAX_PLAYERS = 2;
    private int registeredPlayers = 0;
    private ArrayList<Board> gameBoards;
    private GameState state = GameState.INIT;
    private int playerTurn = 0;
    Object lock;
    
    public GameTracker() {
        // Exists to protect this object from direct instantiation
        lock = new Object();
        gameBoards = new ArrayList<Board>(MAX_PLAYERS);
        System.out.println("Server constructed.");
    }

    public int registerPlayer() {
        synchronized(lock) {
            registeredPlayers++;
            gameBoards.add(new Board("Player " + (registeredPlayers - 1) + " board"));
        }
        return registeredPlayers - 1;
    }

    public void wait(int playerID) {

        switch (state) {
            case INIT:
            {
                System.out.println("Player " + playerID + " is waiting for other players");
                while(registeredPlayers < MAX_PLAYERS) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println(e + " I can't sleep!");
                    }
                }
                System.out.println("Playing");
                state = GameState.PLACING;
                break;
            }
            case PLACING:
            {
                System.out.println("Placing Wait");
                int shipCount =0;
                while(shipCount != 10){
                    for(Board board:gameBoards){
                        shipCount+=board.getShipList().size();
                    }

                    if(shipCount == 10){
                        break;
                    }
                    else{
                        //System.out.println(shipCount);
                        shipCount =0;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.err.println(e + " I can't sleep!");
                        }
                    }
                }

                setBoards(gameBoards);
                state= GameState.PLAYING;
                break;
            }
            case PLAYING:
            {
                while(playerTurn != playerID) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println(e + " I can't sleep!");
                    }
                }
                break;
            }
            default:
                break;
        }
        System.out.println("Done Waiting!");
    }
    
    public List<Board> getBoards() {
        return gameBoards;
    }
    
    public void setBoards(ArrayList<Board> boards) {
        gameBoards = boards;
        playerTurn = (playerTurn + 1) % registeredPlayers;
    }

    public void setBoard(Board board, int playerID){
        System.out.println("Player " + playerID + " board:");
        System.out.println(board.getShipList());
        gameBoards.remove(playerID);
        gameBoards.add(playerID,board);
    }
    
    public boolean isGameOver() {
        System.out.println("Checking if the game is over...");
        for(Board board : gameBoards) {
            if(board.areAllShipsSunk()) {
                System.out.println("Returning True");
                return true;
            }
        }
        return false;
    }
}