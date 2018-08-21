package com.example.angelo.pstsj.GetParticipantCriteria;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsj.CustomAdapters.ParticipantCriteriaAdapter;
import com.example.angelo.pstsj.ListviewHelper.Utility;
import com.example.angelo.pstsj.Objects.ScoreSheet;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.angelo.pstsj.ListviewHelper.Utility.setListViewHeightBasedOnChildren;

/**
 * Created by MastahG on 3/19/2018.
 */

public class ScoreParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ListView lv;
    ArrayList<ScoreSheet> scoreSheets = new ArrayList<>();
    HorizontalBarChart chart;
    public ScoreParser(Context c, String data, ListView lv) {
        this.c = c;
        this.data = data;
        this.lv = lv;
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
            ParticipantCriteriaAdapter adapter = new ParticipantCriteriaAdapter(c, scoreSheets, lv);
            lv.setAdapter(adapter);
            setListViewHeightBasedOnChildren(lv);
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
                int score = jo.getInt("score");
                int cpercent = jo.getInt("cpercent");
                String name = jo.getString("pname");
                String cname = jo.getString("cname");
                scoreSheet = new ScoreSheet();
                //this.total_score = this.total_score + ((float)score * ((float)cpercent / 100));
                scoreSheet.setPname(name);
                scoreSheet.setScore(score);
                scoreSheet.setCpercent(cpercent);
                scoreSheet.setCname(cname);
                scoreSheets.add(scoreSheet);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}