package Transaksi.StokKeluar;

import master.produk.Produk;

public class DetailStokKeluar {
    private int p_id;
    private int dsk_kuantitas;

    private String nama_produk;  // Untuk tampil di TableView detailtransaksi
    private int kuantitas;       // Untuk tampil di TableView detailtransaksi

    public DetailStokKeluar() {
    }

    public DetailStokKeluar(int p_id, int dsk_kuantitas) {
        this.p_id = p_id;
        this.dsk_kuantitas = dsk_kuantitas;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getDsk_kuantitas() {
        return dsk_kuantitas;
    }

    public void setDsk_kuantitas(int dsk_kuantitas) {
        this.dsk_kuantitas = dsk_kuantitas;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    private Produk produk;
    public void setProduk(Produk produk) {
        this.produk = produk;
    }
    public Produk getProduk() {
        return produk;
    }
}

