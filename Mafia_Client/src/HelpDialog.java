import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class HelpDialog extends Dialog {
	// 화면구성에 필요한 기본적인 컴퍼넌트들, 단순한 텍스트 표시이기 때문에 
	// 많은 컴퍼넌트가 필요하지 않음 
	Label titleLabel = null;
    TextArea contentField = null;
    Button txt1Button = null, txt2Button = null, txt3Button = null, txt4Button = null;
    
    // 생성자에서 기본 레이아웃을 NULL 로 하여 게임 GUI를 제외한 모든 GUI 패널들은  
    // 절대좌표로 설정 
    public HelpDialog(Frame owner, String title, String msg) {
    	super(owner, title);
    	this.setLayout(null);
    	this.setResizable(false);
    	this.setBackground(new Color(255,236,139));
	           
    	setModal(true);   	    // 다이얼로그 종료버튼 누르기 전까지
    						    // 모든 인터럽트 방지.	  
    	// 기본 컴퍼넌트 생성과 크기와 폰트와 색과 배치 결정 
    	titleLabel = new Label(msg);
    	titleLabel.setBounds(10,30,300,30);
    	titleLabel.setFont(new Font("",Font.BOLD,14));
    	titleLabel.setBackground(this.getBackground());
	   
    	contentField = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
    	contentField.setBounds(105,60,330,185);
    	contentField.setText("해당 버튼을 누르시면 설명이 나옵니다.");
    	contentField.setFont(new Font("",Font.BOLD,14));
    	contentField.setEditable(false);
    	contentField.setBackground(Color.WHITE);	   

    	txt1Button = new Button("마피아 게임 시작");
    	txt2Button = new Button("게임진행 순서");
    	txt3Button = new Button("게임직업 소개");
    	txt4Button = new Button("기타 기능소개");
       
    	txt1Button.setBounds(10,65,85,30);
    	txt2Button.setBounds(10,115,85,30);
    	txt3Button.setBounds(10,165,85,30);
    	txt4Button.setBounds(10,215,85,30);
	   
    	txt1Button.setBackground(new Color(180,180,170));
    	txt2Button.setBackground(new Color(180,180,170));
    	txt3Button.setBackground(new Color(180,180,170));
    	txt4Button.setBackground(new Color(180,180,170));
	   
       	add(titleLabel);
    	add(contentField);
    	add(txt1Button);
    	add(txt2Button);
    	add(txt3Button);
    	add(txt4Button);
       
       setSize(445,270);
       autoAlign();      
   	   // 다이얼로그 자체의 종료버튼 이벤트 발생시 TextArea 를 
       // 정리하고 다이얼로그 삭제
       addWindowListener(new WindowAdapter() {
    	   public void windowClosing(WindowEvent e) {
    		   contentField.setText("");
    		   dispose();
    	   }
       });
       
       // 버튼 이벤트 발생시에 해당 도움말을  TextArea에 처리.
       ActionListener ac = new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   if(e.getSource() == txt1Button) {
    			   contentField.setText("");
    			   String txt1=" 마피아게임을 시작하려면 '게임 방'에 들어가야 하는데 왼쪽 상단에 있는 이미 개설되어 있는"+ 
    			   "방의 목록을 선택 한 다음에 '게임참여' 버튼을 눌러 방에 들어가는 방법이 있고, 사용자가"+
    			   " 방을 새로 만들어 방에 들어가는 방법이 있다.";
    			   contentField.append(txt1);                             	      			    
    		   }else if(e.getSource() == txt2Button) {
    			   contentField.setText("");
    			   String txt2=" 정인환 교수님 최고  ";
    			   contentField.append(txt2);
    		   }else if(e.getSource() == txt3Button) {
    			   contentField.setText("");
    			   String txt3=" A+주세요  ";
    			   contentField.append(txt3);                             	    
    		   }else if(e.getSource() == txt4Button) {
    			   contentField.setText("");
    			   String txt4=" 게임 대기방이나 게임 진행하는방에는 실시간 채팅 기능을 포함하고 있으며, 귓속말 기능도 지원한다.";
    			   contentField.append(txt4);               	    
    		   }
    	   }
       };
         
       txt1Button.addActionListener(ac);
       txt2Button.addActionListener(ac);
       txt3Button.addActionListener(ac);
       txt4Button.addActionListener(ac);

       // 기본 포커스 이벤트는 TextArea로 설정, 그다지 의미는 없음    
       FocusListener f = new FocusAdapter() {
    	   public void focusGained(FocusEvent e) {
    		   contentField.requestFocus();
    	   }
       };
       
    } 
	// toolkit을 이용한 디폴트 화면의 크기를 받아 정 중에 올 수 있도록
	// 시작점을 찾는 autoAlign() 메소드, 다른 GUI에서도 이를 재사용  
    public void autoAlign() {
    	int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
    	int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
    	int dw = getSize().width;
    	int dh = getSize().height;
    	int x = (((sw - dw) / 2) < 0) ? 0 : ((sw - dw) / 2);
    	int y = (((sh - dh) / 2) < 0) ? 0 : ((sh - dh) / 2);
    	
    	setLocation(x, y);
    }
}