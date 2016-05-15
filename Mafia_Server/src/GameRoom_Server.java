import java.io.IOException;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * �뿡 �ִ� Ŭ���̾�Ʈ(�÷��̾�)���� �����ϱ� ���� ������ ����
 * �뿡���� �� ���� �ִ� Ŭ���̾�Ʈ�� ����  ������ ���൵ �����Ѵ�.
 */

class GameRoom_Server extends Thread {
   Lobby_Server parent;
   //final static int Mafia_NUMBER = 8; // ���Ǿ� ���� ��
   final static int MAX_PARTICIPANT_NUMBER = 8; // �ִ� 8���� ���ӿ� ��������
   final static int MAX_PLAYER_NUMBER = 10; // ���� �ִ� 10�� ���� ����
   int votenum; // ��ǥ �ο� ��
   Vector playerList = null; // �濡 �ִ� �÷��̾� ����Ʈ
   Vector gamingManList = null; // ���� �������� �÷��̾� ����Ʈ
   Vector participantManList = null; // ���� �������� �÷��̾� ����Ʈ
   Vector outplayerList = null; // ������ �÷��̾� ����Ʈ

   GamePlayer_Server master_player; // ���� �÷��̾�

  // Mafia Mafias[];
   int rank[];
   int rank_first;
   int rank_second;
   int ranksum = 0;
   int mafiasum =2;
   String roomId = null; // ���� ���̵�
   boolean onGaming = false; // ���������� ����
   boolean sendLocation = false;

   // �־��� ���̵��� �� ���� ��ü�� �����Ѵ�.
   // �� ��, �־��� �÷��̾ �������� �ȴ�.
   public GameRoom_Server(Lobby_Server parent, String roomId,
         GamePlayer_Server player) {
      this.parent = parent;
      playerList = new Vector(); // �� ���� �ִ� �÷��̾� ����Ʈ
      gamingManList = new Vector(); // ���� ���� �÷��̾� ����Ʈ
      participantManList = new Vector();
      outplayerList = new Vector(); // ���� �߿� ���� �÷��̾� ����Ʈ
      // ���� ���̵� �����ϰ�
      // ���������� �־��� �÷��̾ ���� Ŭ���̾�Ʈ ����Ʈ�� �߰�
      this.roomId = roomId;
      addPlayer(player);
      rank = new int[8];
      for (int i = 0; i < 8; i++) {
         rank[i] = 0;
      }
   }

   // ���ǿ��� ������ ���� Ŭ���̾�Ʈ�� �߰��Ѵ�.
   public void addPlayer(GamePlayer_Server player) {
      if (!playerList.contains(player)) {
         player.room = this; // Ŭ���̾�Ʈ�� �� ���� ����
         playerList.addElement(player); // Ŭ���̾�Ʈ ����Ʈ�� �߰�
         String msg;
       
         
      }
   }

   // �־��� ���̵� �ش��ϴ� Ŭ���̾�Ʈ�� ������ ��´�.
   public GamePlayer_Server getPlayer(String playerId) {
      GamePlayer_Server player = null;
      Enumeration e = playerList.elements();
      while (e.hasMoreElements()) {
         player = (GamePlayer_Server) e.nextElement();
         if (player.id.equals(playerId)) {
            return (player);
         }
      }
      return (null);
   }

   // �־��� Ŭ���̾�Ʈ�� �뿡�� �����Ѵ�.
   // �� ��, Ŭ���̾�Ʈ�� �ܼ��� �� ���� �ִ���,
   // �Ǵ� �����߿� �ִ����� ���� �ٸ��� ó���� �־�� �Ѵ�.
   synchronized public void removePlayer(GamePlayer_Server player) {
      if (player == null) {
         return;
      }
      // ����, �÷��̾� ������ �����Ѵ�.
      player.room = null; // �� ������ ����, null�� ����
      player.lobby.takePlayer(player); // Ŭ���̾�Ʈ�� �뿡�� ���Ƿ� �̵�
      playerList.removeElement(player); // Ŭ���̾�Ʈ�� �뿡�� ����

      // ������ Ŭ���̾�Ʈ�� �����̸� �ٸ� ������ ������ �����Ѵ�.
      if (master_player == player) {
         if (!playerList.isEmpty()) {
            GamePlayer_Server bangjang;
            bangjang = (GamePlayer_Server) playerList.elementAt(0);
            master_player = bangjang;
            sendMessage(master_player.id, "454|MASTER");
         }
      }

      if (gamingManList.contains(player)) {
         // ����, ���������� Ŭ���̾�Ʈ�� �����߿� ���� ��쿡��
         // ������ ������ ����.
         if (onGaming == true) {
            outplayerList.addElement(player);// �߰��� ���� �÷��̾� ����Ʈ�� �����Ѵ�.
            gamingManList.removeElement(player);
         }
      }

      if (playerList.size() == 0) {
         // ������ �����Ѵ�.
         if (onGaming == true) {
            finishGame();
         }
         // �÷��̾ ������ ��, �뿡 �ƹ��� ���ٸ� ���� �����ؾ� �Ѵ�.
         player.lobby.removeRoom(roomId);
      }

      // �� ���� �����������Ƿ� �÷��̾� ����Ʈ�� �ٽ� �˷��ش�.
      broadcastUserList();
   }

