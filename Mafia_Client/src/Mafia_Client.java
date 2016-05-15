import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JFrame;

// 클라이언트의 메인 클래스이다.
// 초기 클라이언트 영역을 구성하는 cardlayout을 구성하고 여러 개의 패널을 생성하여
// 각 패널을 교체하여 화면을 교체하는 식으로 게임을 진행하게 된다.

public class Mafia_Client extends JFrame implements Runnable {
		
	Thread clientLobbyThread = null;  // 이 클래스는 스레드 클래스 임 
	final static String Mafia_SERVER = "127.0.0.1";  // 서버가 정해지면 이를 바꾸어야 함  
	final static int Mafia_PORT = 54444;             // 
	public Socket socket = null;                     // 서버와 연결할 소켓 
	public BufferedReader is = null;
	public PrintWriter os = null;
	private boolean connFlag = false;
	
	// 3개의 메인 GUI 패널을 합칠 CardLayout 
	CardLayout MafiaCardLayout = null;
	Panel cardPanel = null;
	Game_Client gamePanel = null;
	Lobby_Client lobbyPanel = null;
	Login_Client loginPanel = null;
	// 현재 클라이언트가 어디에 있는지 확인하게 될 플래그, 서버에서 온 메시지를 분리할때 중요하게 사용 
	boolean currentRoomIsLobby = true;

	String id = "null";
	

	// 생성자에서 기본적인 GUI 컴퍼넌트 생성과 더불어 3개의 메인 패널을 합칠
	// 카드레이아웃 생성하고 합침. 
	public Mafia_Client() {
		super("게임 클라이언트");
		
		MafiaCardLayout = new CardLayout();
		cardPanel = new Panel(MafiaCardLayout);
		loginPanel = new Login_Client(Mafia_Client.this);
		lobbyPanel = new Lobby_Client(Mafia_Client.this);
		gamePanel = new Game_Client(Mafia_Client.this);

		cardPanel.add(loginPanel, "LoginPanel");
		cardPanel.add(lobbyPanel, "LobbyPanel");
		cardPanel.add(gamePanel, "GamePanel");

		setLayout(new BorderLayout());
		add("Center", cardPanel);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(950, 700);
		setVisible(true);
		setResizable(false);
	}

