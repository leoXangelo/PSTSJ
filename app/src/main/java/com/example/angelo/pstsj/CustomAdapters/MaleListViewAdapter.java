package com.example.angelo.pstsj.CustomAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.angelo.pstsj.GetCriteria.CriteriaSenderReceiver;
import com.example.angelo.pstsj.GetScoreProgressbar.ScoreSenderReceiver;
import com.example.angelo.pstsj.Objects.Participant;
import com.example.angelo.pstsj.R;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.angelo.pstsj.ListviewHelper.Utility.setListViewHeightBasedOnChildren;

/**
 * Created by Angelo on 06/03/2018.
 */

public class MaleListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Participant> participants;
    View bottomSheet;
    DatabaseHelper db;
    ListView maleSheet;
    public MaleListViewAdapter(Context context, ArrayList<Participant> criteria, View bottomSheet, ListView maleSheet){
        this.context = context;
        this.participants = criteria;
        this.bottomSheet = bottomSheet;
        this.maleSheet = maleSheet;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object getItem(int position) {
        return participants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Participant participant = participants.get(position);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_male_list, null);
        }
        TextView num, name, course, age, gender, height, stat, txtScore;
        ImageView avatar;
        ListView criteriaSummary;
        db = new DatabaseHelper(context);
        criteriaSummary = (ListView) convertView.findViewById(R.id.criteriaSummary);
        avatar = (ImageView) convertView.findViewById(R.id.avatar);
        num = (TextView) convertView.findViewById(R.id.txtNum);
        name = (TextView) convertView.findViewById(R.id.txtName);
        /*age = (TextView) convertView.findViewById(R.id.txtAge);
        gender = (TextView) convertView.findViewById(R.id.txtGender);
        height = (TextView) convertView.findViewById(R.id.txtHeight);
        course = (TextView) convertView.findViewById(R.id.txtCourse);
        stat = (TextView) convertView.findViewById(R.id.txtStats);
        */
        Button scoresheet = (Button) convertView.findViewById(R.id.scoresheet);
        txtScore = (TextView) convertView.findViewById(R.id.txtScore);
        ProgressBar scoreProgressBar = (ProgressBar) convertView.findViewById (R.id.circularProgressBar);
        scoreProgressBar.setProgress(0);
        txtScore.setText("0%");
        /*
        new com.example.angelo.pstsj.GetParticipantCriteria.ScoreSenderReceiver(context,
                "http://" + GetServerIP() + "/psts/graphical_reports_judge.php",
                "SELECT * FROM score_table WHERE jname = '" + GetJudge() + "' AND pname = '" + participant.getP_Name() + "' AND pgender = '" + participant.getP_Gender() + "' AND pid = '" + participant.getP_Number()+"' AND sstatus = 'Active' ORDER BY cpercent DESC;",
                criteriaSummary).execute();
                */
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_DRAGGING:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        scoresheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    new CriteriaSenderReceiver(context,
                            "http://" + GetServerIP() + "/psts/select_criteria.php",
                            "",
                            maleSheet,
                            participant.getP_Number(),
                            GetJudge(),
                            participant.getP_Name(),
                            participant.getP_Gender()).execute();
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        new ScoreSenderReceiver(context,
                "http://" + GetServerIP() + "/psts/select_score.php",
                "SELECT cpercent, score FROM score_table WHERE jname = '" + GetJudge() + "' AND pname = '" + participant.getP_Name() + "' AND pgender = '" + participant.getP_Gender() + "' AND pid = '" + participant.getP_Number()+"' AND sstatus = 'Active';",
                txtScore,
                scoreProgressBar).execute();
        Picasso.get()
                .load("http://" + participant.getP_imgurl() + participant.getP_imgfolder())
                .placeholder(R.mipmap.person)
                .resize(768,1280)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.mipmap.person)
                .into(avatar);
        num.setText("#: "+participant.getP_Number()+"");
        name.setText(participant.getP_Name()+"");
        /*course.setText(participant.getP_Course()+"");
        age.setText(participant.getP_Age()+"");
        gender.setText(participant.getP_Gender()+"");
        height.setText(participant.getP_Height()+"");
        stat.setText(participant.getP_Vstat()+"");
        */
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
