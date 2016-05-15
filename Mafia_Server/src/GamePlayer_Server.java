import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

/*
 *   Ŭ���̾�Ʈ�� 1:1 ��Ʈ��ŷ�� ����ϱ� ���� ����� ������ �ش�.
 *   ���� ����, �Ǵ� �� �������� Ŭ���̾�Ʈ���� �޽����� �����ų�,
 *   �Ǵ� �޾Ƶ��̱� ���� GamePlayer_Server ��ü�� �̿��Ѵ�.
 *   ������ �����ϸ�,
 *   Mafia_Server ������ ��ü���� �ش� Ŭ���Ʈ�� ��Ʈ��ŷ�� ����
 *   ������ ������� GamePlayer_Server ��ü�� �����Ѵ�.
 *   GamePlayer_Server ��ü���� ����, ���� ����, �� ���� ���� ������ ������ �ִ�.
 */

class GamePlayer_Server {
   // Ŭ���̾�Ʈ�� ������ ���� �����͸� �ְ� �ޱ� ����, ��Ʈ��ŷ ������ ���� ���� ��ü
   public Socket socket=null;
   // ������ ���� Ŭ���̾�Ʈ�κ��� �����͸� �о���̱� ���� �Է� ��Ʈ��
   public BufferedReader is=null;
   // ������ ���� �����͸� Ŭ���̾�Ʈ�� ������ ���� ��Ʈ��
   public PrintWriter os=null;

   // �� �÷��̾�(Ŭ���̾�Ʈ)�� ���� ����(����) ���� ������
   Lobby_Server lobby=null;
   // �� �÷��̾�(Ŭ���̾�Ʈ)�� ���� ��(���ӷ� �Ǵ� ���ӹ�) ���� ������
   GameRoom_Server room=null;

   // �÷��̾� ���� ����
   String id=null;
  

   // ���������� �����ϴ� �����μ�, ū �ǹ̴� ����
   static int instanceUserCount=0;

   // GamePlayer_Server ��ü�� Mafia_Server ������ ��ü�� run() �޼ҵ忡�� �����Ѵ�.
   // ��ü �����ڷμ�, ����(socket), ���� ����(lobby), �� ����(room) ���� ������ �ʿ�
   // GamePlayer_Server ��ü�� ������ �� �� ���� ������ null�̴�.
   GamePlayer_Server(Socket socket, Lobby_Server lobby, GameRoom_Server room) throws IOException {
      // �÷��̾��� ���̵� ����
      // ���������� �����ϴ� �����ͷμ� �ǹ̰� ����
      instanceUserCount++;

     
      // Ŭ���̾�Ʈ�� ����� ��Ʈ��ũ �������κ��� 
      // �����͸� �а� ���� ���� ����� ��Ʈ�� ��ü�� �����Ѵ�.
      is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      os = new PrintWriter(socket.getOutputStream(), true);

      // ����ó��
      if(is == null) {
         throw new IOException();
      }
      if(os == null) {
         throw new IOException();
      }

      // ����, ���� ����, �� ���� ���� ������ ����
      this.socket = socket;
      this.lobby = lobby;
      this.room = room;
      
   }

   // Ŭ���̾�Ʈ�� ó�� �����ϸ�,
   // Ŭ���̾�Ʈ�� �´�  Ŭ���̾�Ʈ���� �׽�Ʈ �ϱ� ����
   // ó���� ����� �̸��� Ȯ���ϱ� ���� �޽����� ������.
   public void validation() {
      if(is != null) {
         try {
            sendMessage("Request Username"); // ����� ���̵� Ŭ���̾�Ʈ���� ��û
            String userInfo = receiveMessage();   // Ŭ���̾�Ʈ�κ��� ����� ���̵� �о����
            StringTokenizer st = new StringTokenizer(userInfo, "|");
            String id = st.nextToken();
            String pwd = st.nextToken();
            getPlayerInfofromDB(id,pwd);
         } catch(SocketException e) {
            clear();    // SocketException ���ܰ� �߻��ϸ� �߸��� ���� ������
         } catch(IOException e) {
            clear();    // IOException ���ܰ� �߻��ϸ� �߸��� ���� ������
         }
      }
   }
   
   public void getPlayerInfofromDB(String id,String pwd){
	  //DB�κ��� �����͸� �о�´�. use id and pwd
	  this.id = id;
	  	  // sendMessage infomation
	  // �α��� ���� ���θ� �˷���� �Ѵ�.
	  // 210|SUCCESS|win_count|lose_count|total_count|get_money|lose_money|total_money
	  // 210|FAIL|REASON(NOTID or NOTPWD)
	 
   }

