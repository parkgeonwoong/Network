package Network;

public class PrintDebugMessage {
	public static boolean debug = false;
	
	public static void print(Exception e) {
		String d = System.getProperty("debug");
		if("true".equalsIgnoreCase(d) || debug == true) {
			e.printStackTrace();
		}
	}
	
	public static void print(String msg) {
		String d = System.getProperty("debug");
		if("true".equalsIgnoreCase(d) || debug == true) {
			System.err.println(msg);
		}
	}
}
