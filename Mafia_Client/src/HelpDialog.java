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
	// ȭ�鱸���� �ʿ��� �⺻���� ���۳�Ʈ��, �ܼ��� �ؽ�Ʈ ǥ���̱� ������ 
	// ���� ���۳�Ʈ�� �ʿ����� ���� 
	Label titleLabel = null;
    TextArea contentField = null;
    Button txt1Button = null, txt2Button = null, txt3Button = null, txt4Button = null;
    
    // �����ڿ��� �⺻ ���̾ƿ��� NULL �� �Ͽ� ���� GUI�� ������ ��� GUI �гε���  
    // ������ǥ�� ���� 
    public HelpDialog(Frame owner, String title, String msg) {
    	super(owner, title);
    	this.setLayout(null);
    	this.setResizable(false);
    	this.setBackground(new Color(255,236,139));
	           
    	setModal(true);   	    // ���̾�α� �����ư ������ ������
    						    // ��� ���ͷ�Ʈ ����.	  
    	// �⺻ ���۳�Ʈ ������ ũ��� ��Ʈ�� ���� ��ġ ���� 
    	titleLabel = new Label(msg);
    	titleLabel.setBounds(10,30,300,30);
    	titleLabel.setFont(new Font("",Font.BOLD,14));
    	titleLabel.setBackground(this.getBackground());
	   
    	contentField = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
    	contentField.setBounds(105,60,330,185);
    	contentField.setText("�ش� ��ư�� �����ø� ������ ���ɴϴ�.");
    	contentField.setFont(new Font("",Font.BOLD,14));
    	contentField.setEditable(false);
    	contentField.setBackground(Color.WHITE);	   

    	txt1Button = new Button("���Ǿ� ���� ����");
    	txt2Button = new Button("�������� ����");
    	txt3Button = new Button("�������� �Ұ�");
    	txt4Button = new Button("��Ÿ ��ɼҰ�");
       
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
   	   // ���̾�α� ��ü�� �����ư �̺�Ʈ �߻��� TextArea �� 
       // �����ϰ� ���̾�α� ����
       addWindowListener(new WindowAdapter() {
    	   public void windowClosing(WindowEvent e) {
    		   contentField.setText("");
    		   dispose();
    	   }
       });
       
       // ��ư �̺�Ʈ �߻��ÿ� �ش� ������  TextArea�� ó��.
       ActionListener ac = new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   if(e.getSource() == txt1Button) {
    			   contentField.setText("");
    			   String txt1=" ���Ǿư����� �����Ϸ��� '���� ��'�� ���� �ϴµ� ���� ��ܿ� �ִ� �̹� �����Ǿ� �ִ�"+ 
    			   "���� ����� ���� �� ������ '��������' ��ư�� ���� �濡 ���� ����� �ְ�, ����ڰ�"+
    			   " ���� ���� ����� �濡 ���� ����� �ִ�.";
    			   contentField.append(txt1);                             	      			    
    		   }else if(e.getSource() == txt2Button) {
    			   contentField.setText("");
    			   String txt2=" ����ȯ ������ �ְ�  ";
    			   contentField.append(txt2);
    		   }else if(e.getSource() == txt3Button) {
    			   contentField.setText("");
    			   String txt3=" A+�ּ���  ";
    			   contentField.append(txt3);                             	    
    		   }else if(e.getSource() == txt4Button) {
    			   contentField.setText("");
    			   String txt4=" ���� �����̳� ���� �����ϴ¹濡�� �ǽð� ä�� ����� �����ϰ� ������, �ӼӸ� ��ɵ� �����Ѵ�.";
    			   contentField.append(txt4);               	    
    		   }
    	   }
       };
         
       txt1Button.addActionListener(ac);
       txt2Button.addActionListener(ac);
       txt3Button.addActionListener(ac);
       txt4Button.addActionListener(ac);

       // �⺻ ��Ŀ�� �̺�Ʈ�� TextArea�� ����, �״��� �ǹ̴� ����    
       FocusListener f = new FocusAdapter() {
    	   public void focusGained(FocusEvent e) {
    		   contentField.requestFocus();
    	   }
       };
       
    } 
	// toolkit�� �̿��� ����Ʈ ȭ���� ũ�⸦ �޾� �� �߿� �� �� �ֵ���
	// �������� ã�� autoAlign() �޼ҵ�, �ٸ� GUI������ �̸� ����  
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