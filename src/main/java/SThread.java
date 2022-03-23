package main.java;

import java.io.*;
import java.net.*;

public class SThread extends Thread {
	private String[][] rTable; //routing table
	private PrintWriter out, outRouter; //writers (for writing back to the machine and to destination)
	private BufferedReader in, inRouter; //reader (for reading from the machine connected to)
	private String destination; //communication strings
	private int ind; //index in the routing table
	private String arg;

	//Constructor
	SThread(String[][] table, Socket toClient, Socket toRouter, int index, String arg) throws IOException {
		out = new PrintWriter(toClient.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));

		destination = in.readLine(); //initial read (the destination for writing)
		System.out.println("destination = in.readLine(): "+destination);
		out.println("Hello!");

		rTable = table;
		rTable[index][0] = in.readLine(); //IP addresses
		System.out.println("rTable[index][0] = in.readLine(): "+rTable[index][0]);

		rTable[index][1] = in.readLine(); //sockets for communication
		System.out.println("rTable[index][1] = in.readLine(): "+rTable[index][1]);
		ind = index;

		this.outRouter = new PrintWriter(toRouter.getOutputStream(), true);
		this.inRouter = new BufferedReader(new InputStreamReader(toRouter.getInputStream()));
		this.arg = arg;
	}
	
	//Run method (will run for each machine that connects to the ServerRouter)
	public void run() {
		try {
			System.out.println("Finding IP address of " + destination);
			//out.println("Connected to the router."); //confirmation of connection

			//Waits 10 seconds to let the routing table fill with all machines' information
			try {
				sleep(10000);
			} catch(InterruptedException ie) {
				System.out.println("Thread interrupted");
			}

			outRouter.println(destination);
			String dest = inRouter.readLine();

			//Loops through the routing table to find the destination
			long t0 = System.nanoTime();
			for(int i = 0; i < 10; i++) {
				if(dest.equals(rTable[i][0])) {
					String result = rTable[i][1]; //gets the ip address for communication from the table
					System.out.println("Found destination: " + result);
					outRouter.println(result);
				}
			}
			long t = System.nanoTime() - t0;
			System.out.println("Routing lookup took "+(t/1000000.0)+" ms");

			//Send destination IP to client/server
			String result = inRouter.readLine();
			out.println(result);
		} catch (IOException e) {
            System.err.println("Could not listen to socket.");
            System.exit(1);
        }
	}
}