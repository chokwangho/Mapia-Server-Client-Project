import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.SocketException;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//실제 게임 화면 구성을 이루는 부분이다.
//크게 두 부분으로 이루어지는 데, 기본 인터페이스 부분과 게임화면 부분이다.
//게임 화면 부분은 하나의 패널로 만들어 전체 화면에 붙여넣는 형식으로 했다.
//그렇게 한 이유는 쉽게 모듈화를 이루기 위해서였다.
//기본 인터페이스 부분은  채팅과 배팅정보 등등을 위한 영역이다.

public class Game_Client extends Panel {
   // 기본 화면구성에 필요한 컴퍼넌트들과 , Mafia_Client 스레드 클래스의 객체와
   // Racing (게임 GUI) 객체를 사용함.
   
   mafiaday mafiaday;
   int batting[];            // 배팅금액 환산에 필요한 int 배열
   Mafia_Client parentPanel=null;
   long old_money;             // 배팅금액에 관련한 long형 변수
   Button joinButton=null, exitButton=null, readyButton = null, exitButtonB=null;
   List UserList=null;
   TextArea boardChatArea=null;
   TextField boardChatField=null;
   Panel pan=null, sidePan=null, sidePan1=null, sidePanBtn=null;
   Choice bxsecretTalk =null;
   TextArea boardUserinfo = null;
   Image backImage; 
   String num[] = new String[8];

   Panel imagepanel[] = new Panel[8];
   JLabel UserImage[]=new JLabel[8];//= new Label[8];//사용자레이블
   Label UserIdText[]=new Label[8];// = new Label[8]; //아이디 텍스트 받는 부분
   ImageIcon imagecitizen = new ImageIcon("mafia/mafia5.png");
   ImageIcon imagepolice = new ImageIcon("mafia/mafia3.png");
   ImageIcon imagewho = new ImageIcon("mafia/mafia1.png");
   ImageIcon imagemafia = new ImageIcon("mafia/mafia2.png");
   ImageIcon imagedoctor = new ImageIcon("mafia/mafia4.png");
   ImageIcon imagemafiadeath = new ImageIcon("mafia/mafia6.png");
   int countmafia=2;
   int countdoctor=1;
   int countpolice=1;
   int countcitizen=4;
   int countvictory;
   String mafiatemp;
   int mafiatempint;
   // 컴퍼넌트 제어에 필요한 플래그들, 접속중인지, 준비중인지, 방장인지 설정함.
   boolean onJoin=false;        
   boolean onReady=false;
   boolean isMaster=false;
      
   String selectUser="모두에게";
   
   // 생성자에서는 Mafia_Client 클래스 객체와 기본 컴퍼넌트를 구성할 init()메서드 실행
   // 배팅금액 환산에 필요한 배열 초기화와 화면 바탕에 이미지를 생성.
   public Game_Client(Mafia_Client parentPanel) {
      this.parentPanel = parentPanel;
      init();
      batting = new int[8];
      for(int i=0;i<8;i++){
         batting[i]=0;
      }   
      backImage = Toolkit.getDefaultToolkit().createImage("85game_panel.jpg");
   }
      
   // 클라이언트가 이 방(게임 진행 패널, Game_Client)에 들어옴과 동시에 
   // 필요한 컴퍼넌트 제어 과정. 
   public void initComponent() {        
      boardChatArea.setText("");     
      UserList.removeAll();          // UserList와 Chat에 관련한 
      boardChatField.setText("");    // Area, Field들은 모두 디폴트로 
                                       // 아무것도 없이 설정
      joinButton.setEnabled(false);  //접속버튼, 준비버튼, 로비로 돌아가기 
      readyButton.setEnabled(false); //버튼, 게임종료 버튼 설정 
      exitButton.setEnabled(true);
      exitButtonB.setEnabled(true);
   }

   // 기본 컴퍼넌트 생성과 크기와 배치, 백그라운드 색 결정, 실제 게임 GUI 패널을 제외하고 
   // 모두 절대좌표로 구성 하는 메소드 
   public void init() {                   
      setLayout(null);
      setSize(950,700);           // 기본 크기
                  
      /*boardUserinfo = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
      boardUserinfo.setBackground(new Color(238,235,235));
      boardUserinfo.setForeground(Color.BLACK);
      boardUserinfo.setBounds(60, 502, 590, 138);*/
      
      UserList = new List();
      UserList.setBackground(new Color(238,235,235));
      UserList.setForeground(Color.BLACK);
      UserList.setBounds(720, 40, 280, 300);

      boardChatArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY); //채팅창
      boardChatArea.setBackground(new Color(238,235,235));
      boardChatArea.setForeground(Color.BLACK);
      boardChatArea.setBounds(60, 100, 590, 488);

