package nl.sogyo.jesper.webserver;

import sun.jdbc.odbc.ee.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jvdberg on 06/06/2014.
 */
public class Main {
    public static void main(String[] args) {
        ServerSocket socket;
        try {
            socket = new ServerSocket(9090);
            while (true) {
                Socket newConnection = socket.accept();
                Thread thread = new Thread(new nl.sogyo.jesper.webserver.ConnectionHandler(newConnection));
                thread.start();
            }
        }catch (IOException e) {
                e.printStackTrace();
        }
    }
}

