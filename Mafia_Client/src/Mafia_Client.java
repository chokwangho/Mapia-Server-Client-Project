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

// Ŭ���̾�Ʈ�� ���� Ŭ�����̴�.
// �ʱ� Ŭ���̾�Ʈ ������ �����ϴ� cardlayout�� �����ϰ� ���� ���� �г��� �����Ͽ�
// �� �г��� ��ü�Ͽ� ȭ���� ��ü�ϴ� ������ ������ �����ϰ� �ȴ�.

public class Mafia_Client extends JFrame implements Runnable {
		
	Thread clientLobbyThread = null;  // �� Ŭ������ ������ Ŭ���� �� 
	final static String Mafia_SERVER = "127.0.0.1";  // ������ �������� �̸� �ٲپ�� ��  
	final static int Mafia_PORT = 54444;             // 
	public Socket socket = null;                     // ������ ������ ���� 
	public BufferedReader is = null;
	public PrintWriter os = null;
	private boolean connFlag = false;
	
	// 3���� ���� GUI �г��� ��ĥ CardLayout 
	CardLayout MafiaCardLayout = null;
	Panel cardPanel = null;
	Game_Client gamePanel = null;
	Lobby_Client lobbyPanel = null;
	Login_Client loginPanel = null;
	// ���� Ŭ���̾�Ʈ�� ��� �ִ��� Ȯ���ϰ� �� �÷���, �������� �� �޽����� �и��Ҷ� �߿��ϰ� ��� 
	boolean currentRoomIsLobby = true;

	String id = "null";
	

