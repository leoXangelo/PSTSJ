package com.example.angelo.pstsj.LoginMethod;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.angelo.pstsj.MainActivity;
import com.example.angelo.pstsj.R;
import com.example.angelo.pstsj.RequestHandler.RequestHandler;
import com.example.angelo.pstsj.SQLiteDatabase.DatabaseHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by MastahG on 3/31/2018.
 */

public class LoginActivity extends AppCompatActivity {
    Button btnCreateAccount, btnCreateLogin, btnSubmit, btnLogin, btnIP;
    public View bottomSheet, ipSheet;
    TextInputEditText create_firstname, create_name, create_username, create_password, confirm_password, username, password;
    BottomSheetBehavior behavior, ipbehavior;
    TextInputEditText server, admin;
    DatabaseHelper db;
    ImageButton showIP;
    TextView txtserver, txtdevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomSheet = (View) findViewById(R.id.bSheet);
        ipSheet = (View) findViewById(R.id.ipSheet);
        btnCreateLogin = (Button) findViewById(R.id.btnCreateLogin);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        showIP = (ImageButton) findViewById(R.id.showIP);
        btnLogin = (Button) findViewById(R.id.login);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        server = (TextInputEditText) findViewById(R.id.serverIP);
        admin = (TextInputEditText) findViewById(R.id.adminIP);
        //create_firstname = (TextInputEditText) findViewById(R.id.create_firstname);
        create_name = (TextInputEditText) findViewById(R.id.create_name);
        create_username = (TextInputEditText) findViewById(R.id.create_username);
        create_password = (TextInputEditText) findViewById(R.id.create_password);
        username = (TextInputEditText) findViewById(R.id.username);
        btnIP = (Button) findViewById(R.id.btnIP);
        password = (TextInputEditText) findViewById(R.id.password);
        txtserver = (TextView) findViewById(R.id.txtserver);
        txtdevice = (TextView) findViewById(R.id.txtdevice);
        behavior = BottomSheetBehavior.from(bottomSheet);
        ipbehavior = BottomSheetBehavior.from(ipSheet);
        db = new DatabaseHelper(this);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
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

        ipbehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_DRAGGING:
                        ipbehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
/*
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnCreateLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
 */

        showIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipbehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    ipbehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    ipbehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
/*
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Warning");
                if (create_name.getText().toString().equalsIgnoreCase("")){
                    alert.setMessage("Lastname is required");
                    alert.setCancelable(true);
                    AlertDialog build = alert.create();
                    build.show();
                    create_name.requestFocus();
                }else if (create_username.getText().toString().equalsIgnoreCase("")){
                    alert.setMessage("Username is required");
                    alert.setCancelable(true);
                    AlertDialog build = alert.create();
                    build.show();
                    create_username.requestFocus();
                }else if (create_password.getText().toString().equalsIgnoreCase("")){
                    alert.setMessage("Password is required");
                    alert.setCancelable(true);
                    AlertDialog build = alert.create();
                    build.show();
                    create_password.requestFocus();
                }else{
                    CreateAccount();
                }
            }
        });
*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAccount();
            }
        });

        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAdminIP();
                SaveServerIP();
                CurrentIP();
            }
        });

        server.setFilters(filters);
        admin.setFilters(filters);
        CurrentIP();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(ipbehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            ipbehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            ipbehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void CreateAccount(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Signing", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(true);
                    alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }else {
                                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                        }
                    });
                    alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog build = alert.create();
                    build.show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                data.put("auser", create_username.getText().toString());
                data.put("apass", create_password.getText().toString());
                data.put("atype", "Judge");
                data.put("aname", create_name.getText().toString());
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/create_user.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    private void LoginAccount(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Signing", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s.equals("Welcome")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(false);
                    alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ChangeAccountStatus();
                        }
                    });
                    AlertDialog build = alert.create();
                    build.show();
                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(false);
                    alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            username.requestFocus();
                        }
                    });
                    AlertDialog build = alert.create();
                    build.show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                data.put("auser", username.getText().toString());
                data.put("apass", password.getText().toString());
                data.put("atype", "Judge");
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/validate_user.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    private void ChangeAccountStatus(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Checking account", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s.equals("Account Verified")){
                    Toast.makeText(LoginActivity.this, "Account Verified", Toast.LENGTH_SHORT).show();
                    SaveAccount();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    startActivity(intent);
                    LoginActivity.this.finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("alogin", "Login");
                data.put("auser", username.getText().toString());
                data.put("apass", password.getText().toString());
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/update_account_login.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    public void SaveServerIP(){
        int ID = 0;
        if (!server.getText().toString().equals("")){
            Cursor server_list = db.GetServer();
            if (server_list != null && server_list.getCount() > 0){
                if (server_list.moveToNext()){
                    ID = server_list.getInt(0);
                }
                Boolean update = db.UpdateServer(String.valueOf(ID), server.getText().toString());
                if (update == true){
                    //Message here
                    //Toast.makeText(this, "Update Server", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }else{
                Boolean insert = db.SaveServerIP(server.getText().toString());
                if (insert == true){
                    //Message here
                    //Toast.makeText(this, "Insert Server", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }
        }
    }

    public void SaveAdminIP(){
        int ID = 0;
        if (!admin.getText().toString().equals("")){
            Cursor admin_list = db.GetAdmin();
            if (admin_list != null && admin_list.getCount() > 0){
                if (admin_list.moveToNext()){
                    ID = admin_list.getInt(0);
                }
                Boolean update = db.UpdateAdmin(String.valueOf(ID), admin.getText().toString());
                if (update == true){
                    //Message here
                    //Toast.makeText(this, "Update Admin", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }else{
                Boolean insert = db.SaveAdminIP(admin.getText().toString());
                if (insert == true){
                    //Message here
                    //Toast.makeText(this, "Insert Admin", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }
        }
    }

    public void SaveAccount(){
        int ID = 0;
        Cursor check_account = db.GetAccount();
        if (!username.getText().toString().equals("") && !password.getText().toString().equals("")){
            if (check_account != null && check_account.getCount() > 0){
                if (check_account.moveToNext()){
                    ID = check_account.getInt(0);
                }
                Boolean update = db.UpdateAccount(String.valueOf(ID), username.getText().toString(), password.getText().toString());
                if (update == true){
                    //Message here
                    //Toast.makeText(this, "Update Account", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }else {
                Boolean insert = db.SaveAccount(username.getText().toString(), password.getText().toString());
                if (insert == true){
                    //Message here
                    //Toast.makeText(this, "Insert Account", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }
        }
    }

    public void CurrentIP(){
        //fetch server IP
        Cursor server_list = db.GetServer();
        if (server_list != null && server_list.getCount() > 0){
            if (server_list.moveToNext()){
                txtserver.setText(server_list.getString(1));
            }
        }

        //fetch server IP
        Cursor admin_list = db.GetAdmin();
        if (admin_list != null && admin_list.getCount() > 0){
            if (admin_list.moveToNext()){
                txtdevice.setText(admin_list.getString(1));
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
}
