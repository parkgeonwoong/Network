# Network

+ Study_network

서버 프로그램을 작성하는 경우에 프로그래머는 ServerSocket을 생성해서 accept() 메소드
를 호출한다. accept() 메소드는 클라이언트로부터 컨넥션이 요청될 때까지 서버를 블록시
키고 있다가, 클라이언트로부터 요청이 들어오면 클라이언트와 통신할 수 있는 Socket 클래
스를 리턴한다. ServerSocket 클래스는 다음과 같은 메소드들을 갖고 있다.

▪ Socket accept() - 클라이언트로부터 연결이 이루어질 때까지 기다린다. 연결이 이루어지
면 클라이언트와 통신할 수 있는 Socket을 리턴한다.

▪ void bind(SocketAddress endpoint) - ServerSocket을 특정 주소(IP와 포트 번호)로 바
인딩시킨다.

▪ void close() - 소켓을 닫는다.

▪ InetAddress getInetAddress() - 서버 소켓의 주소를 리턴한다.

▪ int getSoTimeout() - SO_TIMEOUT 값을 알아본다. 0값을 리턴하는 경우에는 소켓 타임
아웃 시간이 무한대라는 것을 의미한다.

▪ boolean isClosed() - 서버 소켓이 닫혀져 있는지 여부를 리턴한다.

▪ void setSoTimeout(int timeout) - 소켓 타임아웃(단위: 밀리초)을 설정한다. 타임아웃 시
간을 설정하는 경우에 서버 소켓을 accept() 메소드에서 타입 아웃으로 지정된 시간까지만
클라이언트의 연결을 기다란다. 만약 시간이 되도록 클라이언트의 연결이 없는 경우에는
SocketTimeoutException 예외가 발생한다. timeout 값이 0인 경우에는 무한대를 의미한다.
클라이언트 프로그램을 작성하는 경우에는 Socket 클래스를 생성해서 서버 프로그램에 접
속한다. Socket 클래스는 다음과 같은 생성자를 이용해서 만들 수 있다.

▪ Socket(String host, int port) - host로 주어진 서버에 port 포트 번호를 이용해서 접속한
다.

▪ Socket(InetAddress address, int port) - address의 주소에 있는 서버에 port 포트 번호
를 이용해서 접속한다.

Socket 클래스의 생성자에서 host 매개 변수는 컴퓨터의 이름을 기술하는 문자열이고, port
는 포트 번호를 의미한다. address 매개 변수는 IP 어드레스를 나타낸다. 자바에서는 IP 어
드레스를 위해 InterAddress 클래스를 제공한다. Socket 클래스는 다음과 같은 메소드들을
제공한다.

▪ void bind(SocketAddress bindpoint) - 소켓을 주어진 주소로 바인딩시킨다.

▪ void close() - 소켓을 닫는다.

▪ void connect(SocketAddress endpoint) - 주어진 주소에 있는 서버에 연결한다.

▪ void connect(SocketAddress endpoint, int timeout) - 주어진 주소에 있는 서버에 연결
한다. timeout 시간 안에 연결이 이루어지지 않으면 SocketTimeoutException 예외가 발생
한다.

▪ InetAddress getInetAddress() - 소켓이 연결된 곳의 주소를 리턴한다.

▪ InputStream getInputStream() - 소켓의 입력 스트림을 리턴한다.

▪ boolean getKeepAlive() - SO_KEEPALIVE가 활성화되었는지 알아본다.

▪ OutputStream getOutputStream() - 소켓의 출력 스트림을 리턴한다.

▪ int getPort() - 소켓에 연결된 서버의 포트 번호를 리턴한다.

▪ int getSoTimeout() - SO_TIMEOUT 값을 리턴한다.

▪ boolean isConnected() - 소켓이 연결되었는지 여부를 리턴한다.

▪ void setSoTimeout(int timeout) - SO_TIMEOUT 값(단위: 밀리초)을 설정한다.

▪ void shutdownInput() - 소켓의 입력 스트림을 닫는다.

▪ void shutdownOutput() - 소켓의 출력 스트림을 닫는다.

Socket 클래스를 만든 후에는 클라이언트와 서버 사이에 데이터를 주고받을 수 있는 I/O
스트림을 만들어야 한다. 소켓으로부터 데이터를 받아들이기 위해서는 InputStream이 필요
하고, 데이터를 전송하기 위해서는 OutputStream이 필요하다. 소켓에서 InputStream을 얻
기 위해서는 getInputStream() 메소드를 이용하고, OutputStream을 얻기 위해서는
getOutputStream() 메소드를 이용한다.
