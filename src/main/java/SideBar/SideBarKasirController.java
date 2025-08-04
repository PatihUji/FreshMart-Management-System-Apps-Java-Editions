package SideBar;

import Dashboard.DashboardKasir;
import Helper.MessageBox;
import Transaksi.Pengiriman.PengirimanController;
import Transaksi.Penjualan.PenjualanProdukController;
import database.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import master.karyawan.KaryawanController;
import master.karyawan.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarKasirController implements Initializable {
    @FXML
    private Label lblAccess;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblJabatan;

    @FXML
    private ImageView iconTitle;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnPenjualan;

    @FXML
    private Button btnPengiriman;

    @FXML
    private Button btnKeluar;

    @FXML
    private ImageView iconBtnPenjualan;

    @FXML
    private ImageView iconBtnPengiriman;

    @FXML
    private ImageView iconBtnDashboard;

    @FXML
    private AnchorPane panelKasir;

    @FXML
    private AnchorPane apDataDiri;

    @FXML
    private Label lblNama;

    @FXML
    private Label lblTglLahir;

    @FXML
    private Label lblJenisKelamin;

    @FXML
    private Label lblDDJabatan;

    @FXML
    private Label lblAlamat;

    @FXML
    private Label lblUsername;

    @FXML
    private PasswordField txtPassword;

    private Button currentActiveButton;

    @FXML
    protected void onClickBtnDashboard() throws IOException {
        setActiveButton(btnDashboard);
        lblTitle.setText("Dashboard");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_home_green-icon.png")));
        loadDataDashboard();
    }

    @FXML
    protected void onClickBtnPenjualan() throws IOException {
        setActiveButton(btnPenjualan);
        lblTitle.setText("Penjualan");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_cart_green-icon.png")));
        loadDataPenjualan();
    }

    @FXML
    protected void onClickBtnPengiriman() throws IOException{
        setActiveButton(btnPengiriman);
        lblTitle.setText("Pengiriman");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_delivery_green-icon.png")));
        loadDataPengiriman();
    }

    @FXML
    protected void onMouseEnteredDashboard() {
        if(currentActiveButton == btnDashboard) {
            return;
        }
        btnDashboard.setStyle("-fx-background-color: #FFFDF6");
        btnDashboard.setTextFill(Color.web("#a0c878"));
        iconBtnDashboard.setImage(new Image((getClass().getResource("/Pict/Icons/small_home_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedDashboard() {
        if(currentActiveButton == btnDashboard) {
            return;
        }
        btnDashboard.setStyle("-fx-background-color: #A0C878");
        btnDashboard.setTextFill(Color.web("#FFFDF6"));
        iconBtnDashboard.setImage(new Image((getClass().getResource("/Pict/Icons/small_home_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredPenjualan() {
        if(currentActiveButton == btnPenjualan) {
            return;
        }
        btnPenjualan.setStyle("-fx-background-color: #FFFDF6");
        btnPenjualan.setTextFill(Color.web("#a0c878"));
        iconBtnPenjualan.setImage(new Image((getClass().getResource("/Pict/Icons/small_cart_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedPenjualan() {
        if(currentActiveButton == btnPenjualan) {
            return;
        }
        btnPenjualan.setStyle("-fx-background-color: #A0C878");
        btnPenjualan.setTextFill(Color.web("#FFFDF6"));
        iconBtnPenjualan.setImage(new Image((getClass().getResource("/Pict/Icons/small_cart_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredPengiriman() {
        if(currentActiveButton == btnPengiriman) {
            return;
        }
        btnPengiriman.setStyle("-fx-background-color: #FFFDF6");
        btnPengiriman.setTextFill(Color.web("#a0c878"));
        iconBtnPengiriman.setImage(new Image((getClass().getResource("/Pict/Icons/small_delivery_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedPengiriman() {
        if(currentActiveButton == btnPengiriman) {
            return;
        }
        btnPengiriman.setStyle("-fx-background-color: #A0C878");
        btnPengiriman.setTextFill(Color.web("#FFFDF6"));
        iconBtnPengiriman.setImage(new Image((getClass().getResource("/Pict/Icons/small_delivery_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredKeluar() {
        if(currentActiveButton == btnKeluar) {
            return;
        }
        btnKeluar.setStyle("-fx-background-color: #FF0004");
    }

    @FXML
    protected void onMouseExitedKeluar() {
        if(currentActiveButton == btnKeluar) {
            return;
        }
        btnKeluar.setStyle("-fx-background-color: #A0C878");
    }

    @FXML
    protected void onClickLblAccess(){
        DBConnect connection = new DBConnect();
        karyawan getData = connection.getProfileByUsername(username);
        lblNama.setText(getData.getNama());
        lblTglLahir.setText(getData.getTanggalLahir().toString());
        lblJenisKelamin.setText(getData.getGender());
        lblDDJabatan.setText(getData.getSNama());
        lblAlamat.setText(getData.getAlamat());
        lblUsername.setText(getData.getUsername());
        txtPassword.setText(getData.getPassword());
        apDataDiri.setVisible(true);
    }

    @FXML
    protected void onClickApDataDiri(){
        apDataDiri.setVisible(false);
    }

    @FXML
    protected void onClickBtnGantiPassword(){
        if(txtPassword.getText().isEmpty()){
            MessageBox messageBox = new MessageBox();
            messageBox.alertError("Password tidak boleh kosong!");
        }
        else {
            DBConnect connection = new DBConnect();
            connection.updatePasswordKaryawanByUsername(username, txtPassword.getText());
        }
    }

    @FXML
    protected void onKeyPressTxtPassword(){
        txtPassword.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (txtPassword.getText().length() > 20) {
                event.consume();
            }
        });
    }

    @FXML
    protected void onClickBtnKeluar() throws IOException {
        switchToLogin();
    }

    String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLblAccess(String username) {
        lblAccess.setText(username);
    }

    public void setLblJabatan(String jabatan) {
        lblJabatan.setText(jabatan);
    }

    public void switchToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/thefreshchoice/login.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) btnKeluar.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentActiveButton = btnDashboard;
    }

    private void resetButtonStyle(Button button, String iconPath, String bgColor, String textColor) {
        button.setStyle("-fx-background-color: " + bgColor);
        button.setTextFill(Color.web(textColor));

        if (button == btnDashboard) {
            iconBtnDashboard.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnPenjualan) {
            iconBtnPenjualan.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnPengiriman) {
            iconBtnPengiriman.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        }
    }

    private void setActiveButton(Button button) {
        // Reset semua tombol ke default
        resetButtonStyle(btnDashboard, "/Pict/Icons/small_home_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnPenjualan, "/Pict/Icons/small_cart_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnPengiriman, "/Pict/Icons/small_delivery_white-icon.png", "#A0C878", "#FFFDF6");

        // Ubah tombol aktif
        button.setStyle("-fx-background-color: #FFFDF6");
        button.setTextFill(Color.web("#a0c878"));

        // Ganti ikon tombol aktif
        if (button == btnDashboard) {
            iconBtnDashboard.setImage(new Image(getClass().getResource("/Pict/Icons/small_home_green-icon.png").toExternalForm()));
        }
        else if (button == btnPenjualan) {
            iconBtnPenjualan.setImage(new Image(getClass().getResource("/Pict/Icons/small_cart_green-icon.png").toExternalForm()));
        }
        else if (button == btnPengiriman) {
            iconBtnPengiriman.setImage(new Image(getClass().getResource("/Pict/Icons/small_delivery_green-icon.png").toExternalForm()));
        }
        currentActiveButton = button;
    }

    public void loadDataPenjualan() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/Penjualan/Penjualan.fxml"));
        Parent content = loader.load();
        PenjualanProdukController controller = loader.getController();
        controller.setUserAccess(lblAccess.getText(), username);

        panelKasir.getChildren().clear();
        panelKasir.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataPengiriman() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/Pengiriman/Pengiriman.fxml"));
        Parent content = loader.load();
        PengirimanController controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelKasir.getChildren().clear();
        panelKasir.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardKasir.fxml"));
        Parent content = loader.load();
        DashboardKasir controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelKasir.getChildren().clear();
        panelKasir.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }
}
