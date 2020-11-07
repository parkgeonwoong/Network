package Network;

import java.net.*;

public class MChatServer {
	protected MRoomManager manager;

	public MChatServer(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			manager = new MRoomManager();
			System.out.println("MChatServer is ready");
			while (true) {
				Socket client = server.accept();
				PrintDebugMessage.print("From:" + client.getInetAddress());
				MChatHandler c = new MChatHandler(manager, client);
				c.start();
			}
		} catch (Exception e) {
			PrintDebugMessage.print(e);
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 1)
			new MChatServer(9831);
		else
			new MChatServer(Integer.parseInt(args[0]));
	}
}
