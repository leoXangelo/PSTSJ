package com.example.angelo.pstsj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsj.JudgeInfo.JudgeSenderReceiver;
import com.example.angelo.pstsj.LoginMethod.LoginActivity;
import com.example.angelo.pstsj.Methods.FemaleActivity;
import com.example.angelo.pstsj.Methods.GraphicalReportsActivity;
import com.example.angelo.pstsj.Methods.MaleActivity;
import com.example.angelo.pstsj.RequestHandler.RequestHandler;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String judge_name = "", username = "", password = "";
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        db = new DatabaseHelper(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        JudgeSenderReceiver judgeSenderReceiver = new JudgeSenderReceiver(this,
                "http://" + GetServerIP() + "/psts/judge_info.php",
                "SELECT * FROM accounts WHERE auser = '" + username + "' AND apass = md5('" + password + "');",
                judge_name);
        judgeSenderReceiver.execute();

        //Toast.makeText(this, GetJudge(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            UpdateAccount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return new MaleActivity();
                case 1:
                    return new FemaleActivity();
                    /*
                case 2:
                    return new GraphicalReportsActivity();
                    */
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
    private void UpdateAccount(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Logout", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    //Toast.makeText(MainActivity.this, "Account update " + s,Toast.LENGTH_LONG).show();
                    if (s.equalsIgnoreCase("Success")){
                        SaveAccount();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "No network",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                data.put("status", "Logout");
                data.put("txtuser", GetUsername());
                data.put("txtpass", GetPassword());
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/judge_logout.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
    public void SaveAccount(){
        int ID = 0;
        Cursor check_account = db.GetAccount();
        if (check_account != null && check_account.getCount() > 0){
            if (check_account.moveToNext()){
                ID = check_account.getInt(0);
            }
            Boolean update = db.UpdateAccount(String.valueOf(ID), "", "");
            if (update == true){
                //Message here
                //Toast.makeText(this, "Update Account", Toast.LENGTH_SHORT).show();
            }else{
                //Message here
            }
        }else {
            Boolean insert = db.SaveAccount("", "");
            if (insert == true){
                //Message here
                //Toast.makeText(this, "Insert Account", Toast.LENGTH_SHORT).show();
            }else{
                //Message here
            }
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

    public String GetUsername(){
        String username = "";
        Cursor account = db.GetAccount();
        if (account.moveToNext()){
            username = account.getString(1);
        }
        return username;
    }

    public String GetPassword(){
        String password = "";
        Cursor account = db.GetAccount();
        if (account.moveToNext()){
            password = account.getString(2);
        }
        return password;
    }
}
