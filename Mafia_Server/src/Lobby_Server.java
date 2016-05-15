/*
 * ���ǿ� �ִ� Ŭ���̾�Ʈ(�÷��̾�)���� �����ϱ� ���� ������ ����
 * ���ǿ����� ���� ���� �ִ� Ŭ���̾�Ʈ�� ���� ������ �뵵 �����Ѵ�.
 */

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;


public class Lobby_Server extends Thread {
	Mafia_Server parent;
    Hashtable clientList=null;         // ���� ���� �ִ� Ŭ���̾�Ʈ ����Ʈ
    Hashtable roomList=null;           // �Լ��� �� ����Ʈ
    GameRoom_Server room=null;         // �� ���� �۾��� �ϱ� ���� �ӽ� ��ü

    public Lobby_Server (Mafia_Server parent) {
    	this.parent=parent;
        clientList = new Hashtable();   // Ŭ���̾�Ʈ ����Ʈ�� ���� �ؽ����̺� ��ü ����
        roomList = new Hashtable();     // �� ����Ʈ�� ���� �ؽ����̺� ��ü ����
    }

    // �־��� ���̵��� Ŭ���̾�Ʈ�� ���� GamePlayer_Server ��ü�� ��´�.
    public GamePlayer_Server getPlayer(String playerId) {
        GamePlayer_Server player=null;

        // ���� ���� Ŭ���̾�Ʈ ����Ʈ�� �־��� ���̵��� Ŭ���̾�Ʈ�� �ִ��� Ȯ��
        player = (GamePlayer_Server)clientList.get(playerId);
      
        return player;
    }

    // ��� ������ Ŭ���̾�Ʈ�� ���ǿ� �߰��Ѵ�.
    public void addPlayer(GamePlayer_Server player) {
	    //�÷��̾� ���̵� ���� �ƴϰ� Ŭ���̾�Ʈ ����Ʈ�� ���ٸ�
        if(player.id != null && clientList.get(player.id) == null){
            String message=null;

            // ��� ������ Ŭ���̾�Ʈ���� ȯ�� �޽����� ������.
            message = "210|["+player.id+"]�� � ���ʽÿ�...";
            player.sendMessage(message);     // Ŭ���̾�Ʈ���Ը� �޽��� ����

            // ��� ������ Ŭ���̾�Ʈ�� �������� ���� ���� ��ο��� �˸���.
            message = "200|���|["+player.id+"]���� �����Ͽ����ϴ�..";
            broadcastMessage(message);       // ���� ���� ��ο��� �޽��� ����
            
            //server message state for test
            message = "["+player.id+"]���� �����Ͽ����ϴ�..\n";
            parent.server_state.messageArea.append(message);
            
           // ��� ������ Ŭ���̾�Ʈ�� Ŭ���̾�Ʈ ����Ʈ�� �߰��Ѵ�.
           clientList.put(player.id, player);

           // ���ǳ��� ��ο��� ���ŵ� ����� ����Ʈ�� ������.
           broadcastLobbyUserList();
           broadcastRoomList();
           
           message = player.id+"���� ���ǿ� �����ϼ̽��ϴ�.\n";
           parent.server_state.messageArea.append(message);
        }
    }

    // �־��� Ŭ���̾�Ʈ�� ���ǿ��� �����Ѵ�.
    public void removePlayer(GamePlayer_Server player) {
        // �־��� Ŭ���̾�Ʈ�� Ŭ���̾�Ʈ ����Ʈ���� �����Ѵ�.
        clientList.remove(player.id);
        // ���ǳ��� ��ο��� ���ŵ� ����� ����Ʈ�� ������.
        
        String message;
        message = player.id+"���� ���ǿ��� �����̽��ϴ�.\n";
        parent.server_state.messageArea.append(message);
        
        broadcastLobbyUserList();
        broadcastRoomList();
    }

    // �־��� ���̵��� Ŭ���̾�Ʈ���� �޽����� ������.
    public void sendMessage(String playerId, String message) {
        GamePlayer_Server player=null;

        // Ŭ���̾�Ʈ ����Ʈ���� �־��� ���̵� ���� GamePlayer_Server ��ü�� ��´�.
        player = getPlayer(playerId);
        if(player != null) {
            // �ش� �÷��̾�� �޽����� ������.
            player.sendMessage(message);
        }
    }