   // �־��� �÷��̾ ���� �����߿� �ִ��� ���θ� ����
   boolean inGaming(GamePlayer_Server player) {
      if (playerList.size() == 0) {
         return (false);
      }
      if (gamingManList.contains(player)) {
         return (true);
      }
      return (false);
   }

   // �־��� �÷��̾�� �÷��̾� ����Ʈ�� ������ �ش�.
   void sendUserList(GamePlayer_Server toPlayer) {
      String message = null;
      GamePlayer_Server player = null;

      // �� ���� �ƹ��� ���� ��쿡�� �׳� �����Ѵ�.
      if (playerList.size() == 0) {
         return;
      }

      // ������ ����Ʈ�� ������ ���� �޽��� �ʱ�ȭ
      message = "440|";

      Enumeration e = playerList.elements();
      while (e.hasMoreElements()) {
         player = (GamePlayer_Server) e.nextElement();
         message += player.id + ","; // ���� ������ ǥ��
      }

      // ����Ʈ�� ���� '.' �� ���� ��쿡�� �ݵ�� ������ �־�� �Ѵ�.
      if (message.endsWith(",")) {
         message = message.substring(0, message.length() - 1);
      }

      // �־��� �÷��̾�� �� ���� �÷��̾� ����Ʈ�� ���� �޽����� ������.
      toPlayer.sendMessage(message);
   }

   // �־��� �÷��̾�� �÷��̾� ����Ʈ�� ������ �ش�.
   void broadcastUserList() {
      String message = null;
      GamePlayer_Server player = null;

      // �� ���� �ƹ��� ���� ��쿡�� �׳� �����Ѵ�.
      if (playerList.size() == 0) {
         return;
      }

      Enumeration e = playerList.elements();
      while (e.hasMoreElements()) {
         player = (GamePlayer_Server) e.nextElement();
         sendUserList(player);
      }
   }

   // ������ �����Ѵ�.
   synchronized public void startGame() {
      // rank_index=1;
      // for(int j=0;j<8;j++){
      // rank[j]=0;
      // }
      // onGaming = true;
      String message = null;
      String message2 = null;
      // sendLocation=true;
      //
      // long seed=Calendar.getInstance().getTime().getTime(); //random�Լ��� ��
      // seed�� ����
      // Random random=new Random(seed);
      //
      // Mafias = new Mafia[8];
      // for(int i=0;i<8;i++) {
      // int speed=random.nextInt(); //speed�� �ʱ⿡ �����Ǹ� ������ ����. ����� speed�� ����
      // �����ɼ��� ���� �޸��� ���� �ӵ��� ������.(sleep(speed) ���� )
      // if(speed<=0){
      // speed=-speed+1; //speed�� 0���� ū ���
      // }
      // speed=(speed%50)-40; //speed�� 10,20,30,40,~~~,90
      //
      // Mafias[i] = new Mafia(300,speed);
      // Mafias[i].start();
      // }

      int[] arrayNumber = { 0, 0, 0, 0, 0, 0, 0, 0 };
      int countmafia = 0;
      int countpolice = 0;
      int countdoctor = 0;
      int randjob;
      int randper;
      while (true) {
         randjob = (int) (Math.random() * 3) + 1; // ����8�� 1����8��������
         randper = (int) (Math.random() * 8);

         if (countmafia == 2 && countpolice == 1 && countdoctor == 1)
            break;

         if (countmafia == 2 && randjob == 1)
            continue;
         else if (countpolice == 1 && randjob == 2)
            continue;
         else if (countdoctor == 1 && randjob == 3)
            continue;

         if (arrayNumber[randper] == 0) {
            arrayNumber[randper] = randjob;
            if (randjob == 1)
               countmafia++;
            else if (randjob == 2)
               countpolice++;
            else if (randjob == 3)
               countdoctor++;
         }

      }
      message = "451|START";
      for (int i = 0; i < arrayNumber.length; i++) {
         message2 = "|" + arrayNumber[i];
         message += message2;
      }
      // ������ �����ϵ��� �޽����� ������.
      broadcastMessage(message);

      // server message state for test
      message = "������ �����Ͽ����ϴ�.\n";
      parent.parent.server_state.roomArea.append(message);

   }

