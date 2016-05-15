import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

/*
 *   클라이언트와 1:1 네트워킹을 담당하기 위한 기능을 제공해 준다.
 *   대기실 서버, 또는 룸 서버에서 클라이언트에게 메시지를 보내거나,
 *   또는 받아들이기 위해 GamePlayer_Server 객체를 이용한다.
 *   서버에 접속하면,
 *   Mafia_Server 스레드 객체에서 해당 클라언트와 네트워킹을 위한
 *   소켓을 기반으로 GamePlayer_Server 객체를 생성한다.
 *   GamePlayer_Server 객체에는 소켓, 대기실 서버, 룸 서버 등의 정보를 가지고 있다.
 */

class GamePlayer_Server {
   // 클라이언트와 소켓을 통해 데이터를 주고 받기 위해, 네트워킹 연결을 위한 소켓 객체
   public Socket socket=null;
   // 소켓을 통해 클라이언트로부터 데이터를 읽어들이기 위한 입력 스트림
   public BufferedReader is=null;
   // 소켓을 통해 데이터를 클라이언트에 보내기 위한 스트림
   public PrintWriter os=null;

   // 이 플레이어(클라이언트)가 속한 대기실(대기실) 서버 스레드
   Lobby_Server lobby=null;
   // 이 플레이어(클라이언트)가 속한 룸(게임룸 또는 게임방) 서버 스레드
   GameRoom_Server room=null;

   // 플레이어 정보 저장
   String id=null;
  

   // 내부적으로 유지하는 정보로서, 큰 의미는 없음
   static int instanceUserCount=0;

   // GamePlayer_Server 객체는 Mafia_Server 스레드 객체의 run() 메소드에서 생성한다.
   // 객체 생성자로서, 소켓(socket), 대기실 서버(lobby), 룸 서버(room) 등의 정보가 필요
   // GamePlayer_Server 객체를 생성할 때 룸 서버 정보는 null이다.
   GamePlayer_Server(Socket socket, Lobby_Server lobby, GameRoom_Server room) throws IOException {
      // 플레이언의 아이디 생성
      // 내부적으로 유지하는 데이터로서 의미가 없음
      instanceUserCount++;

     
      // 클라이언트와 연결된 네트워크 소켓으로부터 
      // 데이터를 읽고 쓰기 위한 입출력 스트림 객체를 생성한다.
      is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      os = new PrintWriter(socket.getOutputStream(), true);

      // 예외처리
      if(is == null) {
         throw new IOException();
      }
      if(os == null) {
         throw new IOException();
      }

      // 소켓, 대기실 서버, 룸 서버 등의 정보를 저장
      this.socket = socket;
      this.lobby = lobby;
      this.room = room;
      
   }

   // 클라이언트가 처음 접속하면,
   // 클라이언트가 맞는  클라이언트인지 테스트 하기 위해
   // 처음에 사용자 이름을 확인하기 위한 메시지를 보낸다.
   public void validation() {
      if(is != null) {
         try {
            sendMessage("Request Username"); // 사용자 아이디를 클라이언트에게 요청
            String userInfo = receiveMessage();   // 클라이언트로부터 사용자 아이디를 읽어들임
            StringTokenizer st = new StringTokenizer(userInfo, "|");
            String id = st.nextToken();
            String pwd = st.nextToken();
            getPlayerInfofromDB(id,pwd);
         } catch(SocketException e) {
            clear();    // SocketException 예외가 발생하면 잘못된 연결 설정임
         } catch(IOException e) {
            clear();    // IOException 예외가 발생하면 잘못된 연결 설정임
         }
      }
   }
   
   public void getPlayerInfofromDB(String id,String pwd){
	  //DB로부터 데이터를 읽어온다. use id and pwd
	  this.id = id;
	  	  // sendMessage infomation
	  // 로그인 성공 여부를 알려줘야 한다.
	  // 210|SUCCESS|win_count|lose_count|total_count|get_money|lose_money|total_money
	  // 210|FAIL|REASON(NOTID or NOTPWD)
	 
   }

