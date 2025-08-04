package database;

import Transaksi.Pengiriman.Pengiriman;
import Transaksi.Penjualan.DetailPenjualan;
import Transaksi.Penjualan.DetailPromo;
import Transaksi.Penjualan.Penjualan;
import Transaksi.ReturBarang.DetailReturBarang;
import Transaksi.ReturBarang.ReturBarang;
import Transaksi.StokKeluar.DetailStokKeluar;
import Transaksi.StokKeluar.StokKeluar;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import master.JenisProduk.JenisProduk;
import Helper.MessageBox;
import master.MetodePembayaran.MetodePembayaran;
import master.Promo.Promo;
import master.karyawan.karyawan;
import master.produk.Produk;
import master.setting.Setting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;
    public CallableStatement cstat;
    MessageBox messageBox = new MessageBox();

    public DBConnect() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=TheFreshChoice;encrypt=true;trustServerCertificate=true;user=PROJ3CT;password=1234";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
//            System.out.println("Connection berhasil");
        }
        catch (Exception e) {
            System.out.println("Error saat connect database : " + e);
        }
    }

    public int loginKaryawan(String username, String password, StringBuilder jabatanOut) {
        int resultCode = -99;

        try {
            // Panggil stored procedure: { ? = call sp_login_karyawan(?, ?, ?) }
            cstat = conn.prepareCall("{? = call sp_login_karyawan(?, ?, ?)}");

            // Register RETURN
            cstat.registerOutParameter(1, Types.INTEGER);

            // Set input parameter
            cstat.setString(2, username);
            cstat.setString(3, password);

            // Register OUTPUT parameter
            cstat.registerOutParameter(4, Types.VARCHAR);

            // Eksekusi stored procedure
            cstat.execute();

            // Ambil nilai return dan output
            resultCode = cstat.getInt(1);
            String jabatan = cstat.getString(4);

            if (jabatan != null) {
                jabatanOut.append(jabatan); // set jabatan via StringBuilder
            }

        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return resultCode;
    }

    public String getUserLoginName(String username) {
        String namaKaryawan = null;
        String query = "SELECT dbo.udf_getUserLogin(?) AS NamaKaryawan";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setString(1, username);
            result = pstat.executeQuery();

            if (result.next()) {
                namaKaryawan = result.getString("NamaKaryawan");
            }
        } catch (SQLException e) {
            messageBox.alertWarning(e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                messageBox.alertError(ex.getMessage());
            }
        }

        return namaKaryawan;
    }

    public karyawan getProfileByUsername(String username) {
        karyawan data = null;
        String query = "SELECT kry_nama, kry_tgl_lahir, kry_gender, s_nama, kry_alamat, kry_username, kry_password FROM karyawan k JOIN setting s ON k.s_id = s.s_id WHERE kry_username = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setString(1, username);
            result = pstat.executeQuery();

            if (result.next()) {
                data = new karyawan(
                        result.getString("kry_nama"),
                        result.getString("s_nama"),
                        result.getDate("kry_tgl_lahir").toLocalDate().plusDays(2),
                        result.getString("kry_gender"),
                        result.getString("kry_password"),
                        result.getString("kry_username"),
                        result.getString("kry_alamat")
                );
            }

        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                messageBox.alertError(ex.getMessage());
            }
        }

        return data;
    }

    public boolean updatePasswordKaryawanByUsername(String username, String password) {
        try {
            String query = "UPDATE karyawan SET kry_password = ? WHERE kry_username = ?";
            pstat = conn.prepareStatement(query);
            pstat.setString(1, password);
            pstat.setString(2, username);
            pstat.execute();

            messageBox.alertInfo("Password berhasil diubah!");
            return true;
        }
        catch (SQLException e) {
            messageBox.alertError(e.getMessage());
            return false;
        }
    }

    public boolean insertKaryawan(int s_id, String nama, Date tglLahir, String alamat, String gender, String username, String password, String createdBy) {
        try {
            String query = "{call sp_insert_karyawan(?, ?, ?, ?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, s_id);
            cstat.setString(2, nama);
            cstat.setDate(3, tglLahir); // java.sql.Date
            cstat.setString(4, alamat);
            cstat.setString(5, gender);
            cstat.setString(6, password);
            cstat.setString(7, username);
            cstat.setString(8, createdBy);

            cstat.execute();
            messageBox.alertInfo("Data Karyawan berhasil ditambahkan!");
            return true;

        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
            return false;
        }
    }

    public boolean isKaryawanExist(int kryId) {
        boolean exists = false;
        String query = "SELECT dbo.udf_isKaryawanExist(?)";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, kryId);
            result = pstat.executeQuery();

            if (result.next()) {
                exists = result.getBoolean(1);
            }

        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                messageBox.alertError(ex.getMessage());
            }
        }

        return exists;
    }

    public boolean updateKaryawan(int s_id, int kry_id, String nama, Date tglLahir, String alamat, String gender, String username, String modifBy) {
        try {
            String query = "{call sp_update_karyawan(? ,?, ?, ?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, s_id);
            cstat.setInt(2, kry_id);
            cstat.setString(3, nama);
            cstat.setDate(4, tglLahir); // java.sql.Date
            cstat.setString(5, alamat);
            cstat.setString(6, gender);
            cstat.setString(8, username);
            cstat.setString(9, modifBy);

            cstat.execute();
            messageBox.alertInfo("Data Karyawan berhasil diperbarui!");
            return true;

        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
            return false;
        }
    }

    public List<Setting> getListSettingByKategori(String kategori) {
        List<Setting> list = new ArrayList<>();
        String query = "SELECT s_id, s_nama FROM setting WHERE s_status = 1 AND s_kategori = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setString(1, kategori);
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("s_id");
                String nama = result.getString("s_nama");
                list.add(new Setting(id, nama));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public List<karyawan> getListKaryawan(String search, Integer status, String sortColumn, String sortOrder) {
        List<karyawan> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_karyawan(?, ?, ?, ?)}");
            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);
            cstat.setString(3, sortColumn);
            cstat.setString(4, sortOrder);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new karyawan(
                        result.getInt("kry_id"),
                        result.getInt("s_id"),
                        result.getString("s_nama"),
                        result.getString("kry_nama"),
                        result.getDate("kry_tgl_lahir").toLocalDate().plusDays(2),
                        result.getString("kry_gender"),
                        result.getString("kry_password"),
                        result.getString("kry_username"),
                        result.getString("kry_alamat"),
                        result.getInt("kry_status"),
                        result.getString("kry_created_by"),
                        result.getDate("kry_created_date"),
                        result.getString("kry_modif_by"),
                        result.getDate("kry_modif_date")
                ));
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteKaryawan(int kry_id, String modifiedBy) {
        try {
            String query = "{call sp_setStatus_karyawan(?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, kry_id);
            cstat.setString(2, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
            return false;
        }
    }

    public boolean insertJenisProduk(String jpNama, String createdBy) {
        try {
            String query = "{call sp_insert_jenisProduk(?, ?)}";
            CallableStatement cstat = conn.prepareCall(query);

            cstat.setString(1, jpNama);
            cstat.setString(2, createdBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat insert jenis produk: " + e.getMessage());
            return false;
        }
    }

    public boolean isJenisProdukExist(int jpId) {
        boolean exists = false;
        String query = "SELECT dbo.udf_isJenisProdukExist(?)";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, jpId);
            result = pstat.executeQuery();

            if (result.next()) {
                exists = result.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println("Error saat cek keberadaan jenis produk: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                System.out.println("Error saat menutup resource: " + ex.getMessage());
            }
        }

        return exists;
    }

    public boolean updateJenisProduk(int jpId, String jpNama, String modifBy) {
        try {
            String query = "{call sp_update_jenisProduk(?, ?, ?)}";
            CallableStatement cstat = conn.prepareCall(query);

            cstat.setInt(1, jpId);
            cstat.setString(2, jpNama);
            cstat.setString(3, modifBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat update jenis produk: " + e.getMessage());
            return false;
        } finally {
            try {
                if (cstat != null) cstat.close();
            } catch (SQLException ex) {
                System.out.println("Error saat menutup statement: " + ex.getMessage());
            }
        }
    }

    public List<JenisProduk> getListJenisProduk(String search, Integer status, String sortColumn, String sortOrder) {
        List<JenisProduk> list = new ArrayList<>();
        try {
            CallableStatement cstat = conn.prepareCall("{CALL sp_getList_jenis_produk(?, ?, ?, ?)}");

            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);
            cstat.setString(3, sortColumn);
            cstat.setString(4, sortOrder);

            ResultSet result = cstat.executeQuery();
            while (result.next()) {
                list.add(new JenisProduk(
                        result.getInt("jp_id"),
                        result.getString("jp_nama"),
                        result.getInt("jp_status"),
                        result.getString("jp_created_by"),
                        result.getDate("jp_created_date"),
                        result.getString("jp_modif_by"),
                        result.getDate("jp_modif_date")
                ));
            }

            result.close();
            cstat.close();
        } catch (SQLException e) {
            System.out.println("Error saat mengambil list jenis produk: " + e.getMessage());
        }
        return list;
    }

    public boolean deleteJenisProduk(int jpId, String modifiedBy) {
        try {
            String query = "{call sp_setStatus_jenisProduk(?, ?)}";
            CallableStatement cstat = conn.prepareCall(query);

            cstat.setInt(1, jpId);
            cstat.setString(2, modifiedBy);

            cstat.execute();
            cstat.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat mengubah status jenis produk: " + e.getMessage());
            return false;
        }
    }

    public boolean insertPromo(String nama, Double persentase, Date tanggalMulai, Date tanggalBerakhir, String createdBy) {
        try {
            String query = "{call sp_insert_promo(?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setString(1, nama);
            cstat.setDouble(2, persentase);
            cstat.setTimestamp(3, new java.sql.Timestamp(tanggalMulai.getTime()));
            cstat.setTimestamp(4, new java.sql.Timestamp(tanggalBerakhir.getTime()));
            cstat.setString(5, createdBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat insert promo: " + e.getMessage());
            return false;
        }
    }

    public boolean isPromoExist(int promoId) {
        boolean exists = false;
        String query = "SELECT dbo.udf_isPromoExist(?)";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, promoId);
            result = pstat.executeQuery();

            if (result.next()) {
                exists = result.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println("Error saat cek keberadaan promo: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                System.out.println("Error saat menutup resource: " + ex.getMessage());
            }
        }

        return exists;
    }

    public boolean updatePromo(int prId, String nama, Double persentase, Date tanggalMulai, Date tanggalBerakhir, String modifiedBy) {
        try {
            String query = "{call sp_update_promo(?, ?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, prId);
            cstat.setString(2, nama);
            cstat.setDouble(3, persentase);
            cstat.setTimestamp(4, new java.sql.Timestamp(tanggalMulai.getTime()));
            cstat.setTimestamp(5, new java.sql.Timestamp(tanggalBerakhir.getTime()));
            cstat.setString(6, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat update promo: " + e.getMessage());
            return false;
        }
    }

    public List<Promo> getListPromo(String search, Integer status, String sortColumn, String sortOrder) {
        List<Promo> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_promo(?, ?, ?, ?)}");
            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);
            cstat.setString(3, sortColumn);
            cstat.setString(4, sortOrder);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new Promo(
                        result.getInt("pr_id"),
                        result.getString("pr_nama"),
                        result.getDouble("pr_persentase"),
                        result.getDate("pr_tanggal_mulai").toLocalDate().plusDays(2),
                        result.getDate("pr_tanggal_berakhir").toLocalDate().plusDays(2),
                        result.getInt("pr_status"),
                        result.getString("pr_created_by"),
                        result.getDate("pr_created_date"),
                        result.getString("pr_modif_by"),
                        result.getDate("pr_modif_date")
                ));
            }

            result.close();
            cstat.close();
        } catch (SQLException e) {
            System.out.println("Error saat mengambil list promo: " + e.getMessage());
        }
        return list;
    }

    public boolean deletePromo(int promoId, String modifiedBy) {
        try {
            String query = "{call sp_setStatus_promo(?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, promoId);
            cstat.setString(2, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat menghapus (nonaktifkan) promo: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // ===       SETTING     ===
    // =========================

    public boolean insertSetting(String nama, String kategori, String createdBy) {
        try {
            String query = "{call sp_insert_setting(?, ?, ?)}";
            CallableStatement cstat = conn.prepareCall(query);

            // Set parameter ke stored procedure
            cstat.setString(1, nama);
            cstat.setString(2, kategori);
            cstat.setString(3, createdBy);

            // Eksekusi prosedur
            cstat.execute();
            return true;

        } catch (SQLException e) {
            // Tampilkan pesan error dari SQL Server, misalnya dari RAISERROR
            System.out.println("Gagal insert setting: " + e.getMessage());
            return false;
        }
    }

    public boolean isSettingExist(int s_id) {
        boolean exists = false;
        String query = "SELECT dbo.udf_isSettingExist(?)";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, s_id);
            result = pstat.executeQuery();

            if (result.next()) {
                exists = result.getBoolean(1); // hasil dari fungsi BIT (true/false)
            }

        } catch (SQLException e) {
            System.out.println("Error saat cek keberadaan setting: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                System.out.println("Error saat menutup resource: " + ex.getMessage());
            }
        }

        return exists;
    }

    public boolean updateSetting(int s_id, String nama, String kategori, String modifBy) {
        try {
            String query = "{call sp_update_setting(?, ?, ?, ?)}";
            CallableStatement cstat = conn.prepareCall(query);

            cstat.setInt(1, s_id);
            cstat.setString(2, nama);
            cstat.setString(3, kategori);
            cstat.setString(4, modifBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat update setting: " + e.getMessage());
            return false;
        }
    }

    public List<Setting> getListSetting(String search, Integer status, String sortColumn, String sortOrder) {
        List<Setting> list = new ArrayList<>();
        try {
            String query = "{CALL sp_getList_setting(?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);
            cstat.setString(3, sortColumn);
            cstat.setString(4, sortOrder);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new Setting(
                        result.getInt("s_id"),
                        result.getString("s_nama"),
                        result.getString("s_kategori"),
                        result.getInt("s_status"),
                        result.getString("s_created_by"),
                        result.getDate("s_created_date"),
                        result.getString("s_modif_by"),
                        result.getDate("s_modif_date")
                ));
            }

            result.close();
            cstat.close();
        } catch (SQLException e) {
            System.out.println("Error saat ambil data setting: " + e.getMessage());
        }

        return list;
    }

    public boolean deleteSetting(int s_id, String modifiedBy) {
        try {
            String query = "{call sp_setStatus_setting(?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, s_id);
            cstat.setString(2, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat hapus setting: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // === METODE PEMBAYARAN ===
    // =========================
    public boolean insertMetodePembayaran(String nama, String createdBy) {
        try {
            String query = "{call sp_insert_metodePembayaran(?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setString(1, nama);
            cstat.setString(2, createdBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat insert metode pembayaran: " + e.getMessage());
            return false;
        }
    }


    public boolean isMetodePembayaranExist(int mpbId) {
        boolean exists = false;
        String query = "SELECT dbo.udf_isMetodePembayaranExist(?)";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, mpbId);
            result = pstat.executeQuery();

            if (result.next()) {
                exists = result.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println("Error saat cek keberadaan metode pembayaran: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                System.out.println("Error saat menutup resource: " + ex.getMessage());
            }
        }

        return exists;
    }

    public boolean updateMetodePembayaran(int mpbId, String nama, String modifiedBy) {
        try {
            String query = "{call sp_update_metodePembayaran(?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, mpbId);
            cstat.setString(2, nama);
            cstat.setString(3, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat update metode pembayaran: " + e.getMessage());
            return false;
        }
    }

    public List<MetodePembayaran> getListMetodePembayaran(String search, Integer status, String sortColumn, String sortOrder) {
        List<MetodePembayaran> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_metode_pembayaran(?, ?, ?, ?)}");

            cstat.setString(1, search);
            if (status == null) {
                cstat.setNull(2, Types.INTEGER);
            } else {
                cstat.setInt(2, status);
            }
            cstat.setString(3, sortColumn);
            cstat.setString(4, sortOrder);

            result = cstat.executeQuery();

            while (result.next()) {
                list.add(new MetodePembayaran(
                        result.getInt("mpb_id"),
                        result.getString("mpb_nama"),
                        result.getInt("mpb_status"),
                        result.getString("mpb_created_by"),
                        result.getDate("mpb_created_date"),
                        result.getString("mpb_modif_by"),
                        result.getDate("mpb_modif_date")
                ));
            }

            result.close();
            cstat.close();

        } catch (SQLException e) {
            System.out.println("Error saat ambil data metode pembayaran: " + e.getMessage());
        }

        return list;
    }

    public boolean deleteMetodePembayaran(int mpbId, String modifiedBy) {
        try {
            String query = "{call sp_setStatus_metodePembayaran(?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, mpbId);
            cstat.setString(2, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat mengubah status metode pembayaran: " + e.getMessage());
            return false;
        }
    }

    public boolean insertProduk(int jp_id, String p_nama, Double p_harga, String p_satuan, int p_stok, String p_deskripsi, String p_gambar, String createdBy) {
        try {
            String query = "{call sp_insert_produk(?, ?, ?, ?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, jp_id);
            cstat.setString(2, p_nama);
            cstat.setDouble(3, p_harga);
            cstat.setString(4, p_satuan);
            cstat.setInt(5, p_stok);
            cstat.setString(6, p_deskripsi);
            cstat.setString(7, p_gambar);
            cstat.setString(8, createdBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat insert produk: " + e.getMessage());
            return false;
        }
    }

    public boolean isProdukExist(int p_id) {
        boolean exists = false;
        String query = "SELECT dbo.udf_isProdukExist(?)";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, p_id);
            result = pstat.executeQuery();

            if (result.next()) {
                exists = result.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println("Error saat cek keberadaan produk: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstat != null) pstat.close();
            } catch (SQLException ex) {
                System.out.println("Error saat menutup resource: " + ex.getMessage());
            }
        }

        return exists;
    }

    public boolean updateProduk(int p_id, int jp_id, String p_nama, Double p_harga, String p_satuan, int stok, String p_deskripsi, String p_gambar, String modifBy) {
        try {
            String query = "{call sp_update_produk(? ,?, ?, ?, ?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, p_id);
            cstat.setInt(2, jp_id);
            cstat.setString(3, p_nama);
            cstat.setDouble(4, p_harga);
            cstat.setString(5, p_satuan);
            cstat.setInt(6, stok);
            cstat.setString(7, p_deskripsi);
            cstat.setString(8, p_gambar);
            cstat.setString(9, modifBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
//            System.out.println("Error saat edit produk: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<JenisProduk> getNamaJenisProduk () {
        List<JenisProduk> list = new ArrayList<>();
        String query = "SELECT jp_id, jp_nama FROM jenis_produk WHERE jp_status = 1";

        try {
            pstat = conn.prepareStatement(query);
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("jp_id");
                String nama = result.getString("jp_nama");
                list.add(new JenisProduk(id, nama));
            }
        } catch (SQLException e) {
            System.out.println("Error mengambil data jenis produk: " + e.getMessage());
        }

        return list;
    }

    public List<Produk> getListProduk(String search, Integer status, String sortColumn, String sortOrder) {
        List<Produk> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_produk(?, ?, ?, ?)}");
            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);
            cstat.setString(3, sortColumn);
            cstat.setString(4, sortOrder);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new Produk(
                        result.getInt("p_id"),
                        result.getInt("jp_id"),
                        result.getString("jp_nama"),
                        result.getString("p_nama"),
                        result.getDouble("p_harga"),
                        result.getString("p_satuan"),
                        result.getInt("p_stok"),
                        result.getString("p_deskripsi"),
                        result.getString("p_gambar"),
                        result.getInt("p_status"),
                        result.getString("p_created_by"),
                        result.getDate("p_created_date"),
                        result.getString("p_modif_by"),
                        result.getDate("p_modif_date")
                ));
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteProduk(int p_id, String modifiedBy) {
        try {
            String query = "{call sp_setStatus_produk(?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, p_id);
            cstat.setString(2, modifiedBy);

            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat hapus produk: " + e.getMessage());
            return false;
        }
    }

    /*---- Transaksi Penjualan ----*/
    public boolean insertPenjualan(int s_id, int mpb_id, int kry_id, double total_harga, String createdBy, Integer pnj_status, List<DetailPenjualan> detailTransaksi, List<DetailPromo> detailPromo) {
        try {
            String query = "{call sp_insert_penjualan(?, ?, ?, ?, ?, ?, ?, ?)}";
            SQLServerCallableStatement cstat = (SQLServerCallableStatement) ((SQLServerConnection) conn).prepareCall(query);

            // Parameter biasa
            cstat.setInt(1, s_id);
            cstat.setInt(2, mpb_id);
            cstat.setInt(3, kry_id);
            cstat.setDouble(4, total_harga);
            cstat.setString(5, createdBy);
            if (pnj_status == null) {
                cstat.setInt(6, 1); // default status
            }
            else {
                cstat.setInt(6, pnj_status);
            }

            // Parameter tabel transaksi
            SQLServerDataTable dtTrans = new SQLServerDataTable();
            dtTrans.addColumnMetadata("p_id", java.sql.Types.INTEGER);
            dtTrans.addColumnMetadata("dp_kuantitas", java.sql.Types.INTEGER);
            dtTrans.addColumnMetadata("dp_subtotal", java.sql.Types.DECIMAL);

            for (DetailPenjualan d : detailTransaksi) {
                dtTrans.addRow(d.getP_id(), d.getDp_kuantitas(), d.getDp_subTotal());
            }
            cstat.setStructured(7, "detail_transaksi_penjualan_type", dtTrans);

            // Parameter tabel promo
            SQLServerDataTable dtPromo = new SQLServerDataTable();
            dtPromo.addColumnMetadata("pr_id", java.sql.Types.INTEGER);

            for (DetailPromo p : detailPromo) {
                dtPromo.addRow(p.getPr_id());
            }
            cstat.setStructured(8, "detail_promo_penjualan_type", dtPromo);

            cstat.execute();
            messageBox.alertInfo("Transaksi Berhasil Ditambahkan!");
            return true;

        } catch (SQLException e) {
            messageBox.alertError("Error saat insert penjualan: " + e.getMessage());
            return false;
        }
    }

    public List<MetodePembayaran> getListNamaMetodePembayaran() {
        List<MetodePembayaran> list = new ArrayList<>();
        String query = "SELECT mpb_id, mpb_nama FROM metode_pembayaran WHERE mpb_status = 1";

        try {
            pstat = conn.prepareStatement(query);
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("mpb_id");
                String nama = result.getString("mpb_nama");
                list.add(new MetodePembayaran(id, nama));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public List<Promo> getListNamaPromo() {
        List<Promo> list = new ArrayList<>();
        String query = "SELECT pr_id, pr_nama, pr_persentase FROM promo WHERE pr_status = 1";

        try {
            pstat = conn.prepareStatement(query);
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("pr_id");
                String nama = result.getString("pr_nama");
                Double persentase = result.getDouble("pr_persentase");
                list.add(new Promo(id, nama, persentase));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public Integer getIdKaryawanByUsername(String username) {
        String query = "SELECT kry_id FROM karyawan WHERE kry_status = 1 AND kry_username = ?";

        int id = 0;
        try {
            pstat = conn.prepareStatement(query);
            pstat.setString(1, username);
            result = pstat.executeQuery();

            while (result.next()) {
                id = result.getInt("kry_id");
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return id;
    }

    public List<Penjualan> getListPenjualan(String search, Integer status) {
        List<Penjualan> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_penjualan(?, ?)}");
            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new Penjualan(
                        result.getInt("pnj_id"),
                        result.getDate("pnj_created_date"),
                        result.getInt("s_id"),
                        result.getInt("mpb_id"),
                        result.getInt("kry_id"),
                        result.getString("s_nama"),
                        result.getString("mpb_nama"),
                        result.getString("kry_nama"),
                        result.getDouble("pnj_total_harga"),
                        result.getInt("pnj_status")
                ));
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Penjualan> getListPenjualanByCategory(String search, Integer status, String category) {
        List<Penjualan> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_penjualan_by_category(?, ?, ?)}");
            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);
            cstat.setString(3, category);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new Penjualan(
                        result.getInt("pnj_id"),
                        result.getInt("s_id"),
                        result.getInt("mpb_id"),
                        result.getInt("kry_id"),
                        result.getString("s_nama"),
                        result.getString("mpb_nama"),
                        result.getString("kry_nama"),
                        result.getDouble("pnj_total_harga"),
                        result.getInt("pnj_status")
                ));
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<DetailPenjualan> getListDetailPenjualan(int pnj_id) {
        List<DetailPenjualan> list = new ArrayList<>();
        String query = "SELECT p.p_nama, dtp.dp_kuantitas, p.p_harga FROM detail_transaksi_penjualan dtp\n" +
                "JOIN produk p ON dtp.p_id = p.p_id\n" +
                "WHERE dtp.pnj_id = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, pnj_id);
            result = pstat.executeQuery();

            while (result.next()) {
                String nama = result.getString("p_nama");
                int jumlah = result.getInt("dp_kuantitas");
                Double harga = result.getDouble("p_harga");
                list.add(new DetailPenjualan(nama, jumlah, harga));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    //===============================================================
//                  SP INSERT STOCK KELUAR
//===============================================================
    public boolean insertStokKeluar(int kry_id, int sk_jumlah_keluar, String sk_keterangan, String createdBy, List<DetailStokKeluar> detailStokKeluar) {
        try {
            String query = "{call sp_insert_stok_keluar(?, ?, ?, ?, ?)}";
            SQLServerCallableStatement cstat = (SQLServerCallableStatement) ((SQLServerConnection) conn).prepareCall(query);

            // Parameter biasa
            cstat.setInt(1, kry_id);
            cstat.setInt(2, sk_jumlah_keluar);
            cstat.setString(3, sk_keterangan);
            cstat.setString(4, createdBy);

            // Parameter tabel transaksi
            SQLServerDataTable dtTrans = new SQLServerDataTable();
            dtTrans.addColumnMetadata("p_id", java.sql.Types.INTEGER);
            dtTrans.addColumnMetadata("dsk_kuantitas", java.sql.Types.INTEGER);

            for (DetailStokKeluar d : detailStokKeluar) {
                dtTrans.addRow(d.getP_id(), d.getDsk_kuantitas());
            }
            cstat.setStructured(5, "detail_transaksi_stok_keluar_type", dtTrans);
            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat insert stok keluar: " + e.getMessage());
            return false;
        }
    }

    public List<Produk> getListNamaProduk() {
        List<Produk> list = new ArrayList<>();
        String query = "SELECT p_id, p_nama FROM produk WHERE p_status = 1";

        try {
            pstat = conn.prepareStatement(query);
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("p_id");
                String nama = result.getString("p_nama");
                list.add(new Produk(id, nama));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public List<Produk> getListStockProdukById(int p_id) {
        List<Produk> list = new ArrayList<>();
        String query = "SELECT p_stok FROM produk WHERE p_id = ? AND p_status = 1";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, p_id);
            result = pstat.executeQuery();

            while (result.next()) {
                int stok = result.getInt("p_stok");
                list.add(new Produk(stok));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public List<StokKeluar> getListStokKeluar(String search, Integer status) {
        List<StokKeluar> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_stok_keluar(?, ?)}");
            cstat.setString(1, search);
            if (status == null) cstat.setNull(2, Types.INTEGER); else cstat.setInt(2, status);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new StokKeluar(
                        result.getInt("sk_id"),
                        result.getInt("kry_id"),
                        result.getString("kry_nama"),
                        result.getDate("sk_tanggal_keluar"),
                        result.getInt("sk_jumlah_keluar"),
                        result.getString("sk_keterangan"),
                        result.getString("sk_created_by"),
                        result.getTimestamp("sk_created_date"),
                        result.getString("sk_modif_by"),
                        result.getTimestamp("sk_modif_date")
                ));
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //detail transaksai stok keluar
    public List<DetailStokKeluar> getDetailStokKeluarById(int sk_id) {
        List<DetailStokKeluar> list = new ArrayList<>();

        try {
            String query = "SELECT d.p_id, p.p_nama, d.dsk_kuantitas " +
                    "FROM detail_transaksi_stok_keluar d " +
                    "JOIN produk p ON d.p_id = p.p_id " +
                    "WHERE d.sk_id = ?";
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, sk_id);
            result = pstat.executeQuery();

            while (result.next()) {
                DetailStokKeluar detail = new DetailStokKeluar();
                detail.setP_id(result.getInt("p_id"));
                detail.setNama_produk(result.getString("p_nama"));
                detail.setKuantitas(result.getInt("dsk_kuantitas"));
                list.add(detail);
            }

            result.close();
            pstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public StokKeluar getStokKeluarById(int id) {
        try {
            String query = "SELECT * FROM stok_keluar WHERE sk_id = ?";
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, id);
            result = pstat.executeQuery();

            if (result.next()) {
                return new StokKeluar(
                        result.getInt("sk_id"),
                        result.getInt("kry_id"),
                        null, // kry_nama bisa diabaikan di sini
                        result.getDate("sk_tanggal_keluar"),
                        result.getInt("sk_jumlah_keluar"),
                        result.getString("sk_keterangan"),
                        result.getString("sk_created_by"),
                        result.getTimestamp("sk_created_date"),
                        result.getString("sk_modif_by"),
                        result.getTimestamp("sk_modif_date")
                );
            }

            result.close();
            pstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*--- Retur Barang ---*/

    public boolean insertReturBarang(int kry_id, int pnj_id,int rtr_jumlah_retur, String createdBy, List<DetailReturBarang> detailReturBarang) {
        try {
            String query = "{call sp_insert_retur_barang(?, ?, ?, ?, ?)}";
            SQLServerCallableStatement cstat = (SQLServerCallableStatement) ((SQLServerConnection) conn).prepareCall(query);

            // Parameter biasa
            cstat.setInt(1, kry_id);
            cstat.setInt(2, pnj_id);
            cstat.setInt(3, rtr_jumlah_retur);
            cstat.setString(4, createdBy);

            // Parameter tabel transaksi
            SQLServerDataTable dtTrans = new SQLServerDataTable();
            dtTrans.addColumnMetadata("p_id", java.sql.Types.INTEGER);
            dtTrans.addColumnMetadata("drp_alasan", Types.VARCHAR);
            dtTrans.addColumnMetadata("drp_kuantitas", Types.INTEGER);

            for (DetailReturBarang d : detailReturBarang) {
                dtTrans.addRow(d.getP_id(), d.getDrp_alasan(), d.getDrp_kuantitas());
            }
            cstat.setStructured(5, "detail_transaksi_retur_barang_type", dtTrans);
            cstat.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saat insert retur barang: " + e.getMessage());
            return false;
        }
    }

    public List<Produk> getProdukFromDetailPenjualanById(int pnjId) {
        List<Produk> list = new ArrayList<>();
        String query = "EXEC sp_getProductFromDetailPenjualan_by_id ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, pnjId);
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("p_id");
                String nama = result.getString("p_nama");
                list.add(new Produk(id, nama));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public List<ReturBarang> getListReturBarang(String search) {
        List<ReturBarang> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_retur_pembeli(?)}");
            cstat.setString(1, search);

            result = cstat.executeQuery();
            while (result.next()) {
                list.add(new ReturBarang(
                        result.getInt("rtr_id"),
                        result.getDate("rtr_tanggal_retur").toLocalDate().plusDays(2),
                        result.getInt("rtr_jumlah_retur"),
                        result.getInt("pnj_id"),
                        result.getInt("kry_id"),
                        result.getString("nama_karyawan")
                ));
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<DetailReturBarang> getListDetailReturBarang(int rtr_id) {
        List<DetailReturBarang> list = new ArrayList<>();
        String query = "SELECT p.p_nama, drp.drp_kuantitas, drp.drp_alasan FROM detail_transaksi_retur_pembeli drp\n" +
                "JOIN produk p ON drp.p_id = p.p_id WHERE drp.rtr_id = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, rtr_id);
            result = pstat.executeQuery();

            while (result.next()) {
                String nama = result.getString("p_nama");
                int jumlah = result.getInt("drp_kuantitas");
                String alasan = result.getString("drp_alasan");
                list.add(new DetailReturBarang(nama, jumlah, alasan));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public Integer getListKuantitasProdukTerjualById(int p_id) {
        String query = "SELECT dp_kuantitas FROM detail_transaksi_penjualan WHERE p_id = ?";
        int kuantitas = 0;

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, p_id);
            result = pstat.executeQuery();

            while (result.next()) {
                kuantitas = result.getInt("dp_kuantitas");
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return kuantitas;
    }

    /*--- Pengiriman ---*/

    public List<karyawan> getListDriver() {
        List<karyawan> list = new ArrayList<>();
        String query = "SELECT kry_id, kry_nama FROM karyawan WHERE kry_status = 1 AND s_id = ?";

        try {
            pstat = conn.prepareStatement(query);
            pstat.setInt(1, 6); // 6 = ID jabatan "Driver"
            result = pstat.executeQuery();

            while (result.next()) {
                int id = result.getInt("kry_id");
                String nama = result.getString("kry_nama");
                list.add(new karyawan(id, nama));
            }
        } catch (SQLException e) {
            messageBox.alertError(e.getMessage());
        }

        return list;
    }

    public List<Pengiriman> getListPengiriman(int status) {
        List<Pengiriman> list = new ArrayList<>();

        try {
            CallableStatement cstat = conn.prepareCall("{CALL sp_getList_pengiriman(?, ?, ?, ?)}");
            cstat.setNull(1, Types.VARCHAR); // search = null
            cstat.setInt(2, status);
            cstat.setString(3, "png_id");     // sortColumn default
            cstat.setString(4, "ASC");        // sortOrder default

            ResultSet rs = cstat.executeQuery();

            while (rs.next()) {
                Integer png_id = rs.getObject("png_id") != null ? rs.getInt("png_id") : null;
                Integer pnj_id = rs.getObject("pnj_id") != null ? rs.getInt("pnj_id") : null;
                Integer kry_id = rs.getObject("kry_id") != null ? rs.getInt("kry_id") : null;

                Date tanggalPengiriman = rs.getDate("png_tanggal_pengiriman");
                Time jamPengiriman = rs.getTime("png_jam_pengiriman");
                String alamat = rs.getString("png_alamat_pengiriman");
                String createdBy = rs.getString("png_created_by");
                String modifBy = rs.getString("png_modif_by");
                Date createdDate = rs.getDate("png_created_date");
                Date modifDate = rs.getDate("png_modif_date");

//                Pengiriman p = new Pengiriman(
//                        png_id,
//                        kry_id,
//                        pnj_id,
//                        tanggalPengiriman,
//                        null,              // nama penerima tidak tersedia di SP
//                        alamat,
//                        jamPengiriman,
//                        status,
//                        createdBy,
//                        createdDate,
//                        modifBy,
//                        modifDate
//                );

//                list.add(p);
            }

            rs.close();
            cstat.close();
        } catch (SQLException e) {
            messageBox.alertError("Gagal memuat data pengiriman: " + e.getMessage());
        }

        return list;
    }

    public boolean updatePengiriman(int png_id, Integer kry_id, String alamatPengiriman,
                                    Date tanggalPengiriman, Time jamPengiriman,
                                    int statusPengiriman, String namaPenerima, String modifBy) {
        try {
            String query = "{call sp_update_pengiriman(?, ?, ?, ?, ?, ?, ?, ?)}";
            cstat = conn.prepareCall(query);

            cstat.setInt(1, png_id);

            if (kry_id == null) {
                cstat.setNull(2, java.sql.Types.INTEGER);
            } else {
                cstat.setInt(2, kry_id);
            }

            cstat.setString(3, alamatPengiriman);
            cstat.setDate(4, tanggalPengiriman);
            cstat.setTime(5, jamPengiriman);
            cstat.setInt(6, statusPengiriman);
            cstat.setString(7, namaPenerima); // ← tambahkan nama penerima
            cstat.setString(8, modifBy);      // ← terakhir: modif_by

            cstat.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Pengiriman> getListPenjualanToPengiriman(Integer status) {
        List<Pengiriman> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_penjualan_to_pengiriman}");
            result = cstat.executeQuery();
            while (result.next()) {
                Pengiriman data = new Pengiriman();
                data.setPnj_id(result.getInt("pnj_id"));
                data.setPnj_created_date(result.getDate("pnj_created_date"));
                data.setPnj_created_by(result.getString("pnj_created_by"));
                data.setPnj_total_harga(result.getDouble("pnj_total_harga"));
                list.add(data);
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Pengiriman> getListPengirimanByStatus(int status) {
        List<Pengiriman> list = new ArrayList<>();
        try {
            cstat = conn.prepareCall("{CALL sp_getList_pengiriman_by_status(?)}");
            cstat.setInt(1, status);
            result = cstat.executeQuery();

            while (result.next()) {
                Pengiriman pengiriman = new Pengiriman();
                pengiriman.setPng_id(result.getInt("png_id")); // penting
                pengiriman.setPnj_id(result.getInt("pnj_id"));
                pengiriman.setKry_nama(result.getString("kry_nama"));
                pengiriman.setPng_nama(result.getString("png_nama_penerima")); // ← ini juga penting
                pengiriman.setPng_tanggal(result.getDate("png_tanggal_pengiriman"));
                pengiriman.setPng_jam(result.getTime("png_jam_pengiriman"));
                pengiriman.setPng_status(result.getInt("png_status_pengiriman")); // ← status pengiriman
                pengiriman.setPng_alamat(result.getString("png_alamat_pengiriman"));

                list.add(pengiriman);
            }
            result.close();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void selesaiPengiriman(int pnj_id, String modif_by) {
        try {
            CallableStatement cstat = conn.prepareCall("{call sp_selesai_pengiriman(?, ?)}");
            cstat.setInt(1, pnj_id);
            cstat.setString(2, modif_by);
            cstat.execute();
            cstat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        DBConnect connection = new DBConnect();
    }
}