   // �� ��ü�� ����Ǿ� �ִ� Ŭ���̾�Ʈ���� �־��� �����͸� �����Ѵ�.
   public void sendMessage(String message) {
      if(message != null) {
         os.println(message); // ��Ʈ��ũ ��� ��Ʈ���� �����͸� ���
         os.flush();          // �ݵ�� flush  �� �־�� �����ϴ�.
         if(os.checkError()) {
            // ��½ÿ� ���ܰ� �߻��ߴٸ�,
            // Ŭ���̾�Ʈ���� ��Ʈ��ũ ������ ������ �����̴�.
            // ����, �ش� Ŭ���̾�Ʈ�� ����/�뿡�� ���������ν�,
            // Ŭ���̾�Ʈ���� ������ �������� ó���� �־�� �Ѵ�.
            lobby.removePlayer(this);
         }
      }
   }

   // �� ��ü�� ����Ǿ� �ִ� Ŭ���̾�Ʈ�κ��� �� �����͸� �о���δ�.
   public String receiveMessage() throws SocketException, IOException {
      String message=null;
      try {
         message = is.readLine();   // Ŭ���̾�Ʈ�κ��� �� ���� �����͸� �о����
         if(message == null) {
            // �о���� �����Ͱ� null �� ���, IOException ���ܸ� �߻���Ų��.
            throw(new IOException("Null pointer received...."));
         }
      } catch(SocketException e) {
         // SocketException ���ܰ� �߻��ϸ� �״�� ȣ���� ������ ������.
         // �ַ� ȣ���� ���� ���� ���� ������ �Ǵ� �� ���� �������̴�.
         // SocketException ���ܰ� �߻��ߴٴ� ���� Ŭ���̾�Ʈ���� ��Ʈ��ũ��
         // ����ٴ� ���� �ǹ��ϹǷ� �̿� ���� ó���� �� �־�� �Ѵ�.
         throw(e);
      } catch(InterruptedIOException e) {
         // ũ�� �߿����� ���� ���ܷμ�,
         // Ŭ���̾�Ʈ�� ��Ʈ��ŷ �ϴ� ���� ū ������ ����.
         message = "";
      } catch(IOException e) {
         // IOException ���ܰ� �߻��ϸ� �״�� ȣ���� ������ ������.
         // �ַ� ȣ���� ���� ���� ���� ������ �Ǵ� �� ���� �������̴�.
         // IOException ���ܰ� �߻��ߴٴ� ���� Ŭ���̾�Ʈ���� ��Ʈ��ũ��
         // ����ٴ� ���� �ǹ��ϹǷ� �̿� ���� ó���� �� �־�� �Ѵ�.
         throw(e);
      } catch(Exception e) {
         // ũ�� �߿����� ���� ���ܷμ�,
         // Ŭ���̾�Ʈ�� ��Ʈ��ŷ �ϴ� ���� ū ������ ����.
         message = "";
      }
      return(message);
   }

   // Ŭ���̾�Ʈ�� ����Ǿ� �ִ� ����� ��Ʈ���� �ݰ�,
   // ��Ʈ��ũ ������ �����Ѵ�.
   protected void clear() {
      try {
         if(is != null) {
            is.close();          // ���Ͽ� ���� �Է� ��Ʈ���� �ݴ´�.
            is = null;
         }
      } catch(Exception e) {
         System.out.println(e);
         e.printStackTrace();
      }

      try {
         if(os != null) {
            os.close();          // ���Ͽ� ���� ��� ��Ʈ���� �ݴ´�.
            os = null;
         }
      } catch(Exception e) {
         System.out.println(e);
      }

      try {
         if (socket != null) {
            socket.close();      // ������ �ݴ´�.
            socket = null;
         }
      } catch (Exception e) {
         System.out.println(e);
         e.printStackTrace();
      }
   }

   // ����ϰ� �ִ� �÷��̾ ���� �������Ͽ� �ش��ϴ� ���ڿ��� ������ �ش�.
   String getProfileMessage(String usrId) {
      String profileMessage=null;

      profileMessage=id+",";
    
      return(profileMessage);
   }

   // Object Ŭ�������� �����ϰ� �ִ� toString() �޼ҵ带 �������Ͽ�
   // �÷��̾� ��ü�� ���� ������ �����ϴ� ���ڿ��� ������ �ش�.
   public String toString() {
      return("Player["+id+"] : Socket["+socket+"], Lobby["+lobby+"], Room["+room+"]");
   }
}