package Transaksi.ReturBarang;

public class DetailReturBarang {
    private int p_id;
    private String p_nama;
    private int drp_kuantitas;
    private String drp_alasan;

    public DetailReturBarang(int p_id, String p_nama, int drp_kuantitas, String drp_alasan) {
        this.p_id = p_id;
        this.p_nama = p_nama;
        this.drp_kuantitas = drp_kuantitas;
        this.drp_alasan = drp_alasan;
    }

    public DetailReturBarang(String p_nama, int drp_kuantitas, String drp_alasan) {
        this.p_nama = p_nama;
        this.drp_kuantitas = drp_kuantitas;
        this.drp_alasan = drp_alasan;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getP_nama() { return p_nama; }
    public void setP_nama(String p_nama) { this.p_nama = p_nama; }

    public int getDrp_kuantitas() {
        return drp_kuantitas;
    }

    public void setDrp_kuantitas(int dsk_kuantitas) {
        this.drp_kuantitas = dsk_kuantitas;
    }

    public String getDrp_alasan() {
        return drp_alasan;
    }

    public void setDrp_alasan(String drp_alasan) {
        this.drp_alasan = drp_alasan;
    }
}
