package edu.pitt.battleshipgame.client;

import java.util.*;

import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.GameInterface;
import edu.pitt.battleshipgame.common.GameTracker;

import java.net.URL;

import javafx.application.Platform;
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
    public String abbreviation = "";
    public ArrayList<String> shipAbbr = new ArrayList<>();
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
        //Method that gets called when top button gets pressed

        Button b = (Button)event.getSource();
        System.out.println(b.getText());
        //b.setVisible(false);
        b.setText("HIT");
        b.setDisable(true);
    }
    @FXML
    void place(ActionEvent event) throws InterruptedException {
        //Method that gets called when a bottom button gets pressed
        Button b = (Button)event.getSource();
        String text = b.getText();

        //Add the : that the coordinate requires
        place = text.substring(0, 1) + ":" + text.substring(1,text.length());

        //Wait for the other thread to calculate the length of the ship we are creating
        while(length == 0){
            Thread.sleep(1000);

        }

        //Update the board according to the button press
        updatePlaceBoard(place,length-1, abbreviation);
        length = 0;

    }
    void disableOponentGrid(){
        //Disables all buttons on the top grid

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

    void enableOponentGrid(){
        //Disables all buttons on the top grid

        ObservableList<Node> test = oponentGrid.getChildren();
        int counter = 0;
        for(Node t:test){

            Button b = (Button)t;
            b.setDisable(false);
            counter++;
            if (counter == 100)
                break;
        }
    }
    void enablePlayerGrid(){
        //Enables all the buttons on the player's (bottom) grid
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
        //Disables all the buttons on the player's (bottom) grid)
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
    void startGame(ActionEvent event) throws InterruptedException {



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

        //Thread.currentThread().wait();
        /*
        while(!statusLabel.getText().equals("Done Placing Ships")){
            System.out.println("Testing");
            Thread.sleep(100);
        }


        gi.setBoards(gameBoards);
        System.out.println("Done Placing");
        gameBoards = gi.getBoards();

        GUIGameLoop();

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

    public void GUIPlaceShips(Board board){

        //This is the method that is run when we create a new thread below.
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                for (Ship.ShipType type : Ship.ShipType.values()) {
                    if (type != Ship.ShipType.NONE) {
                        updateMessage("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type));

                        //Wait until the user had pressed a button
                        while(place.equals("None")){
                            Thread.sleep(1000);
                        }
                        Coordinate start = new Coordinate(place);
                        //Gets abbreviation and length of the ship that we are placing.
                        abbreviation = ShipFactory.getAbbreviationFromType(type);
                        length = ShipFactory.getLengthFromType(type);

                        //Wait until all buttons that cannot be legitimately pressed are disabled
                        while(length!=0){
                            Thread.sleep(1000);
                        }
                        place = "None";
                        startPlacement = false;

                        updateMessage("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type));

                        //Wait until the user had pressed a button
                        while(place.equals("None")){
                            //System.out.println(place);
                            Thread.sleep(1000);
                        }

                        //Gets abbreviation and length of the ship that we are placing.
                        abbreviation = ShipFactory.getAbbreviationFromType(type);
                        length = ShipFactory.getLengthFromType(type);

                        //Wait until all buttons that cannot be legitimately pressed are disabled
                        while(length!=0){
                            Thread.sleep(1000);
                        }
                        Coordinate end = new Coordinate(place);
                        place = "None";
                        ShipFactory.newShipFromType(type, start, end,board);


                    }
                }
                //Now that we are done placing ships, disable all buttons on the player's grid
                updateMessage("Done Placing Ships");
                disablePlayerGrid();

                gi.setBoards(gameBoards);
                //GUIGameLoop();

                updateMessage("Waiting for your turn");

                gi.wait(myPlayerID);
                System.out.println("Your Turn!");
                updateMessage("Its your turn!");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //statusLabel.setText("Its your turn!");
                        enableOponentGrid();
                    }
                });

                return 0;
            }
        };
        //Create the new thread and start it.
        statusLabel.textProperty().bind(task.messageProperty());
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    public void updatePlaceBoard(String p, int length, String abbr){
        //Method that disables buttons that need to be disabled, and updates button labels

        //Add ship abbreviation to list
        shipAbbr.add(abbr);

        //If we are placing the "head" of the ship
        if(startPlacement){
            ArrayList<String> options = new ArrayList<>();
            //int x = 0;

            //Seperate number and letter from string.
            char letter = p.charAt(0);
            int num;
            num = Integer.parseInt(p.substring(2,p.length()));
            //Save this location for when we place the "tail" of the ship
            firstPlace = letter + "" + num;

            //Add all possible tail values, depending on the length of the ship
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



            //Create a copy of options because we will be destorying options2
            ArrayList<String> options2 = new ArrayList<>();
            for(String option: options){
                options2.add(option);
            }

            //Checks to make sure intersections are not allowed.
            for(String option:options2){
                char letter1 = firstPlace.charAt(0);
                int num1 = Integer.parseInt(firstPlace.substring(1,firstPlace.length()));

                char letter2 = option.charAt(0);
                int num2 = Integer.parseInt(option.substring(1,option.length()));

                //Lists all buttons that would be disabled if the user were to select that specific option
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

                //Searches for the buttons in path, and if it finds that the button isn't occupied, it removes it.
                ObservableList<Node> buttons = playerGrid.getChildren();
                int counter2=0;
                for(Node b: buttons){
                    Button button = (Button)b;
                    if(path.contains(button.getText())){
                        path.remove(button.getText());
                    }
                    counter2++;
                    if (counter2 == 100)
                        break;
                }
                //When the above for loop finishes, path will contain the coordinates of any occupied button,
                // so if path isn't empty, the path would intersect with another ship, so we remove that option
                if(path.size() != 0){
                    options.remove(option);
                }


            }
            //Disable any button that isn't contained in the good options list.
            ObservableList<Node> test = playerGrid.getChildren();
            int counter = 0;
            for(Node t:test){

                Button b = (Button)t;

                if(!options.contains(b.getText()) || shipAbbr.contains(b.getText())){
                    b.setDisable(true);
                }



                counter++;
                if (counter == 100)
                    break;
            }
            //Tells the method that we have placed the "head" of our ship
            startPlacement = false;

        }
        //we've placed the head, so now we are placing the tail
        else{
            ArrayList<String> greyedOut = new ArrayList<>();
            //int x = 0;

            //Break up strings letter and number
            char letter2 = p.charAt(0);
            int num2;
            num2 = Integer.parseInt(p.substring(2,p.length()));

            //Break up the firstPlace letter and number
            char letter1 = firstPlace.charAt(0);
            int num1;
            num1 = Integer.parseInt(firstPlace.substring(1,p.length()-1));

            String newS = letter1 + "" + num1;
            greyedOut.add(newS);
            newS = letter2 + "" + num2;
            greyedOut.add(newS);

            //Add all the buttons between button1 and button2 to be greyed out
            if(letter1 == letter2){
                for(int i = num1; i<num2; i++){
                    newS = letter1 + "" + i;
                    greyedOut.add(newS);
                }
                for(int i = num2; i<num1; i++){
                    newS = letter1 + "" + i;
                    greyedOut.add(newS);
                }
            }
            if(num1 == num2){
                for(char c = letter1; c<letter2; c++){
                    newS = c+ "" + num1;
                    greyedOut.add(newS);
                }
                for(char c = letter2; c<letter1; c++){
                    newS = c+ "" + num1;
                    greyedOut.add(newS);
                }
            }

            //Disable buttons for the path, sets abbreviations, enables buttons that should be enabled afterwards
            ObservableList<Node> test = playerGrid.getChildren();
            int counter = 0;
            for(Node t:test){

                Button b = (Button)t;

                if(greyedOut.contains(b.getText()) || shipAbbr.contains(b.getText())){
                    placedCoords.add(b.getText());
                    if(greyedOut.contains(b.getText())){
                        b.setText(abbr);
                    }

                    b.setDisable(true);


                }
                else{
                    b.setDisable(false);
                }

                counter++;
                if (counter == 100)
                    break;

            }
            //Says we are ready for the next head ship
            startPlacement = true;

        }

    }
    public void GUIGameLoop(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusLabel.setText("Waiting for your turn");
                    }
                });
                gi.wait(myPlayerID);
                System.out.println("Your Turn!");
                updateMessage("Its your turn!");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusLabel.setText("Its your turn!");
                        enableOponentGrid();
                    }
                });

                return null;
            }
        };

        //statusLabel.textProperty().bind(task.messageProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


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