   // ������ ������ �κ��̴�.
   synchronized public void finishGame() {
  
      onGaming = false;

      // ����Ǿ��ٴ� �޽����� ������.
      String message = "460|END";
      broadcastMessage(message);

      // server message state for test
      message = "������ �������ϴ�.\n";
      parent.parent.server_state.roomArea.append(message);

      // server message state for test
      message = "\n ������ ������ ������ �����ϴ�.\n";
      parent.parent.server_state.roomArea.append(message);

      // server message state for test
      for (int i = 0; i < 8; i++) {
         String m = (i + 1) + "�� ���� " + rank[i] + "������ ���Խ��ϴ�.\n";
         parent.parent.server_state.roomArea.append(m);
      }

      // server message state for test
      parent.parent.server_state.roomArea.append("\n");

     
      gamingManList.removeAllElements();
      participantManList.removeAllElements();
      master_player = null;
   }

 

   

   // ���� ���� �޽����� ���� ó���� �ϴ� �κ��̴�.
   public void processJoin(String playerId) {
      String message;
      GamePlayer_Server player = null;
      player = getPlayer(playerId);

      if (player != null) {
         if ((!participantManList.contains(player))) { // ���� ���� ��Ͽ� ���ԵǾ� ����
                                             // �ʴٸ�...
            if (participantManList.size() < MAX_PARTICIPANT_NUMBER) { // ����
                                                         // ��������
                                                         // �ʰ�����
                                                         // �ʾҴٸ�...
               participantManList.addElement(player);
               message = "420|" + playerId + "|SUCCESS";
               broadcastMessage(message);
               if (master_player == null) {// ������ ���ٸ�
                  master_player = player;// �������� ��ؽ�Ű��
                  sendMessage(master_player.id, "454|MASTER");// ������ �Ǿ�����
                                                   // �˸���.
               }
            } else { // ���� ������ ���� �ʰ��Ͽ��ٸ�...
               message = "420|" + playerId + "|MAX_OVER";// �ʰ� �޽����� ������.
               sendMessage(playerId, message);
            }
         } else { // ���� ��Ͽ� �̹� ���� �Ǿ� �ִٸ�...
            if (master_player == player) {// �����̶��
               participantManList.removeElement(player);
               if (!participantManList.isEmpty()) {// �����ڰ� �ִٸ�
                  Enumeration e = participantManList.elements();
                  GamePlayer_Server bangjang;
                  bangjang = (GamePlayer_Server) e.nextElement();
                  master_player = bangjang;// ���� �ƹ��� ������ �̰�
                  sendMessage(master_player.id, "454|MASTER");// ������ �Ǿ�����
                                                   // �˸���.
                  if (gamingManList.contains(player)) {// ��û�� ����ڴ� ���� ����
                                                // ��Ų��.
                     gamingManList.removeElement(player);
                  }
               } else {
                  master_player = null;
               }
               message = "420|" + playerId + "|FAIL";
               sendMessage(playerId, message);
            } else {// ������ �ƴ϶�� �׳� ���� ���� ��Ű�� �ȴ�.
               participantManList.removeElement(player);
               message = "420|" + playerId + "|FAIL";
               sendMessage(playerId, message);
               if (gamingManList.contains(player)) {
                  gamingManList.removeElement(player);
               }
            }
         }
      }
   }

