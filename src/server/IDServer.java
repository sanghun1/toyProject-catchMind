// ������
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class IDServer {
	private static final String TAG = "IDServer : "; // �±� ����.
	private Vector<ClientInfo> vcClient; // Ŭ���̾�Ʈ�� ������ ��� ����.
	private ServerSocket serverSocket; // ���� ����.
	private Socket socket; // Ŭ���̾�Ʈ�� �����ϸ� ���� ����� ����.

	// �����ڿ��� Ŭ���̾�Ʈ�� ������ ����Ѵ� (���� ������)
	public IDServer() {
		try {
			// ���� ������ ��Ʈ�� '3000'���� ����.
			serverSocket = new ServerSocket(3000);
			vcClient = new Vector<>();
			while (true) {
				System.out.println("Ŭ���̾�Ʈ ��û �����.....");
				socket = serverSocket.accept(); // ������ ������ �����.
				System.out.println("��û�� ������");
				// �߰� �����忡 Ŭ���̾�Ʈ ������ Ÿ������ ����.
				ClientInfo ci = new ClientInfo(socket);
				ci.start();
				vcClient.add(ci); // ���Ϳ� �߰�.

			}

		} catch (Exception e) {

			System.out.println(TAG + "����ȵ�");
		}
	} // END OF CONSTRUCTOR

	// Ŭ���̾�Ʈ�� ���� �޼����� ����ؼ� �ް� ������ ������
	class ClientInfo extends Thread {
		private Socket socket; // Ŭ���̾�Ʈ ������ �޾Ƽ� ����ϴ� ����.
		private PrintWriter writer; // ���� ����.
		private BufferedReader reader; // �б� ����.

		public boolean isClickReadybtn = false; // �غ� ��ư�� Ŭ���ϸ� �����ϴ� ����.
		public boolean isStart = false; // ������ �����ϸ� �����ϴ� ����.
		public boolean isEnd = false; // ���� ������ �����ϴ� ����.
		public boolean isRightAnswer = false; // ������ ���߸� �����ϴ� ����.
		public int checkReady = 0; // ���� �ο� �� ����� �غ� ��ư�� �������� ��� ����.
		public int userTurn = 0; // �����ϴ� Ŭ���̾�Ʈ ���� ���� �ο����ִ� ����.
		public int selectTurn = 1; // ���� ������ ���ϴ� ����.
		public String clientId; // Ŭ���̾�Ʈ ���̵� ��� ����.

		// ���þ ��Ƴ��� �迭.
		public String[] problem = { "���", "����", "����", "�ڹ�", "������Ʈ" };
		public int selectProblem = 0; // � ���þ ������ ���Դ��� ���ϴ� ����.
		public int score = 0; // ������ ���߸� �ö󰡴� ���ھ� ����.

		// �����ڿ��� Ŭ���̾�Ʈ ������ ���� Ŭ���� ���Ͽ� ��´�.
		public ClientInfo(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				String readerMsg = null;
				String[] parsReaderMsg;
				while ((readerMsg = reader.readLine()) != null) {
					parsReaderMsg = readerMsg.split("&");
					// Client Thread���� �����ϴ� ��������.
					protocolID(parsReaderMsg);
					protocolChat(parsReaderMsg);
					protocolSKIP(parsReaderMsg);
					protocolEXIT(parsReaderMsg);
					protocolREADY(parsReaderMsg);
					protocolSTART();
					protocolTURN();
					protocolDraw(parsReaderMsg);
					protocolColor(parsReaderMsg);
				}
			} catch (Exception e) {
				System.out.println(TAG + "�޼��� ��� ����.");
			}
		} // end of run

		// ���� ���� �� ����� ��� ���ִ� �޼���.
		// ��� Ŭ���̾�Ʈ���� ��� [���̵�] [SCORE]���� ���.
		private void protocolRESULT() {
			for (int i = 0; i < vcClient.size(); i++) {
				vcClient.get(i).writer.println("END&*****RESERT*****");
				for (int j = 0; j < vcClient.size(); j++) {
					vcClient.get(i).writer.println(
							"END&[" + vcClient.get(j).clientId + "] is SCORE => [" + vcClient.get(j).score + "]");
				}
				vcClient.get(i).writer.println("END&*****RESERT*****");
			}
		}

		// ���� ���� ��������.
		// ������ ����Ǹ� ��� ���� ������ְ� �������� �ʱⰪ���� ����.
		private void protocolEND(String[] parsReaderMsg) {
			if (isStart == true && isEnd == true) {
				protocolRESULT();
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer.println("END&GAME END");
					vcClient.get(i).isEnd = false;
					vcClient.get(i).isStart = false;
					vcClient.get(i).isClickReadybtn = false;
					vcClient.get(i).isRightAnswer = false;
					vcClient.get(i).checkReady = 0;
					vcClient.get(i).selectTurn = 1;
					vcClient.get(i).score = 0;
				}
			}
		}

		// �ѱ�� ��������.
		// �ѱ�⸦ ��ư�� ������ ���� ���� ������ ��������� ������ �ö��� ����.
		// ���þ ���� ���� �ٲٰ� ���� ���� Ŭ���̾�Ʈ���� �Ѱ���.
		private void protocolSKIP(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("SKIP")) {
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).isRightAnswer = true;
					vcClient.get(i).writer.println("SERVER&Turn Skip.");
					vcClient.get(i).writer.println("ANSWER&");
				}
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).selectProblem++;
				}
				if (selectProblem >= problem.length) {
					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).selectProblem = 0;
					}
				}
				// �ѱ�� ��ư�� �����µ� �������� ���� ��� ���� ����.
				if (selectTurn > vcClient.size()) {
					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).isEnd = true;
					}
					protocolEND(parsReaderMsg);
				}
			}
		}

		// ������ ��ư ��������.
		// ������ ��ư ������ �����޼��� ���, �ش� ���� ����.
		private void protocolEXIT(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("EXIT")) {
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer.println("SERVER&[" + clientId + "] is Exit the room.");
				}
				for (int i = 0; i < vcClient.size(); i++) {
					if (vcClient.get(i).clientId.equals(parsReaderMsg[1])) {
						vcClient.remove(i);
					}
				}
			}
		}

		// �غ� ��ư ��������.
		// ��ư�� ������ �غ� Boolean�� ���, ���� �޼��� ���.
		// ���ӽ����� ���� checkReady��ư�� ����.
		private void protocolREADY(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("READY")) {
				isClickReadybtn = !(isClickReadybtn);
				if (isClickReadybtn == true) {
					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).writer.println("SERVER&[" + clientId + "] is Set Ready.");
						vcClient.get(i).checkReady++;
					}
					System.out.println("checkReady: " + checkReady);
				} else {

					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).writer.println("SERVER&[" + clientId + "] please wait a moment.");
						vcClient.get(i).checkReady--;
					}
					System.out.println("checkReady: " + checkReady);
				}
			}
		}

		// ���� ���� ��������.
		// ���� ������ �غ� ��ư�� ������ �����ϴ� checkReady ���� ������ ������ ���� ���ƾ���.
		// ���� �޼��� ��� �� ���� Ȯ�� boolean���� true�����༭ ���� ������.
		private void protocolSTART() {
			if (checkReady == vcClient.size() && isStart == false) {
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer.println("START&");
					vcClient.get(i).writer.println("SERVER&GAME START.");
					vcClient.get(i).isStart = true;
					vcClient.get(i).isRightAnswer = true;
					System.out.println("vcClient.get(" + i + ").isStart: " + vcClient.get(i).isStart);
				}
			}
			System.out.println("isStart: " + isStart);
			System.out.println("isRightAnswer: " + isRightAnswer);
		}

		// �� ��������.
		// Ŭ���̾�Ʈ�� ���� �� �ο��Ǵ� �ϰ��� selectTurn ���� ���ؼ� ���� ���Ѵ�.
		// ȭ�� ������ ���� ��� Ŭ���̾�Ʈ ȭ���� �ʱ�ȭ ��(���þ�, �׸���) NOTTURN
		// �Ѹ��Ը� �� ���������� ����.
		private void protocolTURN() {
			if (isStart == true && isRightAnswer == true) {
				System.out.println("before selectTurn:" + selectTurn);
				for (int i = 0; i < vcClient.size(); i++) {
					if (vcClient.get(i).userTurn == selectTurn) {
						for (int j = 0; j < vcClient.size(); j++) {
							vcClient.get(j).writer.println("NOTTURN&");
						}
						vcClient.get(i).writer.println("TURN&");
						for (int j = 0; j < vcClient.size(); j++) {
							vcClient.get(j).writer.println("SERVER&[" + vcClient.get(i).clientId + "] is Turn.");
						}
					}
				} // end of for
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).selectTurn++;
					vcClient.get(i).isRightAnswer = false;
				}
				System.out.println("after selectTurn:" + selectTurn);
			}
		}

		// ID ��������.
		// �Է� ���� ���̵� ���� �� ���� �����޼��� ���.
		// �׸��� ���������� �� ���� ���ھ� ���� �ο�.
		// �� �� ������ ���� ����� ���� IDLIST �������ݷ�
		// ��� Ŭ���̾�Ʈ ���� ���̵� ���� ����.
		private void protocolID(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("ID")) {
				clientId = parsReaderMsg[1];
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer.println("SERVER&[" + clientId + "] is enter the room.");
					vcClient.get(i).writer.println("ID&");
					vcClient.get(i).userTurn = i + 1;
					vcClient.get(i).score = 0;
					System.out.println("vcClient.get(" + i + ").clientId: " + vcClient.get(i).clientId);
					System.out.println("vcClient.get(" + i + ").userTurn: " + vcClient.get(i).userTurn);
					System.out.println("userTurn: " + userTurn);
				} // end of for
				for (int i = 0; i < vcClient.size(); i++) {
					for (int j = 0; j < vcClient.size(); j++) {
						vcClient.get(i).writer.println("IDLIST&[" + vcClient.get(j).clientId + "]");
					}
				}
			}
		}

		// ���� �������� �޼���.
		// �Էµ� ���� ���þ�� ���ٸ� �����޼��� ��� �� ���ھ� ����.
		// ������ ���߰� ���� ���� ���ٸ� ���� ����.
		private void protocolANSWER(String[] parsReaderMsg) {
			if (isStart == true && parsReaderMsg[1].equals(problem[selectProblem])) {
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer
							.println("SERVER&[" + clientId + "] , [" + problem[selectProblem] + "] is Right Answer");
					vcClient.get(i).writer.println("ANSWER&");
					vcClient.get(i).isRightAnswer = true;
					if (vcClient.get(i).clientId.equals(clientId)) {
						vcClient.get(i).score++;
						System.out.println("vcClient.get( " + i + " ).score: " + vcClient.get(i).score);
					}
				}
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).selectProblem++;
				}
				if (selectProblem >= problem.length) {
					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).selectProblem = 0;
					}
					System.out.println("selectProblem: " + selectProblem);
				}
				if (selectTurn > vcClient.size()) {
					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).isEnd = true;
					}
					protocolEND(parsReaderMsg);
				}
			}
		}

		// CHAT ��������.
		// TextField �ԷµǴ� ������ ��� ä������ ����.
		// ä�� �� �� ������ �Է��ϸ� ���� �������� �޼��带 ȣ��.
		private void protocolChat(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("CHAT") && parsReaderMsg.length > 1) {
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer.println("CHAT&[" + clientId + "]: " + parsReaderMsg[1]);
				}
				protocolANSWER(parsReaderMsg);
			} else if (parsReaderMsg[0].equals("CHAT")) {
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).writer.println("CHAT&[" + clientId + "] ");
				}
				for (int i = 0; i < vcClient.size(); i++) {
					vcClient.get(i).isRightAnswer = true;
				}
			}
		}
		
		private void protocolDraw(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("DRAW")) {
				for(int i = 0; i < vcClient.size(); i++) {
					if (vcClient.get(i) != this) {
						vcClient.get(i).writer.println("DRAW&"+ parsReaderMsg[1]);
						}
					}
				}
			}
		
		private void protocolColor(String[] parsReaderMsg) {
			if (parsReaderMsg[0].equals("COLOR")) {
				System.out.println("���� Į���û �޽��� ����");
				for(int i = 0; i < vcClient.size(); i++) {
					if (vcClient.get(i) != this) {
						vcClient.get(i).writer.println("COLOR&"+ parsReaderMsg[1]);
						System.out.println("Į�󺯰� �޽��� ����");
						}
					}
				}
			}
		
		
	} // end of class ClientInfo

	public static void main(String[] args) {
		new IDServer();
	}

}
