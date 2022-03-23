package main.java;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TCPClient {
    public static void main(String[] args) throws IOException {
		//Variables for setting up connection and communication
		Socket socket = null; //socket to connect with ServerRouter
		PrintWriter out = null; //for writing to ServerRouter
		BufferedReader in = null; //for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); //Client machine's IP

		//Load IP addresses from config.yml file
		Yaml yaml = new Yaml();	//Create a new YAML instance
		Map<String, Object> config = yaml.load(new FileReader("config-client.yml")); //load config as yaml
		String routerName = (String)config.get("router-ip"); //ServerRouter host name
		int sockNum = (int)config.get("router-port"); //router port number
		int serverPort = (int)config.get("server-port"); //server port number
		String name = (String)config.get("name"); //my name
		String destination = (String)config.get("destination"); //destination name

		//Tries to connect to the ServerRouter
		try {
			socket = new Socket(routerName, sockNum);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		//Variables for message passing
		String fromUser; //messages sent to ServerRouter

		//Communication process (initial sends/receives)
		out.println(destination); //clients send the name of the destination server
		String fromServer = in.readLine(); //initial receive from router (verification of connection)
		System.out.println("ServerRouter: " + fromServer);
		out.println(name); //initial send (name of the client)
		out.println(host); //clients send the IP of its machine
		String destinationIP = in.readLine();
		System.out.println("Destination IP: "+destinationIP);
		socket.close();
		out.close();
		in.close();

		//Connect direct to server
		try {
			socket = new Socket(destinationIP, serverPort);
			System.out.println("Connected to "+destinationIP+":"+serverPort+"!");
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about server: " + destinationIP);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + destinationIP);
			System.exit(1);
		}

		//Loads file.txt
		BufferedReader fromFile = new BufferedReader(new FileReader("file.txt")); //reader for the string file
		String message = "";
		String line;
		while((line = fromFile.readLine()) != null) {
			message += line;
		}
		//message = message.substring(0, message.length()-2);


		/*while((fromServer = in.readLine()) != null) {
			//Receives responses from server
			System.out.println("Server: " + fromServer);
			t1 = System.nanoTime();
			if(fromServer.equals("Bye.")) break; //exit statement
			t = (t1 - t0)/1000000.0; //calculates cycle time between messages


			//Send messages from files to server
			//fromUser = fromFile.readLine(); //reading strings from a file
			fromUser = message;
			int size = 0;
			if(fromUser != null) {
				size = fromUser.getBytes(StandardCharsets.UTF_8).length;
				System.out.println("Client: "+fromUser);
				out.println(fromUser); //sending the strings to the Server via ServerRouter
				System.out.println("Client: Bye.");
				out.println("Bye.");
				t0 = System.nanoTime();
			}
			System.out.println("Message size: "+size+" bytes");
			System.out.println("Cycle Time: "+t+" ms");
		}*/

		long t0 = System.nanoTime();
		int size = message.getBytes(StandardCharsets.UTF_8).length;
		System.out.println("Client: "+message);
		out.println(message); //sending the strings to the Server via ServerRouter

		//Receives responses from server
		fromServer = in.readLine();
		System.out.println("Server: " + fromServer);
		long t1 = System.nanoTime();
		double t = (t1 - t0)/1000000.0; //calculates cycle time between messages
		System.out.println("Message size: "+size+" bytes");
		System.out.println("Cycle Time: "+t+" ms");

		//Closing connections
		out.close();
		in.close();
		socket.close();
    }
}
