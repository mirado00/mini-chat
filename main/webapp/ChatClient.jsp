<%@ page import = java.io.* %>
<%@ page import = java.net.* %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Miresaka</title>
<link rel="stylesheet" href="./stules_ChatClient.css">
</head>
<body>
<body>
	
    <header>
        <h1>Real-Time Chat Application</h1>
      </header>
      <div class="container">
        <div class="chat-window">
          <div class="chat-area">
            <div class="chat-messages">
              <!-- Chat Messages -->
              <%
              try {
                  Socket socket = new Socket("localhost", 12345);
                  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  PrintWriter Out = new PrintWriter(socket.getOutputStream(), true);

                  BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

                  // Demander à l'utilisateur de saisir son identifiant
                  System.out.print("Entrez votre identifiant : ");
                  String clientId = consoleInput.readLine();
                  Out.println(clientId); // Envoyer l'identifiant au serveur

                  System.out.println("Connecté au serveur.");

                  Thread receiveThread = new Thread(() -> {
                      try {
                          String message;
                          while ((message = in.readLine()) != null) {
                              out.println("<label>"+message+"");
                          }
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  });
                  receiveThread.start();

                  String userInput;
                  // Boucle pour lire les saisies utilisateur et envoyer les messages au serveur
                  while ((userInput = consoleInput.readLine()) != null) {
                      Out.println(userInput);
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
              %>
            </div>
          </div>
          <form method="post"  action="http://localhost:8080/proje/ChatClientServelet" class="user-input">
            <input type="text" id="message-input" placeholder="Type your message..." name="message">
            <button id="send-button" type="submit">Send</button>
          </form>
        </div>
      </div>
      <script>
        function sendMessage() {
            const messageInput = document.getElementById('message-input');
            const message = messageInput.value.trim();
            if (message !== '') {
                const chatMessages = document.querySelector('.chat-messages');
                const messageElement = document.createElement('div');
                messageElement.classList.add('message');
                messageElement.textContent = message;
                chatMessages.appendChild(messageElement);
                messageInput.value = '';
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        }

            // Event listener
        document.getElementById('send-button').addEventListener('click', sendMessage);
        document.getElementById('message-input').addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                event.preventDefault();
                sendMessage();
            }
         });
      </script>
</body>
</body>
</html>