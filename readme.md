<!-- ===================== HEADER ===================== -->
<div align="center">
  <img 
    src="https://capsule-render.vercel.app/api?type=waving&color=gradient&text=606%20ChatApp&height=140&section=header"
    alt="606 ChatApp Header"
    width="100%"
  />
</div>

<hr />

<!-- ===================== TITLE ===================== -->
<h1 align="center"> 💬 606 ChatApp – Java Client-Server Chat Application </h1>

<p align="center">
A desktop-based <strong>real-time chat application</strong> built using <strong>Java</strong> and <strong>Swing</strong>,<br />
based on a robust <strong>Client–Server Architecture</strong>.
</p>

<hr />

<!-- ===================== OVERVIEW ===================== -->
<h2 align="center"> 📌 Overview </h2>

<p align="center">
<strong>606 ChatApp</strong> enables multiple users to communicate in real time through a central server.
The application is divided into two independent programs: a <strong>Server</strong> and a <strong>Client</strong>,
each with its own graphical user interface.
</p>

<pre><strong>
architecture: Client–Server
ui_framework: Java Swing
communication: TCP Sockets
key_concepts:
  - Multithreading
  - Network Programming
  - Event-Driven GUI
  - Object-Oriented Design
</strong></pre>

<hr />

<!-- ===================== SYSTEM ARCHITECTURE ===================== -->
<h2 align="center"> 🏗️ System Architecture </h2>

<p align="center">
The system follows a centralized client–server communication model.
</p>

<pre><strong>
Client A ──┐
Client B ──┼──> Server ──> Broadcast ──> All Clients
Client C ──┘
</strong></pre>

<ul>
  <li><strong>Server:</strong> Accepts connections, manages clients, and broadcasts messages</li>
  <li><strong>Client:</strong> Sends messages and receives real-time updates</li>
</ul>

<hr />

<!-- ===================== CORE COMPONENTS ===================== -->
<h2 align="center"> 🧩 Core Components </h2>

<h3>🔹 Server Side</h3>

<pre><strong>
ChatServer.java
  - Uses ServerSocket (Port: 5000 / 5001)
  - Handles multiple clients concurrently
  - Spawns a ClientHandler thread per client
  - Broadcasts messages to all connected clients

ChatServerGUI.java
  - Administrative GUI
  - Displays server logs with timestamps
  - Shows connected clients list
  - Start / Stop server controls
</strong></pre>

<h3>🔹 Client Side</h3>

<pre><strong>
LoginFrame.java
  - User login screen
  - Username validation
  - Animated logo & custom UI elements

ChatClient.java
  - Manages socket connection
  - Dedicated listener thread for incoming messages
  - Handles protocol commands (JOIN, EXIT)

ChatClientGUI.java
  - Main chat interface
  - Chat bubbles with left/right alignment
  - Auto-scroll and keyboard input support
</strong></pre>

<hr />

<!-- ===================== COMMUNICATION PROTOCOL ===================== -->
<h2 align="center"> 🔄 Communication Protocol </h2>

<p align="center">
The application uses a simple text-based protocol:
</p>

<pre><strong>
SUBMIT_NAME
[Username]
[Username]: [Message]
EXIT
SERVER_STOPPED
SERVER_DISCONNECTED
[Username] has joined the chat.
[Username] has left the chat.
</strong></pre>

<hr />

<!-- ===================== FEATURES ===================== -->
<h2 align="center"> ✨ Features </h2>

<ul>
  <li>💬 Real-time multi-user chat</li>
  <li>🧵 Multi-threaded server (one thread per client)</li>
  <li>🎨 Modern dark-themed Swing UI with gradients</li>
  <li>🟣 User messages aligned right with distinct color</li>
  <li>🟢 Other users’ messages aligned left</li>
  <li>📌 System messages centered & italicized</li>
  <li>⏱️ Timestamped messages</li>
  <li>⌨️ Send messages using Enter key</li>
  <li>🚫 Prevents sending empty messages</li>
  <li>📜 Auto-scroll on new messages</li>
</ul>

<hr />

<!-- ===================== PROJECT STRUCTURE ===================== -->
<h2 align="center"> 📂 Project Structure </h2>

<pre><strong>
606-chatapp/
│
├── bin/
│   ├── *.class
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
</strong></pre>

<hr />

<!-- ===================== BUILD ===================== -->
<h2 align="center"> 🛠️ Build & Compile </h2>

<pre><strong>
javac -d bin src/*.java
</strong></pre>

<p align="center">
Verify directories:
</p>

<pre><strong>
dir src
dir bin
</strong></pre>

<hr />

<!-- ===================== RUN ===================== -->
<h2 align="center"> ▶️ How To Run </h2>

<h3>🔹 Start Server</h3>

<pre><strong>
java -cp bin ChatServerGUI
</strong></pre>

<p align="center">
Check listening port:
</p>

<pre><strong>
netstat -an | findstr 5000
</strong></pre>

<h3>🔹 Start Client</h3>

<pre><strong>
java -cp bin LoginFrame
</strong></pre>

<p align="center">
You can also use the provided <code>.jar</code> files or <code>.bat</code> scripts.
</p>

<hr />

<!-- ===================== DESIGN ===================== -->
<h2 align="center"> 🎯 Design Philosophy </h2>

<ul>
  <li>✔️ Clear separation between logic and UI</li>
  <li>✔️ Thread-safe client handling</li>
  <li>✔️ Responsive and modern desktop UI</li>
  <li>✔️ Scalable for multiple concurrent users</li>
</ul>

<hr />

<!-- ===================== CONCLUSION ===================== -->
<h2 align="center"> 🧠 Conclusion </h2>

<p align="center">
606 ChatApp demonstrates how networking, multithreading, and GUI design
can be combined to build a robust real-time communication system.
</p>

<p align="center">
It serves as a strong educational example of Java-based
client–server applications.
</p>

<hr />

<!-- ===================== AUTHOR ===================== -->
<h2 align="center"> 👤 Author </h2>

<p align="center">
<strong>x606</strong><br />
Java Developer & Security Enthusiast
</p>

<hr />

<!-- ===================== FOOTER ===================== -->
<div align="center">
  <img 
    src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=100&section=footer"
    width="100%"
  />
</div>
