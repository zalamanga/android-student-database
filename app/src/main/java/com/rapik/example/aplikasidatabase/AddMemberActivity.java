package com.rapik.example.aplikasidatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.rapik.example.aplikasidatabase.model.MemberModel;
import com.rapik.example.aplikasidatabase.config.MemberInput;
import com.rapik.example.aplikasidatabase.config.Session;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class AddMemberActivity extends AppCompatActivity {
    EditText etfn, etbirth;
    Spinner spclass;
    RadioGroup rgender;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter; //format tanggal
    MemberModel memberModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Session.requireLogin(this)) return;
        setContentView(R.layout.activity_add_member);
        etfn = (EditText) findViewById(R.id.etfullname);
        spclass = (Spinner) findViewById(R.id.kelas);
        rgender = (RadioGroup) findViewById(R.id.rgender);
        etbirth = (EditText) findViewById(R.id.etbirth);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        memberModel = new MemberModel(this);
        memberModel.open();
    }
    public void saveMember(View v) {try {
        String fn = etfn.getText().toString();
        String yc = spclass.getSelectedItem().toString();
        int gender = rgender.getCheckedRadioButtonId();
        if (gender == -1) {
            Toast.makeText(this, "Pilih jenis kelamin.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton rg = (RadioButton) findViewById(gender);
        String g = rg.getText().toString();
        String birth = etbirth.getText().toString();
        String error = MemberInput.validate(fn, yc, g, birth);
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (memberModel.insert(yc, fn.trim(), g, birth) == -1) {
            Toast.makeText(this, "Data belum dapat disimpan.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Data has been saved successfully",
                Toast.LENGTH_LONG).show();
        Intent main = new Intent(this,
                DataMemberActivity.class)

                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);
        finish();
    }
    catch (Exception e) {
        Toast.makeText(this, "Data belum dapat disimpan.", Toast.LENGTH_LONG).show();
    }
    }
    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int
                            dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etbirth.setText(dateFormatter.format(newDate.getTime()));
                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    public void showDate(View v) {
        showDateDialog();
    }
    @Override
    protected void onDestroy() {
        if (memberModel != null) memberModel.close();
        super.onDestroy();
    }
}