   // 이 객체가 연결되어 있는 클라이언트에게 주어진 데이터를 전송한다.
   public void sendMessage(String message) {
      if(message != null) {
         os.println(message); // 네트워크 출력 스트림에 데이터를 출력
         os.flush();          // 반드시 flush  해 주어야 안전하다.
         if(os.checkError()) {
            // 출력시에 예외가 발생했다면,
            // 클라이언트와의 네트워크 연결이 끊어진 상태이다.
            // 따라서, 해당 클라이언트를 대기실/룸에서 제거함으로써,
            // 클라이언트와의 연결이 끊겼음을 처리해 주어야 한다.
            lobby.removePlayer(this);
         }
      }
   }

   // 이 객체가 연결되어 있는 클라이언트로부터 온 데이터를 읽어드인다.
   public String receiveMessage() throws SocketException, IOException {
      String message=null;
      try {
         message = is.readLine();   // 클라이언트로부터 한 줄의 데이터를 읽어들임
         if(message == null) {
            // 읽어들인 데이터가 null 일 경우, IOException 예외를 발생시킨다.
            throw(new IOException("Null pointer received...."));
         }
      } catch(SocketException e) {
         // SocketException 예외가 발생하면 그대로 호출한 쪽으로 던진다.
         // 주로 호출한 쪽은 대기실 서버 스레드 또는 룸 서버 스레드이다.
         // SocketException 예외가 발생했다는 것은 클라이언트와의 네트워크가
         // 끊겼다는 것을 의미하므로 이에 대한 처리를 해 주어야 한다.
         throw(e);
      } catch(InterruptedIOException e) {
         // 크게 중요하지 않은 예외로서,
         // 클라이언트와 네트워킹 하는 데는 큰 문제가 없다.
         message = "";
      } catch(IOException e) {
         // IOException 예외가 발생하면 그대로 호출한 쪽으로 던진다.
         // 주로 호출한 쪽은 대기실 서버 스레드 또는 룸 서버 스레드이다.
         // IOException 예외가 발생했다는 것은 클라이언트와의 네트워크가
         // 끊겼다는 것을 의미하므로 이에 대한 처리를 해 주어야 한다.
         throw(e);
      } catch(Exception e) {
         // 크게 중요하지 않은 예외로서,
         // 클라이언트와 네트워킹 하는 데는 큰 문제가 없다.
         message = "";
      }
      return(message);
   }

   // 클라이언트와 연결되어 있는 입출력 스트림을 닫고,
   // 네트워크 연결을 해제한다.
   protected void clear() {
      try {
         if(is != null) {
            is.close();          // 소켓에 대한 입력 스트림을 닫는다.
            is = null;
         }
      } catch(Exception e) {
         System.out.println(e);
         e.printStackTrace();
      }

      try {
         if(os != null) {
            os.close();          // 소켓에 대한 출력 스트림을 닫는다.
            os = null;
         }
      } catch(Exception e) {
         System.out.println(e);
      }

      try {
         if (socket != null) {
            socket.close();      // 소켓을 닫는다.
            socket = null;
         }
      } catch (Exception e) {
         System.out.println(e);
         e.printStackTrace();
      }
   }

   // 담당하고 있는 플레이어에 대한 프로파일에 해당하는 문자열을 리턴해 준다.
   String getProfileMessage(String usrId) {
      String profileMessage=null;

      profileMessage=id+",";
    
      return(profileMessage);
   }

   // Object 클래스에서 정의하고 있는 toString() 메소드를 재정의하여
   // 플레이어 객체애 다한 설명을 제공하는 문자열을 리턴해 준다.
   public String toString() {
      return("Player["+id+"] : Socket["+socket+"], Lobby["+lobby+"], Room["+room+"]");
   }
}