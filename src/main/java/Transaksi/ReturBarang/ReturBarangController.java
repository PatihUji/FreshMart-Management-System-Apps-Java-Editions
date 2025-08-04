package Transaksi.ReturBarang;

import Helper.MessageBox;
import Transaksi.Penjualan.Penjualan;
import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import master.produk.Produk;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReturBarangController implements Initializable {

    @FXML
    private AnchorPane apTambahProduk;

    @FXML
    private Button btnBatal;

    @FXML
    private Button btnMinQuantity;

    @FXML
    private Button btnPlusQuantity;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnTambahItem;

    @FXML
    private Button btnTambahProduk;

    @FXML
    private ComboBox<Produk> cbProduk;

    @FXML
    private TableColumn<Produk, Void> colAksi;

    @FXML
    private TableColumn <Penjualan, Void> colAksiViewPenjualan;

    @FXML
    private TableColumn<Produk, Void> colAlasan;

    @FXML
    private TableColumn colIdTransaksi;

    @FXML
    private TableColumn colJenisPembelian;

    @FXML
    private TableColumn<Produk, Integer> colJumlah;

    @FXML
    private TableColumn colMetodePembayaran;

    @FXML
    private TableColumn colNamaKasir;

    @FXML
    private TableColumn<Produk, String> colProduk;

    @FXML
    private TableColumn colTotalHarga;

    @FXML
    private Label lblIdPenjualan;

    @FXML
    private Label lblTanggal;

    @FXML
    private Pagination pgnPenjualan;

    @FXML
    private TableView<Penjualan> tbPenjualan;

    @FXML
    private TableView<Produk> tbTambahProduk;

    @FXML
    private TextArea txtAlasan;

    @FXML
    private TextField txtCari;

    @FXML
    private TextField txtKuantitas;

    boolean isApViewTransaksi = false;

    private boolean isFilterApplied = false;

    private boolean errorShown = false;

    //btn riwayat
    @FXML
    public void onClickBtnRiwayat() {
        if(isApViewTransaksi){
            isApViewTransaksi = false;
        }
        else {
            isApViewTransaksi = true;
        }
        apViewTransaksi.setVisible(isApViewTransaksi);
    }

    private String userAccess;

    private String userName;

    public void setUserAccess(String userAccess, String username) {
        this.userAccess = userAccess;
        this.userName = username;
    }

    private boolean isApTambahProduk = false;

    @FXML
    protected void onClickBtnTambahProduk(){
        if(isApTambahProduk){
            isApTambahProduk = false;
        }
        else{
            isApTambahProduk = true;
        }
        apTambahProduk.setVisible(isApTambahProduk);
    }

    @FXML
    void onKeyTypedTxtCari(KeyEvent event) {
        loadDataPenjualan(txtCari.getText());
    }

    @FXML
    void onKeyPressedTxtKuantitas() {
        txtKuantitas.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();

            // Batasi input hanya angka
            if (!character.matches("[0-9]")) {
                event.consume();
                return;
            }

            // Simulasikan input setelah karakter baru dimasukkan
            String currentText = txtKuantitas.getText();
            int caretPosition = txtKuantitas.getCaretPosition();
            StringBuilder newText = new StringBuilder(currentText);
            newText.insert(caretPosition, character);

            String input = newText.toString();

            // Validasi isi input
            try {
                int jumlah = Integer.parseInt(input);

                if (jumlah < 1) {
                    messageBox.alertError("Jumlah minimal item adalah 1!");
                    event.consume();
                    kuantitas = 1;
                    txtKuantitas.setText(String.valueOf(kuantitas));
                }
                stock = connection.getListKuantitasProdukTerjualById(cbProduk.getValue().getId());
                if(jumlah > stock){
                    messageBox.alertError("Jumlah maksimal item adalah "+stock);
                }
            } catch (NumberFormatException e) {
                messageBox.alertError("Input tidak valid!");
                event.consume();
            }
        });
    }

    @FXML
    void onClickBtnMinQuantity(ActionEvent event) {
        try {
            if (txtKuantitas.getText().isEmpty()) {
                kuantitas = 1;
            } else {
                kuantitas = Integer.parseInt(txtKuantitas.getText());
            }

            if (kuantitas > 1) {
                kuantitas--;
                txtKuantitas.setText(String.valueOf(kuantitas));
            }
        } catch (NumberFormatException e) {
            txtKuantitas.setText("1");
        }
//        if(kuantitas > 1) {
//            kuantitas--;
//            txtKuantitas.setText(String.valueOf(kuantitas));
//        }
    }

    @FXML
    void onClickBtnPlusQuantity(ActionEvent event) {
        //BARU
        // Validasi produk dipilih
        if (cbProduk.getValue() == null) {
            messageBox.alertError("Pilih produk terlebih dahulu!");
            return;
        }

        try {
            if (txtKuantitas.getText().isEmpty()) {
                kuantitas = 1;
            } else {
                kuantitas = Integer.parseInt(txtKuantitas.getText());
            }

            stock = connection.getListKuantitasProdukTerjualById(cbProduk.getValue().getId());

        } catch (NumberFormatException e) {
            messageBox.alertError("Format jumlah tidak valid!");
            txtKuantitas.setText("1");
        }
//        if(cbProduk.getValue() != null) {
//            stock = connection.getListStockProdukById(cbProduk.getValue().getId());
//
//            for (Produk p : stock) {
//                if (kuantitas < p.getStok()) {
//                    kuantitas++;
//                    txtKuantitas.setText(String.valueOf(kuantitas));
//                }
//            }
//        }
    }

    @FXML
    protected void onClickBtnTambahItem(){
        //BARU
        // Validasi input
        if (cbProduk.getValue() == null || txtAlasan.getText().trim().isEmpty()) {
            messageBox.alertError("Data tidak boleh kosong!");
            return;
        }

        Produk selected = cbProduk.getValue();
        int jumlah = Integer.parseInt(txtKuantitas.getText());
        String alasan = txtAlasan.getText().trim();

        // Cek duplikat produk
        for (Produk p : tbTambahProduk.getItems()) {
            if (selected.getId() == p.getId()) {
                messageBox.alertError("Produk ini sudah ditambahkan!");
                return;
            }
        }

        Produk newItem = new Produk(jumlah, selected.getId(), selected.getNama(), alasan);
        tbTambahProduk.getItems().add(newItem);

        // PAKAI alertInfo yang sudah ada
        messageBox.alertInfo("Data berhasil ditambahkan!");

        // Reset field
        kuantitas = 1;
        txtKuantitas.setText(String.valueOf(kuantitas));
        cbProduk.setValue(null);
        txtAlasan.clear();
//        Produk newItem = new Produk(Integer.parseInt(txtKuantitas.getText()), cbProduk.getValue().getId(), cbProduk.getValue().getNama(), txtAlasan.getText());
//
//        for(Produk p : tbTambahProduk.getItems()){
//            if (cbProduk.getValue().getId() == p.getId()) {
//                messageBox.alertError("Produk ini sudah ditambahkan");
//                return;
//            }
//        }
//
//        addButtonToTable();
//
//
//        tbTambahProduk.getItems().add(newItem);
//
//        kuantitas = 1;
//        txtKuantitas.setText(String.valueOf(kuantitas));
//        cbProduk.setValue(null);
    }



    @FXML
    void onClickBtnSimpan(ActionEvent event) {
        // Validasi input wajib tidak boleh kosong
        if (idPenjualan == null || tbTambahProduk.getItems().isEmpty()) {
            messageBox.alertError("Silakan pilih transaksi penjualan dan tambahkan minimal 1 produk yang akan diretur.");
            return;
        }

        // Validasi: Apakah transaksi penjualan sudah dipilih?
        if (idPenjualan == null) {
            messageBox.alertError("Silakan pilih transaksi penjualan terlebih dahulu.");
            return;
        }

        // Validasi: Apakah ada item produk yang diretur?
        if (tbTambahProduk.getItems().isEmpty()) {
            messageBox.alertError("Silakan tambahkan minimal 1 produk yang akan diretur.");
            return;
        }

        List<DetailReturBarang> detailReturBarang = new ArrayList<>();
        int totalJumlahRetur = 0;

        // Validasi tiap item
        for (Produk produk : tbTambahProduk.getItems()) {
            if (produk.getStok() <= 0) {
                messageBox.alertError("Jumlah retur untuk produk \"" + produk.getNama() + "\" harus lebih dari 0.");
                return;
            }

            if (produk.getDeskripsi() == null || produk.getDeskripsi().trim().isEmpty()) {
                messageBox.alertError("Alasan retur untuk produk \"" + produk.getNama() + "\" tidak boleh kosong.");
                return;
            }

            DetailReturBarang item = new DetailReturBarang(
                    produk.getId(),
                    produk.getNama(),
                    produk.getStok(),
                    produk.getDeskripsi()
            );
            totalJumlahRetur += produk.getStok();
            detailReturBarang.add(item);
        }

        // Langsung simpan ke database
        connection.insertReturBarang(
                connection.getIdKaryawanByUsername(userName),
                idPenjualan,
                totalJumlahRetur,
                userAccess,
                detailReturBarang
        );

        // Beri notifikasi sukses
        // Beri notifikasi sukses
        messageBox.alertInfo("Data retur berhasil disimpan.");

        // Reset form setelah simpan
        clearForm();

        // Nonaktifkan tombol tambah produk putih
        btnTambahProduk.setDisable(true);

        // Refresh data penjualan
        loadDataPenjualan(null);


//        List<DetailReturBarang> detailReturBarang = new ArrayList<>();
//        int totalJumlahRetur = 0;
//
//        for (Produk produk : tbTambahProduk.getItems()) {
//            DetailReturBarang item = new DetailReturBarang(produk.getId(), produk.getStok(), produk.getDeskripsi());
//            totalJumlahRetur += produk.getStok();
//            detailReturBarang.add(item);
//        }
//
//        connection.insertReturBarang(connection.getIdKaryawanByUsername(userName), idPenjualan, totalJumlahRetur, userAccess, detailReturBarang);
    }

    @FXML
    void onClickBtnBatal(ActionEvent event) {
        clearForm();
        //BARU
        // Nonaktifkan tombol +
        btnTambahProduk.setDisable(true);
    }

    private void loadProdukToComboBox() {
        cbProduk.getItems().clear();

        for (Produk produk : connection.getProdukFromDetailPenjualanById(idPenjualan)) {
            cbProduk.getItems().add(produk);
        }
    }

    private void clearForm() {
        cbProduk.setValue(null);
        lblIdPenjualan.setText(null);
        lblTanggal.setText(LocalDate.now().toString());
        txtAlasan.clear();
        tbTambahProduk.getItems().clear();
        kuantitas = 1;
        txtKuantitas.setText(String.valueOf(kuantitas));
        isApTambahProduk = false;
        apTambahProduk.setVisible(isApTambahProduk);
    }

    private final DBConnect connection = new DBConnect();
    MessageBox messageBox = new  MessageBox();
    int kuantitas;
    Integer idPenjualan;

    Integer stock ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProduk.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colProduk.setStyle("-fx-alignment: CENTER-LEFT;");
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colJumlah.setStyle("-fx-alignment: CENTER-RIGHT;");
        colAlasan.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colAlasan.setStyle("-fx-alignment: CENTER-LEFT;");

        lblTanggal.setText(LocalDate.now().toString());
        kuantitas = 1;
        txtKuantitas.setText(String.valueOf(kuantitas));

        txtKuantitas.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();

            if (!character.matches("[0-9]")) {
                event.consume();
                return;
            }

            String currentText = txtKuantitas.getText();
            int caretPosition = txtKuantitas.getCaretPosition();
            StringBuilder newText = new StringBuilder(currentText);
            newText.insert(caretPosition, character);

            try {
                int jumlah = Integer.parseInt(newText.toString());

                if (jumlah < 1) {
                    if (!errorShown) {
                        messageBox.alertError("Jumlah minimal item adalah 1!");
                        errorShown = true;
                    }
                    txtKuantitas.setText("1");
                    event.consume();
                    return;
                }

                Produk selected = cbProduk.getValue();
                if (selected != null) {
                    stock = connection.getListKuantitasProdukTerjualById(selected.getId());

                }

                errorShown = false;

            } catch (NumberFormatException e) {
                if (!errorShown) {
                    messageBox.alertError("Input tidak valid!");
                    errorShown = true;
                }
                txtKuantitas.setText("1");
                event.consume();
            }
        });

        btnTambahProduk.setDisable(true); // Default disable
        apViewTransaksi.setVisible(false); // Tampilkan view utama saat awal di non-aktifkan

        loadDataPenjualan(null);
        loadDataRetur(null);

        addButtonToTable();
        addButtonToRiwayatTable();
        lockTableViewBehavior(tbPenjualan);
        lockTableViewBehavior(tbTambahProduk);
        lockTableViewBehavior(tbReturBarang);

        cbProduk.setOnAction(event -> {
            if (cbProduk.getValue() != null) {
                kuantitas = connection.getListKuantitasProdukTerjualById(cbProduk.getValue().getId());
                txtKuantitas.setText(String.valueOf(kuantitas));
            }
        });

        txtKuantitas.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();

            if (!character.matches("[0-9]")) {
                event.consume();
                return;
            }

            String currentText = txtKuantitas.getText();
            int caretPosition = txtKuantitas.getCaretPosition();
            StringBuilder newText = new StringBuilder(currentText);
            newText.insert(caretPosition, character);

            stock = connection.getListKuantitasProdukTerjualById(cbProduk.getValue().getId());

            try {
                int jumlah = Integer.parseInt(newText.toString());

                if (jumlah < 1) {
                    if (!errorShown) {
                        messageBox.alertError("Jumlah minimal item adalah 1!");
                        errorShown = true;
                    }
                    txtKuantitas.setText(stock.toString());
                    event.consume();
                    return;
                }



                errorShown = false; // reset jika input valid

            } catch (NumberFormatException e) {
                if (!errorShown) {
                    messageBox.alertError("Input tidak valid!");
                    errorShown = true;
                }
                txtKuantitas.setText("1");
                event.consume();
            }
        });

    }

    /*--- Table View Penjualan ---*/
    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<Penjualan> allPenjualanList = FXCollections.observableArrayList();

    private void loadDataPenjualan(String search) {
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("pnj_id"));
        colIdTransaksi.setStyle("-fx-alignment: CENTER-LEFT;");
        colJenisPembelian.setCellValueFactory(new PropertyValueFactory<>("s_nama"));
        colJenisPembelian.setStyle("-fx-alignment: CENTER-LEFT;");
        colMetodePembayaran.setCellValueFactory(new PropertyValueFactory<>("mpb_nama"));
        colMetodePembayaran.setStyle("-fx-alignment: CENTER-LEFT;");
        colNamaKasir.setCellValueFactory(new PropertyValueFactory<>("kry_nama"));
        colNamaKasir.setStyle("-fx-alignment: CENTER-LEFT;");
        colTotalHarga.setCellValueFactory(new PropertyValueFactory<>("pnj_total_harga"));
        colTotalHarga.setCellFactory(column -> new TableCell<Penjualan, Number>() {
            private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(rupiahFormat.format(item));
                    setAlignment(Pos.CENTER_RIGHT);
                }
            }
        });

        List<Penjualan> list = connection.getListPenjualanByCategory(search, 1, "Pengiriman");
        allPenjualanList.setAll(list);

        int pageCount = (int) Math.ceil(allPenjualanList.size() * 1.0 / ROWS_PER_PAGE);
        pgnPenjualan.setPageCount(pageCount);
        pgnPenjualan.setCurrentPageIndex(0);
        pgnPenjualan.setPageFactory(this::createPage);
    }

    /*--- Paging sehingga tabel tidak bisa discroll ---*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allPenjualanList.size());

        ObservableList<Penjualan> currentPageData = FXCollections.observableArrayList(allPenjualanList.subList(fromIndex, toIndex));

        tbPenjualan.setItems(currentPageData);
        tbPenjualan.refresh();
        return new StackPane();
    }

    private void addButtonToTable() {
        colAksiViewPenjualan.setCellFactory(param -> new TableCell<>() {
            private final Button btnPlus = new Button();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 10, 15));

                //IKON +
                ImageView iconPlus = new ImageView(getClass().getResource("/Pict/Icons/plus-icon.png").toExternalForm());
                iconPlus.setFitHeight(30);
                iconPlus.setFitWidth(30);
                btnPlus.setGraphic(iconPlus);
                btnPlus.setStyle("-fx-background-color: transparent;");


                // Event handler
                //BARU
                btnPlus.setOnAction(e -> {
                    Penjualan data = getTableView().getItems().get(getIndex());
                    idPenjualan = data.getPnj_id();
                    lblIdPenjualan.setText(idPenjualan.toString());
                    loadProdukToComboBox();
                    btnTambahProduk.setDisable(false); // Aktifkan tombol putih +
                });

//                btnPlus.setOnAction(e -> {
//                    Penjualan data = getTableView().getItems().get(getIndex());
//                    idPenjualan = data.getPnj_id();
//                    lblIdPenjualan.setText(idPenjualan.toString());
//                    loadProdukToComboBox();
//                });

                hBox.getChildren().addAll(btnPlus);
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

        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 10, 15));

                ImageView iconDelete = new ImageView(getClass().getResource("/Pict/Icons/delete-icon.png").toExternalForm());
                iconDelete.setFitHeight(30);
                iconDelete.setFitWidth(30);
                btnDelete.setGraphic(iconDelete);
                btnDelete.setStyle("-fx-background-color: transparent;");

                // Event handler
                //BARU
                btnDelete.setOnAction(e -> {
                    Produk produk = getTableView().getItems().get(getIndex());
                    boolean confirmed = messageBox.alertConfirm("Apakah Anda yakin ingin menghapus item ini?");
                    if (confirmed) {
                        getTableView().getItems().remove(produk);
                    }
                });

                hBox.getChildren().addAll(btnDelete);
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

    private void addButtonToRiwayatTable() {
        colAksi1.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetail = new Button();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 0, 10, 15));

                ImageView iconDetail = new ImageView(getClass().getResource("/Pict/Icons/ikon_gambar.png").toExternalForm());
                iconDetail.setFitHeight(30);
                iconDetail.setFitWidth(30);
                btnDetail.setGraphic(iconDetail);
                btnDetail.setStyle("-fx-background-color: transparent;");

                btnDetail.setOnAction(e -> {
                    ReturBarang data = (ReturBarang) getTableView().getItems().get(getIndex());

                    // 1. Set label info
                    lblNamaCs.setText(data.getNamaKasir());
                    lblTgl.setText(data.getTanggalRetur().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    lbljmlh.setText(String.valueOf(data.getJumlah()));

                    // 2. Load detail retur
                    loadDataDetailReturBarang(data.getRtrId());

                    // 3. Tampilkan panel detail
                    apDetailTransaksi.setVisible(true);
                });

                hBox.getChildren().addAll(btnDetail);
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


    /*--- View Data Transaksi ---*/
    @FXML
    private AnchorPane apInputTransaksi;

    @FXML
    private AnchorPane apViewTransaksi;

    @FXML
    private Button btnRiwayat;

    @FXML
    private ComboBox<String> cbUrutan;

    @FXML
    private TableColumn<ReturBarang, Void> colAksi1;

    @FXML
    private TableColumn colIdRetur;

    @FXML
    private TableColumn colTglRetur;

    @FXML
    private TableColumn colJumlahh;

    @FXML
    private TableView<ReturBarang> tbReturBarang;

    @FXML
    private TableView<ReturBarang> tableView;

    @FXML
    private TableColumn colNmKasir;

    @FXML
    private TextField txtCariRiwayat;

    @FXML
    private Pagination pgnRetur;

    @FXML
    private Button btnKembali;

    @FXML
    protected void onClickBtnKembali(ActionEvent event) {
        if(isApViewTransaksi){
            isApViewTransaksi = false;
        }
        else {
            isApViewTransaksi = true;
        }
        apViewTransaksi.setVisible(isApViewTransaksi);
    }


    @FXML
    protected void onTypedTxtCariRiwayat() {
        loadDataRetur(txtCariRiwayat.getText());
    }

    /*--- Menampilkan data pada tabel riwayat retur ---*/
    // Global, bisa dipakai semua method
    private ObservableList<ReturBarang> allReturList = FXCollections.observableArrayList();


    private void loadDataRetur(String search) {
        colIdRetur.setCellValueFactory(new PropertyValueFactory<>("rtrId"));
        colIdRetur.setStyle("-fx-alignment: CENTER-LEFT;");

        colNmKasir.setCellValueFactory(new PropertyValueFactory<>("namaKasir"));
        colNmKasir.setStyle("-fx-alignment: CENTER-LEFT;");

        colTglRetur.setCellValueFactory(new PropertyValueFactory<>("tanggalRetur"));
        colTglRetur.setCellFactory(column -> new TableCell<ReturBarang, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : formatter.format(item));
                setAlignment(Pos.CENTER);
            }
        });


        colJumlahh.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colJumlahh.setStyle("-fx-alignment: CENTER;");

        List<ReturBarang> list = connection.getListReturBarang(search);
        allReturList.setAll(list);

        int pageCount = (int) Math.ceil(allReturList.size() * 1.0 / ROWS_PER_PAGE);
        pgnRetur.setPageCount(pageCount);
        pgnRetur.setCurrentPageIndex(0);
        pgnRetur.setPageFactory(this::createPageRiwayat);
    }

    private Node createPageRiwayat(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allReturList.size());

        ObservableList<ReturBarang> currentPageData = FXCollections.observableArrayList(
                allReturList.subList(fromIndex, toIndex)
        );

        tbReturBarang.setItems(currentPageData);
        tbReturBarang.refresh();

        return new StackPane();
    }

    // detail transaksi
    /*--- Detail Transaksi ---*/
    @FXML
    private AnchorPane apDetailTransaksi;

    @FXML
    private TableView<DetailReturBarang> tbDetailTransaksi;

    @FXML
    private TableColumn colProdukDtl;

    @FXML
    private TableColumn colJumlahhhh;

    @FXML
    private TableColumn colAlasann;
    @FXML
    private Label lblNamaCs;

    @FXML
    private Label lblTgl;

    @FXML
    private Label lblTotalPembelianDtl;

    @FXML
    private Label lbljmlh;

    private void loadDataDetailReturBarang(int idRetur) {
        colProdukDtl.setCellValueFactory(new PropertyValueFactory<>("p_nama")); // nanti diganti ke p_nama jika kamu tambahkan di model
        colProdukDtl.setStyle("-fx-alignment: CENTER-LEFT;");

        colJumlahhhh.setCellValueFactory(new PropertyValueFactory<>("drp_kuantitas"));
        colJumlahhhh.setStyle("-fx-alignment: CENTER-RIGHT;");

        colAlasann.setCellValueFactory(new PropertyValueFactory<>("drp_alasan"));
        colAlasann.setStyle("-fx-alignment: CENTER-LEFT;");

        // Ambil data dari DB
        List<DetailReturBarang> list = connection.getListDetailReturBarang(idRetur);
        ObservableList<DetailReturBarang> observableList = FXCollections.observableArrayList(list);
        tbDetailTransaksi.setItems(observableList);

        lockTableViewBehavior(tbDetailTransaksi); // pastikan method ini ada, misalnya untuk disable resize/sort
    }


    @FXML
    protected void onClickApDetailTransaksi(){
        apDetailTransaksi.setVisible(false);
    }

    private void lockTableViewBehavior(TableView<?> tableView) {
        // Nonaktifkan seleksi baris
        tableView.setSelectionModel(null);

        // Nonaktifkan fokus keyboard
        tableView.setFocusTraversable(false);

    }
}
