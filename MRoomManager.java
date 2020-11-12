package Network;

import java.util.*;

public class MRoomManager {
	private Vector rooms;
	private MChatRoom lounge;
	
	public MRoomManager() {
		rooms = new Vector();
		lounge = new MChatRoom("$#!@¶ó¿îÁö", this);
	}
	
	public void addToLounge(MChatHandler t) {
		lounge.add(t);
	}
	
	public void removeFromLounge(MChatHandler t) {
		lounge.remove(t);
	}
	
	public void createRoom(String name) {
		rooms.add(new MChatRoom(name, this));
		lounge.broadcast(getAllRoomNamesString());
	}
	
	public void removeRoom(MChatRoom room) {
		synchronized(rooms) {
			rooms.remove(room);
		}
		lounge.broadcast(getAllRoomNamesString());
	}
	
	public String[] getAllRoomNames() {
		synchronized(rooms) {
			int n = rooms.size();
			String names[] = new String[n];
			for(int i=0; i<n; i++) {
				MChatRoom c =(MChatRoom) rooms.elementAt(i);
				names[i] = c.getName();
			}
			return names;
		}
	}
	
	public String getAllRoomNamesString() {
		synchronized(rooms) {
			int n = rooms.size();
			StringBuffer buf = new StringBuffer("#!r*");
			for(int i=0; i<n; i++) {
				MChatRoom c = (MChatRoom) rooms.elementAt(i);
				buf.append(c.getName());
				buf.append(":");
			}
			return buf.toString();
		}
	}
	
	public MChatRoom getChattingRoom(String name) { 
		synchronized(rooms) {
			int n = rooms.size();
			for(int i=0; i<n; i++) {
				MChatRoom c = (MChatRoom) rooms.elementAt(i);
			if(name.equals(c.getName())); {
				return c;
				}
			}
		}
		return null;
	}	
}
