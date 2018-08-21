package com.example.angelo.pstsj.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pstsj";
    private static final String SERVERADDRESS = "CREATE TABLE tb_server " +
            "(" +
            "serverID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "serverIP TEXT" +
            ")";
    private static final String ADMINADDRESS = "CREATE TABLE tb_admin " +
            "(" +
            "adminID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "adminIP TEXT" +
            ")";

    private static final String JUDGENAME = "CREATE TABLE tb_judge " +
            "(" +
            "judgeID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "judgeName TEXT" +
            ")";

    private static final String ACCOUNT = "CREATE TABLE tb_account " +
            "(" +
            "accountID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "accountUsername TEXT, " +
            "accountPassword TEXT" +
            ")";

    private static final String RANK = "CREATE TABLE tb_rank " +
            "(" +
            "rankID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "judgeName TEXT, " +
            "participantName TEXT, " +
            "rank INTEGER, " +
            "score FLOAT, " +
            "eventName TEXT" +
            ")";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SERVERADDRESS);
        db.execSQL(ADMINADDRESS);
        db.execSQL(ACCOUNT);
        db.execSQL(JUDGENAME);
        db.execSQL(RANK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "tb_server");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_admin");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_account");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_judge");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_rank");
    }

    public boolean SaveRank(String judgeName, String participantName, int rank, float score, String eventName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judgeName", judgeName);
        cv.put("participantName", participantName);
        cv.put("rank", rank);
        cv.put("score", score);
        cv.put("eventName", eventName);
        long result = db.insert("tb_rank", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Integer DelCurrentRank(){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("tb_rank", "", null);
        return i;
    }

    public boolean SaveServerIP(String server){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("serverIP", server);
        long result = db.insert("tb_server", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveJudge(String judge){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judgeName", judge);
        long result = db.insert("tb_judge", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveAdminIP(String server){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("adminIP", server);
        long result = db.insert("tb_admin", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveAccount(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("accountUsername", username);
        cv.put("accountPassword", password);
        long result = db.insert("tb_account", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean UpdateServer(String ID, String serverIP){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("serverIP", serverIP);
        int result = db.update("tb_server", cv, "serverID =?", new String[]{ID});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateAdmin(String ID, String adminIP){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("adminIP", adminIP);
        int result = db.update("tb_admin", cv, "adminID =?", new String[]{ID});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateAccount(String ID, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("accountUsername", username);
        cv.put("accountPassword", password);
        int result = db.update("tb_account", cv, "accountID =?", new String[]{ID});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateJudge(String ID, String judge){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judgeName", judge);
        int result = db.update("tb_judge", cv, "judgeID =?", new String[]{ID});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public Cursor GetServer(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_server", null);
        return res;
    }

    public Cursor GetAdmin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_admin", null);
        return res;
    }

    public Cursor GetAccount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_account", null);
        return res;
    }

    public Cursor GetJudge(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_judge", null);
        return res;
    }

    public Cursor GetRank(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_rank", null);
        return res;
    }
}