	// �����ڿ��� �⺻���� GUI ���۳�Ʈ ������ ���Ҿ� 3���� ���� �г��� ��ĥ
	// ī�巹�̾ƿ� �����ϰ� ��ħ. 
	public Mafia_Client() {
		super("���� Ŭ���̾�Ʈ");
		
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

	// ������ ������ �̿��Ͽ� �޽��� ��� �غ� 
	public void connect(String playerId, String playerPwd) {
		connFlag = false;				// ����������� Ȯ���� �÷��� 
		try {
			socket = new Socket(Mafia_SERVER, Mafia_PORT);
		} catch (UnknownHostException e) {
			lobbyPanel.chatArea.append("���ܹ߻�: " + e + "\n");
			lobbyPanel.chatArea.append("���ܰ� �߻��Ǿ� ���α׷��� �����մϴ�.\n");
			lobbyPanel.chatArea.append("��� �� ���α׷��� ���� ���� �˴ϴ�.\n");
			lobbyPanel.chatArea.append("�ȳ��� �輼��...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
       	 	dlg.show();                 // �����߻��� �ش� �޽����� �����ϴ� 
			return;                     // WarningDialog ����, �׸��� EXIT �÷��� �������  
		} catch (IOException e) {       // Ŭ���̾�Ʈ �ý��� ���� 
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
       	 	dlg.show();
			return;
		}

		try {                           // ����Ʈ ��Ʈ���� ���ڽ�Ʈ������ ��ȯ 
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			lobbyPanel.chatArea.append("���ܹ߻�: " + e + "\n");
			lobbyPanel.chatArea.append("���ܰ� �߻��Ǿ� ���α׷��� �����մϴ�.\n");
			lobbyPanel.chatArea.append("��� �� ���α׷��� ���� ���� �˴ϴ�.\n");
			lobbyPanel.chatArea.append("�ȳ��� �輼��...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
       	 	dlg.show();                 // �̵� ���� WarningDialog �����ϰ� �ý��� ���� 
       	 	return;
		}
		
		if ((os != null) & (is != null)) {
		} else {
			lobbyPanel.chatArea.append("���ܹ߻�!!!\n");
			lobbyPanel.chatArea.append("���ܰ� �߻��Ǿ� ���α׷��� �����մϴ�.\n");
			lobbyPanel.chatArea.append("��� �� ���α׷��� ���� ���� �˴ϴ�.\n");
			lobbyPanel.chatArea.append("�ȳ��� �輼��...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
       	 	dlg.show();                 // �̵� ���� WarningDialog �����ϰ� �ý��� ���� 
		}

		String receive = null;
		StringTokenizer st = null;
		String command = null;
		try {
			// �ʱ� ���� �۾� , ������� ���̵� Ȯ��
			receive = receiveMessage(); 
			id=playerId;
			sendToServer(playerId + "|" + playerPwd);
		} catch (Exception e) { 
			lobbyPanel.chatArea.append("���ܹ߻�: " + e + "\n");
			lobbyPanel.chatArea.append("���ܰ� �߻��Ǿ� ���α׷��� �����մϴ�.\n");
			lobbyPanel.chatArea.append("��� �� ���α׷��� ���� ���� �˴ϴ�.\n");
			lobbyPanel.chatArea.append("�ȳ��� �輼��...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
       	 	dlg.show();
		}
		connFlag = true;                // �������� ������ Ȯ�ε�.
		try {                           // �� Ŭ���̾�Ʈ���� ����ؼ� �޼����� ��ٸ��°� ��ſ� Ÿ�Ӿƿ� �������� 
			socket.setSoTimeout(10);    // ������ ��ٸ� �� ����.  
		} catch (SocketException e) {
			lobbyPanel.chatArea.append("���ܹ߻�: " + e + "\n");
			lobbyPanel.chatArea.append("���ܰ� �߻��Ǿ� ���α׷��� �����մϴ�.\n");
			lobbyPanel.chatArea.append("��� �� ���α׷��� ���� ���� �˴ϴ�.\n");
			lobbyPanel.chatArea.append("�ȳ��� �輼��...\n");
			WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
       	 	dlg.show();
		}
	}

	// �������� ���� ����� �����ϱ� ���� �޼ҵ� 
	public synchronized void destroy() {
		if (clientLobbyThread != null) {
			clientLobbyThread = null;    // ������ ���� 
		}
		try { 
			if (is != null) {            // inputstream �ݱ� 
				is.close();
				is = null;
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} 

		try { 
			if (os != null) {            // outputstream �ݱ� 
				os.close();
				os = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		try { 
			if (socket != null) {        // socket �ݱ� 
				socket.close();
				socket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	// �� �����带 ���۽�Ű�� �κ��� ������ �ִ� �޼ҵ� 
	public synchronized void start() {
		if (clientLobbyThread == null) {
			try {					    // ���� ��Ʈ������ ��ȯ�� �Ǿ� ���� ������ ��ȯ 
				if (socket != null) {
					if (is == null) {
						is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					}
					if (os == null) {
						os = new PrintWriter(socket.getOutputStream(), true);
					}
				}
				clientLobbyThread = new Thread(this);
				clientLobbyThread.start();  // ������ ���� 
			} catch (IOException e) {
				WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
				dlg.show();                 // �����߻��� Ŭ���̾�Ʈ �ý��� ���� 
			}
		}
	}

	public synchronized void stop() {
		clientLobbyThread = null;
	}

	// ������ �޽����� ������ �޼ҵ�, �ٸ� �гε��� ��� �̸� ��� 
	public void sendToServer(String message) {
		if (message != null) {
			os.println(message);
			os.flush();

			if (os.checkError()) {
			}
		}
	}

	// ������ ���ؼ� ������ �޼������� �� �޼ҵ忡�� �о���.
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

	// Mafia_Client ������ Ŭ������ �ֿ� ���� 
	public void run() {
		String receive = null;
		while (Thread.currentThread() == clientLobbyThread) {
			try {
				Thread.sleep(70);    // ������ ���ϸ� ���� 
			} catch (InterruptedException e) {
			}
			if (connFlag == false) { // ����� ���̵� Ȯ�εɶ����� �������� �������� ����.
				continue;            
			}
			try {                    // �޼����� �޾Ƽ� 
				receive = receiveMessage();
				if (receive == null || "".equals(receive)) {
					continue;
				}
				if (currentRoomIsLobby == true) {
					// ���� Ŭ���̾�Ʈ�� �κ� ������, �κ��� �޽��� ó����ƾ����
					lobbyPanel.processMessage(receive);
				} else {
					// ���� Ŭ���̾�Ʈ�� ���� �뿡 ������, ���� ���� �޽��� ó����ƾ����
					gamePanel.processMessage(receive);
				}
			} catch (SocketException e) { 
				WarningDialog dlg = new WarningDialog(Mafia_Client.this,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|�߸��� ��Ʈ��ũ ������ ����Ǿ����ϴ�.|���α׷��� �����մϴ�.","EXIT");
	       	 	dlg.show();
				return;
			} catch (Exception e) { 
			}
		}
	}

	// Ŭ���̾�Ʈ ���� �Լ� 
	public static void main(String args[]) {
		Mafia_Client hc = new Mafia_Client();
		hc.start();
	}
}