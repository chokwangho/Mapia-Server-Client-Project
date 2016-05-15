import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;

import javax.swing.JPopupMenu;

public class Lobby_Client extends Panel{
	// 기본적인 컴퍼넌트 이외에 Horse_Client 객체를 생성하여 사용함.
	Mafia_Client parentPanel=null;	 	   
	String roomId=null, selectRoom=null;
	String selectUser="모두에게";
	
    Button makeButton = null, joinButton = null, exitButton = null, searchButton=null;
	Button lawButton = null, toptenButton = null;
	TextArea chatArea = null;
	TextField chat = null;
	List roomList = null, userList = null;
	Choice bxsecretTalk = null; //귓속말
	Image backImage; 
	JPopupMenu popupmenu;
	
	// 생성자에서는 Horse_Client 객체를 받고 GUI 초기화를 담당하는 init() 
	// 메소드 실행과, 배경화면에 쓰일 이미지 생성 
	public Lobby_Client(Mafia_Client parentPanel) {
		this.parentPanel = parentPanel;
		init();
		backImage = Toolkit.getDefaultToolkit().createImage("60lobby_panel.jpg");
	}
	   
	// 초기 GUI 컴퍼넌트들을 구성할 메소드 
	public void init(){
		setLayout(null);
		setSize(950,700);
		
		roomList = new List(12, false); //방리스트화면
		roomList.setForeground(Color.blue);
		userList = new List(12, false); //유저리스트화면
		userList.setForeground(Color.blue);
		chatArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY); //채팅창
		chat = new TextField(15); //채팅할수있는 텍스트에어리어

		makeButton = new Button("방만들기");
		joinButton = new Button("게임참여");
		exitButton = new Button("종료하기");
		lawButton = new Button("도움말");
		//searchButton = new Button("ID 검색"); // 없애기
		//toptenButton = new Button("명예의전당"); //없애기
		      
		bxsecretTalk = new Choice(); //귓속말
		bxsecretTalk.setBounds(70,617,100,23);
		bxsecretTalk.setBackground(new Color(238,235,235));
 
		roomList.setBounds(70, 170, 520, 290);
		roomList.setBackground(new Color(238,235,235));
		userList.setBounds(720, 170, 170, 180);
		userList.setBackground(new Color(238,235,235));

		chatArea.setBounds(70, 500, 520, 92);
		chatArea.setBackground(new Color(238,235,235));
		chat.setBounds(180, 617, 410, 23);
		chat.setBackground(Color.WHITE);

		/*searchButton.setBounds(720,430,80,30);
		searchButton.setBackground(new Color(244,164,96));
		searchButton.setFont(new Font("",Font.ROMAN_BASELINE,12));*/
		/*toptenButton.setBounds(810, 430, 80, 30);
		toptenButton.setBackground(new Color(244,164,96));//244;164;96 
		toptenButton.setFont(new Font("",Font.ROMAN_BASELINE,12));*/
		      
		makeButton.setBounds(80, 130, 80, 30);
		makeButton.setBackground(new Color(244,164,96));
		makeButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		joinButton.setBounds(170, 130, 80, 30);
		joinButton.setBackground(new Color(244,164,96));
		joinButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		lawButton.setBounds(720, 610, 80, 30);
		lawButton.setBackground(new Color(244,164,96));
		lawButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		exitButton.setBounds(810, 610, 80, 30);
		exitButton.setBackground(new Color(244,164,96));
		exitButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		                 
		popupmenu=new JPopupMenu();

		add(roomList);
		add(userList);
		add(chatArea);
		add(chat);
		add(makeButton);
		add(joinButton);
		add(exitButton);
		add(lawButton);
		//add(searchButton);
		//add(toptenButton);
		add(bxsecretTalk);
		     
