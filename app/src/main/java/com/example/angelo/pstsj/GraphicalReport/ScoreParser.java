package com.example.angelo.pstsj.GraphicalReport;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsj.Formatter.DecimalFormatter;
import com.example.angelo.pstsj.Objects.Criteria;
import com.example.angelo.pstsj.Objects.ScoreSheet;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by MastahG on 3/19/2018.
 */

public class ScoreParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ListView lv;
    ArrayList<ScoreSheet> scoreSheets = new ArrayList<>();
    HorizontalBarChart chart;
    DatabaseHelper db;
    public ScoreParser(Context c, String data, HorizontalBarChart chart) {
        this.c = c;
        this.data = data;
        this.chart = chart;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer==1)
        {
            //BIND TO LISTVIEW
            //ArrayAdapter adapter=new ArrayAdapter(c,android.R.layout.simple_list_item_1,names);
            //CriteriaAdapter adapter = new CriteriaAdapter(c, criterias, ID, jname, pname, pgender);
            //lv.setAdapter(adapter);
            //current_score.setText(this.score+"");
            DecimalFormat twoDForm = new DecimalFormat("##.####");
            db = new DatabaseHelper(c);
            //score.setText(""+twoDForm.format(this.total_score) + "%");
            //scoreBar.setProgress((int)this.total_score);
            // mChart.setHighlightEnabled(false);


//        yr.setInverted(true);
            Cursor result = db.GetRank();
            if (result != null && result.getCount() > 0){
                int del = db.DelCurrentRank();
                //Toast.makeText(c, "Checking ranks...", Toast.LENGTH_SHORT).show();
            }
            //float total_score;
            ScoreSheet s;
            ArrayList<BarEntry> scores = new ArrayList<>();
            ArrayList<String> nameList = new ArrayList<>();
            int index = 0, ctr = 1, tmp = 0;
            int rank;
            float xx = 0;
            for (int x = 0; x < scoreSheets.size(); x++){
                s = scoreSheets.get(x);
                /*if (!s.getPname().equals(name)){
                    name = s.getPname();
                    nameList.add(index, s.getPname());
                    index++;
                }
                */
                if (s.getTotal() == xx){
                    ctr--;
                    rank = ctr;
                    ctr++;
                }else{
                    rank = ctr;
                    //tmp = rank;
                }
                nameList.add(x, s.getPname());
                scores.add(new BarEntry(x, s.getTotal()));
                Boolean res = db.SaveRank(s.getJname(), s.getPname(), rank, s.getTotal(), s.getEname());
                if (res == true){
                    //Toast.makeText(c, "Checking rank...", Toast.LENGTH_SHORT).show();
                }else {

                }
                xx = s.getTotal();
                ctr++;
                //total_score = total_score + ((float)s.getScore() * ((float)s.getCpercent() / 100));
            }

            /*
            for (int x = 0; x < nameList.size(); x++){
                total_score = 0;
                for (int i = 0; i < scoreSheets.size(); i++){
                    s = scoreSheets.get(i);
                    if (s.getPname().equals(nameList.get(x))){
                        total_score = total_score + ((float)s.getScore() * ((float)s.getCpercent() / 100));
                    }
                }
                scores.add(new BarEntry(x, total_score));
            }
            */
            //scores.add(new BarEntry(0, 99));
            //scores.add(new BarEntry(1, 96));
            //Create dataset
            BarDataSet dataSet = new BarDataSet(scores, "Participants");
            dataSet.setDrawValues(true);
            // Create a data object from the dataSet
            BarData data = new BarData(dataSet);
            // Format data as percentage
            data.setValueFormatter(new DecimalFormatter());

            chart.setDrawBarShadow(false);
            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart.setMaxVisibleValueCount(60);
            // scaling can now only be done on x- and y-axis separately
            chart.setPinchZoom(true);
            // draw shadows for each bar that show the maximum value
            // mChart.setDrawBarShadow(true);
            chart.setDrawGridBackground(false);

            XAxis xl = chart.getXAxis();
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setValueFormatter(new IndexAxisValueFormatter(nameList));
            xl.setDrawAxisLine(true);
            xl.setDrawGridLines(false);
            xl.setGranularity(1f);
            xl.setTextSize(18f);

            YAxis yl = chart.getAxisLeft();
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

            YAxis yr = chart.getAxisRight();
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(false);
            yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            // Make the chart use the acquired data
            chart.setData(data);
            // Display labels for bars
            //chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(nameList));
            // Set the maximum value that can be taken by the bars
            chart.getAxisLeft().setAxisMaximum(100);
            // Display scores inside the bars
            chart.setDrawValueAboveBar(false);
            // Hide grid lines
            chart.getAxisLeft().setEnabled(false);
            chart.getAxisRight().setEnabled(false);
            // Hide graph description
            chart.getDescription().setEnabled(true);
            // Hide graph legend
            chart.getLegend().setEnabled(true);

            // Design
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            data.setValueTextSize(13f);
            data.setValueTextColor(Color.DKGRAY);

            Description description = new Description();
            description.setText("Pageant Scoring and Tabulation System");
            //setData(12, 50);
            chart.setFitBars(true);
            chart.setDescription(description);
            chart.animateY(1200);
            dataSet.notifyDataSetChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        }else {
            Toast.makeText(c,"Unable to Parse",Toast.LENGTH_SHORT).show();
        }
    }

    private int parse()
    {
        //URL url = null;
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;

            scoreSheets.clear();
            ScoreSheet scoreSheet;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                //int score = jo.getInt("score");
                //int cpercent = jo.getInt("cpercent");
                String jname = jo.getString("jname");
                String ename = jo.getString("ename");
                String name = jo.getString("pname");
                String total = String.valueOf(jo.getDouble("total"));
                //this.score = jo.getInt("score");
                /*int id = jo.getInt("cid");
                String cname = jo.getString("cname");
                int cpercent = jo.getInt("cpercent");
                String ename = jo.getString("ename");

                criteria = new Criteria();

                //event.setEvent_ID(id);
                //event.setEvent_Name(ename);
                //event.setEvent_Date(edate);
                //event.setEvent_Place(eplace);
                criteria.setCriteria_ID(id);
                criteria.setCriteria_Name(cname);
                criteria.setCriteria_Percentage(cpercent);
                criteria.setEvent_Name(ename);

                criterias.add(criteria);
                //listEvent.add(ename);
                */
                scoreSheet = new ScoreSheet();
                //this.total_score = this.total_score + ((float)score * ((float)cpercent / 100));
                scoreSheet.setPname(name);
                scoreSheet.setJname(jname);
                scoreSheet.setEname(ename);
                //scoreSheet.setScore(score);
                //scoreSheet.setCpercent(cpercent);
                scoreSheet.setTotal(Float.parseFloat(total));
                scoreSheets.add(scoreSheet);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}