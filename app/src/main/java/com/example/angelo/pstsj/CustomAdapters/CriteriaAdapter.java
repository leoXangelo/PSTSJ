package com.example.angelo.pstsj.CustomAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsj.GetCurrentScore.ScoreSenderReceiver;
import com.example.angelo.pstsj.Objects.Criteria;
import com.example.angelo.pstsj.R;
import com.example.angelo.pstsj.RequestHandler.RequestHandler;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.util.ArrayList;
import java.util.HashMap;

public class CriteriaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Criteria> criteria;
    View bottomSheet;
    int ID;
    String jname, pname, pgender;
    DatabaseHelper db;
    public CriteriaAdapter(Context context, ArrayList<Criteria> criteria, int ID, String jname, String pname, String pgender){
        this.context = context;
        this.criteria = criteria;
        //this.bottomSheet = bottomSheet;
        this.ID = ID;
        this.jname = jname;
        this.pname = pname;
        this.pgender = pgender;
    }

    @Override
    public int getCount() {
        return criteria.size();
    }

    @Override
    public Object getItem(int position) {
        return criteria.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Criteria criterias = criteria.get(position);
        final TextView criteria_name, current_score;
        final EditText new_score;
        Button btnSubmit;
        db = new DatabaseHelper(context);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_scoresheet, null);
        }


        criteria_name = (TextView) convertView.findViewById(R.id.criteriaName);
        current_score = (TextView) convertView.findViewById(R.id.currentScore);
        new_score = (EditText) convertView.findViewById(R.id.newScore);
        btnSubmit = (Button) convertView.findViewById(R.id.btnSubmit);

        criteria_name.setText(criterias.getCriteria_Name());
        new_score.setText("");
        new_score.setHint(criterias.getCriteria_Percentage()+"%");
        current_score.setText("No score");
        final SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(context);
        final com.github.mikephil.charting.charts.HorizontalBarChart barChart = new HorizontalBarChart(context);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new_score.getText().toString().equals("")){
                    if (Integer.parseInt(new_score.getText().toString()) > 0 && Integer.parseInt(new_score.getText().toString()) <= criterias.getCriteria_Percentage()){
                        Score(ID,
                                jname,
                                pname,
                                pgender,
                                criterias.getCriteria_Name(),
                                criterias.getCriteria_Percentage(),
                                Integer.parseInt(new_score.getText().toString()),
                                criterias.getEvent_Name());
                        new ScoreSenderReceiver(context,
                                "http://" + GetServerIP() + "/psts/select_score.php",
                                "SELECT * FROM score_table WHERE jname = '" + jname + "' AND pname = '" + pname + "' AND pgender = '" + pgender + "' AND cname = '" + criterias.getCriteria_Name() + "' AND pid = '" + ID + "';",
                                current_score).execute();
                        new com.example.angelo.pstsj.GraphicalReport.ScoreSenderReceiver(context,
                                "http://" + GetServerIP() + "/psts/graphical_reports_judge.php",
                                "SELECT pid, jname, ename, pname, SUM(score) AS total FROM score_table WHERE pgender = '" + pgender + "' AND jname = '" + GetJudge() + "' AND sstatus = 'Active' GROUP BY pname ORDER BY total DESC;",
                                barChart,
                                swipeRefreshLayout).execute();
                        new_score.setText("");
                    }else{
                        Toast.makeText(context, "Invalid score", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Please specify your score", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new ScoreSenderReceiver(context,
                "http://" + GetServerIP() + "/psts/select_score.php",
                "SELECT * FROM score_table WHERE jname = '" + jname + "' AND pname = '" + pname + "' AND pgender = '" + pgender + "' AND cname = '" + criterias.getCriteria_Name() + "' AND pid = '" + ID + "';",
                current_score).execute();
        notifyDataSetChanged();
        return convertView;
    }


    private void Score(final int ID, final String judgeName, final String participantName, final String participantGender, final String cname, final int cpercent, final int score, final String ename){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context, "Pageant Scoring and Tabulation System", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("pid", String.valueOf(ID));
                data.put("jname", judgeName);
                data.put("pname", participantName);
                data.put("pgender", participantGender);
                data.put("cname", cname);
                data.put("cpercent", String.valueOf(cpercent));
                data.put("score", String.valueOf(score));
                data.put("ename", ename);
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/score_table.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
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
