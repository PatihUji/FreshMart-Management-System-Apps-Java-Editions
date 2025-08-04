package master.produk;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import master.JenisProduk.JenisProduk;
import Helper.MessageBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ProdukController implements Initializable {
    FileChooser fileChooser = new FileChooser();

    private int p_id = 0;

    @FXML
    private Pagination pgnProduk;

    @FXML
    private ImageView imageViewProduk;

    private File selectedFile;
    private String savedFileName;

    private String namaFileGambar = null;  // nama file yang disimpan ke DB

    @FXML
    private TextField txtCari;

    @FXML
    private TextField txtNama;

    @FXML
    private ComboBox<JenisProduk> cbJenisProduk;

    @FXML
    private TextField txtHarga;

    @FXML
    private TextField txtSatuan;

    @FXML
    private TextField txtStock;

    @FXML
    private TextArea txtDeskripsi;

    @FXML
    private TableView<Produk> tbProduk;

    @FXML
    private TableColumn colNama;

    @FXML
    private TableColumn colJenisProduk;

    @FXML
    private TableColumn<Produk, Double> colHarga;

    @FXML
    private TableColumn colSatuan;

    @FXML
    private TableColumn colStok;

    @FXML
    private TableColumn colDeskripsi;

    @FXML
    private TableColumn <Produk, Void> colAksi;

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

    private boolean isSortFilter = false;

    public void setUserAkses(String userAccess) {
        this.userAccess = userAccess;
    }

    DBConnect connection = new DBConnect();

    MessageBox messageBox = new MessageBox();

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Produk");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );


        Stage stage = (Stage) imageViewProduk.getScene().getWindow(); // atau pakai btnUpload.getScene()

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Buat nama file unik
                String fileExt = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                namaFileGambar = System.currentTimeMillis() + fileExt;

                // Buat folder pict/produk jika belum ada
                File pictFolder = new File("src/main/resources/Pict/produk");
                if (!pictFolder.exists()) {
                    pictFolder.mkdirs();  // buat folder jika belum ada
                }

                // Simpan ke folder luar JAR
                File destination = new File(pictFolder, namaFileGambar);
                Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Tampilkan gambar
                Image img = new Image(destination.toURI().toString());
                imageViewProduk.setImage(img);

                System.out.println("File tersimpan di: " + destination.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void setupTextLimit() {
        // Batasi 50 karakter untuk txtSatuan
        UnaryOperator<TextFormatter.Change> limit50 = change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 50) {
                return change;
            }
            return null;
        };

        txtSatuan.setTextFormatter(new TextFormatter<>(limit50));
    }

    @FXML
    private void setupDeskripsiLimit() {
        UnaryOperator<TextFormatter.Change> limit100 = change -> {
            String newText = change.getControlNewText();
            return newText.length() <= 100 ? change : null;
        };

        txtDeskripsi.setTextFormatter(new TextFormatter<>(limit100));
    }

    @FXML
    protected void onClickBtnSimpan(){
        String nama = txtNama.getText();
        String satuan = txtSatuan.getText();
        String stokStr = txtStock.getText();
        String hargaStr = txtHarga.getText().replace(".","");
//        JenisProduk jenisProduk = cbJenisProduk.getValue();
        String deskripsi = txtDeskripsi.getText();

        if (txtNama.getText().isEmpty() || txtHarga.getText().isEmpty() || txtSatuan.getText().isEmpty() || txtStock.getText().isEmpty() || cbJenisProduk.getSelectionModel().getSelectedItem() == null || txtDeskripsi.getText().isEmpty() || namaFileGambar == null || namaFileGambar.trim().isEmpty()) {
            messageBox.alertWarning("Data tidak boleh kosong!");
            return;
        }

        double harga = Double.parseDouble(hargaStr);
        int stok = Integer.parseInt(stokStr);

        if (harga <= 0) {
            messageBox.alertWarning("Harga tidak boleh 0 atau negatif!");
            return;
        }

        if (stok <= 0) {
            messageBox.alertWarning("Stok tidak boleh 0 atau negatif!");
            return;
        }

        else {
            JenisProduk jenisProduk = (JenisProduk) cbJenisProduk.getValue();

            if (p_id != 0 && connection.isProdukExist(p_id)) {
                connection.updateProduk(p_id, jenisProduk.getJp_id(), nama, harga, satuan, stok, deskripsi, namaFileGambar, userAccess);
                clear();
                loadDataProduk();
                messageBox.alertInfo("Data berhasil diperbarui");
            } else {
                // Simpan data baru
                connection.insertProduk(jenisProduk.getJp_id(), nama, harga, satuan, stok, deskripsi, namaFileGambar, userAccess);
                clear();
                loadDataProduk();
                messageBox.alertInfo("Data berhasil ditambahkan");
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
            String karakter = event.getCharacter();
            String teksSaatIni = txtNama.getText();

            if (!karakter.matches("[a-zA-Z\\s]")) {
                event.consume();
                return;
            }

            if (teksSaatIni.length() >= 50) {
                event.consume();
            }
        });
    }

    @FXML
    private void setupNumericTextFields() {
        // Formatter untuk angka bulat (tanpa titik/desimal)
        UnaryOperator<TextFormatter.Change> angkaOnlyFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,9}")) { // Maksimal 9 digit
                return change;
            }
            return null;
        };

        // Terapkan ke txtHarga dan txtStock
