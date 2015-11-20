package waveaxis.com.waveaxis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 19/11/15.
 */
public class NewSettingChangeOver extends Activity implements View.OnClickListener {

    TextView oee, target_time, cp, cpk, setter_name_text,
            operaotr_name_text,part_number_text,start_time,end_time, total_time, current_date, current_time;

    LinearLayout submit_layout;
    Button submit_button;

    TransparentProgressDialog db;

    boolean isSuccess = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_setting_changeover);

        inIt();
    }

    private void inIt() {


        oee= (TextView) findViewById(R.id.oee);
        target_time= (TextView) findViewById(R.id.target_time);
        cp= (TextView) findViewById(R.id.cp);
        cpk= (TextView) findViewById(R.id.cpk);
        setter_name_text= (TextView) findViewById(R.id.setter_name_text);
        operaotr_name_text= (TextView) findViewById(R.id.operaotr_name_text);
        part_number_text= (TextView) findViewById(R.id.part_number_text);
        start_time= (TextView) findViewById(R.id.start_time);
        end_time= (TextView) findViewById(R.id.end_time);
        total_time= (TextView) findViewById(R.id.total_time);
        current_date= (TextView) findViewById(R.id.current_date);
        current_time= (TextView) findViewById(R.id.current_time);
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_layout = (LinearLayout) findViewById(R.id.submit_layout);

        current_time.setText(Constants.CURRENT_TIME);
        current_date.setText(Constants.CURRENT_DATE);
        operaotr_name_text.setText(Constants.OPERATOR_NAME);
        part_number_text.setText(Constants.PART_NUMBER);
        oee.setText(Constants.OEE);
        cp.setText(Constants.CP);
        cpk.setText(Constants.CPK);
        setter_name_text.setText(Constants.SETTER_NAME);
        target_time.setText(Constants.RESET_TARGET_TIME_VALUE);
        start_time.setText(Constants.START_TIME);
        end_time.setText(Constants.END_TIME);
        total_time.setText(Constants.TOTAL_TIME);


        submit_layout.setOnClickListener(this);
        submit_button.setOnClickListener(this);

        final Handler Timerhandler = new Handler();
        Timerhandler.postDelayed(new Runnable() {
            public void run() {
                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                String timeString = sdf.format(date);

                current_time.setText(timeString);
                Constants.CURRENT_TIME = timeString;
                Timerhandler.postDelayed(this, 4000L);
            }
        }, 1000);

    }

    @Override
    public void onClick(View view) {

        if(view==submit_button || view == submit_layout){
            new ResetTargetTime(Constants.RESET_TARGET_TIME_VALUE).execute(new Void[0]);
        }

    }

    //********************************* Reset target time ****************************************//

    public class ResetTargetTime extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap<String, String> result = new HashMap<String, String>();

        ArrayList localArrayList = new ArrayList();

        String resetTIME ;

        public ResetTargetTime(String resetTime_text) {

            this.resetTIME = resetTime_text;
        }

        protected Void doInBackground(Void... paramVarArgs) {


           /* http://phphosting.osvin.net/waveaxisNew/Web_API/ResetTargetTime.php?
            ResetTime=2015-11-11%2018:08:35&InterruptionId=1&MachineId=4*/

            try {
                localArrayList.add(new BasicNameValuePair("ResetTime", this.resetTIME));
                localArrayList.add(new BasicNameValuePair("InterruptionId", Constants.INTERRUPTION_ID));
                localArrayList.add(new BasicNameValuePair("MachineId", Constants.MACHINE_ID));

                result = function.ResetTargetTime(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {

                String response = (String)result.get("Response");
                if (response.equals("true")) {

                    isSuccess = true ;
                    Intent i = new Intent(NewSettingChangeOver.this , Home.class);
                    startActivity(i);

                } else {
                    String message = (String) result.get("MessageWhatHappen");
                    showDialog(message);
                }
            }

            catch (Exception ae) {
                ae.printStackTrace();

            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            try {
                db = new TransparentProgressDialog(NewSettingChangeOver.this,
                        R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // ********************************** Show Dilaog ********************************************//

    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    NewSettingChangeOver.this).create();

            alertDialog.setTitle("Alert !");
            alertDialog.setMessage(msg);
            //alertDialog.setIcon(R.drawable.browse);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    dialog.cancel();
                    if(isSuccess){
                        Intent i = new Intent(NewSettingChangeOver.this , Home.class);
                        startActivity(i);
                    }

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
