package Network;

import java.util.*;

public class MChatRoom {
	private MRoomManager manager;
	private String name;
	protected Vector handlers;
	
	public MChatRoom(String name, MRoomManager m) {
		this.name = name;
		manager = m;
		handlers = new Vector(2, 5);
	}
	
	public void add(MChatHandler h) {
		synchronized(handlers) {
			PrintDebugMessage.print("add:" + h.getUserName());
			handlers.add(h);
		}
	}
	
	public void remove(MChatHandler h) {
		synchronized(handlers) {
			handlers.remove(h);
			if(handlers.size() == 0) {
				manager.removeRoom(this);
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public MChatHandler getHandler(String name) {
		synchronized(handlers) {
			int n = handlers.size();
			for(int i=0; i<n; i++) {
				MChatHandler c = (MChatHandler)handlers.elementAt(i);
				if(name.equals(c.getName())) {
					return c;
				}
			}
			return null;
		}
	}
	
	public String[] getAllNames() {
		synchronized(handlers) {
			int n = handlers.size();
			String names[] = new String[n];
			for(int i=0; i<n; i++) {
				MChatHandler c = (MChatHandler)handlers.elementAt(i);
				names[i] = c.getUserName();
			}
			return names;
		}
	}
	
	public String getAllNamesString() {
		synchronized(handlers) {
			int n = handlers.size();
			StringBuffer buf = new StringBuffer("#!u*");
			for(int i=0; i<n; i++) {
				MChatHandler c = (MChatHandler)handlers.elementAt(i);
				buf.append(c.getUserName());
				buf.append(":");
			}
			return buf.toString();
		}
	}
	
	public void broadcast(String msg) {
		synchronized(handlers) {
			Enumeration e = handlers.elements();
			PrintDebugMessage.print("# of user:" + handlers.size());;
			
			while (e.hasMoreElements()) {
				MChatHandler c = (MChatHandler) e.nextElement();
				c.println(msg);
			}
		}
	}
}
