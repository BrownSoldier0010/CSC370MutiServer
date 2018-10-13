package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class HttpServer {

	public enum HttpMethod {
		Get, Post
	}

	private ServerSocket serverSocket;
	private SocketIOManager socketIO = new SocketIOManager();

	public HttpServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() throws IOException {

		Socket socket;

		while (true) {
			socket = serverSocket.accept();

			// read http request
			Map<String, String> headers = socketIO.readHttpRequestHeader(socket.getInputStream());
			// Determine request type
			HttpMethod requestMethod = determineRequestType(headers);
			OutputStream out = socket.getOutputStream();
			// take action - load file etc.
			switch (requestMethod) {
			case Get:
				getRequest(headers, socket.getOutputStream());
				break;
			case Post:
				
				byte[] requestBody;
				requestBody = socketIO.readHttpRequestBody(socket.getInputStream());
				postRequest(headers, requestBody, out);
				break;

			default:
				break;
			}
			socket.close();
		}
	}
	
	private void postRequest(Map<String, String> headers, byte[] body, OutputStream out) {
		String method = headers.get("CMD");
		String[] parts = method.split(" ");
		String path = "C:/Users/eparr/Neumont/Quarter 5/CSC280/Static Website" + parts[1];
		File file = new File(path); 
		FileOutputStream writer;
		try {
			writer = new FileOutputStream(file);
			writer.write(body);
			writer.close();
			socketIO.writeHttpResponseHeader(out, 200, file.length(), "na");
		} catch (IOException e) {
			socketIO.writeHttpResponseHeader(out, 500, file.length(), "na");
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

	private HttpMethod determineRequestType(Map<String, String> headers) {
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

	// example http response (if all is good)

	// HTTP/1.0 200 OK
	// Content-type: (extension of file) (text/html)
	// Content-length: 237 (how many bytes)

	// <html> ...

	// example http response (if things are bad) (FILE NOT FOUND

	// HTTP 404 Not Found
	// Content-type: text/html
	// Content-length: number of bytes in your error page

	// <html> <body> File Not Found ...

	// if another exception is thrown -
	// HTTP 500 Internal Server Error
	// Content-type: text/html
	// Content-length: number of bytes in your error page

	// <html> <body> Internal Server Error, send back exception message ...
}
