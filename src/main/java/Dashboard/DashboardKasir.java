package Dashboard;

import Helper.MessageBox;
import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashboardKasir implements Initializable {
    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    @FXML
    private TextField tbjumlahpenjualan;

    @FXML
    private TextField tbjumlahpengiriman;

    @FXML
    private PieChart pieChart;

    @FXML
    private DatePicker tglMulai;

    @FXML
    private DatePicker tglSelesai;

    @FXML
    private Button btnBersihkan;

    @FXML
    private Button btnFilter;

    @FXML
    private Button btnDownloadLaporan;

    DBConnect connection = new DBConnect(); // sesuaikan dengan koneksi kamu

    MessageBox messageBox = new MessageBox();

    private void loadPieChartData() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            int jumlahPenjualan = 0;
            int jumlahPengiriman = 0;

            // Ambil jumlah penjualan
            String queryPenjualan = "SELECT COUNT(*) AS jumlah FROM penjualan where pnj_status = 1";
            PreparedStatement stmtPenjualan = connection.conn.prepareStatement(queryPenjualan);
            ResultSet rsPenjualan = stmtPenjualan.executeQuery();
            if (rsPenjualan.next()) {
                jumlahPenjualan = rsPenjualan.getInt("jumlah");
            }

            // Ambil jumlah pengiriman selesai
            String queryPengiriman = "SELECT COUNT(*) AS jumlah FROM pengiriman WHERE png_status_pengiriman = 2";
            PreparedStatement stmtPengiriman = connection.conn.prepareStatement(queryPengiriman);
            ResultSet rsPengiriman = stmtPengiriman.executeQuery();
            if (rsPengiriman.next()) {
                jumlahPengiriman = rsPengiriman.getInt("jumlah");
            }

            int total = jumlahPenjualan + jumlahPengiriman;
            if (total == 0) total = 1; // Hindari pembagian dengan nol

            // Hitung dan buat label persentase
            double persenPenjualan = (jumlahPenjualan * 100.0) / total;
            double persenPengiriman = (jumlahPengiriman * 100.0) / total;

            // Format label: "Nama (jumlah, persen%)"
            String labelPengiriman = String.format("Pengiriman Selesai (%d, %.1f%%)", jumlahPengiriman, persenPengiriman);
            String labelPenjualan  = String.format("Penjualan (%d, %.1f%%)", jumlahPenjualan, persenPenjualan);

            pieData.add(new PieChart.Data(labelPengiriman, jumlahPengiriman));
            pieData.add(new PieChart.Data(labelPenjualan, jumlahPenjualan));

            pieChart.setData(pieData);
            pieChart.setTitle("Perbandingan Pengiriman & Penjualan");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPieChartDataWithDateRange() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            int jumlahPenjualan = 0;
            int jumlahPengiriman = 0;

            if (tglMulai.getValue() == null || tglSelesai.getValue() == null) {
                messageBox.alertWarning("Harap pilih tanggal mulai dan tanggal selesai terlebih dahulu.");
                return;
            }

            if (tglSelesai.getValue().isBefore(tglMulai.getValue())) {
                messageBox.alertWarning("Tanggal selesai tidak boleh lebih awal dari tanggal mulai.");
                return;
            }

            Date startDate = Date.valueOf(tglMulai.getValue());
            Date endDate = Date.valueOf(tglSelesai.getValue().plusDays(1)); // Supaya termasuk tanggal akhir

            // Penjualan
            String queryPenjualan = "SELECT COUNT(*) AS jumlah FROM penjualan WHERE pnj_status = 1 AND pnj_created_date >= ? AND pnj_created_date < ?";
            PreparedStatement stmtPenjualan = connection.conn.prepareStatement(queryPenjualan);
            stmtPenjualan.setDate(1, startDate);
            stmtPenjualan.setDate(2, endDate);
            ResultSet rsPenjualan = stmtPenjualan.executeQuery();
            if (rsPenjualan.next()) {
                jumlahPenjualan = rsPenjualan.getInt("jumlah");
            }

            // Pengiriman
            String queryPengiriman = "SELECT COUNT(*) AS jumlah FROM pengiriman WHERE png_status_pengiriman = 2 AND png_modif_date >= ? AND png_modif_date < ?";
            PreparedStatement stmtPengiriman = connection.conn.prepareStatement(queryPengiriman);
            stmtPengiriman.setDate(1, startDate);
            stmtPengiriman.setDate(2, endDate);
            ResultSet rsPengiriman = stmtPengiriman.executeQuery();
            if (rsPengiriman.next()) {
                jumlahPengiriman = rsPengiriman.getInt("jumlah");
            }

            // Cek apakah ada data
            int total = jumlahPenjualan + jumlahPengiriman;
            if (total == 0) {
                pieChart.setTitle("Tidak Ada Data dalam Rentang Tanggal");
                pieData.clear();
                pieChart.setData(pieData);
                return;
            }

            double persenPenjualan = (jumlahPenjualan * 100.0) / total;
            double persenPengiriman = (jumlahPengiriman * 100.0) / total;

            String labelPenjualan = String.format("Penjualan (%d, %.1f%%)", jumlahPenjualan, persenPenjualan);
            String labelPengiriman = String.format("Pengiriman Selesai (%d, %.1f%%)", jumlahPengiriman, persenPengiriman);

            pieData.add(new PieChart.Data(labelPenjualan, jumlahPenjualan));
            pieData.add(new PieChart.Data(labelPengiriman, jumlahPengiriman));

            pieChart.setData(pieData);
            pieChart.setTitle("Perbandingan Penjualan dan Pengiriman (Filter Tanggal)");

            tbjumlahpenjualan.setText(String.valueOf(jumlahPenjualan));
            tbjumlahpengiriman.setText(String.valueOf(jumlahPengiriman));

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Gagal memuat data pie chart.");
        }
    }


    private void loadDataJumlah() {
        try {
            String queryPenjualan = "SELECT COUNT(*) AS jumlah FROM penjualan WHERE pnj_status = 1";
            String queryPengiriman = "SELECT COUNT(*) AS jumlah FROM pengiriman WHERE png_status_pengiriman = 2";

            Statement stmt = connection.stat;

            ResultSet rsPenjualan = stmt.executeQuery(queryPenjualan);
            int jumlahPenjualan = rsPenjualan.next() ? rsPenjualan.getInt("jumlah") : 0;
            tbjumlahpenjualan.setText(String.valueOf(jumlahPenjualan));

            ResultSet rsPengiriman = stmt.executeQuery(queryPengiriman);
            int jumlahPengiriman = rsPengiriman.next() ? rsPengiriman.getInt("jumlah") : 0;
            tbjumlahpengiriman.setText(String.valueOf(jumlahPengiriman));

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Terjadi kesalahan saat memuat data jumlah.");
        }
    }

    private void exportLaporanToCSV() {
        if (tglMulai.getValue() == null || tglSelesai.getValue() == null) {
            messageBox.alertWarning("Harap pilih tanggal mulai dan tanggal selesai terlebih dahulu.");
            return;
        }

//        if (tglSelesai.getValue().isBefore(tglMulai.getValue())) {
//            messageBox.alertWarning("Tanggal selesai tidak boleh lebih awal dari tanggal mulai.");
//            return;
//        }

        Date startDate = Date.valueOf(tglMulai.getValue());
        Date endDate = Date.valueOf(tglSelesai.getValue());

        // Ubah path sesuai yang kamu minta
        String filePath = "C:/raihan/semester 2/PRG 3 - Java/TheFreshChoice/laporan_transaksi.csv";

        try (java.io.PrintWriter writer = new java.io.PrintWriter(filePath)) {
            writer.println("Jenis,ID,Tanggal,Waktu,Total");

            // Penjualan
            String queryPenjualan = "SELECT pnj_id, pnj_created_date, pnj_total_harga FROM penjualan WHERE pnj_status = 1 AND pnj_created_date BETWEEN ? AND ?";
            PreparedStatement psPenjualan = connection.conn.prepareStatement(queryPenjualan);
            psPenjualan.setDate(1, startDate);
            psPenjualan.setDate(2, endDate);
            ResultSet rsPenjualan = psPenjualan.executeQuery();
            while (rsPenjualan.next()) {
                writer.printf("Penjualan,%d,%s,,%.2f%n",
                        rsPenjualan.getInt("pnj_id"),
                        rsPenjualan.getDate("pnj_created_date"),
                        rsPenjualan.getDouble("pnj_total_harga"));
            }

            // Pengiriman
            String queryPengiriman = "SELECT png_id, png_modif_date, png_jam_pengiriman FROM pengiriman WHERE png_status_pengiriman = 2 AND png_modif_date BETWEEN ? AND ?";
            PreparedStatement psPengiriman = connection.conn.prepareStatement(queryPengiriman);
            psPengiriman.setDate(1, startDate);
            psPengiriman.setDate(2, endDate);
            ResultSet rsPengiriman = psPengiriman.executeQuery();
            while (rsPengiriman.next()) {
                writer.printf("Pengiriman,%d,%s,%s,%n",
                        rsPengiriman.getInt("png_id"),
                        rsPengiriman.getDate("png_modif_date"),
                        rsPengiriman.getTime("png_jam_pengiriman"));
            }

            messageBox.alertInfo("Laporan berhasil disimpan di:\n" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            messageBox.alertError("Gagal mengunduh laporan: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPieChartData();
        loadDataJumlah();
        tbjumlahpenjualan.setEditable(false);
        tbjumlahpengiriman.setEditable(false);

        btnFilter.setOnAction(e -> {
            loadPieChartDataWithDateRange();
        });

        btnBersihkan.setOnAction(e -> {
            tglMulai.setValue(null);
            tglSelesai.setValue(null);
            loadPieChartData();
            loadDataJumlah();
        });
    }
}