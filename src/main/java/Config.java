package main.java;

import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Config {
    //Instance variables
    private String routerIP, name, destination; //router's IP, source name, destination name
    private int routerPort, port; //(other) router's port, my port

    //Constructor - initializes instance variables
    public Config(String fileName) {
        Yaml yaml = new Yaml();	//Create a new YAML instance
        try {
            Map<String, Object> config = yaml.load(new FileReader(fileName)); //load config as yaml
            this.name = (String)config.get("name"); //source name
            this.destination = (String)config.get("destination"); //destination name
            this.routerIP = (String)config.get("router-ip"); //router ip address
            this.routerPort = (int)config.get("router-port"); //router port number
            this.port = (int)config.get("port"); //my port number
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Getters
    public String getRouterIP() { return this.routerIP; }
    public String getName() { return this.name; }
    public String getDestination() { return this.destination; }
    public int getRouterPort() { return this.routerPort; }
    public int getPort() { return this.port; }
}