    // ���� ���� ��ο��� �־��� �޽����� ������.
    public void broadcastMessage(String message) {
        GamePlayer_Server player=null;

        int i=0;
        Enumeration e = clientList.elements();
        while(e.hasMoreElements()) {
            // Ŭ���̾�Ʈ ����Ʈ ���� �ִ� ��� Ŭ���̾�Ʈ��
            // GamePlayer_Server ��ü�� ��� �̸� �̿��Ͽ� �޽����� ������.
            player = (GamePlayer_Server)e.nextElement();
            if(player != null) {
                // �� �÷��̾�� �޽����� ������.
                player.sendMessage(message);
            }
        }
    }

    public void sendLobbyUserList(String playerId) {
	    GamePlayer_Server player = null;
	    try {
	        String message = "220|";
	        // ���� ��ü�� ������ �ִ� Ŭ���̾�Ʈ ����Ʈ ����
	        // ��� Ŭ���̾�Ʈ ����Ʈ�� �����Ѵ�.
	        Enumeration e = clientList.elements();
	        while(e.hasMoreElements()) {
	            // ���� ��ü�� Ŭ���̾�Ʈ ����Ʈ�κ��� �̸��� �ϳ��� �����´�.
	            player = (GamePlayer_Server)e.nextElement();
	            if(player != null) {
	                message += player.id+",";
	            }
	        }
	        // ����� ����Ʈ�� �����ϸ鼭 ���������� �߰��� ',' �� �����ؾ� �Ѵ�.
	        if(message.endsWith(",")) {
	            message = message.substring(0, message.length()-1);
	        }
	        // �־��� ���̵� ���� Ŭ���̾�Ʈ�� GamePlayer_Server ��ü�� ����
	        // ����� ����Ʈ �޽����� �����Ѵ�.
	        player = (GamePlayer_Server)clientList.get(playerId);
	        if(player != null) {
	            player.sendMessage(message);  // �޽��� ����
	        }
	    } catch(Exception e) {
	    }
    }

    // ���� ���� �ִ� ��ü���� �־��� ���� ���� �ִ� ����ڸ���Ʈ�� ������.
    public void broadcastLobbyUserList() {
        GamePlayer_Server player=null;

        // ������ Ŭ���̾�Ʈ ����Ʈ ���� �ִ� �� Ŭ���̾�Ʈ���� 
        // �� ����Ʈ�� ������ �ȴ�.
        Enumeration e = clientList.elements();
        while(e.hasMoreElements()) {
            // ������ Ŭ���̾�Ʈ ����Ʈ���� �ϳ��� GamePlayer_Server ��ü�� �����´�.
            player = (GamePlayer_Server)e.nextElement();
            if(player.id != null && player.room == null) {
                // �ش� Ŭ���̾�Ʈ���� �� ���� ����� ����Ʈ�� ������.
                sendLobbyUserList(player.id);
            }
        }
    }
   
    // �־��� ���̵��� �÷��̾�� �ش� �뿡 �ִ� ����� ����Ʈ�� ������.
    public void sendRoomUserList(String playerId, String roomId) {
        GamePlayer_Server player = null;
        try {
            String message = "221|";
            // �ش� �� ��ü�� ������ �ִ� Ŭ���̾�Ʈ ����Ʈ ����
            // ��� Ŭ���̾�Ʈ ����Ʈ�� �����Ѵ�.
            room = (GameRoom_Server)roomList.get(roomId);
            for(int i=0;i<room.playerList.size();i++) {
                // �� ��ü�� Ŭ���̾�Ʈ ����Ʈ�κ��� �̸��� �ϳ��� �����´�.
                player = (GamePlayer_Server)room.playerList.elementAt(i);
                message += player.id+",";
            }
      
            // ����� ����Ʈ�� �����ϸ鼭 ���������� �߰��� ',' �� �����ؾ� �Ѵ�.
            if(message.endsWith(",")) {
                message = message.substring(0, message.length()-1);
            }

            // �־��� ���̵� ���� Ŭ���̾�Ʈ�� GamePlayer_Server ��ü�� ����
            // ����� ����Ʈ �޽����� �����Ѵ�.
            player = (GamePlayer_Server)clientList.get(playerId);
            if(player != null) {
            	player.sendMessage(message);  // �޽��� ����
            }

        } catch(Exception e) {
        }
    }
   
