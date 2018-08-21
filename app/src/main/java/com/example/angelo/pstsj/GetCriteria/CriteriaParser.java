package com.example.angelo.pstsj.GetCriteria;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsj.CustomAdapters.CriteriaAdapter;
import com.example.angelo.pstsj.Objects.Criteria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MastahG on 3/19/2018.
 */

public class CriteriaParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ListView lv;
    ArrayList<Criteria> criterias = new ArrayList<>();
    int ID;
    String jname, pname, pgender;
    public CriteriaParser(Context c, String data, ListView lv, int ID, String jname, String pname, String pgender) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.ID = ID;
        this.jname = jname;
        this.pname = pname;
        this.pgender = pgender;
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
            CriteriaAdapter adapter = new CriteriaAdapter(c, criterias, ID, jname, pname, pgender);
            lv.setAdapter(adapter);

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

            criterias.clear();
            Criteria criteria;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int id = jo.getInt("cid");
                String cname = jo.getString("cname");
                int cpercent = jo.getInt("cpercent");
                String ename = jo.getString("ename");
                String cstatus = jo.getString("cstatus");

                criteria = new Criteria();

                criteria.setCriteria_ID(id);
                criteria.setCriteria_Name(cname);
                criteria.setCriteria_Percentage(cpercent);
                criteria.setEvent_Name(ename);
                criteria.setCriteria_Status(cstatus);

                criterias.add(criteria);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}