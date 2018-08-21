package com.example.angelo.pstsj.CustomAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsj.GetCurrentScore.ScoreSenderReceiver;
import com.example.angelo.pstsj.Objects.Criteria;
import com.example.angelo.pstsj.Objects.ScoreSheet;
import com.example.angelo.pstsj.R;
import com.example.angelo.pstsj.RequestHandler.RequestHandler;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ParticipantCriteriaAdapter extends BaseAdapter {
    Context context;
    ArrayList<ScoreSheet> scoreSheets;
    ListView lv;
    DatabaseHelper db;
    public ParticipantCriteriaAdapter(Context context, ArrayList<ScoreSheet> scoreSheets, ListView lv){
        this.context = context;
        this.scoreSheets = scoreSheets;
        this.lv = lv;
        //this.bottomSheet = bottomSheet;
    }

    @Override
    public int getCount() {
        return scoreSheets.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreSheets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ScoreSheet scoreSheet = scoreSheets.get(position);
        db = new DatabaseHelper(context);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_participant_criteria, null);
        }
        DecimalFormat twoDForm = new DecimalFormat("#.###");

        TextView percentage, criteriaName;
        ProgressBar progress_limit;
        criteriaName = (TextView) convertView.findViewById(R.id.criteriaName);
        percentage = (TextView) convertView.findViewById(R.id.percentage);
        progress_limit = (ProgressBar) convertView.findViewById(R.id.progress_limit);
        progress_limit.setProgress(0);
        float total = 0;
        total = (float) scoreSheet.getScore() * ((float)scoreSheet.getCpercent() / 100);
        criteriaName.setText(scoreSheet.getCname());
        progress_limit.setMax(scoreSheet.cpercent);
        progress_limit.setProgress((int)total);
        percentage.setText(twoDForm.format(total)+"%");
        notifyDataSetChanged();
        return convertView;
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
}
