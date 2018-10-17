package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	private ServerSocket serverSocket;

	public HttpServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {

		try {
//			while (true) {
				Socket socket = serverSocket.accept();
//                new MySocket(socket).run();
                new Thread(new MySocket(socket)).start();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
