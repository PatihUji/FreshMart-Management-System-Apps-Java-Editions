package master.JenisProduk;

import Helper.MessageBox;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JenisProdukController implements Initializable {
    @FXML
    private TextField txtNama;

    @FXML
    private TableView<JenisProduk> tbJenisProduk;

    @FXML
    private TableColumn colNama;

    @FXML
    private TableColumn<JenisProduk, Void> colAksi;

    @FXML
    private Pagination pgnJenisProduk;

    @FXML
    private TextField txtCari;

    @FXML
    private Button btnSimpan, btnBatal;
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

    private int jp_id = 0;

    MessageBox messageBox = new MessageBox();

    private boolean isSortFilter = false;

    @FXML
    protected void onClickBtnSimpan(){
        if (txtNama.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            messageBox.alertWarning("Silakan lengkapi semua data terlebih dahulu!");
        }
        else {
            if (jp_id != 0 && connection.isJenisProdukExist(jp_id)) {
                connection.updateJenisProduk(jp_id, txtNama.getText(), userAccess);
                clear();
                loadDataJenisProduk();
                messageBox.alertInfo("Jenis Produk berhasil diperbarui!");
            }
            else {

                connection.insertJenisProduk(txtNama.getText(),  userAccess);
                messageBox.alertInfo("Jenis Produk berhasil ditambahkan!");
                clear();
                loadDataJenisProduk();
            }
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
        });
    }

    @FXML
    protected void onKeyTypedTxtCari(){
        colNama.setCellValueFactory(new PropertyValueFactory<>("jp_nama"));
        List<JenisProduk> list = connection.getListJenisProduk(txtCari.getText(), 1, "kry_id", "ASC");
        allJenisProdukList.setAll(list);

        int pageCount = (int) Math.ceil(allJenisProdukList.size() * 1.0 / ROWS_PER_PAGE);
        pgnJenisProduk.setPageCount(pageCount);
        pgnJenisProduk.setCurrentPageIndex(0);
        pgnJenisProduk.setPageFactory(this::createPage);
    }

    /*--- Menghapus semua data ---*/
    private void clear() {
        jp_id = 0;
        txtNama.clear();
    }

    /*--- Membawa masuk nama dari orang login ---*/
    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<JenisProduk> allJenisProdukList = FXCollections.observableArrayList();

    private void loadDataJenisProduk() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("jp_nama"));
        List<JenisProduk> list = connection.getListJenisProduk(null, 1,"jp_id", "ASC");
        allJenisProdukList.setAll(list);

        int pageCount = (int) Math.ceil(allJenisProdukList.size() * 1.0 / ROWS_PER_PAGE);
        pgnJenisProduk.setPageCount(pageCount);
        pgnJenisProduk.setCurrentPageIndex(0);
        pgnJenisProduk.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allJenisProdukList.size());

        ObservableList<JenisProduk> currentPageData =
                FXCollections.observableArrayList(allJenisProdukList.subList(fromIndex, toIndex));

        tbJenisProduk.setItems(currentPageData);
        tbJenisProduk.refresh();
        return new StackPane();
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
        colNama.setCellValueFactory(new PropertyValueFactory<>("jp_nama"));

        // Default
        String sortOrder = "ASC";
        String sortColumn = "jp_nama";

        // Mapping Filter
        if (cbFilter.getValue() != null) {
            sortColumn = switch (cbFilter.getValue()) {
                case "Nama" -> "jp_nama";
                default -> "jp_nama";
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

        List<JenisProduk> list = connection.getListJenisProduk(txtCari.getText(), OrderStatus, sortColumn, sortOrder);
        allJenisProdukList.setAll(list);

        int pageCount = (int) Math.ceil(allJenisProdukList.size() * 1.0 / ROWS_PER_PAGE);
        pgnJenisProduk.setPageCount(pageCount);
        pgnJenisProduk.setCurrentPageIndex(0);
        pgnJenisProduk.setPageFactory(this::createPage);
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

                if (OrderStatus == 1) {
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
                        JenisProduk data = getTableView().getItems().get(getIndex());
                        /*--- Memasukkan data dari tabel ke form di sebelah kanan ---*/
                        jp_id = data.getJp_id();
                        txtNama.setText(data.getJp_nama());
                    });

                    btnDelete.setOnAction(e -> {
                        if(messageBox.alertConfirm("Apakah anda yakin ingin menghapus data?")) {}
                        JenisProduk data = getTableView().getItems().get(getIndex());
                        jp_id = data.getJp_id();
                        connection.deleteJenisProduk(jp_id, userAccess);
                        loadDataJenisProduk();
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
                            JenisProduk data = getTableView().getItems().get(getIndex());
                            jp_id = data.getJp_id();
                            connection.deleteJenisProduk(jp_id, userAccess);
                            loadDataJenisProduk();
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
        loadDataJenisProduk();
        addButtonToTable();
        lockTableViewBehavior(tbJenisProduk);
        cbSort.getItems().addAll("Naik   ⬆", "Turun ⬇");
        cbFilter.getItems().addAll("Nama");
        cbStatus.getItems().addAll("Aktif", "Tidak Aktif");

        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                txtNama.setText(oldValue);
            }
        });

    }


}