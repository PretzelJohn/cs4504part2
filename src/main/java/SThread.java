package main.java;

import java.util.HashMap;

public class SThread extends Thread {
	private final HashMap<String, String> rTable; //routing table
	private final Connection toPeer, toRouter; //connections
	private final String prefix; //thread identifier
	private final String mode; //mode of router (sender/receiver)
	private final String destination; //communication strings

	//Constructor
	SThread(HashMap<String, String> rTable, Connection toPeer, Connection toRouter, int index, String mode) {
		this.rTable = rTable;
		this.toPeer = toPeer;
		this.toRouter = toRouter;
		this.prefix = "#"+index+". ";
		this.mode = mode;

		//1. Handshake with peer
		System.out.print(prefix); //print index
		String name = toPeer.receive("greeting"); //1a. receive name from peer
		System.out.print(prefix);
		this.rTable.put(name, toPeer.getIP()); //store the peer in the routing table
		this.toPeer.setTo(name); //store the peer's name for printing
		this.toPeer.send("greeting", String.format("Hello, %s @ %s:%d!", name, toPeer.getIP(), toPeer.getPort())); //1b. send greeting to peer

		//2. Receive the name of the destination
		System.out.print(prefix);
		this.destination = toPeer.receive("destination"); //2a. receive destination from peer
	}
	
	//Run method (will run for each machine that connects to the ServerRouter)
	public void run() {
		//Waits 10 seconds to let the routing table fill with all machines' information
		try {
			System.out.printf("%sWaiting 10 seconds...\n", prefix);
			sleep(10000);
		} catch(InterruptedException ie) {
			System.out.printf("%sThread interrupted\n", prefix);
		}

		//Send name of destination to other router
		String otherRouterName = "ServerRouter"+(mode.equals("1") ? 2 : 1);
		System.out.printf("%sAsking %s for IP address of %s...\n", prefix, otherRouterName, destination);
		System.out.print(prefix);
		toRouter.send("destination", destination); //3a. send destination name to router
		System.out.print(prefix);
		String destinationName = toRouter.receive("destination"); //3b. receive destination name from router
		System.out.printf("%sFinding IP address of %s for %s...\n", prefix, destinationName, otherRouterName);

		//Search routing table for destination IP
		long t0 = System.nanoTime(); //start timer
		String ip = rTable.get(destinationName);
		long t = System.nanoTime() - t0; //end timer
		System.out.printf("%sRouting lookup took %d ns\n", prefix, t); //print routing time

		//Send destination IP address to other router
		System.out.print(prefix);
		toRouter.send("IP of "+destinationName, ip); //4a. send destination IP to router
		System.out.print(prefix);
		String destinationIP = toRouter.receive("IP of "+destination); //4b. receive destination IP from router

		//Send destination IP address to peer
		System.out.print(prefix); //print index
		toPeer.send("IP of "+destination, destinationIP); //2b. send destination IP to peer
		System.out.println();
	}
}