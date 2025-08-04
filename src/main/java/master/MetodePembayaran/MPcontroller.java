package master.MetodePembayaran;

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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MPcontroller implements Initializable {

    @FXML
    private TextField txtNama;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;
    @FXML
    private TextField txtCari;
    @FXML
    private TableView<MetodePembayaran> tbMetode;
    @FXML
    private TableColumn colNama;
    @FXML
    private TableColumn<MetodePembayaran, Void> colAksi;
    @FXML
    private Pagination pgnMetode;

    @FXML
    private ComboBox<String> cbSort, cbFilter, cbStatus;
    @FXML
    private Button btnSortFilter, btnClear;
    @FXML
    private AnchorPane apSortFilter;

    private boolean isSortFilter = false;
    private int mpb_id = 0;
    private int OrderStatus = 1;

    DBConnect connection = new DBConnect();

    MessageBox messageBox = new MessageBox();

    @FXML
    protected void onClickBtnSimpan() {
        if (txtNama.getText().isEmpty()) {
            messageBox.alertWarning("Nama tidak boleh kosong!");
        } else {
            if (mpb_id != 0 && connection.isMetodePembayaranExist(mpb_id)) {
                connection.updateMetodePembayaran(mpb_id, txtNama.getText(), userAccess);
                clear();
                loadDataMetode();

                messageBox.alertInfo("Metode Pembayaran berhasil diperbarui!");

            } else {

                connection.insertMetodePembayaran(txtNama.getText(), userAccess);
                clear();
                loadDataMetode();

                messageBox.alertInfo("Metode Pembayaran berhasil ditambahkan!");
            }
        }
    }

    @FXML
    protected void onClickBtnBatal() {
        clear();
    }

    /*--- Mengatur tombol yang bisa ditekan ---*/
    @FXML
    protected void onKeyPressTxtNama() {
        txtNama.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
                event.consume();
            }
            if (txtNama.getText().length() >= 50) {
                event.consume();
            }
        });
    }

    /*--- Muncul panel sort & filter ---*/
    @FXML
    protected void onClickBtnSortFilter() {
        if (isSortFilter) {
            apSortFilter.setVisible(false);
            isSortFilter = false;
        } else {
            apSortFilter.setVisible(true);
            isSortFilter = true;
        }
    }

    /*--- Mengubah data berdasarkan sort & filter ---*/
    private void loadFilteredData() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("mpb_nama"));

        // Default
        String sortOrder = "ASC";
        String sortColumn = "mpb_nama";

        // Mapping Filter
        if (cbFilter.getValue() != null) {
            sortColumn = switch (cbFilter.getValue()) {
                case "Nama" -> "mpb_nama";
                case "Tanggal Dibuat" -> "mpb_created_date";
                default -> "mpb_nama";
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

        List<MetodePembayaran> list = connection.getListMetodePembayaran(txtCari.getText(), OrderStatus, sortColumn, sortOrder);
        allMpbList.setAll(list);

        int pageCount = (int) Math.ceil(allMpbList.size() * 1.0 / ROWS_PER_PAGE);
        pgnMetode.setPageCount(pageCount);
        pgnMetode.setCurrentPageIndex(0);
        pgnMetode.setPageFactory(this::createPage);
    }

    /*--- Trigger klik tombol sort ---*/
    @FXML
    protected void onClickCbSort() {
        loadFilteredData();
    }

    /*--- Trigger klik tombol filter ---*/
    @FXML
    protected void onClickCbFilter() {
        loadFilteredData();
    }

    /*--- Clear sort & filter ---*/
    @FXML
    protected void onClickBtnClear() {
        cbSort.setValue(null);
        cbFilter.setValue(null);
        cbStatus.setValue(null);
        OrderStatus = 1;
        loadFilteredData();
    }

    /*--- Searching ---*/
    @FXML
    protected void onKeyTypedTxtCari() {
        loadFilteredData();
    }

    /*--- Menghapus semua data ---*/
    private void clear() {
        mpb_id = 0;
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
    private ObservableList<MetodePembayaran> allMpbList = FXCollections.observableArrayList();

    private void loadDataMetode() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("mpb_nama"));
        List<MetodePembayaran> list = connection.getListMetodePembayaran(null, 1, "mpb_id", "ASC");
        allMpbList.setAll(list);

        int pageCount = (int) Math.ceil(allMpbList.size() * 1.0 / ROWS_PER_PAGE);
        pgnMetode.setPageCount(pageCount);
        pgnMetode.setCurrentPageIndex(0);
        pgnMetode.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allMpbList.size());

        ObservableList<MetodePembayaran> currentPageData =
                FXCollections.observableArrayList(allMpbList.subList(fromIndex, toIndex));

        tbMetode.setItems(currentPageData);
        tbMetode.refresh();
        return new StackPane();
    }

    /*--- Menambahkan tombol berupa ikon pada tabel ---*/
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
                    // Ikon Edit
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/edit-icon.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnEdit.setGraphic(iconEdit);
                    btnEdit.setStyle("-fx-background-color: transparent;");

                    // Ikon Hapus
                    ImageView iconDelete = new ImageView(getClass().getResource("/Pict/Icons/delete-icon.png").toExternalForm());
                    iconDelete.setFitHeight(30);
                    iconDelete.setFitWidth(30);
                    btnDelete.setGraphic(iconDelete);
                    btnDelete.setStyle("-fx-background-color: transparent;");

                    // Event Edit
                    btnEdit.setOnAction(e -> {
                        MetodePembayaran data = getTableView().getItems().get(getIndex());
                        mpb_id = data.getMpb_id();
                        txtNama.setText(data.getMpb_nama());
                    });

                    // Event Delete
                    btnDelete.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin menghapus data?")) {
                            MetodePembayaran data = getTableView().getItems().get(getIndex());
                            mpb_id = data.getMpb_id();
                            connection.deleteMetodePembayaran(mpb_id, userAccess);
                            loadFilteredData();
                        }
                    });

                    hBox.getChildren().addAll(btnEdit, btnDelete);
                } else if (OrderStatus == 0) {
                    // Ikon Restore
                    ImageView iconRestore = new ImageView(getClass().getResource("/Pict/Icons/restore-icon.png").toExternalForm());
                    iconRestore.setFitHeight(30);
                    iconRestore.setFitWidth(30);
                    btnRestore.setGraphic(iconRestore);
                    btnRestore.setStyle("-fx-background-color: transparent;");

                    // Event Restore
                    btnRestore.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin mengaktifkan kembali data?")) {
                            MetodePembayaran data = getTableView().getItems().get(getIndex());
                            mpb_id = data.getMpb_id();
                            connection.deleteMetodePembayaran(mpb_id, userAccess);
                            OrderStatus = 1;
                            loadFilteredData();
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
        // Inisialisasi combobox Filter
        cbFilter.getItems().addAll("Nama");
        cbFilter.setValue(null);

        // Inisialisasi combobox Sort
        cbSort.getItems().addAll("Naik   ⬆", "Turun ⬇");
        cbSort.setValue(null);

        // Inisialisasi combobox Status
        cbStatus.getItems().addAll("Aktif", "Tidak Aktif");
        OrderStatus = 1;

        // Tampilkan data
        loadFilteredData();
        addButtonToTable();
        lockTableViewBehavior(tbMetode);
    }
}

