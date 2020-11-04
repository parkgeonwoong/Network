package Network;

import java.io.*;
import java.net.*;

public class EchoServer {
	private ServerSocket server;
	
	public EchoServer(int port) throws IOException {
		server = new ServerSocket(port);
	}
	
	public void service() throws IOException {
		System.out.println("EchoServer is ready.");
		
		Socket client = server.accept();
		
		InputStream is = client.getInputStream();
		OutputStream os = client.getOutputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		PrintWriter out = new PrintWriter(os, true);
		
		while(true) {
			String msg = in.readLine();
			System.out.println(msg);
			if(msg.equals("bye")) {
				break;
			}
			out.println(">>" + msg);
		}	
	}
	
	public void close() throws IOException {
		server.close();
	}
	
	public static void main(String[] args) {
		try {
			EchoServer es = new EchoServer(1289);
			es.service();
			es.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
