package com.rapik.example.aplikasidatabase.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserModel implements AutoCloseable {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public UserModel(Context c) {
        context = c;
    }
    public UserModel open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    public boolean addUser(String user, String password) {
        try (Cursor existing = database.query(DatabaseHelper.TBL_LOGIN,
                new String[]{DatabaseHelper.ID_LOGIN}, DatabaseHelper.USERNAME + "=?",
                new String[]{user}, null, null, null)) {
            if (existing.getCount() != 0) return false;
        }
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.USERNAME, user);
        contentValue.put(DatabaseHelper.PASSWORD, PasswordHasher.hash(password));
        return database.insert(DatabaseHelper.TBL_LOGIN, null, contentValue) != -1;
    }
    public boolean checkuser(String username, String password) {
        try (Cursor cursor = database.query(DatabaseHelper.TBL_LOGIN,
                new String[]{DatabaseHelper.ID_LOGIN, DatabaseHelper.PASSWORD},
                DatabaseHelper.USERNAME + "=?", new String[]{username}, null, null, null)) {
            // Older sample databases can contain duplicate names. Do not guess an account.
            if (cursor.getCount() != 1 || !cursor.moveToFirst()) return false;
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID_LOGIN));
            String stored = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PASSWORD));
            PasswordHasher.Verification result = PasswordHasher.verify(password, stored);
            if (!result.matches) return false;
            if (result.replacementHash != null) {
                ContentValues upgraded = new ContentValues();
                upgraded.put(DatabaseHelper.PASSWORD, result.replacementHash);
                return database.update(DatabaseHelper.TBL_LOGIN, upgraded,
                        DatabaseHelper.ID_LOGIN + "=? AND " + DatabaseHelper.PASSWORD + "=?",
                        new String[]{Long.toString(id), stored}) == 1;
            }
            return true;
        }
    }
}
