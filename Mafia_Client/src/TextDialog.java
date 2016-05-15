import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//단순히 텍스트를 출력하는 대화상자이다.

class TextDialog extends Dialog {
	// 기본적인 GUI 컴퍼넌트 생성 
	Label msgLabel = null;
	TextField field = null;
	String strTitle = null;

	public TextDialog(Frame owner, String title, String msg) {
		super(owner, title);
		setModal(true);             // 다이얼로그 종료버튼 누르기 전까지
                                    // 모든 인터럽트 방지.

		msgLabel = new Label(msg);
		msgLabel.setBackground(new Color(255,236,139));
		msgLabel.setForeground(Color.BLACK);
           
		field = new TextField();
		field.setBackground(Color.WHITE);
            
		add("North", msgLabel);
		add("Center", field);

		// 다이얼로그 창의 닫기 버튼을 클릭하면
		// 텍스트를 ""로 지운후 창을 닫는다.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				field.setText("");
				dispose();
			}
		});
		
		// 텍스트 필드에 텍스트를 입력한 후 엔터키를 치면,
		// 다이얼로그 창을 닫는다.
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(ae.getSource() == field) {
					dispose();
				}
			}
		});
		
		setSize(300,100);
		setLocation(200, 150);
		autoAlign();
	}

	//화면의 중앙 가까이 위치시킨다.
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