package Transaksi.ReturBarang;

import java.time.LocalDate;

public class ReturBarang {
    private int pnj_id;
    private int rtrId;
    private int kryId;
    private String namaKasir;
    private LocalDate tanggalRetur;
    private int jumlah;

    public ReturBarang() {
    }
    public ReturBarang(int pnj_id, int rtrId, String namaKasir, LocalDate tanggalRetur, int jumlah) {
        this.pnj_id = pnj_id;
        this.rtrId = rtrId;
        this.namaKasir = namaKasir;
        this.tanggalRetur = tanggalRetur;
        this.jumlah = jumlah;
    }

    public ReturBarang(int rtrId, LocalDate tanggalRetur, int jumlah, int pnj_id, int kryId, String namaKasir) {
        this.rtrId = rtrId;
        this.tanggalRetur = tanggalRetur;
        this.jumlah = jumlah;
        this.pnj_id = pnj_id;
        this.kryId = kryId;
        this.namaKasir = namaKasir;
    }

    public int getKryId() {
        return kryId;
    }

    public void setKryId(int kryId) {
        this.kryId = kryId;
    }

    public int getPnj_id() {
        return pnj_id;
    }

    public void setPnj_id(int pnj_id) {
        this.pnj_id = pnj_id;
    }

    public int getRtrId() {
        return rtrId;
    }

    public void setRtrId(int rtrId) {
        this.rtrId = rtrId;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }

    public LocalDate getTanggalRetur() {
        return tanggalRetur;
    }

    public void setTanggalRetur(LocalDate tanggalRetur) {
        this.tanggalRetur = tanggalRetur;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
