package edu.pitt.battleshipgame.client;

import java.util.*;

import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.GameInterface;
import edu.pitt.battleshipgame.common.GameTracker;

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.concurrent.Task;


public class Client extends Application {
    // flip this to turn debug print statements on
    public static final boolean IS_DEBUG_MODE = false;
    
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
    public String shot = "None";
    public boolean donePlacingShips = false;
    public boolean moved = true;
    public String serverIP;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField textfield;
    @FXML
    private Pane background;
    @FXML
    private GridPane oponentGrid;
    @FXML
    private Button a1;
    @FXML
    private GridPane playerGrid;
    @FXML
    private Label statusLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label boardLabel;

    @FXML
    private Button surrenderButton;

    @FXML
    private Button quitButton;
    boolean stopTimer = false;
    public Task<Integer> task;


    @FXML
    void initialize() {
        assert a1 != null : "fx:id=\"a1\" was not injected: check your FXML file 'Test.fxml'.";
        
        if (IS_DEBUG_MODE)
        {
             System.out.println("In initialize()");
        }
        
        // add Battleship styling to UI
        background.setStyle("-fx-background-color: #1167DC;");
        oponentGrid.setStyle("-fx-background-color: #86CBC4;");
        playerGrid.setStyle("-fx-background-color: #86CBC4;");
        statusLabel.setTextFill(Color.WHITE);
        timerLabel.setTextFill(Color.WHITE);
        boardLabel.setTextFill(Color.WHITE);
        ObservableList<Node> og = oponentGrid.getChildren();
        int i = 0;
        for ( Node n : og )
        {
            Button b = (Button)n;
            b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            i++;
            if (i == 100)
            {
                break;
            }
        }
        ObservableList<Node> pg = playerGrid.getChildren();
        i = 0;
        for ( Node n : pg )
        {
            Button b = (Button)n;
            b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            i++;
            if (i == 100)
            {
                break;
            }
        }
        
        statusLabel.setText("Enter the Server IP and then press the Button Below to Begin");
        timerLabel.setVisible(false);

        surrenderButton.setVisible(false);
        quitButton.setVisible(false);

        disableOponentGrid();
        disablePlayerGrid();


        Task<Void> test = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (IS_DEBUG_MODE)
                {
                     System.out.println("Testing");
                }
                
                return null;
            }
        };
        Thread t = new Thread(test);
        t.setDaemon(true);
        t.start();

    }

    @FXML
    void setIP(ActionEvent event) {
        if (IS_DEBUG_MODE)
        {
             System.out.println(textfield.getText());
        }
        
        serverIP=textfield.getText();
    }
    @FXML
    void fire(ActionEvent event) {
        //Method that gets called when top button gets pressed

        Button b = (Button)event.getSource();
        //System.out.println(b.getText());
        //b.setVisible(false);
        //b.setText("HIT");
        String text = b.getText();
        shot= text.substring(0, 1) + ":" + text.substring(1,text.length());

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
            Thread.sleep(10);

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
        //enables all buttons on the top grid that have not been fired on.

        ObservableList<Node> test = oponentGrid.getChildren();
        int counter = 0;
        for(Node t:test){

            Button b = (Button)t;
            if(!(b.getText().equals("Hit") || b.getText().equals("Miss"))){
                b.setDisable(false);
            }

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
    void startGame(ActionEvent event) throws InterruptedException, ExecutionException {

        serverIP = textfield.getText();

        if (IS_DEBUG_MODE)
        {
             System.out.println(serverIP);
        }

       // System.out.println("Testing2");


        //statusLabel.setText("Connecting to Server");

        gi = new ClientWrapper(serverIP);

        if(gi.checkConnection()){
            myPlayerID = gi.registerPlayer();
            Button b = (Button)event.getSource();
            b.setVisible(false);
            textfield.setVisible(false);

            quitButton.setVisible(true);
            surrenderButton.setVisible(true);
            statusLabel.setText("Waiting For another player to connect, You are player" + myPlayerID);

            Task<Void> gameTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if(myPlayerID == 0){
                        gi.wait(myPlayerID);
                    }


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            statusLabel.setText("Both Players have joined, starting game");
                            gameBoards = gi.getBoards();
                            Board board = gameBoards.get(myPlayerID);
                            enablePlayerGrid();

                            Task<Void> messageChecker = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {

                                    while(true){
                                        String message = gi.checkMessages(myPlayerID);
                                        if(message != null) {

                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //statusLabel.setText("Its your turn!");
                                                    statusLabel.textProperty().unbind();
                                                    statusLabel.setText(message);
                                                    disableOponentGrid();
                                                    disablePlayerGrid();
                                                    stopTimer = true;
                                                    surrenderButton.setDisable(true);
                                                }
                                            });
                                            break;

                                        }
                                        Thread.sleep(10);
                                    }

                                    return null;
                                }
                            };

                            Thread messageCheck = new Thread(messageChecker);
                            messageCheck.setDaemon(true);
                            messageCheck.start();

                            //statusLabel.setText("Please enter a start coordinate to place your Battleship");
                            GUIPlaceShips(board);
                        }
                    });



                    return null;
                }
            };



            //System.out.println("Testing Done Waiting");
            //statusLabel.setText("Both Players have joined, starting game");

            Thread gameThread = new Thread(gameTask);
            gameThread.setDaemon(true);
            gameThread.start();



        }
        else{
            statusLabel.setText("Unable to Connect to Server. Check IP and Try again");
        }

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
        primaryStage.setScene(new Scene(root, 800, 800));


        primaryStage.show();
        //Thread.sleep(5000);


    }
    public static void main(String [] args) {
        launch(args);
    }

    public void GUIPlaceShips(Board board){

        //This is the method that is run when we create a new thread below.
        task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                for (Ship.ShipType type : Ship.ShipType.values()) {
                    if (type != Ship.ShipType.NONE) {
                        updateMessage("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type));

                        //Wait until the user had pressed a button
                        while(place.equals("None")){
                            Thread.sleep(10);
                        }
                        Coordinate start = new Coordinate(place);
                        //Gets abbreviation and length of the ship that we are placing.
                        abbreviation = ShipFactory.getAbbreviationFromType(type);
                        length = ShipFactory.getLengthFromType(type);

                        //Wait until all buttons that cannot be legitimately pressed are disabled
                        while(length!=0){
                            if (IS_DEBUG_MODE)
                            {
                                 System.out.println("Wait until all buttons that cannot be legitimately pressed are disabled");
                            }
                            
                            Thread.sleep(10);
                        }
                        place = "None";
                        startPlacement = false;

                        updateMessage("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type));

                        //Wait until the user had pressed a button 
                        while(place.equals("None")){
                            if (IS_DEBUG_MODE)
                            {
                                 System.out.println("Wait until the user had pressed a button end");
                            }
                            
                            Thread.sleep(10);
                        }

                        //Gets abbreviation and length of the ship that we are placing.
                        abbreviation = ShipFactory.getAbbreviationFromType(type);
                        length = ShipFactory.getLengthFromType(type);

                        //Wait until all buttons that can be legitimately pressed are enabled
                        while(length!=0){
                            if (IS_DEBUG_MODE)
                            {
                                 System.out.println("Wait until all buttons that can be legitimately pressed are enabled");
                            }
                            
                            Thread.sleep(10);
                        }
                        Coordinate end = new Coordinate(place);
                        place = "None";
                        
                        // check if ship is "backwards" - if so, invert what we consider start and end for Ship constructor
                        if ((start.getRow() == end.getRow() && start.getCol() > end.getCol()) || (start.getCol() == end.getCol() && start.getRow() > end.getRow()))
                        {
                             ShipFactory.newShipFromType(type, end, start, board);
                        }
                        else
                        {
                             ShipFactory.newShipFromType(type, start, end, board);
                        }
                    }
                }
                //Now that we are done placing ships, disable all buttons on the player's grid
                updateMessage("Done Placing Ships");
                donePlacingShips = true;
                disablePlayerGrid();
                if (IS_DEBUG_MODE)
                {
                     printBoardInfo(board);
                }
                
                //gi.setBoards(gameBoards);
                gi.setBoard(board, myPlayerID);
                updateMessage("Waiting for your opponent to finish placing");
                gi.wait(myPlayerID);

                
                //GUIGameLoop();
                while(!gi.isGameOver()){
                    Thread.sleep(1000);
                    updateMessage("Waiting for your turn");

                    gi.wait(myPlayerID);

                    gameBoards = gi.getBoards();
                    Coordinate lastShot = gi.getLastShot();

                    if (IS_DEBUG_MODE)
                    {
                        System.out.println(lastShot);
                    }

                    if(lastShot != null){
                        Board board = gameBoards.get(myPlayerID);
                        String sunk = checkIfSunk(lastShot, board);
                        if(sunk.contains("sunk")){
                            updateMessage(sunk);
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateHitsOnPlayerBoard(lastShot);


                            }
                        });
                        if(sunk.contains("sunk")){
                            Thread.sleep(5000);
                        }

                    }
                    else{
                        if (IS_DEBUG_MODE)
                        {
                            System.out.println("No Shots Fired, this is the first move");
                        }
                    }

                    if(gi.isGameOver()){
                        updateMessage("You Lost!");
                        stopTimer = true;
                        break;
                    }
                    
                    if (IS_DEBUG_MODE)
                    {
                         System.out.println("Your Turn!");
                    }
                    
                    moved = false;
                    updateMessage("Its your turn!");


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //statusLabel.setText("Its your turn!");
                            enableOponentGrid();
                        }
                    });
                    while(shot.equals("None")){
                        Thread.sleep(10);
                    }


                    moved = true;

                    //System.out.println(gameBoards.get((myPlayerID + 1) % GameTracker.MAX_PLAYERS).toString(true));
                    if (IS_DEBUG_MODE)
                    {
                         System.out.println(shot);
                    }
                    
                    Coordinate move = new Coordinate(shot);
                    gi.sendMove(move);
                    Ship ship = gameBoards.get((myPlayerID + 1) % GameTracker.MAX_PLAYERS).makeMove(move);
                    if(ship == null) {
                        updateMessage("Miss");
                        Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //statusLabel.setText("Its your turn!");
                            updateShotBoard(move, "Miss");
                        }
                    });
                    } 
                    else if (ship.isSunk()) {
                        updateMessage("You sunk "+ ship.getName());
                        sendMessageToOtherPlayer("Your " + ship.getName()+ " was sunk" );
                        Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //statusLabel.setText("Its your turn!");
                            updateShotBoard(move, "Hit");
                        }
                        });
                        Thread.sleep(5000);
                    } 
                    else {
                            updateMessage("Hit");
                            Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                            //statusLabel.setText("Its your turn!");
                                updateShotBoard(move, "Hit");
                            }
                            });
                        }
                    gi.setBoards(gameBoards);
                    shot = "None";
                    
                    if (IS_DEBUG_MODE)
                    {
                         System.out.println("Loop Done");
                    }
                    
                    if(gi.isGameOver()){
                        updateMessage("You Won!");
                        stopTimer = true;
                    }

                    }
                    //updateMessage("Game Finished");
                    Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //statusLabel.setText("Its your turn!");
                        disableOponentGrid();
                    }
                    });


                return 0;
            }
        };
        //Create the new thread and start it.
        statusLabel.textProperty().bind(task.messageProperty());
        Thread gameloop = new Thread(task);
        gameloop.setDaemon(true);
        gameloop.start();


        timerLabel.setVisible(true);


        Task<Void> timeout = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int count = 120;
                while(!donePlacingShips){
                    if(stopTimer){
                        break;
                    }
                    Thread.sleep(1000);
                    count--;
                    updateMessage("Time to Finish Placing: " + count);
                    //System.out.println(count);
                    if(count == 0){
                        sendMessageToOtherPlayer("Opponent Lost Game due to timeout");
                    }

                }
                updateMessage("Finished Placing Ships");
                count = 30;
                while(true){
                    if(stopTimer){
                        updateMessage("Game Over");
                        break;
                    }
                    while(!moved){
                        Thread.sleep(1000);
                        count--;
                        updateMessage("Time to Fire: " + count);
                        while(count == 0){
                            sendMessageToOtherPlayer("Opponent Lost Game due to timeout");

                        }
                    }
                    //System.out.println("Waiting for other player Timer Thread");
                    count = 30;
                    updateMessage("Waiting for other player");

                    if(1!=1){
                        if (IS_DEBUG_MODE)
                        {
                             System.out.println("Breaking (shouldn't happen)");
                        }
                        
                        break;
                    }
                }
                

                return null;
            }
        };
        timerLabel.textProperty().bind(timeout.messageProperty());
        Thread timer = new Thread(timeout);
        timer.setDaemon(true);
        timer.start();


    }
    public void updatePlaceBoard(String p, int length, String abbr){
        //Method that disables buttons that need to be disabled, and updates button labels

        //Add ship abbreviation to list
        shipAbbr.add(abbr);
        if (IS_DEBUG_MODE)
        {
             System.out.println(startPlacement);
        }
        
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
                    if (IS_DEBUG_MODE)
                    {
                         System.out.println("Same Letters");
                    }
                    
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
                
                if (IS_DEBUG_MODE)
                {
                     System.out.println("Removing Intersections");
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
                if (IS_DEBUG_MODE)
                {
                     System.out.println("Checking Intersections");
                }
                
                if(path.size() != 0){
                    options.remove(option);
                }


            }
            
            if (IS_DEBUG_MODE)
            {
                 System.out.println("Disabling Buttons");
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
            num1 = Integer.parseInt(firstPlace.substring(1,firstPlace.length()));

            String newS = letter1 + "" + num1;
            greyedOut.add(newS);
            newS = letter2 + "" + num2;
            greyedOut.add(newS);
            
            if (IS_DEBUG_MODE)
            {
                 System.out.println("Building Path");
            }
            
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
            if (IS_DEBUG_MODE)
            {
                 System.out.println("Enabling Buttons");
            }
            
            ObservableList<Node> test = playerGrid.getChildren();
            int counter = 0;
            for(Node t:test){

                Button b = (Button)t;

                if(greyedOut.contains(b.getText()) || shipAbbr.contains(b.getText())){
                    placedCoords.add(b.getText());
                    if(greyedOut.contains(b.getText())){
                        b.setText(b.getText() + abbr);
                        b.setStyle("-fx-background-color: #696969;");
                        shipAbbr.add(b.getText());
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
    public void updateShotBoard(Coordinate s, String result){
        String c = s.toString();
        if (IS_DEBUG_MODE)
        {
             System.out.println(c.length());
        }
        
        String num = c.substring(2,c.length());
        if (IS_DEBUG_MODE)
        {
             System.out.println(num);
        }
        
        String letter = c.substring(0,1);
        String newCoord = letter+num;
        if (IS_DEBUG_MODE)
        {
             System.out.println(newCoord);
        }
        
        ObservableList<Node> test = oponentGrid.getChildren();
        int counter = 0;
        for(Node t:test){

            Button b = (Button)t;
            if(b.getText().equals(newCoord)){
                b.setDisable(true);
                b.setText(result);
                
                if (b.getText().equals("Hit"))
                {
                     b.setStyle("-fx-base: #FF0000;");
                }
                else if (b.getText().equals("Miss"))
                {
                     b.setStyle("-fx-base: #FFFFFF;");
                }
            }
            else{
                b.setDisable(true);
            }
            counter++;
            if (counter == 100)
                break;
        }
    }

    
    public void printBoardInfo(Board b){
        List<Ship> ships = b.getShipList();
        for(Ship ship: ships){
            System.out.println(ship.getName() + ": " + ship.getCoordinates());
        }
    }
    
    public void updateHitsOnPlayerBoard(Coordinate c){
        String text = c.toString();
        String shot = text.substring(0,1) + text.substring(2,text.length());
        ObservableList<Node> test = playerGrid.getChildren();
        int counter = 0;
        for(Node t:test) {
            Button b = (Button)t;
            if(b.getText().equals(shot)){
                b.setText("Miss");
                b.setStyle("-fx-base: #FFFFFF;");
            }
            else if(b.getText().contains(shot)){
                if(b.getText().contains("10") && !shot.contains("10")){
                    if (IS_DEBUG_MODE)
                    {
                        System.out.println("Coord 10 vs Coord 1 Bug");
                    }
                }
                else{
                    b.setText("Hit");
                    b.setStyle("-fx-base: #FF0000;");
                }

            }
            counter++;
            if(counter == 100)
                break;
        }
    }

    public String checkIfSunk(Coordinate c, Board b){
        List<Ship> ships = b.getShipList();
        for(Ship ship: ships){
            for(Coordinate coord: ship.getCoordinates()){
                if(c.toString().equals(coord.toString())){
                    //Found the ship
                    if(ship.isSunk()){
                        return "Your " + ship.getName() + " was sunk";
                    }
                    else{
                        return " ";
                    }
                }
            }
        }
        return " ";
    }

    @FXML
    void quitGame(ActionEvent event) {
        if(!stopTimer){
            surrender(event);
        }
        System.exit(0);
    }

    @FXML
    void surrender(ActionEvent event) {
        stopTimer = true;
        sendMessageToOtherPlayer("Opponent Surrendered");

    }
    public void sendMessageToOtherPlayer(String message){
        gi.sendMessageToOtherPlayer(message, myPlayerID);
    }
}
