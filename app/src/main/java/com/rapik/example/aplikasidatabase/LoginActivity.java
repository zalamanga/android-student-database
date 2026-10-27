package com.rapik.example.aplikasidatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rapik.example.aplikasidatabase.config.UserModel;
import com.rapik.example.aplikasidatabase.config.Session;

public class LoginActivity extends AppCompatActivity {
    private EditText mViewUser, mViewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mViewUser=findViewById(R.id.et_username);
        mViewPassword =findViewById(R.id.et_password);
    }
    public void signin(View v) {
        String username = mViewUser.getText().toString().trim();
        String password = mViewPassword.getText().toString();
        if (username.isEmpty() || username.length() > 80
                || password.isEmpty() || password.length() > 128) {
            Toast.makeText(this, "Isi username dan password yang valid.", Toast.LENGTH_SHORT).show();
            return;
        }
        try (UserModel model = new UserModel(this).open()) {
            if (!model.checkuser(username, password)) {
                Toast.makeText(this, "Username atau password tidak cocok.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Session.signIn(this, username)) {
                Toast.makeText(this, "Sesi lokal belum dapat disimpan.", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } catch (RuntimeException error) {
            Toast.makeText(this, "Login lokal belum dapat diproses.", Toast.LENGTH_SHORT).show();
        }
    }
    public void signup(View v) {
        Intent registerintent = new Intent(this, RegisterActivity.class);
        startActivity(registerintent);
    }
}
