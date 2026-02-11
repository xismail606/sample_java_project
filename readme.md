<!-- ===================== HEADER ===================== -->
<div align="center">
  <img 
    src="https://capsule-render.vercel.app/api?type=waving&color=gradient&text=606%20ChatApp&height=140&section=header"
    alt="606 ChatApp Header"
    width="100%"
  />
</div>

---

<!-- ===================== TITLE ===================== -->
<h1 align="center"> 💬 606 ChatApp – Java Client-Server Chat Application </h1>

<p align="center">
A desktop-based <strong>real-time chat application</strong> built using <strong>Java</strong> and <strong>Swing</strong>,<br>
based on a robust <strong>Client–Server Architecture</strong>.
</p>

---

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
  - Thread Safety
</strong></pre>

---

<!-- ===================== ARCHITECTURE ===================== -->
<h2 align="center"> 🏗️ System Architecture </h2>

<p align="center">
The system consists of two main components communicating over TCP.
</p>

<pre><strong>
Client A ──┐
Client B ──┼──> Server ──> Broadcast ──> All Clients
Client C ──┘
</strong></pre>

<ul>
  <li><strong>Server:</strong> Manages connections, receives messages, and broadcasts them</li>
  <li><strong>Client:</strong> Sends user messages and receives updates from the server</li>
</ul>

---

<!-- ===================== CORE COMPONENTS ===================== -->
<h2 align="center"> 🧩 Core Components </h2>

<h3>🔹 Server Side</h3>

<pre><strong>
ChatServer.java
  - Uses ServerSocket (Port: 5001)
  - Accepts multiple client connections
  - Spawns a ClientHandler thread per client
  - Broadcasts messages to all connected users
  - Thread-safe with ConcurrentHashMap
  - Automatic duplicate username prevention

ChatServerGUI.java
  - Administrative interface
  - Displays server logs with timestamps
  - Shows connected clients list with status indicators
  - Start / Stop server controls
  - Real-time user count display
</strong></pre>

<h3>🔹 Client Side</h3>

<pre><strong>
LoginFrame.java
  - User authentication screen
  - Username validation (2-15 characters)
  - Animated logo & custom UI components
  - Real-time input validation feedback
  - Connection testing before login

ChatClient.java
  - Manages socket connection
  - Runs a listener thread for incoming messages
  - Handles protocol messages (JOIN, EXIT, NAME_CHANGED)
  - Automatic reconnection handling

ChatClientGUI.java
  - Main chat interface
  - Chat bubbles (left/right alignment)
  - Auto-scroll & keyboard input support
  - Emoji support in system messages
  - Timestamp display for all messages
</strong></pre>

---

<!-- ===================== COMMUNICATION ===================== -->
<h2 align="center"> 🔄 Communication Protocol </h2>

<p align="center">
The application uses a simple text-based protocol.
</p>

<pre><strong>
SUBMIT_NAME
[Username]
NAME_CHANGED:[NewUsername]
[Username]: [Message]
EXIT
SERVER_STOPPED
SERVER_DISCONNECTED
[Username] has joined the chat.
[Username] has left the chat.
</strong></pre>

---

<!-- ===================== FEATURES ===================== -->
<h2 align="center"> ✨ Features </h2>

<ul>
  <li>💬 Real-time multi-user chat</li>
  <li>🧵 Multi-threaded server (one thread per client)</li>
  <li>🚫 Automatic duplicate username prevention</li>
  <li>📏 Username validation (2-15 characters, alphanumeric + underscore)</li>
  <li>🔒 Thread-safe with ConcurrentHashMap</li>
  <li>🎨 Modern dark-themed GUI with gradients</li>
  <li>🟣 User messages aligned right (distinct purple color)</li>
  <li>🟢 Other users' messages aligned left (green color)</li>
  <li>📌 System messages with emoji indicators (🔄✅❌📢⚠️👥)</li>
  <li>⏱️ Timestamped messages (HH:mm:ss dd/MM/yyyy)</li>
  <li>⌨️ Send messages using Enter key</li>
  <li>🚫 Send button disabled for empty input</li>
  <li>📜 Auto-scroll on new messages</li>
  <li>🖱️ Custom gradient buttons with hover effects</li>
  <li>🎯 Graceful connection handling and error recovery</li>
</ul>

---

<!-- ===================== BUG FIXES ===================== -->
<h2 align="center"> 🐛 Bug Fixes & Improvements </h2>

