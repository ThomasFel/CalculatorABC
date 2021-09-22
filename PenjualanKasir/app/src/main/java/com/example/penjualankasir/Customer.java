package com.example.penjualankasir;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final List<Item> items = new ArrayList<>();
    private String nama;
    private double uang;
    private double totalBelanja = 0;

    public void addItem(Item item) {
        items.add(item);
        totalBelanja += item.getTotalHarga();
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUang(double uang) {
        this.uang = uang;
    }

    public String getNama() {
        return nama;
    }

    public double getUang() {
        return uang;
    }

    public String getNamaItem() {
        StringBuilder namaItem = new StringBuilder();

        for (Item item:items) {
            namaItem.append(item.getNamaItem());
            namaItem.append("\n");
        }

        return namaItem.toString();
    }

    public String getJumlahItem() {
        StringBuilder jumlahItem = new StringBuilder();

        for (Item item:items) {
            jumlahItem.append(item.getJumlah());
            jumlahItem.append("\n");
        }

        return jumlahItem.toString();
    }

    public String getHargaItem() {
        StringBuilder hargaItem = new StringBuilder();

        for (Item item:items) {
            hargaItem.append(item.getTotalHarga());
            hargaItem.append("\n");
        }

        return hargaItem.toString();
    }

    public double getTotalBelanja() {
        return totalBelanja;
    }
}