    // �־��� ���̵��� �÷��̾�� �� ����Ʈ�� ������.
    public void sendRoomList(GamePlayer_Server player) {
    	try {
    		String message=null;

    		message = "���� (�����ο�:"+clientList.size()+"),";
    		if(roomList.isEmpty()) {
    			//room is empty
    		} else {
    			Enumeration e = roomList.elements();
    			while(e.hasMoreElements()) {
    				GameRoom_Server room = (GameRoom_Server)e.nextElement();
    				// ���̸�, �����ο�, ���������ο� ���� ������ ����
    				message += room.roomId+" (��ü�����ο�["
    						+ room.playerList.size()
    						+ "]/����������["
    						+ room.gamingManList.size() + "]),";
    			}
    			// �� ����Ʈ�� �����ϸ鼭 ���������� �߰��� ',' �� �����ؾ� �Ѵ�.
    			if(message.endsWith(",")) {
    				message = message.substring(0, message.length()-1);
    			}
    		}

    		// �޽����� �ϼ��Ѵ�.
    		message = "230|"+message;

    		// �־��� ���̵� ���� Ŭ���̾�Ʈ�� GamePlayer_Server ��ü�� ����
    		// ����� ����Ʈ �޽����� �����Ѵ�.
    		if(player != null) {
    			player.sendMessage(message);  // �޽��� ����
    		}
    	} catch(Exception e) {
    	}
    }

    // ���� ���� �ִ� ��ü���� �� ����Ʈ�� �����Ѵ�.
    public void broadcastRoomList() {
    	GamePlayer_Server player=null;

    	// ������ Ŭ���̾�Ʈ ����Ʈ ���� �ִ� �� Ŭ���̾�Ʈ���� 
    	// �� ����Ʈ�� ������ �ȴ�.
    	Enumeration e = clientList.elements();
    	while(e.hasMoreElements()) {
    		// ������ Ŭ���̾�Ʈ ����Ʈ���� �ϳ��� GamePlayer_Server ��ü�� �����´�.
    		player = (GamePlayer_Server)e.nextElement();
    		if(player != null) {
    			// �ش� Ŭ���̾�Ʈ���� �� ����Ʈ�� ������.
    			sendRoomList(player);
    		}
    	}
    }
   
    // �־��� ���̵� ���� Ŭ���̾�Ʈ��
    // �־��� �̸��� ���� �����Ѵ�.
    public int makeRoom(String playerId, String roomId) {
    	GamePlayer_Server player = (GamePlayer_Server)clientList.get(playerId);

    	//������ ���̸��� ���� ���� �ִ��� �˻�
    	if(!roomList.isEmpty()) {
    		String room;
    		Enumeration e = roomList.keys();
    		while(e.hasMoreElements()) {
    			room = (String)e.nextElement();
    			if(room.equals(roomId)){
    				return -1;
    			}
    		}
    	}
    	// roomId, player ������ �̿��Ͽ� ���� �����ϱ� ���� GameRoom ��ü�� �����Ѵ�.
    	room = new GameRoom_Server(this,roomId, player);

    	// ���� ������ ������ �ִ� �� ����Ʈ�� ��� ������ �� ��ü�� �߰��Ѵ�.
    	roomList.put(roomId, room);

    	// �� ���� ��ü�� �� ���� �ִ� Ŭ���̾�Ʈ�κ��� �� �����͸�
    	// �ֱ������� üũ�ؾ� �ϹǷ�, ������μ� �����Ѵ�.
    	// ����, �� ���� ��ü�� ������ �Ŀ��� �ݵ�� start() �޼ҵ� ȣ���Ͽ�
    	// �����带 ���۽��� �־�� �Ѵ�.
    	room.start();
    	//�� ������ ���������� �ٽ� �� ����Ʈ�� ��ε�ĳ��Ʈ�Ѵ�.
    	broadcastRoomList();
   
    	// server message state for test
        String message = "["+playerId+"] ���� ��û�� ���� ���� ����������ϴ�.\n";
        parent.server_state.messageArea.append(message);
        
    	return 0;
    }

    // �־��� �÷��̾ ���ǿ��� ���� �����Ͽ� ������ ���� ���� ó���Ѵ�.
    public int enterRoom(String playerId, String roomId) {
    	// �־��� ���̵��� �� ��ü�� ��´�.
    	GameRoom_Server room = (GameRoom_Server)roomList.get(roomId);

    	if(room != null) {
    		GamePlayer_Server player = (GamePlayer_Server)clientList.get(playerId);
    		
    		if(room.playerList.size() < room.MAX_PLAYER_NUMBER){
    			String message = "250|SUCCESS";
				sendMessage(playerId,message);
        		// ���� ��ü�� Ŭ���̾�Ʈ ����Ʈ���� �־��� �÷��̾ ������ ��,
        		removePlayer(player);

        		// �� ��ü�� Ŭ���̾�Ʈ ����Ʈ�� �߰��Ѵ�.
        		room.addPlayer(player);

        		// Ŭ���̾�Ʈ�� ���ǿ��� ������ �̵������Ƿ�,
        		// ���� ���� ��ο��� �־��� �� ������ ������ �� �ֵ��� �Ѵ�.
        		broadcastLobbyUserList();
        		
        		// server message state for test
                message = "["+playerId+"]���� ["+roomId+"]�� �����Ͽ����ϴ�..\n";
                parent.server_state.messageArea.append(message);
        		
        		return 0;
    		} else {
    			return -1;
    		}
    	} else {
    		return -1;
    	}
    }

