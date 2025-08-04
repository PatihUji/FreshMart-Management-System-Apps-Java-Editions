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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardOwner implements Initializable {
    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    @FXML
    private TextField tbjumlahpenjualan;

    @FXML
    private TextField tbjumlahpengiriman;

    @FXML
    private TextField tbretur;

    @FXML
    private TextField tbstok;

    @FXML
    private PieChart pieChart;

    @FXML
    private PieChart pieChartPengiriman;

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

    @FXML
    private Button btnstok;

    @FXML
    private Button btnretur;

    @FXML
    private Button btnpenjualan;

    DBConnect connection = new DBConnect(); // sesuaikan dengan koneksi kamu

    MessageBox messageBox = new MessageBox();

    private void loadPieChartData() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            int jumlahPenjualan = 0;
            int jumlahRetur = 0;
            int jumlahStokKeluar = 0;
            int jumlahPengiriman = 0;

            // Penjualan
            String queryPenjualan = "SELECT ISNULL(SUM(dp.dp_kuantitas), 0) AS total_kuantitas " +
                    "FROM penjualan p " +
                    "JOIN detail_transaksi_penjualan dp ON p.pnj_id = dp.pnj_id " +
                    "WHERE p.pnj_status = 1";
            PreparedStatement stmtPenjualan = connection.conn.prepareStatement(queryPenjualan);
            ResultSet rsPenjualan = stmtPenjualan.executeQuery();
            if (rsPenjualan.next()) {
                jumlahPenjualan = rsPenjualan.getInt("total_kuantitas");
            }

            // Pengiriman
            String queryPengiriman = "SELECT COUNT(*) AS jumlah FROM pengiriman WHERE png_status_pengiriman = 2";
            PreparedStatement stmtPengiriman = connection.conn.prepareStatement(queryPengiriman);
            ResultSet rsPengiriman = stmtPengiriman.executeQuery();
            if (rsPengiriman.next()) {
                jumlahPengiriman = rsPengiriman.getInt("jumlah");
            }


            // Retur
            String queryRetur = "SELECT ISNULL(SUM(rtr_jumlah_retur), 0) AS total_retur FROM retur_pembeli";
            PreparedStatement stmtRetur = connection.conn.prepareStatement(queryRetur);
            ResultSet rsRetur = stmtRetur.executeQuery();
            if (rsRetur.next()) {
                jumlahRetur = rsRetur.getInt("total_retur");
            }

            // Stok Keluar
            String queryStok = "SELECT ISNULL(SUM(sk_jumlah_keluar), 0) AS total_stok FROM stok_keluar";
            PreparedStatement stmtStok = connection.conn.prepareStatement(queryStok);
            ResultSet rsStok = stmtStok.executeQuery();
            if (rsStok.next()) {
                jumlahStokKeluar = rsStok.getInt("total_stok");
            }

            int total = jumlahPenjualan + jumlahRetur + jumlahStokKeluar;
            if (total == 0) total = 1;

            double persenPenjualan = (jumlahPenjualan * 100.0) / total;
            double persenRetur = (jumlahRetur * 100.0) / total;
            double persenStok = (jumlahStokKeluar * 100.0) / total;

            pieData.add(new PieChart.Data(String.format("Penjualan (%d, %.1f%%)", jumlahPenjualan, persenPenjualan), jumlahPenjualan));
            pieData.add(new PieChart.Data(String.format("Retur (%d, %.1f%%)", jumlahRetur, persenRetur), jumlahRetur));
            pieData.add(new PieChart.Data(String.format("Stok Keluar (%d unit, %.1f%%)", jumlahStokKeluar, persenStok), jumlahStokKeluar));

            pieChart.setData(pieData);
            pieChart.setTitle("Perbandingan Penjualan, Retur, dan Stok Keluar");

            tbjumlahpenjualan.setText(String.valueOf(jumlahPenjualan));
            tbretur.setText(String.valueOf(jumlahRetur));
            tbstok.setText(String.valueOf(jumlahStokKeluar));
            tbjumlahpengiriman.setText(String.valueOf(jumlahPengiriman));

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Gagal memuat data PieChart.");
        }
    }

    private void loadPieChartDataWithDateRange() {
//        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
//
//        try {
//            int jumlahPenjualan = 0;
//            int jumlahPengiriman = 0;
//
//            if (tglMulai.getValue() == null || tglSelesai.getValue() == null) {
//                messageBox.alertWarning("Harap pilih tanggal mulai dan tanggal selesai terlebih dahulu.");
//                return;
//            }
//
//            if (tglSelesai.getValue().isBefore(tglMulai.getValue())) {
//                messageBox.alertWarning("Tanggal selesai tidak boleh lebih awal dari tanggal mulai.");
//                return;
//            }
//
//            java.sql.Date startDate = java.sql.Date.valueOf(tglMulai.getValue());
//            java.sql.Date endDate = java.sql.Date.valueOf(tglSelesai.getValue().plusDays(1)); // Supaya termasuk tanggal akhir
//
//            // Penjualan
//            String queryPenjualan = "SELECT COUNT(*) AS jumlah FROM penjualan WHERE pnj_status = 1 AND pnj_created_date >= ? AND pnj_created_date < ?";
//            PreparedStatement stmtPenjualan = connection.conn.prepareStatement(queryPenjualan);
//            stmtPenjualan.setDate(1, startDate);
//            stmtPenjualan.setDate(2, endDate);
//            ResultSet rsPenjualan = stmtPenjualan.executeQuery();
//            if (rsPenjualan.next()) {
//                jumlahPenjualan = rsPenjualan.getInt("jumlah");
//            }
//
//            // Pengiriman
//            String queryPengiriman = "SELECT COUNT(*) AS jumlah FROM pengiriman WHERE png_status_pengiriman = 2 AND png_modif_date >= ? AND png_modif_date < ?";
//            PreparedStatement stmtPengiriman = connection.conn.prepareStatement(queryPengiriman);
//            stmtPengiriman.setDate(1, startDate);
//            stmtPengiriman.setDate(2, endDate);
//            ResultSet rsPengiriman = stmtPengiriman.executeQuery();
//            if (rsPengiriman.next()) {
//                jumlahPengiriman = rsPengiriman.getInt("jumlah");
//            }
//
//            // Cek apakah ada data
//            int total = jumlahPenjualan + jumlahPengiriman;
//            if (total == 0) {
//                pieChart.setTitle("Tidak Ada Data dalam Rentang Tanggal");
//                pieData.clear();
//                pieChart.setData(pieData);
//                return;
//            }
//
//            double persenPenjualan = (jumlahPenjualan * 100.0) / total;
//            double persenPengiriman = (jumlahPengiriman * 100.0) / total;
//
//            String labelPenjualan = String.format("Penjualan (%d, %.1f%%)", jumlahPenjualan, persenPenjualan);
//            String labelPengiriman = String.format("Pengiriman Selesai (%d, %.1f%%)", jumlahPengiriman, persenPengiriman);
//
//            pieData.add(new PieChart.Data(labelPenjualan, jumlahPenjualan));
//            pieData.add(new PieChart.Data(labelPengiriman, jumlahPengiriman));
//
//            pieChart.setData(pieData);
//            pieChart.setTitle("Perbandingan Penjualan dan Pengiriman (Filter Tanggal)");
//
//            tbjumlahpenjualan.setText(String.valueOf(jumlahPenjualan));
//            tbjumlahpengiriman.setText(String.valueOf(jumlahPengiriman));
//
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//            messageBox.alertError("Gagal memuat data pie chart.");
//        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            int jumlahPenjualan = 0;
            int jumlahRetur = 0;
            int jumlahStokKeluar = 0;

            if (tglMulai.getValue() == null || tglSelesai.getValue() == null) {
                messageBox.alertWarning("Harap pilih tanggal mulai dan tanggal selesai terlebih dahulu.");
                return;
            }

            if (tglSelesai.getValue().isBefore(tglMulai.getValue())) {
                messageBox.alertWarning("Tanggal selesai tidak boleh lebih awal dari tanggal mulai.");
                return;
            }

            java.sql.Date startDate = java.sql.Date.valueOf(tglMulai.getValue());
            java.sql.Date endDate = java.sql.Date.valueOf(tglSelesai.getValue().plusDays(1)); // Supaya termasuk tanggal akhir

            // Penjualan
            String queryPenjualan = "SELECT ISNULL(SUM(dp.dp_kuantitas), 0) AS total_kuantitas " +
                    "FROM penjualan p " +
                    "JOIN detail_transaksi_penjualan dp ON p.pnj_id = dp.pnj_id " +
                    "WHERE p.pnj_status = 1 AND p.pnj_created_date >= ? AND p.pnj_created_date < ?";
            PreparedStatement stmtPenjualan = connection.conn.prepareStatement(queryPenjualan);
            stmtPenjualan.setDate(1, startDate);
            stmtPenjualan.setDate(2, endDate);
            ResultSet rsPenjualan = stmtPenjualan.executeQuery();
            if (rsPenjualan.next()) {
                jumlahPenjualan = rsPenjualan.getInt("total_kuantitas");
            }

            // Retur
            String queryRetur = "SELECT ISNULL(SUM(rtr_jumlah_retur), 0) AS total_retur " +
                    "FROM retur_pembeli " +
                    "WHERE rtr_tanggal_retur >= ? AND rtr_tanggal_retur < ?";
            PreparedStatement stmtRetur = connection.conn.prepareStatement(queryRetur);
            stmtRetur.setDate(1, startDate);
            stmtRetur.setDate(2, endDate);
            ResultSet rsRetur = stmtRetur.executeQuery();
            if (rsRetur.next()) {
                jumlahRetur = rsRetur.getInt("total_retur");
            }

            // Stok Keluar
            String queryStok = "SELECT ISNULL(SUM(sk_jumlah_keluar), 0) AS total_stok " +
                    "FROM stok_keluar " +
                    "WHERE sk_tanggal_keluar >= ? AND sk_tanggal_keluar < ?";
            PreparedStatement stmtStok = connection.conn.prepareStatement(queryStok);
            stmtStok.setDate(1, startDate);
            stmtStok.setDate(2, endDate);
            ResultSet rsStok = stmtStok.executeQuery();
            if (rsStok.next()) {
                jumlahStokKeluar = rsStok.getInt("total_stok");
            }

            int total = jumlahPenjualan + jumlahRetur + jumlahStokKeluar;
            if (total == 0) {
                pieChart.setTitle("Tidak Ada Data dalam Rentang Tanggal");
                pieData.clear();
                pieChart.setData(pieData);
                tbjumlahpenjualan.setText("0");
                tbretur.setText("0");
                tbstok.setText("0");
                return;
            }

            double persenPenjualan = (jumlahPenjualan * 100.0) / total;
            double persenRetur = (jumlahRetur * 100.0) / total;
            double persenStok = (jumlahStokKeluar * 100.0) / total;

            pieData.add(new PieChart.Data(String.format("Penjualan (%d, %.1f%%)", jumlahPenjualan, persenPenjualan), jumlahPenjualan));
            pieData.add(new PieChart.Data(String.format("Retur (%d, %.1f%%)", jumlahRetur, persenRetur), jumlahRetur));
            pieData.add(new PieChart.Data(String.format("Stok Keluar (%d, %.1f%%)", jumlahStokKeluar, persenStok), jumlahStokKeluar));

            pieChart.setData(pieData);
            pieChart.setTitle("Perbandingan Penjualan, Retur, dan Stok Keluar (Filter Tanggal)");

            tbjumlahpenjualan.setText(String.valueOf(jumlahPenjualan));
            tbretur.setText(String.valueOf(jumlahRetur));
            tbstok.setText(String.valueOf(jumlahStokKeluar));

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Gagal memuat data pie chart.");
        }
    }

    private void loadPieChartPengirimanWithDateRange() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            if (tglMulai.getValue() == null || tglSelesai.getValue() == null) {
                messageBox.alertWarning("Harap pilih tanggal mulai dan tanggal selesai terlebih dahulu.");
                return;
            }

            if (tglSelesai.getValue().isBefore(tglMulai.getValue())) {
                messageBox.alertWarning("Tanggal selesai tidak boleh lebih awal dari tanggal mulai.");
                return;
            }

            java.sql.Date startDate = java.sql.Date.valueOf(tglMulai.getValue());
            java.sql.Date endDate = java.sql.Date.valueOf(tglSelesai.getValue().plusDays(1)); // Supaya termasuk tanggal akhir

            int pengirimanBelum = 0;
            int pengirimanSedang = 0;
            int pengirimanSelesai = 0;

            String query = "SELECT png_status_pengiriman, COUNT(*) AS jumlah " +
                    "FROM pengiriman " +
                    "WHERE png_modif_date >= ? AND png_modif_date < ? " +
                    "GROUP BY png_status_pengiriman";

            PreparedStatement stmt = connection.conn.prepareStatement(query);
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int status = rs.getInt("png_status_pengiriman");
                int jumlah = rs.getInt("jumlah");

                switch (status) {
                    case 0:
                        pengirimanBelum = jumlah;
                        break;
                    case 1:
                        pengirimanSedang = jumlah;
                        break;
                    case 2:
                        pengirimanSelesai = jumlah;
                        break;
                }
            }

            int total = pengirimanBelum + pengirimanSedang + pengirimanSelesai;
            if (total == 0) {
                pieChartPengiriman.setTitle("Tidak Ada Data Pengiriman pada Rentang Tanggal");
                pieChartPengiriman.setData(pieData);
                return;
            }

            double persenBelum = pengirimanBelum * 100.0 / total;
            double persenSedang = pengirimanSedang * 100.0 / total;
            double persenSelesai = pengirimanSelesai * 100.0 / total;

            pieData.add(new PieChart.Data(
                    String.format("Belum Dikirim (%d, %.1f%%)", pengirimanBelum, persenBelum),
                    pengirimanBelum
            ));
            pieData.add(new PieChart.Data(
                    String.format("Sedang Dikirim (%d, %.1f%%)", pengirimanSedang, persenSedang),
                    pengirimanSedang
            ));
            pieData.add(new PieChart.Data(
                    String.format("Selesai Dikirim (%d, %.1f%%)", pengirimanSelesai, persenSelesai),
                    pengirimanSelesai
            ));

            pieChartPengiriman.setData(pieData);
            pieChartPengiriman.setTitle("Status Pengiriman (Filter Tanggal)");

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Gagal memuat data pie chart pengiriman dengan filter tanggal.");
        }
    }


    private void loadPieChartPengiriman() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            int pengirimanBelum = 0;
            int pengirimanSedang = 0;
            int pengirimanSelesai = 0;

            // Query jumlah pengiriman berdasarkan status
            String query = "SELECT png_status_pengiriman, COUNT(*) AS jumlah " +
                    "FROM pengiriman GROUP BY png_status_pengiriman";
            PreparedStatement stmt = connection.conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int status = rs.getInt("png_status_pengiriman");
                int jumlah = rs.getInt("jumlah");

                switch (status) {
                    case 0:
                        pengirimanBelum = jumlah;
                        break;
                    case 1:
                        pengirimanSedang = jumlah;
                        break;
                    case 2:
                        pengirimanSelesai = jumlah;
                        break;
                }
            }

            int total = pengirimanBelum + pengirimanSedang + pengirimanSelesai;
            if (total == 0) total = 1; // Hindari pembagian dengan nol

            double persenBelum = pengirimanBelum * 100.0 / total;
            double persenSedang = pengirimanSedang * 100.0 / total;
            double persenSelesai = pengirimanSelesai * 100.0 / total;

            pieData.add(new PieChart.Data(
                    String.format("Belum Dikirim (%d, %.1f%%)", pengirimanBelum, persenBelum),
                    pengirimanBelum
            ));
            pieData.add(new PieChart.Data(
                    String.format("Sedang Dikirim (%d, %.1f%%)", pengirimanSedang, persenSedang),
                    pengirimanSedang
            ));
            pieData.add(new PieChart.Data(
                    String.format("Selesai Dikirim (%d, %.1f%%)", pengirimanSelesai, persenSelesai),
                    pengirimanSelesai
            ));

            pieChartPengiriman.setData(pieData);
            pieChartPengiriman.setTitle("Status Pengiriman");

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Gagal memuat data PieChart Pengiriman.");
        }
    }



    private void loadDataJumlah() {
        try {
//            String queryPenjualan = "SELECT COUNT(*) AS jumlah FROM penjualan WHERE pnj_status = 1";
            String queryPengiriman = "SELECT COUNT(*) AS jumlah FROM pengiriman WHERE png_status_pengiriman = 2";

            Statement stmt = connection.stat;

//            ResultSet rsPenjualan = stmt.executeQuery(queryPenjualan);
//            int jumlahPenjualan = rsPenjualan.next() ? rsPenjualan.getInt("jumlah") : 0;
//            tbjumlahpenjualan.setText(String.valueOf(jumlahPenjualan));

            ResultSet rsPengiriman = stmt.executeQuery(queryPengiriman);
            int jumlahPengiriman = rsPengiriman.next() ? rsPengiriman.getInt("jumlah") : 0;
//            tbjumlahpengiriman.setText(String.valueOf(jumlahPengiriman));

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Terjadi kesalahan saat memuat data jumlah.");
        }
    }

    public LocalDate getTglMulai() {
        return tglMulai.getValue();
    }

    public LocalDate getTglSelesai() {
        return tglSelesai.getValue();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPieChartData();
        loadPieChartPengiriman();
        loadDataJumlah();
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1); // 1 Juli 2025
        LocalDate lastDay = YearMonth.now().atEndOfMonth();     // 31 Juli 2025

        tglMulai.setValue(firstDay);
        tglSelesai.setValue(lastDay);

        btnFilter.setOnAction(e -> {
            loadPieChartDataWithDateRange();
            loadPieChartPengirimanWithDateRange();
        });

        btnBersihkan.setOnAction(e -> {
            tglMulai.setValue(null);
            tglSelesai.setValue(null);
            loadPieChartData();
            loadPieChartPengiriman();
            loadDataJumlah();

            tglMulai.setValue(firstDay);
            tglSelesai.setValue(lastDay);
        });
    }
}
