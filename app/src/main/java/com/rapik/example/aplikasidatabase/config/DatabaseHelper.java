package com.rapik.example.aplikasidatabase.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    // Nama Tabel
    public static final String TABLE_NAME = "tbl_student";
    public static final String _ID = "_id";
    public static final String KELAS = "kelas";
    public static final String NAMA = "nama";
    public static final String JENIS_KELAMIN = "jk";
    public static final String TANGGAL_LAHIR = "tanggal_lahir";
    //tabel login
    public static final String TBL_LOGIN = "tbl_login";
    public static final String ID_LOGIN = "ID";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    static final String DB_NAME = "Akademik.DB";
    static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "("
            + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KELAS + " TEXT NOT NULL, "
            + NAMA + " TEXT," + JENIS_KELAMIN + " TEXT," + TANGGAL_LAHIR + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_login (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Version stays 1. Password upgrades update one matched row, never reset tables.
        onCreate(db);
    }

}