    // �־��� ���̵��� ���� ���� ��ü�� �� ����Ʈ���� �����Ѵ�.
    public void removeRoom(String roomId) {
    	// �־��� ���̵��� �� ��ü�� ��´�.
    	GameRoom_Server room = (GameRoom_Server)roomList.get(roomId);

    	if(room != null) {       // ���� ��ü�� �ִ� �� ����Ʈ���� �־��� ���̵��� �� ��ü�� �����Ѵ�.
    		roomList.remove(roomId);
    		// ���� ���ŵǾ����Ƿ�, ���� ���� ��ο��� �� ����Ʈ�� �����ϵ��� �Ѵ�.
    		broadcastRoomList();
    		
    		// server message state for test
            String message = "["+roomId+"] ���� ���ŵǾ����ϴ�.\n";
            parent.server_state.messageArea.append(message);
    	}
    }


    // �־��� ���̵�(who)�� Ŭ���̾�Ʈ�� ���� �������� ������ 
    // �־��� ���̵� ���� Ŭ���̾�Ʈ(to)���� ������.
    public void sendProfile(String to, String who) {
    	GamePlayer_Server whoPlayer=null, toPlayer=null;
    	try {
    		// �־��� ���̵�(who)�� Ŭ���̾�Ʈ ��ü�� ��´�.
    		whoPlayer = getPlayer(who);
    		// �־��� ���̵�(to)�� Ŭ���̾�Ʈ ��ü�� ��´�.
    		toPlayer = getPlayer(to);
    		String profileMessage = "260|"+whoPlayer.getProfileMessage(who);
    		toPlayer.sendMessage(profileMessage);
    	} catch(Exception e) {
    	}
    }

    // �뿡 �ִ� Ŭ���̾�Ʈ�� �ٽ� ���Ƿ� ���ƿ��� �� ���,
    // ������ Ŭ���̾�Ʈ ����Ʈ�� �ش� Ŭ���̾�Ʈ�� �ٽ� �߰��Ѵ�.
    public void takePlayer(GamePlayer_Server player) {
    	int i=0;
    	// ������ Ŭ���̾�Ʈ ����Ʈ�� �ش� Ŭ���̾�Ʈ�� �ٽ� �߰��Ѵ�.
    	clientList.put(player.id, player);

    	// ���ǳ��� ��ο��� ���ŵ� ����� ����Ʈ�� ������.
        broadcastLobbyUserList();
        broadcastRoomList();
        
        String message;
        message = player.id+"���� ���ǿ� �����ϼ̽��ϴ�.\n";
        parent.server_state.messageArea.append(message);
    }

