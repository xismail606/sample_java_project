C:\---\-----g-\-------\606 chat\
├── bin/
│   ├── ChatClient.class
│   ├── ChatClientGUI.class
│   ├── ChatClientGUI$1.class
│   ├── ChatClientGUI$2.class
│   ├── ChatClientGUI$3.class
│   ├── ChatClientGUI$4.class
│   ├── ChatClientGUI$5.class
│   ├── ChatServer.class
│   ├── ChatServer$ClientHandler.class
│   ├── ChatServerGUI.class
│   ├── ChatServerGUI$1.class
│   ├── ChatServerGUI$2.class
│   ├── ChatServerGUI$3.class
│   ├── ChatServerGUI$4.class
│   ├── LoginFrame.class
│   ├── LoginFrame$1.class
│   ├── LoginFrame$2.class
│   ├── LoginFrame$3.class
│   ├── LoginFrame$4.class
│   ├── LoginFrame$5.class
│   ├── LoginFrame$6.class
│   ├── LoginFrame$7.class
│   ├── LoginFrame$8.class
│   ├── LoginFrame$9.class
│   └── LoginFrame$RoundedBorder.class
│
├── manifest/
│   ├── client.mf
│   ├── server.mf
│   ├── start-client.bat
│   └── start-server.bat
│
├── src/
│   ├── ChatClient.java
│   ├── ChatClientGUI.java
│   ├── ChatServer.java
│   ├── ChatServerGUI.java
│   └── LoginFrame.java
│
├── ChatClient.jar
├── ChatServer.jar
└── readme.txt
---------
javac -d bin src/*.java
----------
dir src
---------
dir bin
-------------
java -cp bin ChatServerGUI
---------------
netstat -an | findstr 5000
---------------
java -cp bin LoginFrame