   // ���� �޽����� ó���ϴ� �κ��̴�.
   public void processReady(String playerId) {
      String message;
      GamePlayer_Server player = null;
      player = getPlayer(playerId);

      if (player == master_player) {// �����̶�� �̴� �� ���� ������ �ǹ��Ѵ�.
         gamingManList.addElement(player);
         if (gamingManList.containsAll(participantManList)) {
        	 if(!(votenum==8))
        		 votenum++;
            parent.parent.server_state.roomArea.append("��ǥ�� �� �ο� " +votenum + "\n");
            startGame();
         }
      } else {// ������ �ƴ϶��
         if (!gamingManList.contains(player)) {// ������ ���Խ�Ű��
            gamingManList.addElement(player);
            message = "453|READY|DISABLE";
            if(!(votenum==8))
       		 votenum++;
            sendMessage(player.id, message);
         } else {// �ִٸ� ������Ų��.
            gamingManList.removeElement(player);
            message = "453|READY|ENABLE";
            if(!(votenum==8))
       		 votenum++;
            sendMessage(player.id, message);
         }
      }
   }

   // �־��� �÷��̾�� �޽����� ������.
   public void sendMessage(String playerId, String message) {
      GamePlayer_Server player = null;

      // Ŭ���̾�Ʈ ����Ʈ���� �־��� ���̵� ���� GamePlayer_Server ��ü�� ��´�.
      player = getPlayer(playerId);
      if (player != null) {
         // �ش� �÷��̾�� �޽����� ������.
         player.sendMessage(message);
      }
   }

   // �־��� �޽����� �� ���� ��� ����鿡�� ��ε�ĳ���� �Ѵ�.
   public void broadcastMessage(String message) {
      GamePlayer_Server player = null;
      if (message == null) {
         return;
      }
      for (int i = 0; i < playerList.size(); i++) {
         player = (GamePlayer_Server) playerList.elementAt(i);
         player.sendMessage(message);
      }
   }

