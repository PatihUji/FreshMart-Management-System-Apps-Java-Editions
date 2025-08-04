package Transaksi.Pengiriman;

import Helper.MessageBox;
import Transaksi.Penjualan.DetailPenjualan;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import master.karyawan.karyawan;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

//import static master.produk.ProdukController.ROWS_PER_PAGE;

public class PengirimanController implements Initializable {
    @FXML
    private TextField tbid;

    @FXML
    private TextField tbtotal;

    @FXML
    private TextField tbnama;

    @FXML
    private AnchorPane paneKanan;

    @FXML
    private ComboBox<karyawan> cbkry;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private TextArea tbalamat;

    @FXML
    private DatePicker tbtgl;

    @FXML
    private TextField tbjam;

    @FXML
    private TextField tbmenit;

    @FXML
    private TableColumn <Pengiriman, Void> colAksi;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn tgltrs;

    @FXML
    private TableColumn colHarga;

    @FXML
    private TableColumn colNama;

    @FXML
    private Pagination pgnPengiriman;

    @FXML
    private TableView<Pengiriman> tbPengiriman;

    @FXML
    private TableView<Pengiriman> tbPengirimanSelesai;

    @FXML
    private TableColumn colIdpenjualan;

    @FXML
    private TableColumn colNamaDriver;

    @FXML
    private TableColumn colTanggal;

    @FXML
    private TableColumn colJam;

    @FXML
    private TableColumn colAlamat;

    @FXML
    private TableColumn <Pengiriman, Void> colAksiPengiriman;

    int OrderStatus = 0;

    private String userAccess;

    public void setUserAccess(String userAccess) {
        this.userAccess = userAccess;
    }

    DBConnect connection = new DBConnect();

    MessageBox messageBox = new MessageBox();

    private int p_id;

    /*--- Menambahkan tombol berupa ikon pada tabel ---*/
    private void addButtonToTable() {
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnAdd = new Button();
            private final Button btnDone = new Button();
            private final Button btnGambar = new  Button();
            private final Button btnDetail = new Button();
            private final HBox hBox = new HBox(10);
            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 20, 0));

                if (OrderStatus == 0) {
                    // load ikon detail
//                    ImageView iconGambar = new ImageView(getClass().getResource("/Pict/Icons/ikon_gambar.png").toExternalForm());
//                    iconGambar.setFitHeight(30);
//                    iconGambar.setFitWidth(30);
//                    btnDetail.setGraphic(iconGambar);
//                    btnDetail.setStyle("-fx-background-color: transparent;");

                    // Load ikon tambah
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/plus-icon.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnAdd.setGraphic(iconEdit);
                    btnAdd.setStyle("-fx-background-color: transparent;");

                    btnAdd.setOnAction(e -> {
                        Pengiriman data = getTableView().getItems().get(getIndex());
                        /*--- Memasukkan data dari tabel ke form di sebelah kanan ---*/
                        tbid.setText(String.valueOf(data.getPnj_id().intValue()));

                        tbtotal.setText(String.valueOf(data.getPnj_total_harga().intValue()));
                    });
                    hBox.getChildren().addAll(btnAdd);
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
        colAksiPengiriman.setCellFactory(param -> new TableCell<>() {
            private final Button btnAdd = new Button();
            private final Button btnDone = new Button();
            private final Button btnDetail = new Button();
            private final HBox hBox = new HBox(10);
            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 20, 0));

                if (OrderStatus == 1){
//                    ImageView iconDetail = new ImageView(getClass().getResource("/Pict/Icons/ikon_gambar.png").toExternalForm());
//                    iconDetail.setFitHeight(30);
//                    iconDetail.setFitWidth(30);
//                    btnDetail.setGraphic(iconDetail);
//                    btnDetail.setStyle("-fx-background-color: transparent;");
//
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/done-icon.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnDone.setGraphic(iconEdit);
                    btnDone.setStyle("-fx-background-color: transparent;");

                    btnDone.setOnAction(e -> {
                        Pengiriman data = getTableView().getItems().get(getIndex());

                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Konfirmasi Selesaikan Pengiriman");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Apakah Anda yakin ingin menyelesaikan pesanan ini?");
                        confirm.initOwner(main.thefreshchoice.MainApplication.primaryStage);

                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                // Gunakan SP baru
                                connection.selesaiPengiriman(data.getPnj_id(), userAccess);
                                // Refresh data
                                loadDataPengirimanSelesai(1); // atau loadDataPengirimanSelesai(1) sesuai kebutuhan
                                messageBox.alertInfo("Pengiriman berhasil diselesaikan.");
                                loadDataPengirimanSelesai(1);
                            }
                        });
                    });
