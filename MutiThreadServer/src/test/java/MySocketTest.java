import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.junit.Test;

import server.HttpServer;
import server.SocketIOManager;

public class MySocketTest {

	private SocketIOManager socketIOManager;
	private HttpServer server;
	private Random rand;
	private int countOfRequests;
	private String[] userAgents = {"Chrome", "Internet Explorer", "Mozilla"}; 

	@Test
	public void RandomRequestSimulation() {
		rand = new Random(); 
		try {
			server = new HttpServer(1234);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(() -> server.run()).start();

		for (int i = 0; i < 20; i++) {
			int k = i;
			try {
				HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:1234/index.html")
						.openConnection();
				con.setDoInput(true);
				con.setRequestMethod("GET");
				System.out.println("Response Code " + con.getResponseCode());
//				 con.setRequestProperty("User-Agent", userAgents[rand.nextInt(3)]);
				con.connect();
				// con.disconnect();

			} catch (IOException e) {
			}
		}
		
		 System.out.println("Requests sent " + HttpServer.requestCount);
		

	}

}
