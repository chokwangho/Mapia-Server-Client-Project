import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.StringTokenizer;

//경고를 출력하기 위한 대화상자이다.

class WarningDialog extends Dialog {
	// 기본적인 GUI 컴퍼넌트 
	Button okButton=null;
	Label msgLabel_1, msgLabel_2, msgLabel_3;
	Panel msgPanel=null, buttonPanel=null;
	String msg, flag;

	// 생성자에서는 기본적인 컴퍼넌트 생성과 크기와 위치와 색 결정 
	public WarningDialog(Frame owner, String title, String message, String flag ) {
		super(owner, title, true);
		setLayout(null);
		setBackground(new Color(255,236,139));
		setSize(300,200);
		setResizable(false);
		this.flag = flag;
      
		// 경고메세지를 나누어 라벨에 입히고 이를 배치 
		StringTokenizer sst = new StringTokenizer(message, "|");
		msg = sst.nextToken();
		msgLabel_1 = new Label(msg,Label.LEFT);
		msgLabel_1.setBackground(this.getBackground());
		msgLabel_1.setBounds(30,30,240,30);
		msg = sst.nextToken();
		msgLabel_2 = new Label(msg,Label.LEFT);
		msgLabel_2.setBackground(this.getBackground());
		msgLabel_2.setBounds(30,60,240,30);
		msg = sst.nextToken();
		msgLabel_3 = new Label(msg,Label.LEFT);
		msgLabel_3.setBackground(this.getBackground());
		msgLabel_3.setBounds(30,90,240,30);
            
		okButton = new Button("OK");     
		okButton.setBackground(new Color(180,180,170));
		okButton.setFont(new Font("",Font.ROMAN_BASELINE,12));
		okButton.setBounds(200,150,80,30);

		add(msgLabel_1);
		add(msgLabel_2);
		add(msgLabel_3);
		add(okButton);
  
		autoAlign();

		// "OK" 버튼을 클릭하면 다이얼로그 창을 닫는다.
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				process();
			}
		});

		// 윈도우의 닫기 버튼을 클릭하면 다이얼로그 창을 닫는다.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				process();
			}
		});      
     
		// 키 이벤트 발생시의 이벤트 설정, 위와 동일한 내용 구현  
		okButton.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent k){
				if(k.getKeyCode() == k.VK_ENTER){
					process();   
				}
			}
		});
	}
   
	public void autoAlign() {
		int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		int dw = getSize().width;
		int dh = getSize().height;
		int x = (((sw - dw) / 2) < 0) ? 0 : ((sw - dw) / 2);
		int y = (((sh - dh) / 2) < 0) ? 0 : ((sh - dh) / 2);
		setLocation(x, y);
	}
   
	// 플래그가 'EXIT' 일때 (WarningDialog 호출시에 마지막 메세지 )
	public void process() {
		if(flag.equals("EXIT")){
			dispose();
			System.exit(1);     // 클라이언트 시스템 종료 
		} else {
			dispose();
		}
	}
}