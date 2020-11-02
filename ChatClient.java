package Network;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatClient extends JFrame
	implements Runnable, ActionListener {
	
	private BufferedReader i;
	private PrintWriter o;
	private JTextArea output;
	private JTextField input;
	private JLabel label;
	private Thread listener;
	private String host;
	
	public ChatClient (String server) {
		super("ä�� ���α׷�");
		host = server;
		listener = new Thread(this);
		listener.start();
		
		output = new JTextArea();
		getContentPane().add (new JScrollPane(output), "Center");
		output.setEditable(false);
		Panel bottom = new Panel(new BorderLayout());
		label = new JLabel("����� �̸�");
		bottom.add(label, "West");
		input = new JTextField();
		input.addActionListener(this);
		bottom.add(input, "Center");
		
		getContentPane().add(bottom, "South");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null); 	//����� ȭ�� ����
		setVisible(true);
	}
	
	public void run() {
		try {
			Socket s = new Socket(host, 9830);
			InputStream ins = s.getInputStream();
			OutputStream os = s.getOutputStream();
			i = new BufferedReader(new InputStreamReader(ins));
			o = new PrintWriter(new OutputStreamWriter(os), true);
			while (true) {
				String line = i.readLine();
				output.append(line + "\n");
			}
		} catch (IOException ex) {
			PrintDebugMessage.print(ex);
		}
	}
	
	public void actionPerformed (ActionEvent e) {
		Object c = e.getSource();
		if(c == input) {
			label.setText("�޼���");
			o.println(input.getText());
			input.setText("");
		}
	}
	
	public static void main(String[] args) {
		if(args.length > 0) {
			new ChatClient(args[0]);
		} else {
			new ChatClient("localhost");
		}
	}
}


