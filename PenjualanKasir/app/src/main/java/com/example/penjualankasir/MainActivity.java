package com.example.penjualankasir;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText etNamaPelanggan, etNamaBarang, etJmlBarang, etHarga, etJmlUang;
    TextView tvNamaPembeli, tvNamaBarang, tvJmlBarang, tvHarga, tvUangBayar,
            tvTotal, tvKembalian, tvKeterangan, tvUangBayarTxt, tvTotalTxt,
            tvTotalBayar, tvTotalHarga;
    Button btnTambah, btnProses, btnHapus, btnKeluar;

    double uangByr, total, kembalian;

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Aplikasi Penjualan");

        customer = new Customer();

        //EditText
        etNamaPelanggan = findViewById(R.id.etNamaPelanggan);
        etNamaBarang = findViewById(R.id.etNamaBarang);
        etJmlBarang = findViewById(R.id.etJmlBarang);
        etHarga = findViewById(R.id.etHarga);
        etJmlUang = findViewById(R.id.etJmlUang);

        //TextView
        tvNamaPembeli = findViewById(R.id.viewNameCust);
        tvNamaBarang = findViewById(R.id.viewListBarang);
        tvJmlBarang = findViewById(R.id.viewJumlah);
        tvHarga = findViewById(R.id.viewHarga);
        tvUangBayar = findViewById(R.id.viewUangBayarResAmount);
        tvTotal = findViewById(R.id.viewTotalResAmount);
        tvKembalian = findViewById(R.id.viewUangKembalian);
        tvKeterangan = findViewById(R.id.viewKeterangan);
        tvUangBayarTxt = findViewById(R.id.viewUangBayarRes);
        tvTotalTxt = findViewById(R.id.viewTotalRes);
        tvTotalBayar = findViewById(R.id.viewTotalBayar);
        tvTotalHarga = findViewById(R.id.viewTotalHarga);

        //Button
        btnTambah = findViewById(R.id.btnTambah);
        btnProses = findViewById(R.id.btnProses);
        btnHapus = findViewById(R.id.btnHapus);
        btnKeluar = findViewById(R.id.btnKeluar);

        //Tambah
        btnTambah.setOnClickListener(view -> {
            String namaItem = etNamaBarang.getText().toString().trim();
            String jumlahItem = etJmlBarang.getText().toString().trim();
            String hargaItem = etHarga.getText().toString().trim();

            double hargaItem2 = Double.parseDouble(hargaItem);
            int jumlahItem2 = Integer.parseInt(jumlahItem);

            Item item = new Item(namaItem, jumlahItem2, hargaItem2);

            customer.addItem(item);

            String tvTotalHargasetText = "Total Harga : Rp " + item.getTotalHarga();
            String tvTotalBayarsetText = "Total Bayar : Rp " + customer.getTotalBelanja();
            tvTotalHarga.setText(tvTotalHargasetText);
            tvTotalBayar.setText(tvTotalBayarsetText);

            etNamaBarang.setText("");
            etHarga.setText("");
            etJmlBarang.setText("");

            etNamaBarang.setHint(namaItem);
            etHarga.setHint(hargaItem);
            etJmlBarang.setHint(jumlahItem);

            Toast.makeText(getApplicationContext(), "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
        });

        //Proses
        btnProses.setOnClickListener(view -> {
            String namaPelanggan = etNamaPelanggan.getText().toString().trim();
            String uangBayar = etJmlUang.getText().toString().trim();

            uangByr = Double.parseDouble(uangBayar);
            total = customer.getTotalBelanja();
            customer.setNama(namaPelanggan);
            customer.setUang(uangByr);

            String viewUangBayarTxt = "Uang Bayar";
            String viewTotalTxt = "Total";
            String viewNamaPembeli = "Nama Pembeli : " + customer.getNama() + "\n\nDaftar Belanjaan :";
            String viewNamaBarang = "Nama Barang\n" + customer.getNamaItem();
            String viewJmlBarang = "Quantity\n" + customer.getJumlahItem();
            String viewHarga = "Harga\n" + customer.getHargaItem() + "\n----------";
            String viewUangBayar = Double.toString(customer.getUang());
            String viewTotal = Double.toString(total);

            tvUangBayarTxt.setText(viewUangBayarTxt);
            tvTotalTxt.setText(viewTotalTxt);
            tvNamaPembeli.setText(viewNamaPembeli);
            tvNamaBarang.setText(viewNamaBarang);
            tvJmlBarang.setText(viewJmlBarang);
            tvHarga.setText(viewHarga);
            tvUangBayar.setText(viewUangBayar);
            tvTotal.setText(viewTotal);

            kembalian = (uangByr - total);
            String viewKeterangan;
            String viewKembalian;

            if (uangByr < total) {
                viewKeterangan = "Keterangan : Uang bayar kurang Rp. " + (-kembalian);
                viewKembalian = "Uang Kembalian : Rp. 0";
            }

            else {
                viewKeterangan = "Keterangan : Tunggu kembalian";
                viewKembalian = "Uang Kembalian : Rp. " + kembalian;
            }

            tvKeterangan.setText(viewKeterangan);
            tvKembalian.setText(viewKembalian);

            etNamaPelanggan.setText("");
            etNamaPelanggan.setHint(namaPelanggan);

            etJmlUang.setText("");
            etJmlUang.setHint(Double.toString(customer.getUang()));

            String setTotalHarga = "Total Harga :";
            String setTotalBayar = "Total Bayar :";

            tvTotalHarga.setText(setTotalHarga);
            tvTotalBayar.setText(setTotalBayar);

            btnHapus.setVisibility(View.VISIBLE);
            btnKeluar.setVisibility(View.VISIBLE);
        });

        btnHapus.setOnClickListener(view -> {
            tvNamaPembeli.setText("");
            tvNamaBarang.setText("");
            tvJmlBarang.setText("");
            tvHarga.setText("");
            tvUangBayar.setText("");
            tvKembalian.setText("");
            tvKeterangan.setText("");
            tvTotal.setText("");
            tvTotalTxt.setText("");
            tvUangBayarTxt.setText("");

            btnHapus.setVisibility(View.INVISIBLE);
            btnKeluar.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), "Data sudah dihapus", Toast.LENGTH_SHORT).show();
        });

        btnKeluar.setOnClickListener(view -> moveTaskToBack(true));

    }
}