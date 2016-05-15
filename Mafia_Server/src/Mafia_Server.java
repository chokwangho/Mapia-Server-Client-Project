/*
 * 네트워크 경마 서버 프로그램(서버측 애플리케이션)으로 동작하며,
 * 클라이언트가 처음 접속했을 때, 이를 처리하기 위한 스레드 클래스이다.
 * 클라이언트가 Mafia_PORT(=54444) 포트로 접속하면
 * 이 클라이언트와의 네트워킹을 담당할 GamePlayer_Server 객체를 생성한다.
 */

import java.net.*;
import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class Mafia_Server extends Thread {
	Mafia_Server_GUI server_state;  //GUI 쓰레드 생성
	public static final int Mafia_PORT = 54444; // 접속을 위한 서버 포트
	Lobby_Server lobby=null; // 서버는 시작과 함께 대기실 쓰레드를 생성한다.

	int port; // 서버 포트 정보
	ServerSocket MafiaServerSocket=null; // 서버 소켓

	// 서버 애플리케이션 프로그램의 main() 메소드
	public static void main(String args[]) {
		Mafia_Server MafiaSever = new Mafia_Server(Mafia_PORT); //메인서버 실행
		MafiaSever.server_state.lobbyArea.append("Mafia Server up and running...\n");
		MafiaSever.server_state.lobbyArea.append("Mafia Server Initializing.........\n");
		// 스레드를 시작한다.
		// 스레드의 start() 메소드를 호출하면
		// 내부적으로 자동으로 run() 메소드가 호출된다.
		MafiaSever.start();
	}

	// 서버 클래스의 객체 생성자
	public Mafia_Server(int port) {
		try {
			// 주어진 포트에서 네트워크 요청을 기다리는
			// 서버 소켓(ServerSocket 객체)을 생성한다.
			this.port = port;
			MafiaServerSocket = new ServerSocket(this.port);
			server_state = new Mafia_Server_GUI();
			server_state.setSize(950,700);
			server_state.setVisible(true);
		} catch (IOException e) {
			System.exit(1);
		}

		// 클라이언트가 처음 접속하여 네트워크 연결이 성립되면,
        // Mafia_Server 스레드에서 대기실(Lobby 객체 스레드)로 넘긴다.
        // 따라서, 실제로 대기실에서의 네트워킹을 담당하는 서버로서 동작한다.
        lobby = new Lobby_Server(this); // 대기실 스레드 실행
        lobby.start();       // 대기실에서 네트워킹을 담당한 서버 스레드 시작
        
        server_state.lobbyArea.append("GameServer listening on " + Mafia_PORT + " PORT\n");
        server_state.lobbyArea.append("Lobby thread is started...\n");
    }

    // Mafia_Server 스레드의 바디
    public void run() {
    	Socket socket=null;
        GamePlayer_Server player=null;
		
        while(true) {
        	try {
        		// Mafia_PORT(=54444)에서 클라이언트의 접속을 기다린다.
        		// listening...
        		// 이 때, accept() 메소드에서는 클라이언트가 접속하면
        		// 해당 클라이언트와 네트워킹을 위한 Socket 객체를 리턴해 준다.
        		socket = MafiaServerSocket.accept();
        		if(socket == null) {
        			continue;
        		} else {
        			// 주어진 socket 객체와 네트워킹을 전담할 GamePlayer_Server 객체 생성
        			// 소켓(socket), 대기실 서버 스레드(lobby), 룸 스레드(null) 등...
        			// 이 때, GamePlayer_Server 객체는 클라이언트 또는 플레이어라 한다.
        			player = new GamePlayer_Server(socket, lobby, null);
        			if(player == null) {
        				System.out.println("PLAYER is null");
        				continue;
        			}
        		}
        		// 클라이언트가 네트워크 경마 클라이언트가 맞는지 체크
        		player.validation();

        		// 접속한 클라이언트를 대기실의 플레이어 리스트에 추가한다.
        		lobby.addPlayer(player);

        		// 접속한 클라이언트에게 룸 리스트를 보낸다.
        		lobby.broadcastRoomList();

        		// 대기실에 룸 리스트를 전체에게 다시 보낸다.
        		lobby.broadcastLobbyUserList();

        		// 플레이어의 소켓의 타임아웃(SO_TIMEOUT)을 10msec로 설정한다.
        		// 일반적으로, 소켓에서 데이터를 읽기 위해 블럭킹 되는데
        		// SO_TIMEOUT을 설정함으로써 데이터를 읽기위해 블럭킹 되었더라도,
        		// 타임아웃이 발생하는 블럭킹 상태에서 빠져나오도록 한다.
        		player.socket.setSoTimeout(10);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }
}