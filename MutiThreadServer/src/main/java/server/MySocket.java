package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import server.HttpServer.HttpMethod;

public class MySocket implements Runnable {

	private Socket socket;
	private HttpMethod requestMethod;
	private Map<String, String> headers;
	private OutputStream out;
	private SocketIOManager socketIO = new SocketIOManager();

	public MySocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// read http request
			headers = socketIO.readHttpRequestHeader(socket.getInputStream());
			System.out.println(headers);
			// Determine request type
			requestMethod = determineRequestType(headers);
			out = socket.getOutputStream();
			getRequest(headers, out); 
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getRequest(Map<String, String> headers, OutputStream out) throws IOException {
		String method = headers.get("CMD");
		String[] parts = method.split(" ");
		String path = "C:/Users/eparr/Neumont/Quarter 5/CSC280/Static Website" + parts[1];
		File file = new File(path);
		String line = "";

		if (!file.exists()) {
			File error = new File("C:/Users/eparr/Neumont/Quarter 5/CSC280/Static Website/filenotfound.html");
			socketIO.writeHttpResponseHeader(out, 404, error.length(), "na");
			FileInputStream fileIO = new FileInputStream(error);
			int i;
			while ((i = fileIO.read()) != -1) {
				out.write(i);
			}
			fileIO.close();
		} else {
			System.out.println("Writing File to Chrome");
			String[] fileExtension = parts[1].split("\\.");
			socketIO.writeHttpResponseHeader(out, 200, file.length(), fileExtension[fileExtension.length - 1]);
			FileInputStream fileIO = new FileInputStream(file);
			int i;
			while ((i = fileIO.read()) != -1) {
				out.write(i);
			}
			fileIO.close();
		}

	}

	private HttpMethod determineRequestType(Map<String, String> headers2) {
		String method = headers.get("CMD");
		String[] parts = method.split(" ");
		System.out.println(parts[0]);
		if (parts[0].equalsIgnoreCase("get")) {
			return HttpMethod.Get;
		} else if (parts[0].equalsIgnoreCase("post")) {
			return HttpMethod.Post;
		} else {
			return null;
		}
	}

}
