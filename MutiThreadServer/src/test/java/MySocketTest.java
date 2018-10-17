import org.junit.Before;
import org.junit.Test;
import server.HttpServer;
import server.Main;
import server.SocketIOManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MySocketTest {

    //	private static final int requestCount = 10;
//	private static final int requestSendTime = 1000;
    private SocketIOManager socketIOManager;
    private HttpServer server;
    private String[] agents;
    private Random rand;

    @Test
    public void RandomRequestSimulation() {
        new Thread(() -> server.run()).start();

        for (int i = 0; i < 2000; i++) {
            new Thread(() -> {
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:1234/index.html").openConnection();
                    con.setDoInput(true);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", agents[rand.nextInt(5)]);
                    con.connect();
                    con.disconnect();
                } catch (IOException e) {}
            }).start();
        }
    }

    @Test
    public void getInexistentFile() throws IOException {
        new Thread(() -> server.run()).start();

        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:1234/asdfasdf.html").openConnection();
        con.setDoInput(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", agents[rand.nextInt(5)]);
        con.connect();

        System.out.println(con.getResponseCode());

        String result = this.readContent(con.getErrorStream());
//        System.out.println(result);
        assertEquals("<p>File not found</p>\n", result);
    }

    @Test
    public void getClientCountWhenFileIsInexistent() throws IOException {
        new Thread(() -> server.run()).start();

        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:1234/asdfasdf.html").openConnection();
        con.setDoInput(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", agents[rand.nextInt(5)]);
        con.connect();

        System.out.println(con.getResponseCode());

        String result = Main.countOfVisitedClient.values().toArray()[0].toString();
        assertEquals("1", result);
    }

    @Test
    public void getUserClient_ReturnChrome() throws IOException {
        String expected = "Chrome";
        String result = socketIOManager.getUserClient(this.agents[0]);
        assertEquals(expected, result);
    }

    @Test
    public void getUserClient_ReturnOpera() throws IOException {
        String expected = "Opera";
        String result = socketIOManager.getUserClient(this.agents[1]);
        assertEquals(expected, result);
    }

    @Test
    public void getUserClient_ReturnFirefox() throws IOException {
        String expected = "Firefox";
        String result = socketIOManager.getUserClient(this.agents[2]);
        assertEquals(expected, result);
    }

    @Test
    public void getUserClient_ReturnSafari() throws IOException {
        String expected = "Safari";
        String result = socketIOManager.getUserClient(this.agents[3]);
        assertEquals(expected, result);
    }

    @Test
    public void getUserClient_ReturnVivaldi() throws IOException {
        String expected = "Vivaldi";
        String result = socketIOManager.getUserClient(this.agents[4]);
        assertEquals(expected, result);
    }

    @Before
    public void setUp() throws IOException {
        this.socketIOManager = new SocketIOManager();
        this.server = new HttpServer(1234);
        this.rand = new Random();

        //0.Chrome, 1.Opera, 2.Firefox, 3.Safari, 4, Vivaldi
        this.agents = new String[]
                {
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36 OPR/55.0.2994.61",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:62.0) Gecko/20100101 Firefox/62.0",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Safari/605.1.15",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.102 Safari/537.36 Vivaldi/1.93.955.42"
                };
    }

    public String readContent(InputStream in) {
        String content = "";
        int currentChar;
        try (DataInputStream input = new DataInputStream(in)) {
            while ((currentChar = input.read()) != -1) {
                content += (char) currentChar;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