//        txtHarga.setTextFormatter(new TextFormatter<>(angkaOnlyFilter));
        txtStock.setTextFormatter(new TextFormatter<>(angkaOnlyFilter));

        DecimalFormat currencyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("in", "ID"));
        currencyFormat.setGroupingUsed(true);
        currencyFormat.setMaximumFractionDigits(0);  // Tidak pakai koma/desimal

        UnaryOperator<TextFormatter.Change> hargaFilter = change -> {
            String newText = change.getControlNewText().replace(".", "");

            // Cegah input non-angka
            if (!newText.matches("\\d*")) {
                return null;
            }

            return change;
        };

// Formatter untuk txtHarga
        TextFormatter<String> hargaFormatter = new TextFormatter<>(hargaFilter);
        txtHarga.setTextFormatter(hargaFormatter);

// Tambahkan listener agar diformat setelah pengguna mengetik
        txtHarga.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;

            String cleaned = newValue.replaceAll("\\.", "");
            try {
                long value = Long.parseLong(cleaned);
                txtHarga.setText(currencyFormat.format(value));
            } catch (NumberFormatException e) {
                txtHarga.setText(oldValue);
            }
        });
    }

    @FXML
    protected void onKeyTypedTxtCari(){
//        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
//        colJenisProduk.setCellValueFactory(new PropertyValueFactory<>("jpnama"));
//        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
//        colSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));
//        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
//        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
//        List<Produk> list = connection.getListProduk(txtCari.getText(), 1, null, "p_id", "ASC");
//        allProdukList.setAll(list);
//
//        int pageCount = (int) Math.ceil(allProdukList.size() * 1.0 / ROWS_PER_PAGE);
//        pgnProduk.setPageCount(pageCount);
//        pgnProduk.setCurrentPageIndex(0);
//        pgnProduk.setPageFactory(this::createPage);
        loadFilteredData();
    }

    /*--- Menghapus semua data ---*/
    private void clear() {
        p_id = 0;
        txtNama.clear();
        txtHarga.clear();
        txtSatuan.clear();
        txtStock.clear();
        cbJenisProduk.setValue(null);
        txtDeskripsi.clear();
        imageViewProduk.setImage(new Image((getClass().getResource("/Pict/Icons/upload_produk.jpg").toExternalForm())));
    }

    /*--- Menambahkan item pada combobox jabatan ---*/
    private void loadComboBoxJenisProduk(){
        cbJenisProduk.getItems().clear();

        for (JenisProduk jenisProduk : connection.getNamaJenisProduk()) {
            cbJenisProduk.getItems().add(jenisProduk);
        }
//            if (cbJabatan != null) {
//                if (cbJabatan.getItems() != null) {
//                    cbJabatan.getItems().clear();
//                } else {
//                    cbJabatan.setItems(FXCollections.observableArrayList());
//                }
//
//                for (Setting jabatan : connection.getListSettingByKategori("jabatan")) {
//                    cbJabatan.getItems().add(jabatan);
//                }
//            } else {
//                System.out.println("cbJabatan belum terinisialisasi.");
//            }
    }

    /*--- Membawa masuk nama dari orang login ---*/
    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<Produk> allProdukList = FXCollections.observableArrayList();

    private void loadDataProduk() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJenisProduk.setCellValueFactory(new PropertyValueFactory<>("jpnama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colHarga.setCellFactory(column -> new TableCell<Produk, Double>() {
            @Override
            protected void updateItem(Double harga, boolean empty) {
                super.updateItem(harga, empty);
                if (empty || harga == null) {
                    setText(null);
                } else {
                    DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("in", "ID"));
                    df.setMaximumFractionDigits(2);
                    df.setMinimumFractionDigits(2);
                    setText(df.format(harga));
                }
                setAlignment(Pos.CENTER_RIGHT);
            }
        });

        colSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));

        colSatuan.setCellFactory(column -> new TableCell<Produk, String>() {
            @Override
            protected void updateItem(String satuan, boolean empty) {
                super.updateItem(satuan, empty);
                if (empty || satuan == null) {
                    setText(null);
                } else {
                    setText(satuan);
                }
                setAlignment(Pos.CENTER_LEFT); // atau Pos.CENTER_LEFT
            }
        });

        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        colStok.setCellFactory(column -> new TableCell<Produk, Integer>() {
            @Override
            protected void updateItem(Integer stok, boolean empty) {
                super.updateItem(stok, empty);
                if (empty || stok == null) {
                    setText(null);
                } else {
                    setText(stok.toString());
                }

                setAlignment(Pos.CENTER_RIGHT); // Rata kanan
            }
        });

        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

        colDeskripsi.setCellFactory(column -> new TableCell<Produk, String>() {
            @Override
            protected void updateItem(String satuan, boolean empty) {
                super.updateItem(satuan, empty);
                if (empty || satuan == null) {
                    setText(null);
                } else {
                    setText(satuan);
                }
                setAlignment(Pos.CENTER_LEFT); // atau Pos.CENTER_LEFT
            }
        });

        List<Produk> list = connection.getListProduk(null, 1, "p_nama", "ASC");
        allProdukList.setAll(list);

        int pageCount = (int) Math.ceil(allProdukList.size() * 1.0 / ROWS_PER_PAGE);
        pgnProduk.setPageCount(pageCount);
        pgnProduk.setCurrentPageIndex(0);
        pgnProduk.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allProdukList.size());

        ObservableList<Produk> currentPageData =
                FXCollections.observableArrayList(allProdukList.subList(fromIndex, toIndex));

        tbProduk.setItems(currentPageData);
        tbProduk.refresh();
        return new StackPane();
    }

    /*--- Menambahkan tombol berupa ikon pada tabel ---*/
    private void addButtonToTable() {
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button();
            private final Button btnDelete = new Button();
            private final Button btnGambar = new  Button();
            private final Button btnRestore = new Button();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 20, 0));

                if (OrderStatus == 1) {
                    // load ikon gambar
                    ImageView iconGambar = new ImageView(getClass().getResource("/Pict/Icons/ikon_gambar.png").toExternalForm());
                    iconGambar.setFitHeight(30);
                    iconGambar.setFitWidth(30);
                    btnGambar.setGraphic(iconGambar);
                    btnGambar.setStyle("-fx-background-color: transparent;");

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
                    btnGambar.setOnAction(e -> {
                        Produk data = getTableView().getItems().get(getIndex());
                        String gambar = data.getGambar();
                        File fileGambar = new File("src/main/resources/Pict/produk/" + gambar);

                        if (!fileGambar.exists()) {
                            messageBox.alertError("Gambar tidak ditemukan!");
                            return;
                        }

                        Stage stage = new Stage();

                        // Header kustom dengan tombol close
                        Button btnClose = new Button("X");
                        btnClose.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
                        btnClose.setOnAction(ev -> stage.close());

                        HBox header = new HBox(btnClose);
                        header.setAlignment(Pos.TOP_RIGHT);
                        header.setPadding(new Insets(10, 10, 0, 10));
                        header.setStyle("-fx-background-color: #14532d;");

                        // Isi gambar dan label
//                    Label lblNamaFile = new Label("Nama File: " + gambar);
//                    lblNamaFile.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

                        ImageView imgView = new ImageView(new Image(fileGambar.toURI().toString()));
                        imgView.setFitWidth(600);
                        imgView.setPreserveRatio(true);
                        imgView.setSmooth(true);

//                    VBox content = new VBox(10, lblNamaFile, imgView);
                        VBox content = new VBox(10, imgView);
                        content.setAlignment(Pos.CENTER);
                        content.setPadding(new Insets(0, 0, 20, 0));

                        VBox root = new VBox(header, content);
                        root.setStyle("-fx-background-color: #14532d;");

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(main.thefreshchoice.MainApplication.primaryStage);
                        stage.show();
                    });

                    btnEdit.setOnAction(e -> {
                        Produk data = getTableView().getItems().get(getIndex());
                        /*--- Memasukkan data dari tabel ke form di sebelah kanan ---*/
                        p_id = data.getId();
                        txtNama.setText(data.getNama());
//                    txtHarga.setText(data.getHarga().toString());
                        txtHarga.setText(String.valueOf(data.getHarga().intValue()));
                        txtSatuan.setText(data.getSatuan());
                        txtStock.setText(data.getStok().toString());
                        txtDeskripsi.setText(data.getDeskripsi());
                        File imgFile = new File("src/main/resources/Pict/produk/" + data.getGambar());
                        if (imgFile.exists()) {
                            imageViewProduk.setImage(new Image(imgFile.toURI().toString()));
                        } else {
                            imageViewProduk.setImage(new Image(getClass().getResource("/Pict/Icons/upload_produk.jpg").toExternalForm()));
                        }

                        namaFileGambar = data.getGambar();
//                    imageViewProduk.setImage(new Image((getClass().getResource(data.getGambar()).toExternalForm())));
                        for (JenisProduk item : cbJenisProduk.getItems()) {
                            if (item.getJp_nama().equals(data.getJpnama())) {
                                cbJenisProduk.setValue(item);
                                break;
                            }
                        }
                    });

                    btnDelete.setOnAction(e -> {
                        Produk data = getTableView().getItems().get(getIndex());
                        p_id = data.getId();

                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Konfirmasi Penghapusan");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Apakah Anda yakin ingin menghapus data ini?");
                        confirm.initOwner(main.thefreshchoice.MainApplication.primaryStage);

                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                connection.deleteProduk(p_id, userAccess);
                                loadDataProduk();
                                messageBox.alertInfo("Data berhasil dihapus");
                            }
                        });
                    });

                    hBox.getChildren().addAll(btnGambar, btnEdit, btnDelete);
                }
                else if (OrderStatus == 0){
                    // Load ikon restore
                    ImageView iconRestore = new ImageView(getClass().getResource("/Pict/Icons/restore-icon.png").toExternalForm());
                    iconRestore.setFitHeight(30);
                    iconRestore.setFitWidth(30);
                    btnRestore.setGraphic(iconRestore);
                    btnRestore.setStyle("-fx-background-color: transparent;");

                    btnRestore.setOnAction(e -> {
                        if (messageBox.alertConfirm("Apakah anda yakin ingin mengaktifkan kembali data?")) {
                            Produk data = getTableView().getItems().get(getIndex());
                            p_id = data.getId();
                            connection.deleteProduk(p_id, userAccess);
                            OrderStatus = 1;
                            loadDataProduk();
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
        colJenisProduk.setCellValueFactory(new PropertyValueFactory<>("jpnama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colHarga.setCellFactory(column -> new TableCell<Produk, Double>() {
            @Override
            protected void updateItem(Double harga, boolean empty) {
                super.updateItem(harga, empty);
                if (empty || harga == null) {
                    setText(null);
                } else {
                    DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("in", "ID"));
                    df.setMaximumFractionDigits(2);
                    df.setMinimumFractionDigits(2);
                    setText(df.format(harga));
                }
                setAlignment(Pos.CENTER_RIGHT);
            }
        });

        colSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));

        colSatuan.setCellFactory(column -> new TableCell<Produk, String>() {
            @Override
            protected void updateItem(String satuan, boolean empty) {
                super.updateItem(satuan, empty);
                if (empty || satuan == null) {
                    setText(null);
                } else {
                    setText(satuan);
                }
                setAlignment(Pos.CENTER_LEFT); // atau Pos.CENTER_LEFT
            }
        });

        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        colStok.setCellFactory(column -> new TableCell<Produk, Integer>() {
            @Override
            protected void updateItem(Integer stok, boolean empty) {
                super.updateItem(stok, empty);
                if (empty || stok == null) {
                    setText(null);
                } else {
                    setText(stok.toString());
                }

                setAlignment(Pos.CENTER_RIGHT); // Rata kanan
            }
        });

        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

        colDeskripsi.setCellFactory(column -> new TableCell<Produk, String>() {
            @Override
            protected void updateItem(String satuan, boolean empty) {
                super.updateItem(satuan, empty);
                if (empty || satuan == null) {
                    setText(null);
                } else {
                    setText(satuan);
                }
                setAlignment(Pos.CENTER_LEFT); // atau Pos.CENTER_LEFT
            }
        });

        // Default
        String sortOrder = "ASC";
        String sortColumn = "p_nama";

        // Mapping Filter
        if (cbFilter.getValue() != null) {
            sortColumn = switch (cbFilter.getValue()) {
                case "Nama" -> "p_nama";
                case "Jenis Produk" -> "jp_nama";
                case "Harga" -> "p_harga";
                case "Satuan" -> "p_satuan";
                case "Stok" -> "p_stok";
                case "Deskripsi" -> "p_deskripsi";
                default -> "p_nama";
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

        List<Produk> list = connection.getListProduk(txtCari.getText(), OrderStatus, sortColumn, sortOrder);
        allProdukList.setAll(list);

        int pageCount = (int) Math.ceil(allProdukList.size() * 1.0 / ROWS_PER_PAGE);
        pgnProduk.setPageCount(pageCount);
        pgnProduk.setCurrentPageIndex(0);
        pgnProduk.setPageFactory(this::createPage);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onKeyPressTxtNama();
        setupNumericTextFields();
        setupTextLimit();
        setupDeskripsiLimit();
        loadDataProduk();
        addButtonToTable();
        loadComboBoxJenisProduk();
        lockTableViewBehavior(tbProduk);
        cbSort.getItems().addAll("Naik   ⬆", "Turun ⬇");
        cbFilter.getItems().addAll("Nama", "Jenis Produk", "Harga", "Satuan", "Stok", "Deskripsi");
        cbStatus.getItems().addAll("Aktif", "Tidak Aktif");
    }
}
