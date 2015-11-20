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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import imageloader.ImageLoader;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 17/11/15.
 */
public class Home extends Activity implements View.OnClickListener {

    Spinner menu_spinner, part_number_spinner, operator_name_spinner;

    MyAdapter mAdapter ;

    MyAdapter1 mAdapter1 ;

    MyAdapter2 mAdapter2 ;

    LinearLayout start_layout;

    Button start_button;

    TextView oee_quality, spindle_value, cp, cpk, part_value, msa, current_date, current_time;

    LinearLayout part_number_layout , cpp_cpk, spindle_run;

    ImageView part_image;

    int timerCount = 0;

    boolean firstTimeOnScreen = true;
    boolean isConnected ;

    ImageLoader imageLoader;

    int menu_count = 0;

     int opPosToSet = 0;

     int partPosToSet = 0;

    String device_id;

    String device_name;

    TransparentProgressDialog db;

    boolean isSuccess = false  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.home);

        inIt();
    }

    private void inIt() {

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        imageLoader = new ImageLoader(getApplicationContext());

        operator_name_spinner = (Spinner) findViewById(R.id.operator_name_spinner);
        part_number_spinner = (Spinner) findViewById(R.id.part_number_spinner);
        menu_spinner = (Spinner) findViewById(R.id.menu_spinner);
        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        start_button = (Button) findViewById(R.id.start_button);
        oee_quality = (TextView) findViewById(R.id.oee_quality);
        part_number_layout = (LinearLayout) findViewById(R.id.part_number_layout);
        cpp_cpk = (LinearLayout) findViewById(R.id.cpp_cpk);
        spindle_run = (LinearLayout) findViewById(R.id.spindle_run);
        spindle_value = (TextView) findViewById(R.id.spindle_value);
        cp = (TextView) findViewById(R.id.cp);
        cpk = (TextView) findViewById(R.id.cpk);
        part_value = (TextView) findViewById(R.id.part_value);
        part_image = (ImageView) findViewById(R.id.part_image);
        msa = (TextView) findViewById(R.id.msa);
        current_time = (TextView) findViewById(R.id.current_time);
        current_date = (TextView) findViewById(R.id.current_date);

        if (isConnected) {

            device_id = android.provider.Settings.Secure.getString(getApplicationContext()
                    .getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            device_name = android.os.Build.MODEL;
            new addMachine().execute(new Void[0]);

            //
        } else {
            showDialog(Constants.No_INTERNET);
        }



        start_button.setOnClickListener(this);
        start_layout.setOnClickListener(this);

       menu_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if (i == 1) {
                   menu_count = 1;
                 /*  showSettingDialog();*/
               } else if (i == 2) {
                   menu_count = 2;
                /*   Intent intent = new Intent(Home.this, InterruptionScreen.class);
                   startActivity(intent);*/
               } else {
                   menu_count = 0;
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

        part_number_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Constants.PART_NUMBER_TOSHOW = "Part Number : " + Constants.partsList.get(i).get("part_no");
                Constants.PART_IAMGE_TOSHOW = Constants.partsList.get(i).get("part_image");

                Log.e("image url==>", "" + Constants.PART_IAMGE);

                Log.e("firstTimeOnScreen==>", "" + firstTimeOnScreen);

                if (!firstTimeOnScreen) {
                    Log.e("showImageDialog","showImageDialog");
                    showPartImageDialog();
                }

                firstTimeOnScreen = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        operator_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               /* Constants.OPERATOR_NAME = Constants.operatorList.get(i).get("operator_name");
                Constants.OPERATOR_ID = Constants.operatorList.get(i).get("operator_id");*/

                //  operator_name_spinner.setSelection(opPosToSet);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







    }



        protected void showPartImageDialog() {
            try {
                final Dialog dialog;
                dialog = new Dialog(Home.this,R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
                dialog.setContentView(R.layout.partimage_dialog);
               getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setCancelable(true);
                Drawable d = new ColorDrawable(Color.BLACK);
                d.setAlpha(0);
                dialog.getWindow().setBackgroundDrawable(d);

                ImageView part_iamge;

                part_iamge = (ImageView) dialog.findViewById(R.id.part_iamge);

                Log.e("url==>",""+Constants.PART_IAMGE_TOSHOW);

                imageLoader.DisplayImage(Constants.PART_IAMGE_TOSHOW,R.drawable.noimg,part_iamge);

                dialog.show();
            } catch (Exception e) {

                e.printStackTrace();
            }

        }


    protected void showSettingDialog() {
        try {
            final Dialog dialog;
            dialog = new Dialog(Home.this,R.style.Theme_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
            dialog.setContentView(R.layout.part_no_operator_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);
            Drawable d = new ColorDrawable(Color.BLACK);
            d.setAlpha(0);
            dialog.getWindow().setBackgroundDrawable(d);

           LinearLayout save_layout, next_layout;
            Button save_button, next_button;

          final   Spinner inner_part_spinner, inner_operator_spinner;

            save_layout = (LinearLayout) dialog.findViewById(R.id.save_layout);
            save_button = (Button) dialog.findViewById(R.id.save_button);
            next_layout = (LinearLayout) dialog.findViewById(R.id.next_layout);
            next_button = (Button) dialog.findViewById(R.id.next_button);
            inner_operator_spinner = (Spinner) dialog.findViewById(R.id.inner_operator_spinner);
            inner_part_spinner = (Spinner) dialog.findViewById(R.id.inner_part_spinner);

            final ArrayList<String> partList = new ArrayList<String>();
            partList.add("Part Number");
            for(int i=0;i<Constants.partsList.size();i++){
                partList.add(Constants.partsList.get(i).get("part_no")) ;
            }
            mAdapter2 = new MyAdapter2(Home.this,
                    partList);
            inner_part_spinner.setAdapter(mAdapter2);

            // set adapter for operator name

            ArrayList<String> operatorList = new ArrayList<String>();
            operatorList.add("Operator Name");
            for(int i=0;i<Constants.operatorList.size();i++) {
                operatorList.add(Constants.operatorList.get(i).get("operator_name")) ;
            }
            mAdapter2 = new MyAdapter2(Home.this,
                    operatorList);
            inner_operator_spinner.setAdapter(mAdapter2);

            save_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();


                    int partPOS =  inner_part_spinner.getSelectedItemPosition();
                    int opPOS = inner_operator_spinner.getSelectedItemPosition();


                    Log.e("partPOS==>",""+partPOS);
                    Log.e("opPOS==>",""+opPOS);

                    if(partPOS==0){
                        showDialog("Please select Part Number");
                    } else if(opPOS==0){
                        showDialog("Please select Operator Name");
                    } else {

                        String   partID = Constants.partsList.get(partPOS-1).get("part_id");
                        String  opID = Constants.operatorList.get(opPOS-1).get("operator_id");
                        CallSettingAPI(partID,opID);
                    }

                }


            });
            save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();


                    int partPOS =  inner_part_spinner.getSelectedItemPosition();
                    int opPOS = inner_operator_spinner.getSelectedItemPosition();


                    Log.e("partPOS==>",""+partPOS);
                    Log.e("opPOS==>",""+opPOS);

                    if(partPOS==0){
                        showDialog("Please select Part Number");
                    } else if(opPOS==0){
                        showDialog("Please select Operator Name");
                    } else {

                        String   partID = Constants.partsList.get(partPOS-1).get("part_id");
                        String  opID = Constants.operatorList.get(opPOS-1).get("operator_id");
                        CallSettingAPI(partID,opID);
                    }




                }
            });
            next_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void CallSettingAPI(String partID, String opID) {

        new Settings(partID,opID).execute(new Void[0]);
    }

    @Override
    public void onClick(View view) {

         if(view==start_button || view==start_layout){
             Log.e("menu_count==",""+menu_count);
            if (menu_count == 1) {
                showSettingDialog();
            } else if (menu_count == 2) {
                Intent intent = new Intent(Home.this, InterruptionScreen.class);
                startActivity(intent);
            }
        }
    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

       ArrayList<String> menuItems = new ArrayList<String>();

        public MyAdapter(Context context,
                         ArrayList<String> menuList) {
            mInflater = LayoutInflater.from(getApplicationContext());
            menuItems= menuList;

        }


        @Override
        public int getCount() {

            return menuItems.size();
        }

        @Override
        public Object getItem(int position) {
            return menuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.menu_spinner_item, true);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.menu_spinner_item_dropdown, false);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent, int spinnerRow, boolean isDefaultRow) {

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(spinnerRow, parent, false);
            TextView txt = (TextView)row.findViewById(R.id.text);
            ImageView img = (ImageView)row.findViewById(R.id.img);

            txt.setText(menuItems.get(position));
            if(position==0){
               img.setBackgroundResource(R.drawable.menu);
            } else if(position==1){
               img.setBackgroundResource(R.drawable.settings);
            } else if(position==2){
                img.setBackgroundResource(R.drawable.interruption);
            } else if(position==3){
               img.setBackgroundResource(R.drawable.tool_life);
            } else if(position==4){
              img.setBackgroundResource(R.drawable.production);
            }
            return row;
        }

    }


    class MyAdapter1 extends BaseAdapter {

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
            // TODO Auto-generated method stub
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

    // ******************************** Settings API ******************************************//

    public class Settings extends AsyncTask<Void , Void ,Void >{

        Functions function = new Functions();

        HashMap<String, String> result = new HashMap<String, String>();

        ArrayList localArrayList = new ArrayList();

        String PART_ID , OP_ID ;

        public Settings(String partID, String opID) {
            this.PART_ID = partID;
            this.OP_ID =opID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
              /* http://phphosting.osvin.net/waveaxisNew/Web_API/SettingChange.php?
              MachineId=11&PartId=4&OperatorId=1*/

            try {
                localArrayList.add(new BasicNameValuePair("PartId", this.PART_ID));
                localArrayList.add(new BasicNameValuePair("OperatorId",this.OP_ID));
                localArrayList.add(new BasicNameValuePair("MachineId", Constants.MACHINE_ID));

                result = function.Settings(localArrayList);

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
                String message = (String) result.get("MessageWhatHappen");
                if (response.equals("true")) {
                    isSuccess = true ;
                    showDialog(message);

                } else {

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
                db = new TransparentProgressDialog(Home.this,
                        R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ******************************* show Dialog ********************************************//
    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    Home.this).create();

            alertDialog.setTitle("Alert !");
            alertDialog.setMessage(msg);
            //alertDialog.setIcon(R.drawable.browse);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    dialog.cancel();
                    if(isSuccess){
                        Intent i = new Intent(Home.this , Home.class);
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
             db.dismiss();

            try {

                String response = (String)result.get("Response");
                if (response.equals("true")) {

                    Constants.MACHINE_ID = (String) result.get("machine_id");
                    Constants.MACHINE_NAME = (String) result.get("machine_name");

                    Constants.OEE = (String) result.get("equipment_effectiveness");
                    Constants.CP = (String) result.get("cp");
                    Constants.CPK = (String) result.get("cpk");
                    Constants.SPINDLE = (String) result.get("no_of_spindleRun");
                    Constants.QULAITY_ISSUE = (String) result.get("quality_issue");
                    Constants.PART_NUMBER = (String) result.get("default_part_no");
                    Constants.OPERATOR_NAME = (String) result.get("default_operator");
                    Constants.OPERATOR_ID = (String) result.get("default_operator_id");
                    Constants.PART_ID = (String) result.get("");
                    Constants.PART_IAMGE = (String) result.get("");

                    FillAllDetails();

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
                db = new TransparentProgressDialog(Home.this,
                        R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void FillAllDetails() {
        long date = System.currentTimeMillis();

        SimpleDateFormat dateToShow = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateToShow.format(date);

        current_date.setText(currentDate);
        Constants.CURRENT_DATE = currentDate;

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String timeString = sdf.format(date);

        current_time.setText(timeString);
        Constants.CURRENT_TIME = timeString;

        oee_quality.setVisibility(View.GONE);
        part_number_layout.setVisibility(View.GONE);
        cpp_cpk.setVisibility(View.GONE);
        spindle_run.setVisibility(View.GONE);


        // set adapter for part number and images

        final ArrayList<String> partList = new ArrayList<String>();


        for(int i=0;i<Constants.partsList.size();i++){

            partList.add(Constants.partsList.get(i).get("part_no")) ;
            if(Constants.PART_NUMBER.equals(Constants.partsList.get(i).get("part_no"))){
                partPosToSet = i;
            }
        }
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                partList);
        part_number_spinner.setAdapter(mAdapter1);
        part_number_spinner.setSelection(partPosToSet);

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


        // set adapter for operator name

        ArrayList<String> operatorList = new ArrayList<String>();


        for(int i=0;i<Constants.operatorList.size();i++) {
            operatorList.add(Constants.operatorList.get(i).get("operator_name")) ;
            if(Constants.OPERATOR_NAME.equals(Constants.operatorList.get(i).get("operator_name"))){

                opPosToSet = i;
            }
        }
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                operatorList);
        operator_name_spinner.setAdapter(mAdapter1);
        operator_name_spinner.setSelection(opPosToSet);




        ArrayList<String> menuItems = new ArrayList<String>();
        menuItems.add("Menu");
        menuItems.add("Setting Change");
        menuItems.add("Interruption");
        menuItems.add("Tool Life");
        menuItems.add("Production Rate");

        mAdapter = new MyAdapter(getApplicationContext(),
                menuItems);
        menu_spinner.setAdapter(mAdapter);


        msa.setText(Constants.MACHINE_NAME+" - " + Constants.MACHINE_ID);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (timerCount == 0) {

                    oee_quality.setVisibility(View.VISIBLE);
                    part_number_layout.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.GONE);

                    oee_quality.setText("Overall Equipment Effectiveness : " + Constants.OEE);
                } else if (timerCount == 1) {
                    oee_quality.setVisibility(View.GONE);
                    part_number_layout.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.VISIBLE);

                    spindle_value.setText("No of Spindle Run : " + Constants.SPINDLE);
                } else if (timerCount == 2) {
                    oee_quality.setVisibility(View.GONE);
                    part_number_layout.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.VISIBLE);

                    cp.setText("CP : " + Constants.CP);
                    cpk.setText("CPK : " + Constants.CPK);
                } else if (timerCount == 3) {

                    oee_quality.setVisibility(View.VISIBLE);
                    part_number_layout.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);

                    oee_quality.setText("Quality Issue : " + Constants.QULAITY_ISSUE);
                } else if (timerCount == 4) {
                    oee_quality.setVisibility(View.GONE);
                    part_number_layout.setVisibility(View.VISIBLE);
                    spindle_run.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);

                    part_value.setText(Constants.PART_NUMBER);
                    imageLoader.DisplayImage(Constants.PART_IAMGE, R.drawable.noimg, part_image);


                }

                timerCount++;
                if (timerCount == 5) {
                    timerCount = 0;
                }
                handler.postDelayed(this, 10000L);
            }
        }, 1000);
    }

}