      bxsecretTalk = new Choice(); //모두에게
      bxsecretTalk.setBounds(60,598,86,23);
      bxsecretTalk.setBackground(new Color(238,235,235));

      boardChatField = new TextField(20); //글쓰는 창
      boardChatField.setBackground(Color.WHITE);
      boardChatField.setForeground(Color.BLACK);
      boardChatField.setBounds(156, 598, 494, 23);
    
      readyButton = new Button("READY");
      readyButton.setBounds(720, 570, 80, 30);
      readyButton.setBackground(new Color(152,251,152));
         
      joinButton = new Button("참가하기");
      joinButton.setBounds(810, 570, 80, 30);
      joinButton.setBackground(new Color(152,251,152));

      exitButton = new Button("나가기");
      exitButton.setBounds(720, 610, 80, 30);
      exitButton.setBackground(new Color(152,251,152));
         
      exitButtonB = new Button("종료하기");
      exitButtonB.setBounds(810, 610, 80, 30);
      exitButtonB.setBackground(new Color(152,251,152));
       
      for(int i=0; i<8; i++)
      {
         imagepanel[i] = new Panel();
         UserImage[i] = new JLabel();
         UserIdText[i] = new Label();
         
         UserImage[i].setIcon(imagewho);
         UserImage[i].setName("image"+i);
         UserImage[i].setEnabled(true);
         UserIdText[i].setEnabled(true);
      }
      /*
      for(int i=0; i<8; i++)
      {
         UserImage[i] = new JLabel();
         UserIdText[i] = new Label();
      }
      Panel image = new Panel();
      
      UserImage[0].setIcon(imagewho);
      
      add(image);
      image.setBounds(680,60, 110, 110 );
      image.add(UserImage[0]);
      */
      
      imagepanel[0].setBounds(680,60, 110, 110 );
      imagepanel[1].setBounds(800,60, 110, 110 );
      imagepanel[2].setBounds(680,180, 110, 110 );
      imagepanel[3].setBounds(800,180, 110, 110 );
      imagepanel[4].setBounds(680,300, 110, 110 );
      imagepanel[5].setBounds(800,300, 110, 110 );
      imagepanel[6].setBounds(680,420, 110, 110 );
      imagepanel[7].setBounds(800,420, 110, 110 );
      
      
//      UserImage[0].setBounds(680,60, 110, 110 );
//      UserImage[1].setBounds(800,60, 110, 110 );
//      UserImage[2].setBounds(680,180, 110, 110 );
//      UserImage[3].setBounds(800,180, 110, 110 );
//      UserImage[4].setBounds(680,300, 110, 110 );
//      UserImage[5].setBounds(800,300, 110, 110 );
//      UserImage[6].setBounds(680,420, 110, 110 );
//      UserImage[7].setBounds(800,420, 110, 110 );
      UserIdText[0].setBounds(680,170,110,23);
      UserIdText[1].setBounds(800,170,110,23);
      UserIdText[2].setBounds(680,290,110,23);
      UserIdText[3].setBounds(800,290,110,23);
      UserIdText[4].setBounds(680,410,110,23);
      UserIdText[5].setBounds(800,410,110,23);
      UserIdText[6].setBounds(680,530,110,23);
      UserIdText[7].setBounds(800,530,110,23);
      
      for(int i=0; i<8; i++)
      {
         add(imagepanel[i]);
         imagepanel[i].add(UserImage[i]);
         add(UserIdText[i]);
      }
      /*User1 = new Label();
      User1.setBounds(680,60, 110, 110 );
      userId.setBound
      UserId1 = new Label();
      UserId1.setBounds(680,170,110,23);
      UserId2 = new Label();
      UserId2.setBounds(800,170,110,23);*/
      // 게임이 실제 돌아가는 GUI 패널을 컴퍼넌트 
      // 넣듯이 절대좌표를 설정하여 이 패널에 ADD
      mafiaday = new mafiaday(this);
//      mafia.setBounds(20,30,680,450);
      //add(race);    //검정색 배경 
         
      //add(boardUserinfo);
      //add(UserList);
      add(boardChatArea);
      add(boardChatField);
      add(bxsecretTalk);

