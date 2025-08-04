package main.thefreshchoice;

import SideBar.*;
import database.DBConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ImageView btnExit;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ImageView iconEye;

    DBConnect connection = new DBConnect();

    @FXML
    protected void onClickExit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void onClickLogin() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        StringBuilder jabatan = new StringBuilder();

        if(username.equals("Admin") && password.equals("123")) {
            SwitchToAdminMenu(txtUsername.getText(), "Admin", username);
        }

        else if(username.equals("Kasir") && password.equals("123")) {
            SwitchToKasirMenu(txtUsername.getText(), "Kasir", username);
        }
        else if(username.equals("Quality Control") && password.equals("123")) {
            SwitchToQualityControlMenu(txtUsername.getText(), "Quality Control", username);
        }
        else if(username.equals("Customer Service") && password.equals("123")) {
            SwitchToCustomerServiceMenu(txtUsername.getText(), "Customer Service", username);
        }
        else if(username.equals("Owner") && password.equals("123")) {
            SwitchToOwnerMenu(txtUsername.getText(), "Owner", username);
        }

        else if(connection.loginKaryawan(username, password, jabatan) == 0){
            if(jabatan.toString().equals("Admin")){
                SwitchToAdminMenu(connection.getUserLoginName(txtUsername.getText()), jabatan.toString(), username);
            }
            else if(jabatan.toString().equals("Kasir")){
                SwitchToKasirMenu(connection.getUserLoginName(txtUsername.getText()), jabatan.toString(), username);
            }
            else if(jabatan.toString().equals("Quality Control")){
                SwitchToQualityControlMenu(connection.getUserLoginName(txtUsername.getText()), jabatan.toString(), username);
            }
            else if(jabatan.toString().equals("Customer Service")){
                SwitchToCustomerServiceMenu(connection.getUserLoginName(txtUsername.getText()), jabatan.toString(), username);
            }
            else if(jabatan.toString().equals("Owner")){
                SwitchToOwnerMenu(connection.getUserLoginName(txtUsername.getText()), jabatan.toString(), username);
            }
        }
    }

    private void SwitchToAdminMenu(String username, String jabatan, String usernameAccess) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SideBar/sideBarAdmin.fxml"));
        Parent root = fxmlLoader.load();
        SideBarAdminController sideBar = fxmlLoader.getController();
        sideBar.setLblAccess(username);
        sideBar.setLblJabatan(jabatan);
        sideBar.setUsername(usernameAccess);

        sideBar.loadDataDashboard();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void SwitchToKasirMenu(String username, String jabatan, String usernameAccess) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SideBar/sideBarKasir.fxml"));
        Parent root = fxmlLoader.load();
        SideBarKasirController sideBar = fxmlLoader.getController();
        sideBar.setLblAccess(username);
        sideBar.setLblJabatan(jabatan);
        sideBar.setUsername(usernameAccess);

        sideBar.loadDataDashboard();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void SwitchToQualityControlMenu(String username, String jabatan, String usernameAccess) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SideBar/sideBarQualityControl.fxml"));
        Parent root = fxmlLoader.load();
        SideBarQualityControlController sideBar = fxmlLoader.getController();
        sideBar.setLblAccess(username);
        sideBar.setLblJabatan(jabatan);
        sideBar.setUsername(usernameAccess);

        sideBar.loadDataDashboard();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void SwitchToCustomerServiceMenu(String username, String jabatan, String usernameAccess) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SideBar/sideBarCustomerService.fxml"));
        Parent root = fxmlLoader.load();
        SideBarCustomerServiceController sideBar = fxmlLoader.getController();
        sideBar.setLblAccess(username);
        sideBar.setLblJabatan(jabatan);
        sideBar.setUsername(usernameAccess);

        sideBar.loadDataDashboard();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void SwitchToOwnerMenu(String username, String jabatan, String usernameAccess) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SideBar/SideBarOwner.fxml"));
        Parent root = fxmlLoader.load();
        SideBarOwnerController sideBar = fxmlLoader.getController();
        sideBar.setLblAccess(username);
        sideBar.setLblJabatan(jabatan);
        sideBar.setUsername(usernameAccess);

        sideBar.loadDataDashboard();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private boolean isPasswordVisible = false;

    @FXML
    protected void onTogglePassword() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            iconEye.setImage(new Image(getClass().getResource("/Pict/Icons/eye-open.png").toExternalForm()));
        } else {
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            iconEye.setImage(new Image(getClass().getResource("/Pict/Icons/eye-closed.png").toExternalForm()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());

        // Default tersembunyi
        txtPasswordVisible.setVisible(false);
        txtPasswordVisible.setManaged(false);

        // Set default icon mata tertutup
        iconEye.setImage(new Image(getClass().getResource("/Pict/Icons/eye-closed.png").toExternalForm()));
    }
}