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
public class Client extends Thread {

    private String   address;
    private int      port;
    private TextView chat;

    private String OPtype;
    private int a;
    private int b;

    private Socket socket;

    public Client(
            String address,
            int port,
            TextView chat,
            String opType,
            int a,
            int b) {
        this.address                 = address;
        this.port                    = port;
        this.chat = chat;
        this.a = a;
        this.b = b;
        this.OPtype = opType;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter    = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(OPtype + "," + String.valueOf(a) + "," + String.valueOf(b));
                printWriter.flush();
                String infoFromSV;
                while ((infoFromSV = bufferedReader.readLine()) != null) {
                    final String SVSays = infoFromSV;
                    chat.post(new Runnable() {
                        @Override
                        public void run() {
                            chat.append("[SERVER]: " + SVSays + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
