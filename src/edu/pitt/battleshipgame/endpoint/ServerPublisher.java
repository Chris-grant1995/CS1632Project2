package edu.pitt.battleshipgame.endpoint;

import javax.xml.ws.Endpoint;

import edu.pitt.battleshipgame.server.ServerWrapper;

import java.net.Inet4Address;
import java.net.UnknownHostException;

//Endpoint publisher
public class ServerPublisher {
    public static void main(String [] args) throws UnknownHostException {
        System.out.println("Constructing Server Please Wait.");

        String addr = "http://" + Inet4Address.getLocalHost().getHostAddress() + ":9999/battleship";
        //System.out.println("IP:" + Inet4Address.getLocalHost().getHostAddress());
        Endpoint.publish(addr, new ServerWrapper());

    }
}