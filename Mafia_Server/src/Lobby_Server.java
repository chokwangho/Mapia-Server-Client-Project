/*
 * 대기실에 있는 클라이언트(플레이어)들을 관리하기 위한 스레드 서버
 * 대기실에서는 대기실 내에 있는 클라이언트는 물론 개설된 룸도 관리한다.
 */

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;


public class Lobby_Server extends Thread {
	Mafia_Server parent;
    Hashtable clientList=null;         // 대기실 내에 있는 클라이언트 리스트
    Hashtable roomList=null;           // 게설된 룸 리스트
    GameRoom_Server room=null;         // 룸 관련 작업을 하기 위한 임시 객체

    public Lobby_Server (Mafia_Server parent) {
    	this.parent=parent;
        clientList = new Hashtable();   // 클라이언트 리스트를 위한 해시테이블 객체 생성
        roomList = new Hashtable();     // 룸 리스트를 위한 해시테이블 객체 생성
    }

    // 주어진 아이디의 클라이언트에 대한 GamePlayer_Server 객체를 얻는다.
    public GamePlayer_Server getPlayer(String playerId) {
        GamePlayer_Server player=null;

        // 대기실 내의 클라이언트 리스트에 주어진 아이디의 클라이언트가 있는지 확인
        player = (GamePlayer_Server)clientList.get(playerId);
      
        return player;
    }

    // 방금 접속한 클라이언트를 대기실에 추가한다.
    public void addPlayer(GamePlayer_Server player) {
	    //플레이어 아이디가 널이 아니고 클라이언트 리스트에 없다면
        if(player.id != null && clientList.get(player.id) == null){
            String message=null;

            // 방금 접속한 클라이언트에게 환영 메시지를 보낸다.
            message = "210|["+player.id+"]님 어서 오십시요...";
            player.sendMessage(message);     // 클라이언트에게만 메시지 전송

            // 방금 접속한 클라이언트가 들어왔음을 대기실 내의 모두에게 알린다.
            message = "200|운영자|["+player.id+"]님이 입장하였습니다..";
            broadcastMessage(message);       // 대기실 내의 모두에게 메시지 전송
            
            //server message state for test
            message = "["+player.id+"]님이 입장하였습니다..\n";
            parent.server_state.messageArea.append(message);
            
           // 방금 접속한 클라이언트를 클라이언트 리스트에 추가한다.
           clientList.put(player.id, player);

           // 대기실내의 모두에게 갱신된 사용자 리스트를 보낸다.
           broadcastLobbyUserList();
           broadcastRoomList();
           
           message = player.id+"님이 대기실에 입장하셨습니다.\n";
           parent.server_state.messageArea.append(message);
        }
    }

    // 주어진 클라이언트를 대기실에서 삭제한다.
    public void removePlayer(GamePlayer_Server player) {
        // 주어진 클라이언트를 클라이언트 리스트에서 삭제한다.
        clientList.remove(player.id);
        // 대기실내의 모두에게 갱신된 사용자 리스트를 보낸다.
        
        String message;
        message = player.id+"님이 대기실에서 나가셨습니다.\n";
        parent.server_state.messageArea.append(message);
        
        broadcastLobbyUserList();
        broadcastRoomList();
    }

    // 주어진 아이디의 클라이언트에게 메시지를 보낸다.
    public void sendMessage(String playerId, String message) {
        GamePlayer_Server player=null;

        // 클라이언트 리스트에서 주어진 아이디를 갖는 GamePlayer_Server 객체를 얻는다.
        player = getPlayer(playerId);
        if(player != null) {
            // 해당 플레이어에게 메시지를 보낸다.
            player.sendMessage(message);
        }
    }

    // 대기실 내의 모두에게 주어진 메시지를 보낸다.
    public void broadcastMessage(String message) {
        GamePlayer_Server player=null;

        int i=0;
        Enumeration e = clientList.elements();
        while(e.hasMoreElements()) {
            // 클라이언트 리스트 내에 있는 모든 클라이언트의
            // GamePlayer_Server 객체를 얻어 이를 이용하여 메시지를 보낸다.
            player = (GamePlayer_Server)e.nextElement();
            if(player != null) {
                // 각 플레이어에게 메시지를 보낸다.
                player.sendMessage(message);
            }
        }
    }

