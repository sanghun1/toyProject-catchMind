// ������
package design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.*;

public class MainStart extends JFrame {
	// �ʼ� �±�
	private static final String TAG = "mainStart :";
	// Ŭ���̾�Ʈ�� �Է��� ���̵� ���� Ŭ���̾�Ʈ�� �˵��� ���� ������ ����. 
	private String IDString; 
	// ���þ ��Ƴ��� �迭.
	public String[] problem = { "���", "����", "����", "�ڹ�", "������Ʈ" };
	// ���� ��ȭ�� �� ���� ���þ ���������� �����ϴ� ����.
	public int selectProblem = 0;
	

	private ImageIcon icGameStart;
	
	private ImageIcon iconBlackPen;
	private ImageIcon iconRedPen;
	private ImageIcon iconOrangePen;
	private ImageIcon iconYellowPen;
	private ImageIcon iconGreenPen;
	private ImageIcon iconBluePen;
	private ImageIcon iconIndigoPen;
	private ImageIcon iconPurplePen;

	// ���
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	// �̹��� �г�
	private MyPanel plMain; // �ʱ� ���� ȭ�� �̹���

	// plMain�� ���Ե�
	private JButton btnStart; // ���̵� �Է� �� ���ӽ��� ��ư

	// btnStart�� ������ plId�� ��Ÿ��
	private JPanel plId; // ���̵� �Է� �г�
	private JPanel plSub; // ���̵� �Է� �г�

	// plId�� ���Ե�
	private JLabel laId; // '���̵� �Է��ϼ���'��
	private TextField tfIdInput; // ���̵� �Է�
	private JButton btnId; // ���̵� �Է� ��ư

	// btnStart�� ������ plDrawRoom�� �ٲ�
	private JPanel plDrawRoom; // �׸���� �г�(ȭ�� ��ü)

	// plDrawRoom�� ���Ե�

	// plTop�� plMplId
	private JPanel plTopMpId;

	// ���� �� �̸� , ���þ�, �ѱ�� ��ư
	private JPanel plTop;

	// plTop�� ���Ե�
	private JLabel laQuizTitle; // '���þ� : ' ��
	private JLabel laQuiz; // ���þ� ���� ��
	private JButton btnSkip; // �ѱ�� ��ư

	// ������ ������ �߰� �׸���
	private JPanel plMplId; // �׸���

	// plMplId�� ���Ե�
	private MyPanel1 plDraw; // �׸��� �̹���

	// �Ʒ��� �ȷ�Ʈ
	private JPanel plBottom; // �ȷ�Ʈ

	private MyButton1 btnDelete; // ���찳 �̹���

	// plBottom�� ���Ե�
	private MyPanel2 plPalette; // ũ���Ľ� �̹���

	private JButton btnBlackDrawPen;
	private JButton btnRedDrawPen;
	private JButton btnOrangeDrawPen;
	private JButton btnYellowDrawPen;
	private JButton btnGreenDrawPen;
	private JButton btnBlueDrawPen;
	private JButton btnIndigoDrawPen;
	private JButton btnPurpleDrawPen;

	private MyButton btnEraser; // ���찳 �̹���

	// ������ �������, ä��, �غ�Ϸ�, ������ ��ư
	private JPanel plEast;

	// plEast�� ���Ե�
	private JTextArea taUserList; // ���� ��� ��

	// plEast�� ���Ե� ä�� �г�
	private JPanel plChat; // ä��â, ä�� �Է¶�

	// plChat�� ���Ե�
	private TextField tfChat; // ä�� �Է�
	private JTextArea taChat; // ä�� �α�
	private JScrollPane scrChat;

	// �غ�Ϸ�, ������ ��ư �г�
	private JPanel btnPanel; // ä��â, ä�� �Է¶�

	// btnPanel�� ���Ե�
	private JButton btnReady; // �غ� �Ϸ� ��ư
	private JButton btnExit; // ������ ��ư

	// ��Ʈ ũ�� ����
	private Font ftSmall; // 16pxũ�� ��Ʈ
	private Font ftMedium; // 24pxũ�� ��Ʈ
	private Font ftLarge; // 36pxũ�� ��Ʈ
	
