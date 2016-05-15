import java.io.IOException;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * 룸에 있는 클라이언트(플레이어)들을 관리하기 위한 스레드 서버
 * 룸에서는 룸 내에 있는 클라이언트는 물론  게임의 진행도 관리한다.
 */

class GameRoom_Server extends Thread {
   Lobby_Server parent;
   //final static int Mafia_NUMBER = 8; // 마피아 서의 수
   final static int MAX_PARTICIPANT_NUMBER = 8; // 최대 8명이 게임에 참여가능
   final static int MAX_PLAYER_NUMBER = 10; // 방은 최대 10명 입장 가능
   int votenum; // 투표 인원 수
   Vector playerList = null; // 방에 있는 플레이어 리스트
   Vector gamingManList = null; // 게임 레디중인 플레이어 리스트
   Vector participantManList = null; // 게임 참가중인 플레이어 리스트
   Vector outplayerList = null; // 도망간 플레이어 리스트

   GamePlayer_Server master_player; // 방장 플레이어

  // Mafia Mafias[];
   int rank[];
   int rank_first;
   int rank_second;
   int ranksum = 0;
   int mafiasum =2;
   String roomId = null; // 룸의 아이디
   boolean onGaming = false; // 게임중인지 여부
   boolean sendLocation = false;

   // 주어진 아이디의 룸 서버 객체를 생성한다.
   // 이 때, 주어진 플레이어가 방주인이 된다.
   public GameRoom_Server(Lobby_Server parent, String roomId,
         GamePlayer_Server player) {
      this.parent = parent;
      playerList = new Vector(); // 룸 내에 있는 플레이어 리스트
      gamingManList = new Vector(); // 게임 중인 플레이어 리스트
      participantManList = new Vector();
      outplayerList = new Vector(); // 게임 중에 나간 플레이어 리스트
      // 룸의 아이디를 설정하고
      // 방주인으로 주어진 플레이어를 룸의 클라이언트 리스트에 추가
      this.roomId = roomId;
      addPlayer(player);
      rank = new int[8];
      for (int i = 0; i < 8; i++) {
         rank[i] = 0;
      }
   }

   // 대기실에서 룸으로 들어온 클라이언트를 추가한다.
   public void addPlayer(GamePlayer_Server player) {
      if (!playerList.contains(player)) {
         player.room = this; // 클라이언트의 룸 정보 설정
         playerList.addElement(player); // 클라이언트 리스트에 추가
         String msg;
       
         
      }
   }

   // 주어진 아이디에 해당하는 클라이언트의 정보를 얻는다.
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

   // 주어진 클아이언트를 룸에서 제거한다.
   // 이 때, 클라이언트가 단순히 룸 내에 있는지,
   // 또는 게임중에 있는지에 따라 다르게 처리해 주어야 한다.
   synchronized public void removePlayer(GamePlayer_Server player) {
      if (player == null) {
         return;
      }
      // 먼저, 플레이어 정보를 갱신한다.
      player.room = null; // 룸 정보를 제거, null로 설정
      player.lobby.takePlayer(player); // 클라이언트를 룸에서 대기실로 이동
      playerList.removeElement(player); // 클라이언트를 룸에서 제거

      // 나가는 클라이언트가 방장이면 다른 임의의 방장을 선택한다.
      if (master_player == player) {
         if (!playerList.isEmpty()) {
            GamePlayer_Server bangjang;
            bangjang = (GamePlayer_Server) playerList.elementAt(0);
            master_player = bangjang;
            sendMessage(master_player.id, "454|MASTER");
         }
      }

      if (gamingManList.contains(player)) {
         // 만약, 빠져나가는 클라이언트가 게임중에 있을 경우에는
         // 문제가 복잡해 진다.
         if (onGaming == true) {
            outplayerList.addElement(player);// 중간에 나간 플레이어 리스트를 유지한다.
            gamingManList.removeElement(player);
         }
      }

      if (playerList.size() == 0) {
         // 세션을 종료한다.
         if (onGaming == true) {
            finishGame();
         }
         // 플레이어를 제거한 후, 룸에 아무도 없다면 룸을 삭제해야 한다.
         player.lobby.removeRoom(roomId);
      }

      // 한 명이 빠져나갔으므로 플레이어 리스트를 다시 알려준다.
      broadcastUserList();
   }

   // 주어진 플레이어가 현재 게임중에 있는지 여부를 리턴
   boolean inGaming(GamePlayer_Server player) {
      if (playerList.size() == 0) {
         return (false);
      }
      if (gamingManList.contains(player)) {
         return (true);
      }
      return (false);
   }

