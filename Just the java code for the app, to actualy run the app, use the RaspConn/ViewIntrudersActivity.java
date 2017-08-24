package com.siukit.raspconn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ViewIntrudersActivity extends AppCompatActivity {

    private static String jsonString;
    private static StringBuilder jsonResult;
    private static ArrayList<Intruder> intruderArrayList;

    //for handling JSOn data
    private static JSONObject jsonResponse;
    private static JSONArray jsonMainNode;
    private static JSONObject jsonChildNode;
    private static Intruder intruder;
    private static UnauthLogsAdaptor unauthLogsAdapter;

    public static Context context;
    private static ListView logList;

    private Handler handler = new Handler();

    private static String SERVER_IP = "192.168.3.2";

    //check if asynctask is completed or not
    private boolean isBusy = false;
    private boolean stop = false;

    //Display the menu spinner on custom menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_menu, menu);
        MenuItem my_menu = menu.findItem(R.id.log_menu);
        Spinner spinnerView = (Spinner) MenuItemCompat.getActionView(my_menu);
        //use custom menu adapter
        ArrayAdapter<CharSequence> menu_adapter = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);
        menu_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //different options for user to select on the menu
        if (spinnerView instanceof Spinner) {
            final Spinner spinner = (Spinner) spinnerView;

            spinner.setAdapter(menu_adapter);

            spinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String selectedItem = parent.getItemAtPosition(position).toString();

                    //when 'Turn on alarm' was chosen
                    if (selectedItem.equals("Turn on siren")) {
//                        if (!isSpinnerTouched) return;
//                        Toast.makeText(getApplicationContext(), "Alarm are turned on!!", Toast.LENGTH_LONG).show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Response.Listener<String> rl = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                JSONObject jr = null;
                                                try {
                                                    jr = new JSONObject(response);
                                                    boolean success = jr.getBoolean("success");

                                                    //if switched mode successfully
                                                    if (success) {
                                                        Toast.makeText(getApplicationContext(), "SIREN IS TURNED ON!", Toast.LENGTH_LONG).show();
                                                        // if fail to switch mode
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "SIREN CAN NOT BE TURNED ON! CHECK SERVER CONNECTION!", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        //send the String 'on' to 'is_on' column on system_mode table
                                        SwitchModeRequest switchModeRequest = new SwitchModeRequest("on", "siren", rl);
                                        RequestQueue queue = Volley.newRequestQueue(ViewIntrudersActivity.this);
                                        queue.add(switchModeRequest);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("This will TURN ON the SIREN immediately!" +
                                " Press Yes to confirm or No to cancel.")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();


                    }

                    //when 'Turn off alarm' was chosen
                    if (selectedItem.equals("Turn off siren")) {
//                        Toast.makeText(getApplicationContext(), "Alarm are turned on!!", Toast.LENGTH_LONG).show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Response.Listener<String> rl = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                JSONObject jr = null;
                                                try {
                                                    jr = new JSONObject(response);
                                                    boolean success = jr.getBoolean("success");

                                                    //if switched mode successfully
                                                    if (success) {
                                                        Toast.makeText(getApplicationContext(), "SIREN IS TURNED OFF!", Toast.LENGTH_LONG).show();
                                                        // if fail to switch mode
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "SIREN CAN NOT BE TURNED OFF! CHECK SERVER CONNECTION!", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        //send the String 'on' to 'is_on' column on system_mode table
                                        SwitchModeRequest switchModeRequest = new SwitchModeRequest("off", "siren", rl);
                                        RequestQueue queue = Volley.newRequestQueue(ViewIntrudersActivity.this);
                                        queue.add(switchModeRequest);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("This will TURN OFF the siren immediately!" +
                                " Press Yes to confirm or No to cancel.")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();


                    }

                    //when 'Remove all logs' was chosen
                    if (selectedItem.equals("Remove all logs")) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        new ViewIntrudersActivity.RemoveLogs().execute();
                                        Toast.makeText(getApplicationContext(), "Authorized logs are being removed.", Toast.LENGTH_LONG).show();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to remove all logs?" +
                                " (All images were backed up on cloud server but will no longer available to view on app)")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_intruders);
        context = this;
        startHandler();
    }

    //fetch logs data from the database
    class GetUnauthLogsJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            System.out.println("ASYNCTASK IS RUNNING");
            jsonResult = new StringBuilder();
            try {
                //connect to getUsers php file
//                URL url = new URL("http://" + SERVER_IP + "/get_unauth_logs.php");
                URL url = new URL("http://194.81.104.22/~14412104/dis/get_unauth_logs.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(conn.getInputStream());
//                    String data = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((jsonString = reader.readLine()) != null) {
                    jsonResult.append(jsonString);
                }
//                System.out.println(jsonString);
                reader.close();
                in.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            test = jsonResult.toString();
//            System.out.println("TEST:" + test);

            return null;
        }

        protected void onPostExecute(String result) {

            showIntrudersLogs();
        }

    }

    //show logs on list view
    protected void showIntrudersLogs() {

        intruderArrayList = new ArrayList<Intruder>();
//        intruderArrayList = null;

        try {
            jsonResponse = new JSONObject(jsonResult.toString());
            jsonMainNode = jsonResponse.optJSONArray("log");

//            System.out.println("Response from JSON:     " + jsonResult.toString());

            for (int i = 0; i < jsonMainNode.length(); i++) {
                intruder = new Intruder();
                jsonChildNode = jsonMainNode.getJSONObject(i);
                intruder.setEntryTime(jsonChildNode.optString("entry_time"));
                intruder.setPicture(jsonChildNode.optString("picture"));
                intruderArrayList.add(intruder);
                System.out.println("Entry time: " + intruder.getEntryTime());
            }
        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
//                    Toast.LENGTH_SHORT).show();
//            System.out.println("ERROR:     " + e);
        }
        Collections.reverse(intruderArrayList);
        unauthLogsAdapter = new UnauthLogsAdaptor(this, R.layout.list_unauth_logs, intruderArrayList);
        logList = (ListView) findViewById(R.id.lvUnauthLogs);

        logList.setAdapter(unauthLogsAdapter);
    }

    //run AysncTask every three second (after previous one has finished)
    public void startHandler() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isBusy) startAysncTask();
                if (!stop) startHandler();
                new ViewIntrudersActivity.GetUnauthLogsJSON().execute();
            }
        }, 5000);
    }

    private void startAysncTask () {
        new ViewIntrudersActivity.GetUnauthLogsJSON().execute();
    }

    private class RemoveLogs extends AsyncTask<Object, Object, Void> {
        @Override
        protected Void doInBackground(Object... strings) {

            HttpURLConnection conn = null;
            try {
                //connect to getUsers php file
//                URL url = new URL("http://" + SERVER_IP + "/rm_unauth_logs.php");
                URL url = new URL("http://194.81.104.22/~14412104/dis/rm_unauth_logs.php");
                conn = (HttpURLConnection) url.openConnection();

                if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
                    InputStream is = conn.getInputStream();
                }else{
                    InputStream err = conn.getErrorStream();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }
    }
}
