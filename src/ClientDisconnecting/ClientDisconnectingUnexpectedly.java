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
//        JSONEchoClient theClient = new JSONEchoClient();
        ClientDisconnectingUnexpectedly theClient = new ClientDisconnectingUnexpectedly();
        theClient.go();
    }
    private void go() {
        while(true){
            try {
                Scanner systemInScanner = new Scanner(System.in);
                System.out.printf("Enter the message to send to the server.\n");
                String messageForServer = systemInScanner.nextLine();

                URL url = new URL("http://localhost:8080/json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);//allows POST
                JSONOutputStream outToServer = new JSONOutputStream(connection.getOutputStream());

                HashMap<String, Object> request = new HashMap<>();
                request.put("command", "Speak");
                request.put("message", messageForServer);

                outToServer.writeObject(request);

                JSONInputStream inFromServer = new JSONInputStream(connection.getInputStream());
                HashMap<String, Object> response = (HashMap<String, Object>) inFromServer.readObject();
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
