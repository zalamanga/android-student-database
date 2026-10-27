package com.rapik.example.aplikasidatabase;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rapik.example.aplikasidatabase.config.UserModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText mViewUser, mViewPassword, mViewRepassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mViewUser =findViewById(R.id.et_username);
        mViewPassword =findViewById(R.id.et_passwordSignup);
        mViewRepassword =findViewById(R.id.et_passwordSignup2);
    }
    public void registerUser(View v) {
        String username = mViewUser.getText().toString().trim();
        String password = mViewPassword.getText().toString();
        String cnfpas = mViewRepassword.getText().toString();
        if (username.isEmpty() || username.length() > 80) {
            mViewUser.setError("Username wajib diisi, maksimal 80 karakter.");
            return;
        }
        if (password.length() < 8 || password.length() > 128) {
            mViewPassword.setError("Gunakan password 8 sampai 128 karakter.");
            return;
        }
        if (!password.equals(cnfpas)) {
            mViewRepassword.setError("Konfirmasi password tidak cocok.");
            return;
        }
        try (UserModel model = new UserModel(this).open()) {
            if (!model.addUser(username, password)) {
                mViewUser.setError("Username sudah ada atau akun belum dapat disimpan.");
                return;
            }
            makeText(this, "Akun demo lokal tersimpan. Silakan masuk.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (RuntimeException error) {
            makeText(this, "Akun lokal belum dapat disimpan.", Toast.LENGTH_SHORT).show();
        }
    }
    public void backlogin(View v) {
        finish();}
}
