package com.example.dbsederhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nrp, nama;
    private Button simpan, ambilData, updateData, deleteData;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper openDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = (EditText) findViewById(R.id.nrp);
        nama = (EditText) findViewById(R.id.nama);
        simpan = (Button) findViewById(R.id.simpan);
        ambilData = (Button) findViewById(R.id.ambilData);

        simpan.setOnClickListener(operasi);
        ambilData.setOnClickListener(operasi);
        updateData.setOnClickListener(operasi);
        deleteData.setOnClickListener(operasi);

        openDB = new SQLiteOpenHelper(this, "db.sql", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        dbku = openDB.getWritableDatabase();
        dbku.execSQL("create table if not exists mhs(nrp TEXT, nama TEXT);");
    }

    @Override
    protected void onStop() {
        dbku.close();
        openDB.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.simpan:simpan();
                break;

                case R.id.ambilData:ambilData();
                break;

                case R.id.updateData:update();
                break;

                case R.id.deleteData:update();
            }
        }
    };

    private void simpan() {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp", nrp.getText().toString());
        dataku.put("nama", nama.getText().toString());
        dbku.insert("mhs", null, dataku);
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_LONG).show();
    }

    private void ambilData() {
        Cursor cur = dbku.rawQuery("select * from mhs where nrp='" + nrp.getText().toString() + "'", null);

        if (cur.getCount() > 0) {
            Toast.makeText(this, "Data ditemukan sejumlah " + cur.getCount(), Toast.LENGTH_LONG).show();
            cur.moveToFirst();
            nama.setText(cur.getString(cur.getColumnIndex("nama")));
        }

        else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_LONG).show();
        }
    }

    private void update() {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp", nrp.getText().toString());
        dataku.put("nama", nama.getText().toString());
        dbku.update("mhs", dataku, "nrp='" + nrp.getText().toString() + "'", null);
        Toast.makeText(this, "Data Ter-update", Toast.LENGTH_LONG).show();
    }

    private void delete() {
        dbku.delete("mhs", "nrp='" + nrp.getText().toString() + "'", null);
        Toast.makeText(this, "Data Terhapus", Toast.LENGTH_LONG).show();
    }
}