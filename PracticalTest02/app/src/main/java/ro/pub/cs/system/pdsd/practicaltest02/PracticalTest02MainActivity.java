package ro.pub.cs.system.pdsd.practicaltest02;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chat = (TextView) findViewById(R.id.chat);
        chat.setMovementMethod(new ScrollingMovementMethod());
    }

    private Server serverThread             = null;
    private Client clientThread             = null;
    TextView chat = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test02_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startServer(View v){
        EditText portTxt = (EditText) findViewById(R.id.server_port);

        Integer port = -1;

        try {
            port = Integer.parseInt(portTxt.getText().toString());
        }
        catch (Exception ex){
            Log.d(Constants.TAG, "Invalid port: " + portTxt.getText().toString());

            Toast.makeText(
                    getApplicationContext(),
                    "Server port should be filled with int!",
                    Toast.LENGTH_SHORT
            ).show();
        }

        if(port > 0) {
            Log.d(Constants.TAG, "startSV clicked, port: " + port.toString());

            serverThread = new Server(port, chat);
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }
        }
    }

    public void startClient(String opType, int a, int b){
        EditText portTxt = (EditText) findViewById(R.id.server_port);

        Integer port = -1;
        String addr = "localhost";

        try {
            port = Integer.parseInt(portTxt.getText().toString());
        }
        catch (Exception ex){
            Log.d(Constants.TAG, "Invalid port: " + portTxt.getText().toString());

            Toast.makeText(
                    getApplicationContext(),
                    "Server port should be filled with int!",
                    Toast.LENGTH_SHORT
            ).show();
        }
        if(port > 0) {
            Log.d(Constants.TAG, "[INTF] Requested " + opType + " for " + String.valueOf(a) + " and " + String.valueOf(b));
            clientThread = new Client(
                    addr,
                    port,
                    chat, opType, a, b);
            clientThread.start();


        }
    }


    public void sumReq(View v){

        String opType = Constants.SUM_TYPE;

        EditText op1 = (EditText)findViewById(R.id.num_1);
        EditText op2 = (EditText)findViewById(R.id.num_2);


        int a = Integer.parseInt(op1.getText().toString());
        int b = Integer.parseInt(op2.getText().toString());

        startClient(opType, a, b);
    }

    public void mulReq(View v){

        String opType = Constants.MUL_TYPE;

        EditText op1 = (EditText)findViewById(R.id.num_1);
        EditText op2 = (EditText)findViewById(R.id.num_2);


        int a = Integer.parseInt(op1.getText().toString());
        int b = Integer.parseInt(op2.getText().toString());

        startClient(opType, a, b);

    }
}