    /*
     * ���ǿ� �ִ� Ŭ���̾�Ʈ(�÷��̾�)���� �����ϱ� ���� ������ ����
     * ���ǿ����� ���� ���� �ִ� Ŭ���̾�Ʈ�� ���� ������ �뵵 �����Ѵ�.
     */
    public void run() {
    	GamePlayer_Server player = null;
    	// ���� ������ ���� ��ü�� Ŭ���̾�Ʈ ����Ʈ�� ��ϵǾ� �ִ� 
    	// Ŭ���̾�Ʈ��κ��� �� �޽����� �ֱ������� üũ�Ͽ� �о�´�.
    	// ����, ������μ� �����ϸ�, ���ѷ����� ���´�.
    	while(true) {
    		// Read Data
    		try {
    			// CPU�� �����ϴ� �����尡 �Ǵ� ���� �����ϱ� ���� �����̸� �ش�. 
    			sleep(100);
    		} catch(InterruptedException ie) {
    		}

    		// Ŭ���̾�Ʈ ����Ʈ�� ��ϵǾ� �ִ� Ŭ���̾�Ʈ ��ü�� �ϳ��� ��������
    		// ���� clientList �ؽ����̺�κ��� Enumeration ��ü�� ��´�.
    		Enumeration e = clientList.elements();
    		while(e.hasMoreElements()) {
    			// Enumeration ��ü�κ��� �ϳ��� Ŭ���̾�Ʈ ��ü�� ���´�. 
    			player = (GamePlayer_Server)e.nextElement();
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

    				// Mafia Protocol
    				// command|arg1|arg2|arg3|...
    				StringTokenizer st = new StringTokenizer(receive, "|");
    				int protocol = Integer.parseInt(st.nextToken());
    				switch(protocol){
    				case 100:{
    					// ��ε�ĳ��Ʈ ä��
    					// 100|fromId|chatMessage
    					String fromId = st.nextToken();
    					String chatMessage = st.nextToken();
    					String message = "200|"+fromId+"|"+chatMessage;
    					broadcastMessage(message);
    					
    					// server message state for test
    		            message = "["+fromId+"]���� ��ü���� �޽����� �����̽��ϴ�.\n";
    		            parent.server_state.messageArea.append(message);
    				} break;
    				case 101:{ 
    					// //����ĳ��Ʈ ä��
    					// 101|fromId|toId|chatMessage
    					String fromId = st.nextToken();
    					String toId = st.nextToken();
    					String chatMessage = st.nextToken();
    					String message;
    					message = "201|"+fromId+"|"+chatMessage;
    					
    					sendMessage(toId, message);   // ���� ������ ����ĳ����
    					
    					// server message state for test
    		            message = "["+fromId+"]���� "+"["+toId+"]�Կ��� �޽����� �����̽��ϴ�.\n";
    		            parent.server_state.messageArea.append(message);
    				} break;
    				case 110:{ 
    					// ���� ���� ��û - ���ο� Ŭ���̾�Ʈ�� ������ �ǹ�
    					// ���Ǹ� ���� add player �κп��� ���
    				} break;
    				case 111:{ 
    					// ���� ����
    					// 111|fromId
    					String fromId = st.nextToken();
    					// Ŭ���̾�Ʈ�� ���� �޽����� ��ε�ĳ����
    					String message = "200|"+fromId+"|["+fromId+"]���� �����ϼ̽��ϴ�.";
    					broadcastMessage(message);
    					// ���� Ŭ���̾�Ʈ���� �߰���� �޽��� ����
    					message = "211|["+fromId+"]�� �ȳ��� ������.";
    					sendMessage(fromId, message);
    					
    					// server message state for test
    		            message = "["+fromId+"]���� �����ϼ˽��ϴ�..\n";
    		            parent.server_state.messageArea.append(message);
    				} break;
    				case 120:{   
    					// ���� ����� ����Ʈ ��û
    					// 120|fromId
    					String fromId = st.nextToken();
    					sendLobbyUserList(fromId);
    				} break;
    				case 121:{ 
    					// �� ����� ����Ʈ ��û
    					// 121|fromId|roomId
    					String fromId = st.nextToken();
    					String roomId = st.nextToken();
    					sendRoomUserList(fromId,roomId);
    				} break;
    				case 130:{  
    					// �� ����Ʈ ��û
    					// 130|fromId
    					String fromId = st.nextToken();
                        sendRoomList(getPlayer(fromId));
    				} break;
    				case 140:{ 
    					// �� ����
    					// 140|fromId|roomId
    					String fromId = st.nextToken();
    					String roomId = st.nextToken();
                   
    					int success;
    					String message;
                   
    					success = makeRoom(fromId, roomId);
                   
    					if(success == -1){
    						message = "240|FAIL";
    						sendMessage(fromId,message);
    					}
    					else{
    						message = "240|SUCCESS";  // �游��� ���
    						// ���� ���� Ŭ���̾�Ʈ�� ���� ����Ʈ���� �����Ѵ�.
        					sendMessage(fromId,message);
        					removePlayer(player);
    					}
    					broadcastRoomList();
    				} break;
    				case 150:{         
    					//�� ����
    					// 150|fromId|roomId
    					String fromId = st.nextToken();
    					String roomId = st.nextToken();
    					
    					int success = enterRoom(fromId, roomId);
    					String message;
    					
    					if(success == 0){
        					// ���� ���� ���δ�  enterRoom �޼ҵ� �ȿ��� �޽����� ������.
    						// �̴� enterRoom���� �̹� Ŭ���̾�Ʈ�� ������ �Ѿ�� ������
    						// �Ѿ�� ���� �޽����� ������ �Ѵ�.
        					// �ش� �� ��ü(room)�� �޼ҵ带 �̿��Ͽ� �� ���� �ִ�
        					// Ŭ���̾�Ʈ���� ���� ��Ȳ�� �����Ѵ�.
        					room.broadcastUserList();
        					broadcastRoomList();
    					} else {
    						message = "250|FAIL";
    						sendMessage(fromId,message);
    					}
    				} break;
    				case 160:{         
    					// �������� ��û
    					// 160|fromId|whoId
    					String fromId = st.nextToken();
    					String whoId = st.nextToken();
    					sendProfile(fromId, whoId);
    				} break;
    				}   
    			} catch(SocketException ne) {
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