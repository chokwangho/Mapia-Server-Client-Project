import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class mafiaday extends Panel {
	Stage stage;
	Game_Client parentPanel;
	Panel timepanel = null;
	Panel daypanel = null;

	JLabel daylabel = null;
	JLabel timeout = null;

	String message = null;
	static int daycount = 0;
	int Morning_Night = 0;
	int totaltimesecond;
	int timeoutint = 0;
	myMafia mymafi = new myMafia();
	myDoctor mydoc = new myDoctor();
	myPolice mypoli = new myPolice();
	myMouse my = new myMouse();
	countdown th;

	// ��ǥ ���̾� �׷�
	public mafiaday(Game_Client parentPanel) {
		this.parentPanel = parentPanel;

	}

	public void startRacing() {

		timepanel = new Panel(new FlowLayout());
		daypanel = new Panel(new FlowLayout());

		daylabel = new JLabel("dayofday", JLabel.CENTER);
		timeout = new JLabel("timeout", JLabel.CENTER);

		daypanel.setBounds(60, 50, 80, 40);

		timepanel.setBounds(510, 50, 130, 40);

		daylabel.setSize(80, 40);
		daylabel.setForeground(Color.RED);
		timeout.setSize(130, 40);
		timeout.setForeground(Color.RED);

		timepanel.add(timeout);
		daypanel.add(daylabel);

		parentPanel.add(daypanel);
		parentPanel.add(timepanel);
		th = new countdown(5);
		th.start();

		chattime();

	}

	public void chattime() {
		parentPanel.boardChatArea.append("���� ��ҽ��ϴ�\n");
		parentPanel.boardChatArea.append("��ȭ�� ���� ���ǾƸ� �����س�����\n");
		parentPanel.boardChatArea.append("�ð��� ������ ��ǥ �ð����� �Ѿ�ϴ�\n");
		stage = new Stage(0);
		timeoutint = 0;
		th.settime(20);
		daylabel.setText((daycount + 1) + "��° ��");
		// countdown t= new countdown(3,1);
		// t1 = new countdown(3);
		// t1.start();
		// daylabel.setText((daycount+1)+"��° ��");

	}

	public void votetime() {
		for (int j = 0; j < 8; j++)
			parentPanel.UserImage[j].removeMouseListener(my);
		parentPanel.boardChatArea.append("���� ���Ǿ��� �� ���� ����� ��ǥ�ϼ���\n");
		parentPanel.boardChatArea.append("��ǥ�� ���� ���� ���� ����� �׽��ϴ�\n");
		stage = new Stage(1);
		// my = new myMouse();

		daylabel.setText("��ǥ �ð�");

		for (int i = 0; i < 8; i++) {
			if (parentPanel.parentPanel.id.equals(parentPanel.UserIdText[i]
					.getText()) && parentPanel.UserImage[i].isEnabled()) {
				for (int j = 0; j < 8; j++) {
					if (parentPanel.UserImage[j].isEnabled()) {
						parentPanel.UserImage[j].addMouseListener(my);
					}
				}
			}
		}

		timeoutint = 1;
		th.settime(20);
		// t2 = new countdown(5);
		// t2.start();

	}

	// public void nightmission() {
	// daylabel.setText((daycount + 1) + "��° ��");
	//
	// stage = new Stage(2);
	// th.settime(20);
	// nightmissionmafia(); //���Ǿ� ��Ȱ
	// nightmissionpolice();//���� ����
	// nightmissiondoctor();//�ǻ� ����
	//
	// //�ù� ����
	//
	//
	// daycount++;
	// }
	public void nightmissiondoctor() {
		for (int j = 0; j < 8; j++) {
			parentPanel.UserImage[j].removeMouseListener(my);
			parentPanel.UserImage[j].removeMouseListener(mymafi);
			parentPanel.UserImage[j].removeMouseListener(mydoc);
			parentPanel.UserImage[j].removeMouseListener(mypoli);
		}
		parentPanel.boardChatArea.append("�ǻ�� �츮�� ���� ����� �����ϼ���\n");
		stage = new Stage(4);
		daylabel.setText((daycount + 1) + "��° ��");
		th.settime(20);

		// mydoc = new myDoctor();
		for (int i = 0; i < 8; i++) {

			if (parentPanel.parentPanel.id.equals(parentPanel.UserIdText[i]
					.getText())
					&& (parentPanel.num[i].equals("3"))
					&& parentPanel.UserIdText[i].isEnabled()) {
				parentPanel.boardChatArea.setEnabled(true);
				for (int j = 0; j < 8; j++) {
					if (parentPanel.num[j].equals("3"))
						continue;
//					if (parentPanel.UserImage[j].isEnabled()) {
					parentPanel.boardChatArea.append(j+" addMouseListener\n");
					parentPanel.UserImage[j].addMouseListener(mydoc);
//					}
				}
			} else if (parentPanel.parentPanel.id
					.equals(parentPanel.UserIdText[i])) {
				parentPanel.boardChatArea.setEnabled(false);
			}
		}

		for (int i = 0; i < 8; i++) {
			if (parentPanel.parentPanel.id.equals(parentPanel.UserIdText[i])) {
				parentPanel.boardChatArea.setEnabled(true);
			}
		}

		daycount++;
	}

	public void nightmissionpolice() {
		for (int j = 0; j < 8; j++) {
			parentPanel.UserImage[j].removeMouseListener(my);
			parentPanel.UserImage[j].removeMouseListener(mymafi);
			parentPanel.UserImage[j].removeMouseListener(mydoc);
			parentPanel.UserImage[j].removeMouseListener(mypoli);
		}
		parentPanel.boardChatArea.append("������ �ش� ����� ������ ������� ����� �����ϼ���\n");
		stage = new Stage(3);
		daylabel.setText((daycount + 1) + "��° ��");
		th.settime(20);
		// mypoli = new myPolice();
		for (int i = 0; i < 8; i++) {
			if (parentPanel.parentPanel.id.equals(parentPanel.UserIdText[i]
					.getText())
					&& (parentPanel.num[i].equals("2"))
					&& parentPanel.UserIdText[i].isEnabled()) {
				parentPanel.boardChatArea.setEnabled(true);
				parentPanel.boardChatArea.append("���� ����\n");
				for (int j = 0; j < 8; j++) {
					if (parentPanel.num[j].equals("2"))
					{
						parentPanel.boardChatArea.append("�ڱ�����\n");
						continue;
					}
					if (parentPanel.UserImage[j].isEnabled()) {
						parentPanel.boardChatArea.append(j+" addMouseListener\n");
						parentPanel.UserImage[j].addMouseListener(mypoli);
					}
				}
				// �������� ����
			} else if (parentPanel.parentPanel.id
					.equals(parentPanel.UserIdText[i])) {
				parentPanel.boardChatArea.setEnabled(false);
			}
		}

	}

	public void nightmissionmafia() // ��̼�, ����, �ǻ�, ���Ǿ�

	{
		for (int j = 0; j < 8; j++) {
			parentPanel.UserImage[j].removeMouseListener(my);
			parentPanel.UserImage[j].removeMouseListener(mymafi);
			parentPanel.UserImage[j].removeMouseListener(mydoc);
			parentPanel.UserImage[j].removeMouseListener(mypoli);
		}
		parentPanel.boardChatArea.append("���� �Խ��ϴ�\n");
		parentPanel.boardChatArea.append("���Ǿƴ� ���ϻ���� �����ϼ���\n");

		stage = new Stage(2);
		daylabel.setText((daycount + 1) + "��° ��");
		th.settime(20);
		// ���Ǿ� ������ �ǻ簡 ���� ��� ������ �� ��� �츮�� ���̽�
		// mymafi = new myMafia();
		for (int i = 0; i < 8; i++) {
			if (parentPanel.parentPanel.id.equals(parentPanel.UserIdText[i]
					.getText())
					&& (parentPanel.num[i].equals("1"))
					&& parentPanel.UserIdText[i].isEnabled()) {
				parentPanel.boardChatArea.setEnabled(true);
				for (int j = 0; j < 8; j++) {
					if (parentPanel.num[j].equals("1"))
						continue;
					if (parentPanel.UserImage[j].isEnabled()) {
						parentPanel.UserImage[j].addMouseListener(mymafi);

					}

				}
				// �������� ����
			} else if (parentPanel.parentPanel.id
					.equals(parentPanel.UserIdText[i])) {
				parentPanel.boardChatArea.setEnabled(false);
			}
		}

		// ���Ǿ� ��ǥ �α�, ������ ��� �� ����
		// ���Ǿƴ� ���� �˾ƾ� �� ó���� ä�� �α� ������
		// ���ǾƸ��� ä�� �α�
		// ������ ��� ������ Ŭ������ ��, �޼����� ���Ǿ����� �ƴ��� �޼��� ����
	}


	public void stopRacing() { // ���� ����
		th.stop();
		
		// ��ư �� enabled
	}

	class Stage {
		int stage; // �ܰ�

		public Stage(int stage) {
			this.stage = stage;
		}

		synchronized void getStage() {
			if (stage == 0) {

				votetime();
			}
			if (stage == 1) {

				nightmissionmafia();
			}
			if (stage == 2) {

				nightmissionpolice();
			}
			if (stage == 3) {

				nightmissiondoctor();
			}
			if (stage == 4) {
				chattime();
			}

		}
	}

	public class countdown extends Thread {
		// Stage stage= new Stage(0);//�����忡�� �����
		String time = null;

		public countdown(int timesecond) {// ������
			totaltimesecond = timesecond;
		}

		public void settime(int timesecond) {
			totaltimesecond = timesecond;
		}

		public void run() {

			while (true) {
				if (totaltimesecond == 0) {

					stage.getStage();

				}

				try {
					Thread.sleep(1000);
					time = Integer.toString(totaltimesecond--);
					timeout.setText(time);
					// System.out.println(i--+"��");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class myMouse extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();

			if (l.getName().equals("image0")) {
				String name = "357|" + parentPanel.parentPanel.id + "|0";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 0 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "357|" + parentPanel.parentPanel.id + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 1 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "357|" + parentPanel.parentPanel.id + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "357|" + parentPanel.parentPanel.id + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "357|" + parentPanel.parentPanel.id + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "357|" + parentPanel.parentPanel.id + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "357|" + parentPanel.parentPanel.id + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "357|" + parentPanel.parentPanel.id + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			}
		}

	}

	public class myMafia extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();

			if (l.getName().equals("image0")) {
				String name = "358|" + "mafia" + "|0";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 0 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "358|" + "mafia" + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 1 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "358|" + "mafia" + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "358|" + "mafia" + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "358|" + "mafia" + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "358|" + "mafia" + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "358|" + "mafia" + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "358|" + "mafia" + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			}

		}
	}

	public class myPolice extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();

			if (l.getName().equals("image0")) {
				String name = "358|" + "police" + "|0";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 0 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "358|" + "police" + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 1 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "358|" + "police" + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "358|" + "police" + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "358|" + "police" + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "358|" + "police" + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "358|" + "police" + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "358|" + "police" + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			}

		}
	}

	public class myDoctor extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();

			if (l.getName().equals("image0")) {
				String name = "358|" + "doctor" + "|0";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 0 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "358|" + "doctor" + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 1 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "358|" + "doctor" + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "358|" + "doctor" + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "358|" + "doctor" + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "358|" + "doctor" + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "358|" + "doctor" + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "358|" + "doctor" + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "Ŭ���̾�Ʈ���� 2 Ŭ��");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			}
		}
	}
}