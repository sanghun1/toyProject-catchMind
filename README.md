# toyProject-catchMind
----------------------------------------

### 게임 목적

----------------------------------------

- 소켓 통신을 통해 서로 채팅을 하고 그림을 그리고 맞추는 게임을 자바 스윙으로 구현.

 

### 게임 실행 방법

----------------------------------------

1. IDServer 실행

    - 클라이언트의 접속을 대기하는 서버를 실행.

   

2. MainStart 실행

    - 서버에 입장하는 클라이언트 실행.

    - 다수로 접속 가능.

  

 

### 게임 설명 

----------------------------------------

- 입장한 인원이 모두 준비 버튼을 누르면 게임이 자동으로 시작된다.

- 한 사람이 그림을 그리고 나머지 사람이 어떤 그림을 그리는지 채팅창에 입력하면 된다.

- 그림을 맞추면 스코어 점수가 올라간다.

- 그림을 그리는 순서는 방에 들어온 순서대로 진행된다.

- 넘기기 버튼으로 자신의 차례를 넘길 수 있다.

- 그리기 기회가 한 번씩 주어지고 끝이나면 게임이 종료된다.

- 게임이 종료되면 채팅창에 스코어 점수를 집계하여 보여준다.

- 다시 준비 버튼을 모든 인원이 누르면 게임이 시작된다.






### 핵심 기능 설명 

----------------------------------------

- 클라이언트를 실행하고 게임시작을 누르면 아이디 입력란이 나온다. 아이디 입력란에 아이디를 입력하고 게임시작을 누르면 방으로 입장한다.

![초기화면](https://user-images.githubusercontent.com/39526249/102368772-9345cb80-3ffe-11eb-80c1-c9ee10d18a44.gif)

- 게임시작전에는 방에 있는 참여자 모두가 그림을 그릴 수 있다.

![준비 전 그림 공유](https://user-images.githubusercontent.com/39526249/102371081-2849c400-4001-11eb-8f28-e15595d8e375.gif)

- 참여자가 모두 준비버튼을 누르면 게임이 시작된다.

![준비완료](https://user-images.githubusercontent.com/39526249/102375295-b2942700-4005-11eb-9217-7aa296762d7e.gif)

- 게임이 시작되면 그림은 한명이서 그릴 수 있고 순서는 방에 입장한 순서대로 턴이 돌아간다. 

![그림권한](https://user-images.githubusercontent.com/39526249/102375706-28988e00-4006-11eb-804a-4e00954f3b70.gif)

- 나머지는 채팅으로 정답을 맞춘다. 정답을 맞추거나, 출제자가 넘기기 버튼을 넘기면 턴이 바뀐다.

![상어정답](https://user-images.githubusercontent.com/39526249/102375801-4534c600-4006-11eb-8433-63f8f7b3804c.gif)

 
 
### 통신프로토콜

----------------------------------------

 ``` java
package protocol;

public interface protocol {

	String READY = "READY";
	String SERVER = "SERVER";
	String CHAT = "CHAT";
	String ID = "ID";
	String IDLIST = "IDLIST";
	String START = "START";
	String END = "END";
	String TURN = "TURN";
	String NOTTURN = "NOTTURN";
	String SKIP = "SKIP";
	String EXIT = "EXIT";
	String ANSWER = "ANSWER";
	String DRAW = "DRAW";
	String COLOR = "COLOR";
 
 }
 ```
 
### 게임 시연 장면
----------------------------------------

[![초기화면](https://user-images.githubusercontent.com/39526249/102422691-ac746980-404a-11eb-84c7-3e2a5794eb88.png)](https://www.youtube.com/watch?v=OVq4EvkwDhk)
 
 이미지클릭
