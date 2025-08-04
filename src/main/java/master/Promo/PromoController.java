package master.Promo;

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
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PromoController implements Initializable {
    @FXML
    private TextField txtNama;

    @FXML
    private DatePicker dpTanggalMulai;

    @FXML
    private DatePicker dpTanggalBerakhir;

    @FXML
    private TextField txtPersentase;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnBatal;

    @FXML
    private TableView<Promo> tbPromo;

    @FXML
    private TableColumn colNama;

    @FXML
    private TableColumn colPersentase;

    @FXML
    private TableColumn colTanggalMulai;

    @FXML
    private TableColumn colTanggalBerakhir;

    @FXML
    private TableColumn<Promo, Void> colAksi;

    @FXML
    private Pagination pgnPromo;

    @FXML
    private TextField txtCari;

    @FXML
    private ComboBox<String> cbSort, cbFilter, cbStatus;
    @FXML
    private Button btnSortFilter, btnClear;
    @FXML
    private AnchorPane apSortFilter;

    private boolean isSortFilter = false;
    private int p_id = 0;
    private int OrderStatus = 1;

    DBConnect connection = new DBConnect();

    private int pr_id = 0;

    MessageBox messageBox = new MessageBox();

    @FXML
    protected void onClickBtnSimpan() {
        if (txtNama.getText().isEmpty() || txtPersentase.getText().isEmpty() || dpTanggalMulai.getValue() == null || dpTanggalBerakhir.getValue() == null) {
            messageBox.alertWarning("Silakan lengkapi semua data terlebih dahulu.");
        } else {
            try {
                double persentase = Double.parseDouble(txtPersentase.getText());

                if (persentase <= 0) {
                    messageBox.alertWarning("Persentase tidak boleh kurang dari sama dengan 0.");
                    return;
                } else if (persentase > 100) {
                    messageBox.alertWarning("Persentase tidak boleh lebih dari 100.");
                    return;
                }

                if (pr_id != 0 && connection.isPromoExist(pr_id)) {
                    connection.updatePromo(
                            pr_id,
                            txtNama.getText(),
                            persentase,
                            java.sql.Date.valueOf(dpTanggalMulai.getValue()),
                            java.sql.Date.valueOf(dpTanggalBerakhir.getValue()),
                            userAccess
                    );
                    clear();
                    loadDataPromo();

                    messageBox.alertInfo("Promo berhasil diperbarui!");
                } else {
                    connection.insertPromo(
                            txtNama.getText(),
                            persentase,
                            java.sql.Date.valueOf(dpTanggalMulai.getValue()),
                            java.sql.Date.valueOf(dpTanggalBerakhir.getValue()),
                            userAccess
                    );
                    clear();
                    loadDataPromo();

                    messageBox.alertInfo("Promo berhasil ditambahkan!");
                }
            } catch (NumberFormatException e) {
                messageBox.alertWarning("Persentase harus berupa angka yang valid.");
            }
        }
    }

    /*--- Mengatur tombol yang bisa ditekan ---*/
    // Validasi nama
    @FXML
    protected void onKeyPressTxtNama() {
        txtNama.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[a-zA-Z\\s]")) {
                event.consume();
            }
            if (txtNama.getText().length() > 50) {
                event.consume();
            }
        });
    }

    //validasi persentase
    @FXML
    protected void onKeyPresstxtPersentase() {
        txtPersentase.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            // Cek apakah karakter yang diketik bukan angka
            if (!event.getCharacter().matches("\\d")) {
                event.consume();
            }
            // Cek apakah panjang teks melebihi 3 karakter
            if (txtPersentase.getText().length() >= 3) {
                event.consume();
            }
        });
    }

    @FXML
    protected void onActionDpTanggalMulai() {
        LocalDate tanggalMulai = dpTanggalMulai.getValue();
        LocalDate tanggalBerakhir = dpTanggalBerakhir.getValue();
        LocalDate today = LocalDate.now();

        if (tanggalMulai != null) {
            if (tanggalMulai.isBefore(today)) {
                messageBox.alertWarning("Tanggal mulai promo tidak boleh sebelum hari ini!");
                dpTanggalMulai.setValue(null);
            } else if (tanggalBerakhir != null && tanggalMulai.isAfter(tanggalBerakhir)) {
                messageBox.alertWarning("Tanggal mulai promo tidak boleh setelah tanggal berakhir!");
                dpTanggalMulai.setValue(null);
            }
        }
    }

    @FXML
    protected void onActionDpTanggalBerakhir() {
        LocalDate tanggalMulai = dpTanggalMulai.getValue();
        LocalDate tanggalBerakhir = dpTanggalBerakhir.getValue();
        LocalDate today = LocalDate.now();

        if (tanggalBerakhir != null) {
            if (tanggalBerakhir.isBefore(today)) {
                messageBox.alertWarning("Tanggal berakhir promo tidak boleh sebelum hari ini!");
                dpTanggalBerakhir.setValue(null);
            } else if (tanggalMulai != null && tanggalBerakhir.isBefore(tanggalMulai)) {
                messageBox.alertWarning("Tanggal berakhir promo tidak boleh sebelum tanggal mulai!");
                dpTanggalBerakhir.setValue(null);
            }
        }
    }

    @FXML
    protected void onClickBtnBatal() {
        clear();
    }

    /*--- Mengubah data berdasarkan sort & filter ---*/
    private void loadFilteredData() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("pr_nama"));
        colPersentase.setCellValueFactory(new PropertyValueFactory<>("pr_persentase"));
        colTanggalMulai.setCellValueFactory(new PropertyValueFactory<>("pr_tanggalMulai"));
        colTanggalBerakhir.setCellValueFactory(new PropertyValueFactory<>("pr_tanggalBerakhir"));

        // Format persentase
        colPersentase.setCellFactory(tc -> new TableCell<Promo, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", value));
                }
            }
        });

        // Default
        String sortOrder = "ASC";
        String sortColumn = "pr_nama";

        // Mapping Filter
        if (cbFilter.getValue() != null) {
            sortColumn = switch (cbFilter.getValue()) {
                case "Nama" -> "pr_nama";
                case "Persentase" -> "pr_persentase";
                case "Tanggal Mulai" -> "pr_tanggal_mulai";
                case "Tanggal Berakhir" -> "pr_tanggal_berakhir";
                default -> "pr_nama";
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

        List<Promo> list = connection.getListPromo(txtCari.getText(), OrderStatus, sortColumn, sortOrder);
        allPromoList.setAll(list);

        int pageCount = (int) Math.ceil(allPromoList.size() * 1.0 / ROWS_PER_PAGE);
        pgnPromo.setPageCount(pageCount);
        pgnPromo.setCurrentPageIndex(0);
        pgnPromo.setPageFactory(this::createPage);
    }

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


    @FXML
    protected void onClickCbSort() {
        loadFilteredData();
    }

    @FXML
    protected void onClickCbFilter() {
        loadFilteredData();
    }

    @FXML
    protected void onClickBtnClear() {
        cbSort.setValue(null);
        cbFilter.setValue(null);
        cbStatus.setValue(null);
        OrderStatus = 1;
        loadFilteredData();
    }

    @FXML
    protected void onKeyTypedTxtCari() {
        loadFilteredData();
    }


    /*--- Menghapus semua data ---*/
    private void clear() {
        pr_id = 0;
        txtNama.clear();
        txtPersentase.clear();
        dpTanggalMulai.setValue(null);
        dpTanggalBerakhir.setValue(null);
    }

    /*--- Membawa masuk nama dari orang login ---*/
    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<Promo> allPromoList = FXCollections.observableArrayList();

    private void loadDataPromo() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("pr_nama"));
        colPersentase.setCellValueFactory(new PropertyValueFactory<>("pr_persentase"));
        // Tambahan: Format tampilan kolom persentase agar muncul tanda %
        colPersentase.setCellFactory(tc -> new TableCell<Promo, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", value));
                }
            }
        });
        colTanggalMulai.setCellValueFactory(new PropertyValueFactory<>("pr_tanggalMulai"));
        colTanggalBerakhir.setCellValueFactory(new PropertyValueFactory<>("pr_tanggalBerakhir"));
        List<Promo> list = connection.getListPromo(null, 1, "pr_id", "ASC");
        allPromoList.setAll(list);

        int pageCount = (int) Math.ceil(allPromoList.size() * 1.0 / ROWS_PER_PAGE);
        pgnPromo.setPageCount(pageCount);
        pgnPromo.setCurrentPageIndex(0);
        pgnPromo.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allPromoList.size());

        ObservableList<Promo> currentPageData =
                FXCollections.observableArrayList(allPromoList.subList(fromIndex, toIndex));

        tbPromo.setItems(currentPageData);
        tbPromo.refresh();
        return new StackPane();
    }

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
                    // Edit icon
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/edit-icon.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnEdit.setGraphic(iconEdit);
                    btnEdit.setStyle("-fx-background-color: transparent;");

                    // Delete icon
                    ImageView iconDelete = new ImageView(getClass().getResource("/Pict/Icons/delete-icon.png").toExternalForm());
                    iconDelete.setFitHeight(30);
                    iconDelete.setFitWidth(30);
                    btnDelete.setGraphic(iconDelete);
                    btnDelete.setStyle("-fx-background-color: transparent;");

                    btnEdit.setOnAction(e -> {
                        Promo data = getTableView().getItems().get(getIndex());
                        pr_id = data.getPr_id();
                        txtNama.setText(data.getPr_nama());
                        txtPersentase.setText(String.format("%.0f", data.getPr_persentase()));
                        dpTanggalMulai.setValue(data.getPr_tanggalMulai());
                        dpTanggalBerakhir.setValue(data.getPr_tanggalBerakhir());
                    });

                    btnDelete.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin menghapus data?")) {
                            Promo data = getTableView().getItems().get(getIndex());
                            pr_id = data.getPr_id();
                            connection.deletePromo(pr_id, userAccess);
                            loadFilteredData();
                        }
                    });

                    hBox.getChildren().addAll(btnEdit, btnDelete);
                } else if (OrderStatus == 0) {
                    // Restore icon
                    ImageView iconRestore = new ImageView(getClass().getResource("/Pict/Icons/restore-icon.png").toExternalForm());
                    iconRestore.setFitHeight(30);
                    iconRestore.setFitWidth(30);
                    btnRestore.setGraphic(iconRestore);
                    btnRestore.setStyle("-fx-background-color: transparent;");

                    btnRestore.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin mengaktifkan kembali promo ini?")) {
                            Promo data = getTableView().getItems().get(getIndex());
                            pr_id = data.getPr_id();
                            connection.deletePromo(pr_id, userAccess);
                            loadFilteredData();
                        }
                    });

                    hBox.getChildren().add(btnRestore);
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
        tableView.setSelectionModel(null);
        tableView.setFocusTraversable(false);
        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            tableView.lookupAll(".column-header").forEach(header -> {
                header.setMouseTransparent(true);
            });
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onKeyPresstxtPersentase();

        // Isi pilihan ComboBox
        cbFilter.getItems().addAll("Nama", "Persentase", "Tanggal Mulai", "Tanggal Berakhir");
        cbSort.getItems().addAll("Naik   ⬆", "Turun ⬇");
        cbStatus.getItems().addAll("Aktif", "Tidak Aktif");

        loadDataPromo();
        loadFilteredData();
        addButtonToTable();
        lockTableViewBehavior(tbPromo);
    }
}
