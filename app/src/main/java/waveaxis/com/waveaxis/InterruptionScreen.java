package waveaxis.com.waveaxis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 17/11/15.
 */
public class InterruptionScreen extends Activity implements View.OnClickListener{

    //Spinner part_number_spinner, operator_name_spinner;

    //MyAdapter1 mAdapter1 ;

    MyAdapter2 mAdapter2 ;

    TextView msa,operator_name, part_number,oee, target_time,cp, cpk,current_date,
            current_time,interruption_text ,start_time ,end_time, total_time;

    LinearLayout reset_layout;

    Button reset_button , fixed;

    TransparentProgressDialog db;

    Boolean isConnected ;

    Spinner interruption_spinner;

    int  InterruptionCounter ;

    boolean isSuccess = false ;

    ArrayList<HashMap<String , String>> InterruptionListing = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.interruption_screen);

        inIt();
    }

    private void inIt() {

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        /*operator_name_spinner = (Spinner) findViewById(R.id.operator_name_spinner);
        part_number_spinner = (Spinner) findViewById(R.id.part_number_spinner);*/
        msa = (TextView) findViewById(R.id.msa);
        reset_button = (Button) findViewById(R.id.reset_button);
        reset_layout = (LinearLayout) findViewById(R.id.reset_layout);
        operator_name = (TextView) findViewById(R.id.operator_name);
        part_number = (TextView) findViewById(R.id.part_number);
        oee = (TextView) findViewById(R.id.oee);
        target_time = (TextView) findViewById(R.id.target_time);
        cp = (TextView) findViewById(R.id.cp);
        cpk = (TextView) findViewById(R.id.cpk);
        current_time = (TextView) findViewById(R.id.current_time);
        current_date = (TextView) findViewById(R.id.current_date);
        interruption_spinner = (Spinner) findViewById(R.id.interruption_spinner);
        interruption_text = (TextView) findViewById(R.id.interruption_text);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        total_time = (TextView) findViewById(R.id.total_time);
        fixed = (Button) findViewById(R.id.fixed);


        reset_layout.setOnClickListener(this);
        reset_button.setOnClickListener(this);
        fixed.setOnClickListener(this);

        operator_name.setText("Operator Name : " + Constants.OPERATOR_NAME);
        part_number.setText(Constants.PART_NUMBER);
        oee.setText("OEE : "+Constants.OEE);
        cp.setText("CP : "+Constants.CP);
        cpk.setText("CP : "+Constants.CPK);
        msa.setText(Constants.MACHINE_NAME+" - " + Constants.MACHINE_ID);

        long date = System.currentTimeMillis();

        // SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");

        SimpleDateFormat dateToShow = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateToShow.format(date);

        current_date.setText(currentDate);

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String timeString = sdf.format(date);

        current_time.setText(timeString);

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

        /*ArrayList<String> partList = new ArrayList<String>();
        partList.add("Part Number");
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                partList);
        part_number_spinner.setAdapter(mAdapter1);

        ArrayList<String> operatorList = new ArrayList<String>();
        operatorList.add("Operator Name");
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                operatorList);
        operator_name_spinner.setAdapter(mAdapter1);*/

        interruption_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Constants.INTERRUPTION_NAME = "Interruption";
                    Constants.INTERRUPTION_ID="";
                    Constants.TARGET_TIME="0";
                    Constants.START_TIME="0";
                    Constants.END_TIME="0";
                    Constants.TOTAL_TIME="0";
                    interruption_text.setText(Constants.INTERRUPTION_NAME);
                    target_time.setText(Constants.TARGET_TIME);
                    start_time.setText("0");
                    end_time.setText("0");
                    total_time.setText("0");
                }

                else {
                    long date = System.currentTimeMillis();

                    InterruptionCounter = i - 1;

                    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                    String timeString = sdf.format(date);

                    current_time.setText(timeString);

                    Constants.INTERRUPTION_NAME = InterruptionListing.get(InterruptionCounter).get("interruption_name");
                    Constants.INTERRUPTION_ID=InterruptionListing.get(InterruptionCounter).get("interruption_id");
                    Constants.TARGET_TIME=InterruptionListing.get(InterruptionCounter).get("target_time");
                    Constants.START_TIME=timeString;
                    Constants.END_TIME="0";
                    Constants.TOTAL_TIME="0";

                    interruption_text.setText(Constants.INTERRUPTION_NAME);
                    target_time.setText(Constants.TARGET_TIME);
                    start_time.setText(Constants.START_TIME);
                    end_time.setText("0");
                    total_time.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        if (isConnected) {

            new getInterruptionListing().execute(new Void[0]);

        } else {
            showDialog(Constants.No_INTERNET);
        }
    }

    @Override
    public void onClick(View view) {

        if(view==reset_layout || view == reset_button){
            if(Constants.START_TIME.length()>1) {
                showResetDialog();
            } else {
               showDialog("Please select interruption first.");
            }
        }

        else if(view == fixed){
            if(Constants.START_TIME.length()>1) {
                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                String timeString = sdf.format(date);

                Constants.END_TIME = timeString ;

                Constants.TOTAL_TIME = StringUtils.timeDifferenceInHours(Constants.START_TIME,Constants.END_TIME);

                Constants.TOTAL_TIME = String.format("%.2f", Double.parseDouble(Constants.TOTAL_TIME));

                end_time.setText(Constants.END_TIME);
                total_time.setText(Constants.TOTAL_TIME+" Hrs");

                new FixInterrupt().execute(new Void[0]);

            } else {
                showDialog("Please select interruption first.");
            }
        }
    }

    // ************************* signIn Dialog ********************************************//

    protected void showResetDialog() {
        try {
            final Dialog dialog;
            dialog = new Dialog(InterruptionScreen.this,R.style.Theme_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
            dialog.setContentView(R.layout.sign_in_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(true);
            Drawable d = new ColorDrawable(Color.BLACK);
            d.setAlpha(0);
            dialog.getWindow().setBackgroundDrawable(d);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final EditText username, password;
            Button login;

            username = (EditText) dialog.findViewById(R.id.username);
            password = (EditText) dialog.findViewById(R.id.password);
            login = (Button) dialog.findViewById(R.id.login);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username_text = username.getText().toString();
                    String password_text = password.getText().toString();

                    if(username_text.trim().length()<1){
                        username.setError("Please enter username.");
                    }  else if(password_text.trim().length()<1){
                        password.setError("Please enter password.");
                    }
                    else {
                        // CallSignInAPI
                        dialog.dismiss();
                        new SignIn(username_text, password_text).execute(new Void[0]);
                    }
                }
            });


            dialog.show();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    /*class MyAdapter1 extends BaseAdapter {

        LayoutInflater mInflater = null;

        ArrayList<String> ListItems = new ArrayList<String>();

        public MyAdapter1(Context context,
                          ArrayList<String> ListDetail) {
            mInflater = LayoutInflater.from(getApplicationContext());
            ListItems= ListDetail;

        }


        @Override
        public int getCount() {

            return ListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return ListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.menu_spinner_item_dropdown, true);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.menu_spinner_item, false);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent, int spinnerRow, boolean isDefaultRow) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(spinnerRow, parent, false);
            TextView txt = (TextView)row.findViewById(R.id.text);
            ImageView img = (ImageView)row.findViewById(R.id.img);

            txt.setText(ListItems.get(position));
            img.setVisibility(View.GONE);
            return row;
        }

    }
    */
    // ************************************ SignIn API ****************************************//

    public class SignIn extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap<String, String> result = new HashMap<String, String>();

        ArrayList localArrayList = new ArrayList();
        String USERNAME , PASSWORD ;

        public SignIn(String username_text, String password_text) {
            this.USERNAME = username_text;
            this.PASSWORD = password_text;
        }


        protected Void doInBackground(Void... paramVarArgs) {


            //http://phphosting.osvin.net/waveaxisNew/Web_API/CheckCredentials.php?username=admin@gmail.com&password=demo@123

            try {
                localArrayList.add(new BasicNameValuePair("username", this.USERNAME));
                localArrayList.add(new BasicNameValuePair("password", this.PASSWORD));

                result = function.SignIn(localArrayList);

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
                    Constants.SETTER_NAME = this.USERNAME;
                    showResetTimerDialog();

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
                db = new TransparentProgressDialog(InterruptionScreen.this,
                        R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // ************************** Fix Interrupt ************************************************//

    public class FixInterrupt extends AsyncTask<Void,Void,Void>{

        Functions function = new Functions();

        HashMap<String, String> result = new   HashMap<String, String>();

        ArrayList localArrayList = new ArrayList();


        @Override
        protected Void doInBackground(Void... voids) {

           /* http://phphosting.osvin.net/waveaxisNew/Web_API/InterruptionTimer.php?
            MachineId=13&InterruptionId=4&StartTime=06:00:00&EndTime=09:00:00&TotalTime=1%20hour*/

            try {
                localArrayList.add(new BasicNameValuePair("MachineId", Constants.MACHINE_ID));
                localArrayList.add(new BasicNameValuePair("InterruptionId", Constants.INTERRUPTION_ID));
                localArrayList.add(new BasicNameValuePair("StartTime", Constants.START_TIME));
                localArrayList.add(new BasicNameValuePair("EndTime", Constants.END_TIME));
                localArrayList.add(new BasicNameValuePair("TotalTime", Constants.TOTAL_TIME));

                result = function.fixInterrupt(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            db.dismiss();

            try {

                String response = (String)result.get("Response");
                if (response.equals("true")) {

                    isSuccess = true ;
                    String msg = (String) result.get("MessageWhatHappen");
                    showDialog(msg);


                } else {
                    String message = (String) result.get("MessageWhatHappen");
                    showDialog(message);
                }
            }

            catch (Exception ae) {
                ae.printStackTrace();

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                db = new TransparentProgressDialog(InterruptionScreen.this,
                        R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //***************************** Interruption Listing Details ********************************//

    public class getInterruptionListing extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String, String>> result = new   ArrayList<HashMap<String, String>>();

        ArrayList localArrayList = new ArrayList();

        protected Void doInBackground(Void... paramVarArgs) {


            // http://phphosting.osvin.net/waveaxisNew/Web_API/getInterruptionList.php?MachineId=7

            try {
                localArrayList.add(new BasicNameValuePair("MachineId", Constants.MACHINE_ID));

                result = function.getInterruptionList(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
             db.dismiss();

            try {
                InterruptionListing.clear();
                if(result.size()>0){
                    InterruptionListing.addAll(result);

                    final ArrayList<String> innList = new ArrayList<String>();
                    innList.add("Interruption");

                    for(int i=0;i<InterruptionListing.size();i++){
                        innList.add(InterruptionListing.get(i).get("interruption_name")) ;
                    }

                    mAdapter2 = new MyAdapter2(getApplicationContext(),
                            innList);
                    interruption_spinner.setAdapter(mAdapter2);
                }

                else {
                    final ArrayList<String> innList = new ArrayList<String>();
                    innList.add("Interruption");

                    mAdapter2 = new MyAdapter2(getApplicationContext(),
                            innList);
                    interruption_spinner.setAdapter(mAdapter2);
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
                db = new TransparentProgressDialog(InterruptionScreen.this,
                        R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //********************* Interruption spinner Adapter *****************************************//

    class MyAdapter2 extends BaseAdapter {

        LayoutInflater mInflater = null;

        ArrayList<String> ListItems = new ArrayList<String>();

        public MyAdapter2(Context context,
                          ArrayList<String> ListDetail) {
            mInflater = LayoutInflater.from(getApplicationContext());
            ListItems= ListDetail;

        }


        @Override
        public int getCount() {

            return ListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return ListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.part_no_itemlist, true);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.part_no_itemlist, false);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent, int spinnerRow, boolean isDefaultRow) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(spinnerRow, parent, false);
            TextView txt = (TextView)row.findViewById(R.id.text);

            txt.setText(ListItems.get(position));
            return row;
        }

    }

    // *************************** ResetTimerDialog **********************************************//

    protected void showResetTimerDialog() {
        try {
            final Dialog dialog;
            dialog = new Dialog(InterruptionScreen.this,R.style.Theme_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
            dialog.setContentView(R.layout.reset_time_layout);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(true);
            Drawable d = new ColorDrawable(Color.BLACK);
            d.setAlpha(0);
            dialog.getWindow().setBackgroundDrawable(d);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final EditText reset_time;
            Button reset;

            reset_time = (EditText) dialog.findViewById(R.id.reset_time);
            reset = (Button) dialog.findViewById(R.id.reset);

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String resetTime_text = reset_time.getText().toString();
                    if(resetTime_text.length()>1){

                        Constants.RESET_TARGET_TIME_VALUE = resetTime_text;
                        Intent i = new Intent(InterruptionScreen.this , NewSettingChangeOver.class);
                        startActivity(i);

                    }
                    else {
                        showDialog("Please enter new Target Time.");
                    }

                }
            });


            dialog.show();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }



    // ********************************** Show Dilaog ********************************************//

    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    InterruptionScreen.this).create();

            alertDialog.setTitle("Alert !");
            alertDialog.setMessage(msg);
            //alertDialog.setIcon(R.drawable.browse);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    dialog.cancel();
                    if(isSuccess){
                        Intent i = new Intent(InterruptionScreen.this , Home.class);
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
