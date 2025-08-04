package master.karyawan;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import Helper.MessageBox;
import master.setting.Setting;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class KaryawanController implements Initializable {
    @FXML
    private TextField txtNama;

    @FXML
    private DatePicker dpTanggalLahir;

    @FXML
    private ComboBox<String> cbJenisKelamin;

    @FXML
    private ComboBox<Setting> cbJabatan;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextArea txtAlamat;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnBatal;

    @FXML
    private TableView<karyawan> tbKaryawan;

    @FXML
    private TableColumn colNama;

    @FXML
    private TableColumn colJabatan;

    @FXML
    private TableColumn colTglLahir;

    @FXML
    private TableColumn colJenisKelamin;

    @FXML
    private TableColumn colAlamat;

    @FXML
    private TableColumn<karyawan, Void> colAksi;

    @FXML
    private Pagination pgnKaryawan;

    @FXML
    private TextField txtCari;

    @FXML
    private Label lblReqUsername;

    @FXML
    private Label lblReqPassword;

    @FXML
    private Button btnSortFilter;

    @FXML
    private AnchorPane apSortFilter;

    @FXML
    private ComboBox<String> cbSort;

    @FXML
    private ComboBox<String> cbFilter;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> cbStatus;

    DBConnect connection = new DBConnect();

    private int kry_id = 0;

    private String username = null;

    private String password = null;

    MessageBox messageBox = new MessageBox();

    private boolean isSortFilter = false;

    @FXML
    protected void onClickBtnSimpan(){
        Setting setting = cbJabatan.getValue();

        if (kry_id != 0 && connection.isKaryawanExist(kry_id)) {
            if(!isJabatanAccess){
                username = null;
                password = null;
            }
            if (isJabatanAccess && (txtNama.getText().isEmpty() || txtUsername.getText().isEmpty() || txtAlamat.getText().isEmpty() || cbJenisKelamin.getSelectionModel().getSelectedItem() == null || cbJabatan.getSelectionModel().getSelectedItem() == null || dpTanggalLahir.getValue() == null)) {
                messageBox.alertError("Data tidak boleh kosong!");
            }
            else if(!isJabatanAccess && (txtNama.getText().isEmpty() || txtAlamat.getText().isEmpty() || cbJenisKelamin.getSelectionModel().getSelectedItem() == null || cbJabatan.getSelectionModel().getSelectedItem() == null || dpTanggalLahir.getValue() == null)){
                messageBox.alertError("Data tidak boleh kosong!");
            }
            else {
                if(isJabatanAccess) {
                    username = txtUsername.getText();
                }
                connection.updateKaryawan(setting.getS_id(), kry_id, txtNama.getText(), java.sql.Date.valueOf(dpTanggalLahir.getValue()), txtAlamat.getText(), cbJenisKelamin.getValue(), username, userAccess);
                clear();
                loadDataKaryawan();
            }
        }
        else {
            if (isJabatanAccess && (txtNama.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtAlamat.getText().isEmpty() || cbJenisKelamin.getSelectionModel().getSelectedItem() == null || cbJabatan.getSelectionModel().getSelectedItem() == null || dpTanggalLahir.getValue() == null)) {
                messageBox.alertError("Data tidak boleh kosong!");
            }
            else if (!isJabatanAccess && (txtNama.getText().isEmpty() || txtAlamat.getText().isEmpty() || cbJenisKelamin.getSelectionModel().getSelectedItem() == null || cbJabatan.getSelectionModel().getSelectedItem() == null || dpTanggalLahir.getValue() == null)) {
                messageBox.alertError("Data tidak boleh kosong!");
            }
            else {
                if(isJabatanAccess){
                    username = txtUsername.getText();
                    password = txtPassword.getText();
                }
                else {
                    username = null;
                    password = null;
                }
                connection.insertKaryawan(setting.getS_id(), txtNama.getText(), java.sql.Date.valueOf(dpTanggalLahir.getValue()), txtAlamat.getText(), cbJenisKelamin.getValue(), username, password, userAccess);
                clear();
                loadDataKaryawan();
            }
        }
    }

    private boolean isJabatanAccess = false;

    @FXML
    protected void onClickCbJabatan(){
        var selectedItem = cbJabatan.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            lblReqUsername.setVisible(false);
            lblReqPassword.setVisible(false);
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtUsername.setPromptText(null);
            txtPassword.setPromptText(null);
            txtUsername.setText(null);
            txtPassword.setText(null);
            isJabatanAccess = false;
            return;
        }

        String selected = selectedItem.toString();

        boolean jabatanMatch = selected.equals("Admin") ||
                selected.equals("Quality Control") ||
                selected.equals("Customer Service") ||
                selected.equals("Owner") ||
                selected.equals("Kasir");

        if (kry_id == 0 && jabatanMatch) {
            lblReqUsername.setVisible(true);
            lblReqPassword.setVisible(true);
            txtUsername.setEditable(true);
            txtPassword.setEditable(true);
            txtUsername.setPromptText("Username");
            txtPassword.setPromptText("Password");
            isJabatanAccess = true;
        } else if (kry_id != 0 && jabatanMatch) {
            lblReqUsername.setVisible(true);
            lblReqPassword.setVisible(false);
            txtUsername.setEditable(true);
            txtPassword.setEditable(false);
            txtUsername.setPromptText("Username");
            txtPassword.setPromptText(null);
            txtUsername.setText(username);
            isJabatanAccess = true;
        } else {
            lblReqUsername.setVisible(false);
            lblReqPassword.setVisible(false);
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtUsername.setPromptText(null);
            txtPassword.setPromptText(null);
            txtUsername.clear();
            txtPassword.clear();
            isJabatanAccess = false;
        }
    }

    @FXML
    protected void onClickBtnBatal(){
        clear();
    }

    /*--- Mengatur tombol yang bisa ditekan ---*/
    @FXML
    protected void onKeyPressTxtNama(){
        txtNama.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
                event.consume();
            }

            if (txtNama.getText().length() > 50) {
                event.consume();
            }
        });
    }

    @FXML
    protected void onKeyPressTxtAlamat(){
        txtAlamat.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (txtAlamat.getText().length() > 100) {
                event.consume();
            }
        });
    }

    @FXML
    protected void onKeyPressTxtUsername(){
        txtUsername.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[a-zA-Z]")) {
                event.consume();
            }

            if (txtUsername.getText() != null && txtUsername.getText().length() > 20) {
                event.consume();
            }
        });
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
    protected void onActionDpTanggalLahir(){
        LocalDate newValue = dpTanggalLahir.getValue();
        if (newValue != null) {
            LocalDate today = LocalDate.now();
            Period age = Period.between(newValue, today);

            if (age.getYears() < 19) {
                messageBox.alertWarning("Umur tidak mencukupi!");
                dpTanggalLahir.setValue(null);
            }
            else if(age.getYears() > 50){
                messageBox.alertWarning("Umur telah melebihi batas maksimal!");
                dpTanggalLahir.setValue(null);
            }
        }

        /*.isAfter, .isBefore, .isEqual*/
    }

    /*--- Muncul panel sort & filter ---*/
    @FXML
    protected void onClickBtnSortFilter(){
        if(isSortFilter){
            apSortFilter.setVisible(false);
            isSortFilter = false;
        }
        else {
            apSortFilter.setVisible(true);
            isSortFilter = true;
        }
    }

    int OrderStatus = 1;

    /*--- Mengubah data berdasarkan sort & filter ---*/
    private void loadFilteredData() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJabatan.setCellValueFactory(new PropertyValueFactory<>("sNama"));
        colTglLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colJenisKelamin.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        // Default
        String sortOrder = "ASC";
        String sortColumn = "kry_nama";

        // Mapping Filter
        if (cbFilter.getValue() != null) {
            sortColumn = switch (cbFilter.getValue()) {
                case "Nama" -> "kry_nama";
                case "Jabatan" -> "s_nama";
                case "Jenis Kelamin" -> "kry_gender";
                case "Alamat" -> "kry_alamat";
                default -> "kry_nama";
            };
        }

        // Mapping Sort
        if (cbSort.getValue() != null) {
            sortOrder = switch (cbSort.getValue()) {
                case "Naik   ⬆" -> "ASC";
                case "Turun ⬇" -> "DESC";
                default -> "ASC";
            };
        }

        // Mapping Status
        if (cbStatus.getValue() != null) {
            OrderStatus = switch (cbStatus.getValue()) {
                case "Aktif" -> 1;
                case "Tidak Aktif" -> 0;
                default -> 1;
            };
        }

        List<karyawan> list = connection.getListKaryawan(txtCari.getText(), OrderStatus, sortColumn, sortOrder);
        allKaryawanList.setAll(list);

        int pageCount = (int) Math.ceil(allKaryawanList.size() * 1.0 / ROWS_PER_PAGE);
        pgnKaryawan.setPageCount(pageCount);
        pgnKaryawan.setCurrentPageIndex(0);
        pgnKaryawan.setPageFactory(this::createPage);
    }

    /*--- Trigger klik tombol sort ---*/
    @FXML
    protected void onClickCbSort(){
        loadFilteredData();
    }

    /*--- Trigger klik tombol filter ---*/
    @FXML
    protected void onClickCbFilter(){
        loadFilteredData();
    }

    /*--- Clear sort & filter ---*/
    @FXML
    protected void onClickBtnClear(){
        cbSort.setValue(null);
        cbFilter.setValue(null);
        cbStatus.setValue(null);
        OrderStatus = 1;
        loadFilteredData();
    }

    /*--- Searching ---*/
    @FXML
    protected void onKeyTypedTxtCari(){
        loadFilteredData();
    }

    /*--- Menghapus semua data ---*/
    private void clear() {
        kry_id = 0;
        txtNama.clear();
        txtUsername.clear();
        txtPassword.clear();
        username = null;
        password = null;
        txtAlamat.clear();
        cbJenisKelamin.setValue(null);
        cbJabatan.setValue(null);
        dpTanggalLahir.setValue(null);
        isJabatanAccess = false;
    }

    /*--- Menambahkan item pada combobox jabatan ---*/
    private void loadComboBoxJabatan(){
        cbJabatan.getItems().clear();

        for (Setting jabatan : connection.getListSettingByKategori("jabatan")) {
            cbJabatan.getItems().add(jabatan);
        }
    }

    /*--- Membawa masuk nama dari orang login ---*/
    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<karyawan> allKaryawanList = FXCollections.observableArrayList();

    private void loadDataKaryawan() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJabatan.setCellValueFactory(new PropertyValueFactory<>("sNama"));
        colTglLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colJenisKelamin.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        List<karyawan> list = connection.getListKaryawan(null, 1, "kry_nama", "ASC");
        allKaryawanList.setAll(list);

        int pageCount = (int) Math.ceil(allKaryawanList.size() * 1.0 / ROWS_PER_PAGE);
        pgnKaryawan.setPageCount(pageCount);
        pgnKaryawan.setCurrentPageIndex(0);
        pgnKaryawan.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allKaryawanList.size());

        ObservableList<karyawan> currentPageData =
                FXCollections.observableArrayList(allKaryawanList.subList(fromIndex, toIndex));

        tbKaryawan.setItems(currentPageData);
        tbKaryawan.refresh();
        return new StackPane();
    }

    /*--- Menambahkan tombol berupa ikon pada tabel ---*/
    private void addButtonToTable() {
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button();
            private final Button btnDelete = new Button();
            private final Button btnRestore = new Button();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 20, 0));

                if(OrderStatus == 1) {
                    // Load ikon edit
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/edit-icon.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnEdit.setGraphic(iconEdit);
                    btnEdit.setStyle("-fx-background-color: transparent;");

                    // Load ikon hapus
                    ImageView iconDelete = new ImageView(getClass().getResource("/Pict/Icons/delete-icon.png").toExternalForm());
                    iconDelete.setFitHeight(30);
                    iconDelete.setFitWidth(30);
                    btnDelete.setGraphic(iconDelete);
                    btnDelete.setStyle("-fx-background-color: transparent;");

                    // Event handler
                    btnEdit.setOnAction(e -> {
                        karyawan data = getTableView().getItems().get(getIndex());
                        /*--- Memasukkan data dari tabel ke form di sebelah kanan ---*/
                        kry_id = data.getId();
                        txtNama.setText(data.getNama());
                        dpTanggalLahir.setValue((data.getTanggalLahir()));
                        txtUsername.setText(data.getUsername());
                        username = data.getUsername();
                        password = data.getPassword();
                        txtAlamat.setText(data.getAlamat());
                        for (Setting item : cbJabatan.getItems()) {
                            if (item.getS_nama().equals(data.getSNama())) {
                                cbJabatan.setValue(item);
                                break;
                            }
                        }
                        cbJenisKelamin.setValue(data.getGender());
                        onClickCbJabatan();
                    });

                    btnDelete.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin menghapus data?")) {
                            karyawan data = getTableView().getItems().get(getIndex());
                            kry_id = data.getId();
                            connection.deleteKaryawan(kry_id, userAccess);
                            loadDataKaryawan();
                            onClickBtnClear();
                        }
                    });

                    hBox.getChildren().addAll(btnEdit, btnDelete);
                }
                else if(OrderStatus == 0) {
                    // Load ikon restore
                    ImageView iconRestore = new ImageView(getClass().getResource("/Pict/Icons/restore-icon.png").toExternalForm());
                    iconRestore.setFitHeight(30);
                    iconRestore.setFitWidth(30);
                    btnRestore.setGraphic(iconRestore);
                    btnRestore.setStyle("-fx-background-color: transparent;");

                    btnRestore.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin mengaktifkan kembali data?")) {
                            karyawan data = getTableView().getItems().get(getIndex());
                            kry_id = data.getId();
                            connection.deleteKaryawan(kry_id, userAccess);
                            loadDataKaryawan();
                            onClickBtnClear();
                        }
                    });

                    hBox.getChildren().addAll(btnRestore);
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        });
    }

    /*--- Membuat tabel tidak bisa diubah ---*/
    private void lockTableViewBehavior(TableView<?> tableView) {
        // Nonaktifkan seleksi baris
        tableView.setSelectionModel(null);

        // Nonaktifkan resize kolom & sort
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            colAksi.setPrefWidth(118);
            column.setResizable(false);
        }

        // Nonaktifkan fokus keyboard
        tableView.setFocusTraversable(false);

        // Nonaktifkan drag-drop header kolom
        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            tableView.lookupAll(".column-header").forEach(header -> {
                header.setMouseTransparent(true);
            });
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataKaryawan();
        addButtonToTable();
        loadComboBoxJabatan();
        cbJenisKelamin.getItems().addAll("Laki-laki", "Perempuan");
        lockTableViewBehavior(tbKaryawan);
        cbSort.getItems().addAll("Naik   ⬆", "Turun ⬇");
        cbFilter.getItems().addAll("Nama", "Jabatan", "Jenis Kelamin", "Alamat");
        cbStatus.getItems().addAll("Aktif", "Tidak Aktif");
    }
}
