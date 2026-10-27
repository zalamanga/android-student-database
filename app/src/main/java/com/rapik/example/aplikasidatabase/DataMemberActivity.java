package com.rapik.example.aplikasidatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rapik.example.aplikasidatabase.config.DatabaseHelper;
import com.rapik.example.aplikasidatabase.model.MemberModel;
import com.rapik.example.aplikasidatabase.config.Session;
public class DataMemberActivity extends AppCompatActivity {
    private MemberModel memberModel;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.KELAS, DatabaseHelper.NAMA,
            DatabaseHelper.JENIS_KELAMIN, DatabaseHelper.TANGGAL_LAHIR };
    final int[] to = new int[] { R.id.tvid, R.id.tvkelas, R.id.tvnama,
            R.id.tvjenis_kelamin, R.id.tvtanggal_lahir };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Session.requireLogin(this)) return;
        setContentView(R.layout.activity_data_member);memberModel = new MemberModel(this);
        memberModel.open();
        Cursor cursor = memberModel.fetch();
        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
/* Adapter untuk menunjuk data di database
untuk di tampilkan dalam list member yang mana data tersebut
menunjuk ke fragment dari ListView */
        adapter = new SimpleCursorAdapter(this, R.layout.activity_fragment,
                cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
// OnCLickListiner untuk Data Member yang telah ada di database
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.tvid);
                TextView kelasTextView = (TextView)
                        view.findViewById(R.id.tvkelas);
                TextView namaTextView = (TextView) view.findViewById(R.id.tvnama);
                TextView jenisKelaminTextView = (TextView)
                        view.findViewById(R.id.tvjenis_kelamin);
                TextView tanggalLahirTextView = (TextView)
                        view.findViewById(R.id.tvtanggal_lahir);
                String id = idTextView.getText().toString();
                String kelas = kelasTextView.getText().toString();
                String nama = namaTextView.getText().toString();
                String jenis_kelamin = jenisKelaminTextView.getText().toString();
                String tanggal_lahir = tanggalLahirTextView.getText().toString();
// Proses Intent untuk mengirim data ke halaman Edit
                Intent modify_intent = new Intent(getApplicationContext(),ModifyMemberActivity.class);
                modify_intent.putExtra("kelas", kelas);
                modify_intent.putExtra("nama", nama);
                modify_intent.putExtra("jenis_kelamin", jenis_kelamin);
                modify_intent.putExtra("tanggal_lahir", tanggal_lahir);
                modify_intent.putExtra("id", id);
                startActivity(modify_intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_record) {
            Intent add_mem = new Intent(this, AddMemberActivity.class);
            startActivity(add_mem);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        if (adapter != null) adapter.changeCursor(null);
        if (memberModel != null) memberModel.close();
        super.onDestroy();
    }
}
