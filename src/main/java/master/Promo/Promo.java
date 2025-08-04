package master.Promo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.Date;
import java.time.LocalDate;

public class Promo {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private int pr_id;
    private String pr_nama;
    private Double pr_persentase;
    private LocalDate pr_tanggalMulai;
    private LocalDate pr_tanggalBerakhir;
    private int pr_status;
    private String pr_createdBy;
    private Date pr_createdDate;
    private String pr_modifBy;
    private Date pr_modifDate;
    public Promo(int pr_id, String pr_nama, Double pr_persentase, LocalDate pr_tanggalMulai,
                 LocalDate pr_tanggalBerakhir, int pr_status, String pr_createdBy, Date pr_createdDate,
                 String pr_modifBy, Date pr_modifDate) {
        this.pr_id = pr_id;
        this.pr_nama = pr_nama;
        this.pr_persentase = pr_persentase;
        this.pr_tanggalMulai = pr_tanggalMulai;
        this.pr_tanggalBerakhir = pr_tanggalBerakhir;
        this.pr_status = pr_status;
        this.pr_createdBy = pr_createdBy;
        this.pr_createdDate = pr_createdDate;
        this.pr_modifBy = pr_modifBy;
        this.pr_modifDate = pr_modifDate;
    }

    public Promo(int pr_id, String pr_nama, Double pr_persentase) {
        this.pr_id = pr_id;
        this.pr_nama = pr_nama;
        this.pr_persentase = pr_persentase;
    }

    public int getPr_id() {
        return pr_id;
    }

    public void setPr_id(int pr_id) {
        this.pr_id = pr_id;
    }

    public String getPr_nama() {
        return pr_nama;
    }

    public void setPr_nama(String pr_nama) {
        this.pr_nama = pr_nama;
    }

    public Double getPr_persentase() {
        return pr_persentase;
    }

    public void setPr_persentase(Double pr_persentase) {
        this.pr_persentase = pr_persentase;
    }

    public LocalDate getPr_tanggalMulai() {
        return pr_tanggalMulai;
    }

    public void setPr_tanggalMulai(LocalDate pr_tanggalMulai) {
        this.pr_tanggalMulai = pr_tanggalMulai;
    }

    public LocalDate getPr_tanggalBerakhir() {
        return pr_tanggalBerakhir;
    }

    public void setPr_tanggalBerakhir(LocalDate pr_tanggalBerakhir) {
        this.pr_tanggalBerakhir = pr_tanggalBerakhir;
    }

    public int getPr_status() {
        return pr_status;
    }

    public void setPr_status(int pr_status) {
        this.pr_status = pr_status;
    }

    public String getPr_createdBy() {
        return pr_createdBy;
    }

    public void setPr_createdBy(String pr_createdBy) {
        this.pr_createdBy = pr_createdBy;
    }

    public Date getPr_createdDate() {
        return pr_createdDate;
    }

    public void setPr_createdDate(Date pr_createdDate) {
        this.pr_createdDate = pr_createdDate;
    }

    public String getPr_modifBy() {
        return pr_modifBy;
    }

    public void setPr_modifBy(String pr_modifBy) {
        this.pr_modifBy = pr_modifBy;
    }

    public Date getPr_modifDate() {
        return pr_modifDate;
    }

    public void setPr_modifDate(Date pr_modifDate) {
        this.pr_modifDate = pr_modifDate;
    }

    @Override
    public String toString() {
        return pr_nama;
    }

    public boolean isSelected() { return selected.get(); }

    public void setSelected(boolean value) { selected.set(value); }

    public BooleanProperty selectedProperty() { return selected; }
}

