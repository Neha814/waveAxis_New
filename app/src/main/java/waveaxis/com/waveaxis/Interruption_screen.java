package waveaxis.com.waveaxis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sandeep on 17/11/15.
 */
public class Interruption_screen extends Activity {

    Spinner part_number_spinner, operator_name_spinner;

    MyAdapter1 mAdapter1 ;

    TextView msa ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.interruption_screen);

        inIt();
    }

    private void inIt() {
        operator_name_spinner = (Spinner) findViewById(R.id.operator_name_spinner);
        part_number_spinner = (Spinner) findViewById(R.id.part_number_spinner);
        msa = (TextView) findViewById(R.id.msa);

        ArrayList<String> partList = new ArrayList<String>();
        partList.add("Part Number");
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                partList);
        part_number_spinner.setAdapter(mAdapter1);

        ArrayList<String> operatorList = new ArrayList<String>();
        operatorList.add("Operator Name");
        mAdapter1 = new MyAdapter1(getApplicationContext(),
                operatorList);
        operator_name_spinner.setAdapter(mAdapter1);
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
