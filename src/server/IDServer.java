// 최종본
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class IDServer {
	private static final String TAG = "IDServer : "; // 태그 생성.
	private Vector<ClientInfo> vcClient; // 클라이언트의 정보를 담는 벡터.
	private ServerSocket serverSocket; // 서버 소켓.
	private Socket socket; // 클라이언트가 접속하면 새로 만드는 소켓.

	// 생성자에서 클라이언트의 접속을 대기한다 (메인 스레드)
	public IDServer() {
		try {
			// 서버 소켓의 포트를 '3000'으로 설정.
			serverSocket = new ServerSocket(3000);
			vcClient = new Vector<>();
			while (true) {
				System.out.println("클라이언트 요청 대기중.....");
				socket = serverSocket.accept(); // 서버의 접속을 대기중.
				System.out.println("요청이 성공함");
				// 추가 스레드에 클라이언트 소켓을 타켓으로 설정.
				ClientInfo ci = new ClientInfo(socket);
				ci.start();
				vcClient.add(ci); // 백터에 추가.

			}

		} catch (Exception e) {

			System.out.println(TAG + "연결안됨");
		}
	} // END OF CONSTRUCTOR

	// 클라이언트로 부터 메세지를 계속해서 받고 보내는 스레드
	class ClientInfo extends Thread {
		private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수.
		private PrintWriter writer; // 쓰기 버퍼.
		private BufferedReader reader; // 읽기 버퍼.

		public boolean isClickReadybtn = false; // 준비 버튼을 클릭하면 동작하는 변수.
		public boolean isStart = false; // 게임이 시작하면 동작하는 변수.
		public boolean isEnd = false; // 게임 끝나면 동작하는 변수.
		public boolean isRightAnswer = false; // 정답을 맞추면 동작하는 변수.
		public int checkReady = 0; // 입장 인원 중 몇명이 준비 버튼을 눌렀는지 담는 변수.
		public int userTurn = 0; // 접속하는 클라이언트 한테 턴을 부여해주는 변수.
		public int selectTurn = 1; // 누가 턴인지 정하는 변수.
		public String clientId; // 클라이언트 아이디를 담는 변수.

		// 제시어를 담아놓은 배열.
		public String[] problem = { "상어", "과제", "시험", "자바", "프로젝트" };
		public int selectProblem = 0; // 어떤 제시어가 문제로 나왔는지 정하는 변수.
		public int score = 0; // 정답을 맞추면 올라가는 스코어 변수.

		// 생성자에서 클라이언트 소켓을 내부 클래스 소켓에 담는다.
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
					// Client Thread에서 동작하는 프로토콜.
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
				System.out.println(TAG + "메세지 통신 실패.");
			}
		} // end of run

		// 게임 종료 후 결과를 출력 해주는 메서드.
		// 모든 클라이언트에게 모든 [아이디] [SCORE]값을 출력.
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

		// 게임 종료 프로토콜.
		// 게임이 종료되면 결과 값을 출력해주고 변수들을 초기값으로 만듬.
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

		// 넘기기 프로토콜.
		// 넘기기를 버튼을 누르면 답을 맞춘 로직이 실행되지만 점수는 올라가지 않음.
		// 제시어를 다음 으로 바꾸고 턴을 다음 클라이언트에게 넘겨줌.
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
				// 넘기기 버튼을 눌렀는데 다음턴이 없을 경우 게임 종료.
				if (selectTurn > vcClient.size()) {
					for (int i = 0; i < vcClient.size(); i++) {
						vcClient.get(i).isEnd = true;
					}
					protocolEND(parsReaderMsg);
				}
			}
		}

		// 나가기 버튼 프로토콜.
		// 나가기 버튼 누르면 서버메세지 출력, 해당 벡터 제거.
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

		// 준비 버튼 프로토콜.
		// 버튼을 누르면 준비 Boolean값 토글, 서버 메세지 출력.
		// 게임시작을 위한 checkReady버튼을 증가.
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

		// 게임 시작 프로토콜.
		// 게임 시작은 준비 버튼을 누르면 증가하는 checkReady 값과 벡터의 사이즈 값과 같아야함.
		// 서버 메세지 출력 후 정답 확인 boolean값을 true로해줘서 턴을 시작함.
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

		// 턴 프로토콜.
		// 클라이언트가 접속 시 부여되는 턴값을 selectTurn 값과 비교해서 턴을 정한다.
		// 화면 구성을 위해 모든 클라이언트 화면을 초기화 후(제시어, 그림판) NOTTURN
		// 한명에게만 턴 프로토콜을 보냄.
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

		// ID 프로토콜.
		// 입력 받은 아이디를 저장 후 입장 서버메세지 출력.
		// 그리고 순차적으로 턴 값과 스코어 값을 부여.
		// 이 후 입장한 유저 목록을 위해 IDLIST 프로토콜로
		// 모든 클라이언트 한테 아이디 값을 전송.
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

		// 정답 프로토콜 메서드.
		// 입력된 값이 제시어와 같다면 서버메세지 출력 후 스코어 증가.
		// 정답을 맞추고 다음 턴이 없다면 게임 종료.
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

		// CHAT 프로토콜.
		// TextField 입력되는 값들은 모두 채팅으로 받음.
		// 채팅 값 중 정답을 입력하면 정답 프로토콜 메서드를 호출.
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
				System.out.println("서버 칼라요청 메시지 들어옴");
				for(int i = 0; i < vcClient.size(); i++) {
					if (vcClient.get(i) != this) {
						vcClient.get(i).writer.println("COLOR&"+ parsReaderMsg[1]);
						System.out.println("칼라변경 메시지 보냄");
						}
					}
				}
			}
		
		
	} // end of class ClientInfo

	public static void main(String[] args) {
		new IDServer();
	}

}