    public void sendLobbyUserList(String playerId) {
	    GamePlayer_Server player = null;
	    try {
	        String message = "220|";
	        // 대기실 객체가 가지고 있는 클라이언트 리스트 내의
	        // 모든 클라이언트 리스트를 생성한다.
	        Enumeration e = clientList.elements();
	        while(e.hasMoreElements()) {
	            // 대기실 객체의 클라이언트 리스트로부터 이름을 하나씩 가져온다.
	            player = (GamePlayer_Server)e.nextElement();
	            if(player != null) {
	                message += player.id+",";
	            }
	        }
	        // 사용자 리스트를 생성하면서 마지막으로 추가된 ',' 를 제거해야 한다.
	        if(message.endsWith(",")) {
	            message = message.substring(0, message.length()-1);
	        }
	        // 주어진 아이디를 갖는 클라이언트의 GamePlayer_Server 객체를 얻어와
	        // 사용자 리스트 메시지를 전달한다.
	        player = (GamePlayer_Server)clientList.get(playerId);
	        if(player != null) {
	            player.sendMessage(message);  // 메시지 전송
	        }
	    } catch(Exception e) {
	    }
    }

    // 대기실 내에 있는 전체에게 주어진 대기실 내에 있는 사용자리스트를 보낸다.
    public void broadcastLobbyUserList() {
        GamePlayer_Server player=null;

        // 대기실의 클라이언트 리스트 내에 있는 각 클라이언트에게 
        // 룸 리스트를 보내면 된다.
        Enumeration e = clientList.elements();
        while(e.hasMoreElements()) {
            // 대기실의 클라이언트 리스트에서 하나의 GamePlayer_Server 객체를 가져온다.
            player = (GamePlayer_Server)e.nextElement();
            if(player.id != null && player.room == null) {
                // 해당 클라이언트에게 룸 내의 사용자 리스트를 보낸다.
                sendLobbyUserList(player.id);
            }
        }
    }
   
    // 주어진 아이디의 플레이어에게 해당 룸에 있는 사용자 리스트를 보낸다.
    public void sendRoomUserList(String playerId, String roomId) {
        GamePlayer_Server player = null;
        try {
            String message = "221|";
            // 해당 룸 객체가 가지고 있는 클라이언트 리스트 내의
            // 모든 클라이언트 리스트를 생성한다.
            room = (GameRoom_Server)roomList.get(roomId);
            for(int i=0;i<room.playerList.size();i++) {
                // 룸 객체의 클라이언트 리스트로부터 이름을 하나씩 가져온다.
                player = (GamePlayer_Server)room.playerList.elementAt(i);
                message += player.id+",";
            }
      
            // 사용자 리스트를 생성하면서 마지막으로 추가된 ',' 를 제거해야 한다.
            if(message.endsWith(",")) {
                message = message.substring(0, message.length()-1);
            }

            // 주어진 아이디를 갖는 클라이언트의 GamePlayer_Server 객체를 얻어와
            // 사용자 리스트 메시지를 전달한다.
            player = (GamePlayer_Server)clientList.get(playerId);
            if(player != null) {
            	player.sendMessage(message);  // 메시지 전송
            }

        } catch(Exception e) {
        }
    }
   