		// 포커스는 chat 입력을 하게될 TextField에 설정 
		FocusListener fl = new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				chat.requestFocus();
			}
		};
		   
		chatArea.addFocusListener(fl);
		roomList.addFocusListener(fl);
		userList.addFocusListener(fl);
		       		       
		joinButton.setEnabled(false);
		
		// 방 목록에 있는 리스트들 중에 하나를 클릭했을 시에 우측의 사용자 리스트를 변환시키게 할 이벤트 
		roomList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					selectRoom = roomList.getSelectedItem();
					if(selectRoom != null) {
						selectRoom = selectRoom.substring(0, selectRoom.lastIndexOf('(')-1).trim();
						if("대기실".equals(selectRoom)){ // 대기실 사용자 리스트 요청 
							sendToServer("120|"+parentPanel.id);
							joinButton.setEnabled(false);
						}
						else{                           // 해당 룸 사용자 리스트 요청 
							sendToServer("121|"+parentPanel.id+"|"+selectRoom);           
							joinButton.setEnabled(true);
						}
					}
				}
			}
		});
		   
		//귓말을 하기위한 사용자를 선택하능한  콤보박스에 선택된 부분의 이름을 String으로 변환함.
		bxsecretTalk.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					selectUser = (String)bxsecretTalk.getSelectedItem();
				}
			}
		});
		   
		//이벤트가 발생했을때 서버에 메세지를 보내는 부분을 모두 다루는 루틴 
		//Chat 메세지를 서버에 보내거나, 귓속말(해당사용자에게)을 보내거나, 
		//방만들기, 방 접속하기, 도움말 보기, 현재 게임순위보기,
		//게임종료버튼 
		ActionListener wa = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == chat) {
					String message = chat.getText();
					if(selectUser.equals("모두에게")) {  // 따로 선택된 유저가 없을 시 
						if(message != null && !"".equals(message)){
							sendToServer("100|"+parentPanel.id+"|"+message);
						}                               // 브로드캐스트  채팅이 됨  
					} else {
						if(message != null && !"".equals(message)) {
							sendToServer("101|"+parentPanel.id+"|"+selectUser+"|"+message);
						}                               // 아니면 유니캐스트 채팅이 됨 
					}
		               
					chat.setText("");
				}  else if(e.getSource() == makeButton) {
					makeButton.setEnabled(false);
					joinButton.setEnabled(false);
					exitButton.setEnabled(false);       // 방을 만들기 위한 방이름을 입력받을 다이얼로그 생성
					TextDialog s = new TextDialog(parentPanel, "방 만들기...", "방 이름을 입력하세요...");
					s.show();

					if(!"".equals(s.field.getText().trim())) {
						roomId = s.field.getText().trim();
						sendToServer("140|"+parentPanel.id+"|"+roomId);
					} else {                            // 방 생성을 알림  
						makeButton.setEnabled(true);
						joinButton.setEnabled(true);
						exitButton.setEnabled(true);
					}		               
				} else if(e.getSource() == joinButton) {
					selectRoom = roomList.getSelectedItem();
					selectRoom = selectRoom.substring(0, selectRoom.lastIndexOf('(')-1).trim();
					if(selectRoom != null) {            // 방 입장을 알림  
						makeButton.setEnabled(false);
						joinButton.setEnabled(false);
						exitButton.setEnabled(false);
						sendToServer("150|"+parentPanel.id+"|"+selectRoom);
					}
				} else if(e.getSource() == lawButton) {  
													    // 도움말 다이얼로그 생성  
					HelpDialog h = new HelpDialog(parentPanel, "Help","도움말 입니다.");
					h.show();
				} /*else if(e.getSource() == toptenButton) {
													    // 랭킹 다이얼로그 생성  
					RankDialog r = new RankDialog(parentPanel, "Current Ranking", "현재 랭킹 순위입니다.");
					r.show();
				}*/ else if(e.getSource() == exitButton) {
					try {                               // 종료 버튼시 클라이언트 시스템 종료 
						sendToServer("111|"+parentPanel.id);
						parentPanel.destroy();
					}
					catch(Exception ee){
					}
					finally{
						System.exit(1);   
					}	
				}
			}
		};

		chat.addActionListener(wa);
		makeButton.addActionListener(wa);
		joinButton.addActionListener(wa);
		exitButton.addActionListener(wa);
		lawButton.addActionListener(wa);
		//toptenButton.addActionListener(wa);
		userList.addActionListener(wa);		      
	}	
	
	// 배경화면을 그리게 될 메소드 
	public void paint(Graphics g) {
		g.drawImage(backImage, 0, 0, this);
	}
	
	// Horse_Client내에 있는 메소드에서 서버에 메세지 보내는 처리를 담당  
	public void sendToServer(String message) {
	     parentPanel.sendToServer(message);
	}
	
	// 서버로 부터온 룸 리스트를 해당 GUI(roomlist)에 적용 
	public void showRoomList(String roomIdList) {
	 	  if(roomList == null) {
			  roomList = new List(12, false);
	       } else if(roomList.getItemCount() > 0) {
	    	   roomList.removeAll();
	       }

	       StringTokenizer sst = new StringTokenizer(roomIdList, ",");
	       while(sst.hasMoreTokens()) {
	          try {
	             String room = sst.nextToken();
	             if(room != null) {
	                roomList.add(room);
	             }
	          } catch(Exception ee) {
	          }
	       } 
	}
	   	
	// 서버로 부터 온 접속해 있는 클라이언트  리스트를 해당 GUI(userlist)에 적용 
	public void showUserList(String IdList) {
		bxsecretTalk.removeAll();
		bxsecretTalk.addItem("모두에게");
		if(userList == null) {
			userList = new List(12, false);
		} else if(userList.getItemCount() > 0) {
			userList.removeAll();
		}
		StringTokenizer sst = new StringTokenizer(IdList, ",");
		while(sst.hasMoreTokens()) {
			try {
				String user = sst.nextToken();
				if(user != null && !"".equals(user)) {
					userList.add(user);
					bxsecretTalk.addItem(user);
				}
			} catch(Exception ee) {
			}
		}
	}
	   	  
	// 서버에서  온 메세지를 프로토콜에 맞게 해당 GUI 컴퍼넌트에 처리하는 부분	  	
	public void processMessage(String receive) {		  
		StringTokenizer st = new StringTokenizer(receive, "|");	
		int protocol = Integer.parseInt(st.nextToken());
		switch(protocol){	
		case 200: {
			// 브로드캐스트 메시지 처리
			// 200|fromId|chatMessage
			String fromId = st.nextToken();
			String chat = st.nextToken();
			chatArea.append("[" + fromId + "] : " + chat + "\n");
		} break;
		case 201: {
			// 유니캐스트 메시지 처리
			// 201|fromId|chatMessage
			String fromId = st.nextToken();
			String chat = st.nextToken();
			chatArea.append("["+fromId+"]님으로부터 온 귓속말 : "+chat +"\n");
		} break;
		case 210: {
			// 연결 설정 환영 메시지 처리
			// 210|comment
			// 210|SUCCESS|win_count|lose_count|total_count|get_money|lose_money|total_money
			// 210|FAIL|REASON(NOTID or NOTPWD)
			String comment=st.nextToken();
			if(comment.equals("FAIL")){
				comment=st.nextToken();
				if(comment.equals("NOTID")){
					//message
					parentPanel.MafiaCardLayout.previous(parentPanel.cardPanel);
					parentPanel.loginPanel.exitButton.setEnabled(true);
				} else if(comment.equals("NOTPWD")){
					parentPanel.MafiaCardLayout.previous(parentPanel.cardPanel);
					parentPanel.loginPanel.exitButton.setEnabled(true);
				} else {
					chatArea.append("죄송합니다.\n");
					chatArea.append("지금은 접속이 불가능 합니다.\n");
					chatArea.append("프로그램을 종료합니다.\n");
					chatArea.append("안녕히 계세요!!!\n");
					WarningDialog dlg = new WarningDialog(parentPanel,"Warning : Program Exit","지금은 접속이 불가합니다.|프로그램을 종료합니다.|안녕히 계세요...","EXIT");
					dlg.show();
					System.exit(1);
				}
			} else if(comment.equals("SUCCESS")){
				chatArea.append("성공적으로 접속이 이루어졌습니다. ^ _ ^\n");
				
			} else{
				chatArea.append(comment+"\n"); 
			}
		} break;
		case 220: {
			// 요청한 로비 사용자 리스트 처리
			// 220|id1,id2,id3,id4... 
			String userIds=st.nextToken();
			showUserList(userIds);
		} break;
		case 221: {
			// 요청한 룸 사용자 처리
			// 221|id1,id2,id3...
			String userIds=st.nextToken();
			showUserList(userIds);
		} break;
		case 230: {
			// 요청한 룸 리스트 처리
			// 230|roomId1,roomId2,roomId3...
			String roomIds=st.nextToken();
			showRoomList(roomIds);
		} break;
		case 240: {
			// 방 만들기 처리 결과 표시
			// 240|SUCCESS(FAIL)
			String result=st.nextToken();
		          
			if(result.equals("SUCCESS")){ //SUCCESS
				parentPanel.gamePanel.initComponent();
				parentPanel.setVisible(false);
				parentPanel.MafiaCardLayout.next(parentPanel.cardPanel);
				parentPanel.setVisible(true);
				parentPanel.currentRoomIsLobby = false;				
				sendToServer("310|"+parentPanel.id);
			}else{ //FAIL
				WarningDialog dlg = new WarningDialog(parentPanel,"Warning : 방제가 중복되었습니다.","죄송합니다.|같은 이름의 방제가 있습니다.|다시 만들어 주세요","NOTEXIT");
				dlg.show();
				makeButton.setEnabled(true);
				exitButton.setEnabled(true);
			}
		} break;
		case 250: {
			// 방 참여하기 처리 결과 표시
			// 250|SUCCESSFAIL)
			String result=st.nextToken();		          
			if(result.equals("SUCCESS")){
				chatArea.append("sssssss\n");
				parentPanel.gamePanel.initComponent();		    		  
				parentPanel.setVisible(false);
				parentPanel.MafiaCardLayout.next(parentPanel.cardPanel);
				parentPanel.setVisible(true);
				parentPanel.currentRoomIsLobby = false;
		          
				sendToServer("310|"+parentPanel.id);
			}
			else {
				chatArea.append("방인원을 초과하였거나 다른 이유로 인해서 참가할 수 없습니다.\n");
				makeButton.setEnabled(true);
				exitButton.setEnabled(true);
			}
		} break;
		}
	}
}