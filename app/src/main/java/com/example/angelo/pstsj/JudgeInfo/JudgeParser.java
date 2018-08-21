package com.example.angelo.pstsj.JudgeInfo;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;
import android.widget.Toast;


import com.example.angelo.pstsj.Objects.Judge;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MastahG on 3/19/2018.
 */

public class JudgeParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    String judge_name;
    ArrayList<Judge> judges = new ArrayList<>();
    DatabaseHelper db;
    public JudgeParser(Context c, String data, String judge_name) {
        this.c = c;
        this.data = data;
        this.judge_name = judge_name;
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
            //JudgeAdapter adapter = new JudgeAdapter(c, judges);
            //lv.setAdapter(adapter);
            Judge judge = judges.get(0);
            judge_name = judge.getJudge_name();
            db = new DatabaseHelper(c);
            SaveJudge(judge_name);
            //Toast.makeText(c,"Unable to Parse " + judge_name,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(c,"Unable to Parse",Toast.LENGTH_SHORT).show();
        }
    }

    private int parse() {
        //URL url = null;
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;

            judges.clear();
            Judge judge;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int id = jo.getInt("aid");
                String aname = jo.getString("aname");
                String atype = jo.getString("atype");
                String astatus = jo.getString("astatus");
                String alogin = jo.getString("alogin");

                judge = new Judge();

                judge.setID(id);
                judge.setJudge_name(aname);
                judge.setJudge_type(atype);
                judge.setJudge_status(astatus);
                judge.setJudge_login(alogin);

                judges.add(judge);
                //listEvent.add(ename);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void SaveJudge(String judgeName){
        Cursor judge_list = db.GetJudge();
        int ID = 0;
        if (judge_list != null && judge_list.getCount() > 0){
            if (judge_list.moveToNext()){
                ID = judge_list.getInt(0);
            }
            Boolean update = db.UpdateJudge(String.valueOf(ID), judgeName);
            if (update == true){
                //Toast.makeText(c, "Judge Update", Toast.LENGTH_SHORT).show();
            }else{
            }
        }else{
            Boolean insert = db.SaveJudge(judgeName);
            if (insert == true){
                //Toast.makeText(c, "Judge Insert", Toast.LENGTH_SHORT).show();
            }else{

            }
        }
    }
}