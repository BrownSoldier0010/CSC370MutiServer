package server;

import java.io.IOException;

public class Main {
	
	public static final int REQUEST_COUNT = 10; 

	public static void main(String[] args) throws IOException {
		HttpServer server = new HttpServer(1234);
		server.run();

	}
}