	// Brush ��ǥ��
	int x, y;
	// Brush ����
	//Color color;
	
	// Draw�� �ʿ��� ����
	private BufferedImage imgBuff;
	private JLabel drawLabel;
	private JPanel drawPanel;
	private Brush brush;
	String sendDraw = null;
	String sendColor = null;
	boolean drawPPAP = true;

	
	// �̹��� �ż���
	private ImageIcon ImageSetSize(ImageIcon icon, int width, int heigth) {
		Image xImage = icon.getImage();
		Image yImage = xImage.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		ImageIcon xyImage = new ImageIcon(yImage);
		return xyImage;
	}

	// �̹��� ���Կ� Ŭ����
	class MyPanel extends JPanel {
		private ImageIcon icon = new ImageIcon("img\\catchMindBG.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	};

	class MyPanel1 extends JPanel {
		private ImageIcon icon = new ImageIcon("img\\draw.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	};

	class MyPanel2 extends JPanel {
		private ImageIcon icon = new ImageIcon("img\\drawColor.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	};

	class MyButton extends JButton {
		private ImageIcon icon = new ImageIcon("img\\drawEraser.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
			setBorderPainted(false); // ��ư �׵θ� ����
		}
	};

	class MyButton1 extends JButton {
		private ImageIcon icon = new ImageIcon("img\\allDelete.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
			setBorderPainted(false); // ��ư �׵θ� ����
		}
	};

	public MainStart() {
		init();
		setting();
		batch();
		listener();
		setVisible(true);
	}

	private void init() {
		// �̹��� �г�
		plMain = new MyPanel(); // �ʱ� ���� ȭ�� �̹���
		plTopMpId = new MyPanel1(); // plMplId - �׸��� �̹���
		plPalette = new MyPanel2(); // plBottom - ũ���Ľ� �̹���
		btnEraser = new MyButton(); // plBottom - ���찳 �̹���
		btnDelete = new MyButton1(); // plBottom - ������ �̹���

		// �г�
		plId = new JPanel(); // plMain - �ʱ� ���̵� �Է� �г�
		plSub = new JPanel(); // plMain - �ʱ� ���̵� �Է� �г�
		plDrawRoom = new JPanel(); // ���̵� �Է��ϰ� ��ư�� ������ ������ �г�, ����ȭ�� ��ü

		// plDrawRoom
		plTop = new JPanel();
		plMplId = new JPanel();
		plBottom = new JPanel();
		plEast = new JPanel();
		btnPanel = new JPanel();

		// plEast
		plChat = new JPanel();

		// �̹���
		icGameStart = new ImageIcon("img\\gameStart.png"); // ���ӽ��� ��ư �̹���
		
		iconBlackPen = new ImageIcon("img\\drawBlackPen.png");
		iconRedPen= new ImageIcon("img\\drawRedPen.png");
		iconOrangePen = new ImageIcon("img\\drawOrangePen.png");
		iconYellowPen = new ImageIcon("img\\drawYellowPen.png");
		iconGreenPen = new ImageIcon("img\\drawGreenPen.png");
		iconBluePen = new ImageIcon("img\\drawBluePen.png");
		iconIndigoPen = new ImageIcon("img\\drawIndigoPen.png");
		iconPurplePen = new ImageIcon("img\\drawPurplePen.png");
		
		// ��ư
		btnStart = new JButton(icGameStart); // plMain
		btnId = new JButton(icGameStart); // plMain
		btnSkip = new JButton("�ѱ��"); // plTop
		btnReady = new JButton("�غ�"); // plEast
		btnExit = new JButton("������"); // plEast

		btnBlackDrawPen = new JButton(iconBlackPen);
		btnRedDrawPen = new JButton(iconRedPen);
		btnOrangeDrawPen = new JButton(iconOrangePen);
		btnYellowDrawPen = new JButton(iconYellowPen);
		btnGreenDrawPen = new JButton(iconGreenPen);
		btnBlueDrawPen = new JButton(iconBluePen);
		btnIndigoDrawPen = new JButton(iconIndigoPen);
		btnPurpleDrawPen = new JButton(iconPurplePen);

		// ��
		laId = new JLabel("���̵�"); // plMain
		laQuizTitle = new JLabel("���þ�");
		laQuiz = new JLabel("����"); // plTop

		// �ؽ�Ʈ �Է¶�
		tfIdInput = new TextField(); // plMain
		tfChat = new TextField(); // plEast

		// �ؽ�Ʈ ����
		taChat = new JTextArea(); // plEast
		taUserList = new JTextArea();
		// ��Ʈ
		ftSmall = new Font("�������", Font.PLAIN, 16);
		ftMedium = new Font("�������", Font.PLAIN, 24);
		ftLarge = new Font("�������", Font.PLAIN, 36);

		// ��ũ�� ��
		scrChat = new JScrollPane(taChat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// ��ο� ĵ����
		imgBuff = new BufferedImage(750, 450, BufferedImage.TYPE_INT_ARGB);
		drawLabel = new JLabel(new ImageIcon(imgBuff));
		drawPanel = new JPanel();
		brush = new Brush();
	}

	private void setting() {
		setTitle("ĳġ���ε�");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// plMain
		setContentPane(plMain);
		plMain.setLayout(null);
		btnStart.setBounds(300, 360, 180, 110); // btnStart ��ġ, ũ�� ���� (x, y, width, height)
		btnStart.setBorderPainted(false); // ��ư �׵θ� ����

		icGameStart = ImageSetSize(icGameStart, 180, 110); // ���� ���� ��ư �̹���
		// plId
		plId.setLayout(null);
		plId.setVisible(false); // ��Ȱ��ȭ
		plId.setBackground(new Color(242, 242, 242));
		plId.setBounds(180, 200, 420, 300); // plId ��ġ, ũ�� ���� (x, y, width, height) ��ǥ�� plMain ����

		plSub.setLayout(null);
		plSub.setVisible(false); // ��Ȱ��ȭ
		plSub.setBorder(new LineBorder(new Color(87, 87, 87), 3, true));
		plSub.setBounds(90, 50, 246, 36); // plId ��ġ, ũ�� ���� (x, y, width, height) ��ǥ�� plMain ����

		laId.setBounds(0, 2, 62, 32); // laId ��ġ, ũ�� ���� (x, y, width, height) ��ǥ�� plId ����
		laId.setBorder(new LineBorder(new Color(87, 87, 87), 2, true));
		laId.setFont(ftSmall);
		laId.setHorizontalAlignment(JLabel.CENTER); // ���� ��� ����

		tfIdInput.setBounds(63, 3, 180, 30); // tfIdInput ��ġ, ũ�� ���� (x, y, width, height) ��ǥ�� plId ����
		tfIdInput.setBackground(new Color(242, 242, 242, 255));
		tfIdInput.setFont(ftMedium);

		btnId.setBounds(120, 150, 180, 110); // btnId ��ġ, ũ�� ���� (x, y, width, height) ��ǥ�� plId ����
		btnId.setBorderPainted(false); // ��ư �׵θ� ����

		// plDrawRoom
		plDrawRoom.setLayout(null);
		plDrawRoom.setVisible(false); // ��Ȱ��ȭ
		plDrawRoom.setBounds(70, 120, 1005, 660);// plDrawRoom ��ġ, ũ�� ���� ��ǥ�� plMain ����

		// plDrawRoom - plTopMpId
		plTopMpId.setLayout(null);
		plTopMpId.setBackground(new Color(255, 255, 255, 255));
		plTopMpId.setBounds(0, 0, 750, 530);

		// plDrawRoom - plTop
		plTop.setLayout(null);
		plTop.setBackground(new Color(255, 255, 255, 0));
		plTop.setBounds(0, 0, 750, 80); // plTop ��ġ, ũ�� ���� ��ǥ�� plDrawRoom ����

		// plDrawRoom - plMplId
		plMplId.setLayout(null);
		plMplId.setBackground(new Color(255, 255, 255, 255));
		plMplId.setBounds(0, 110, 750, 450); // plMplId ��ġ, ũ�� ���� ��ǥ�� plDrawRoom ����

		// plDrawRoom - plBottom
		plBottom.setLayout(null);
		plBottom.setBackground(new Color(242, 242, 242, 255));
		plBottom.setBounds(0, 530, 700, 130); // plBottom ��ġ, ũ�� ���� ��ǥ�� plDrawRoom ����
		
		iconBlackPen = ImageSetSize(iconBlackPen, 65, 130);
		iconRedPen = ImageSetSize(iconRedPen, 65, 130);
		iconOrangePen = ImageSetSize(iconOrangePen, 65, 130);
		iconYellowPen = ImageSetSize(iconYellowPen, 65, 130);
		iconGreenPen = ImageSetSize(iconGreenPen, 65, 130);
		iconBluePen = ImageSetSize(iconBluePen, 65, 130);
		iconIndigoPen = ImageSetSize(iconIndigoPen, 65, 130);
		iconPurplePen = ImageSetSize(iconPurplePen, 65, 130);
		
		btnBlackDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnBlackDrawPen.setBounds(0, 0, 65, 130);
		btnBlackDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		btnRedDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnRedDrawPen.setBounds(65, 0, 65, 130);
		btnRedDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		btnOrangeDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnOrangeDrawPen.setBounds(130, 0, 65, 130);
		btnOrangeDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		btnYellowDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnYellowDrawPen.setBounds(195, 0, 65, 130);
		btnYellowDrawPen.setBorderPainted(false); // ��ư �׵θ� ����

		btnGreenDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnGreenDrawPen.setBounds(260, 0, 65, 130);
		btnGreenDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		btnBlueDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnBlueDrawPen.setBounds(325, 0, 65, 130);
		btnBlueDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		btnIndigoDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnIndigoDrawPen.setBounds(390, 0, 65, 130);
		btnIndigoDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		btnPurpleDrawPen.setBackground(new Color(242, 242, 242, 255));
		btnPurpleDrawPen.setBounds(455, 0, 65, 130);
		btnPurpleDrawPen.setBorderPainted(false); // ��ư �׵θ� ����
		
		// plDrawRoom - plEast
		plEast.setLayout(null);
		plEast.setBounds(750, 0, 255, 530); // plEast ��ġ, ũ�� ���� ��ǥ�� plDrawRoom ����

		// plDrawRoom - plChat
		plChat.setLayout(null);

		// plDrawRoom - btnPanel
		btnPanel.setLayout(null);
		btnPanel.setBackground(new Color(242, 242, 242, 255));
		btnPanel.setBounds(700, 530, 405, 130);

		// plTop

		// plMplId
//		plDraw.setBackground(new Color(242, 242, 242, 255));
//		plDraw.setBounds(0, 0, 750, 420); // plDraw ��ġ, ũ�� ���� ��ǥ�� plMplId ����

		// plBottom
		plPalette.setLayout(null);
		plPalette.setBackground(new Color(242, 242, 242, 255));
		plPalette.setBounds(0, 0, 520, 130); // plPalette ��ġ, ũ�� ���� ��ǥ�� plBottom ����

		btnEraser.setBackground(new Color(242, 242, 242, 255));
		btnEraser.setBounds(520, 0, 80, 130); // btnEraser ��ġ, ũ�� ���� ��ǥ�� plBottom ����

		btnDelete.setBackground(new Color(242, 242, 242, 255));
		btnDelete.setBounds(600, 0, 100, 130); // btnEraser ��ġ, ũ�� ���� ��ǥ�� plBottom ����

		// plEast
		taUserList.setBounds(0, 0, 255, 150); // taUserList ��ġ, ũ�� ���� ��ǥ�� plEast ����
		taUserList.setFont(ftMedium);
		taUserList.setBackground(new Color(242, 242, 242, 255));
		taUserList.setLineWrap(true);

		plChat.setBackground(Color.WHITE);
		plChat.setBounds(0, 150, 255, 385); // plChat ��ġ, ũ�� ���� ��ǥ�� plEast ����

		// plEast - plChat
		tfChat.setBackground(Color.WHITE);
		tfChat.setBounds(0, 350, 255, 30); // tfChat ��ġ, ũ�� ���� ��ǥ�� plEast ����
		tfChat.setFont(ftMedium);
		tfChat.setBackground(new Color(242, 242, 242, 255));
		tfChat.setColumns(30);

		scrChat.setBounds(0, 0, 255, 350); // taChat ��ġ, ũ�� ���� ��ǥ�� plEast ����
		scrChat.setFocusable(false);

		taChat.setLineWrap(true);
		taChat.setBackground(new Color(242, 242, 242, 255));

		// btnPanel
		laQuizTitle.setVisible(true);
		laQuizTitle.setBounds(0, 2, 155, 65); // laQuiz ��ġ, ũ�� ���� ��ǥ�� plTop ����
		laQuizTitle.setFont(ftMedium);
		laQuizTitle.setBackground(new Color(242, 242, 242, 255));
		laQuizTitle.setHorizontalAlignment(JLabel.CENTER); // ���� ��� ����

		laQuiz.setVisible(false);
		laQuiz.setBounds(0, 67, 155, 65); // laQuiz ��ġ, ũ�� ���� ��ǥ�� plTop ����
		laQuiz.setFont(ftMedium);
		laQuiz.setBackground(new Color(242, 242, 242, 255));
		laQuiz.setHorizontalAlignment(JLabel.CENTER); // ���� ��� ����

		btnReady.setBounds(150, 2, 155, 65); // btnReady ��ġ, ũ�� ���� ��ǥ�� plEast ����
		btnReady.setFont(ftMedium);
		btnReady.setBackground(new Color(242, 242, 242, 255));
		btnReady.setBorder(new LineBorder(new Color(87, 87, 87), 5, true));

		btnSkip.setVisible(false);
		btnSkip.setBounds(150, 2, 155, 65); // btnSkip ��ġ, ũ�� ���� ��ǥ�� plTop ����
		btnSkip.setFont(ftMedium);
		btnSkip.setBackground(new Color(242, 242, 242, 255));
		btnSkip.setBorder(new LineBorder(new Color(87, 87, 87), 5, true));

		btnExit.setBounds(150, 62, 155, 65); // btnExit ��ġ, ũ�� ���� ��ǥ�� plEast ����
		btnExit.setFont(ftMedium);
		btnExit.setBackground(new Color(242, 242, 242, 255));
		btnExit.setBorder(new LineBorder(new Color(87, 87, 87), 5, true));

		// ��ο� ĵ����
		drawLabel.setBounds(0, 0, 750, 450);
		drawLabel.setBackground(new Color(255, 255, 255, 0));
		brush.setBounds(0, 0, 750, 450);
		
		setSize(800, 640);
	}

	private void batch() {
		plMain.add(btnStart);
		plMain.add(plId);
		plMain.add(plDrawRoom);
		btnStart.setIcon(icGameStart);

		plId.add(plSub);
		plSub.add(laId);
		plSub.add(tfIdInput);
		plId.add(btnId);
		btnId.setIcon(icGameStart);

		plDrawRoom.add(plTopMpId);

		plTopMpId.add(plTop);
		plTopMpId.add(plMplId);

		plDrawRoom.add(plBottom);
		plDrawRoom.add(plEast);
		plDrawRoom.add(btnPanel);

//		plMplId.add(plDraw);

		plBottom.add(plPalette);
		plBottom.add(btnEraser);
		plBottom.add(btnDelete);

		plPalette.add(btnBlackDrawPen);
		plPalette.add(btnRedDrawPen);
		plPalette.add(btnOrangeDrawPen);
		plPalette.add(btnYellowDrawPen);
		plPalette.add(btnGreenDrawPen);
		plPalette.add(btnBlueDrawPen);
		plPalette.add(btnIndigoDrawPen);
		plPalette.add(btnPurpleDrawPen);

		plEast.add(plChat);
		plEast.add(taUserList);

		plChat.add(scrChat);
		plChat.add(tfChat);

		btnPanel.add(laQuiz);
		btnPanel.add(laQuizTitle);
		btnPanel.add(btnReady);
		btnPanel.add(btnSkip);
		btnPanel.add(btnExit);
		
		// ��ο�
		plMplId.add(drawLabel);
		plMplId.add(brush);
		
		
	}

	private void listener() {
		// Enter �Է½� ä�� �޼����� �������� �̺�Ʈ.
		tfChat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendChat();
			}
		});

		// �� �̺�Ʈ�� plId�� Ȱ��ȭ �Ǿ ���̵� �Է��� �� ����.
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btnStart = (JButton) e.getSource();
				plId.setVisible(true); // plId Ȱ��ȭ
				plSub.setVisible(true); // plId Ȱ��ȭ
				btnStart.setVisible(false); // btnStart ��Ȱ��ȭ
			}
		});

		// �� �̺�Ʈ�� plDrawRoom�� Ȱ��ȭ �Ǿ �׸���濡 ������.
		btnId.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// JButton btnId = (JButton)e.getSource();
				connectServer(); // ������ ����
				sendInsertId();
			}
		});

		// ������ ��ư �̺�Ʈ.
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendExit();
				System.exit(0);
			}
		});

		// �غ� ��ư �̺�Ʈ.
		btnReady.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendReady();
			}
		});

		// �ѱ�� ��ư �̺�Ʈ.
		btnSkip.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendSkip();
			}
		});
		// ���콺�� �������� �׸��� �̺�Ʈ
		
			drawLabel.addMouseMotionListener(new MouseMotionListener() {			
				@Override
				public void mouseMoved(MouseEvent e) {		
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					if (drawPPAP == true) {
						System.out.println("ppap true ���� ��");
					sendDraw = "DRAW&" + e.getX() + "," + e.getY();
					brush.xx = e.getX();
					brush.yy = e.getY();
					brush.repaint();
					brush.printAll(imgBuff.getGraphics());
					writer.println(sendDraw);
					}
					else {
						System.out.println("ppap false ���� ��");
					}
				}
			});
	
		// ������ �� �̺�Ʈ
		btnBlackDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Black";
				brush.setColor(Color.BLACK);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// ������ �� �̺�Ʈ
		btnRedDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Red";
				brush.setColor(Color.RED);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// �������� �� �̺�Ʈ
		btnOrangeDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Orange";
				brush.setColor(Color.ORANGE);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// ����� �� �̺�Ʈ
		btnYellowDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Yellow";
				brush.setColor(Color.YELLOW);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// �ʷϻ� �� �̺�Ʈ
		btnGreenDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Green";
				brush.setColor(Color.GREEN);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// �ϴû� �� �̺�Ʈ
		btnBlueDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Blue";
				brush.setColor(Color.CYAN);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// �Ķ��� �� �̺�Ʈ
		btnIndigoDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Indigo";
				brush.setColor(Color.BLUE);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// ��ũ�� �� �̺�Ʈ
		btnPurpleDrawPen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Purple";
				brush.setColor(Color.PINK);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// ���찳(���) �̺�Ʈ
		btnEraser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "White";
				brush.setColor(Color.WHITE);
				writer.println(sendColor);
				System.out.println("�� ���� : " + sendColor);
			}
		});
		// ��ο� ĵ���� �ʱ�ȭ �̺�Ʈ
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete ��ư ������");
				sendColor = "COLOR&" + "Delete";
				writer.println(sendColor);
				brush.setClearC(false);
				cleanDraw();
				System.out.println("��ο� ĵ���� �ʱ�ȭ");
			}
		});
	}

	// ���� �� ���� ���� �޼���.
	private void connectServer() {
		try {
			socket = new Socket("localhost", 3000);
			ReaderThread rt = new ReaderThread();
			rt.start();
		} catch (Exception e) {
			System.out.println(TAG + "���� ���� ����");
		}
	}

	// EXIT �������� �޼���.
	private void sendExit() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("EXIT&" + IDString);
		} catch (Exception e) {
			System.out.println(TAG + "Exit Msg writer fail...");
		}
	}

	// SKIP �������� �޼���.
	private void sendSkip() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("SKIP&");
		} catch (Exception e) {
			System.out.println(TAG + "Skip Msg writer fail...");
		}
	}

	// READY �������� �޼���.
	private void sendReady() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("READY&");
		} catch (Exception e) {
			System.out.println(TAG + "Ready Msg send fail...");
		}

	}

	// CHAT �������� �޼���.
	private void sendChat() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			String chatString = tfChat.getText();
			writer.println("CHAT&" + chatString);
			tfChat.setText("");
		} catch (Exception e) {
			System.out.println(TAG + "ä�� �޼��� ��û ����");
		}
	}

	// ID �������� �޼���
	private void sendInsertId() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			IDString = tfIdInput.getText();
			if ((IDString.equals(""))) { // NULL�� �Է½�
				IDString = "emptyID";
				writer.println("ID&" + IDString);
				plId.setVisible(false); // plId ��Ȱ��ȭ
				plSub.setVisible(false); // plId Ȱ��ȭ
				plDrawRoom.setVisible(true); // plDrawRoom Ȱ��ȭ
				setSize(1152, 864);
			} else { // ���̵� �� �Է½�.
				writer.println("ID&" + IDString);
				tfIdInput.setText("");
				plId.setVisible(false); // plId ��Ȱ��ȭ
				plSub.setVisible(false); // plId Ȱ��ȭ
				plDrawRoom.setVisible(true); // plDrawRoom Ȱ��ȭ
				setSize(1152, 864);
			}

		} catch (IOException e) {
			System.out.println(TAG + "�غ� �޼��� ��û ����");
		}
	}
	// ��ο� ĵ���� �ʱ�ȭ �޼���
	private void cleanDraw() {
		brush.setClearC(false);
		brush.repaint();
		brush.printAll(imgBuff.getGraphics());
	}

	// ������ ���� �޼����� �޾� TextArea�� �ѷ��ִ� Thread.
	class ReaderThread extends Thread {
		private BufferedReader reader;

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String readerMsg = null;
				String[] parsReaderMsg;
				while ((readerMsg = reader.readLine()) != null) {
					parsReaderMsg = readerMsg.split("&");
					if (parsReaderMsg[0].equals("DRAW")) {
						String[] drawM = parsReaderMsg[1].split(",");
						x = Integer.parseInt(drawM[0]);
						y = Integer.parseInt(drawM[1]);
						brush.setX(x);
						brush.setY(y);
						brush.repaint();
						brush.printAll(imgBuff.getGraphics());
						System.out.println("�귯�� �� : " + drawM);
						System.out.println("�귯�� �� : " + x);
						System.out.println("�귯�� �� : " + y);		
					}
					else if (parsReaderMsg[0].equals("COLOR")) {
						System.out.println("�� ���� ��û ����");
						if (parsReaderMsg[1].equals("Black")) {
							System.out.println("������ ��û");
							brush.setColor(Color.BLACK);
						}else if (parsReaderMsg[1].equals("Red")) {
							System.out.println("������ ��û");
							brush.setColor(Color.RED);
						}else if (parsReaderMsg[1].equals("Orange")) {
							System.out.println("��Ȳ�� ��û");
							brush.setColor(Color.ORANGE);
						}else if (parsReaderMsg[1].equals("Yellow")) {
							System.out.println("����� ��û");
							brush.setColor(Color.YELLOW);
						}else if (parsReaderMsg[1].equals("Green")) {
							System.out.println("�ʷϻ� ��û");
							brush.setColor(Color.GREEN);
						}else if (parsReaderMsg[1].equals("Blue")) {
							System.out.println("�Ķ��� ��û");
							brush.setColor(Color.CYAN);
						}else if (parsReaderMsg[1].equals("Indigo")) {
							System.out.println("�ε�� ������ �֠��� ������ ��û");
							brush.setColor(Color.BLUE);
						}else if (parsReaderMsg[1].equals("Purple")) {
							System.out.println("���ð��� ��ũ ��û");
							brush.setColor(Color.PINK);
						}else if (parsReaderMsg[1].equals("White")) {
							System.out.println("���찳 ��û");
							brush.setColor(Color.WHITE);
						}else if (parsReaderMsg[1].equals("Delete")) {
							System.out.println("ȭ�� ���� ��û");
							brush.setClearC(false);
							brush.repaint();
							brush.printAll(imgBuff.getGraphics());
						}
					}
					else if (parsReaderMsg[0].equals("SERVER")) {
						taChat.append("[SERVER]: " + parsReaderMsg[1] + "\n");
					} else if (parsReaderMsg[0].equals("CHAT") && parsReaderMsg.length > 1) {
						taChat.append(parsReaderMsg[1] + "\n");
					} else if (parsReaderMsg[0].equals("START")) {
						btnReady.setVisible(false);
						
					} else if (parsReaderMsg[0].equals("ID")) {
						taUserList.setText("");
					} else if (parsReaderMsg[0].equals("IDLIST")) {
						taUserList.append(parsReaderMsg[1] + "\n");
					} else if (parsReaderMsg[0].equals("TURN")) {
						laQuiz.setText(problem[selectProblem]);
						laQuiz.setVisible(true);
						btnSkip.setVisible(true);
						drawPPAP = true;
						tfChat.setEnabled(false);
						plBottom.setVisible(true);
						System.out.println("�� �� ��");
					} else if (parsReaderMsg[0].equals("NOTTURN")) {
						laQuiz.setVisible(false);
						btnSkip.setVisible(false);
						System.out.println("�� �� �ƴ�");
						brush.setDrawPen(false);
						drawPPAP = false;
						tfChat.setEnabled(true);
						plBottom.setVisible(false);
						System.out.println(drawPPAP);
					} else if (parsReaderMsg[0].equals("ANSWER")) {
						selectProblem++;
						if (selectProblem >= problem.length) {
							selectProblem = 0;
						}
					} else if (parsReaderMsg[0].equals("END")) {
						taChat.append("[SERVER]: " + parsReaderMsg[1] + "\n");
						btnReady.setVisible(true);
						tfChat.setEnabled(true);
						plBottom.setVisible(true);
						btnSkip.setVisible(false);
						btnReady.setVisible(true);
						laQuiz.setVisible(false);
						drawPPAP = true;
					}	 else {
						taChat.append("\n");
					}
					// ��ũ���� ������ ����.
					scrChat.getVerticalScrollBar().setValue(scrChat.getVerticalScrollBar().getMaximum());
				}
			}
			catch (IOException e) {
				System.out.println(TAG + "��� ����");
			}
		}
	}
	// �׸��� ���� ���� ����� �ִ� Ŭ����
	class Brush extends JLabel {
		public int xx, yy;
		public Color color = Color.BLACK;
		public boolean drawPen = true;
		public boolean clearC = true;
		
		@Override
		public void paint(Graphics g) {
			if (drawPen == true) {
				g.setColor(color);
				g.fillOval(xx - 10, yy - 10, 10, 10);
				System.out.println(drawPPAP);
			}else if (drawPen == false) {
				g.setColor(Color.WHITE);
				g.fillOval(0, 0, 0, 0);
				System.out.println(drawPPAP);
				System.out.println("�귯�� ��� �� �ϰ� ����");
			}
			if (clearC == true) {
				g.setColor(color);
				g.fillOval(xx - 10, yy - 10, 10, 10);
			}else if (clearC == false) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, 1000, 1000);
				clearC = true;
				System.out.println("ĵ���� Ŭ���� �����");
			}
			
		}
		
		public void setX(int x) {
			this.xx = x;
		}
		public void setY(int y) {
			this.yy = y;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public void setDrawPen(boolean drawPen) {
			this.drawPen = drawPen;
		}
		public void setClearC(boolean clearC) {
			this.clearC = clearC;
		}
	}
	

	public static void main(String[] args) {
		new MainStart();
	}
}