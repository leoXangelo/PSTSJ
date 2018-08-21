package com.example.angelo.pstsj.Methods;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.angelo.pstsj.GetMaleParticipant.ParticipantSenderReceiver;
import com.example.angelo.pstsj.JudgeInfo.JudgeSenderReceiver;
import com.example.angelo.pstsj.R;
import com.example.angelo.pstsj.RequestHandler.RequestHandler;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;

/**
 * Created by MastahG on 3/30/2018.
 */

public class MaleActivity extends Fragment {
    public View view, bottomSheet;
    ListView maleList, maleSheet;
    DatabaseHelper db;
    Button btnClose;
    private SwipeRefreshLayout swipeRefreshLayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_male, container, false);
        db = new DatabaseHelper(view.getContext());
        maleList = (ListView) view.findViewById(R.id.maleList);
        maleSheet = (ListView) view.findViewById(R.id.maleSheet);
        bottomSheet = (View) view.findViewById(R.id.bSheet);
        btnClose = (Button) view.findViewById(R.id.btnClose);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ParticipantSenderReceiver(view.getContext(),
                        "http://" + GetServerIP() + "/psts/select_participant.php",
                        "Male",
                        maleList,
                        swipeRefreshLayout,
                        bottomSheet,
                        maleSheet).execute();
            }
        });
        new ParticipantSenderReceiver(view.getContext(),
                "http://" + GetServerIP() + "/psts/select_participant.php",
                "Male",
                maleList,
                swipeRefreshLayout,
                bottomSheet,
                maleSheet).execute();
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    /*new ParticipantSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/select_participant.php",
                            "Male",
                            maleList,
                            swipeRefreshLayout,
                            bottomSheet,
                            maleSheet).execute();
                            */
                    Cursor res = db.GetRank();
                    if (res != null && res.getCount() > 0){
                        while (res.moveToNext()){
                            VerifyRank(res.getString(1),
                                    res.getString(2),
                                    res.getInt(3),
                                    res.getFloat(4),
                                    res.getString(5),
                                    "Male");
                        }
                    }
                    maleList.invalidateViews();
                    maleSheet.invalidateViews();
                }
            }
        });
        if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        return view;
    }

    private void VerifyRank(final String jname, final String pname, final int rank, final float score, final String ename, final String pgender){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MaleActivity.this.getActivity(), "Verifying", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    //Toast.makeText(context,"Rank success",Toast.LENGTH_LONG).show();
                }else {
                    //Toast.makeText(context,"Rank failed",Toast.LENGTH_LONG).show();
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
