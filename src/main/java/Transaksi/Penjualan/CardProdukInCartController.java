package Transaksi.Penjualan;

import Helper.MessageBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class CardProdukInCartController {

    @FXML
    private Button btnMinQuantity;

    @FXML
    private Button btnPlusQuantity;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private ImageView imvGambarProduk;

    @FXML
    private Label lblHarga;

    @FXML
    private Label lblJenisProduk;

    @FXML
    private Label lblNamaProduk;

    @FXML
    private TextField txtKuantitas;

    @FXML
    private HBox hbCartItem;

    @FXML
    void onClickBtnMinQuantity(ActionEvent event) {
        if(kuantitas > 1){
            kuantitas--;
            txtKuantitas.setText(String.valueOf(kuantitas));
            lblHarga.setText(RupiahFormat(harga*kuantitas));
            parentController.setTotalHarga();
        }
    }

    @FXML
    void onClickBtnPlusQuantity() {
        if(kuantitas < stock){
            kuantitas++;
            txtKuantitas.setText(String.valueOf(kuantitas));
            lblHarga.setText(RupiahFormat(harga*kuantitas));
            parentController.setTotalHarga();
        }
    }

    @FXML
    void onclickBtnDeleteItem(ActionEvent event) {
        Parent parent = hbCartItem.getParent();

        if (parent instanceof VBox parentVBox) {
            parentVBox.getChildren().remove(hbCartItem);

        } else if (parent instanceof GridPane parentGrid) {
            parentGrid.getChildren().remove(hbCartItem);
        }

        parentController.setTotalHarga();
    }

    @FXML
    void onKeyPressedTxtKuantitas() {
        txtKuantitas.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();

            // Batasi input hanya angka
            if (!character.matches("[0-9]")) {
                event.consume();
                return;
            }

            // Simulasikan input setelah karakter baru dimasukkan
            String currentText = txtKuantitas.getText();
            int caretPosition = txtKuantitas.getCaretPosition();
            StringBuilder newText = new StringBuilder(currentText);
            newText.insert(caretPosition, character);

            String input = newText.toString();

            // Validasi isi input
            try {
                int jumlah = Integer.parseInt(input);
                if (jumlah < 1) {
                    messageBox.alertError("Jumlah minimal item adalah 1!");
                    event.consume();
                    kuantitas = 1;
                    txtKuantitas.setText(String.valueOf(kuantitas));
                    lblHarga.setText(RupiahFormat(harga*kuantitas));
                } else if (jumlah > stock) {
                    messageBox.alertError("Jumlah item tidak boleh lebih dari stok yang ada!");
                    event.consume();
                    kuantitas = 1;
                    txtKuantitas.setText(String.valueOf(kuantitas));
                    lblHarga.setText(RupiahFormat(harga*kuantitas));
                }
            } catch (NumberFormatException e) {
                messageBox.alertError("Input tidak valid!");
                event.consume();
            }
        });
    }

    MessageBox messageBox = new MessageBox();
    private Integer idProduk, kuantitas = 1, stock;
    private Double harga;

    private PenjualanProdukController parentController;

    public void setParentController(PenjualanProdukController controller) {
        this.parentController = controller;
    }

    public Integer getIdProduk(){
        return idProduk;
    }

    public Integer getKuantitas(){
        return kuantitas;
    }

    public Double getSubTotalHarga(){
        return harga * kuantitas;
    }

    public void setData(Integer idProduk, String pictUrl, String namaProduk, Double harga, String jenisProduk, Integer stock) {
        File file = new File("src/main/resources/Pict/produk/" + pictUrl);
        if (file.exists()) {
            imvGambarProduk.setImage(new Image(file.toURI().toString()));
        } else {
            imvGambarProduk.setImage(new Image(new File("src/main/resources/Pict/produk/image_not_found.jpg").toURI().toString()));
        }

        this.idProduk = idProduk;
        this.stock = stock;
        this.harga = harga;

        lblNamaProduk.setText(namaProduk);

        lblHarga.setText(RupiahFormat(harga));
        lblJenisProduk.setText(jenisProduk);
        txtKuantitas.setText(kuantitas.toString());
    }

    public String RupiahFormat(Double uang){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(uang);
    }
}
