package ro.pub.cs.system.pdsd.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by manu on 16/05/16.
 */
public class CommunicationThread extends Thread {

    private Server serverThread;
    private Socket socket;


    private TextView chat;

    public CommunicationThread(Server serverThread, Socket socket, TextView chat) {
        this.serverThread = serverThread;
        this.socket       = socket;
        this.chat         = chat;
    }


    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client!");

                    String data = bufferedReader.readLine();

                    final String CLSays = data;
                    chat.post(new Runnable() {
                        @Override
                        public void run() {
                            chat.append("[CLIENT]: " + CLSays + "\n");
                        }
                    });

                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Client says: " + data);

                    String[] params = CLSays.split(",");

                    long result = 0;
                    String resp = null;

                    int a = Integer.parseInt(params[1]);
                    int b = Integer.parseInt(params[2]);

                    if(params[0].equals(Constants.SUM_TYPE)) {
                        result = ((long) a) + b;
                    } else if(params[0].equals(Constants.MUL_TYPE)) {
                        try {
                            Thread.sleep(2000);                 //1000 milliseconds is one second.
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        result = ((long) a) * b;
                    }

                    if (result > Integer.MAX_VALUE) {
                       resp = "overflow";
                    } else if (result < Integer.MIN_VALUE) {
                        resp = "overflow";
                    } else {
                        resp = String.valueOf(result);
                    }

                    printWriter.println(resp);
                    printWriter.flush();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }

    }
}