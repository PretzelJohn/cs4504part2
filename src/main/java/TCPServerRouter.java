package main.java;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class TCPServerRouter {
	public static void main(String[] args) throws IOException {
		HashMap<String, String> routingTable = new HashMap<>(); //routing table
		Config config = new Config("config-router"+args[0]+".yml"); //config
		boolean running = true; //running state

		//Connect to other router
		String routerName = "ServerRouter"+args[0]; //name of this router
		String otherRouterName = "ServerRouter"+(args[0].equals("1") ? 2 : 1); //name of the other router
		Connection toRouter = new Connection(routerName, otherRouterName); //connection to other router
		ServerSocket serverSocket = new ServerSocket(config.getPort()); //server socket to listen for requests
		if(args[0].equals("1")) {
			toRouter.accept(serverSocket); //accept connection from router 2
		} else {
			toRouter.connect(config.getRouterIP(), config.getRouterPort()); //connect to router 1
		}

		//Listen for incoming peer connection requests
		int ind = 0; //index of peer
		while(running) {
			Connection toPeer = new Connection(routerName, "Peer"); //connection to peer
			toPeer.accept(serverSocket); //accepts a connection to a peer
			SThread t = new SThread(routingTable, toPeer, toRouter, ind, args[0]); //creates a handler thread
			t.start(); //starts the thread
			ind++; //increments the index
		}

		//closing connection
		toRouter.disconnect();
    }
}