package edu.pitt.battleshipgame.endpoint;

import javax.xml.ws.Endpoint;

import edu.pitt.battleshipgame.server.ServerWrapper;
import javafx.concurrent.Task;

import java.net.Inet4Address;
import java.net.UnknownHostException;

//Endpoint publisher
public class ServerPublisher {
    // flip this to turn debug print statements on
    public static final boolean IS_DEBUG_MODE = false;
    
    public static void main(String [] args) throws UnknownHostException, InterruptedException {
        if (IS_DEBUG_MODE)
        {
             System.out.println("Constructing Server Please Wait.");
        }

        /*Task<Void> test = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Testing");
                return null;
            }
        };
        Thread t = new Thread(test);
        t.setDaemon(true);
        t.start();*/


        String ip = Inet4Address.getLocalHost().getHostAddress();
        String addr = "http://" + ip + ":9999/battleship";
        //System.out.println("IP:" + Inet4Address.getLocalHost().getHostAddress());
        Endpoint.publish(addr, new ServerWrapper());
        System.out.println(ip);

    }
}