   // �� ���� �������� �ٵ�μ�
   // �뿡 �ִ� Ŭ���̾�Ʈ(�÷��̾�)���� �����ϱ� ���� ������ ����
   // �뿡���� �� ���� �ִ� Ŭ���̾�Ʈ�� ���� ������ ���൵ �����Ѵ�.
   public void run() {
      GamePlayer_Server player=null;

      // �� ���� ������ ��ü�� �� ���� Ŭ���̾�Ʈ ����Ʈ�� ��ϵǾ� �ִ� 
      // Ŭ���̾�Ʈ��κ��� �� �޽����� �ֱ������� üũ�Ͽ� �о�´�.
      // ����, ������μ� �����ϸ�, ���ѷ����� ���´�.
      while(true) {         
         // Read Data
         try {
            // CPU ����(�Ǵ� �������)�ϴ� �����尡 �Ǵ� ���� �����ϱ� ���� 
            // �����̸� �ش�.
            Thread.sleep(80);
         } catch(InterruptedException ie) {
         }
        
         // send Mafia location
        
            
         // Ŭ���̾�Ʈ ����Ʈ�� ��ϵǾ� �ִ� Ŭ���̾�Ʈ ��ü�� �ϳ��� ��������
         // ���� playerList �ؽ����̺�κ��� Enumeration ��ü�� ��´�.
         for(int i=0;i<playerList.size();i++) {
            // Enumeration ��ü�κ��� �ϳ��� Ŭ���̾�Ʈ ��ü�� ���´�. 
            player = (GamePlayer_Server)playerList.elementAt(i);
            try {
               String receive = null;

// �� Ŭ���̾�Ʈ�κ��� �����͸� �о�´�.
// �� ��, �� Ŭ���̾�Ʈ�κ��� �����Ͱ� �� ������
// ������ ��ٸ���(��ŷ) ���� �ƴ϶�
// SO_TIME �� ������ �ð�(10msec)�� ������
// ���Ͽ��� �о���̱� ���� ��ŷ �� ���� ���������� �ȴ�.
// �׷�����, ���� Ŭ���̾�Ʈ�κ��� �����͸� �о���� �� �ִ�.
               receive = player.receiveMessage();

// player.receiveMessage() �޼ҵ��� ���� ����� ������ ����.
//   1) Ŭ���̾�Ʈ�� ���� ���: SocketException or IOException ���� �߻�
//                              try {
//                                 ...
//                              } catch(SocketException e) {
//                                 �� �κп��� �ش� ���� ó��
//                                   - ����Ʈ���� �����Ѵ�.
//                              } catch(IOException e) {
//                                 �� �κп��� �ش� ���� ó��
//                                   - ����Ʈ���� �����Ѵ�.
//                              }
//   2) null �Ǵ� "" �� ����: Ŭ���̾�Ʈ�κ��� �� �����Ͱ� ���ų�
//                            �Ǵ�, SO_TIME�� ������ �ð��� ���� ���
//   3) ���ڿ� ������: Ŭ���̾�Ʈ�κ��� ���������� �����͸� ���� ���

// ������ ���� �� ���� ��쿡 ���� ó���ϴ� ���̴�.

// 1) ���� ���� ó���� �� �Ʒ��� �ִ� catch(...) ������ ó���Ѵ�.
// 2) Ŭ���̾�Ʈ�κ��� ���޵� �����Ͱ� ���� ���
               if(receive == null || "".equals(receive)) {
                  continue;
               }

// 3) Ŭ���̾�Ʈ�κ��� �����͸� ���������� �о���� ���
//    �������ݿ� ���� �����͸� ó���Ѵ�.

               StringTokenizer st = new StringTokenizer(receive, "|");
               int protocol = Integer.parseInt(st.nextToken());
               String message=null;
               switch(protocol){
               case 300:{
                  // ��ε�ĳ��Ʈ ä��
                  // 300|fromId|chatMessage
                   String fromId = st.nextToken();
                   String chatStr = st.nextToken();
                   message = "400|"+fromId+"|"+chatStr;
                   broadcastMessage(message);
               } break;
               case 301:{
                  // ����ĳ��Ʈ ä��
                  // 301|fromId|toId|chatMessage
                   String fromId = st.nextToken();
                   String toId = st.nextToken();
                   String chatStr = st.nextToken();
                   message = "401|"+fromId+"|"+chatStr;
                   sendMessage(toId,message);
               } break;
               case 310:{
                  // �� ����
                   // 310|fromID
                   String fromId = st.nextToken();

                   player = getPlayer(fromId);
                   if(player != null) {
                      message = "410|"+fromId;
                      broadcastMessage(message);
                      
                      // ���� ������ ���θ� ���� ���� ��ư Ȱ��ȭ ���θ� �˸���.
                         if(onGaming) {
                            message = "452|PARTICIPANT|DISABLE";
                            sendMessage(fromId,message);
                         } else {
                            message = "452|PARTICIPANT|ENABLE";
                            sendMessage(fromId,message);
                         }
                      player.sendMessage(message);  
                      
                      if(master_player == player){
                            sendMessage(fromId,"454|MASTER");
                      }
                   }
                   broadcastUserList();
               } break;
               case 320:{
                  // ���ӿ� �����ϱ�
                   // 320|fromId
                   String fromId = st.nextToken();
                   processJoin(fromId);
               } break;
               case 330:{
                  // Ŭ���̾�Ʈ ����(�κ�� �̵�)
                   // 330|fromId
                   String fromId = st.nextToken();
                   player = getPlayer(fromId);
                   if(player != null) {
                      player.lobby.takePlayer(player);
                      removePlayer(player);
                      message = "430|"+fromId;
                      broadcastMessage(message);
                   }
                   broadcastUserList();
               } break;
               case 331:{
                  // Ŭ���̾�Ʈ ����
                   // 331|fromId
                   String fromId = st.nextToken();
                   player = getPlayer(fromId);
                   if(player != null) {
                      removePlayer(player);
                      message = "430|"+fromId;
                      broadcastMessage(message);
                   }
                   broadcastUserList();
               } break;
               case 340:{
                  // Ŭ���̾�Ʈ ����Ʈ ��û
                   // 340|fromId
                   String fromId = st.nextToken();
                   player = getPlayer(fromId);
                   if(player != null) {
                      sendUserList(player);
                   }
               } break;
               case 350:{
                  // ���� ���� ��û
                  // 350|fromId
                  String fromId = st.nextToken();
                  processReady(fromId);
               } break;
               
               case 357:{ // ���θ��� ��Ʈ��ȣ �����ؾ��� ��Ʈ ��ȣ
                  // 357|id|��ȣ 0~7 Jlabel ��
                  //���� ���� ��ǥ ���� ��� Client���� �˷���
                  String fromid = st.nextToken();
                  String fromNum = st.nextToken();
                  
                  String msg = "400|" + fromid + "|�Բ��� " + (Integer.parseInt(fromNum)+1) +"�� �÷��̾ ��ǥ �ϼ̽��ϴ�.";
                  broadcastMessage(msg);
                  
                  
                  
                  if(Integer.parseInt(fromNum) == 0)
                  { rank[0]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 1)
                  { rank[1]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 2)
                  { rank[2]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 3)
                  { rank[3]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 4)
                  { rank[4]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 5)
                  { rank[5]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 6)
                  { rank[6]++; ranksum ++; }
                  else if(Integer.parseInt(fromNum) == 7)
                  { rank[7]++; ranksum ++; }
                  
                  parent.parent.server_state.roomArea.append(msg+ "\n");
                  for(int k=0; k<8;k++)
                  {
                     parent.parent.server_state.roomArea.append((k+1)+"��° ǥ�� "+rank[k]+ "\n");
                  }
                  parent.parent.server_state.roomArea.append("�� ��ǥ �� " +ranksum+ "\n");
                  
                  if(ranksum == votenum) // �� ��ǥ���� 8�� ��
                  {
                     if (rank[0] > rank[1]) 
                     {
                        rank_first = 0;
                        rank_second = 1;
                     } 
                     else 
                     {
                        rank_first = 1;
                        rank_second = 0;
                     }

                     for (int k = 2; k < 8; k++) 
                     {
                        if (rank[k] > rank[rank_second]) 
                        {
                           if (rank[k] > rank[rank_first]) 
                           {
                              rank_second = rank_first;
                              rank_first = k;
                           } 
                           else 
                           {
                              rank_second = k;
                           }
                        }
                     }
                     String msg2 = "";
                     if(rank[rank_first] == rank[rank_second])
                     {
                        msg2 = "457|NODEATH"; //���� 1,2�� ��ǥ���� ���ٸ� �״»���� ����.
                        ranksum=0;
                         parent.parent.server_state.roomArea.append("���� ��ǥ�� �� �ο� " +votenum + "\n");
                         for(int j=0; j<8; j++)
                            rank[j] = 0;
                     }
                     else
                     {
                        String firstpara = String.valueOf(rank_first); //1�� ��ǥ�� ���ڰ� ���� 0~7
                        msg2 = "457|" + firstpara;
                        
                        ranksum=0;
                         votenum--;
                         parent.parent.server_state.roomArea.append("���� ��ǥ�� �� �ο� " +votenum + "\n");
                         for(int j=0; j<8; j++)
                            rank[j] = 0;
                     }
                     
                     broadcastMessage(msg2);
                     parent.parent.server_state.roomArea.append(msg2 + "\n");
                     
                     
                     break;
                  }
                  
               }break;
               case 358:{
                   //358|id|0~7
                   //358|mafiaid|0~7
                   //358|doctor|0~7
                   //358|police|0~7
                   String fromid = st.nextToken();
                   String fromNum = st.nextToken();
                   
                   String msg2 = "458|"+fromid+"|" + fromNum;
                   broadcastMessage(msg2);
                   parent.parent.server_state.roomArea.append(msg2 + "\n");
                   
                }break;
               case 359:
               {
            	   //359|num-
            	   String frommsg = st.nextToken();
            	   votenum--;
               }break;
               case 370:{
                  // ����� ���� ��û
                  // 370|fromId
                  String msg;
                   
               } break;
               case 380:{
                  
                  // 380|fromId|x|x|x|x|x|x|x|x
                  String fromId = st.nextToken();
                  player = getPlayer(fromId);
                 
                 
               } break;
               }
            } catch(SocketException e) { //Stream doesn't exist.
               // 1) ���� ���� ���� ó���Ѵ�.
               // Ŭ���̾�Ʈ���� ��Ʈ��ŷ�� ���� ����̹Ƿ�,
               // �ݵ�� �ش� Ŭ���̾�Ʈ�� ���ǿ��� �����ؾ� �Ѵ�.
               removePlayer(player);
            } catch(IOException ne) {
               // 1) ���� ���� ���� ó���Ѵ�.
               // Ŭ���̾�Ʈ���� ��Ʈ��ŷ�� ���� ����̹Ƿ�,
               // �ݵ�� �ش� Ŭ���̾�Ʈ�� ���ǿ��� �����ؾ� �Ѵ�.
               removePlayer(player);
            }
         }
      }
   }
}