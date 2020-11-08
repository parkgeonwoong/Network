package Network;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.StringTokenizer;

public class MChatApplet extends Applet implements Runnable, ActionListener {
	private BufferedReader i;
	private PrintWriter o;
	private TextArea output;
	private TextField input,logtext, roominput;
	private Thread listener;
	private CardLayout card;
	private boolean stop, timeouted;
	private Choice users;
	private List rooms;
	private Button exit;
	
	public void init() {
		card = new CardLayout();
		setLayout (card);
		Panel login = new Panel(new BorderLayout());
		Panel bottom = new Panel();
		logtext.addActionListener(this);
		bottom.add(new Label("�α���:"));
		bottom.add(logtext);
		login.add("South", bottom);
		
		Panel lounge = new Panel(new BorderLayout());
		lounge.add("North", new Label("���� ä�� �� ���", Label.CENTER));
		lounge.add("Center", rooms = new List());
		Panel loungeBottom = new Panel();
		loungeBottom.add(new Label("���� ���� �� �̸�:"));
		loungeBottom.add(roominput = new TextField(15));
		roominput.addActionListener(this);
		rooms.addActionListener(this);
		lounge.add("South", loungeBottom);
		
		Panel chat = new Panel(new BorderLayout());
		chat.add("Center", output = new TextArea());
		output.setEnabled(false);
		Panel chatBottom = new Panel(new BorderLayout(10,5));
		Panel left = new Panel(new BorderLayout(2,5));
		Panel right = new Panel(new BorderLayout(2,5));
		left.add("West", new Label("ä�� �Է�"));
		left.add("Center", input = new TextField(20));
		input.addActionListener(this);
		right.add("West", new Label("�ӼӸ�"));
		right.add("Center", users = new Choice());
		right.add("East", exit = new Button("������"));
		exit.addActionListener(this);
		users.add("��ü �����");
		chatBottom.add("East", right);
		chatBottom.add("Center", left);
		chat.add("South", chatBottom);
		add(login, "login");
		add(lounge, "room");
		add(chat, "chat");
		card.show(this, "login");
		logtext.requestFocus();
	}
	
	public void start() {
		if(timeouted)
			return;
		stop = false;
		listener = new Thread(this);
		listener.start();
	}
	
	public void stop() {
		stop = true;
	}
	
	public void run() {
		try {
			String host = getCodeBase().getHost();
			String port = getParameter("port");
			if(port == null)
				port = "9831";
			Socket s = new Socket(host, Integer.parseInt(port));
			s.setSoTimeout(60000*20);
			i = new BufferedReader(new InputStreamReader(s.getInputStream()));
			o = new PrintWriter(s.getOutputStream(), true);
			execute();
		} catch (Exception ioe) {
			
		}
	}
	
	public void execute() {
		try {
			while (!stop) {
				String line = i.readLine();
				if(line.startsWith("#!")) {
					parse(line);
				} else {
					output.append(line);
					output.append("\n");
				}
			}
		} catch(Exception ioe) {
		} finally {
			stop = true;
			timeouted = true;
			listener = null;
			o.close();
		}
	}
	
	protected void parse(String line) {
		char c = line.charAt(2);
		switch(c) {
			case 'u':
				handleUser(line); 	//����� ����
				break;
			case 'r':
				handleRoom(line);	//ä�� �� ����
				break;
			default:
				break;
		}
 	}

	void handleUser(String line) {
		char d = line.charAt(3);
		switch(d) {
			case '+':		//���ο� ����� �߰�
				String uname = line.substring(4);
				users.add(uname);
				output.append(uname);
				output.append("���� �����ϼ̽��ϴ�.\n");
				break;
			case '-':		//���� ���� ���� �����
				try {
					users.remove(line.substring(4));
				} catch (Exception e) {}
				break;
			case '*': 		//���� ��� �����
				int n = users.getItemCount();
				for(int i=0; i<n; i++)
					users.remove(i);
				
				StringTokenizer st = new StringTokenizer(line.substring(4), ":");
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					users.add(token);
				}
				break;
			default:
				break;
		}
	}
	
	void handleRoom(String line) {
		char d = line.charAt(3);
		switch(d) {
			case '*':		//��� �� ���
				StringTokenizer st = new StringTokenizer(line.substring(4), ":");
				rooms.removeAll();
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					rooms.add(token);
				}
				break;
			default:
				break;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Component c = (Component) e.getSource();
		if(c == logtext) { 			//����� �α���
			String loginname = logtext.getText();
			loginname  = loginname.trim();
			if(loginname == null || loginname.length() == 0) {
				return;
			}
			o.println(loginname);
			o.println("!#r*");
			card.show(this, "room");
		} else if(c == input) { 		//��ȭ �Է�
			if(users.getSelectedIndex() != 0) {
				StringBuffer msg = new StringBuffer("!#$");
				msg.append(users.getSelectedItem());
				msg.append(":");
				msg.append(input.getText());
				o.println(msg.toString());
			} else {
				o.println(input.getText());
			}
			input.setText("");
		} else if (c == rooms) {		//ä�� �� ����
			o.println("!#r+" + rooms.getSelectedItem());
			o.println("!#u*");
			card.show(this, "chat");
			output.setText(rooms.getSelectedItem() + "�� ���� ���� ȯ���մϴ�.\n");
			input.requestFocus();
		} else if (c == exit ) { 		//���� ���� ����
			o.println("!#r-");
			o.println("!#r*");
			card.show(this, "room");
			int nu = users.getItemCount() - 1;
			for(int i=nu; i>0; i--) {
				users.remove(i);
			}
		} else if (c == roominput) { 	//���ο� �� ����� 
			String rname = roominput.getText().trim();
			if(rname == null || rname.length() == 0)
				return;
			o.println("!#rc" + rname);
			o.println("!#r+" + rname);
			o.println("!#u*");
			card.show(this, "chat");
			output.setText(rname + "�� ����̽��ϴ�.\n");
			roominput.setText("");
			input.requestFocus();
		}
	}
}
