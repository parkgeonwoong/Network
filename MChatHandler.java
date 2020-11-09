package Network;

import java.net.*;
import java.io.*;
import java.util.*;

public class MChatHandler extends Thread {
	private Socket s;
	private BufferedReader i;
	private PrintWriter o;
	private MRoomManager manager;
	private MChatRoom room;
	private boolean stop;
	private String name;

	public MChatHandler(MRoomManager man, Socket s) throws IOException{
		this.s = s;
		this.manager = man;
		manager.addToLounge(this);
		InputStream ins = s.getInputStream();
		OutputStream os = s.getOutputStream();
		i = new BufferedReader(new InputStreamReader(ins));
		o = new PrintWriter(new OutputStreamWriter(os), true);
	}
	
	public void setChattingRoom(MChatRoom c) {
		manager.removeFromLounge(this);
		room = c;
		c.add(this);
		broadcast("#!u+"+name, false);
	}
	
	public void leaveRoom() {
		if(room != null) {
			broadcast(name + " left.");
			broadcast("#!u-"+name, false);
			manager.addToLounge(this);
			room.remove(this);
		}
		room = null;
	}
	
	public String getUserName() {
		return name;
	}
	
	public void setUserName(String name) {
		this.name = name;
	}
	
	public void run() {
		try {
			name = i.readLine();
			while(!stop) {
				String msg = i.readLine();
				PrintDebugMessage.print("received:"+ msg);
				if(msg.startsWith("!#")) {
					char c = msg.charAt(2);
					switch(c) {
					case '$':		//�ӼӸ�
						String toName = msg.substring(3, msg.indexOf(":"));
						if(toName.equals(name)) {
							StringBuffer content = new StringBuffer(name);
							content.append("(�ӼӸ�) - ");
							content.append(msg.substring(msg.indexOf(":")+1));
							o.println(content.toString());
							continue;
						}
						MChatHandler to = room.getHandler(toName);
						if(to != null) {
							StringBuffer content = new StringBuffer(name);
							content.append("(�ӼӸ�) -");
							content.append(msg.substring(msg.indexOf(":")+1));
							to.o.println(content.toString());
							o.println(content.toString());
						}
						continue;
					case 'r': 		//ä�� �� ����
						handleRoom(msg);
						break;
					case 'u':		//����� ����
						handleUser(msg);
					default:
					}
				} else
					broadcast(name + " - " + msg);
			}
		} catch (Exception ex) {
			PrintDebugMessage.print(ex);
		} finally {
			if(room != null) {
				room.remove(this);
			}
			broadcast("#!-" + name);
			broadcast(name + "���� �����̽��ϴ�.");
			try {
				s.close();
			} catch(IOException ex) {
				PrintDebugMessage.print(ex);
			}
		}
	} 
	
	void handleRoom(String msg) throws IOException {
		char d = msg.charAt(3);
		switch(d) {
		case '+':		//ä�� �� ����
			MChatRoom cr = manager.getChattingRoom(msg.substring(4));
			setChattingRoom(cr);
			break;
		case '-':		//���� ���� ����
			leaveRoom();
			break;
		case '*': 		//��ü ä�� �� ���
			o.println(manager.getAllRoomNamesString());
			PrintDebugMessage.print(manager.getAllRoomNamesString());
			break;
		case 'c': 		//���ο� ���� ����
			manager.createRoom(msg.substring(4));
			break;
		default:
			PrintDebugMessage.print(msg);
		}
	}
	
	void handleUser(String msg) throws IOException {
		if(room == null) 
			return;
		
		char d = msg.charAt(3);
		switch(d) {
			case '*': 		//ä�� ������ü ����� ���
				o.println(manager.getAllRoomNamesString());
				PrintDebugMessage.print(manager.getAllRoomNamesString());
				break;
			default:
				PrintDebugMessage.print(msg);
		}
	}
	
	protected void println(String msg) {
		o.println(msg);
	}
	
	protected void broadcast(String message) {
		broadcast(message, true);
	}
	
	protected void broadcast (String message, boolean tome) {
		if(room == null)
			return;
		
		synchronized(room.handlers) {
			Enumeration e = room.handlers.elements();
			PrintDebugMessage.print("# of user:" + room.handlers.size());
			
			while(e.hasMoreElements()) {
				MChatHandler c = (MChatHandler) e.nextElement();
				
				if(!tome && c == this) 
					continue;
				
				synchronized (c.o) {
					c.println(message);
				}
			}
		}
	}
}
