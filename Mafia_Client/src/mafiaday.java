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

	// 투표 다이어 그램
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
		parentPanel.boardChatArea.append("낮이 밝았습니다\n");
		parentPanel.boardChatArea.append("대화를 통해 마피아를 추측해내세요\n");
		parentPanel.boardChatArea.append("시간이 끝나면 투표 시간으로 넘어갑니다\n");
		stage = new Stage(0);
		timeoutint = 0;
		th.settime(20);
		daylabel.setText((daycount + 1) + "번째 낮");
		// countdown t= new countdown(3,1);
		// t1 = new countdown(3);
		// t1.start();
		// daylabel.setText((daycount+1)+"번째 밤");

	}

	public void votetime() {
		for (int j = 0; j < 8; j++)
			parentPanel.UserImage[j].removeMouseListener(my);
		parentPanel.boardChatArea.append("가장 마피아일 것 같은 사람을 투표하세요\n");
		parentPanel.boardChatArea.append("투표를 가장 많이 받은 사람을 죽습니다\n");
		stage = new Stage(1);
		// my = new myMouse();

		daylabel.setText("투표 시간");

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
	// daylabel.setText((daycount + 1) + "번째 밤");
	//
	// stage = new Stage(2);
	// th.settime(20);
	// nightmissionmafia(); //마피아 역활
	// nightmissionpolice();//경찰 역할
	// nightmissiondoctor();//의사 역할
	//
	// //시민 역할
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
		parentPanel.boardChatArea.append("의사는 살리고 싶은 사람을 선택하세요\n");
		stage = new Stage(4);
		daylabel.setText((daycount + 1) + "번째 밤");
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
		parentPanel.boardChatArea.append("경찰은 해당 사람의 직업을 보고싶은 사람을 선택하세요\n");
		stage = new Stage(3);
		daylabel.setText((daycount + 1) + "번째 밤");
		th.settime(20);
		// mypoli = new myPolice();
		for (int i = 0; i < 8; i++) {
			if (parentPanel.parentPanel.id.equals(parentPanel.UserIdText[i]
					.getText())
					&& (parentPanel.num[i].equals("2"))
					&& parentPanel.UserIdText[i].isEnabled()) {
				parentPanel.boardChatArea.setEnabled(true);
				parentPanel.boardChatArea.append("경찰 받음\n");
				for (int j = 0; j < 8; j++) {
					if (parentPanel.num[j].equals("2"))
					{
						parentPanel.boardChatArea.append("자기제외\n");
						continue;
					}
					if (parentPanel.UserImage[j].isEnabled()) {
						parentPanel.boardChatArea.append(j+" addMouseListener\n");
						parentPanel.UserImage[j].addMouseListener(mypoli);
					}
				}
				// 프로토콜 별도
			} else if (parentPanel.parentPanel.id
					.equals(parentPanel.UserIdText[i])) {
				parentPanel.boardChatArea.setEnabled(false);
			}
		}

	}

	public void nightmissionmafia() // 밤미션, 경찰, 의사, 마피아

	{
		for (int j = 0; j < 8; j++) {
			parentPanel.UserImage[j].removeMouseListener(my);
			parentPanel.UserImage[j].removeMouseListener(mymafi);
			parentPanel.UserImage[j].removeMouseListener(mydoc);
			parentPanel.UserImage[j].removeMouseListener(mypoli);
		}
		parentPanel.boardChatArea.append("밤이 왔습니다\n");
		parentPanel.boardChatArea.append("마피아는 죽일사람을 선택하세요\n");

		stage = new Stage(2);
		daylabel.setText((daycount + 1) + "번째 밤");
		th.settime(20);
		// 마피아 지목후 의사가 같은 사람 찍으면 그 사람 살리는 케이스
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
				// 프로토콜 별도
			} else if (parentPanel.parentPanel.id
					.equals(parentPanel.UserIdText[i])) {
				parentPanel.boardChatArea.setEnabled(false);
			}
		}

		// 마피아 투표 로그, 나머지 경우 다 뒤짐
		// 마피아는 서로 알아야 함 처음에 채팅 로그 보내줌
		// 마피아만의 채팅 로그
		// 경찰의 경우 상대방을 클릭했을 때, 메세지로 마피아인지 아닌지 메세지 전달
	}


	public void stopRacing() { // 게임 종료
		th.stop();
		
		// 버튼 올 enabled
	}

	class Stage {
		int stage; // 단계

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
		// Stage stage= new Stage(0);//스레드에서 사용할
		String time = null;

		public countdown(int timesecond) {// 생성자
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
					// System.out.println(i--+"초");
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
				// + "클라이언트에서 0 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "357|" + parentPanel.parentPanel.id + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 1 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "357|" + parentPanel.parentPanel.id + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "357|" + parentPanel.parentPanel.id + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "357|" + parentPanel.parentPanel.id + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "357|" + parentPanel.parentPanel.id + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "357|" + parentPanel.parentPanel.id + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "357|" + parentPanel.parentPanel.id + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

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
				// + "클라이언트에서 0 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "358|" + "mafia" + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 1 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "358|" + "mafia" + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "358|" + "mafia" + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "358|" + "mafia" + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "358|" + "mafia" + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "358|" + "mafia" + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "358|" + "mafia" + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

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
				// + "클라이언트에서 0 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "358|" + "police" + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 1 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "358|" + "police" + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "358|" + "police" + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "358|" + "police" + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "358|" + "police" + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "358|" + "police" + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "358|" + "police" + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

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
				// + "클라이언트에서 0 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image1")) {
				String name = "358|" + "doctor" + "|1";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 1 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image2")) {
				String name = "358|" + "doctor" + "|2";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image3")) {
				String name = "358|" + "doctor" + "|3";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image4")) {
				String name = "358|" + "doctor" + "|4";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image5")) {
				String name = "358|" + "doctor" + "|5";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image6")) {
				String name = "358|" + "doctor" + "|6";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			} else if (l.getName().equals("image7")) {
				String name = "358|" + "doctor" + "|7";
				parentPanel.sendToServer(name);
				// System.out.println(parentPanel.parentPanel.id
				// + "클라이언트에서 2 클릭");

				for (int j = 0; j < 8; j++)
					parentPanel.UserImage[j].removeMouseListener(this);
			}
		}
	}
}