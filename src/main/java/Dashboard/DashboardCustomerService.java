package Dashboard;

import Helper.MessageBox;
import database.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardCustomerService implements Initializable {

    @FXML
    private Button btnFilter;

    @FXML
    private Button btnBersihkan;

    @FXML
    private TextField tbstok;

    @FXML
    private DatePicker tglmulai;

    @FXML
    private DatePicker tglselesai;

    @FXML
    private BarChart viewstok;

    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    DBConnect connection = new DBConnect();
    MessageBox messageBox = new MessageBox();

    @FXML
    private TextField tbprodukterbanyak;


    private void loadBarChartStokKeluarByMonth() {
        /// /////////////Month/////////////////////
        viewstok.getData().clear(); // Hapus data sebelumnya

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Retur Produk / Bulan");

        try {
            // Default tanggal jika tidak dipilih: dari awal tahun ini sampai hari ini
            LocalDate defaultStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate defaultEnd = LocalDate.of(LocalDate.now().getYear(), 12, 31);
            tglselesai.setValue(LocalDate.now());


            // Ambil dari DatePicker, jika null pakai default
            LocalDate startDate = (tglmulai.getValue() != null) ? tglmulai.getValue() : defaultStart;
            LocalDate endDate = (tglselesai.getValue() != null) ? tglselesai.getValue().plusDays(1) : defaultEnd.plusDays(1); // plusDays biar inclusive

            // Konversi ke SQL Date
            java.sql.Date sqlStart = java.sql.Date.valueOf(startDate);
            java.sql.Date sqlEnd = java.sql.Date.valueOf(endDate);

            String query = "SELECT FORMAT(rtr_tanggal_retur, 'MMMM yyyy', 'id-ID') AS bulan_tahun, " +
                    "SUM(rtr_jumlah_retur) AS total " +
                    "FROM retur_pembeli " +
                    "WHERE rtr_tanggal_retur >= ? AND rtr_tanggal_retur < ? " +
                    "GROUP BY FORMAT(rtr_tanggal_retur, 'MMMM yyyy', 'id-ID') " +
                    "ORDER BY MIN(rtr_tanggal_retur) ASC";

            PreparedStatement stmt = connection.conn.prepareStatement(query);
            stmt.setDate(1, sqlStart);
            stmt.setDate(2, sqlEnd);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String bulanTahun = rs.getString("bulan_tahun");
                int total = rs.getInt("total");
                series.getData().add(new XYChart.Data<>(bulanTahun, total));
            }

            viewstok.getData().add(series);
            viewstok.setTitle("Retur Produk Bulanan");

            // Tampilkan juga di textfield tbstok
            int totalStok = series.getData().stream()
                    .mapToInt(data -> data.getYValue().intValue())
                    .sum();
            tbstok.setText(String.valueOf(totalStok));

        } catch (SQLException e) {
            e.printStackTrace();
            messageBox.alertError("Gagal memuat data BarChart stok keluar.");
        }

        /// //////////// DAY//////////////////
//        viewstok.getData().clear();
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Stok Keluar per Hari");
//
//        try {
//            LocalDate defaultStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
//            LocalDate defaultEnd = LocalDate.now();
//
//            tglselesai.setValue(LocalDate.now());
//
//            LocalDate startDate = (tglmulai.getValue() != null) ? tglmulai.getValue() : defaultStart;
//            LocalDate endDate = (tglselesai.getValue() != null) ? tglselesai.getValue().plusDays(1) : defaultEnd.plusDays(1);
//
//            java.sql.Date sqlStart = java.sql.Date.valueOf(startDate);
//            java.sql.Date sqlEnd = java.sql.Date.valueOf(endDate);
//
//            String query = "SELECT CONVERT(varchar, sk_tanggal_keluar, 23) AS tanggal, " +
//                    "SUM(sk_jumlah_keluar) AS total " +
//                    "FROM stok_keluar " +
//                    "WHERE sk_tanggal_keluar >= ? AND sk_tanggal_keluar < ? " +
//                    "GROUP BY CONVERT(varchar, sk_tanggal_keluar, 23) " +
//                    "ORDER BY MIN(sk_tanggal_keluar)";
//
//            PreparedStatement stmt = connection.conn.prepareStatement(query);
//            stmt.setDate(1, sqlStart);
//            stmt.setDate(2, sqlEnd);
//
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                String tanggal = rs.getString("tanggal");
//                int total = rs.getInt("total");
//                series.getData().add(new XYChart.Data<>(tanggal, total));
//            }
//
//            viewstok.getData().add(series);
//            viewstok.setTitle("Stok Keluar Harian");
//
//            int totalStok = series.getData().stream()
//                    .mapToInt(data -> data.getYValue().intValue())
//                    .sum();
//            tbstok.setText(String.valueOf(totalStok));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            messageBox.alertError("Gagal memuat data BarChart stok keluar harian.");
//        }



//////////////////// berdasar stok terbanyak
//        viewstok.getData().clear();
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Stok Keluar per Hari");
//
//        try {
//            LocalDate defaultStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
//            LocalDate defaultEnd = LocalDate.now();
//
//            tglselesai.setValue(LocalDate.now());
//
//            LocalDate startDate = (tglmulai.getValue() != null) ? tglmulai.getValue() : defaultStart;
//            LocalDate endDate = (tglselesai.getValue() != null) ? tglselesai.getValue().plusDays(1) : defaultEnd.plusDays(1);
//
//            java.sql.Date sqlStart = java.sql.Date.valueOf(startDate);
//            java.sql.Date sqlEnd = java.sql.Date.valueOf(endDate);
//
//            // Cari produk dengan stok keluar terbanyak
//            String queryProdukTerbanyak = "SELECT TOP 1 p.p_nama, SUM(sk.sk_jumlah_keluar) AS total " +
//                    "FROM stok_keluar sk " +
//                    "JOIN produk p ON sk.p_id = p.p_id " +
//                    "WHERE sk_tanggal_keluar >= ? AND sk_tanggal_keluar < ? " +
//                    "GROUP BY p.p_nama " +
//                    "ORDER BY total DESC";
//
//            PreparedStatement stmtProduk = connection.conn.prepareStatement(queryProdukTerbanyak);
//            stmtProduk.setDate(1, sqlStart);
//            stmtProduk.setDate(2, sqlEnd);
//
//            ResultSet rsProduk = stmtProduk.executeQuery();
//            if (rsProduk.next()) {
//                String namaProduk = rsProduk.getString("p_nama");
//                int jumlah = rsProduk.getInt("total");
//                tbprodukterbanyak.setText(namaProduk + " (" + jumlah + ")");
//            } else {
//                tbprodukterbanyak.setText("Tidak ada data");
//            }
//
//
//            viewstok.getData().add(series);
//            viewstok.setTitle("Stok Keluar Harian");
//
//            int totalStok = series.getData().stream()
//                    .mapToInt(data -> data.getYValue().intValue())
//                    .sum();
//            tbstok.setText(String.valueOf(totalStok));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            messageBox.alertError("Gagal memuat data BarChart stok keluar harian.");
//        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        tglmulai.setValue(LocalDate.of(2024, 1, 1)); // default
//        tglselesai.setValue(LocalDate.now());

        tbstok.setEditable(false); // Tidak bisa diketik manual

        loadBarChartStokKeluarByMonth();

        LocalDate firstDay = LocalDate.now().withDayOfYear(1); // 1 Januari tahun ini
        LocalDate lastDay = LocalDate.now().withMonth(12).withDayOfMonth(31); // 31 Desember tahun ini

        tglmulai.setValue(firstDay);
        tglselesai.setValue(lastDay);


        btnFilter.setOnAction(e -> {
            loadBarChartStokKeluarByMonth();
        });

        btnBersihkan.setOnAction(e -> {
//            tglmulai.setValue(LocalDate.of(2024, 1, 1));
            LocalDate defaultStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            tglselesai.setValue(LocalDate.now());
            loadBarChartStokKeluarByMonth();
        });
    }
}