    // 주어진 아이디의 플레이어에게 룸 리스트를 보낸다.
    public void sendRoomList(GamePlayer_Server player) {
    	try {
    		String message=null;

    		message = "대기실 (참여인원:"+clientList.size()+"),";
    		if(roomList.isEmpty()) {
    			//room is empty
    		} else {
    			Enumeration e = roomList.elements();
    			while(e.hasMoreElements()) {
    				GameRoom_Server room = (GameRoom_Server)e.nextElement();
    				// 룸이름, 참여인원, 게임참가인원 등의 정보를 포함
    				message += room.roomId+" (전체참여인원["
    						+ room.playerList.size()
    						+ "]/게임참가중["
    						+ room.gamingManList.size() + "]),";
    			}
    			// 룸 리스트를 생성하면서 마지막으로 추가된 ',' 를 제거해야 한다.
    			if(message.endsWith(",")) {
    				message = message.substring(0, message.length()-1);
    			}
    		}

    		// 메시지를 완성한다.
    		message = "230|"+message;

    		// 주어진 아이디를 갖는 클라이언트의 GamePlayer_Server 객체를 얻어와
    		// 사용자 리스트 메시지를 전달한다.
    		if(player != null) {
    			player.sendMessage(message);  // 메시지 전송
    		}
    	} catch(Exception e) {
    	}
    }

    // 대기실 내에 있는 전체에게 룸 리스트를 전달한다.
    public void broadcastRoomList() {
    	GamePlayer_Server player=null;

    	// 대기실의 클라이언트 리스트 내에 있는 각 클라이언트에게 
    	// 룸 리스트를 보내면 된다.
    	Enumeration e = clientList.elements();
    	while(e.hasMoreElements()) {
    		// 대기실의 클라이언트 리스트에서 하나의 GamePlayer_Server 객체를 가져온다.
    		player = (GamePlayer_Server)e.nextElement();
    		if(player != null) {
    			// 해당 클라이언트에게 방 리스트를 보낸다.
    			sendRoomList(player);
    		}
    	}
    }
   
    // 주어진 아이디를 갖는 클라이언트가
    // 주어진 이름의 룸을 생성한다.
    public int makeRoom(String playerId, String roomId) {
    	GamePlayer_Server player = (GamePlayer_Server)clientList.get(playerId);

    	//동일한 룸이름을 갖는 룸이 있는지 검사
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
    	// roomId, player 정보를 이용하여 룸을 관리하기 위한 GameRoom 객체를 생성한다.
    	room = new GameRoom_Server(this,roomId, player);

    	// 대기실 서버가 가지고 있는 룸 리스트에 방금 생성한 룸 객체를 추가한다.
    	roomList.put(roomId, room);

    	// 룸 서버 객체는 룸 내에 있는 클라이언트로부터 온 데이터를
    	// 주기적으로 체크해야 하므로, 스레드로서 동작한다.
    	// 따라서, 룸 서버 객체를 생성한 후에는 반드시 start() 메소들 호출하여
    	// 스레드를 시작시켜 주어야 한다.
    	room.start();
    	//방 정보가 변경했으니 다시 방 리스트를 브로드캐스트한다.
    	broadcastRoomList();
   
    	// server message state for test
        String message = "["+playerId+"] 님의 요청에 의해 방이 만들어졌습니다.\n";
        parent.server_state.messageArea.append(message);
        
    	return 0;
    }

    // 주어진 플레이어가 대기실에서 룸을 선택하여 룸으로 들어가는 것을 처리한다.
    public int enterRoom(String playerId, String roomId) {
    	// 주어진 아이디의 룸 객체를 얻는다.
    	GameRoom_Server room = (GameRoom_Server)roomList.get(roomId);

    	if(room != null) {
    		GamePlayer_Server player = (GamePlayer_Server)clientList.get(playerId);
    		
    		if(room.playerList.size() < room.MAX_PLAYER_NUMBER){
    			String message = "250|SUCCESS";
				sendMessage(playerId,message);
        		// 대기실 객체의 클라이언트 리스트에서 주어진 플레이어를 제거한 후,
        		removePlayer(player);

        		// 룸 객체의 클라이언트 리스트에 추가한다.
        		room.addPlayer(player);

        		// 클라이언트가 대기실에서 룸으로 이동했으므로,
        		// 대기실 내의 모두에게 주어진 룸 정보를 갱신할 수 있도록 한다.
        		broadcastLobbyUserList();
        		
        		// server message state for test
                message = "["+playerId+"]님이 ["+roomId+"]에 입장하였습니다..\n";
                parent.server_state.messageArea.append(message);
        		
        		return 0;
    		} else {
    			return -1;
    		}
    	} else {
    		return -1;
    	}
    }

