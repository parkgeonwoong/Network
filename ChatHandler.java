package Network;

import java.net.*;
import java.io.*;

public class ChatHandler extends Thread {
	private Socket 	s;
	private BufferedReader i;
	private PrintWriter o;
	private ChatServer server;
	
	public ChatHandler(ChatServer server, Socket s) throws IOException {
		this.s = s;
		this.server = server;
		InputStream ins = s.getInputStream();
		OutputStream os = s.getOutputStream();
		i = new BufferedReader(new InputStreamReader(ins));
		o = new PrintWriter(new OutputStreamWriter(os), true);
	}
	
	public void run() {
		String name = "";
		try {
			name = i.readLine();
			server.register(this);
			broadcast(name + "¥‘¿Ã πÊπÆ«œºÃΩ¿¥œ¥Ÿ.");
			while (true) {
				String msg = i.readLine();
				broadcast (name + "-" + msg);
			}
			
		} catch (IOException ex) {
			PrintDebugMessage.print(ex);
		}
		server.unregister(this);
		broadcast(name + "¥‘¿Ã ≥™∞°ºÃΩ¿¥œ¥Ÿ.");
		try {
			i.close();
			o.close();
			s.close();
		} catch(IOException ex) {
			PrintDebugMessage.print(ex);
		}
	}
	
	protected void println(String message) { 
		o.println(message);
	}
	
	protected void broadcast(String message) {
		server.broadcast(message);
	}

}