<ul>
  <li>✅ Fixed broadcast issue - users now see their own messages</li>
  <li>✅ Fixed corrupted emoji rendering (👥 instead of ðŸ'¥)</li>
  <li>✅ Fixed LogArea display issues in server GUI</li>
  <li>✅ Added automatic duplicate username handling with counter (User → User1 → User2)</li>
  <li>✅ Implemented username length validation (2-15 chars)</li>
  <li>✅ Improved thread safety with ConcurrentHashMap</li>
  <li>✅ Enhanced system messages with proper emoji indicators</li>
  <li>✅ Better color scheme for system messages (gold instead of red)</li>
  <li>✅ Fixed client not seeing own messages in chat</li>
  <li>✅ Improved connection timeout handling</li>
</ul>

---

<!-- ===================== PROJECT STRUCTURE ===================== -->
<h2 align="center"> 📂 Project Structure </h2>

```
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
│   ├── LoginFrame.java
│
├── ChatClient.jar
└── ChatServer.jar
└── README.md
```

---

<!-- ===================== REQUIREMENTS ===================== -->
<h2 align="center"> ⚙️ Requirements </h2>

<ul>
  <li>☕ <strong>Java JDK 8</strong> or higher</li>
  <li>🖥️ <strong>Windows, macOS, or Linux</strong></li>
  <li>📡 <strong>Network connection</strong> (localhost or LAN)</li>
  <li>🔌 <strong>Port 5001</strong> available (not blocked by firewall)</li>
</ul>

---

<!-- ===================== USAGE ===================== -->
<h2 align="center"> 📖 Usage Guide </h2>

<h3>🔹 First Time Setup</h3>

<ol>
  <li>Extract all files to a folder</li>
  <li>Double-click ChatServer.jar to start the server</li>
  <li>Double-click ChatClient.jar to start one or more clients</code></li>
</ol>

<h3>🔹 Daily Usage</h3>

<ol>
  <li>Double-click ChatServer.jar first</li>
  <li>Double-click ChatClient.jar</li>
  <li>Enter username (2-15 characters, alphanumeric + underscore)</li>
  <li>Start chatting!</li>
</ol>

<h3>🔹 Username Rules</h3>

<ul>
  <li>✅ Minimum 2 characters</li>
  <li>✅ Maximum 15 characters</li>
  <li>✅ Letters, numbers, and underscore only</li>
  <li>❌ No spaces or special characters</li>
  <li>🔄 Duplicate names get automatic counter (Ahmed → Ahmed1)</li>
</ul>

---

<!-- ===================== DESIGN ===================== -->
<h2 align="center"> 🎯 Design Philosophy </h2>

<ul>
  <li>✔️ Clear separation between logic and UI</li>
  <li>✔️ Thread-safe communication with ConcurrentHashMap</li>
  <li>✔️ Responsive and modern desktop UI</li>
  <li>✔️ Scalable for multiple users</li>
  <li>✔️ Graceful error handling and recovery</li>
  <li>✔️ Clean code architecture</li>
  <li>✔️ Professional project structure</li>
</ul>

---

<!-- ===================== TECHNICAL DETAILS ===================== -->
<h2 align="center"> 🔧 Technical Details </h2>

<h3>🔹 Server Architecture</h3>

<ul>
  <li><strong>Concurrency:</strong> ConcurrentHashMap for thread-safe client management</li>
  <li><strong>Threading:</strong> One dedicated thread per client connection</li>
  <li><strong>Port:</strong> 5001 (configurable in code)</li>
  <li><strong>Protocol:</strong> Text-based message protocol over TCP</li>
</ul>

<h3>🔹 Client Architecture</h3>

<ul>
  <li><strong>Connection:</strong> Socket with 3-second timeout</li>
  <li><strong>Threading:</strong> Separate listener thread for incoming messages</li>
  <li><strong>UI:</strong> Event-driven Swing components</li>
  <li><strong>State Management:</strong> Real-time UI updates via SwingUtilities</li>
</ul>

<h3>🔹 Message Flow</h3>

<pre><strong>
1. Client connects → Server accepts
2. Server requests username → Client sends
3. Server validates & broadcasts join
4. Messages flow: Client → Server → All Clients
5. Client disconnects → Server broadcasts leave
</strong></pre>

---

<!-- ===================== TROUBLESHOOTING ===================== -->
<h2 align="center"> 🔍 Troubleshooting </h2>

<h3>🔹 Common Issues</h3>

<table align="center">
  <tr>
    <th>Problem</th>
    <th>Solution</th>
  </tr>
  <tr>
    <td>Java not recognized</td>
    <td>Install Java JDK and add to PATH</td>
  </tr>
  <tr>
    <td>Cannot connect to server</td>
    <td>Ensure server is running first</td>
  </tr>
  <tr>
    <td>Port already in use</td>
    <td>Close other apps using port 5001</td>
  </tr>
  <tr>
    <td>Username rejected</td>
    <td>Use 2-15 chars, alphanumeric + underscore only</td>
  </tr>
</table>

---

<!-- ===================== FUTURE ENHANCEMENTS ===================== -->
<h2 align="center"> 🚀 Future Enhancements </h2>

<ul>
  <li>🔐 Add user authentication and password protection</li>
  <li>💾 Message history persistence</li>
  <li>📁 File sharing capabilities</li>
  <li>🎨 Customizable themes and color schemes</li>
  <li>🔔 Sound notifications for new messages</li>
  <li>👥 Private messaging between users</li>
  <li>🌐 Support for remote server connections</li>
  <li>📊 Server statistics and analytics</li>
  <li>🔒 Message encryption</li>
  <li>👤 User profiles with avatars</li>
</ul>

---

<!-- ===================== CONCLUSION ===================== -->
<h2 align="center"> 🧠 Conclusion </h2>

<p align="center">
606 ChatApp demonstrates how networking, multithreading, and GUI design
can be combined to build a robust real-time communication system.
</p>

<p align="center">
It serves as a strong educational example of Java-based
client–server applications with modern UI design and professional code structure.
</p>

<p align="center">
The application showcases best practices in:
</p>

<ul>
  <li>✅ Socket programming</li>
  <li>✅ Multi-threaded application design</li>
  <li>✅ Thread-safe concurrent data structures</li>
  <li>✅ Event-driven GUI development</li>
  <li>✅ Clean code architecture</li>
</ul>

---

<!-- ===================== LICENSE ===================== -->
<h2 align="center"> 📄 License </h2>

<p align="center">
This project is open source and available for educational purposes.
</p>

---

<!-- ===================== AUTHOR ===================== -->
<h2 align="center"> 👤 Author </h2>

<p align="center">
<strong>x606</strong><br>
</p>

<p align="center">
<em>Developed with ☕ Java and 💜 Passion</em>
</p>

---

<!-- ===================== FOOTER ===================== -->
<div align="center">
  <img 
    src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=100&section=footer"
    width="100%"
  />
</div>





