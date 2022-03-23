package main.java;

import org.yaml.snakeyaml.Yaml;

import java.net.*;
import java.io.*;
import java.util.Map;

public class TCPServerRouter {
	public static void main(String[] args) throws IOException {
		Socket clientSocket = null; //socket for the thread
		String[][] routingTable = new String[10][2]; //routing table

		//Load IP addresses from config.yml file
		Yaml yaml = new Yaml();	//Create a new YAML instance
		Map<String, Object> config = yaml.load(new FileReader("config-router"+args[0]+".yml")); //Load config as yaml
		String name = (String)config.get("router-ip");
		int sockNum = (int)config.get("router-port"); //port number
		int sockNum2 = (int)config.get("router2-port"); //other router's port number
		int ind = 0; //index in the routing table
		boolean running = true; //status

		//Accepting connections
		ServerSocket serverSocket = null; //server socket for accepting connections
		try {
			serverSocket = new ServerSocket(sockNum);
			System.out.println("ServerRouter is Listening on port: "+sockNum+".");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not listen on port: "+sockNum+".");
			System.exit(1);
		}

		//Router connection
		Socket routerSocket = null;
		if(args[0].equals("1")) {
			//Listen for connections from other router
			try {
				routerSocket = serverSocket.accept();
				System.out.println("Accepted router: "+routerSocket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else if(args[0].equals("2")) {
			//Send connection to other router
			try {
				routerSocket = new Socket(name, sockNum2);
				System.out.println("Connected to router: "+routerSocket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			System.err.println("Invalid router id in args!");
			System.exit(1);
		}


		//Creating threads with accepted connections
		while(running) {
			try {
				clientSocket = serverSocket.accept();
				SThread t = new SThread(routingTable, clientSocket, routerSocket, ind, args[0]); //creates a thread with a random port
				t.start(); //starts the thread
				ind++; //increments the index
				System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				System.err.println("Client/Server failed to connect.");
				System.exit(1);
			}
		}

		//closing connections
		clientSocket.close();
		serverSocket.close();
    }
}