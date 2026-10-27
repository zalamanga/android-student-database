package com.rapik.example.aplikasidatabase.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.rapik.example.aplikasidatabase.LoginActivity;

public final class Session {
    private static final String FILE = "local_training_session";

    private Session() {}

    public static boolean signIn(Context context, String username) {
        return context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
                .putString("username", username).commit();
    }

    public static boolean signOut(Context context) {
        return context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static boolean requireLogin(Activity activity) {
        String username = activity.getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .getString("username", "");
        if (username != null && !username.isEmpty()) return true;
        activity.startActivity(new Intent(activity, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        activity.finish();
        return false;
    }
}