   // 주어진 플레이어에게 플레이어 리스트를 전달해 준다.
   void sendUserList(GamePlayer_Server toPlayer) {
      String message = null;
      GamePlayer_Server player = null;

      // 룸 내에 아무도 없을 경우에는 그냥 리턴한다.
      if (playerList.size() == 0) {
         return;
      }

      // 참가자 리스트를 보내기 위한 메시지 초기화
      message = "440|";

      Enumeration e = playerList.elements();
      while (e.hasMoreElements()) {
         player = (GamePlayer_Server) e.nextElement();
         message += player.id + ","; // 현재 참가자 표시
      }

      // 리스트의 끝에 '.' 가 있을 경우에는 반드시 제거해 주어야 한다.
      if (message.endsWith(",")) {
         message = message.substring(0, message.length() - 1);
      }

      // 주어진 플레이어에게 룸 내의 플레이어 리스트에 대한 메시지를 보낸다.
      toPlayer.sendMessage(message);
   }

   // 주어진 플레이어에게 플레이어 리스트를 전달해 준다.
   void broadcastUserList() {
      String message = null;
      GamePlayer_Server player = null;

      // 룸 내에 아무도 없을 경우에는 그냥 리턴한다.
      if (playerList.size() == 0) {
         return;
      }

      Enumeration e = playerList.elements();
      while (e.hasMoreElements()) {
         player = (GamePlayer_Server) e.nextElement();
         sendUserList(player);
      }
   }

   // 게임을 시작한다.
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
      // long seed=Calendar.getInstance().getTime().getTime(); //random함수에 들어갈
      // seed값 추출
      // Random random=new Random(seed);
      //
      // Mafias = new Mafia[8];
      // for(int i=0;i<8;i++) {
      // int speed=random.nextInt(); //speed는 초기에 생성되면 변하지 않음. 참고로 speed가 높게
      // 측정될수록 실제 달리는 말의 속도는 느리다.(sleep(speed) 때문 )
      // if(speed<=0){
      // speed=-speed+1; //speed는 0보다 큰 양수
      // }
      // speed=(speed%50)-40; //speed는 10,20,30,40,~~~,90
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
         randjob = (int) (Math.random() * 3) + 1; // 직벙8개 1에서8까지선택
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
      // 게임을 시작하도록 메시지를 보낸다.
      broadcastMessage(message);

