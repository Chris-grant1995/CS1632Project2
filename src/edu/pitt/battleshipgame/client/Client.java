package edu.pitt.battleshipgame.client;

import java.util.*;

import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.GameInterface;
import edu.pitt.battleshipgame.common.GameTracker;

import java.net.URL;

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
    public int length = 0;
    public boolean startPlacement = true;
    public String firstPlace = "";
    public ArrayList<String> placedCoords = new ArrayList<>();
    //public String[][] playerBoard = new String[10][10];
    //public ArrayList<Pair> playerBoardList = new ArrayList<>();
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
    void place(ActionEvent event) throws InterruptedException {
        Button b = (Button)event.getSource();
        String text = b.getText();
        place = text.substring(0, 1) + ":" + text.substring(1,text.length());
        System.out.println(place);
        //Thread.sleep(5000);
        while(length == 0){
            Thread.sleep(1000);

        }
        System.out.println(place);
        updatePlaceBoard(place,length-1);
        length = 0;

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



        //System.out.println("Testing");
        Button b = (Button)event.getSource();
       // System.out.println("Testing2");
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
        GUIPlaceShips(board);
        /*
        gi.setBoards(gameBoards);
        System.out.println("Done Placing");
        gameBoards = gi.getBoards();

        board = gameBoards.get(myPlayerID);

        updatePlayerBoard(board);
        */

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
    public void updatePlayerBoard(Board board){
        List<Ship> ships = board.getShipList();

        for(Ship ship:ships){
            List<Coordinate> coords = ship.getCoordinates();
            System.out.println(ship.getName());
            for (Coordinate coord:coords){
                System.out.println(coord);
            }
        }
    }
    public void GUIPlaceShips(Board board){
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
                        length = ShipFactory.getLengthFromType(type);
                        System.out.println("Length " +length );
                        //System.out.println(length);
                        //updatePlaceBoard(place, length-1);
                        while(length!=0){
                            Thread.sleep(1000);
                        }
                        place = "None";
                        startPlacement = false;

                        updateMessage("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type));
                        //System.out.println("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type));
                        while(place.equals("None")){
                            //System.out.println(place);
                            Thread.sleep(1000);
                        }
                        length = ShipFactory.getLengthFromType(type);
                        while(length!=0){
                            Thread.sleep(1000);
                        }
                        Coordinate end = new Coordinate(place);
                        place = "None";
                        ShipFactory.newShipFromType(type, start, end,board);


                    }
                }
                System.out.println("Done");
                disablePlayerGrid();
                return 0;
            }
        };

        statusLabel.textProperty().bind(task.messageProperty());
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    public void updatePlaceBoard(String p, int length){
        if(startPlacement){
            ArrayList<String> options = new ArrayList<>();
            //int x = 0;
            char letter = p.charAt(0);
            int num;
            if(p.contains("10")){
                num = Integer.parseInt(p.substring(2,4));
            }
            else{
                num = Integer.parseInt(p.substring(2,3));
            }

            firstPlace = letter + "" + num;
            /*switch (letter){
                case 'A':
                    x = 0;
                    break;
                case 'B':
                    x=1;
                    break;
                case 'C':
                    x = 2;
                    break;
                case 'D':
                    x=3;
                    break;
                case 'E':
                    x = 4;
                    break;
                case 'F':
                    x=5;
                    break;
                case 'G':
                    x = 6;
                    break;
                case 'H':
                    x=7;
                    break;
                case 'I':
                    x = 8;
                    break;
                case 'J':
                    x=9;
                    break;
            }

            int y = num -1;

            playerBoard[y][x] = "b";
            */
            char newL = (char) (letter-length);
            String newS = newL + "" + num;
            options.add(newS);

            newL = (char) (letter+length);
            newS = newL + "" + num;
            options.add(newS);

            int newN = num - length;
            newS = letter + "" + newN;
            options.add(newS);

            newN = num + length;
            newS = letter + "" + newN;
            options.add(newS);

            ObservableList<Node> test = playerGrid.getChildren();
            System.out.println(firstPlace);
            int counter = 0;
            System.out.println(options);


            ArrayList<String> options2 = new ArrayList<>();
            for(String option: options){
                options2.add(option);
            }


            for(String option:options2){
                char letter1 = firstPlace.charAt(0);
                int num1 = Integer.parseInt(firstPlace.substring(1,firstPlace.length()));

                char letter2 = option.charAt(0);
                int num2 = Integer.parseInt(option.substring(1,option.length()));

                ArrayList<String> path = new ArrayList<>();
                if(letter1 == letter2){
                    System.out.println("Same Letters");
                    for(int i = num1; i<num2; i++){
                        newS = letter1 + "" + i;
                        path.add(newS);
                    }
                    for(int i = num2; i<num1; i++){
                        newS = letter1 + "" + i;
                        path.add(newS);
                    }
                }
                if(num1 == num2){
                    for(char c = letter1; c<letter2; c++){
                        newS = c+ "" + num1;
                        path.add(newS);
                    }
                    for(char c = letter2; c<letter1; c++){
                        newS = c+ "" + num1;
                        path.add(newS);
                    }
                }
                System.out.println(path);
                ObservableList<Node> buttons = playerGrid.getChildren();
                int counter2=0;
                for(Node b: buttons){
                    System.out.println(counter2);
                    Button button = (Button)b;
                    if(path.contains(button.getText())){
                        path.remove(button.getText());
                    }
                    counter2++;
                    if (counter2 == 100)
                        break;
                }
                System.out.println(path);
                if(path.size() != 0){
                    options.remove(option);
                }


            }
            for(Node t:test){

                Button b = (Button)t;

                if(!options.contains(b.getText()) || b.getText().equals("B")){
                    b.setDisable(true);
                }



                counter++;
                if (counter == 100)
                    break;
            }
            startPlacement = false;

        }
        else{
            ArrayList<String> greyedOut = new ArrayList<>();
            //int x = 0;
            char letter2 = p.charAt(0);
            int num2;
            if(p.contains("10")){
                num2 = Integer.parseInt(p.substring(2,4));
            }
            else{
                num2 = Integer.parseInt(p.substring(2,3));
            }
            char letter1 = firstPlace.charAt(0);
            int num1;
            if(p.contains("10")){
                num1 = Integer.parseInt(firstPlace.substring(1,3));
            }
            else{
                num1 = Integer.parseInt(firstPlace.substring(1,2));
            }

            String newS = letter1 + "" + num1;
            greyedOut.add(newS);
            newS = letter2 + "" + num2;
            greyedOut.add(newS);

            System.out.println("Num1" + num1);
            System.out.println("Num2" + num2);

            if(letter1 == letter2){
                System.out.println("Same Letters");
                for(int i = num1; i<num2; i++){
                    newS = letter1 + "" + i;
                    greyedOut.add(newS);
                    System.out.println("1:" + newS);
                }
                for(int i = num2; i<num1; i++){
                    newS = letter1 + "" + i;
                    greyedOut.add(newS);
                    System.out.println("2:" + newS);
                }
            }
            if(num1 == num2){
                System.out.println("Same Numbers");
                for(char c = letter1; c<letter2; c++){
                    newS = c+ "" + num1;
                    greyedOut.add(newS);
                }
                for(char c = letter2; c<letter1; c++){
                    newS = c+ "" + num1;
                    greyedOut.add(newS);
                }
            }


            ObservableList<Node> test = playerGrid.getChildren();
            int counter = 0;
            for(Node t:test){

                Button b = (Button)t;

                if(greyedOut.contains(b.getText()) || b.getText().equals("B")){
                    placedCoords.add(b.getText());
                    b.setText("B");
                    b.setDisable(true);


                }
                else{
                    b.setDisable(false);
                }

                counter++;
                if (counter == 100)
                    break;

            }
            startPlacement = true;

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