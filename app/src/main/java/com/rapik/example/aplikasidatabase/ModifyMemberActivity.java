package com.rapik.example.aplikasidatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.rapik.example.aplikasidatabase.config.MemberInput;
import com.rapik.example.aplikasidatabase.config.Session;

import com.rapik.example.aplikasidatabase.model.MemberModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class ModifyMemberActivity extends AppCompatActivity {

    EditText etfn, etbirth;
    Spinner spclass;
    RadioGroup rgender;
    RadioButton rmale, rfemale;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter; //format tanggal
    MemberModel memberModel;
    long _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Session.requireLogin(this)) return;
        setContentView(R.layout.activity_modify_member);etfn = (EditText) findViewById(R.id.etfullname);
        spclass = (Spinner) findViewById(R.id.kelas);
        rgender = (RadioGroup) findViewById(R.id.rgender);
        rmale = (RadioButton) findViewById(R.id.rbmale);
        rfemale = (RadioButton) findViewById(R.id.rbfemale);
        etbirth = (EditText) findViewById(R.id.etbirth);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        memberModel = new MemberModel(this);
        memberModel.open();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String kelas = intent.getStringExtra("kelas");
        String nama = intent.getStringExtra("nama");
        String jenis_kelamin = intent.getStringExtra("jenis_kelamin");
        String tanggal_lahir = intent.getStringExtra("tanggal_lahir");
        try {
            _id = Long.parseLong(id);
            if (_id <= 0 || jenis_kelamin == null) throw new IllegalArgumentException();
        } catch (RuntimeException error) {
            Toast.makeText(this, "Data pilihan tidak valid.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        etfn.setText(nama);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        adapter.add("Pilih kelas");
        adapter.addAll(getResources().getStringArray(R.array.kelas));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spclass.setAdapter(adapter);
        int position = kelas == null ? -1 : adapter.getPosition(kelas);
        spclass.setSelection(position > 0 ? position : 0);
        if (position <= 0) {
            Toast.makeText(this, "Kelas lama tidak tersedia. Pilih kelas sebelum menyimpan.",
                    Toast.LENGTH_LONG).show();
        }
        if(jenis_kelamin.equals("Male")) rmale.setChecked(true);
        else rfemale.setChecked(true);
        etbirth.setText(tanggal_lahir);
    }
    public void updateMember(View v) {
        String fn = etfn.getText().toString();
        Object selectedClass = spclass.getSelectedItem();
        if (selectedClass == null || spclass.getSelectedItemPosition() <= 0) {
            Toast.makeText(this, "Pilih kelas sebelum menyimpan perubahan.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String yc = selectedClass.toString();
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
        if (memberModel.update(_id, yc, fn.trim(), g, birth) != 1) {
            Toast.makeText(this, "Data tidak ditemukan atau belum dapat diperbarui.", Toast.LENGTH_SHORT).show();
            return;
        }
        this.returnHome();
    }
    public void deleteMember(View v) {
        if (memberModel.delete(_id) != 1) {
            Toast.makeText(this, "Data tidak ditemukan atau belum dapat dihapus.", Toast.LENGTH_SHORT).show();
            return;
        }
        this.returnHome();
    }
    // Fungsi untuk kembali ke halaman awal
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                DataMemberActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
        finish();
    }
    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new
                DatePickerDialog.OnDateSetListener() {@Override
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
        showDateDialog();}
    @Override
    protected void onDestroy() {
        if (memberModel != null) memberModel.close();
        super.onDestroy();
    }
}
