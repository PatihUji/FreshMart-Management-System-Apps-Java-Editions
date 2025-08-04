package SideBar;

import Dashboard.DashboardAdmin;
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
import master.JenisProduk.JenisProdukController;
import Helper.MessageBox;
import master.MetodePembayaran.MPcontroller;
import master.Promo.PromoController;
import master.karyawan.KaryawanController;
import master.karyawan.karyawan;
import master.produk.ProdukController;
import master.setting.SettingController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarAdminController implements Initializable {
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
    private Button btnKaryawan;

    @FXML
    private Button btnSetting;

    @FXML
    private Button btnProduk;

    @FXML
    private Button btnJenisProduk;

    @FXML
    private Button btnMetodePembayaran;

    @FXML
    private Button btnPromo;

    @FXML
    private Button btnKeluar;

    @FXML
    private ImageView iconBtnKaryawan;

    @FXML
    private ImageView iconBtnSetting;

    @FXML
    private ImageView iconBtnProduk;

    @FXML
    private ImageView iconBtnJenisProduk;

    @FXML
    private ImageView iconBtnMetodePembayaran;

    @FXML
    private ImageView iconBtnPromo;

    @FXML
    private ImageView iconBtnDashboard;

    @FXML
    private AnchorPane panelAdmin;

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
    protected void onClickBtnKeluar() throws IOException {
        switchToLogin();
    }

    @FXML
    protected void onClickBtnKaryawan() throws IOException {
        setActiveButton(btnKaryawan);
        lblTitle.setText("Karyawan");
        iconTitle.setImage(new Image(getClass().getResourceAsStream("/Pict/Icons/big_person1_green-icon.png")));
        loadDataKaryawan();
    }

    @FXML
    protected void onClickBtnDashboard() throws IOException {
        setActiveButton(btnDashboard);
        lblTitle.setText("Dashboard");
        iconTitle.setImage(new Image(getClass().getResourceAsStream("/Pict/Icons/big_home_green-icon.png")));
        loadDataDashboard();
    }

    @FXML
    protected void onClickBtnSetting() throws IOException {
        setActiveButton(btnSetting);
        lblTitle.setText("Setting");
        iconTitle.setImage(new  Image(getClass().getResourceAsStream("/Pict/Icons/big_setting_green-icon.png")));
        loadDataSetting();
    }

    @FXML
    protected void onClickBtnProduk() throws IOException {
        setActiveButton(btnProduk);
        lblTitle.setText("Produk");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_produk_green-icon.png")));
        loadDataProduk();
    }

    @FXML
    protected void onClickBtnJenisProduk() throws IOException {
        setActiveButton(btnJenisProduk);
        lblTitle.setText("Jenis Produk");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_jproduk_green-icon.png")));
        loadDataJenisProduk();
    }

    @FXML
    protected void onClickBtnMetodePembayaran() throws IOException {
        setActiveButton(btnMetodePembayaran);
        lblTitle.setText("Metode Pembayaran");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_credit_green-icon.png")));
        loadDataMetodePembayaran();
    }

    @FXML
    protected void onClickBtnPromo() throws IOException{
        setActiveButton(btnPromo);
        lblTitle.setText("Promo");
        iconTitle.setImage( new Image(getClass().getResourceAsStream("/Pict/Icons/big_dollar_green-icon.png")));
        loadDataPromo();
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
    protected void onMouseEnteredKaryawan() {
        if(currentActiveButton == btnKaryawan) {
            return;
        }
       btnKaryawan.setStyle("-fx-background-color: #FFFDF6");
       btnKaryawan.setTextFill(Color.web("#a0c878"));
       iconBtnKaryawan.setImage(new Image((getClass().getResource("/Pict/Icons/small_person1_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedKaryawan() {
        if(currentActiveButton == btnKaryawan) {
            return;
        }
        btnKaryawan.setStyle("-fx-background-color: #A0C878");
        btnKaryawan.setTextFill(Color.web("#FFFDF6"));
        iconBtnKaryawan.setImage(new Image((getClass().getResource("/Pict/Icons/small_person1-icon_white.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredSetting() {
        if(currentActiveButton == btnSetting) {
            return;
        }
        btnSetting.setStyle("-fx-background-color: #FFFDF6");
        btnSetting.setTextFill(Color.web("#a0c878"));
        iconBtnSetting.setImage(new Image((getClass().getResource("/Pict/Icons/small_setting_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedSetting() {
        if(currentActiveButton == btnSetting) {
            return;
        }
        btnSetting.setStyle("-fx-background-color: #A0C878");
        btnSetting.setTextFill(Color.web("#FFFDF6"));
        iconBtnSetting.setImage(new Image((getClass().getResource("/Pict/Icons/small_setting_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredProduk() {
        if(currentActiveButton == btnProduk) {
            return;
        }
        btnProduk.setStyle("-fx-background-color: #FFFDF6");
        btnProduk.setTextFill(Color.web("#a0c878"));
        iconBtnProduk.setImage(new Image((getClass().getResource("/Pict/Icons/small_produk_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedProduk() {
        if(currentActiveButton == btnProduk) {
            return;
        }
        btnProduk.setStyle("-fx-background-color: #A0C878");
        btnProduk.setTextFill(Color.web("#FFFDF6"));
        iconBtnProduk.setImage(new Image((getClass().getResource("/Pict/Icons/small_produk_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredJenisProduk() {
        if(currentActiveButton == btnJenisProduk) {
            return;
        }
        btnJenisProduk.setStyle("-fx-background-color: #FFFDF6");
        btnJenisProduk.setTextFill(Color.web("#a0c878"));
        iconBtnJenisProduk.setImage(new Image((getClass().getResource("/Pict/Icons/small_jproduk_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedJenisProduk() {
        if(currentActiveButton == btnJenisProduk) {
            return;
        }
        btnJenisProduk.setStyle("-fx-background-color: #A0C878");
        btnJenisProduk.setTextFill(Color.web("#FFFDF6"));
        iconBtnJenisProduk.setImage(new Image((getClass().getResource("/Pict/Icons/small_jproduk_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredMetodePembayaran() {
        if(currentActiveButton == btnMetodePembayaran) {
            return;
        }
        btnMetodePembayaran.setStyle("-fx-background-color: #FFFDF6");
        btnMetodePembayaran.setTextFill(Color.web("#a0c878"));
        iconBtnMetodePembayaran.setImage(new Image((getClass().getResource("/Pict/Icons/small_credit_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedMetodePembayaran() {
        if(currentActiveButton == btnMetodePembayaran) {
            return;
        }
        btnMetodePembayaran.setStyle("-fx-background-color: #A0C878");
        btnMetodePembayaran.setTextFill(Color.web("#FFFDF6"));
        iconBtnMetodePembayaran.setImage(new Image((getClass().getResource("/Pict/Icons/small_credit_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseEnteredPromo() {
        if(currentActiveButton == btnPromo) {
            return;
        }
        btnPromo.setStyle("-fx-background-color: #FFFDF6");
        btnPromo.setTextFill(Color.web("#a0c878"));
        iconBtnPromo.setImage(new Image((getClass().getResource("/Pict/Icons/small_dollar_green-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedPromo() {
        if(currentActiveButton == btnPromo) {
            return;
        }
        btnPromo.setStyle("-fx-background-color: #A0C878");
        btnPromo.setTextFill(Color.web("#FFFDF6"));
        iconBtnPromo.setImage(new Image((getClass().getResource("/Pict/Icons/small_dollar_white-icon.png").toExternalForm())));
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
        } else if (button == btnKaryawan) {
            iconBtnKaryawan.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnSetting) {
            iconBtnSetting.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnProduk) {
            iconBtnProduk.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnJenisProduk) {
            iconBtnJenisProduk.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnMetodePembayaran) {
            iconBtnMetodePembayaran.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } else if (button == btnPromo) {
            iconBtnPromo.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        }
    }

    private void setActiveButton(Button button) {
        // Reset semua tombol ke default
        resetButtonStyle(btnDashboard, "/Pict/Icons/small_home_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnKaryawan, "/Pict/Icons/small_person1-icon_white.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnSetting, "/Pict/Icons/small_setting_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnProduk, "/Pict/Icons/small_produk_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnJenisProduk, "/Pict/Icons/small_jproduk_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnMetodePembayaran, "/Pict/Icons/small_credit_white-icon.png", "#A0C878", "#FFFDF6");
        resetButtonStyle(btnPromo, "/Pict/Icons/small_dollar_white-icon.png", "#A0C878", "#FFFDF6");

        // Ubah tombol aktif
        button.setStyle("-fx-background-color: #FFFDF6");
        button.setTextFill(Color.web("#a0c878"));

        // Ganti ikon tombol aktif
        if (button == btnDashboard) {
            iconBtnDashboard.setImage(new Image(getClass().getResource("/Pict/Icons/small_home_green-icon.png").toExternalForm()));
        }
        else if (button == btnKaryawan) {
            iconBtnKaryawan.setImage(new Image(getClass().getResource("/Pict/Icons/small_person1_green-icon.png").toExternalForm()));
        }
        else if (button == btnSetting) {
            iconBtnSetting.setImage(new Image(getClass().getResource("/Pict/Icons/small_setting_green-icon.png").toExternalForm()));
        }
        else if (button == btnProduk) {
            iconBtnProduk.setImage(new Image(getClass().getResource("/Pict/Icons/small_produk_green-icon.png").toExternalForm()));
        }
        else if (button == btnJenisProduk) {
            iconBtnJenisProduk.setImage(new Image(getClass().getResource("/Pict/Icons/small_jproduk_green-icon.png").toExternalForm()));
        }
        else if (button == btnMetodePembayaran) {
            iconBtnMetodePembayaran.setImage(new Image(getClass().getResource("/Pict/Icons/small_credit_green-icon.png").toExternalForm()));
        }
        else if (button == btnPromo) {
            iconBtnPromo.setImage(new Image(getClass().getResource("/Pict/Icons/small_dollar_green-icon.png").toExternalForm()));
        }
        currentActiveButton = button;
    }

    public void loadDataDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardAdmin.fxml"));
        Parent content = loader.load();
        DashboardAdmin controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelAdmin.getChildren().clear();
        panelAdmin.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataKaryawan() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/karyawan/karyawan.fxml"));
            Parent content = loader.load();
            KaryawanController controller = loader.getController();
            controller.setUserAccess(lblAccess.getText());

            panelAdmin.getChildren().clear();
            panelAdmin.getChildren().add(content);

            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataJenisProduk() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/JenisProduk/JenisProduk.fxml"));
        Parent content = loader.load();
        JenisProdukController controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelAdmin.getChildren().clear();
        panelAdmin.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataPromo() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/Promo/Promo.fxml"));
        Parent content = loader.load();
        PromoController controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelAdmin.getChildren().clear();
        panelAdmin.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataSetting() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/setting/setting.fxml"));
        Parent content = loader.load();
        SettingController controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelAdmin.getChildren().clear();
        panelAdmin.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataMetodePembayaran() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/MetodePembayaran/MetodePembayaran.fxml"));
        Parent content = loader.load();
        MPcontroller controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelAdmin.getChildren().clear();
        panelAdmin.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public void loadDataProduk() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/produk/produk.fxml"));
        Parent content = loader.load();
        ProdukController controller = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelAdmin.getChildren().clear();
        panelAdmin.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

}