    // 주어진 아이디의 룸을 대기실 객체의 룸 리스트에서 제거한다.
    public void removeRoom(String roomId) {
    	// 주어진 아이디의 룸 객체를 얻는다.
    	GameRoom_Server room = (GameRoom_Server)roomList.get(roomId);

    	if(room != null) {       // 대기실 객체에 있는 룸 리스트에서 주어진 아이디의 룸 객체를 제거한다.
    		roomList.remove(roomId);
    		// 룸이 제거되었으므로, 대기실 내의 모두에게 룸 리스트를 갱신하도록 한다.
    		broadcastRoomList();
    		
    		// server message state for test
            String message = "["+roomId+"] 룸이 제거되었습니다.\n";
            parent.server_state.messageArea.append(message);
    	}
    }


    // 주어진 아이디(who)의 클라이언트에 대한 프로파일 정보를 
    // 주어진 아이디를 갖는 클라이언트(to)에게 보낸다.
    public void sendProfile(String to, String who) {
    	GamePlayer_Server whoPlayer=null, toPlayer=null;
    	try {
    		// 주어진 아이디(who)의 클라이언트 객체를 얻는다.
    		whoPlayer = getPlayer(who);
    		// 주어진 아이디(to)의 클라이언트 객체를 얻는다.
    		toPlayer = getPlayer(to);
    		String profileMessage = "260|"+whoPlayer.getProfileMessage(who);
    		toPlayer.sendMessage(profileMessage);
    	} catch(Exception e) {
    	}
    }

    // 룸에 있던 클라이언트가 다시 대기실로 돌아오려 할 경우,
    // 대기실의 클라이언트 리스트에 해당 클라이언트를 다시 추가한다.
    public void takePlayer(GamePlayer_Server player) {
    	int i=0;
    	// 대기실의 클라이언트 리스트에 해당 클라이언트를 다시 추가한다.
    	clientList.put(player.id, player);

    	// 대기실내의 모두에게 갱신된 사용자 리스트를 보낸다.
        broadcastLobbyUserList();
        broadcastRoomList();
        
        String message;
        message = player.id+"님이 대기실에 입장하셨습니다.\n";
        parent.server_state.messageArea.append(message);
    }

