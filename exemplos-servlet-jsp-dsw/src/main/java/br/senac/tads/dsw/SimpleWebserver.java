/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.dsw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fernando.tsuda
 */
public class SimpleWebserver {

    private final int port;
    private static final Logger logger = LoggerFactory.getLogger(SimpleWebserver.class);
    private static final int THREAD_POOL_SIZE = 10;

    public SimpleWebserver(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor(); 
                ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port: {}", port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleClient(clientSocket));
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String clientInputLine;
            StringBuilder headerIn = new StringBuilder();
            
            while ((clientInputLine = in.readLine()) != null) {
                logger.info("Client Request: {}", clientInputLine);
                headerIn.append(clientInputLine).append("\r\n");
                if (clientInputLine.isEmpty()) {
                     break;
                }
            }

            String bodyOutTemplate = """
                <!doctype html>
                <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>TADS DSW</title>
                          
                    </head>
                    <body>
                        <h1>Exemplo Servidor Web Java</h1>
                        <p>Teste alteração</p>
                        <hr>
                        <h2>Mensagem Request</h2>
                        <pre>{0}</pre>
                    </body>
                </html>
                
                """;
            String body = MessageFormat.format(bodyOutTemplate.replace("'", "''"), headerIn.toString());
            
            int length = body.length();
            LocalDateTime now = LocalDateTime.now();

            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Date: " + now + "\r\n");
            out.write("Server: Custom Server\r\n");
            out.write("Content-Type: text/html\r\n");
            // out.write("Content-Length: " + length + "\r\n");
            out.write("\r\n");
            out.write(body);
        } catch (IOException e) {
            logger.error("Error handling client", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        SimpleWebserver server = new SimpleWebserver(port);
        server.start();
    }
}
