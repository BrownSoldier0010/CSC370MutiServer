package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocketIOManager {

	public Map<String, String> readHttpRequestHeader(InputStream in) {
		String rawHeaders = readToTerminalSymbol(in, "\r\n\r\n");
		Map<String, String> headers = new HashMap<>();
		String[] splitHeaders = rawHeaders.split("\r\n");
		for (String string : splitHeaders) {
			string = string.trim();
			String[] kvArr = string.split(":");

			if (kvArr.length == 2) {
				headers.put(kvArr[0].trim(), kvArr[1].trim());
			} else if (kvArr.length >= 3) {
				String value = "";
				for (int i = 1; i < kvArr.length; i++) {
					value += kvArr[i];
				}
				headers.put(kvArr[0], value);
			} else {
				headers.put("CMD", string);
			}
		}
		for (String string : splitHeaders) {
			System.out.println("Header " + string);
		}
		return headers;
	}

	public byte[] readHttpRequestBody(InputStream in) throws IOException {
		int bytesInStream; 
		ArrayList<Byte> bytes = new ArrayList<>(); 
		while ((bytesInStream = in.available()) > 0) {
			byte[] chunk = new byte[bytesInStream]; 
			in.read(chunk); 
			for (byte b : chunk) {
				bytes.add(b); 
			}
		}
		byte[] body = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			body[i] = bytes.get(i);
		}
		return  body; 
	}

	public void writeHttpResponseHeader(OutputStream out, int responseCode, long fileLength, String fileExtension) {
		String responseHeader;
		switch (responseCode) {
		case 200:
			if(fileExtension.equals("css")) {
				responseHeader = "HTTP/1.0 200 OK\r\nContent-type:text/css\r\nContent-length:" + fileLength + "\r\n\r\n";
			} else if (fileExtension.equals("js")) {
				responseHeader = "HTTP/1.0 200 OK\r\nContent-type:text/javascript\r\nContent-length:" + fileLength + "\r\n\r\n";
			} else {
				responseHeader = "HTTP/1.0 200 OK\r\nContent-type:text/html\r\nContent-length:" + fileLength + "\r\n\r\n";
			}
			break;
		case 404:
			responseHeader = "HTTP/1.0 404 Not Found\r\nContent-type:text/html\r\nContent-length:" + fileLength
					+ "\r\n\r\n";
			break;
		default:
			responseHeader = "HTTP/1.0 500 Internal Server Error\r\nContent-type:text/html\r\nContent-length:"
					+ fileLength + "\r\n\r\n";
			break;
		}
		try {
			out.write(responseHeader.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeHttpResponseBody(OutputStream out, byte[] bodyData) {

	}

	public String readToTerminalSymbol(InputStream in, String terminalSymbol) {
		String header = "";
		int i;

		try {
			while ((i = in.read()) != -1) {
				header += (char) i;
				if (header.contains(terminalSymbol)) {
					return header;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// return that header cannot be read, internal server error
			return null;
		}
		return null;
	}

	public String readKnownLength(InputStream in, int size) {
		// reading the body, or the length from the header
		// read until hit byte amount for body
		return null;
	}

}
