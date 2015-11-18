package waveaxis.com.waveaxis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

import imageloader.ImageLoader;
import utils.NetConnection;

/**
 * Created by sandeep on 17/11/15.
 */
public class Home extends Activity implements View.OnClickListener {

    Spinner menu_spinner, part_number_spinner, operator_name_spinner;

    MyAdapter mAdapter ;

    MyAdapter1 mAdapter1 ;

    LinearLayout start_layout;

    Button start_button;

    TextView oee_quality;

    LinearLayout part_number_layout , cpp_cpk, spindle_run;

    int timerCount = 0;

    boolean isConnected ;

    ImageLoader imageLoader;

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

        oee_quality.setVisibility(View.GONE);
        part_number_layout.setVisibility(View.GONE);
        cpp_cpk.setVisibility(View.GONE);
        spindle_run.setVisibility(View.GONE);

        // set adapter for part number and images

        ArrayList<String> partList = new ArrayList<String>();
        partList.add("Part Number");
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                partList);
        part_number_spinner.setAdapter(mAdapter1);

        // set adapter for operator name

        ArrayList<String> operatorList = new ArrayList<String>();
        operatorList.add("Operator Name");
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                operatorList);
        operator_name_spinner.setAdapter(mAdapter1);


        ArrayList<String> menuItems = new ArrayList<String>();
        menuItems.add("Menu");
        menuItems.add("Setting Change");
        menuItems.add("Interruption");
        menuItems.add("Tool Life");
        menuItems.add("Production Rate");

        mAdapter = new MyAdapter(getApplicationContext(),
                menuItems);
        menu_spinner.setAdapter(mAdapter);

        start_button.setOnClickListener(this);
        start_layout.setOnClickListener(this);

       menu_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if (i == 1) {
                   showSettingDialog();
               } else if (i == 2) {
                   Intent intent = new Intent(Home.this, Interruption_screen.class);
                   startActivity(intent);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });


       final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Log.e("timer==>", "" + timerCount);
                if (timerCount == 0) {

                    oee_quality.setVisibility(View.VISIBLE);
                    part_number_layout.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.GONE);

                    oee_quality.setText("Overall Equipment Effectiveness 86.35%");
                }

                else if (timerCount == 1) {
                    oee_quality.setVisibility(View.GONE);
                    part_number_layout.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.VISIBLE);
                }

                else if (timerCount == 2) {
                    oee_quality.setVisibility(View.GONE);
                    part_number_layout.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.VISIBLE);
                }

                else if (timerCount == 3) {

                    oee_quality.setVisibility(View.VISIBLE);
                    part_number_layout.setVisibility(View.GONE);
                    spindle_run.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);

                    oee_quality.setText("Quality Issue");
                }

                else if(timerCount == 4){
                    oee_quality.setVisibility(View.GONE);
                    part_number_layout.setVisibility(View.VISIBLE);
                    spindle_run.setVisibility(View.GONE);
                    cpp_cpk.setVisibility(View.GONE);

                }

                timerCount++;
                if (timerCount == 5) {
                    timerCount = 0;
                }
                handler.postDelayed(this, 10000L);
            }
        }, 1000);


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

            save_layout = (LinearLayout) dialog.findViewById(R.id.save_layout);
            save_button = (Button) dialog.findViewById(R.id.save_button);
            next_layout = (LinearLayout) dialog.findViewById(R.id.next_layout);
            next_button = (Button) dialog.findViewById(R.id.next_button);

            save_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   dialog.dismiss();
                }
            });
            save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        if(view==start_button || view == start_layout){
            Intent i = new Intent(Home.this , Interruption_screen.class);
            startActivity(i);
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
}
