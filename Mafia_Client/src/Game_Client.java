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

//���� ���� ȭ�� ������ �̷�� �κ��̴�.
//ũ�� �� �κ����� �̷������ ��, �⺻ �������̽� �κа� ����ȭ�� �κ��̴�.
//���� ȭ�� �κ��� �ϳ��� �гη� ����� ��ü ȭ�鿡 �ٿ��ִ� �������� �ߴ�.
//�׷��� �� ������ ���� ���ȭ�� �̷�� ���ؼ�����.
//�⺻ �������̽� �κ���  ä�ð� �������� ����� ���� �����̴�.

public class Game_Client extends Panel {
   // �⺻ ȭ�鱸���� �ʿ��� ���۳�Ʈ��� , Mafia_Client ������ Ŭ������ ��ü��
   // Racing (���� GUI) ��ü�� �����.
   
   mafiaday mafiaday;
   int batting[];            // ���ñݾ� ȯ�꿡 �ʿ��� int �迭
   Mafia_Client parentPanel=null;
   long old_money;             // ���ñݾ׿� ������ long�� ����
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
   JLabel UserImage[]=new JLabel[8];//= new Label[8];//����ڷ��̺�
   Label UserIdText[]=new Label[8];// = new Label[8]; //���̵� �ؽ�Ʈ �޴� �κ�
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
   // ���۳�Ʈ ��� �ʿ��� �÷��׵�, ����������, �غ�������, �������� ������.
   boolean onJoin=false;        
   boolean onReady=false;
   boolean isMaster=false;
      
   String selectUser="��ο���";
   
   // �����ڿ����� Mafia_Client Ŭ���� ��ü�� �⺻ ���۳�Ʈ�� ������ init()�޼��� ����
   // ���ñݾ� ȯ�꿡 �ʿ��� �迭 �ʱ�ȭ�� ȭ�� ������ �̹����� ����.
   public Game_Client(Mafia_Client parentPanel) {
      this.parentPanel = parentPanel;
      init();
      batting = new int[8];
      for(int i=0;i<8;i++){
         batting[i]=0;
      }   
      backImage = Toolkit.getDefaultToolkit().createImage("85game_panel.jpg");
   }
      
   // Ŭ���̾�Ʈ�� �� ��(���� ���� �г�, Game_Client)�� ���Ȱ� ���ÿ� 
   // �ʿ��� ���۳�Ʈ ���� ����. 
   public void initComponent() {        
      boardChatArea.setText("");     
      UserList.removeAll();          // UserList�� Chat�� ������ 
      boardChatField.setText("");    // Area, Field���� ��� ����Ʈ�� 
                                       // �ƹ��͵� ���� ����
      joinButton.setEnabled(false);  //���ӹ�ư, �غ��ư, �κ�� ���ư��� 
      readyButton.setEnabled(false); //��ư, �������� ��ư ���� 
      exitButton.setEnabled(true);
      exitButtonB.setEnabled(true);
   }

   // �⺻ ���۳�Ʈ ������ ũ��� ��ġ, ��׶��� �� ����, ���� ���� GUI �г��� �����ϰ� 
   // ��� ������ǥ�� ���� �ϴ� �޼ҵ� 
   public void init() {                   
      setLayout(null);
      setSize(950,700);           // �⺻ ũ��
                  
      /*boardUserinfo = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
      boardUserinfo.setBackground(new Color(238,235,235));
      boardUserinfo.setForeground(Color.BLACK);
      boardUserinfo.setBounds(60, 502, 590, 138);*/
      
      UserList = new List();
      UserList.setBackground(new Color(238,235,235));
      UserList.setForeground(Color.BLACK);
      UserList.setBounds(720, 40, 280, 300);

      boardChatArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY); //ä��â
      boardChatArea.setBackground(new Color(238,235,235));
      boardChatArea.setForeground(Color.BLACK);
      boardChatArea.setBounds(60, 100, 590, 488);

      bxsecretTalk = new Choice(); //��ο���
      bxsecretTalk.setBounds(60,598,86,23);
      bxsecretTalk.setBackground(new Color(238,235,235));