	// 서버와 소켓을 이용하여 메시지 통신 준비 
	public void connect(String playerId, String playerPwd) {
		connFlag = false;				// 연결상태임을 확인할 플래그 
		try {
			socket = new Socket(Mafia_SERVER, Mafia_PORT);
		} catch (UnknownHostException e) {
			lobbyPanel.chatArea.append("예외발생: " + e + "\n");
			lobbyPanel.chatArea.append("예외가 발생되어 프로그램을 종료합니다.\n");
			lobbyPanel.chatArea.append("잠시 후 프로그램이 강제 종료 됩니다.\n");
			lobbyPanel.chatArea.append("안녕히 계세요...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
       	 	dlg.show();                 // 에러발생시 해당 메시지를 포함하는 
			return;                     // WarningDialog 생성, 그리고 EXIT 플래그 사용으로  
		} catch (IOException e) {       // 클라이언트 시스템 종료 
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
       	 	dlg.show();
			return;
		}

		try {                           // 바이트 스트림을 문자스트림으로 변환 
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			lobbyPanel.chatArea.append("예외발생: " + e + "\n");
			lobbyPanel.chatArea.append("예외가 발생되어 프로그램을 종료합니다.\n");
			lobbyPanel.chatArea.append("잠시 후 프로그램이 강제 종료 됩니다.\n");
			lobbyPanel.chatArea.append("안녕히 계세요...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
       	 	dlg.show();                 // 이도 역시 WarningDialog 생성하고 시스템 종료 
       	 	return;
		}
		
		if ((os != null) & (is != null)) {
		} else {
			lobbyPanel.chatArea.append("예외발생!!!\n");
			lobbyPanel.chatArea.append("예외가 발생되어 프로그램을 종료합니다.\n");
			lobbyPanel.chatArea.append("잠시 후 프로그램이 강제 종료 됩니다.\n");
			lobbyPanel.chatArea.append("안녕히 계세요...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
       	 	dlg.show();                 // 이도 역시 WarningDialog 생성하고 시스템 종료 
		}

		String receive = null;
		StringTokenizer st = null;
		String command = null;
		try {
			// 초기 설정 작업 , 사용자의 아이디 확인
			receive = receiveMessage(); 
			id=playerId;
			sendToServer(playerId + "|" + playerPwd);
		} catch (Exception e) { 
			lobbyPanel.chatArea.append("예외발생: " + e + "\n");
			lobbyPanel.chatArea.append("예외가 발생되어 프로그램을 종료합니다.\n");
			lobbyPanel.chatArea.append("잠시 후 프로그램이 강제 종료 됩니다.\n");
			lobbyPanel.chatArea.append("안녕히 계세요...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
       	 	dlg.show();
		}
		connFlag = true;                // 이제서야 연결이 확인됨.
		try {                           // 한 클라이언트에게 계속해서 메세지를 기다리는것 대신에 타임아웃 설정으로 
			socket.setSoTimeout(10);    // 무한정 기다릴 순 없다.  
		} catch (SocketException e) {
			lobbyPanel.chatArea.append("예외발생: " + e + "\n");
			lobbyPanel.chatArea.append("예외가 발생되어 프로그램을 종료합니다.\n");
			lobbyPanel.chatArea.append("잠시 후 프로그램이 강제 종료 됩니다.\n");
			lobbyPanel.chatArea.append("안녕히 계세요...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
       	 	dlg.show();
		}
	}

	// 서버와의 소켓 통신을 해제하기 위한 메소드 
	public synchronized void destroy() {
		if (clientLobbyThread != null) {
			clientLobbyThread = null;    // 스레드 종료 
		}
		try { 
			if (is != null) {            // inputstream 닫기 
				is.close();
				is = null;
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} 

		try { 
			if (os != null) {            // outputstream 닫기 
				os.close();
				os = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		try { 
			if (socket != null) {        // socket 닫기 
				socket.close();
				socket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	// 이 스레드를 시작시키는 부분을 가지고 있는 메소드 
	public synchronized void start() {
		if (clientLobbyThread == null) {
			try {					    // 문자 스트림으로 변환이 되어 있지 않으면 변환 
				if (socket != null) {
					if (is == null) {
						is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					}
					if (os == null) {
						os = new PrintWriter(socket.getOutputStream(), true);
					}
				}
				clientLobbyThread = new Thread(this);
				clientLobbyThread.start();  // 스레드 시작 
			} catch (IOException e) {
				WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
				dlg.show();                 // 에러발생시 클라이언트 시스템 종료 
			}
		}
	}

	public synchronized void stop() {
		clientLobbyThread = null;
	}

	// 서버에 메시지를 보내는 메소드, 다른 패널들은 모두 이를 사용 
	public void sendToServer(String message) {
		if (message != null) {
			os.println(message);
			os.flush();

			if (os.checkError()) {
			}
		}
	}

	// 소켓을 통해서 들어오는 메세지들은 이 메소드에서 읽어짐.
	public String receiveMessage() throws SocketException {
		String message = null;
		try {
			message = is.readLine();
		} catch (SocketException e) {
			throw (e);
		} catch (Exception e) {
			return "";
		}
		return message;
	}

	// Mafia_Client 스레드 클래스의 주요 역할 
	public void run() {
		String receive = null;
		while (Thread.currentThread() == clientLobbyThread) {
			try {
				Thread.sleep(70);    // 서버의 부하를 막음 
			} catch (InterruptedException e) {
			}
			if (connFlag == false) { // 사용자 아이디가 확인될때까지 다음으로 진행하지 않음.
				continue;            
			}
			try {                    // 메세지를 받아서 
				receive = receiveMessage();
				if (receive == null || "".equals(receive)) {
					continue;
				}
				if (currentRoomIsLobby == true) {
					// 현재 클라이언트가 로비에 있으면, 로비의 메시지 처리루틴으로
					lobbyPanel.processMessage(receive);
				} else {
					// 현재 클라이언트가 게임 룸에 있으면, 게임 방의 메시지 처리루틴으로
					gamePanel.processMessage(receive);
				}
			} catch (SocketException e) { 
				WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","지금은 접속이 불가합니다.|잘못된 네트워크 연산이 수행되었습니다.|프로그램을 종료합니다.","EXIT");
	       	 	dlg.show();
				return;
			} catch (Exception e) { 
			}
		}
	}

	// 클라이언트 메인 함수 
	public static void main(String args[]) {
		Mafia_Client hc = new Mafia_Client();
		hc.start();
	}
}