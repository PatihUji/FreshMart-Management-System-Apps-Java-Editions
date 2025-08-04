package Transaksi.StokKeluar;

import java.sql.Date;
import java.sql.Timestamp;

public class StokKeluar {
    private int sk_id;
    private int kry_id;
    private String kry_nama;
    private Date sk_tanggal_keluar;
    private int sk_jumlah_keluar;
    private String sk_keterangan;
    private String sk_created_by;
    private Timestamp sk_created_date;
    private String sk_modif_by;
    private Timestamp sk_modif_date;

    public StokKeluar(int sk_id, int kry_id, String kry_nama, Date sk_tanggal_keluar,
                      int sk_jumlah_keluar, String sk_keterangan, String sk_created_by,
                      Timestamp sk_created_date, String sk_modif_by, Timestamp sk_modif_date) {
        this.sk_id = sk_id;
        this.kry_id = kry_id;
        this.kry_nama = kry_nama;
        this.sk_tanggal_keluar = sk_tanggal_keluar;
        this.sk_jumlah_keluar = sk_jumlah_keluar;
        this.sk_keterangan = sk_keterangan;
        this.sk_created_date = sk_created_date;
        this.sk_modif_by = sk_modif_by;
        this.sk_modif_date = sk_modif_date;
    }

    public StokKeluar() {}

    public int getSk_id() {
        return sk_id;
    }

    public void setSk_id(int sk_id) {
        this.sk_id = sk_id;
    }

    public int getKry_id() {
        return kry_id;
    }

    public void setKry_id(int kry_id) {
        this.kry_id = kry_id;
    }

    public Date getSk_tanggal_keluar() {
        return sk_tanggal_keluar;
    }

    public void setSk_tanggal_keluar(Date sk_tanggal_keluar) {
        this.sk_tanggal_keluar = sk_tanggal_keluar;
    }

    public String getKry_nama() {
        return kry_nama;
    }

    public void setKry_nama(String kry_nama) {
        this.kry_nama = kry_nama;
    }

    public int getSk_jumlah_keluar() {
        return sk_jumlah_keluar;
    }

    public void setSk_jumlah_keluar(int sk_jumlah_keluar) {
        this.sk_jumlah_keluar = sk_jumlah_keluar;
    }

    public String getSk_keterangan() {
        return sk_keterangan;
    }

    public void setSk_keterangan(String sk_keterangan) {
        this.sk_keterangan = sk_keterangan;
    }

    public String getSk_created_by() {
        return sk_created_by;
    }

    public void setSk_created_by(String sk_created_by) {
        this.sk_created_by= sk_created_by;
    }

    public String getSk_modif_by() {
        return sk_modif_by;
    }

    public void setSk_modif_by(String sk_modif_by) {
        this.sk_modif_by = sk_modif_by;
    }

    public Timestamp getSk_created_date() {
        return sk_created_date;
    }

    public void setSk_created_date(Timestamp sk_created_date) {
        this.sk_created_date = sk_created_date;
    }

    public Timestamp getSk_modif_date() {
        return sk_modif_date;
    }

    public void setSk_modif_date(Timestamp sk_modif_date) {
        this.sk_modif_date = sk_modif_date;
    }

    // Getter & Setter jika perlu
}