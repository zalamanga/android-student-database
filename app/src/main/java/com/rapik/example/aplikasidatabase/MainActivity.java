package com.rapik.example.aplikasidatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.rapik.example.aplikasidatabase.config.Session;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Session.requireLogin(this)) return;
        setContentView(R.layout.activity_main);
    }
    public void logout(View v) {
        if (!Session.signOut(this)) {
            Toast.makeText(this, "Sesi belum dapat dihapus. Coba lagi.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
    public void openMembers(View v) {
        startActivity(new Intent(this, DataMemberActivity.class));
    }
}
