package master.produk;

import java.sql.Date;

public class Produk {
    private int id;
    private int jpId;
    private String jpnama;
    private String nama;
    private Double harga;
    private String satuan;
    private Integer stok;
    private String deskripsi;
    private String gambar;
    private int status;
    private String createdBy;
    private Date createdDate;
    private String modifBy;
    private Date modifDate;

    public Produk(int id, int jpId, String jpnama, String nama, Double harga, String satuan, Integer stok, String deskripsi, String gambar, int status, String createdBy, Date createdDate, String modifBy, Date modifDate) {
        this.id = id;
        this.jpId = jpId;
        this.jpnama = jpnama;
        this.nama = nama;
        this.harga = harga;
        this.satuan = satuan;
        this.stok = stok;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifBy = modifBy;
        this.modifDate = modifDate;
    }

    public Produk(String nama, String jpnama, double harga, String satuan, int stok, String deskripsi) {
        this.nama = nama;
        this.jpnama = jpnama;
        this.harga = harga;
        this.satuan = satuan;
        this.stok = stok;
        this.deskripsi = deskripsi;
    }

    public Produk(Integer stok, int id, String nama) {
        this.stok = stok;
        this.id = id;
        this.nama = nama;
    }

    public Produk(Integer stok, int id, String nama, String deskripsi) {
        this.stok = stok;
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
    }

    public Produk(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public Produk(int stok) {
        this.stok = stok;
    }

    public String getJpnama() {
        return jpnama;
    }

    public void setJpnama(String jpnama) {
        this.jpnama = jpnama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJpId() {
        return jpId;
    }

    public void setJpId(int jpId) {
        this.jpId = jpId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifBy() {
        return modifBy;
    }

    public void setModifBy(String modifBy) {
        this.modifBy = modifBy;
    }

    public Date getModifDate() {
        return modifDate;
    }

    public void setModifDate(Date modifDate) {
        this.modifDate = modifDate;
    }

    @Override
    public String toString() {
        return nama;
    }
}
