package edu.pitt.battleshipgame.common;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.pitt.battleshipgame.common.board.Board;
import edu.pitt.battleshipgame.common.board.Coordinate;
import javafx.concurrent.Task;
import java.lang.Thread;

public class GameTracker {
    public static final int MAX_PLAYERS = 2;
    private int registeredPlayers = 0;
    private ArrayList<Board> gameBoards;
    private GameState state = GameState.INIT;
    private Integer playerTurn;
    public Coordinate lastShot;
    public boolean gameOver = false;
    public String[] messages = new String[2];
    public int[] counts = {0,0};
    Object lock;
    public boolean test = false;
    
    public GameTracker() throws UnknownHostException, InterruptedException {
        // Exists to protect this object from direct instantiation
        lock = new Object();
        gameBoards = new ArrayList<Board>(MAX_PLAYERS);
        System.out.println("Server constructed.");



        //System.out.println("IP:" + Inet4Address.getLocalHost().getHostAddress());
    }
    public void createThreads() throws InterruptedException {
        Task<Void> player0Connection = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                test = true;
                System.out.println("Hello From 0");
                checkConnection(0);
                return null;
            }
        };

        Thread player0 = new Thread(player0Connection);
        player0.setDaemon(true);
        player0.start();
        System.out.println(player0.isAlive());

        Task<Void> player1Connection = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Hello from 1");
                checkConnection(1);
                return null;
            }
        };
        System.out.println(player0.isAlive());
        Thread player1 = new Thread(player1Connection);
        player1.setDaemon(true);
        player1.start();
        System.out.println(player0.isAlive());
    }
    public int registerPlayer() {
        synchronized(lock) {
            registeredPlayers++;
            gameBoards.add(new Board("Player " + (registeredPlayers - 1) + " board"));
        }
        return registeredPlayers - 1;
    }
    public void sendMove(Coordinate move){
        lastShot = move;
    }
    public Coordinate getLastShot(){
        return lastShot;
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
                        Thread.sleep(500);
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
        
        // If this is the first turn, randomly generate who gets it
        if (state == GameState.PLACING && playerTurn == null)
        {
             playerTurn = (Math.random() < 0.5 ? 0 : 1);
        }
        else
        {
             playerTurn = (playerTurn + 1) % registeredPlayers;
        }
    }

    public void setBoard(Board board, int playerID){
        System.out.println("Player " + playerID + " board:");
        System.out.println(board.getShipList());
        gameBoards.remove(playerID);
        gameBoards.add(playerID,board);
    }
    
    public boolean isGameOver() {
        System.out.println("Checking if the game is over...");
        if(gameOver){
            return true;
        }
        for(Board board : gameBoards) {
            if(board.areAllShipsSunk()) {
                System.out.println("Returning True");
                return true;
            }
        }
        return false;
    }
    public void sendMessage(String message, int playerID){
        if(message.equals("Opponent Lost Game due to timeout")){
            gameOver = true;
            messages[playerID] = "You lose the game due to timeout";
        }

        messages[(playerID+1)%registeredPlayers] = message;
    }
    public String checkMessage(int playerID){
        counts[playerID]++;
        return messages[playerID];
    }
    public void checkConnection(int playerID) throws InterruptedException {
        while(counts[playerID] !=0){
            System.out.println("Test");
            Thread.sleep(1000);
        }
        int count = 0;
        while(!gameOver){
            count++;
            System.out.println(count + "" + playerID);
            if(count > counts[playerID] + 2){
                System.out.println("Player "+ playerID + "DCed");
                sendMessage("Your opponent has disconnected", (playerID+1)%registeredPlayers);

            }
            Thread.sleep(1000);
        }

    }
}