      // server message state for test
      message = "게임을 시작하였습니다.\n";
      parent.parent.server_state.roomArea.append(message);

   }

   // 게임을 끝내는 부분이다.
   synchronized public void finishGame() {
  
      onGaming = false;

      // 종료되었다는 메시지를 보낸다.
      String message = "460|END";
      broadcastMessage(message);

      // server message state for test
      message = "게임이 끝났습니다.\n";
      parent.parent.server_state.roomArea.append(message);

      // server message state for test
      message = "\n 말들의 순위는 다음과 같습니다.\n";
      parent.parent.server_state.roomArea.append(message);

      // server message state for test
      for (int i = 0; i < 8; i++) {
         String m = (i + 1) + "번 말이 " + rank[i] + "등으로 들어왔습니다.\n";
         parent.parent.server_state.roomArea.append(m);
      }

      // server message state for test
      parent.parent.server_state.roomArea.append("\n");

     
      gamingManList.removeAllElements();
      participantManList.removeAllElements();
      master_player = null;
   }

 

   

   // 게임 참여 메시지에 대한 처리를 하는 부분이다.
   public void processJoin(String playerId) {
      String message;
      GamePlayer_Server player = null;
      player = getPlayer(playerId);

      if (player != null) {
         if ((!participantManList.contains(player))) { // 게임 참여 목록에 포함되어 있지
                                             // 않다면...
            if (participantManList.size() < MAX_PARTICIPANT_NUMBER) { // 게임
                                                         // 참가수를
                                                         // 초과하지
                                                         // 않았다면...
               participantManList.addElement(player);
               message = "420|" + playerId + "|SUCCESS";
               broadcastMessage(message);
               if (master_player == null) {// 방장이 없다면
                  master_player = player;// 방장으로 등극시키고
                  sendMessage(master_player.id, "454|MASTER");// 방장이 되었음을
                                                   // 알린다.
               }
            } else { // 게임 참가자 수를 초과하였다면...
               message = "420|" + playerId + "|MAX_OVER";// 초과 메시지를 보낸다.
               sendMessage(playerId, message);
            }
         } else { // 게임 목록에 이미 포함 되어 있다면...
            if (master_player == player) {// 방장이라면
               participantManList.removeElement(player);
               if (!participantManList.isEmpty()) {// 참여자가 있다면
                  Enumeration e = participantManList.elements();
                  GamePlayer_Server bangjang;
                  bangjang = (GamePlayer_Server) e.nextElement();
                  master_player = bangjang;// 새로 아무나 방장을 뽑고
                  sendMessage(master_player.id, "454|MASTER");// 방장이 되었음을
                                                   // 알린다.
                  if (gamingManList.contains(player)) {// 요청한 사용자는 참여 해제
                                                // 시킨다.
                     gamingManList.removeElement(player);
                  }
               } else {
                  master_player = null;
               }
               message = "420|" + playerId + "|FAIL";
               sendMessage(playerId, message);
            } else {// 방장이 아니라면 그냥 참여 해제 시키면 된다.
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

   // 레디 메시지를 처리하는 부분이다.
   public void processReady(String playerId) {
      String message;
      GamePlayer_Server player = null;
      player = getPlayer(playerId);

      if (player == master_player) {// 방장이라면 이는 곧 게임 시작을 의미한다.
         gamingManList.addElement(player);
         if (gamingManList.containsAll(participantManList)) {
        	 if(!(votenum==8))
        		 votenum++;
            parent.parent.server_state.roomArea.append("투표할 총 인원 " +votenum + "\n");
            startGame();
         }
      } else {// 방장이 아니라면
         if (!gamingManList.contains(player)) {// 없으면 포함시키고
            gamingManList.addElement(player);
            message = "453|READY|DISABLE";
            if(!(votenum==8))
       		 votenum++;
            sendMessage(player.id, message);
         } else {// 있다면 배제시킨다.
            gamingManList.removeElement(player);
            message = "453|READY|ENABLE";
            if(!(votenum==8))
       		 votenum++;
            sendMessage(player.id, message);
         }
      }
   }

   // 주어진 플레이어에게 메시지를 보낸다.
   public void sendMessage(String playerId, String message) {
      GamePlayer_Server player = null;

      // 클라이언트 리스트에서 주어진 아이디를 갖는 GamePlayer_Server 객체를 얻는다.
      player = getPlayer(playerId);
      if (player != null) {
         // 해당 플레이어에게 메시지를 보낸다.
         player.sendMessage(message);
      }
   }

   // 주어진 메시지를 룸 내의 모든 사람들에게 브로드캐스팅 한다.
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

   // 룸 서버 스레드의 바디로서
   // 룸에 있는 클라이언트(플레이어)들을 관리하기 위한 스레드 서버
   // 룸에서는 룸 내에 있는 클라이언트는 물론 게임의 진행도 관리한다.
   public void run() {
      GamePlayer_Server player=null;

      // 룸 서버 스레드 객체는 룸 내의 클라이언트 리스트에 등록되어 있는 
      // 클라이언트들로부터 온 메시지를 주기적으로 체크하여 읽어온다.
      // 따라서, 스레드로서 동작하며, 무한루프를 갖는다.
      while(true) {         
         // Read Data
         try {
            // CPU 독식(또는 욕심쟁이)하는 스레드가 되는 것을 방지하기 위해 
            // 딜레이를 준다.
            Thread.sleep(80);
         } catch(InterruptedException ie) {
         }
        
         // send Mafia location
        
            
         // 클라이언트 리스트에 등록되어 있는 클라이언트 객체를 하나씩 가져오기
         // 위해 playerList 해시테이블로부터 Enumeration 객체를 얻는다.
         for(int i=0;i<playerList.size();i++) {
            // Enumeration 객체로부터 하나의 클라이언트 객체를 얻어온다. 
            player = (GamePlayer_Server)playerList.elementAt(i);
            try {
               String receive = null;

// 각 클라이언트로부터 데이터를 읽어온다.
// 이 때, 각 클라이언트로부터 데이터가 올 때까지
// 무한정 기다리는(블럭킹) 것이 아니라
// SO_TIME 에 설정된 시간(10msec)이 지나면
// 소켓에서 읽어들이기 위해 블럭킹 된 것을 빠져나오게 된다.
// 그래야지, 다음 클라이언트로부터 데이터를 읽어들일 수 있다.
               receive = player.receiveMessage();

// player.receiveMessage() 메소드의 실행 결과는 다음과 같다.
//   1) 클라이언트가 끊긴 경우: SocketException or IOException 예외 발생
//                              try {
//                                 ...
//                              } catch(SocketException e) {
//                                 이 부분에서 해당 예외 처리
//                                   - 리스트에서 제거한다.
//                              } catch(IOException e) {
//                                 이 부분에서 해당 예외 처리
//                                   - 리스트에서 제거한다.
//                              }
//   2) null 또는 "" 값 리턴: 클라이언트로부터 온 데이터가 없거나
//                            또는, SO_TIME에 지정된 시간이 지난 경우
//   3) 문자열 데이터: 클라이언트로부터 정상적으로 데이터를 읽은 경우

// 다음은 위의 세 가지 경우에 따라 처리하는 예이다.

// 1) 번에 대한 처리는 저 아래에 있는 catch(...) 문에서 처리한다.
// 2) 클라이언트로부터 전달된 데이터가 없는 경우
               if(receive == null || "".equals(receive)) {
                  continue;
               }

// 3) 클라이언트로부터 데이터를 정상적으로 읽어들인 경우
//    프로토콜에 따라 데이터를 처리한다.

               StringTokenizer st = new StringTokenizer(receive, "|");
               int protocol = Integer.parseInt(st.nextToken());
               String message=null;
               switch(protocol){
               case 300:{
                  // 브로드캐스트 채팅
                  // 300|fromId|chatMessage
                   String fromId = st.nextToken();
                   String chatStr = st.nextToken();
                   message = "400|"+fromId+"|"+chatStr;
                   broadcastMessage(message);
               } break;
               case 301:{
                  // 유니캐스트 채팅
                  // 301|fromId|toId|chatMessage
                   String fromId = st.nextToken();
                   String toId = st.nextToken();
                   String chatStr = st.nextToken();
                   message = "401|"+fromId+"|"+chatStr;
                   sendMessage(toId,message);
               } break;
               case 310:{
                  // 룸 입장
                   // 310|fromID
                   String fromId = st.nextToken();

                   player = getPlayer(fromId);
                   if(player != null) {
                      message = "410|"+fromId;
                      broadcastMessage(message);
                      
                      // 게임 중인지 여부를 가려 참여 버튼 활성화 여부를 알린다.
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
                  // 게임에 참여하기
                   // 320|fromId
                   String fromId = st.nextToken();
                   processJoin(fromId);
               } break;
               case 330:{
                  // 클라이언트 퇴장(로비로 이동)
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
                  // 클라이언트 종료
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
                  // 클라이언트 리스트 요청
                   // 340|fromId
                   String fromId = st.nextToken();
                   player = getPlayer(fromId);
                   if(player != null) {
                      sendUserList(player);
                   }
               } break;
               case 350:{
                  // 게임 레디 요청
                  // 350|fromId
                  String fromId = st.nextToken();
                  processReady(fromId);
               } break;
               
               case 357:{ // 새로만든 포트번호 수정해야할 포트 번호
                  // 357|id|번호 0~7 Jlabel 값
                  //가장 많이 투표 받은 사람 Client에게 알려주
                  String fromid = st.nextToken();
                  String fromNum = st.nextToken();
                  
                  String msg = "400|" + fromid + "|님께서 " + (Integer.parseInt(fromNum)+1) +"번 플레이어를 투표 하셨습니다.";
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
                     parent.parent.server_state.roomArea.append((k+1)+"번째 표수 "+rank[k]+ "\n");
                  }
                  parent.parent.server_state.roomArea.append("총 득표 수 " +ranksum+ "\n");
                  
                  if(ranksum == votenum) // 총 투표수가 8개 면
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
                        msg2 = "457|NODEATH"; //만약 1,2등 득표수가 같다면 죽는사람이 없다.
                        ranksum=0;
                         parent.parent.server_state.roomArea.append("다음 투표할 총 인원 " +votenum + "\n");
                         for(int j=0; j<8; j++)
                            rank[j] = 0;
                     }
                     else
                     {
                        String firstpara = String.valueOf(rank_first); //1등 득표수 인자값 전달 0~7
                        msg2 = "457|" + firstpara;
                        
                        ranksum=0;
                         votenum--;
                         parent.parent.server_state.roomArea.append("다음 투표할 총 인원 " +votenum + "\n");
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
                  // 사용자 정보 요청
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
               // 1) 번에 대한 예외 처리한다.
               // 클라이언트와의 네트워킹이 끊긴 경우이므로,
               // 반드시 해당 클라이언트를 대기실에서 제거해야 한다.
               removePlayer(player);
            } catch(IOException ne) {
               // 1) 번에 대한 예외 처리한다.
               // 클라이언트와의 네트워킹이 끊긴 경우이므로,
               // 반드시 해당 클라이언트를 대기실에서 제거해야 한다.
               removePlayer(player);
            }
         }
      }
   }
}