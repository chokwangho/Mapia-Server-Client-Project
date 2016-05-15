import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//�ܼ��� �ؽ�Ʈ�� ����ϴ� ��ȭ�����̴�.

class TextDialog extends Dialog {
	// �⺻���� GUI ���۳�Ʈ ���� 
	Label msgLabel = null;
	TextField field = null;
	String strTitle = null;

	public TextDialog(Frame owner, String title, String msg) {
		super(owner, title);
		setModal(true);             // ���̾�α� �����ư ������ ������
                                    // ��� ���ͷ�Ʈ ����.

		msgLabel = new Label(msg);
		msgLabel.setBackground(new Color(255,236,139));
		msgLabel.setForeground(Color.BLACK);
           
		field = new TextField();
		field.setBackground(Color.WHITE);
            
		add("North", msgLabel);
		add("Center", field);

		// ���̾�α� â�� �ݱ� ��ư�� Ŭ���ϸ�
		// �ؽ�Ʈ�� ""�� ������ â�� �ݴ´�.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				field.setText("");
				dispose();
			}
		});
		
		// �ؽ�Ʈ �ʵ忡 �ؽ�Ʈ�� �Է��� �� ����Ű�� ġ��,
		// ���̾�α� â�� �ݴ´�.
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

	//ȭ���� �߾� ������ ��ġ��Ų��.
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