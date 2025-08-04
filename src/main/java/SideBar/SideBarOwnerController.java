package SideBar;

import Dashboard.DashboardKasir;
import Dashboard.DashboardOwner;
import Helper.MessageBox;
import database.DBConnect;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import master.karyawan.karyawan;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class SideBarOwnerController {

    @FXML
    private Button btnKeluar;

    @FXML
    private Button btnLpReturBarang;

    @FXML
    private Button btnLpStokKeluar;

    @FXML
    private Button btnLpPenjualan;

    @FXML
    private Button btnLpPengiriman;

    @FXML
    private Label lblAccess;

    @FXML
    private Label lblJabatan;

    @FXML
    private Label lblTitle;

    @FXML
    private AnchorPane panelOwner;

    @FXML
    private ImageView imvKeluar;

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

    @FXML
    protected void onMouseEnteredLpPenjualan(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpPenjualan);
        transition.setToY(-50);
        transition.play();
    }

    @FXML
    protected void onMouseExitedLpPenjualan(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpPenjualan);
        transition.setToY(9);
        transition.play();
    }

    @FXML
    protected void onMouseEnteredLpStokKeluar(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpStokKeluar);
        transition.setToY(-50);
        transition.play();
    }

    @FXML
    protected void onMouseExitedLpStokKeluar(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpStokKeluar);
        transition.setToY(9);
        transition.play();
    }

    @FXML
    protected void onMouseEnteredLpReturBarang(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpReturBarang);
        transition.setToY(-50);
        transition.play();
    }

    @FXML
    protected void onMouseExitedLpReturBarang(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpReturBarang);
        transition.setToY(9);
        transition.play();
    }

    @FXML
    protected void onMouseEnteredLpPengiriman(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpPengiriman);
        transition.setToY(-50);
        transition.play();
    }

    @FXML
    protected void onMouseExitedLpPengiriman(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), btnLpPengiriman);
        transition.setToY(9);
        transition.play();
    }

    @FXML
    protected void onMouseEnteredKeluar(){
        imvKeluar.setImage(new Image((getClass().getResource("/Pict/Icons/small_logout_red-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseExitedKeluar(){
        imvKeluar.setImage(new Image((getClass().getResource("/Pict/Icons/small_logout_white-icon.png").toExternalForm())));
    }

    @FXML
    protected void onMouseClickKeluar() throws IOException {
        switchToLogin();
    }

    @FXML
    protected void onMouseClickLpPenjualan() {
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("TanggalMulai", java.sql.Date.valueOf(dashboardOwnerController.getTglMulai()));
            param.put("TanggalAkhir", java.sql.Date.valueOf(dashboardOwnerController.getTglSelesai()));

            JasperPrint jp = JasperFillManager.fillReport(
                    getClass().getResourceAsStream("/Laporan/LaporanTransaksiPenjualan.jasper"),
                    param,
                    connection.conn
            );

            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tambahkan ini untuk fullscreen
            viewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMouseClickLpStokKeluar() {
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("TanggalMulai", java.sql.Date.valueOf(dashboardOwnerController.getTglMulai()));
            param.put("TanggalAkhir", java.sql.Date.valueOf(dashboardOwnerController.getTglSelesai()));

            JasperPrint jp = JasperFillManager.fillReport(
                    getClass().getResourceAsStream("/Laporan/LaporanTransaksiStok.jasper"),
                    param,
                    connection.conn
            );

            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tambahkan ini untuk fullscreen
            viewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMouseClickLpReturBarang() {
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("TanggalMulai", java.sql.Date.valueOf(dashboardOwnerController.getTglMulai()));
            param.put("TanggalAkhir", java.sql.Date.valueOf(dashboardOwnerController.getTglSelesai()));

            JasperPrint jp = JasperFillManager.fillReport(
                    getClass().getResourceAsStream("/Laporan/LaporanTransaksiRetur.jasper"),
                    param,
                    connection.conn
            );

            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tambahkan ini untuk fullscreen
            viewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DashboardOwner dashboardOwnerController;
    DBConnect connection = new DBConnect();

    @FXML
    protected void onMouseClickLpPengiriman() {
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("TanggalMulai", java.sql.Date.valueOf(dashboardOwnerController.getTglMulai()));
            param.put("TanggalAkhir", java.sql.Date.valueOf(dashboardOwnerController.getTglSelesai()));

            JasperPrint jp = JasperFillManager.fillReport(
                    getClass().getResourceAsStream("/Laporan/LaporanTransaksiPengiriman.jasper"),
                    param,
                    connection.conn
            );

            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tambahkan ini untuk fullscreen
            viewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void loadDataDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardOwner.fxml"));
        Parent content = loader.load();
        DashboardOwner controller = loader.getController();
        dashboardOwnerController = loader.getController();
        controller.setUserAccess(lblAccess.getText());

        panelOwner.getChildren().clear();
        panelOwner.getChildren().add(content);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

}
