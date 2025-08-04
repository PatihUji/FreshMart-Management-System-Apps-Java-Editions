package Transaksi.Pengiriman;

import java.sql.Date;
import java.sql.Time;

public class Pengiriman {
    Integer png_id;
    Integer pnj_id;
    Integer kry_id;
    String kry_nama;
    Integer p_id;
    String p_nama;                //penerima
    String png_nama;
    Date png_tanggal;
    Time png_jam;
    Integer png_status;
    String png_modif_by;
    Date png_modif_tanggal;
    Double pnj_total_harga;
    String pnj_created_by;
    Date pnj_created_date;
    String png_alamat;
    Date png_created_date;

    public Date getPnj_created_date() {
        return pnj_created_date;
    }

    public void setPnj_created_date(Date pnj_created_date) {
        this.pnj_created_date = pnj_created_date;
    }

    public Date getPng_created_date() {
        return png_created_date;
    }

    public void setPng_created_date(Date png_created_date) {
        this.png_created_date = png_created_date;
    }

    public String getPng_alamat() {
        return png_alamat;
    }

    public void setPng_alamat(String png_alamat) {
        this.png_alamat = png_alamat;
    }

    public String getPnj_created_by() {
        return pnj_created_by;
    }

    public void setPnj_created_by(String pnj_created_by) {
        this.pnj_created_by = pnj_created_by;
    }

    public Double getPnj_total_harga() {
        return pnj_total_harga;
    }

    public void setPnj_total_harga(Double pnj_total_harga) {
        this.pnj_total_harga = pnj_total_harga;
    }

    public Pengiriman(){

    }

    public Pengiriman (Integer pnj_id, String kry_nama, Double pnj_total_harga){
        this.kry_nama = kry_nama;
        this.pnj_id = pnj_id;
        this.pnj_total_harga = pnj_total_harga;
    }

    public Pengiriman(Integer png_id, Integer pnj_id, Integer kry_id, String kry_nama,
                      Integer p_id, String p_nama, String png_nama, Date png_tanggal, Time png_jam,
                      Integer png_status, String png_modif_by, Date png_modif_tanggal) {
        this.png_id = png_id;
        this.pnj_id = pnj_id;
        this.kry_id = kry_id;
        this.kry_nama = kry_nama;
        this.p_id = p_id;
        this.p_nama = p_nama;
        this.png_nama = png_nama;
        this.png_tanggal = png_tanggal;
        this.png_jam = png_jam;
        this.png_status = png_status;
        this.png_modif_by = png_modif_by;
        this.png_modif_tanggal = png_modif_tanggal;
    }

    public Integer getPng_id() {
        return png_id;
    }

    public void setPng_id(Integer png_id) {
        this.png_id = png_id;
    }

    public Integer getPnj_id() {
        return pnj_id;
    }

    public void setPnj_id(Integer pnj_id) {
        this.pnj_id = pnj_id;
    }

    public Integer getKry_id() {
        return kry_id;
    }

    public void setKry_id(Integer kry_id) {
        this.kry_id = kry_id;
    }

    public String getKry_nama() {
        return kry_nama;
    }

    public void setKry_nama(String kry_nama) {
        this.kry_nama = kry_nama;
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    public String getP_nama() {
        return p_nama;
    }

    public void setP_nama(String p_nama) {
        this.p_nama = p_nama;
    }

    public String getPng_nama() {
        return png_nama;
    }

    public void setPng_nama(String png_nama) {
        this.png_nama = png_nama;
    }

    public Date getPng_tanggal() {
        return png_tanggal;
    }

    public void setPng_tanggal(Date png_tanggal) {
        this.png_tanggal = png_tanggal;
    }

    public Time getPng_jam() {
        return png_jam;
    }

    public void setPng_jam(Time png_jam) {
        this.png_jam = png_jam;
    }

    public Integer getPng_status() {
        return png_status;
    }

    public void setPng_status(Integer png_status) {
        this.png_status = png_status;
    }

    public String getPng_modif_by() {
        return png_modif_by;
    }

    public void setPng_modif_by(String png_modif_by) {
        this.png_modif_by = png_modif_by;
    }

    public Date getPng_modif_tanggal() {
        return png_modif_tanggal;
    }

    public void setPng_modif_tanggal(Date png_modif_tanggal) {
        this.png_modif_tanggal = png_modif_tanggal;
    }
}
