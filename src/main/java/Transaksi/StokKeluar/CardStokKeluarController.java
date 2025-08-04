package Transaksi.StokKeluar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import master.produk.Produk;

import java.net.URL;
import java.util.ResourceBundle;

public class CardStokKeluarController implements Initializable {

    @FXML
    private Label lblNamaProduk;

    @FXML
    private Label lblJumlahStok;

    private StokKeluarController parentController;

    public void setParentController(StokKeluarController controller) {
        this.parentController = controller;
    }

    public void setData(Produk produk) {
        lblNamaProduk.setText(produk.getNama());
        lblJumlahStok.setText(produk.getStok().toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Optional: kamu bisa tambah style/animasi atau default text di sini
    }
}