package main.java;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TCPPeer {
    public static void main(String[] args) throws IOException {
		//Variables for setting up connection and communication
		Config config = new Config("config-peer"+args[0]+".yml");

		//Fetch IP address of destination from router
		Connection toRouter = new Connection(config.getName(), "ServerRouter"); //router connection
		toRouter.connect(config.getRouterIP(), config.getRouterPort()); //connect to router
		toRouter.send("greeting", config.getName()); //1a. send my name to router
		toRouter.receive("greeting"); //1b. receive greeting from router
		toRouter.send("destination", config.getDestination()); //2a. send destination name to router
		String destinationIP = toRouter.receive("destination IP"); //2b. receive destination IP from router
		toRouter.disconnect(); //disconnect from router

		//Connect directly to destination
		Connection toPeer = new Connection(config.getName(), config.getDestination()); //peer connection
		if(args[0].equals("1")) {
			//RECEIVER MODE
			toPeer.accept(new ServerSocket(config.getPort())); //accept connection from peer
			String message = toPeer.receive("message"); //5b. receive message from peer
			toPeer.send("response", message.toUpperCase()); //6a. send response back to peer
		} else {
			//SENDER MODE
			//Load message from file.txt
			BufferedReader fromFile = new BufferedReader(new FileReader(args[1]));
			String message = ""; //the message
			String line;
			while((line = fromFile.readLine()) != null) {
				message += line + " ";
			}

			toPeer.connect(destinationIP, config.getPort()); //connect to peer

			//Send message to peer and wait for response
			int size = message.getBytes(StandardCharsets.UTF_8).length; //message size
			long t0 = System.nanoTime(); //start cycle timer
			toPeer.send("message", message); //5a. send message to peer
			toPeer.receive("response"); //6b. read response from peer
			long t1 = System.nanoTime(); //end cycle timer

			//Print statistics
			System.out.printf("Message size: %d bytes\n", size); //print message size
			System.out.printf("Cycle time: %.2f ms\n", (t1 - t0)/1000000.0); //print round-trip time
		}

		//Close connection
		toPeer.disconnect(); //disconnect from peer
    }
}
