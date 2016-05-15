import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.StringTokenizer;

//��� ����ϱ� ���� ��ȭ�����̴�.

class WarningDialog extends Dialog {
	// �⺻���� GUI ���۳�Ʈ 
	Button okButton=null;
	Label msgLabel_1, msgLabel_2, msgLabel_3;
	Panel msgPanel=null, buttonPanel=null;
	String msg, flag;

	// �����ڿ����� �⺻���� ���۳�Ʈ ������ ũ��� ��ġ�� �� ���� 
	public WarningDialog(Frame owner, String title, String message, String flag ) {
		super(owner, title, true);
		setLayout(null);
		setBackground(new Color(255,236,139));
		setSize(300,200);
		setResizable(false);
		this.flag = flag;
      
		// ���޼����� ������ �󺧿� ������ �̸� ��ġ 
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

		// "OK" ��ư�� Ŭ���ϸ� ���̾�α� â�� �ݴ´�.
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				process();
			}
		});

		// �������� �ݱ� ��ư�� Ŭ���ϸ� ���̾�α� â�� �ݴ´�.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				process();
			}
		});      
     
		// Ű �̺�Ʈ �߻����� �̺�Ʈ ����, ���� ������ ���� ����  
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
   
	// �÷��װ� 'EXIT' �϶� (WarningDialog ȣ��ÿ� ������ �޼��� )
	public void process() {
		if(flag.equals("EXIT")){
			dispose();
			System.exit(1);     // Ŭ���̾�Ʈ �ý��� ���� 
		} else {
			dispose();
		}
	}
}