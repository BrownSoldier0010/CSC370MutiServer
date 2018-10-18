package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class HttpServer {
	public static final int REQUEST_LIMIT = 10;
	private ServerSocket serverSocket;

	public HttpServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		ExecutorService service = Executors.newCachedThreadPool();

		try {
				Socket socket = serverSocket.accept();
// new MySocket(socket).run();
				service.submit(new MySocket(socket));
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
