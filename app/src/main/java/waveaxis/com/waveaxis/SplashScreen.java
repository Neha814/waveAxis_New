package waveaxis.com.waveaxis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class SplashScreen extends AppCompatActivity {

    String device_id;

    String device_name;

    boolean isConnected ;

    TransparentProgressDialog db ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        setContentView(R.layout.activity_main);

        device_id = Settings.Secure.getString(getApplicationContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID);

        device_name = android.os.Build.MODEL;

        if (isConnected) {

            new addMachine().execute(new Void[0]);

          //
        } else {
            showDialog(Constants.No_INTERNET);
        }

    }


    public class addMachine extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap<String, String> result = new HashMap<String, String>();

        ArrayList localArrayList = new ArrayList();

        protected Void doInBackground(Void... paramVarArgs) {


            //http://phphosting.osvin.net/waveaxisNew/Web_API/addDevice.php?devicename=XT1033&devicetoken=3ioio

            try {
                localArrayList.add(new BasicNameValuePair("devicetoken", device_id));
                localArrayList.add(new BasicNameValuePair("devicename", device_name));

                result = function.AddDevice(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            // db.dismiss();

            try {

                String response = (String)result.get("Response");
                if (response.equals("true")) {

                    Constants.DEVICE_ID = (String) result.get("deviceid");
                    new GetMachineDetails().execute(new Void[0]);

                } else {
                    showDialog("Device not added.Please try again.");
                }
            }

            catch (Exception ae) {
                ae.printStackTrace();
                showDialog(Constants.ERROR_MSG);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            try {
               /* db = new TransparentProgressDialog(SplashScreen.this,
                        R.drawable.loadingicon);
                db.show();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class GetMachineDetails extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap<String, String> result = new HashMap<String, String>();

        ArrayList localArrayList = new ArrayList();

        protected Void doInBackground(Void... paramVarArgs) {


           // http://phphosting.osvin.net/waveaxisNew/Web_API/getMachinenModule.php?deviceid=3

            try {
                localArrayList.add(new BasicNameValuePair("deviceid", Constants.DEVICE_ID));

                result = function.GetMachineDetails(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
           // db.dismiss();

            try {

                String response = (String)result.get("Response");
                if (response.equals("true")) {

                    Constants.MACHINE_ID = (String) result.get("machine_id");
                    Constants.MACHINE_NAME = (String) result.get("machine_id");

                    Constants.OEE = (String) result.get("equipment_effectiveness");
                    Constants.CP = (String) result.get("cp");
                    Constants.CPK = (String) result.get("cpk");
                    Constants.SPINDLE = (String) result.get("no_of_spindleRun");
                    Constants.QULAITY_ISSUE = (String) result.get("quality_issue");

                    Intent i = new Intent(SplashScreen.this , Home.class);
                    startActivity(i);

                } else if(response.equals("false")) {
                    showDialog("No data found");
                }
                else {
                    showDialog(Constants.ERROR_MSG);
                }
            }

            catch (Exception ae) {
                ae.printStackTrace();
                showDialog(Constants.ERROR_MSG);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            try {
               /* db = new TransparentProgressDialog(SplashScreen.this,
                        R.drawable.loadingicon);
                db.show();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    SplashScreen.this).create();

            alertDialog.setTitle("Alert !");
            alertDialog.setMessage(msg);
            //alertDialog.setIcon(R.drawable.browse);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    dialog.cancel();

                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