      boardChatField = new TextField(20); //�۾��� â
      boardChatField.setBackground(Color.WHITE);
      boardChatField.setForeground(Color.BLACK);
      boardChatField.setBounds(156, 598, 494, 23);
    
      readyButton = new Button("READY");
      readyButton.setBounds(720, 570, 80, 30);
      readyButton.setBackground(new Color(152,251,152));
         
      joinButton = new Button("�����ϱ�");
      joinButton.setBounds(810, 570, 80, 30);
      joinButton.setBackground(new Color(152,251,152));

      exitButton = new Button("������");
      exitButton.setBounds(720, 610, 80, 30);
      exitButton.setBackground(new Color(152,251,152));
         
      exitButtonB = new Button("�����ϱ�");
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
      // ������ ���� ���ư��� GUI �г��� ���۳�Ʈ 
      // �ֵ��� ������ǥ�� �����Ͽ� �� �гο� ADD
      mafiaday = new mafiaday(this);
//      mafia.setBounds(20,30,680,450);
      //add(race);    //������ ��� 
         
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
      
      FocusListener wa = new FocusAdapter() {   //��Ŀ���� Chat �޼��� �Է�
         public void focusGained(FocusEvent e) {//�ϴ� TextField�� ���� 
            boardChatField.requestFocus();
         }
      };
         
      UserList.addFocusListener(wa);
      boardChatArea.addFocusListener(wa);
      
