package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Map<String, Integer> countOfVisitedClient = new HashMap<>();

	public static void main(String[] args) throws IOException {
		HttpServer server = new HttpServer(1234);
		server.run();
		
		countOfVisitedClient.entrySet().stream().forEach(e -> 
			System.out.println(e.getKey() + " - " + e.getValue())); 
	}
}
