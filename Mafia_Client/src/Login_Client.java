import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login_Client extends Panel {
	// 기본적인 컴퍼넌트 이외에 Mafia_Client 객체를 사용 
	Mafia_Client parentPanel=null;
	Button connectButton = null, exitButton = null, newaccountButton = null;//회원가입 삭제
	Label idLabel=null, passwordLabel=null;
    TextArea newsArea = null;
	TextField idField = null;
	TextField passwordField=null;
	Image backImage; 

	// 생성자에서는 Mafia_Client 객체를 받고, 기본 컴퍼넌트 구성하는 init() 메소드 실행후에
	// 배경화면으로 사용될 이미지 생성 
	public Login_Client(Mafia_Client parentPanel) {
		this.parentPanel = parentPanel;      
		init();
		backImage = Toolkit.getDefaultToolkit().createImage("60login_panel.jpg");  
	}

	// 초기 GUI 컴포넌트 생성 
	public void init() {
		setLayout(null);
		setSize(950,700);

		//실제로는 서버로부터 업데이트 소식을 받아서 처리해야 할 부분
		newsArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
		newsArea.setBackground(new Color(238,235,235));
		newsArea.setForeground(Color.darkGray);
		newsArea.append("\n\n");
		newsArea.append("       ----------네트워크TermProject----------\n\n\n");
		newsArea.append("      11월 14일\n\n");
		newsArea.append("      프로젝트 시작\n\n\n");
		newsArea.append("   \n\n");
		
		newsArea.setFont(new Font("",Font.BOLD,14));
		newsArea.setBounds(275, 200, 400, 300);
		newsArea.setEditable(false);
      
		idLabel = new Label("ID", Label.CENTER);
		idLabel.setBounds(365, 533, 80, 23);
		idLabel.setFont(new Font("Batang",Font.BOLD,12));
		idLabel.setBackground(new Color(207,200,182));
		passwordLabel = new Label("PASSWORD", Label.CENTER);
		passwordLabel.setBounds(365, 567, 80, 23);
		passwordLabel.setFont(new Font("Batang",Font.BOLD,12));
		passwordLabel.setBackground(new Color(207,200,182));
      
		connectButton = new Button("로그인");
		connectButton.setBounds(385, 610, 80, 30);
		connectButton.setBackground(new Color(180,180,170));
		connectButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		/*newaccountButton = new Button("회원가입");
		newaccountButton.setBounds(435, 610, 80, 30);
		newaccountButton.setBackground(new Color(180,180,170));
		newaccountButton.setFont(new Font("",Font.ROMAN_BASELINE,12));*/
		exitButton = new Button("나가기");
		exitButton.setBounds(485, 610, 80, 30);
		exitButton.setBackground(new Color(180,180,170));
		exitButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
      
		idField = new TextField(20);
		idField.setBackground(Color.WHITE);
		idField.setForeground(Color.BLACK);
		idField.setBounds(475, 533, 130, 23);
		passwordField = new TextField(20);
		passwordField.setBackground(Color.WHITE);
		passwordField.setForeground(Color.BLACK);
		passwordField.setBounds(475, 567, 130, 23);
		passwordField.setEchoCharacter('*');
		
		add(newsArea);
		add(idLabel);
		add(passwordLabel);
		add(connectButton);
		//add(newaccountButton);
		add(exitButton);
		add(newsArea);
		add(idField);
		add(passwordField);		
      
		// 포커스는 id 입력하는 TextField에 설정 
		FocusListener wa = new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				idField.requestFocus();
			}
		};      
		newsArea.addFocusListener(wa);
		
		// id 입력하는 곳에 키 이벤트 발생시 Connect() 이를 처리 
		idField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == k.VK_ENTER) {
					connect();
				}
			}
		});
      
		// 패스워드 입력하는 키 이벤트 발생시 Connect() 이를 처리 
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == k.VK_ENTER) {
					connect();
				}
			}
		});
		
		// 버튼과 관련한 이벤트 처리 
		ActionListener w = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == connectButton) {
					connect();
				} else if(e.getSource() == exitButton) {
					System.exit(1);
				}/* else if(e.getSource() == newaccountButton) {           
					AccountDialog a = new AccountDialog(parentPanel, "회원가입양식","good");
					a.show();            // DB의 미구현으로 유용하지 않음 
				}*/
			}
		};
      
		connectButton.addActionListener(w);
		exitButton.addActionListener(w);
		//newaccountButton.addActionListener(w);
	}
	
	// 배경화면 그리는 메소드 
	public void paint(Graphics g) {
		g.drawImage(backImage, 0, 0, this);
	}
  
	// 아이디가 있음이  확인되면 Mafia_Client 내에 connect 함수에게 아이디,패스워드를 넘겨주고
	// 클라이언트 GUI를 card_layout '다음'화면(lobby_Panel)으로 넘겨주고  
	// 현재 클라이언트가 로비에 있음을 플래그를 통해 설정한다.
	public void connect() {
		if(!idField.getText().equals("") && !passwordField.getText().equals("")){
			exitButton.setEnabled(false);
			parentPanel.connect(idField.getText(),passwordField.getText());
			parentPanel.setVisible(false);
			parentPanel.MafiaCardLayout.next(parentPanel.cardPanel);
			parentPanel.setVisible(true);
			parentPanel.currentRoomIsLobby = true;
		} else {
			WarningDialog dlg = new WarningDialog(parentPanel,"Warning : Incorrect user information","잘못 입력했습니다.|정확한 아이디와 패스워드를 입력해 주세요.|다시 한번 해볼까요.","NOTEXIT");
			dlg.show();
		}
	}
}