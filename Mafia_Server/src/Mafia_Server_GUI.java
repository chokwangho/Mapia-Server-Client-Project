
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;

//������ GUI�� �����ϴ� �κ��̴�. ���� ������ �ʱ� �������� �������̴�.
//�� ������ ���е� ���¸� �� �� �гο� ��� ������ ���·� ��������.
//�ܼ��� Ʈ������ �����ϰų� �׽�Ʈ ������ ���Ǿ� ����.
public class Mafia_Server_GUI extends JFrame {
	JLabel lobbyLabel;  
	JLabel messageLabel;
	JLabel roomLabel;
	JTextArea lobbyArea;
	JTextArea messageArea;
	JTextArea roomArea;
	JPanel lobbyPanel, messagePanel, roomPanel;
	JPanel waitingPanel, statePanel, rPanel;
	JTabbedPane serverTab;
		
	//������ - ���� ��Ʈ�Ѱ� �г��� �����Ѵ�.
	public Mafia_Server_GUI(){
		super("Server Main Frame");
		setLayout(new BorderLayout(2,2));
		
		makeWaiting();
		waitingPanel = new JPanel();
		waitingPanel.add(lobbyPanel);
		
		makeLobby();
		statePanel = new JPanel();
		statePanel.add(messagePanel);
		
		makeRoom();
		roomPanel = new JPanel();
		roomPanel.add(rPanel);
		
		//�� ���� �г��� �ϳ��� �����ο� ��ϵǾ�����.
		serverTab = new JTabbedPane();
		serverTab.addTab("����", waitingPanel);
		serverTab.addTab("����", statePanel);   
		serverTab.addTab("�����", roomPanel);
		
		getContentPane().add("Center", serverTab); 
	     	    
		//������ ������ �Ϸ��Ѵ�.
	    setSize(950,700);  
	    autoAlign();  
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void makeWaiting(){
		//������ �ʱ� ���� ���µ��� ǥ���ϱ� ���� �г��̴�.
		lobbyPanel = new JPanel();
		lobbyPanel.setLayout(new BorderLayout());
		lobbyLabel = new JLabel("Message", JLabel.CENTER);
		lobbyArea = new JTextArea("");
		lobbyArea.setColumns(80);
		lobbyArea.setRows(33);
		lobbyArea.setEditable(true);
		JScrollPane spLobby = new JScrollPane(lobbyArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lobbyPanel.add(lobbyLabel, BorderLayout.NORTH);
		lobbyPanel.add(spLobby, BorderLayout.CENTER);
	}
	
	public void makeLobby(){
		// Ŭ���̾�Ʈ�� �κ� ���� ���� ���¸� ǥ���ϱ� ���� �г��� �����Ѵ�.
		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messageLabel = new JLabel("Message", JLabel.CENTER);
		messageArea = new JTextArea("");
		messageArea.setColumns(80);
		messageArea.setRows(33);
		messageArea.setEditable(true);
		JScrollPane spMessage = new JScrollPane(messageArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messagePanel.add(messageLabel, BorderLayout.NORTH);
		messagePanel.add(spMessage, BorderLayout.CENTER);
	}
	
	public void makeRoom(){
		//������ �����ϴ� ���� ���¸� ǥ���ϱ� ���� �г��̴�.
		rPanel = new JPanel();
		rPanel.setLayout(new BorderLayout());
		roomLabel = new JLabel("Message", JLabel.CENTER);
		roomArea = new JTextArea("");
		roomArea.setColumns(80);
		roomArea.setRows(33);
		roomArea.setEditable(true);
		JScrollPane spMessage = new JScrollPane(roomArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rPanel.add(roomLabel, BorderLayout.NORTH);
		rPanel.add(spMessage, BorderLayout.CENTER);
	}
	
	public void autoAlign() {
		//ȭ���� �߾ӿ� �����ϰ� ��ġ ��Ų��.
        int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		int dw = getSize().width;
		int dh = getSize().height;
		int x = (((sw - dw) / 2) < 0) ? 0 : ((sw - dw) / 2);
		int y = (((sh - dh) / 2) < 0) ? 0 : ((sh - dh) / 2);
		
 	    setLocation(x, y);
	}
}