      add(joinButton);
      add(exitButton);
      add(exitButtonB);
      add(readyButton);
//      for(int i=0; i<8; i++){
//         UserImage[i].setIcon(imagewho);
//         add(UserImage[i]);
//         add(UserIdText[i]);
//      }
      
      FocusListener wa = new FocusAdapter() {   //포커스는 Chat 메세지 입력
         public void focusGained(FocusEvent e) {//하는 TextField로 설정 
            boardChatField.requestFocus();
         }
      };
         
      UserList.addFocusListener(wa);
      boardChatArea.addFocusListener(wa);
      
       // 귓말 선택하는 콤보박스(초이스)에 이벤트 발생시 해당 부분을 스트링으로 변환  
      bxsecretTalk.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
               selectUser = (String)bxsecretTalk.getSelectedItem();
            }
         }
      });
      }
   
   // 화면 배경화면 그리는 부분 
   public void paint(Graphics g) {
      g.drawImage(backImage, 0, 0, this);
   }
      
   //자체적으로 처리할 이벤트를 제외한
   //서버와 메세지 전송이 필요한 이벤트가 발생했을 때 처리하는 종합적인 이벤트 루틴  
   public synchronized boolean action(Event event, Object arg) {
      String message = null;
      if(event.target == exitButton) {
         message = "330|"+parentPanel.id;   // 로비로 이동을 알림
         sendToServer(message);             // 하단  메소드를 확인바람 
         
         parentPanel.lobbyPanel.makeButton.setEnabled(true);
         parentPanel.lobbyPanel.joinButton.setEnabled(false);
         parentPanel.lobbyPanel.exitButton.setEnabled(true);
         
         parentPanel.setVisible(false);     // 로비패널로 돌아가게 함 (직전의 패널은 로비패널임)
         parentPanel.MafiaCardLayout.previous(parentPanel.cardPanel);
         parentPanel.setVisible(true);      // 클라이언트가 현재 로비에 있음을 설정 (Mafia_Client에서 유용하게 이를 사용) 
         parentPanel.currentRoomIsLobby = true;               
         parentPanel.lobbyPanel.chatArea.setText("대기실에 입장 하셨습니다.\n");
               
         message = "130|"+parentPanel.id;   // 방리스트 요청
         sendToServer(message);
         message = "120|"+parentPanel.id;   // 대기실 사용자 리스트 요청
         sendToServer(message);
         isMaster=false;                    // 방장자격은 자동 박탈됨 
         for(int i=0; i<8; i++){
            batting[i]=0;
         }
//         race.select_Mafia=-1;
      } else if(event.target == exitButtonB) {
         message = "331|"+parentPanel.id;    // 게임 종료를 알림
         sendToServer(message);
         System.exit(1);                     // 클라이언트 시스템 종료 
      } else if(event.target == joinButton) {
         message = "320|"+parentPanel.id;    // 게임접속을 알림
         sendToServer(message);
      } else if(event.target == readyButton) {
         /*int sum=0;                           
         for(int i=0;i<8;i++){
            sum+=batting[i];
         }
         if(sum<=0){                         // 배팅을 하지 않으면  게임 진행이 안됨
            boardUserinfo.append("게임을 진행 하시려면 배팅을 해야 합니다.\n");
         } else {
         */
            message = "350|"+parentPanel.id;// 게임준비를 알림
            sendToServer(message);
         /*   message = "380|"+parentPanel.id+"|"+batting[0]+"|"+batting[1]+"|"+batting[2]+"|"+batting[3]+"|"+batting[4]+"|"+batting[5]+"|"+batting[6]+"|"+batting[7];
            sendToServer(message);
            
            boardUserinfo.append(parentPanel.id+"님이 배팅하신 내역입니다.\n");
            for(int i=0;i<8;i++){           // 현재 배팅상황을 TextArea에  모두 append
               boardUserinfo.append((i+1)+"번 말에 "+batting[i]+"를  배팅하셨습니다.\n");
            }
         }*/
      }  else if(event.target == boardChatField) {
         message = boardChatField.getText();
         if(selectUser.equals("모두에게")) {  // 브로드캐스트 채팅 메세지를 전송
            if(message != null && !"".equals(message)){
               message = "300|"+parentPanel.id+"|"+message;
               sendToServer(message);
               boardChatField.setText("");// 그리고 난후 깨끗하게 지움
            }
         } else {
            if(message != null && !"".equals(message)){
               message = "301|"+parentPanel.id+"|"+selectUser+"|"+message;
               sendToServer(message);     // 유니캐스트  채팅메세지 전송
               boardChatField.setText("");
            }
         }
      }
      return true;
   }

   // 배팅 결과를 마찬가지로 TextArea에 append 처리하는 부분   
   public void processBatting(){
      boardUserinfo.append("\n게임이 종료하였습니다.\n\n");
     
     
   }
      
      
   // 서버로부터 온 사용자 목록을  GUI내 UserList와 귓말 선택가능한 콤보박스(Choice)에 추가
   public void showUserList(String UserIds) {
      bxsecretTalk.removeAll();
      bxsecretTalk.addItem("모두에게");
      if(UserList == null) {
         UserList = new List(12, false);
      } else if(UserList.getItemCount() > 0) {
         UserList.removeAll();
         for(int i=0; i<8; i++)
        	 UserIdText[i].setText("");
      }
      StringTokenizer sst = new StringTokenizer(UserIds, ",");
      int count =0;
      while(sst.hasMoreTokens()) {
         try {
            String user = sst.nextToken();
            if(user != null && !"".equals(user)) {
               UserList.add(user);         // 리스트에 추가하는 부분 
               
               UserIdText[count].setText(user); // 유저 아이디 라이블에 추가하는 부분
               UserIdText[count].setAlignment(Label.CENTER);//유저아이디 가운데 정렬
               
               bxsecretTalk.addItem(user); // Choice에 추가하는 부분 
               count++;
            }
         } catch(Exception ee) {
         }
      }
   }
   
   // 모든 서버와의 통신은 Mafia_Client에서 처리, 따라서 이와같이 설정 
   public void sendToServer(String message) {
      if(message != null) {      
         parentPanel.sendToServer(message);
      } else {
         System.out.println("ERROR: NULL WRITING");
      }
   }
      
   // Game_Client에 메세지이면 이를 이곳에서 처리, 어떠한 패널의 메세지인지 확인하는 
   // 부분은 Mafia_Client에 있음 
   public String receiveMessage() throws SocketException {
      try {                   
         return(parentPanel.receiveMessage());
      } catch(SocketException e) {
         throw(e);
      }
   }
   
   // 게임을 시작하는 메소드 
   public void startGame(String[] num) {
	   
      mafiaday.startRacing();
   }
      
   // 게임을 종료하는 메소드 
   public void finishGame() {
      for(int i=0;i<8;i++){
         batting[i]=0;
      }
      mafiaday.stopRacing();
//      mafia.select_Mafia=-1;
   }

   
   // 서버로부터 온 메시지를 처리하는 부분, 이의 실행은 currentRoomIsLobby 플래그를 이용
   // Game_Client에 대한 부분이면 이곳에서 컴퍼넌트에 해당 내용 처리, 프로토콜을 바탕으로  
   // 메세지를 구별하여 메세지 처리 
   public void processMessage(String receive) {
      StringTokenizer st = new StringTokenizer(receive, "|");
      int protocol = Integer.parseInt(st.nextToken());
      String message=null;
      switch(protocol){
         
      case 400: {
         // 브로드캐스트 메시지 처리
         // 400|fromId|chatMessage
         String fromId = st.nextToken();
         String chat = st.nextToken();
         boardChatArea.append("[" + fromId + "] : " + chat + "\n");
      } break;
                       
      case 401: {
         // 유니캐스트 메시지 처리
         // 401|fromId|chatMessage
         String fromId = st.nextToken();         
         String chat = st.nextToken();
         boardChatArea.append("[" + fromId + "]님으로부터의 귓속말  : " + chat + "\n");
      } break;
         
      case 410: {         
         // 클라이언트 입장             
         // 410|who             
         String who = st.nextToken();             
         boardChatArea.append(who+"님이 입장 하셨습니다.\n");                   
      } break;
         
      case 420: {
         // 클라이언트 게임 참여          
         // 420|who|STATS(SUCCESS or MAX_OVER or FAIL)     
         String who = st.nextToken();             
         String result = st.nextToken();       
             
         if(who.equals(parentPanel.id)){                   
            if(result.equals("SUCCESS")){
               joinButton.setLabel("구경하기");
               exitButton.setEnabled(false);
               exitButtonB.setEnabled(false);
               onJoin = true;
               readyButton.setLabel("게임레디");
               boardChatArea.append("게임에 참가했습니다.\n");
               readyButton.setEnabled(true);
         //      race.Mafia_display();
             
            } else if(result.equals("MAX_OVER")){
               //메시지 창을 통해 최대 게임 이용자 수가 초과하였음을 알린다.
               boardChatArea.append("참가자수의 초과로 게임에 참가할 수 없습니다.\n");
            } else { //FAIL
               boardChatArea.append("구경하기 상태로 이동합니다.\n");
               onJoin = false;
               exitButton.setEnabled(true);
               exitButtonB.setEnabled(true);
               if(isMaster) {
                  isMaster=false;            
               }
               joinButton.setLabel("게임참가");
               readyButton.setEnabled(false);
               readyButton.setLabel("게임레디");
         //      race.origin_display();
               for(int i=0;i<8;i++){
                  batting[i]=0;
               }
         //      race.select_Mafia=-1;
            }
         } else {
            if(result.equals("SUCCESS")){
               boardChatArea.append("["+who+"]님이 게임에 참가하셨습니다.\n");
            }
         }
      } break;
         
      case 430: {
         // 클라이언트 퇴장( 로비로 이동 )
         // 430|who
         String who = st.nextToken();
         boardChatArea.append(who+"님이 퇴장하셨습니다.\n");
         String msg = "340|"+parentPanel.id;
         sendToServer(msg);
         
      } break;
         
      case 431: {              
         // 클라이언트 종료              
         // 431|who          
         String who = st.nextToken();        
         boardChatArea.append(who+"님이 퇴장하셨습니다.\n");
         String msg = "340|"+parentPanel.id;
         sendToServer(msg);
      } break;
         
      case 440: {
         // 클라이언트 리스트 표시             
         // 440|id1,id2,id3...          
         String UserIds = st.nextToken();          
         showUserList(UserIds);
      } break;
         
      case 451: {               
         // 게임 시작              
         // 451|START|x|x|x|x|x|x|x|x
         // 0 0 1 1 2 0 0
         String startmessage = st.nextToken();
         boardChatArea.append("게임이 시작되었습니다.\n"); 
         for(int i=0; i<8; i++)
	      {
	         UserImage[i].setEnabled(true);
	         UserIdText[i].setEnabled(true);
	         UserImage[i].setIcon(imagewho);
	      }
         for(int i=0; i<8; i++)
         {
            num[i] = st.nextToken();
         }
         
//         System.out.println(parentPanel.id);
         
         for(int i=0; i<UserList.getItemCount(); i++)
         {
            String same = UserList.getItem(i);
//            System.out.println(parentPanel.id + "= "+ same);
            
            
            if(same.equals(parentPanel.id))
            {
               if(num[i].equals("0"))
               {boardChatArea.append("당신은 시민입니다.\n");
              
                  UserImage[i].setIcon(imagecitizen);
               }
               else if(num[i].equals("1"))
               {boardChatArea.append("당신은 마피아입니다.\n");
               
                  UserImage[i].setIcon(imagemafia);
                 
               }
               else if(num[i].equals("2"))
               {boardChatArea.append("당신은 경찰입니다.\n");
                  UserImage[i].setIcon(imagepolice);
               }
               else if(num[i].equals("3"))
               {boardChatArea.append("당신은 의사입니다.\n");
                  UserImage[i].setIcon(imagedoctor);
               }
            }
         }
         
                     
         onReady = false;          
         exitButton.setEnabled(false);          
         exitButtonB.setEnabled(false);
         readyButton.setEnabled(false);
         joinButton.setEnabled(false);
         startGame(num);      // GUI 패널의 게임 시작
         String Userjobs = st.nextToken();
        
          
          
      } break;
         
      case 452: {           
         // 게임 참여버튼 활성화 메시지             
         // 452|PARTICIPANT|ENABLE(DISABLE)             
         String participant = st.nextToken();          
         participant = st.nextToken();          
         if(participant.equals("ENABLE")) {          
            joinButton.setEnabled(true);
            boardChatArea.append("게임 구경 상태입니다.\n");
         } else {          
            joinButton.setEnabled(false);
            boardChatArea.append("게임 조인 상태입니다.\n");
         }         
      } break;
         
      case 453: {              
         // 게임 레디버튼 활성화 메시지          
         // 453|READY|ENABLE(DISABLE)   
         String ready;
         String participant;
         ready = st.nextToken();         
         participant = st.nextToken();          
         if(participant.equals("ENABLE")) {         
            readyButton.setLabel("게임레디");
            boardChatArea.append("게임 조인 상태입니다.\n");
         } else {      
            readyButton.setLabel("좀더준비");
            boardChatArea.append("게임 레디 상태입니다.\n");
         }            
      } break;             
            
      case 454: {               
         // 방장이 되었음을 알리는 메시지              
         // 454|MASTER             
         String msg = st.nextToken();             
         if("MASTER".equals(msg)){                  
            boardChatArea.append("방장이 되셨습니다.\n");                  
            isMaster=true;                  
            readyButton.setLabel("START");             
         }         
      } break;             
      case 457:{
         //457|NODEATH or 457|0~7 String값 받음
         String msg = st.nextToken();
         System.out.println(msg);
         if("NODEATH".equals(msg))
         {
            boardChatArea.append("아무도 죽지 않았습니다.\n");
         }
         else if("0".equals(msg))
         {
            UserImage[0].setIcon(null);
            UserImage[0].setEnabled(false);
            UserIdText[0].setEnabled(false);
            num[0]="";
            boardChatArea.append(UserList.getItem(0)+" 님이 죽었습니다.\n");
         }
         else if("1".equals(msg))
         {
            UserImage[1].setIcon(null);
            UserImage[1].setEnabled(false);
            UserIdText[1].setEnabled(false);
            num[1]="";
            boardChatArea.append(UserList.getItem(1)+" 님이 죽었습니다.\n");
         }
         else if("2".equals(msg))
         {
            UserImage[2].setIcon(null);
            UserImage[2].setEnabled(false);
            UserIdText[2].setEnabled(false);
            num[2]="";
            boardChatArea.append(UserList.getItem(2)+" 님이 죽었습니다.\n");
         }
         else if("3".equals(msg))
         {
            UserImage[3].setIcon(null);
            UserImage[3].setEnabled(false);
            UserIdText[3].setEnabled(false);
            num[3]="";
            boardChatArea.append(UserList.getItem(3)+" 님이 죽었습니다.\n");
         }
         else if("4".equals(msg))
         {
            UserImage[4].setIcon(null);
            UserImage[4].setEnabled(false);
            UserIdText[4].setEnabled(false);
            num[4]="";
            boardChatArea.append(UserList.getItem(4)+" 님이 죽었습니다.\n");
         }
         else if("5".equals(msg))
         {
            UserImage[5].setIcon(null);
            UserImage[5].setEnabled(false);
            UserIdText[5].setEnabled(false);
            num[5]="";
            boardChatArea.append(UserList.getItem(5)+" 님이 죽었습니다.\n");
         }
         else if("6".equals(msg))
         {
            UserImage[6].setIcon(null);
            UserImage[6].setEnabled(false);
            UserIdText[6].setEnabled(false);
            num[6]="";
            boardChatArea.append(UserList.getItem(6)+" 님이 죽었습니다.\n");
         }
         else if("7".equals(msg))
         {
            UserImage[7].setIcon(null);
            UserImage[7].setEnabled(false);
            UserIdText[7].setEnabled(false);
            num[7]="";
            boardChatArea.append(UserList.getItem(7)+" 님이 죽었습니다.\n");
         }
         System.out.println("vote됨");
         
         //여기서 시민 및 마피아 수 계산해서 승리 조건 만들면됌
         
         // 인원수 8명일때 의 승리 조건
         countcitizen=0; countmafia=0; countdoctor=0; countpolice=0;
         for(int i=0; i<8; i++)
         {
            if(num[i].equals("0"))
               countcitizen++;
            else if(num[i].equals("1"))
               countmafia++;
            else if(num[i].equals("2"))
               countpolice++;
            else if(num[i].equals("3"))
               countdoctor++;
         }         
         countvictory = countcitizen+countpolice+countdoctor;
        
         if(countmafia == countvictory)
         {
            System.out.println("mafia Victory");
            boardChatArea.append("mafia Victory\n");
            finishGame();
            joinButton.setEnabled(true);
            exitButton.setEnabled(true);
            exitButtonB.setEnabled(true);
            readyButton.setEnabled(true);
         }
         else if(countmafia == 0)
         {
            System.out.println("citizen Victory");
            boardChatArea.append("citizen Victorn\n ");
            finishGame();
            joinButton.setEnabled(true);
            exitButton.setEnabled(true);
            exitButtonB.setEnabled(true);
            readyButton.setEnabled(true);
         }
        // boardChatArea.append(boradchat);
       
        
      
         
      }break;
      case 458:{
          String fromid = st.nextToken();
          String fromNum = st.nextToken();
          
          
          if(fromid.equals("mafia") &&(countmafia==2 || countmafia==1))
          { 
             if("0".equals(fromNum))
             {
                UserImage[0].setIcon(imagemafiadeath);
                
                UserIdText[0].setEnabled(false);
                mafiatemp = num[0];
                mafiatempint = 0;
                num[0]="";
                boardChatArea.append(UserList.getItem(0)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("1".equals(fromNum))
             {
                UserImage[1].setIcon(imagemafiadeath);
                UserIdText[1].setEnabled(false);
                mafiatemp = num[1];
                mafiatempint = 1;
                num[1]="";
                boardChatArea.append(UserList.getItem(1)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("2".equals(fromNum))
             {
                UserImage[2].setIcon(imagemafiadeath);
                UserIdText[2].setEnabled(false);
                mafiatemp = num[2];
                mafiatempint = 2;
                num[2]="";
                boardChatArea.append(UserList.getItem(2)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("3".equals(fromNum))
             {
                UserImage[3].setIcon(imagemafiadeath);
                UserIdText[3].setEnabled(false);
                mafiatemp = num[3];
                mafiatempint = 3;
                num[3]="";
                boardChatArea.append(UserList.getItem(3)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("4".equals(fromNum))
             {
                UserImage[4].setIcon(imagemafiadeath);
                UserIdText[4].setEnabled(false);
                mafiatemp = num[4];
                mafiatempint = 4;
                num[4]="";
                boardChatArea.append(UserList.getItem(4)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("5".equals(fromNum))
             {
                UserImage[5].setIcon(imagemafiadeath);
                UserIdText[5].setEnabled(false);
                mafiatemp = num[5];
                
                mafiatempint = 5;
                num[5]="";
                boardChatArea.append(UserList.getItem(5)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("6".equals(fromNum))
             {
                UserImage[6].setIcon(imagemafiadeath);
                UserIdText[6].setEnabled(false);
                mafiatemp = num[6];
                mafiatempint = 6;
                num[6]="";
                boardChatArea.append(UserList.getItem(6)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             else if("7".equals(fromNum))
             {
                UserImage[7].setIcon(imagemafiadeath);
                UserIdText[7].setEnabled(false);
                mafiatemp = num[7];
                mafiatempint = 7;
                num[7]="";
                boardChatArea.append(UserList.getItem(7)+" 님이 죽었습니다.\n");
                boardChatArea.setEnabled(false);
             }
             System.out.println("마피아됨");
             mafiaday.nightmissionpolice();
             break;
          }
          
          
          
          else if(fromid.equals("police") && (countpolice==1))
          {
        	  System.out.println("값이 오긴 오니?");
             for(int i=0; i<8; i++)
             {
                if(parentPanel.id.equals(UserIdText[i].getText()) &&
                      num[i].equals("2"))
                {
                   if("0".equals(fromNum))
                   {
                      if(num[0].equals("0"))
                         UserImage[0].setIcon(imagecitizen);
                      else if(num[0].equals("1"))
                         UserImage[0].setIcon(imagemafia);
                      else if(num[0].equals("3"))
                         UserImage[0].setIcon(imagedoctor);
                   }
                   else if("1".equals(fromNum))
                   {
                      if(num[1].equals("0"))
                         UserImage[1].setIcon(imagecitizen);
                      else if(num[1].equals("1"))
                         UserImage[1].setIcon(imagemafia);
                      else if(num[1].equals("3"))
                         UserImage[1].setIcon(imagedoctor);
                   }
                   else if("2".equals(fromNum))
                   {
                      if(num[2].equals("0"))
                         UserImage[2].setIcon(imagecitizen);
                      else if(num[2].equals("1"))
                         UserImage[2].setIcon(imagemafia);
                      else if(num[2].equals("3"))
                         UserImage[2].setIcon(imagedoctor);
                   }
                   else if("3".equals(fromNum))
                   {
                      if(num[3].equals("0"))
                         UserImage[3].setIcon(imagecitizen);
                      else if(num[3].equals("1"))
                         UserImage[3].setIcon(imagemafia);
                      else if(num[3].equals("3"))
                         UserImage[3].setIcon(imagedoctor);
                   }
                   else if("4".equals(fromNum))
                   {
                      if(num[4].equals("0"))
                         UserImage[4].setIcon(imagecitizen);
                      else if(num[4].equals("1"))
                         UserImage[4].setIcon(imagemafia);
                      else if(num[4].equals("3"))
                         UserImage[4].setIcon(imagedoctor);
                   }
                   else if("5".equals(fromNum))
                   {
                      if(num[5].equals("0"))
                         UserImage[5].setIcon(imagecitizen);
                      else if(num[5].equals("1"))
                         UserImage[5].setIcon(imagemafia);
                      else if(num[5].equals("3"))
                         UserImage[5].setIcon(imagedoctor);
                   }
                   else if("6".equals(fromNum))
                   {
                      if(num[6].equals("0"))
                         UserImage[6].setIcon(imagecitizen);
                      else if(num[6].equals("1"))
                         UserImage[6].setIcon(imagemafia);
                      else if(num[6].equals("3"))
                         UserImage[6].setIcon(imagedoctor);
                   }
                   else if("7".equals(fromNum))
                   {
                      if(num[7].equals("0"))
                         UserImage[7].setIcon(imagecitizen);
                      else if(num[7].equals("1"))
                         UserImage[7].setIcon(imagemafia);
                      else if(num[7].equals("3"))
                         UserImage[7].setIcon(imagedoctor);
                   }
                }
             }
             System.out.println("경찰됨");
             mafiaday.nightmissiondoctor();
             break;
          }
          
          else if(fromid.equals("doctor") && countdoctor==1 )
          {
        	
             if(mafiatempint == Integer.parseInt(fromNum))
             {
                for(int i=0; i<8; i++)
                {
                   if(parentPanel.id.equals(UserIdText[i].getText()))
                   {
                      UserImage[Integer.parseInt(fromNum)].setIcon(imagewho);
                         
                      UserImage[Integer.parseInt(fromNum)].setEnabled(true);
                      UserIdText[Integer.parseInt(fromNum)].setEnabled(true);
                      boardChatArea.append(UserList.getItem(Integer.parseInt(fromNum))+" 님이 살았습니다.\n");
                      boardChatArea.setEnabled(true);
                   }
                   if(parentPanel.id.equals(UserIdText[i].getText()) &&
                         (i == Integer.parseInt(fromNum)))
                   {
                      if(mafiatemp.equals("0"))
                      {
                         UserImage[i].setIcon(imagecitizen);
                         num[i] = "0";
                      }
                      else if(mafiatemp.equals("1"))
                      {
                         UserImage[i].setIcon(imagemafia);
                         num[i] = "1";
                      }
                      else if(mafiatemp.equals("2"))
                      {
                         UserImage[i].setIcon(imagepolice);
                         num[i] = "2";
                      }
                   }
                }
                System.out.println("의사됨");
                
             }
             else
             {
            	 UserImage[mafiatempint].setIcon(null);
            	 String msg = "359|num-";
            	 sendToServer(msg);
             }
             //해당 값에 대해 count****** --;
//             if(mafiatemp.equals("0"))
//                countcitizen--;
//             else if(mafiatemp.equals("1"))
//                countmafia--;
//             else if(mafiatemp.equals("2"))
//                countpolice = 0;
          }
          mafiatemp = null; 
          mafiatempint =-1;
          // 다시 승리조건
          countcitizen=0; countmafia=0; countdoctor=0; countpolice=0;
          for(int i=0; i<8; i++)
          {
             if(num[i].equals("0"))
                countcitizen++;
             else if(num[i].equals("1"))
                countmafia++;
             else if(num[i].equals("2"))
                countpolice++;
             else if(num[i].equals("3"))
                countdoctor++;
          }         
          countvictory = countcitizen+countpolice+countdoctor;
         
          if(countmafia == countvictory)
          {
             System.out.println("mafia Victory");
             boardChatArea.append("mafia Victory\n");
             finishGame();
             joinButton.setEnabled(true);
             exitButton.setEnabled(true);
             exitButtonB.setEnabled(true);
             readyButton.setEnabled(true);
          }
          else if(countmafia == 0)
          {
             System.out.println("citizen Victory");
             boardChatArea.append("citizen Victorn\n ");
             finishGame();
             joinButton.setEnabled(true);
             exitButton.setEnabled(true);
             exitButtonB.setEnabled(true);
             readyButton.setEnabled(true);
          }
          
          
          mafiaday.chattime();
       }break;
      case 460: {             
         // 게임종료              
         // 460|END       
         finishGame();
         boardChatArea.append("게임이 끝났습니다.\n");         
         exitButton.setEnabled(true);         
         exitButtonB.setEnabled(true);
         readyButton.setEnabled(false);
         readyButton.setLabel("게임레디");
         joinButton.setEnabled(true);
         joinButton.setLabel("게임참가");
         
      } break;
      case 470: {
         // 사용자 정보 요청 처리
         // 470|id|win_count|lose_count|total_count|get_money|lose_money|total_money
        
      }
      case 490: {   
     
      }break;
      }
   }        
}
