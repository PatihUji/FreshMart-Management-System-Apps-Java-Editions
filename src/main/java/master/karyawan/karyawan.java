package master.karyawan;

import java.sql.Date;
import java.time.LocalDate;

public class karyawan {
    private int id;
    private int sId;
    private String sNama;
    private String nama;
    private LocalDate tanggalLahir;
    private String gender;
    private String username;
    private String password;
    private String alamat;
    private int status;
    private String createdBy;
    private Date createdDate;
    private String modifBy;
    private Date modifDate;

    // Constructor, getter & setter
    public karyawan(int id, int sId, String sNama, String nama, LocalDate tanggalLahir, String gender, String password, String username, String alamat, int status, String createdBy, Date createdDate, String modifBy, Date modifDate) {
        this.id = id;
        this.sId = sId;
        this.sNama = sNama;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.gender = gender;
        this.password = password;
        this.username = username;
        this.alamat = alamat;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifBy = modifBy;
        this.modifDate = modifDate;
    }

    public karyawan(){

    }

    public karyawan(int id, String nama){
        this.id = id;
        this.nama = nama;
    }

    public karyawan(String nama, String sNama, LocalDate tanggalLahir, String gender, String password, String username, String alamat) {
        this.sNama = sNama;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.gender = gender;
        this.password = password;
        this.username = username;
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getSNama() {
        return sNama;
    }

    public void setSNama(String sNama) {
        this.sNama = sNama;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