    /*
     * 대기실에 있는 클라이언트(플레이어)들을 관리하기 위한 스레드 서버
     * 대기실에서는 대기실 내에 있는 클라이언트는 물론 개설된 룸도 관리한다.
     */
    public void run() {
    	GamePlayer_Server player = null;
    	// 대기실 스레드 서버 객체는 클라이언트 리스트에 등록되어 있는 
    	// 클라이언트들로부터 온 메시지를 주기적으로 체크하여 읽어온다.
    	// 따라서, 스레드로서 동작하며, 무한루프를 갖는다.
    	while(true) {
    		// Read Data
    		try {
    			// CPU를 독식하는 스레드가 되는 것을 방지하기 위해 딜레이를 준다. 
    			sleep(100);
    		} catch(InterruptedException ie) {
    		}

    		// 클라이언트 리스트에 등록되어 있는 클라이언트 객체를 하나씩 가져오기
    		// 위해 clientList 해시테이블로부터 Enumeration 객체를 얻는다.
    		Enumeration e = clientList.elements();
    		while(e.hasMoreElements()) {
    			// Enumeration 객체로부터 하나의 클라이언트 객체를 얻어온다. 
    			player = (GamePlayer_Server)e.nextElement();
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

    				// Mafia Protocol
    				// command|arg1|arg2|arg3|...
    				StringTokenizer st = new StringTokenizer(receive, "|");
    				int protocol = Integer.parseInt(st.nextToken());
    				switch(protocol){
    				case 100:{
    					// 브로드캐스트 채팅
    					// 100|fromId|chatMessage
    					String fromId = st.nextToken();
    					String chatMessage = st.nextToken();
    					String message = "200|"+fromId+"|"+chatMessage;
    					broadcastMessage(message);
    					
    					// server message state for test
    		            message = "["+fromId+"]님이 전체에게 메시지를 보내셨습니다.\n";
    		            parent.server_state.messageArea.append(message);
    				} break;
    				case 101:{ 
    					// //유니캐스트 채팅
    					// 101|fromId|toId|chatMessage
    					String fromId = st.nextToken();
    					String toId = st.nextToken();
    					String chatMessage = st.nextToken();
    					String message;
    					message = "201|"+fromId+"|"+chatMessage;
    					
    					sendMessage(toId, message);   // 대기실 내에서 유니캐스팅
    					
    					// server message state for test
    		            message = "["+fromId+"]님이 "+"["+toId+"]님에게 메시지를 보내셨습니다.\n";
    		            parent.server_state.messageArea.append(message);
    				} break;
    				case 110:{ 
    					// 연결 설정 요청 - 새로운 클라이언트의 연결을 의미
    					// 편의를 위해 add player 부분에서 담당
    				} break;
    				case 111:{ 
    					// 연결 해제
    					// 111|fromId
    					String fromId = st.nextToken();
    					// 클라이언트의 퇴장 메시지를 브로드캐스팅
    					String message = "200|"+fromId+"|["+fromId+"]님이 퇴장하셨습니다.";
    					broadcastMessage(message);
    					// 퇴장 클라이언트에게 잘가라는 메시지 전달
    					message = "211|["+fromId+"]님 안녕히 가세요.";
    					sendMessage(fromId, message);
    					
    					// server message state for test
    		            message = "["+fromId+"]님이 퇴장하셧습니다..\n";
    		            parent.server_state.messageArea.append(message);
    				} break;
    				case 120:{   
    					// 대기실 사용자 리스트 요청
    					// 120|fromId
    					String fromId = st.nextToken();
    					sendLobbyUserList(fromId);
    				} break;
    				case 121:{ 
    					// 룸 사용자 리스트 요청
    					// 121|fromId|roomId
    					String fromId = st.nextToken();
    					String roomId = st.nextToken();
    					sendRoomUserList(fromId,roomId);
    				} break;
    				case 130:{  
    					// 룸 리스트 요청
    					// 130|fromId
    					String fromId = st.nextToken();
                        sendRoomList(getPlayer(fromId));
    				} break;
    				case 140:{ 
    					// 룸 생성
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
    						message = "240|SUCCESS";  // 방만들기 결과
    						// 룸을 만든 클라이언트를 대기실 리스트에서 삭제한다.
        					sendMessage(fromId,message);
        					removePlayer(player);
    					}
    					broadcastRoomList();
    				} break;
    				case 150:{         
    					//룸 입장
    					// 150|fromId|roomId
    					String fromId = st.nextToken();
    					String roomId = st.nextToken();
    					
    					int success = enterRoom(fromId, roomId);
    					String message;
    					
    					if(success == 0){
        					// 입장 성공 여부는  enterRoom 메소드 안에서 메시지로 보낸다.
    						// 이는 enterRoom에서 이미 클라이언트가 룸으로 넘어가기 때문에
    						// 넘어가기 전에 메시지를 보내야 한다.
        					// 해당 룸 객체(room)의 메소드를 이용하여 룸 내에 있는
        					// 클라이언트에게 현재 상황을 갱신한다.
        					room.broadcastUserList();
        					broadcastRoomList();
    					} else {
    						message = "250|FAIL";
    						sendMessage(fromId,message);
    					}
    				} break;
    				case 160:{         
    					// 프로파일 요청
    					// 160|fromId|whoId
    					String fromId = st.nextToken();
    					String whoId = st.nextToken();
    					sendProfile(fromId, whoId);
    				} break;
    				}   
    			} catch(SocketException ne) {
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