
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;

//서버의 GUI를 구성하는 부분이다. 서버 생성시 초기 보여지는 프레임이다.
//세 가지의 구분된 상태를 각 탭 패널에 담아 적절한 형태로 보여진다.
//단순히 트래픽을 관찰하거나 테스트 용으로 사용되어 진다.
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
		
	//생성자 - 각종 컨트롤과 패널을 구성한다.
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
		
		//세 개의 패널은 하나의 탭폐인에 등록되어진다.
		serverTab = new JTabbedPane();
		serverTab.addTab("서버", waitingPanel);
		serverTab.addTab("대기실", statePanel);   
		serverTab.addTab("방관리", roomPanel);
		
		getContentPane().add("Center", serverTab); 
	     	    
		//프레임 구성을 완료한다.
	    setSize(950,700);  
	    autoAlign();  
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void makeWaiting(){
		//서버의 초기 동작 상태들을 표현하기 위한 패널이다.
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
		// 클라이언트가 로비에 있을 때의 상태를 표현하기 위한 패널을 구성한다.
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
		//서버와 교신하는 룸의 상태를 표현하기 위한 패널이다.
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
		//화면의 중앙에 적당하게 위치 시킨다.
        int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		int dw = getSize().width;
		int dh = getSize().height;
		int x = (((sw - dw) / 2) < 0) ? 0 : ((sw - dw) / 2);
		int y = (((sh - dh) / 2) < 0) ? 0 : ((sh - dh) / 2);
		
 	    setLocation(x, y);
	}
}

