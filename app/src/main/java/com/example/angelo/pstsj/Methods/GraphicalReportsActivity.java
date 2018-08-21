package com.example.angelo.pstsj.Methods;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.angelo.pstsj.GraphicalReport.ScoreSenderReceiver;
import com.example.angelo.pstsj.R;
import com.example.angelo.pstsj.RequestHandler.RequestHandler;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphicalReportsActivity extends Fragment {
    public View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected HorizontalBarChart mChart;
    DatabaseHelper db;
    Button submitRank;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_graphical_reports, container, false);
        db = new DatabaseHelper(view.getContext());
        mChart = (HorizontalBarChart) view.findViewById(R.id.chart1);
        submitRank = (Button) view.findViewById(R.id.submitRank);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        final Spinner spinnerCustom = (Spinner) view.findViewById(R.id.spinnerCustom);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        ArrayList<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(view.getContext(),gender);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                new ScoreSenderReceiver(getActivity(),
                        "http://" + GetServerIP() + "/psts/graphical_reports_judge.php",
                        "SELECT pid, jname, ename, pname, SUM(score * (cpercent/100)) AS total FROM score_table WHERE pgender = '" + item + "' AND jname = '" + GetJudge() + "' AND sstatus = 'Active' GROUP BY pname ORDER BY total DESC;",
                        mChart,
                        swipeRefreshLayout).execute();
                //Toast.makeText(view.getContext(), item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //final String x = mChart.getXAxis().getValueFormatter().getFormattedValue(e.getY(), mChart.getXAxis());
                //Toast.makeText(view.getContext(), x, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        submitRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Cursor res = db.GetRank();
                String result = "";
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        Toast.makeText(view.getContext().getApplicationContext(),
                                res.getString(1) + res.getString(2) + res.getInt(3) + res.getFloat(4) + res.getString(5),
                                Toast.LENGTH_LONG).show();
                    }
                }
                */

                Cursor res = db.GetRank();
                if (res != null && res.getCount() > 0){
                    while (res.moveToNext()){
                        VerifyRank(res.getString(1),
                                res.getString(2),
                                res.getInt(3),
                                res.getFloat(4),
                                res.getString(5),
                                spinnerCustom.getSelectedItem().toString());
                    }
                }
            }
        });
        return view;
    }
    private void VerifyRank(final String jname, final String pname, final int rank, final float score, final String ename, final String pgender){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Signing", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }else {

                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                //data.put("auser", username.getText().toString());
                //data.put("apass", password.getText().toString());
                //data.put("atype", "Judge");
                //String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/validate_user.php",data);

                data.put("jname", jname);
                data.put("pname", pname);
                data.put("pgender", pgender);
                data.put("rank", String.valueOf(rank));
                data.put("score", String.valueOf(score));
                data.put("ename", ename);
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/save_rank.php",data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }



        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(24);
            txt.setTypeface(txt.getTypeface(), Typeface.BOLD);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#75CCED"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(24);
            txt.setTypeface(txt.getTypeface(), Typeface.BOLD);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_circle_black_24dp, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#75CCED"));
            return  txt;
        }

    }

    public String GetServerIP(){
        //fetch server IP
        String IP = "";
        Cursor server_list = db.GetServer();
        if (server_list != null && server_list.getCount() > 0){
            if (server_list.moveToNext()){
                IP = server_list.getString(1);
            }
        }
        return IP;
    }

    public String GetJudge(){
        String name = "";
        Cursor judge_name = db.GetJudge();
        if (judge_name != null && judge_name.getCount() > 0){
            if (judge_name.moveToNext()){
                name = judge_name.getString(1);
            }
        }
        return name;
    }
}
