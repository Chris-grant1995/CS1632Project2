package edu.pitt.battleshipgame.common.ships;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.board.*;

public abstract class ShipFactory {
    public static Ship newShipFromType(Ship.ShipType type, Coordinate start, Coordinate end, Board board) {
        switch (type) {
            case BATTLESHIP:
                return new Battleship(start, end, board);
            case CARRIER:
                return new Carrier(start, end, board);
            case CRUISER:
                return new Cruiser(start, end, board);
            case SUBMARINE:
                return new Submarine(start, end, board);
            case DESTROYER:
                return new Destroyer(start, end, board);
            default:
                throw new IllegalArgumentException(type + " does not identify a valid ShipType.");
        }
    }

    public static int maxAllowedFromType(Ship.ShipType type) {
        switch (type) {
            case BATTLESHIP:
                return Battleship.MAX_ALLOWED;
            case CARRIER:
                return Carrier.MAX_ALLOWED;
            case CRUISER:
                return Cruiser.MAX_ALLOWED;
            case SUBMARINE:
                return Submarine.MAX_ALLOWED;
            case DESTROYER:
                return Destroyer.MAX_ALLOWED;
            default:
                throw new IllegalArgumentException(type + " does not identify a valid ShipType.");
        }
    }
    
    public static String getNameFromType(Ship.ShipType type) {
        switch (type) {
            case BATTLESHIP:
                return Battleship.NAME;
            case CARRIER:
                return Carrier.NAME;
            case CRUISER:
                return Cruiser.NAME;
            case SUBMARINE:
                return Submarine.NAME;
            case DESTROYER:
                return Destroyer.NAME;
            default:
                throw new IllegalArgumentException(type + " does not identify a valid ShipType.");
        }
    }
    public static String getAbbreviationFromType(Ship.ShipType type) {
        switch (type) {
            case BATTLESHIP:
                return "B";
            case CARRIER:
                return "CA";
            case CRUISER:
                return "CR";
            case SUBMARINE:
                return "S";
            case DESTROYER:
                return "D";
            default:
                throw new IllegalArgumentException(type + " does not identify a valid ShipType.");
        }
    }
    public static int getLengthFromType(Ship.ShipType type) {
        switch (type) {
            case BATTLESHIP:
                return Battleship.LENGTH;
            case CARRIER:
                return Carrier.LENGTH;
            case CRUISER:
                return Cruiser.LENGTH;
            case SUBMARINE:
                return Submarine.LENGTH;
            case DESTROYER:
                return Destroyer.LENGTH;
            default:
                throw new IllegalArgumentException(type + " does not identify a valid ShipType.");
        }
    }
}