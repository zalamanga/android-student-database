package com.rapik.example.aplikasidatabase.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rapik.example.aplikasidatabase.config.DatabaseHelper;
public class MemberModel {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public MemberModel(Context c) {
        context = c;
    }
    public MemberModel open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    // Query insert data
    public long insert(String kelas, String nama, String jenis_kelamin, String
            tanggal_lahir) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.KELAS, kelas);
        contentValue.put(DatabaseHelper.NAMA, nama);
        contentValue.put(DatabaseHelper.JENIS_KELAMIN, jenis_kelamin);
        contentValue.put(DatabaseHelper.TANGGAL_LAHIR, tanggal_lahir);
        return database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }
    // Query ambil/read data
    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID,
                DatabaseHelper.KELAS, DatabaseHelper.NAMA,DatabaseHelper.JENIS_KELAMIN, DatabaseHelper.TANGGAL_LAHIR};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    // Query update data
    public int update(long _id, String kelas, String nama, String jenis_kelamin,
                      String tanggal_lahir) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KELAS, kelas);
        contentValues.put(DatabaseHelper.NAMA, nama);
        contentValues.put(DatabaseHelper.JENIS_KELAMIN, jenis_kelamin);
        contentValues.put(DatabaseHelper.TANGGAL_LAHIR, tanggal_lahir);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues,
                DatabaseHelper._ID + " = " + _id, null);
        return i;
    }
    // Query delete data
    public int delete(long _id) {
        return database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=?",
                new String[]{Long.toString(_id)});
    }
}
