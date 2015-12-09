package ClientDisconnecting;

import org.quickconnectfamily.json.JSONInputStream;
import org.quickconnectfamily.json.JSONOutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Lloyd on 12/4/2015.
 */

// IN order to actually run this, make sure to have both the client and server
    // classes up and next to each other. Make sure the Services > Tomcat is shutdown
    // at the start of the connection process. Then run the index.jsp file from the server
    // side of things. Only then can you ctrl+shift+10 from this class to get it to
    // connect successfully. Tada! Type in your message to the server, and see the feedback
    // in the runtime window.
public class ClientDisconnectingUnexpectedly {
    public static void main(String[] args){
        ClientDisconnectingUnexpectedly theClient = new ClientDisconnectingUnexpectedly();
        theClient.go();
    }
    private void go() {
        while(true){
            try {
                Scanner systemInScanner = new Scanner(System.in);
                System.out.printf("Enter the message to send to the server.\n");
                String messageForServer = systemInScanner.nextLine();

                // Note: Where do I put the statement to put it to sleep for a bit, so I can manually shut down the client
                // connection? Hah - and how do I tell it to go to sleep?
                // You put it in the JSONEcho*.java class. JSONEchoClient or JSONEchoServer. Right after
                // making the JSONInputStream and JSONOutputStream.

                // Nasty path: Try to have two clients open at the same time. Result: They are both technically running,
                // so they are using the same output window to communicate with the server, and the server is handling
                // both clients.

                // Nasty path: Connect initially to the server, but then shut down the client, see what happens. Result:
                // No problems.

                URL url = new URL("http://localhost:8080/json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);//allows POST
                JSONOutputStream outToServer = new JSONOutputStream(connection.getOutputStream());

                HashMap<String, Object> request = new HashMap<>();
                request.put("command", "Speak");
                request.put("message", messageForServer);

                // Sending the hashmap to the server.
                outToServer.writeObject(request);

                // Nasty path: Run this: Thread.currentThread().sleep(100000); right here on the client side after connecting
                // successfully to the server, then manually shut down the client after sending a message.
                // Results: returned no errors and Process finished with exit code -1.

                JSONInputStream inFromServer = new JSONInputStream(connection.getInputStream());
                HashMap<String, Object> response = (HashMap<String, Object>) inFromServer.readObject();

                // Nasty path: try to put the thread to sleep here, while using a unique message to signify where it is
                // in the process. Once it returns from the server, manually shut down the client.
                // Errors: none. Process finished with exit code -1.
//                System.out.println("Foggy Bear Messaging Active");
//                Thread.currentThread().sleep(100000);

                if (response.get("command").equals("Done")) {
                    System.out.println("Sent request: " + request + "and  got response  " + response);
                } else {
                    System.out.println("Oops. got " + response);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