//                    hBox.getChildren().addAll(btnDetail, btnDone);
                    hBox.getChildren().addAll(btnDone);
                }
                else if (OrderStatus == 2){
                    ImageView iconEdit = new ImageView(getClass().getResource("/Pict/Icons/ikon_gambar.png").toExternalForm());
                    iconEdit.setFitHeight(30);
                    iconEdit.setFitWidth(30);
                    btnDetail.setGraphic(iconEdit);
                    btnDetail.setStyle("-fx-background-color: transparent;");

                    btnDetail.setOnAction(e -> {
                       Pengiriman data = getTableView().getItems().get(getIndex());
                       lblNamaKurirDtl.setText(data.getKry_nama());
                       lblNamaPenerimaDtl.setText(data.getPng_nama());
                       lblTanggalDikirimDtl.setText(data.getPng_tanggal().toString());
                       lblAlamatDtl.setText(data.getPng_alamat());

                       loadDataDetailTransaksi(data.getPnj_id());

                        apDetailTransaksi.setVisible(true);
                    });

                    hBox.getChildren().addAll(btnDetail);
//                    loadDataPengirimanSelesai(2);
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

    private void loadComboBoxDriver() {
    cbkry.getItems().clear(); // cbDriver = ComboBox<Karyawan>

    for (karyawan driver : connection.getListDriver()) {
        cbkry.getItems().add(driver);
    }

    // Supaya tampilannya hanya nama driver
    cbkry.setConverter(new StringConverter<karyawan>() {
        @Override
        public String toString(karyawan object) {
            return object != null ? object.getNama() : "";
        }

        @Override
        public karyawan fromString(String string) {
            return cbkry.getItems().stream()
                    .filter(k -> k.getNama().equals(string))
                    .findFirst()
                    .orElse(null);
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
        tbtotal.setTextFormatter(hargaFormatter);

// Tambahkan listener agar diformat setelah pengguna mengetik
        tbtotal.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;

            String cleaned = newValue.replaceAll("\\.", "");
            try {
                long value = Long.parseLong(cleaned);
                tbtotal.setText(currencyFormat.format(value));
            } catch (NumberFormatException e) {
                tbtotal.setText(oldValue);
            }
        });
    }

    private void setupTimeFields() {
        // Batasi input hanya angka maksimal 2 digit
        UnaryOperator<TextFormatter.Change> timeFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d{0,2}") ? change : null;
        };

        // Formatter untuk jam
        TextFormatter<String> jamFormatter = new TextFormatter<>(timeFilter);
        tbjam.setTextFormatter(jamFormatter);

        tbjam.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;

            try {
                int jam = Integer.parseInt(newVal);
                if (jam > 23) {
                    tbjam.setText("23");
                }
            } catch (NumberFormatException e) {
                tbjam.setText("0");
            }
        });

        // Formatter untuk menit
        TextFormatter<String> menitFormatter = new TextFormatter<>(timeFilter);
        tbmenit.setTextFormatter(menitFormatter);

        tbmenit.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;

            try {
                int menit = Integer.parseInt(newVal);
                if (menit > 59) {
                    tbmenit.setText("59");
                }
            } catch (NumberFormatException e) {
                tbmenit.setText("0");
            }
        });
    }

    private void setupNamaPenerima() {
        UnaryOperator<TextFormatter.Change> hurufOnlyFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]{0,50}")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> namaFormatter = new TextFormatter<>(hurufOnlyFilter);
        tbnama.setTextFormatter(namaFormatter);
    }

    private void setupJamDanMenit() {
        UnaryOperator<TextFormatter.Change> jamFilter = change -> {
            String newJam = change.getControlNewText();
            if (!newJam.matches("\\d{0,2}")) return null;

            try {
                if (!newJam.isEmpty()) {
                    int jam = Integer.parseInt(newJam);
                    if (jam > 23) return null;

                    String menitStr = tbmenit.getText();
                    if (!menitStr.isEmpty() && menitStr.length() == 2 && newJam.length() == 2) {
                        int menit = Integer.parseInt(menitStr);
                        LocalTime input = LocalTime.of(jam, menit);
                        if (input.isBefore(LocalTime.now())) return null;
                    }
                }
                return change;
            } catch (Exception e) {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> menitFilter = change -> {
            String newMenit = change.getControlNewText();
            if (!newMenit.matches("\\d{0,2}")) return null;

            try {
                if (!newMenit.isEmpty()) {
                    int menit = Integer.parseInt(newMenit);
                    if (menit > 59) return null;

                    String jamStr = tbjam.getText();
                    if (!jamStr.isEmpty() && jamStr.length() == 2 && newMenit.length() == 2) {
                        int jam = Integer.parseInt(jamStr);
                        LocalTime input = LocalTime.of(jam, menit);
                        if (input.isBefore(LocalTime.now())) return null;
                    }
                }
                return change;
            } catch (Exception e) {
                return null;
            }
        };

        tbjam.setTextFormatter(new TextFormatter<>(jamFilter));
        tbmenit.setTextFormatter(new TextFormatter<>(menitFilter));
    }

    private void setupAlamatPengiriman() {
        UnaryOperator<TextFormatter.Change> alamatFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 100) {
                return change;
            }
            return null;
        };

        TextFormatter<String> alamatFormatter = new TextFormatter<>(alamatFilter);
        tbalamat.setTextFormatter(alamatFormatter);
    }

    @FXML
    private AnchorPane apSortFilter;

    private boolean isSortFilter = false;

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

    private ObservableList<Pengiriman> allPengirimanList = FXCollections.observableArrayList();

    private static final int ROWS_PER_PAGE = 8;

    private void loadDataPengiriman() {
        colId.setCellValueFactory(new PropertyValueFactory<>("pnj_id"));
        colId.setStyle("-fx-alignment: CENTER-LEFT;");
        tgltrs.setCellValueFactory(new PropertyValueFactory<>("pnj_created_date"));

        colNama.setCellValueFactory(new PropertyValueFactory<>("pnj_created_by"));
        colNama.setStyle("-fx-alignment: CENTER-LEFT;");

        colHarga.setCellValueFactory(new PropertyValueFactory<>("pnj_total_harga"));
        colHarga.setCellFactory(column -> new TableCell<Pengiriman, Double>() {
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

        List<Pengiriman> list = connection.getListPenjualanToPengiriman(0);
        allPengirimanList.setAll(list);

        int pageCount = (int) Math.ceil(allPengirimanList.size() * 1.0 / ROWS_PER_PAGE);
        pgnPengiriman.setPageCount(pageCount);
        pgnPengiriman.setCurrentPageIndex(0);
        pgnPengiriman.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allPengirimanList.size());

        ObservableList<Pengiriman> currentPageData =
                FXCollections.observableArrayList(allPengirimanList.subList(fromIndex, toIndex));

        tbPengiriman.setItems(currentPageData);
        tbPengiriman.refresh();
        return new StackPane();
    }

    @FXML
    protected void onClickBtnSimpan(){
        if (tbalamat.getText().isEmpty()
                || cbkry.getSelectionModel().getSelectedItem() == null
                || tbtgl.getValue() == null
                || tbjam.getText().isEmpty()
                || tbmenit.getText().isEmpty()
                || tbnama.getText().isEmpty()) {
            messageBox.alertWarning("Semua data bertanda * wajib diisi!");
            return;
        }

        Integer pnj_id = Integer.parseInt(tbid.getText());
        String nama = tbnama.getText();
        String alamat = tbalamat.getText();
        String hargaStr = tbtotal.getText().replace(".","");

        if (tbid.getText().isEmpty() || tbnama.getText().isEmpty() || tbtotal.getText().isEmpty() || tbalamat.getText().isEmpty() || cbkry.getSelectionModel().getSelectedItem() == null || tbjam.getText().isEmpty() || tbmenit.getText().isEmpty()) {
            messageBox.alertWarning("Data tidak boleh kosong");
            return;
        }

        double harga = Double.parseDouble(hargaStr);

        if (harga <= 0) {
            messageBox.alertWarning("Harga tidak boleh 0 atau negatif!");
            return;
        }

        int jam = Integer.parseInt(tbjam.getText());
        int menit = Integer.parseInt(tbmenit.getText());

        Time sqlTime = Time.valueOf(String.format("%02d:%02d:00", jam, menit));

        karyawan kry = (karyawan) cbkry.getValue();

        connection.updatePengiriman(pnj_id, kry.getId(), tbalamat.getText(), Date.valueOf(tbtgl.getValue()),
                sqlTime, 1, tbnama.getText(), userAccess);
        messageBox.alertInfo("Data berhasil diperbarui");
        clear();
        loadComboBoxDriver();
        loadDataPengiriman();
    }

    @FXML
    protected void onClickBtnBatal(){
        clear();
    }

    protected void clear(){
        cbkry.setValue(null);
        tbid.setText("");
        tbnama.setText("");
        tbalamat.setText("");
        tbtotal.setText("");
        tbjam.setText("");
        tbmenit.setText("");
    }

    @FXML
    protected void onClickBtnClear(){
        loadComboBoxDriver();
        tbPengiriman.setVisible(true);
        paneKanan.setVisible(true);
        tbPengirimanSelesai.setVisible(false);
        OrderStatus = 0;
        cbStatus.setValue(null);
        loadDataPengiriman();
//        OrderStatus = 0;
//        loadFilteredData();
    }

    /*--- Membuat tabel tidak bisa diubah ---*/
    private void lockTableViewBehavior(TableView<?> tableView) {
        // Nonaktifkan seleksi baris
        tableView.setSelectionModel(null);

        // Nonaktifkan resize kolom & sort
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setResizable(true);
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

    private void loadDataPengirimanSelesai(int status) {
        colIdpenjualan.setCellValueFactory(new PropertyValueFactory<>("pnj_id"));
        colNamaDriver.setCellValueFactory(new PropertyValueFactory<>("kry_nama"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("png_tanggal"));
        colJam.setCellValueFactory(new PropertyValueFactory<>("png_jam"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("png_alamat"));

        colNamaDriver.setStyle("-fx-alignment: CENTER-LEFT;");
        colTanggal.setStyle("-fx-alignment: CENTER-LEFT;");
        colIdpenjualan.setStyle("-fx-alignment: CENTER-LEFT;");
        colJam.setStyle("-fx-alignment: CENTER-LEFT;");
        colAlamat.setStyle("-fx-alignment: CENTER-LEFT;");

        colTanggal.setCellFactory(column -> new TableCell<Pengiriman, Date>() {
            @Override
            protected void updateItem(Date tanggal, boolean empty) {
                super.updateItem(tanggal, empty);
                setText(empty || tanggal == null ? null : tanggal.toString());
            }
        });
        colJam.setCellFactory(column -> new TableCell<Pengiriman, Time>() {
            @Override
            protected void updateItem(Time jam, boolean empty) {
                super.updateItem(jam, empty);
                if (empty || jam == null) {
                    setText(null);
                } else {
                    setText(jam.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        });

        List<Pengiriman> list = connection.getListPengirimanByStatus(status);
        tbPengirimanSelesai.setItems(FXCollections.observableArrayList(list));
        allPengirimanList.setAll(list);


        int pageCount = (int) Math.ceil(allPengirimanList.size() * 1.0 / ROWS_PER_PAGE);
        pgnPengiriman.setPageCount(pageCount);
        pgnPengiriman.setCurrentPageIndex(0);
        pgnPengiriman.setPageFactory(this::createPage);

    }


    @FXML
    protected void onClickCbFilter() {
        String selectedFilter = cbStatus.getValue();

        if ("Belum   Dikirim".equalsIgnoreCase(selectedFilter)) {
            loadComboBoxDriver();
            tbPengiriman.setVisible(true);
            paneKanan.setVisible(true);
            tbPengirimanSelesai.setVisible(false);
            OrderStatus = 0;
            loadDataPengiriman(); // Menampilkan status = 0
        }
        else if ("Sedang Dikirim".equalsIgnoreCase(selectedFilter)) {
            tbPengiriman.setVisible(false);
            paneKanan.setVisible(false);
            tbPengirimanSelesai.setVisible(true);
            OrderStatus = 1;
            System.out.println("Jumlah Data : " + allPengirimanList.size());
            loadDataPengirimanSelesai(1); // Ambil status = 1
        }
        else if ("Selesai  Dikirim".equalsIgnoreCase(selectedFilter)) {
            tbPengiriman.setVisible(false);
            paneKanan.setVisible(false);
            tbPengirimanSelesai.setVisible(true);
            OrderStatus = 2;
            System.out.println("Jumlah Data : " + allPengirimanList.size());
            loadDataPengirimanSelesai(2); // Ambil status = 2
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBoxDriver();
        setupNumericTextFields();
        tbtotal.setEditable(false);
        tbid.setEditable(false);
        tbtgl.setDisable(true);
        tbtgl.setStyle("-fx-opacity: 1; -fx-background-color: #f0f0f0;");
//        setupTimeFields();

        cbStatus.getItems().addAll("Belum   Dikirim", "Sedang Dikirim", "Selesai  Dikirim");
        cbStatus.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            onClickCbFilter();
        });
        setupJamDanMenit();
        setupNamaPenerima();
        setupAlamatPengiriman();
        loadDataPengiriman();
        lockTableViewBehavior(tbPengiriman);
        System.out.println("Jumlah Data : " + allPengirimanList.size());
        tbtgl.setValue(LocalDate.now());
        addButtonToTable();
//        cbStatus.getItems().addAll("Belum  Dikirim", "Sedang  Dikirim", "Selesai Dikirim");
    }

    /*--- Detail Transaksi ---*/
    @FXML
    private AnchorPane apDetailTransaksi;

    @FXML
    private TableView<DetailPenjualan> tbDetailTransaksi;

    @FXML
    private TableColumn colJumlahDtl;

    @FXML
    private TableColumn colProdukDtl;

    @FXML
    private Label lblAlamatDtl;

    @FXML
    private Label lblNamaKurirDtl;

    @FXML
    private Label lblNamaPenerimaDtl;

    @FXML
    private Label lblTanggalDikirimDtl;

    private void loadDataDetailTransaksi(int idTransaksi){
        colProdukDtl.setCellValueFactory(new PropertyValueFactory<>("p_nama"));
        colProdukDtl.setStyle("-fx-alignment: CENTER-LEFT;");
        colJumlahDtl.setCellValueFactory(new PropertyValueFactory<>("dp_kuantitas"));
        colJumlahDtl.setStyle("-fx-alignment: CENTER-RIGHT;");

        List<DetailPenjualan> list = connection.getListDetailPenjualan(idTransaksi);
        ObservableList<DetailPenjualan> observableList = FXCollections.observableArrayList(list);
        tbDetailTransaksi.setItems(observableList);

        lockTableViewBehavior(tbDetailTransaksi);
    }

    @FXML
    protected void onClickApDetailTransaksi(){
        apDetailTransaksi.setVisible(false);
    }
}
