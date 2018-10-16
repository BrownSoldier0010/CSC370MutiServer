package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {

	public enum HttpMethod {
		Get, Post
	}

	private ServerSocket serverSocket;

	public HttpServer(int port) throws IOException {
		serverSocket = new ServerSocket(port, Integer.MAX_VALUE);
	}

	public void run() {

		try {
			while (true) {
				Socket socket = serverSocket.accept(); 
				MySocket socket2 = new MySocket(socket);
				socket2.run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
