package com.example.kontakhp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ImageView add, edit, find, del;
    private KontakAdapter kAdapter;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper dbopen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);
        add = (ImageView) findViewById(R.id.add);
        edit = (ImageView) findViewById(R.id.edit);
        find = (ImageView) findViewById(R.id.find);
        del = (ImageView) findViewById(R.id.del);

        add.setOnClickListener(operasi);
        edit.setOnClickListener(operasi);
        find.setOnClickListener(operasi);
        del.setOnClickListener(operasi);

        ArrayList<Kontak> listKontak = new ArrayList<>();
        kAdapter = new KontakAdapter(this, 0, listKontak);
        lv.setAdapter(kAdapter);

        dbopen = new SQLiteOpenHelper(this, "kontak.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        dbku = dbopen.getWritableDatabase();
        dbku.execSQL("create table if not exists kontak(nama TEXT, nohp TEXT);");

        get_item();
        sort_list();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:add_data();
                break;

                case R.id.edit:edit_data();
                break;

                case R.id.find:find_data();
                break;

                case R.id.del:delete_data();
                break;
            }
        }
    };

    private void sort_list() {
        kAdapter.sort(new Comparator<Kontak>() {
            @Override
            public int compare(Kontak kontak, Kontak t1) {
                return kontak.getNama().compareTo(t1.getNama());
            }
        });
    }

    private void get_item() {
        Cursor cur = dbku.rawQuery("select nama, nohp from kontak", null);
        Toast.makeText(this, "Terdapat sejumlah " + cur.getCount(), Toast.LENGTH_LONG).show();

        int i = 0;

        if (cur.getCount() > 0) {
            cur.moveToFirst();
        }

        while (i < cur.getCount()) {
            insertKontak(cur.getString(cur.getColumnIndex("nama")), cur.getString(cur.getColumnIndex("nohp")));
            cur.moveToNext();
            i++;
        }

        cur.close();
    }

    private void add_item (String nm, String hp) {
        ContentValues datanya = new ContentValues();
        datanya.put("nama", nm);
        datanya.put("nohp", hp);
        dbku.insert("kontak", null, datanya);

        Kontak newKontak = new Kontak(nm, hp);
        kAdapter.add(newKontak);
    }

    private void edit_item (String oldName, String oldHp, String nm, String hp) {
        ContentValues datanya = new ContentValues();
        datanya.put("nama", nm);
        datanya.put("nohp", hp);

        dbku.update("kontak", datanya, "nama='" + oldName + "' and nohp='" + oldHp + "'", null);
        Toast.makeText(this, "Data berhasil diganti", Toast.LENGTH_LONG).show();
    }

    private void delete_item (String nm, String hp) {
        dbku.delete("kontak", "nama='" + nm + "' and nohp='" + hp + "'", null);
        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_LONG).show();
    }

    private void find_item(String nm) {
        Cursor cur = dbku.rawQuery("select nama, nohp from kontak where nama LIKE '%" + nm + "%'", null);
        Toast.makeText(this, "Terdapat sejumlah " + cur.getCount(), Toast.LENGTH_LONG).show();

        int i = 0;

        if (cur.getCount() > 0) {
            cur.moveToFirst();
        }

        while (i < cur.getCount()) {
            insertKontak(cur.getString(cur.getColumnIndex("nama")), cur.getString(cur.getColumnIndex("nohp")));
            cur.moveToNext();
            i++;
        }

        cur.close();
    }

    private void insertKontak(String nm, String hp) {
        Kontak newKontak = new Kontak(nm, hp);
        kAdapter.add(newKontak);
    }

    private void add_data() {
        AlertDialog.Builder buat = new AlertDialog.Builder(this);
        buat.setTitle("Add Kontak");

        View vAdd = LayoutInflater.from(this).inflate(R.layout.add_kontak, null);
        final EditText nm = (EditText) vAdd.findViewById(R.id.e_nm);
        final EditText hp = (EditText) vAdd.findViewById(R.id.e_hp);

        buat.setView(vAdd);
        nm.setText("");
        hp.setText("");

        // Set up the buttons
        buat.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_item(nm.getText().toString(), hp.getText().toString());
                Toast.makeText(getBaseContext(), "Data Tersimpan", Toast.LENGTH_LONG).show();

                dialog.dismiss();
                sort_list();
            }
        });

        buat.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        buat.show();
    }

    private void edit_data() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder ubah = new AlertDialog.Builder(MainActivity.this);
                ubah.setTitle("Edit Kontak");

                View vEdit = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_kontak, null);
                final EditText nm = (EditText) vEdit.findViewById(R.id.e_nm);
                final EditText hp = (EditText) vEdit.findViewById(R.id.e_hp);
                final TextView tNama = (TextView) view.findViewById(R.id.tNama);
                final TextView tHp = (TextView) view.findViewById(R.id.tnoHp);

                nm.setText(tNama.getText());
                hp.setText(tHp.getText());

                ubah.setView(vEdit);

                // Set up the buttons
                ubah.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        edit_item(tNama.getText().toString(), tHp.getText().toString(), nm.getText().toString(), hp.getText().toString());
                        kAdapter.remove(kAdapter.getItem(position));
                        Kontak newKontak = new Kontak(nm.getText().toString(), hp.getText().toString());
                        kAdapter.add(newKontak);

                        dialog.dismiss();
                        sort_list();
                    }
                });

                ubah.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

                ubah.show();
            }
        });
    }

    private void delete_data() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder hapus = new AlertDialog.Builder(MainActivity.this);
                hapus.setTitle("Hapus Kontak");

                View vHapus = LayoutInflater.from(MainActivity.this).inflate(R.layout.delete_kontak, null);
                final TextView nm = (TextView) view.findViewById(R.id.tNama);
                final TextView hp = (TextView) view.findViewById(R.id.tnoHp);

                hapus.setView(vHapus);

                // Set up the buttons
                hapus.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        delete_item(nm.getText().toString(), hp.getText().toString());
                        kAdapter.remove(kAdapter.getItem(position));

                        dialog.dismiss();
                        sort_list();
                    }
                });

                hapus.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

                hapus.show();
            }
        });
    }

    private void find_data() {
        kAdapter.clear();
        AlertDialog.Builder cari = new AlertDialog.Builder(this);
        cari.setTitle("Cari Kontak");

        View vCari = LayoutInflater.from(this).inflate(R.layout.find_kontak, null);
        final EditText nm = (EditText) vCari.findViewById(R.id.e_nm);

        cari.setView(vCari);

        // Set up the buttons
        cari.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                find_item(nm.getText().toString());
                dialog.dismiss();
                sort_list();
            }
        });

        cari.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                get_item();
                sort_list();
            }
        });

        cari.show();
    }
}