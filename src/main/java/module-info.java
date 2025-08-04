module main.thefreshchoice {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.smartcardio;
    requires java.desktop;
    requires com.microsoft.sqlserver.jdbc;
    requires net.sf.jasperreports.core;

    opens main.thefreshchoice to javafx.fxml;
    opens master.setting to javafx.fxml;
    opens master.karyawan to javafx.fxml;
    opens master.JenisProduk to javafx.fxml;
    opens master.Promo to javafx.fxml;
    opens master.MetodePembayaran to javafx.fxml;
    opens master.produk to javafx.fxml;
    opens Transaksi.Penjualan to javafx.fxml;
    opens Transaksi.Pengiriman to javafx.fxml;
    opens Transaksi.StokKeluar to javafx.fxml;
    opens Transaksi.ReturBarang to javafx.fxml;
    opens SideBar to javafx.fxml;
    opens Helper to javafx.fxml;
    opens Dashboard to javafx.fxml;

    exports main.thefreshchoice;
    exports master.setting;
    exports master.karyawan;
    exports master.JenisProduk;
    exports master.Promo;
    exports master.MetodePembayaran;
    exports master.produk;
    exports Transaksi.Penjualan;
    exports Transaksi.StokKeluar;
    exports Transaksi.ReturBarang;
    exports Transaksi.Pengiriman;
    exports Dashboard;
}