package Dashboard;

import database.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardAdmin implements Initializable {
    private String userAccess;

    @FXML
    private TextField tbjumlahkaryawan;

    @FXML
    private TextField tbjumlahproduk;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    DBConnect connection = new DBConnect();

    private void loadDataJumlah() {
        try {
            String queryKaryawan = "SELECT COUNT(*) AS jumlah FROM karyawan WHERE kry_status = 1";
            String queryProduk = "SELECT COUNT(*) AS jumlah FROM produk WHERE p_status = 1";

            // Koneksi
            java.sql.Statement stmt = connection.stat; // asumsi kamu sudah punya `connection.stat`

            // Hitung jumlah karyawan
            ResultSet rsKaryawan = stmt.executeQuery(queryKaryawan);
            if (rsKaryawan.next()) {
                int jumlahKaryawan = rsKaryawan.getInt("jumlah");
                tbjumlahkaryawan.setText(String.valueOf(jumlahKaryawan));
            }

            // Hitung jumlah produk
            ResultSet rsProduk = stmt.executeQuery(queryProduk);
            if (rsProduk.next()) {
                int jumlahProduk = rsProduk.getInt("jumlah");
                tbjumlahproduk.setText(String.valueOf(jumlahProduk));
            }

        } catch (SQLException e) {

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataJumlah();
        tbjumlahkaryawan.setEditable(false);
        tbjumlahproduk.setEditable(false);
    }
}
