package edu.pitt.battleshipgame.common.board;

import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

import edu.pitt.battleshipgame.common.ships.Ship;

public class Board implements Serializable {
    // flip this to turn debug print statements on
    public static final boolean IS_DEBUG_MODE = false;
    
    public static final int BOARD_DIM = 10;
    // We could track a Ship-Bool pair but it is just as easy to have
    // two arrays. The Ships array will keep track of the ships on the
    // game board. The moves array will be initialized to false and will
    // change to true when that move is made.
    //private ArrayList < ArrayList < Ship > > theShips;
    private Ship [][] theShips;
    private boolean [][] moves;
    // Keep a list of all ships on this board for quick searching.
    LinkedList<Ship> shipList;
    private String name;

    public Board (String _name) {
        theShips = new Ship[BOARD_DIM][BOARD_DIM];
        moves = new boolean[BOARD_DIM][BOARD_DIM];
        shipList = new LinkedList<Ship>();
        name = _name;
    }
    
    public String getName() {
        return name;
    }
    
    public void addShip(Ship ship) {
        if (!canShipFit(ship)) {
            throw new IllegalArgumentException("This board already has the maximum amount of: " + ship.getName());
        }
        for (Coordinate coord : ship.getCoordinates()){
            theShips[coord.getRow()][coord.getCol()] = ship;
        }
        shipList.add(ship);
    }
    
    public Ship makeMove(Coordinate move) {
        /*moves[move.getCol()][move.getRow()] = true;
        Ship ship = theShips[move.getCol()][move.getRow()];
        if(ship != null) {
            ship.registerHit();
        }
        return ship;
        */

        for(Ship s:shipList){
            List<Coordinate> coords = s.getCoordinates();
            for(Coordinate coord: coords){
                if(coord.toString().equals(move.toString())){
                    s.registerHit();
                    return s;
                }
            }
        }
        return null;
    }
    
    public boolean canShipFit(Ship ship) {
        int shipCount = 0;
        for (Ship s : shipList) {
            if (s.getType() == ship.getType()) {
                shipCount++;
            }
        }
        if (shipCount >= ship.maxAllowed()) {
            return false;
        } else {
            return true;
        }
    }
    
    public List<Ship> getShipList() {
        return shipList;
    }
    
    public boolean areAllShipsSunk() {
        if (IS_DEBUG_MODE)
        {
             System.out.println("ShipList Length: " + shipList.size());
        }
        
        for (Ship s : shipList) {
            if (IS_DEBUG_MODE)
            {
                 System.out.println(s.getName());
                 System.out.println(s.getCoordinates());
                 System.out.println(s.isSunk());
            }
            
            if (! s.isSunk()) {
                if (IS_DEBUG_MODE)
                {
                    System.out.println("Not All Ships are sunk");
                }
                
                return false;
            }
        }
        return true;
    }
    
    public String toString() {
        return toString(false);
    }
    
    public String toString(boolean showShips) {
        StringBuilder sb = new StringBuilder();
        // Buld an intermediate representation of the board as a character array
        char [][] boardRepresentation = new char[BOARD_DIM+1][BOARD_DIM+1];
        boardRepresentation[0][0] = '+';
        for (int row = 1; row < BOARD_DIM+1; row++) {
            // The first column will be filled with the row labels
            boardRepresentation[row][0] = Integer.toString(row).charAt(0);
        }
        for (int col = 1; col < BOARD_DIM+1; col++) {
            boardRepresentation[0][col] = Coordinate.reverseColumnLookup(col-1);
        }
        for (int row = 0; row < BOARD_DIM; row++) {
            for (int col = 0; col < BOARD_DIM; col++) {
                if (moves[row][col]) {
                    if (theShips[row][col] != null) {
                        boardRepresentation[row+1][col+1] = 'X';
                    } else {
                        boardRepresentation[row+1][col+1] = 'O';
                    }
                }
                if (showShips && theShips[row][col] != null) {
                    boardRepresentation[row+1][col+1] = 'S';
                }
            }
        }
        for (char [] row : boardRepresentation) {
            sb.append(row);
            sb.append('\n');
        }
        return sb.toString();
    }
}