       // �Ӹ� �����ϴ� �޺��ڽ�(���̽�)�� �̺�Ʈ �߻��� �ش� �κ��� ��Ʈ������ ��ȯ  
      bxsecretTalk.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
               selectUser = (String)bxsecretTalk.getSelectedItem();
            }
         }
      });
      }
   
   // ȭ�� ���ȭ�� �׸��� �κ� 
   public void paint(Graphics g) {
      g.drawImage(backImage, 0, 0, this);
   }
      
   //��ü������ ó���� �̺�Ʈ�� ������
   //������ �޼��� ������ �ʿ��� �̺�Ʈ�� �߻����� �� ó���ϴ� �������� �̺�Ʈ ��ƾ  
   public synchronized boolean action(Event event, Object arg) {
      String message = null;
      if(event.target == exitButton) {
         message = "330|"+parentPanel.id;   // �κ�� �̵��� �˸�
         sendToServer(message);             // �ϴ�  �޼ҵ带 Ȯ�ιٶ� 
         
         parentPanel.lobbyPanel.makeButton.setEnabled(true);
         parentPanel.lobbyPanel.joinButton.setEnabled(false);
         parentPanel.lobbyPanel.exitButton.setEnabled(true);
         
         parentPanel.setVisible(false);     // �κ��гη� ���ư��� �� (������ �г��� �κ��г���)
         parentPanel.MafiaCardLayout.previous(parentPanel.cardPanel);
         parentPanel.setVisible(true);      // Ŭ���̾�Ʈ�� ���� �κ� ������ ���� (Mafia_Client���� �����ϰ� �̸� ���) 
         parentPanel.currentRoomIsLobby = true;               
         parentPanel.lobbyPanel.chatArea.setText("���ǿ� ���� �ϼ̽��ϴ�.\n");
               
         message = "130|"+parentPanel.id;   // �渮��Ʈ ��û
         sendToServer(message);
         message = "120|"+parentPanel.id;   // ���� ����� ����Ʈ ��û
         sendToServer(message);
         isMaster=false;                    // �����ڰ��� �ڵ� ��Ż�� 
         for(int i=0; i<8; i++){
            batting[i]=0;
         }
//         race.select_Mafia=-1;
      } else if(event.target == exitButtonB) {
         message = "331|"+parentPanel.id;    // ���� ���Ḧ �˸�
         sendToServer(message);
         System.exit(1);                     // Ŭ���̾�Ʈ �ý��� ���� 
      } else if(event.target == joinButton) {
         message = "320|"+parentPanel.id;    // ���������� �˸�
         sendToServer(message);
      } else if(event.target == readyButton) {
         /*int sum=0;                           
         for(int i=0;i<8;i++){
            sum+=batting[i];
         }
         if(sum<=0){                         // ������ ���� ������  ���� ������ �ȵ�
            boardUserinfo.append("������ ���� �Ͻ÷��� ������ �ؾ� �մϴ�.\n");
         } else {
         */
            message = "350|"+parentPanel.id;// �����غ� �˸�
            sendToServer(message);
         /*   message = "380|"+parentPanel.id+"|"+batting[0]+"|"+batting[1]+"|"+batting[2]+"|"+batting[3]+"|"+batting[4]+"|"+batting[5]+"|"+batting[6]+"|"+batting[7];
            sendToServer(message);
            
            boardUserinfo.append(parentPanel.id+"���� �����Ͻ� �����Դϴ�.\n");
            for(int i=0;i<8;i++){           // ���� ���û�Ȳ�� TextArea��  ��� append
               boardUserinfo.append((i+1)+"�� ���� "+batting[i]+"��  �����ϼ̽��ϴ�.\n");
            }
         }*/
      }  else if(event.target == boardChatField) {
         message = boardChatField.getText();
         if(selectUser.equals("��ο���")) {  // ��ε�ĳ��Ʈ ä�� �޼����� ����
            if(message != null && !"".equals(message)){
               message = "300|"+parentPanel.id+"|"+message;
               sendToServer(message);
               boardChatField.setText("");// �׸��� ���� �����ϰ� ����
            }
         } else {
            if(message != null && !"".equals(message)){
               message = "301|"+parentPanel.id+"|"+selectUser+"|"+message;
               sendToServer(message);     // ����ĳ��Ʈ  ä�ø޼��� ����
               boardChatField.setText("");
            }
         }
      }
      return true;
   }

   // ���� ����� ���������� TextArea�� append ó���ϴ� �κ�   
   public void processBatting(){
      boardUserinfo.append("\n������ �����Ͽ����ϴ�.\n\n");
     
     
   }
      
      
   // �����κ��� �� ����� �����  GUI�� UserList�� �Ӹ� ���ð����� �޺��ڽ�(Choice)�� �߰�
   public void showUserList(String UserIds) {
      bxsecretTalk.removeAll();
      bxsecretTalk.addItem("��ο���");
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
               UserList.add(user);         // ����Ʈ�� �߰��ϴ� �κ� 
               
               UserIdText[count].setText(user); // ���� ���̵� ���̺� �߰��ϴ� �κ�
               UserIdText[count].setAlignment(Label.CENTER);//�������̵� ��� ����
               
               bxsecretTalk.addItem(user); // Choice�� �߰��ϴ� �κ� 
               count++;
            }
         } catch(Exception ee) {
         }
      }
   }
   
   // ��� �������� ����� Mafia_Client���� ó��, ���� �̿Ͱ��� ���� 
   public void sendToServer(String message) {
      if(message != null) {      
         parentPanel.sendToServer(message);
      } else {
         System.out.println("ERROR: NULL WRITING");
      }
   }
      
   // Game_Client�� �޼����̸� �̸� �̰����� ó��, ��� �г��� �޼������� Ȯ���ϴ� 
   // �κ��� Mafia_Client�� ���� 
   public String receiveMessage() throws SocketException {
      try {                   
         return(parentPanel.receiveMessage());
      } catch(SocketException e) {
         throw(e);
      }
   }
   
   // ������ �����ϴ� �޼ҵ� 
   public void startGame(String[] num) {
	   
      mafiaday.startRacing();
   }
      
   // ������ �����ϴ� �޼ҵ� 
   public void finishGame() {
      for(int i=0;i<8;i++){
         batting[i]=0;
      }
      mafiaday.stopRacing();
//      mafia.select_Mafia=-1;
   }

   
   // �����κ��� �� �޽����� ó���ϴ� �κ�, ���� ������ currentRoomIsLobby �÷��׸� �̿�
   // Game_Client�� ���� �κ��̸� �̰����� ���۳�Ʈ�� �ش� ���� ó��, ���������� ��������  
   // �޼����� �����Ͽ� �޼��� ó�� 
   public void processMessage(String receive) {
      StringTokenizer st = new StringTokenizer(receive, "|");
      int protocol = Integer.parseInt(st.nextToken());
      String message=null;
      switch(protocol){
         
      case 400: {
         // ��ε�ĳ��Ʈ �޽��� ó��
         // 400|fromId|chatMessage
         String fromId = st.nextToken();
         String chat = st.nextToken();
         boardChatArea.append("[" + fromId + "] : " + chat + "\n");
      } break;
                       
      case 401: {
         // ����ĳ��Ʈ �޽��� ó��
         // 401|fromId|chatMessage
         String fromId = st.nextToken();         
         String chat = st.nextToken();
         boardChatArea.append("[" + fromId + "]�����κ����� �ӼӸ�  : " + chat + "\n");
      } break;
         
      case 410: {         
         // Ŭ���̾�Ʈ ����             
         // 410|who             
         String who = st.nextToken();             
         boardChatArea.append(who+"���� ���� �ϼ̽��ϴ�.\n");                   
      } break;
         
      case 420: {
         // Ŭ���̾�Ʈ ���� ����          
         // 420|who|STATS(SUCCESS or MAX_OVER or FAIL)     
         String who = st.nextToken();             
         String result = st.nextToken();       
             
         if(who.equals(parentPanel.id)){                   
            if(result.equals("SUCCESS")){
               joinButton.setLabel("�����ϱ�");
               exitButton.setEnabled(false);
               exitButtonB.setEnabled(false);
               onJoin = true;
               readyButton.setLabel("���ӷ���");
               boardChatArea.append("���ӿ� �����߽��ϴ�.\n");
               readyButton.setEnabled(true);
         //      race.Mafia_display();
             
            } else if(result.equals("MAX_OVER")){
               //�޽��� â�� ���� �ִ� ���� �̿��� ���� �ʰ��Ͽ����� �˸���.
               boardChatArea.append("�����ڼ��� �ʰ��� ���ӿ� ������ �� �����ϴ�.\n");
            } else { //FAIL
               boardChatArea.append("�����ϱ� ���·� �̵��մϴ�.\n");
               onJoin = false;
               exitButton.setEnabled(true);
               exitButtonB.setEnabled(true);
               if(isMaster) {
                  isMaster=false;            
               }
               joinButton.setLabel("��������");
               readyButton.setEnabled(false);
               readyButton.setLabel("���ӷ���");
         //      race.origin_display();
               for(int i=0;i<8;i++){
                  batting[i]=0;
               }
         //      race.select_Mafia=-1;
            }
         } else {
            if(result.equals("SUCCESS")){
               boardChatArea.append("["+who+"]���� ���ӿ� �����ϼ̽��ϴ�.\n");
            }
         }
      } break;
         
      case 430: {
         // Ŭ���̾�Ʈ ����( �κ�� �̵� )
         // 430|who
         String who = st.nextToken();
         boardChatArea.append(who+"���� �����ϼ̽��ϴ�.\n");
         String msg = "340|"+parentPanel.id;
         sendToServer(msg);
         
      } break;
         
      case 431: {              
         // Ŭ���̾�Ʈ ����              
         // 431|who          
         String who = st.nextToken();        
         boardChatArea.append(who+"���� �����ϼ̽��ϴ�.\n");
         String msg = "340|"+parentPanel.id;
         sendToServer(msg);
      } break;
         
      case 440: {
         // Ŭ���̾�Ʈ ����Ʈ ǥ��             
         // 440|id1,id2,id3...          
         String UserIds = st.nextToken();          
         showUserList(UserIds);
      } break;
         
      case 451: {               
         // ���� ����              
         // 451|START|x|x|x|x|x|x|x|x
         // 0 0 1 1 2 0 0
         String startmessage = st.nextToken();
         boardChatArea.append("������ ���۵Ǿ����ϴ�.\n"); 
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
               {boardChatArea.append("����� �ù��Դϴ�.\n");
              
                  UserImage[i].setIcon(imagecitizen);
               }
               else if(num[i].equals("1"))
               {boardChatArea.append("����� ���Ǿ��Դϴ�.\n");
               
                  UserImage[i].setIcon(imagemafia);
                 
               }
               else if(num[i].equals("2"))
               {boardChatArea.append("����� �����Դϴ�.\n");
                  UserImage[i].setIcon(imagepolice);
               }
               else if(num[i].equals("3"))
               {boardChatArea.append("����� �ǻ��Դϴ�.\n");
                  UserImage[i].setIcon(imagedoctor);
               }
            }
         }
         
                     
         onReady = false;          
         exitButton.setEnabled(false);          
         exitButtonB.setEnabled(false);
         readyButton.setEnabled(false);
         joinButton.setEnabled(false);
         startGame(num);      // GUI �г��� ���� ����
         String Userjobs = st.nextToken();
        
          
          
      } break;
         
      case 452: {           
         // ���� ������ư Ȱ��ȭ �޽���             
         // 452|PARTICIPANT|ENABLE(DISABLE)             
         String participant = st.nextToken();          
         participant = st.nextToken();          
         if(participant.equals("ENABLE")) {          
            joinButton.setEnabled(true);
            boardChatArea.append("���� ���� �����Դϴ�.\n");
         } else {          
            joinButton.setEnabled(false);
            boardChatArea.append("���� ���� �����Դϴ�.\n");
         }         
      } break;
         
      case 453: {              
         // ���� �����ư Ȱ��ȭ �޽���          
         // 453|READY|ENABLE(DISABLE)   
         String ready;
         String participant;
         ready = st.nextToken();         
         participant = st.nextToken();          
         if(participant.equals("ENABLE")) {         
            readyButton.setLabel("���ӷ���");
            boardChatArea.append("���� ���� �����Դϴ�.\n");
         } else {      
            readyButton.setLabel("�����غ�");
            boardChatArea.append("���� ���� �����Դϴ�.\n");
         }            
      } break;             
            
      case 454: {               
         // ������ �Ǿ����� �˸��� �޽���              
         // 454|MASTER             
         String msg = st.nextToken();             
         if("MASTER".equals(msg)){                  
            boardChatArea.append("������ �Ǽ̽��ϴ�.\n");                  
            isMaster=true;                  
            readyButton.setLabel("START");             
         }         
      } break;             
      case 457:{
         //457|NODEATH or 457|0~7 String�� ����
         String msg = st.nextToken();
         System.out.println(msg);
         if("NODEATH".equals(msg))
         {
            boardChatArea.append("�ƹ��� ���� �ʾҽ��ϴ�.\n");
         }
         else if("0".equals(msg))
         {
            UserImage[0].setIcon(null);
            UserImage[0].setEnabled(false);
            UserIdText[0].setEnabled(false);
            num[0]="";
            boardChatArea.append(UserList.getItem(0)+" ���� �׾����ϴ�.\n");
         }
         else if("1".equals(msg))
         {
            UserImage[1].setIcon(null);
            UserImage[1].setEnabled(false);
            UserIdText[1].setEnabled(false);
            num[1]="";
            boardChatArea.append(UserList.getItem(1)+" ���� �׾����ϴ�.\n");
         }
         else if("2".equals(msg))
         {
            UserImage[2].setIcon(null);
            UserImage[2].setEnabled(false);
            UserIdText[2].setEnabled(false);
            num[2]="";
            boardChatArea.append(UserList.getItem(2)+" ���� �׾����ϴ�.\n");
         }
         else if("3".equals(msg))
         {
            UserImage[3].setIcon(null);
            UserImage[3].setEnabled(false);
            UserIdText[3].setEnabled(false);
            num[3]="";
            boardChatArea.append(UserList.getItem(3)+" ���� �׾����ϴ�.\n");
         }
         else if("4".equals(msg))
         {
            UserImage[4].setIcon(null);
            UserImage[4].setEnabled(false);
            UserIdText[4].setEnabled(false);
            num[4]="";
            boardChatArea.append(UserList.getItem(4)+" ���� �׾����ϴ�.\n");
         }
         else if("5".equals(msg))
         {
            UserImage[5].setIcon(null);
            UserImage[5].setEnabled(false);
            UserIdText[5].setEnabled(false);
            num[5]="";
            boardChatArea.append(UserList.getItem(5)+" ���� �׾����ϴ�.\n");
         }
         else if("6".equals(msg))
         {
            UserImage[6].setIcon(null);
            UserImage[6].setEnabled(false);
            UserIdText[6].setEnabled(false);
            num[6]="";
            boardChatArea.append(UserList.getItem(6)+" ���� �׾����ϴ�.\n");
         }
         else if("7".equals(msg))
         {
            UserImage[7].setIcon(null);
            UserImage[7].setEnabled(false);
            UserIdText[7].setEnabled(false);
            num[7]="";
            boardChatArea.append(UserList.getItem(7)+" ���� �׾����ϴ�.\n");
         }
         System.out.println("vote��");
         
         //���⼭ �ù� �� ���Ǿ� �� ����ؼ� �¸� ���� ������
         
         // �ο��� 8���϶� �� �¸� ����
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
                boardChatArea.append(UserList.getItem(0)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("1".equals(fromNum))
             {
                UserImage[1].setIcon(imagemafiadeath);
                UserIdText[1].setEnabled(false);
                mafiatemp = num[1];
                mafiatempint = 1;
                num[1]="";
                boardChatArea.append(UserList.getItem(1)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("2".equals(fromNum))
             {
                UserImage[2].setIcon(imagemafiadeath);
                UserIdText[2].setEnabled(false);
                mafiatemp = num[2];
                mafiatempint = 2;
                num[2]="";
                boardChatArea.append(UserList.getItem(2)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("3".equals(fromNum))
             {
                UserImage[3].setIcon(imagemafiadeath);
                UserIdText[3].setEnabled(false);
                mafiatemp = num[3];
                mafiatempint = 3;
                num[3]="";
                boardChatArea.append(UserList.getItem(3)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("4".equals(fromNum))
             {
                UserImage[4].setIcon(imagemafiadeath);
                UserIdText[4].setEnabled(false);
                mafiatemp = num[4];
                mafiatempint = 4;
                num[4]="";
                boardChatArea.append(UserList.getItem(4)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("5".equals(fromNum))
             {
                UserImage[5].setIcon(imagemafiadeath);
                UserIdText[5].setEnabled(false);
                mafiatemp = num[5];
                
                mafiatempint = 5;
                num[5]="";
                boardChatArea.append(UserList.getItem(5)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("6".equals(fromNum))
             {
                UserImage[6].setIcon(imagemafiadeath);
                UserIdText[6].setEnabled(false);
                mafiatemp = num[6];
                mafiatempint = 6;
                num[6]="";
                boardChatArea.append(UserList.getItem(6)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             else if("7".equals(fromNum))
             {
                UserImage[7].setIcon(imagemafiadeath);
                UserIdText[7].setEnabled(false);
                mafiatemp = num[7];
                mafiatempint = 7;
                num[7]="";
                boardChatArea.append(UserList.getItem(7)+" ���� �׾����ϴ�.\n");
                boardChatArea.setEnabled(false);
             }
             System.out.println("���ǾƵ�");
             mafiaday.nightmissionpolice();
             break;
          }
          
          
          
          else if(fromid.equals("police") && (countpolice==1))
          {
        	  System.out.println("���� ���� ����?");
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
             System.out.println("������");
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
                      boardChatArea.append(UserList.getItem(Integer.parseInt(fromNum))+" ���� ��ҽ��ϴ�.\n");
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
                System.out.println("�ǻ��");
                
             }
             else
             {
            	 UserImage[mafiatempint].setIcon(null);
            	 String msg = "359|num-";
            	 sendToServer(msg);
             }
             //�ش� ���� ���� count****** --;
//             if(mafiatemp.equals("0"))
//                countcitizen--;
//             else if(mafiatemp.equals("1"))
//                countmafia--;
//             else if(mafiatemp.equals("2"))
//                countpolice = 0;
          }
          mafiatemp = null; 
          mafiatempint =-1;
          // �ٽ� �¸�����
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
         // ��������              
         // 460|END       
         finishGame();
         boardChatArea.append("������ �������ϴ�.\n");         
         exitButton.setEnabled(true);         
         exitButtonB.setEnabled(true);
         readyButton.setEnabled(false);
         readyButton.setLabel("���ӷ���");
         joinButton.setEnabled(true);
         joinButton.setLabel("��������");
         
      } break;
      case 470: {
         // ����� ���� ��û ó��
         // 470|id|win_count|lose_count|total_count|get_money|lose_money|total_money
        
      }
      case 490: {   
     
      }break;
      }
   }        
}
