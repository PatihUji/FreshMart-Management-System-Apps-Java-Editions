package Transaksi.Penjualan;

import java.util.Date;

public class Penjualan {
    private int pnj_id;
    private int s_id;
    private int mpb_id;
    private int kry_id;
    private String s_nama;
    private String mpb_nama;
    private String kry_nama;
    private Double pnj_total_harga;
    private int pnj_status;
    private Date pnj_created_date;

    public Penjualan(int pnj_id, Date pnj_created_date, int s_id, int mpb_id, int kry_id, String s_nama, String mpb_nama, String kry_nama, Double pnj_total_harga, int pnj_status) {
        this.pnj_id = pnj_id;
        this.pnj_created_date = pnj_created_date;
        this.s_id = s_id;
        this.mpb_id = mpb_id;
        this.kry_id = kry_id;
        this.s_nama = s_nama;
        this.mpb_nama = mpb_nama;
        this.kry_nama = kry_nama;
        this.pnj_total_harga = pnj_total_harga;
        this.pnj_status = pnj_status;
    }

    public Penjualan(int pnj_id, int s_id, int mpb_id, int kry_id, String s_nama, String mpb_nama, String kry_nama, Double pnj_total_harga, int pnj_status) {
        this.pnj_id = pnj_id;
        this.s_id = s_id;
        this.mpb_id = mpb_id;
        this.kry_id = kry_id;
        this.s_nama = s_nama;
        this.mpb_nama = mpb_nama;
        this.kry_nama = kry_nama;
        this.pnj_total_harga = pnj_total_harga;
        this.pnj_status = pnj_status;
    }

    public Date getPnj_created_date() {
        return pnj_created_date;
    }

    public void setPnj_created_date(Date pnj_created_date) {
        this.pnj_created_date = pnj_created_date;
    }

    public int getPnj_id() {
        return pnj_id;
    }

    public void setPnj_id(int pnj_id) {
        this.pnj_id = pnj_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public int getMpb_id() {
        return mpb_id;
    }

    public void setMpb_id(int mpb_id) {
        this.mpb_id = mpb_id;
    }

    public int getKry_id() {
        return kry_id;
    }

    public void setKry_id(int kry_id) {
        this.kry_id = kry_id;
    }

    public String getS_nama() {
        return s_nama;
    }

    public void setS_nama(String s_nama) {
        this.s_nama = s_nama;
    }

    public String getMpb_nama() {
        return mpb_nama;
    }

    public void setMpb_nama(String mpb_nama) {
        this.mpb_nama = mpb_nama;
    }

    public String getKry_nama() {
        return kry_nama;
    }

    public void setKry_nama(String kry_nama) {
        this.kry_nama = kry_nama;
    }

    public Double getPnj_total_harga() {
        return pnj_total_harga;
    }

    public void setPnj_total_harga(Double pnj_total_harga) {
        this.pnj_total_harga = pnj_total_harga;
    }

    public int getPnj_status() {
        return pnj_status;
    }

    public void setPnj_status(int pnj_status) {
        this.pnj_status = pnj_status;
    }
}
