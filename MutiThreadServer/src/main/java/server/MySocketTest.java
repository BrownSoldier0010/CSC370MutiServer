package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.junit.Test;

public class MySocketTest {

	private static final int requestCount = 10;
	private static final int requestSendTime = 1000;
	private static final String chrome = "Chrome"; 
	private static final String firefox = "Firefox"; 
	private static final String safari = "Safari"; 
	private Random rand; 

	@Test
	public void test() {
		
		rand = new Random(); 

		while (true) {
			
			// set loop

			try {

				// three random browser strings
				// generate rand between 1-3
				// based on rand - set random user agent with params

				URL url = new URL("localhost:1234/index.html");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");

				//

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
