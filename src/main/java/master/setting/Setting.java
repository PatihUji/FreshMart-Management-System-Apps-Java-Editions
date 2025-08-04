package master.setting;

import java.sql.Date;

public class Setting {
    private int s_id;
    private String s_nama;
    private String s_kategori;
    private int s_status;
    private String s_createdBy;
    private Date s_createdDate;
    private String s_modifBy;
    private Date s_modifDate;

    public Setting(int s_id, String s_nama, String s_kategori, int s_status,
                   String s_createdBy, Date s_createdDate,
                   String s_modifBy, Date s_modifDate) {
        this.s_id = s_id;
        this.s_nama = s_nama;
        this.s_kategori = s_kategori;
        this.s_status = s_status;
        this.s_createdBy = s_createdBy;
        this.s_createdDate = s_createdDate;
        this.s_modifBy = s_modifBy;
        this.s_modifDate = s_modifDate;
    }

    public Setting(int s_id, String s_nama) {
        this.s_id = s_id;
        this.s_nama = s_nama;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getS_nama() {
        return s_nama;
    }

    public void setS_nama(String s_nama) {
        this.s_nama = s_nama;
    }

    public String getS_kategori() {
        return s_kategori;
    }

    public void setS_kategori(String s_kategori) {
        this.s_kategori = s_kategori;
    }

    public int getS_status() {
        return s_status;
    }

    public void setS_status(int s_status) {
        this.s_status = s_status;
    }

    public String getS_createdBy() {
        return s_createdBy;
    }

    public void setS_createdBy(String s_createdBy) {
        this.s_createdBy = s_createdBy;
    }

    public Date getS_createdDate() {
        return s_createdDate;
    }

    public void setS_createdDate(Date s_createdDate) {
        this.s_createdDate = s_createdDate;
    }

    public String getS_modifBy() {
        return s_modifBy;
    }

    public void setS_modifBy(String s_modifBy) {
        this.s_modifBy = s_modifBy;
    }

    public Date getS_modifDate() {
        return s_modifDate;
    }

    public void setS_modifDate(Date s_modifDate) {
        this.s_modifDate = s_modifDate;
    }

    @Override
    public String toString() {
        return s_nama; // agar hanya nama yang tampil di ComboBox
    }
}
