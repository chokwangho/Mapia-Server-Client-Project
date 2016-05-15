import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login_Client extends Panel {
	// �⺻���� ���۳�Ʈ �̿ܿ� Mafia_Client ��ü�� ��� 
	Mafia_Client parentPanel=null;
	Button connectButton = null, exitButton = null, newaccountButton = null;//ȸ������ ����
	Label idLabel=null, passwordLabel=null;
    TextArea newsArea = null;
	TextField idField = null;
	TextField passwordField=null;
	Image backImage; 

	// �����ڿ����� Mafia_Client ��ü�� �ް�, �⺻ ���۳�Ʈ �����ϴ� init() �޼ҵ� �����Ŀ�
	// ���ȭ������ ���� �̹��� ���� 
	public Login_Client(Mafia_Client parentPanel) {
		this.parentPanel = parentPanel;      
		init();
		backImage = Toolkit.getDefaultToolkit().createImage("60login_panel.jpg");  
	}

	// �ʱ� GUI ������Ʈ ���� 
	public void init() {
		setLayout(null);
		setSize(950,700);

		//�����δ� �����κ��� ������Ʈ �ҽ��� �޾Ƽ� ó���ؾ� �� �κ�
		newsArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
		newsArea.setBackground(new Color(238,235,235));
		newsArea.setForeground(Color.darkGray);
		newsArea.append("\n\n");
		newsArea.append("       ----------��Ʈ��ũTermProject----------\n\n\n");
		newsArea.append("      11�� 14��\n\n");
		newsArea.append("      ������Ʈ ����\n\n\n");
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
      
		connectButton = new Button("�α���");
		connectButton.setBounds(385, 610, 80, 30);
		connectButton.setBackground(new Color(180,180,170));
		connectButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		/*newaccountButton = new Button("ȸ������");
		newaccountButton.setBounds(435, 610, 80, 30);
		newaccountButton.setBackground(new Color(180,180,170));
		newaccountButton.setFont(new Font("",Font.ROMAN_BASELINE,12));*/
		exitButton = new Button("������");
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
      
		// ��Ŀ���� id �Է��ϴ� TextField�� ���� 
		FocusListener wa = new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				idField.requestFocus();
			}
		};      
		newsArea.addFocusListener(wa);
		
		// id �Է��ϴ� ���� Ű �̺�Ʈ �߻��� Connect() �̸� ó�� 
		idField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == k.VK_ENTER) {
					connect();
				}
			}
		});
      
		// �н����� �Է��ϴ� Ű �̺�Ʈ �߻��� Connect() �̸� ó�� 
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == k.VK_ENTER) {
					connect();
				}
			}
		});
		
		// ��ư�� ������ �̺�Ʈ ó�� 
		ActionListener w = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == connectButton) {
					connect();
				} else if(e.getSource() == exitButton) {
					System.exit(1);
				}/* else if(e.getSource() == newaccountButton) {           
					AccountDialog a = new AccountDialog(parentPanel, "ȸ�����Ծ��","good");
					a.show();            // DB�� �̱������� �������� ���� 
				}*/
			}
		};
      
		connectButton.addActionListener(w);
		exitButton.addActionListener(w);
		//newaccountButton.addActionListener(w);
	}
	
	// ���ȭ�� �׸��� �޼ҵ� 
	public void paint(Graphics g) {
		g.drawImage(backImage, 0, 0, this);
	}
  
	// ���̵� ������  Ȯ�εǸ� Mafia_Client ���� connect �Լ����� ���̵�,�н����带 �Ѱ��ְ�
	// Ŭ���̾�Ʈ GUI�� card_layout '����'ȭ��(lobby_Panel)���� �Ѱ��ְ�  
	// ���� Ŭ���̾�Ʈ�� �κ� ������ �÷��׸� ���� �����Ѵ�.
	public void connect() {
		if(!idField.getText().equals("") && !passwordField.getText().equals("")){
			exitButton.setEnabled(false);
			parentPanel.connect(idField.getText(),passwordField.getText());
			parentPanel.setVisible(false);
			parentPanel.MafiaCardLayout.next(parentPanel.cardPanel);
			parentPanel.setVisible(true);
			parentPanel.currentRoomIsLobby = true;
		} else {
			WarningDialog dlg = new WarningDialog(parentPanel,"Warning : Incorrect user information","�߸� �Է��߽��ϴ�.|��Ȯ�� ���̵�� �н����带 �Է��� �ּ���.|�ٽ� �ѹ� �غ����.","NOTEXIT");
			dlg.show();
		}
	}
}