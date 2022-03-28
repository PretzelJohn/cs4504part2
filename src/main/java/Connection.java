package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
    //Instance variables
    private Socket socket; //socket used for communication
    private PrintWriter out; //output stream (to socket)
    private BufferedReader in; //input stream (from socket)
    private String from, to; //source and destination for messages

    //Constructor - initializes instance variables
    public Connection(String from, String to) {
        this.socket = null;
        this.out = null;
        this.in = null;
        this.from = from;
        this.to = to;
    }

    //Accepts an incoming connection request
    public void accept(ServerSocket serverSocket) {
        try {
            System.out.printf("%s is listening on port %d...\n", from, serverSocket.getLocalPort());
            socket = serverSocket.accept(); //accept connection
            out = new PrintWriter(socket.getOutputStream(), true); //establish output stream (to socket)
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //establish input stream (from socket)
            System.out.printf("Accepted connection from %s @ %s:%d!\n\n", to, socket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());
        } catch (IOException e) {
            System.err.printf("Could not listen on port: %d.\n", serverSocket.getLocalPort());
            System.exit(1);
        }
    }

    //Sends a connection request
    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port); //create socket
            out = new PrintWriter(socket.getOutputStream(), true); //establishes output stream (to socket)
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //establishes input stream (from socket)
            System.out.printf("Connected to %s @ %s:%d!\n", to, ip, port);
        } catch (UnknownHostException e) {
            System.err.printf("Don't know about %s!\n", to); //prints 'destination not found' error
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.printf("Couldn't get I/O for the connection to: %s\n", ip); //prints connection error
            System.exit(1);
        }
    }

    //Disconnects from IO streams and socket
    public void disconnect() {
        try {
            out.close(); //close output stream (to socket)
            in.close(); //close input stream (from socket)
            socket.close(); //close socket
            System.out.printf("Disconnected from %s!\n\n", to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sends a message to the socket
    public void send(String description, String message) {
        out.println(message); //send message
        System.out.printf("%s ---> %s (%s): %s\n", from, to, description, message); //print message
    }

    //Receives a message from the socket
    public String receive(String description) {
        try {
            String response = in.readLine(); //receive response
            System.out.printf("%s <--- %s (%s): %s\n", from, to, description, response); //print response
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    //Setters
    public void setTo(String to) { this.to = to; }

    //Getters
    public String getIP() { return this.socket.getInetAddress().getHostAddress(); }
    public int getPort() { return this.socket.getPort(); }
}
