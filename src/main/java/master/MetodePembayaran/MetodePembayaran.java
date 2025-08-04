package master.MetodePembayaran;

import java.sql.Date;

public class MetodePembayaran {
    private int mpb_id;
    private String mpb_nama;
    private int mpb_status;
    private String mpb_created_by;
    private Date mpb_created_date;
    private String mpb_modif_by;
    private Date mpb_modif_date;

    // Constructor lengkap
    public MetodePembayaran(int mpb_id, String mpb_nama, int mpb_status,
                            String mpb_created_by, Date mpb_created_date,
                            String mpb_modif_by, Date mpb_modif_date) {
        this.mpb_id = mpb_id;
        this.mpb_nama = mpb_nama;
        this.mpb_status = mpb_status;
        this.mpb_created_by = mpb_created_by;
        this.mpb_created_date = mpb_created_date;
        this.mpb_modif_by = mpb_modif_by;
        this.mpb_modif_date = mpb_modif_date;
    }

    // Constructor minimal (misalnya untuk combobox)
    public MetodePembayaran(int mpb_id, String mpb_nama) {
        this.mpb_id = mpb_id;
        this.mpb_nama = mpb_nama;
    }

    // Getter dan Setter
    public int getMpb_id() {
        return mpb_id;
    }

    public void setMpb_id(int mpb_id) {
        this.mpb_id = mpb_id;
    }

    public String getMpb_nama() {
        return mpb_nama;
    }

    public void setMpb_nama(String mpb_nama) {
        this.mpb_nama = mpb_nama;
    }

    public int getMpb_status() {
        return mpb_status;
    }

    public void setMpb_status(int mpb_status) {
        this.mpb_status = mpb_status;
    }

    public String getMpb_created_by() {
        return mpb_created_by;
    }

    public void setMpb_created_by(String mpb_created_by) {
        this.mpb_created_by = mpb_created_by;
    }

    public Date getMpb_created_date() {
        return mpb_created_date;
    }

    public void setMpb_created_date(Date mpb_created_date) {
        this.mpb_created_date = mpb_created_date;
    }

    public String getMpb_modif_by() {
        return mpb_modif_by;
    }

    public void setMpb_modif_by(String mpb_modif_by) {
        this.mpb_modif_by = mpb_modif_by;
    }

    public Date getMpb_modif_date() {
        return mpb_modif_date;
    }

    public void setMpb_modif_date(Date mpb_modif_date) {
        this.mpb_modif_date = mpb_modif_date;
    }

    @Override
    public String toString() {
        return mpb_nama; // Menampilkan hanya nama di ComboBox
    }
}
