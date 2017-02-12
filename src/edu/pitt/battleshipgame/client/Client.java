package edu.pitt.battleshipgame.client;

import java.util.ArrayList;
import java.util.Scanner;

import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.GameInterface;
import edu.pitt.battleshipgame.common.GameTracker;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.concurrent.Task;

public class Client extends Application {
    public static GameInterface gi;
    public static int myPlayerID;
    public static ArrayList<Board> gameBoards;
    public static Scanner scan = new Scanner(System.in);
    public String place = "None";
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane oponentGrid;
    @FXML
    private Button a1;
    @FXML
    private GridPane playerGrid;
    @FXML
    private Label statusLabel;

    @FXML
    void initialize() {
        assert a1 != null : "fx:id=\"a1\" was not injected: check your FXML file 'Test.fxml'.";
        System.out.println("In initialize()");
        statusLabel.setText("Press the Button Below to Begin");
        disableOponentGrid();
        disablePlayerGrid();


    }
    @FXML
    void fire(ActionEvent event) {
        Button b = (Button)event.getSource();
        System.out.println(b.getText());
        //b.setVisible(false);
        b.setText("HIT");
        b.setDisable(true);
    }
    @FXML
    void place(ActionEvent event) {
        Button b = (Button)event.getSource();
        String text = b.getText();
        place = text.substring(0,1) + ":" + text.substring(1);
        System.out.println(place);

    }
    void disableOponentGrid(){
        ObservableList<Node> test = oponentGrid.getChildren();
        int counter = 0;
        for(Node t:test){

            Button b = (Button)t;
            b.setDisable(true);
            counter++;
            if (counter == 100)
                break;
        }
    }
    void enablePlayerGrid(){
        ObservableList<Node> test = playerGrid.getChildren();
        int counter = 0;
        for(Node t:test){

            Button b = (Button)t;
            b.setDisable(false);
            counter++;
            if (counter == 100)
                break;
        }
    }
    void disablePlayerGrid(){
        ObservableList<Node> test = playerGrid.getChildren();
        int counter = 0;
        for(Node t:test){

            Button b = (Button)t;
            b.setDisable(true);
            counter++;
            if (counter == 100)
                break;
        }
    }
    @FXML
    void startGame(ActionEvent event) {
        System.out.println("Testing");
        Button b = (Button)event.getSource();
        System.out.println("Testing2");
        b.setVisible(false);
        gi = new ClientWrapper();
        myPlayerID = gi.registerPlayer();
        statusLabel.setText("Waiting For another player to connect, You are player" + myPlayerID);
        gi.wait(myPlayerID);
        System.out.println("Testing Done Waiting");
        statusLabel.setText("Both Players have joined, starting game");

        gameBoards = gi.getBoards();
        Board board = gameBoards.get(myPlayerID);
        enablePlayerGrid();
        //statusLabel.setText("Please enter a start coordinate to place your Battleship");

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                System.out.println("Tests");
                for (Ship.ShipType type : Ship.ShipType.values()) {
                    if (type != Ship.ShipType.NONE) {
                        updateMessage("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type));
                        //System.out.println("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type));

                        while(place.equals("None")){
                            System.out.println(place);
                            Thread.sleep(1000);
                        }
                        Coordinate start = new Coordinate(place);
                        place = "None";

                        updateMessage("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type));
                        System.out.println("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type));
                        while(place.equals("None")){
                            System.out.println(place);
                            Thread.sleep(1000);
                        }
                        System.out.println("Done");
                        Coordinate end = new Coordinate(place);
                        place = "None";
                        ShipFactory.newShipFromType(type, start, end,board);


                    }
                }
                return 0;
            }
        };

        statusLabel.textProperty().bind(task.messageProperty());
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
        //Thread.sleep(5000);


    }
    public static void main(String [] args) {

        launch(args);
        System.out.println("Test");
        /*
        gi = new ClientWrapper();
        myPlayerID = gi.registerPlayer();
        System.out.println("You have registered as Player " + myPlayerID);
        System.out.println("Please wait for other players to join");
        gi.wait(myPlayerID);
        System.out.println("Both Players have joined, starting the game.");
        gameBoards = gi.getBoards();
        placeShips(gameBoards.get(myPlayerID));
        System.out.println("Your board:");
        System.out.println(gameBoards.get(myPlayerID).toString(true));
        gi.setBoards(gameBoards);
        gameLoop();

        */
    }

    public static void placeShips(Board board) {
        System.out.println("Your Board:");
        System.out.println(board.toString(true));
        for(Ship.ShipType type : Ship.ShipType.values()) {
            if(type != Ship.ShipType.NONE) {
                System.out.println("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type));
                Coordinate start = new Coordinate(scan.nextLine());
                System.out.println("Please enter an end coordinate to place your " + ShipFactory.getNameFromType(type));
                Coordinate end = new Coordinate(scan.nextLine());
                // We don't need to track a reference to the ship since it will be
                // on the board.
                ShipFactory.newShipFromType(type, start, end, board);
            }
        }
    }
    public static void gameLoop() {
        System.out.println("The game is starting!");
        do {
            // Wait for our turn
            gi.wait(myPlayerID);
            // Get the updated boards
            gameBoards = gi.getBoards();
            System.out.println("Where would you like to place your move?");
            Coordinate move = new Coordinate(scan.nextLine());
            Ship ship = gameBoards.get((myPlayerID + 1) % GameTracker.MAX_PLAYERS).makeMove(move);
            if(ship == null) {
                System.out.println("Miss");
            } else if (ship.isSunk()) {
                System.out.println("You sunk " + ship.getName());
            } else {
                System.out.println("Hit");
            }
            // Send the updated boards.
            gi.setBoards(gameBoards);
        } while(!gi.isGameOver());
        System.out.println("The Game is Over!");
    }
}