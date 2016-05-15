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
	// �⺻���� ���۳�Ʈ �̿ܿ� Horse_Client ��ü�� �����Ͽ� �����.
	Mafia_Client parentPanel=null;	 	   
	String roomId=null, selectRoom=null;
	String selectUser="��ο���";
	
    Button makeButton = null, joinButton = null, exitButton = null, searchButton=null;
	Button lawButton = null, toptenButton = null;
	TextArea chatArea = null;
	TextField chat = null;
	List roomList = null, userList = null;
	Choice bxsecretTalk = null; //�ӼӸ�
	Image backImage; 
	JPopupMenu popupmenu;
	
	// �����ڿ����� Horse_Client ��ü�� �ް� GUI �ʱ�ȭ�� ����ϴ� init() 
	// �޼ҵ� �����, ���ȭ�鿡 ���� �̹��� ���� 
	public Lobby_Client(Mafia_Client parentPanel) {
		this.parentPanel = parentPanel;
		init();
		backImage = Toolkit.getDefaultToolkit().createImage("60lobby_panel.jpg");
	}
	   
	// �ʱ� GUI ���۳�Ʈ���� ������ �޼ҵ� 
	public void init(){
		setLayout(null);
		setSize(950,700);
		
		roomList = new List(12, false); //�渮��Ʈȭ��
		roomList.setForeground(Color.blue);
		userList = new List(12, false); //��������Ʈȭ��
		userList.setForeground(Color.blue);
		chatArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY); //ä��â
		chat = new TextField(15); //ä���Ҽ��ִ� �ؽ�Ʈ�����

		makeButton = new Button("�游���");
		joinButton = new Button("��������");
		exitButton = new Button("�����ϱ�");
		lawButton = new Button("����");
		//searchButton = new Button("ID �˻�"); // ���ֱ�
		//toptenButton = new Button("��������"); //���ֱ�
		      
		bxsecretTalk = new Choice(); //�ӼӸ�
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
		     
		// ��Ŀ���� chat �Է��� �ϰԵ� TextField�� ���� 
		FocusListener fl = new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				chat.requestFocus();
			}
		};
		   
		chatArea.addFocusListener(fl);
		roomList.addFocusListener(fl);
		userList.addFocusListener(fl);
		       		       
		joinButton.setEnabled(false);
		
		// �� ��Ͽ� �ִ� ����Ʈ�� �߿� �ϳ��� Ŭ������ �ÿ� ������ ����� ����Ʈ�� ��ȯ��Ű�� �� �̺�Ʈ 
		roomList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					selectRoom = roomList.getSelectedItem();
					if(selectRoom != null) {
						selectRoom = selectRoom.substring(0, selectRoom.lastIndexOf('(')-1).trim();
						if("����".equals(selectRoom)){ // ���� ����� ����Ʈ ��û 
							sendToServer("120|"+parentPanel.id);
							joinButton.setEnabled(false);
						}
						else{                           // �ش� �� ����� ����Ʈ ��û 
							sendToServer("121|"+parentPanel.id+"|"+selectRoom);           
							joinButton.setEnabled(true);
						}
					}
				}
			}
		});
		   
		//�Ӹ��� �ϱ����� ����ڸ� �����ϴ���  �޺��ڽ��� ���õ� �κ��� �̸��� String���� ��ȯ��.
		bxsecretTalk.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					selectUser = (String)bxsecretTalk.getSelectedItem();
				}
			}
		});
		   
		//�̺�Ʈ�� �߻������� ������ �޼����� ������ �κ��� ��� �ٷ�� ��ƾ 
		//Chat �޼����� ������ �����ų�, �ӼӸ�(�ش����ڿ���)�� �����ų�, 
		//�游���, �� �����ϱ�, ���� ����, ���� ���Ӽ�������,
		//���������ư 
		ActionListener wa = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == chat) {
					String message = chat.getText();
					if(selectUser.equals("��ο���")) {  // ���� ���õ� ������ ���� �� 
						if(message != null && !"".equals(message)){
							sendToServer("100|"+parentPanel.id+"|"+message);
						}                               // ��ε�ĳ��Ʈ  ä���� ��  
					} else {
						if(message != null && !"".equals(message)) {
							sendToServer("101|"+parentPanel.id+"|"+selectUser+"|"+message);
						}                               // �ƴϸ� ����ĳ��Ʈ ä���� �� 
					}
		               
					chat.setText("");
				}  else if(e.getSource() == makeButton) {
					makeButton.setEnabled(false);
					joinButton.setEnabled(false);
					exitButton.setEnabled(false);       // ���� ����� ���� ���̸��� �Է¹��� ���̾�α� ����
					TextDialog s = new TextDialog(parentPanel, "�� �����...", "�� �̸��� �Է��ϼ���...");
					s.show();

					if(!"".equals(s.field.getText().trim())) {
						roomId = s.field.getText().trim();
						sendToServer("140|"+parentPanel.id+"|"+roomId);
					} else {                            // �� ������ �˸�  
						makeButton.setEnabled(true);
						joinButton.setEnabled(true);
						exitButton.setEnabled(true);
					}		               
				} else if(e.getSource() == joinButton) {
					selectRoom = roomList.getSelectedItem();
					selectRoom = selectRoom.substring(0, selectRoom.lastIndexOf('(')-1).trim();
					if(selectRoom != null) {            // �� ������ �˸�  
						makeButton.setEnabled(false);
						joinButton.setEnabled(false);
						exitButton.setEnabled(false);
						sendToServer("150|"+parentPanel.id+"|"+selectRoom);
					}
				} else if(e.getSource() == lawButton) {  
													    // ���� ���̾�α� ����  
					HelpDialog h = new HelpDialog(parentPanel, "Help","���� �Դϴ�.");
					h.show();
				} /*else if(e.getSource() == toptenButton) {
													    // ��ŷ ���̾�α� ����  
					RankDialog r = new RankDialog(parentPanel, "Current Ranking", "���� ��ŷ �����Դϴ�.");
					r.show();
				}*/ else if(e.getSource() == exitButton) {
					try {                               // ���� ��ư�� Ŭ���̾�Ʈ �ý��� ���� 
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
	
	// ���ȭ���� �׸��� �� �޼ҵ� 
	public void paint(Graphics g) {
		g.drawImage(backImage, 0, 0, this);
	}
	
	// Horse_Client���� �ִ� �޼ҵ忡�� ������ �޼��� ������ ó���� ���  
	public void sendToServer(String message) {
	     parentPanel.sendToServer(message);
	}
	
	// ������ ���Ϳ� �� ����Ʈ�� �ش� GUI(roomlist)�� ���� 
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
	   	
	// ������ ���� �� ������ �ִ� Ŭ���̾�Ʈ  ����Ʈ�� �ش� GUI(userlist)�� ���� 
	public void showUserList(String IdList) {
		bxsecretTalk.removeAll();
		bxsecretTalk.addItem("��ο���");
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
	   	  
	// ��������  �� �޼����� �������ݿ� �°� �ش� GUI ���۳�Ʈ�� ó���ϴ� �κ�	  	
	public void processMessage(String receive) {		  
		StringTokenizer st = new StringTokenizer(receive, "|");	
		int protocol = Integer.parseInt(st.nextToken());
		switch(protocol){	
		case 200: {
			// ��ε�ĳ��Ʈ �޽��� ó��
			// 200|fromId|chatMessage
			String fromId = st.nextToken();
			String chat = st.nextToken();
			chatArea.append("[" + fromId + "] : " + chat + "\n");
		} break;
		case 201: {
			// ����ĳ��Ʈ �޽��� ó��
			// 201|fromId|chatMessage
			String fromId = st.nextToken();
			String chat = st.nextToken();
			chatArea.append("["+fromId+"]�����κ��� �� �ӼӸ� : "+chat +"\n");
		} break;
		case 210: {
			// ���� ���� ȯ�� �޽��� ó��
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
					chatArea.append("�˼��մϴ�.\n");
					chatArea.append("������ ������ �Ұ��� �մϴ�.\n");
					chatArea.append("���α׷��� �����մϴ�.\n");
					chatArea.append("�ȳ��� �輼��!!!\n");
					WarningDialog dlg = new WarningDialog(parentPanel,"Warning : Program Exit","������ ������ �Ұ��մϴ�.|���α׷��� �����մϴ�.|�ȳ��� �輼��...","EXIT");
					dlg.show();
					System.exit(1);
				}
			} else if(comment.equals("SUCCESS")){
				chatArea.append("���������� ������ �̷�������ϴ�. ^ _ ^\n");
				
			} else{
				chatArea.append(comment+"\n"); 
			}
		} break;
		case 220: {
			// ��û�� �κ� ����� ����Ʈ ó��
			// 220|id1,id2,id3,id4... 
			String userIds=st.nextToken();
			showUserList(userIds);
		} break;
		case 221: {
			// ��û�� �� ����� ó��
			// 221|id1,id2,id3...
			String userIds=st.nextToken();
			showUserList(userIds);
		} break;
		case 230: {
			// ��û�� �� ����Ʈ ó��
			// 230|roomId1,roomId2,roomId3...
			String roomIds=st.nextToken();
			showRoomList(roomIds);
		} break;
		case 240: {
			// �� ����� ó�� ��� ǥ��
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
				WarningDialog dlg = new WarningDialog(parentPanel,"Warning : ������ �ߺ��Ǿ����ϴ�.","�˼��մϴ�.|���� �̸��� ������ �ֽ��ϴ�.|�ٽ� ����� �ּ���","NOTEXIT");
				dlg.show();
				makeButton.setEnabled(true);
				exitButton.setEnabled(true);
			}
		} break;
		case 250: {
			// �� �����ϱ� ó�� ��� ǥ��
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
				chatArea.append("���ο��� �ʰ��Ͽ��ų� �ٸ� ������ ���ؼ� ������ �� �����ϴ�.\n");
				makeButton.setEnabled(true);
				exitButton.setEnabled(true);
			}
		} break;
		}
	}
}