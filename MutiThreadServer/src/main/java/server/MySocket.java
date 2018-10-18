package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class MySocket implements Runnable {

    private Socket socket;
    private HttpMethod requestMethod;
    private Map<String, String> headers;
    private OutputStream out;
    private SocketIOManager socketIO = new SocketIOManager();
    Semaphore semaphore = new Semaphore(HttpServer.REQUEST_LIMIT);

    public MySocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // read http request
            semaphore.acquire();
            headers = socketIO.readHttpRequestHeader(socket.getInputStream());
            System.out.println(headers);
            // Determine request type
            requestMethod = determineRequestType(headers);
            String userClient = socketIO.getUserClient(headers.get("User-Agent"));
            addClientCount(userClient);
            getRequest(headers, socket.getOutputStream());
            Main.countOfVisitedClient.forEach((k, v) -> System.out.println(k + " - " + v));
            System.out.println();

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
    }

    private synchronized void getRequest(Map<String, String> headers, OutputStream out) throws IOException {
        String method = headers.get("CMD");
        System.out.println(method);
        String[] parts = method.split(" ");
		String path = "C:/Static Website" + parts[1];
        //String path = "/Users/Tony/Documents/Code/Java/workspace/CSC280/Static Website" + parts[1];
        File file = new File(path);
        String line = "";

        if (!file.exists() || parts[1].equals("/")) {
			File error = new File("C:/Static Website/filenotfound.html");
            //File error = new File("/Users/Tony/Documents/Code/Java/workspace/CSC280/Static Website/filenotfound.html");
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

    private synchronized void addClientCount(String client) {
        if (Main.countOfVisitedClient.containsKey(client)) {
            int count = Main.countOfVisitedClient.get(client);
            Main.countOfVisitedClient.put(client, ++count);
        } else {
            Main.countOfVisitedClient.put(client, 1);
        }
    }

    private synchronized HttpMethod determineRequestType(Map<String, String> headers2) {
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

enum HttpMethod {
    Get, Post
}
