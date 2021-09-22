package com.example.penjualankasir;

public class Item {
    private final String namaItem;
    private final int jumlah;
    private final double harga;

    public Item(String namaItem, int jumlah, double harga) {
        this.namaItem = namaItem;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public double getTotalHarga() {
        return harga * jumlah;
    }
}
