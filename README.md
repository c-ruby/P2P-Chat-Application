# Program 7 - Java Chat Application

**Course:** CMSC 3320/001  
**Group:** 7  
**Members:**  
- Caleb Ruby — calebruby.cs@gmail.com
- Adir Turgeman — tur28711@pennwest.edu  
- Caleb Rachocki — rac3146@pennwest.edu  
- Ryan Miller — mil0780@pennwest.edu  

---

## Overview
This project is a simple two-way chat application built in Java using sockets and AWT for the graphical user interface.  
It allows two users to communicate over a network connection — one acting as the **server**, the other as the **client**.

---

## Features
- GUI built with Java AWT  
- Client and Server modes  
- Adjustable port and host settings  
- Configurable timeout  
- Message display and dialog logs  
- Graceful connection and disconnection handling  

---

## Requirements
- Java 8 or later  
- IDE or command-line environment that supports Java compilation and execution  

---

## How to Run

1. **Compile the code:**
   ```
   javac Main.java Chat.java
   ```

2. **Start the server:**
   ```
   java Main
   ```
   - Click **"Start Server"** in the GUI.
   - The server will begin listening for a connection.

3. **Start the client (on the same or different machine):**
   ```
   java Main
   ```
   - Enter the host IP address and port number.  
   - Click **"Connect"**.

4. **Chat away!**
   - Type your message in the text box and press **Enter** or click **Send**.

---

## Notes
- Default port: **44004**  
- Default timeout: **2000 ms**  
- The server must be started before the client connects.  
- Both applications can run on the same machine by using `localhost` as the host.

---

## Future Improvements
- Add message encryption for security  
- Enhance GUI layout and design  
- Implement user authentication  
- Add support for multiple clients

---

## License
This project is for educational purposes as part of **CMSC 3320** coursework.

