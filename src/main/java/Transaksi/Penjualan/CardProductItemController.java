package Transaksi.Penjualan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class CardProductItemController implements Initializable {
    @FXML
    private ImageView imvGambarProduk;

    @FXML
    private Label lblHargaSatuan;

    @FXML
    private Label lblJenisProduk;

    @FXML
    private Label lblNamaProduk;

    @FXML
    private Label lblStok;

    @FXML
    private VBox vbCardItem;

    @FXML
    private BorderPane bpContent;

    @FXML
    protected void onClickVbCardItem(){
        loadDataItemsToCart();
    }

    String pictUrl = null;
    Integer idProduk;

    private PenjualanProdukController parentController;

    public void setParentController(PenjualanProdukController controller) {
        this.parentController = controller;
    }

    public void setData(Integer idProduk, String pictUrl, String namaProduk, Double harga, String satuan, String jenisProduk, Integer stok) {
        File file = new File("src/main/resources/Pict/produk/" + pictUrl);
        if (file.exists()) {
            imvGambarProduk.setImage(new Image(file.toURI().toString()));
            this.pictUrl = pictUrl;
        } else {
            imvGambarProduk.setImage(new Image(new File("src/main/resources/Pict/produk/image_not_found.jpg").toURI().toString()));
            this.pictUrl = "image_not_found.jpg";
        }

        this.idProduk = idProduk;
        lblNamaProduk.setText(namaProduk);

        lblHargaSatuan.setText(RupiahFormat(harga) + " / " + satuan);
        lblJenisProduk.setText(jenisProduk);
        lblStok.setText(stok.toString() + " stok tersisa");
    }

    private void loadDataItemsToCart (){
        try {
            GridPane keranjangGrid = parentController.getKeranjangGrid();

            // 🔍 Cek apakah idProduk sudah ada
            for (Node node : keranjangGrid.getChildren()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/Penjualan/CardProductInCart.fxml"));

                if (node.getUserData() instanceof CardProdukInCartController existingController) {
                    if (existingController.getIdProduk() == idProduk) {
                        existingController.onClickBtnPlusQuantity();
                        return;
                    }
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/Penjualan/CardProductInCart.fxml"));
            Parent item = loader.load();

            CardProdukInCartController controller = loader.getController();

            String pureHarga = lblHargaSatuan.getText().replace("Rp", "").replace(".", "").replaceAll(",.*", "").trim();

            controller.setData(idProduk, pictUrl, lblNamaProduk.getText(), Double.parseDouble(pureHarga), lblJenisProduk.getText(), Integer.parseInt(lblStok.getText().replaceAll(" .*", "")));

            controller.setParentController(parentController);

            item.setUserData(controller);

            int row = keranjangGrid.getRowCount();
            keranjangGrid.add(item, 0, row);

            parentController.setTotalHarga();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String RupiahFormat(Double uang){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(uang);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imvGambarProduk.setFitWidth(236);
        imvGambarProduk.setFitHeight(189);
        imvGambarProduk.setPreserveRatio(false); // opsional

        SVGPath clip = new SVGPath();
        clip.setContent("M0,15 Q0,0 15,0 H221 Q236,0 236,15 V189 H0 Z");
        imvGambarProduk.setClip(clip);
    }
}
