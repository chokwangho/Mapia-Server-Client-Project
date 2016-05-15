/*
 * ��Ʈ��ũ �渶 ���� ���α׷�(������ ���ø����̼�)���� �����ϸ�,
 * Ŭ���̾�Ʈ�� ó�� �������� ��, �̸� ó���ϱ� ���� ������ Ŭ�����̴�.
 * Ŭ���̾�Ʈ�� Mafia_PORT(=54444) ��Ʈ�� �����ϸ�
 * �� Ŭ���̾�Ʈ���� ��Ʈ��ŷ�� ����� GamePlayer_Server ��ü�� �����Ѵ�.
 */

import java.net.*;
import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class Mafia_Server extends Thread {
	Mafia_Server_GUI server_state;  //GUI ������ ����
	public static final int Mafia_PORT = 54444; // ������ ���� ���� ��Ʈ
	Lobby_Server lobby=null; // ������ ���۰� �Բ� ���� �����带 �����Ѵ�.

	int port; // ���� ��Ʈ ����
	ServerSocket MafiaServerSocket=null; // ���� ����

	// ���� ���ø����̼� ���α׷��� main() �޼ҵ�
	public static void main(String args[]) {
		Mafia_Server MafiaSever = new Mafia_Server(Mafia_PORT); //���μ��� ����
		MafiaSever.server_state.lobbyArea.append("Mafia Server up and running...\n");
		MafiaSever.server_state.lobbyArea.append("Mafia Server Initializing.........\n");
		// �����带 �����Ѵ�.
		// �������� start() �޼ҵ带 ȣ���ϸ�
		// ���������� �ڵ����� run() �޼ҵ尡 ȣ��ȴ�.
		MafiaSever.start();
	}

	// ���� Ŭ������ ��ü ������
	public Mafia_Server(int port) {
		try {
			// �־��� ��Ʈ���� ��Ʈ��ũ ��û�� ��ٸ���
			// ���� ����(ServerSocket ��ü)�� �����Ѵ�.
			this.port = port;
			MafiaServerSocket = new ServerSocket(this.port);
			server_state = new Mafia_Server_GUI();
			server_state.setSize(950,700);
			server_state.setVisible(true);
		} catch (IOException e) {
			System.exit(1);
		}

		// Ŭ���̾�Ʈ�� ó�� �����Ͽ� ��Ʈ��ũ ������ �����Ǹ�,
        // Mafia_Server �����忡�� ����(Lobby ��ü ������)�� �ѱ��.
        // ����, ������ ���ǿ����� ��Ʈ��ŷ�� ����ϴ� �����μ� �����Ѵ�.
        lobby = new Lobby_Server(this); // ���� ������ ����
        lobby.start();       // ���ǿ��� ��Ʈ��ŷ�� ����� ���� ������ ����
        
        server_state.lobbyArea.append("GameServer listening on " + Mafia_PORT + " PORT\n");
        server_state.lobbyArea.append("Lobby thread is started...\n");
    }

    // Mafia_Server �������� �ٵ�
    public void run() {
    	Socket socket=null;
        GamePlayer_Server player=null;
		
        while(true) {
        	try {
        		// Mafia_PORT(=54444)���� Ŭ���̾�Ʈ�� ������ ��ٸ���.
        		// listening...
        		// �� ��, accept() �޼ҵ忡���� Ŭ���̾�Ʈ�� �����ϸ�
        		// �ش� Ŭ���̾�Ʈ�� ��Ʈ��ŷ�� ���� Socket ��ü�� ������ �ش�.
        		socket = MafiaServerSocket.accept();
        		if(socket == null) {
        			continue;
        		} else {
        			// �־��� socket ��ü�� ��Ʈ��ŷ�� ������ GamePlayer_Server ��ü ����
        			// ����(socket), ���� ���� ������(lobby), �� ������(null) ��...
        			// �� ��, GamePlayer_Server ��ü�� Ŭ���̾�Ʈ �Ǵ� �÷��̾�� �Ѵ�.
        			player = new GamePlayer_Server(socket, lobby, null);
        			if(player == null) {
        				System.out.println("PLAYER is null");
        				continue;
        			}
        		}
        		// Ŭ���̾�Ʈ�� ��Ʈ��ũ �渶 Ŭ���̾�Ʈ�� �´��� üũ
        		player.validation();

        		// ������ Ŭ���̾�Ʈ�� ������ �÷��̾� ����Ʈ�� �߰��Ѵ�.
        		lobby.addPlayer(player);

        		// ������ Ŭ���̾�Ʈ���� �� ����Ʈ�� ������.
        		lobby.broadcastRoomList();

        		// ���ǿ� �� ����Ʈ�� ��ü���� �ٽ� ������.
        		lobby.broadcastLobbyUserList();

        		// �÷��̾��� ������ Ÿ�Ӿƿ�(SO_TIMEOUT)�� 10msec�� �����Ѵ�.
        		// �Ϲ�������, ���Ͽ��� �����͸� �б� ���� ��ŷ �Ǵµ�
        		// SO_TIMEOUT�� ���������ν� �����͸� �б����� ��ŷ �Ǿ�����,
        		// Ÿ�Ӿƿ��� �߻��ϴ� ��ŷ ���¿��� ������������ �Ѵ�.
        		player.socket.setSoTimeout(10);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }
}