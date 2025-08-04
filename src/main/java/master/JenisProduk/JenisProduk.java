package master.JenisProduk;

import java.sql.Date;

public class JenisProduk {
    private int jp_id;
    private String jp_nama;
    private int jp_status;
    private String jp_createdBy;
    private Date jp_createdDate;
    private String jp_modifiedBy;
    private Date jp_modifiedDate;

    // Constructor lengkap
    public JenisProduk(int jp_id, String jp_nama, int jp_status, String jp_createdBy, Date jp_createdDate, String jp_modifiedBy, Date jp_modifiedDate) {
        this.jp_id = jp_id;
        this.jp_nama = jp_nama;
        this.jp_status = jp_status;
        this.jp_createdBy = jp_createdBy;
        this.jp_createdDate = jp_createdDate;
        this.jp_modifiedBy = jp_modifiedBy;
        this.jp_modifiedDate = jp_modifiedDate;
    }

    public JenisProduk(int id, String nama) {
        this.jp_id = id;
        this.jp_nama = nama;
    }

    public int getJp_id() {
        return jp_id;
    }

    public void setJp_id(int jp_id) {
        this.jp_id = jp_id;
    }

    public String getJp_nama() {
        return jp_nama;
    }

    public void setJp_nama(String jp_nama) {
        this.jp_nama = jp_nama;
    }

    public int getJp_status() {
        return jp_status;
    }

    public void setJp_status(int jp_status) {
        this.jp_status = jp_status;
    }

    public String getJp_createdBy() {
        return jp_createdBy;
    }

    public void setJp_createdBy(String jp_createdBy) {
        this.jp_createdBy = jp_createdBy;
    }

    public Date getJp_createdDate() {
        return jp_createdDate;
    }

    public void setJp_createdDate(Date jp_createdDate) {
        this.jp_createdDate = jp_createdDate;
    }

    public String getJp_modifiedBy() {
        return jp_modifiedBy;
    }

    public void setJp_modifiedBy(String jp_modifiedBy) {
        this.jp_modifiedBy = jp_modifiedBy;
    }

    public Date getJp_modifiedDate() {
        return jp_modifiedDate;
    }

    public void setJp_modifiedDate(Date jp_modifiedDate) {
        this.jp_modifiedDate = jp_modifiedDate;
    }

    @Override
    public String toString() {
        return jp_nama;
    }
}

