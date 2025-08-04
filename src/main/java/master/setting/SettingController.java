package master.setting;

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
import javafx.stage.Modality;
import Helper.MessageBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML private TextField txtNama;
    @FXML private ComboBox<String> cbKategori;
    @FXML private Button btnSimpan, btnBatal;
    @FXML private TextField txtCari;
    @FXML private TableView<Setting> tbSetting;
    @FXML private TableColumn colNama, colKategori, colAksi;
    @FXML private Pagination pgnSetting;
    @FXML
    private ComboBox<String> cbSort, cbFilter, cbStatus;
    @FXML
    private Button btnSortFilter, btnClear;
    @FXML
    private AnchorPane apSortFilter;

    private DBConnect connection = new DBConnect();
    private ObservableList<Setting> allSettingList = FXCollections.observableArrayList();
    private static final int ROWS_PER_PAGE = 8;
    private int s_id = 0;
    private String userAccess;
    private boolean isSortFilter = false;
    private int OrderStatus = 1;
    MessageBox messageBox = new MessageBox();


    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    @FXML
    protected void onClickBtnSimpan() {
        if (txtNama.getText().isEmpty() || cbKategori.getValue() == null) {
            messageBox.alertWarning("Tidak boleh kosong");
        } else {
            if (s_id != 0 && connection.isSettingExist(s_id)) {
                connection.updateSetting(s_id, txtNama.getText(), cbKategori.getValue(), userAccess);
                messageBox.alertInfo("Setting berhasil diperbarui");
            } else {
                connection.insertSetting(txtNama.getText(), cbKategori.getValue(), userAccess);
                messageBox.alertInfo("Setting berhasil ditambahkan");
            }
            clear();
            loadDataSetting();
        }
    }

    @FXML
    protected void onClickBtnBatal() {
        clear();
    }

    @FXML
    protected void onKeyTypedTxtCari() {
        loadDataSetting();
    }

    @FXML
    protected void onKeyPressTxtNama(){
        txtNama.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
                event.consume();
            }
        });
    }

    private void clear() {
        s_id = 0;
        txtNama.clear();
        cbKategori.setValue(null);
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

    /*--- Mengubah data berdasarkan sort & filter ---*/
    private void loadFilteredData() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("s_nama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("s_kategori"));

        // Default
        String sortOrder = "ASC";
        String sortColumn = "s_nama";

        // Mapping Filter
        if (cbFilter.getValue() != null) {
            sortColumn = switch (cbFilter.getValue()) {
                case "Nama" -> "s_nama";
                case "Kategori" -> "s_kategori";
                default -> "s_nama";
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

        List<Setting> list = connection.getListSetting(txtCari.getText(), OrderStatus, sortColumn, sortOrder);
        allSettingList.setAll(list);

        int pageCount = (int) Math.ceil(allSettingList.size() * 1.0 / ROWS_PER_PAGE);
        pgnSetting.setPageCount(pageCount);
        pgnSetting.setCurrentPageIndex(0);
        pgnSetting.setPageFactory(this::createPage);
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

    private void loadComboBoxKategori() {
        cbKategori.getItems().clear();
        cbKategori.getItems().addAll("Jabatan", "Jenis Pembelian"); // contoh
    }

    private void loadDataSetting() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("s_nama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("s_kategori"));
        List<Setting> list = connection.getListSetting(txtCari.getText(), 1, "s_id", "ASC");
        allSettingList.setAll(list);
        int pageCount = (int) Math.ceil(allSettingList.size() * 1.0 / ROWS_PER_PAGE);
        pgnSetting.setPageCount(pageCount);
        pgnSetting.setCurrentPageIndex(0);
        pgnSetting.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int from = pageIndex * ROWS_PER_PAGE;
        int to = Math.min(from + ROWS_PER_PAGE, allSettingList.size());
        ObservableList<Setting> currentPage = FXCollections.observableArrayList(allSettingList.subList(from, to));
        tbSetting.setItems(currentPage);
        tbSetting.refresh();
        return new StackPane();
    }

    private void addButtonToTable() {
        colAksi.setCellFactory(param -> new TableCell<Setting, Void>() {
            private final Button btnEdit = new Button();
            private final Button btnDelete = new Button();
            private final Button btnRestore = new Button();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 20, 0));

                if (OrderStatus == 1) {
                    // Icon Edit
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/edit-icon.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnEdit.setGraphic(iconEdit);
                    btnEdit.setStyle("-fx-background-color: transparent;");

                    // Icon Delete
                    ImageView iconDelete = new ImageView(getClass().getResource("/Pict/Icons/delete-icon.png").toExternalForm());
                    iconDelete.setFitHeight(30);
                    iconDelete.setFitWidth(30);
                    btnDelete.setGraphic(iconDelete);
                    btnDelete.setStyle("-fx-background-color: transparent;");

                    // Action saat klik Edit
                    btnEdit.setOnAction(e -> {
                        Setting data = getTableView().getItems().get(getIndex());
                        s_id = data.getS_id();
                        txtNama.setText(data.getS_nama());
                        cbKategori.setValue(data.getS_kategori());
                    });

                    // Action saat klik Delete
                    btnDelete.setOnAction(e -> {
                        if(messageBox.alertConfirm("Apakah anda yakin ingin menghapus data?")){
                            Setting data = getTableView().getItems().get(getIndex());
                            connection.deleteSetting(data.getS_id(), userAccess);
                            loadDataSetting();
                        }
                    });

                    hBox.getChildren().addAll(btnEdit, btnDelete);
                } else if (OrderStatus == 0) {
                    // Load ikon restore
                    ImageView iconRestore = new ImageView(getClass().getResource("/Pict/Icons/restore-icon.png").toExternalForm());
                    iconRestore.setFitHeight(30);
                    iconRestore.setFitWidth(30);
                    btnRestore.setGraphic(iconRestore);
                    btnRestore.setStyle("-fx-background-color: transparent;");

                    btnRestore.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin mengaktifkan kembali data?")) {
                            Setting data = getTableView().getItems().get(getIndex());
                            s_id = data.getS_id();
                            connection.deleteSetting(s_id, userAccess);
                            loadDataSetting();
                            OrderStatus = 1;
                            onClickBtnClear();
                        }
                    });
                    hBox.getChildren().addAll(btnRestore);
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }


    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(main.thefreshchoice.MainApplication.primaryStage);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.show();
    }

    /*--- Membuat tabel tidak bisa diubah ---*/
    private void lockTableViewBehavior(TableView<?> tableView) {
        // Nonaktifkan seleksi baris
        tableView.setSelectionModel(null);

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
        loadComboBoxKategori();
        loadDataSetting();
        addButtonToTable();
        lockTableViewBehavior(tbSetting);
        cbSort.getItems().addAll("Naik   ⬆", "Turun ⬇");
        cbFilter.getItems().addAll("Nama", "Kategori");
        cbStatus.getItems().addAll("Aktif", "Tidak Aktif");

        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                txtNama.setText(oldValue);
            }
        });

    }
}

