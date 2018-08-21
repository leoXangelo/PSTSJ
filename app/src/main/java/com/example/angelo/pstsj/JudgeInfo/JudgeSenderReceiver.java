package com.example.angelo.pstsj.JudgeInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsj.MysqlConnect.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by MastahG on 3/19/2018.
 */

public class JudgeSenderReceiver extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAddress;
    String query;
    ListView lv;
    ProgressDialog pd;
    SwipeRefreshLayout refresh;
    String judge_name;
    public JudgeSenderReceiver(Context c, String urlAddress, String query, String judge_name) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.query = query;
        this.judge_name = judge_name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Pageant Scoring and Tabulation System");
        pd.setMessage("Searching...Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.sendAndReceive();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.dismiss();
        //refresh.setRefreshing(false);
        //RESET LISTVIEW
        //lv.setAdapter(null);

        if(s != null)
        {
            if(!s.contains("null"))
            {
                JudgeParser p = new JudgeParser(c, s, judge_name);
                p.execute();

                //noNetworkImg.setVisibility(View.INVISIBLE);
                //noDataImg.setVisibility(View.INVISIBLE);

            }else
            {
                //noNetworkImg.setVisibility(View.INVISIBLE);
                //noDataImg.setVisibility(View.VISIBLE);
                Toast.makeText(c,"No Data ",Toast.LENGTH_SHORT).show();
            }
        }else {
            //.setVisibility(View.VISIBLE);
            //noDataImg.setVisibility(View.INVISIBLE);
            Toast.makeText(c,"No network",Toast.LENGTH_SHORT).show();
        }

    }

    private String sendAndReceive()
    {
        HttpURLConnection con= Connector.connect(urlAddress);
        if(con==null)
        {
            return null;
        }

        try
        {
            OutputStream os=con.getOutputStream();

            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
            bw.write(new DataPackager(query).packageData());

            bw.flush();

            //RELEASE RES
            bw.close();
            os.close();

            //SOME RESPONSE ????
            int responseCode=con.getResponseCode();

            //DECODE
            if(responseCode==con.HTTP_OK)
            {
                //RETURN SOME DATA stream
                InputStream is=con.getInputStream();

                //READ IT
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response=new StringBuffer();

                if(br != null)
                {
                    while ((line=br.readLine()) != null)
                    {
                        response.append(line+"n");
                    }

                }else {
                    return null;
                }

                return response.toString();

            }else {
                return String.valueOf(responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}