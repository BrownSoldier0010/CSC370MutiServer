package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

	private ServerSocket serverSocket;
	public static Map<String, Integer> countOfVisitedClient = new HashMap<>();
	public static int requestCount; 

	public HttpServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {

		try {
			while (true) {
				Socket socket = serverSocket.accept();
				// new MySocket(socket).run();
				new Thread(new MySocket(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
