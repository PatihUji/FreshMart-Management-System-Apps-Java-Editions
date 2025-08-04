package Transaksi.StokKeluar;

import Helper.MessageBox;
import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import master.produk.Produk;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class StokKeluarController implements Initializable { //polymorphism, karena class mengimplementasikan interface

    @FXML
    private Button btnBatal;

    @FXML
    private Button btnSimpan;

    @FXML
    private ComboBox<Produk> cbProduk;

    @FXML
    private Label dpTanggal;

    @FXML
    private TextField txtCari;

    @FXML
    private TextArea txtKeterangan;

    @FXML
    private ScrollPane spDataField;

    @FXML
    private AnchorPane apTambahProduk;

    @FXML
    private Button btnMinQuantity;

    @FXML
    private Button btnPlusQuantity;

    @FXML
    private TableColumn<Produk, Void> colAksi;

    @FXML
    private TableColumn<Produk, Integer> colJumlah;

    @FXML
    private TableColumn<Produk, String> colProduk;

    @FXML
    private TableView<Produk> tbTambahProduk;

    @FXML
    private TextField txtKuantitas;

    private boolean isApTambahProduk = false;

    @FXML
    protected void onClickBtnTambahProduk(){
        if(isApTambahProduk){
            isApTambahProduk = false;
        }
        else{
            isApTambahProduk = true;
        }
        apTambahProduk.setVisible(isApTambahProduk); //Menampilkan atau menyembunyikan panel apTambahProduk
        // sesuai nilai boolean isApTambahProduk.
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
                stock = connection.getListStockProdukById(cbProduk.getValue().getId());
                for (Produk p : stock){
                    if (jumlah > p.getStok()) {
                        messageBox.alertError("Jumlah item tidak boleh lebih dari stok yang ada!");
                        event.consume();
                        kuantitas = 1;
                        txtKuantitas.setText(String.valueOf(kuantitas));
                    }
                }
            } catch (NumberFormatException e) {
                messageBox.alertError("Input tidak valid!");
                event.consume();
            }
        });
    }

    @FXML
    void onClickBtnMinQuantity(ActionEvent event) {
        if(kuantitas > 1) {
            kuantitas--;
            txtKuantitas.setText(String.valueOf(kuantitas));
        }
    }

    @FXML
    void onClickBtnPlusQuantity() {
        if(cbProduk.getValue() != null) {
            stock = connection.getListStockProdukById(cbProduk.getValue().getId());

            for (Produk p : stock) {
                if (kuantitas < p.getStok()) {
                    kuantitas++;
                    txtKuantitas.setText(String.valueOf(kuantitas));
                }
            }
        }
    }

    @FXML
    protected void onClickBtnTambahItem(){
        // Validasi jika produk belum dipilih
        if (cbProduk.getValue() == null) {
            messageBox.alertError("Silakan pilih produk terlebih dahulu!");
            return;
        }

        Produk newItem = new Produk(Integer.parseInt(txtKuantitas.getText()), cbProduk.getValue().getId(), cbProduk.getValue().getNama());

        for(Produk p : tbTambahProduk.getItems()){
            if (cbProduk.getValue().getId() == p.getId()) {
                messageBox.alertError("Produk ini sudah ditambahkan");
                return;
            }
        }

        addButtonToTableTambahProduk();

        tbTambahProduk.getItems().add(newItem);

        kuantitas = 1;
        txtKuantitas.setText(String.valueOf(kuantitas));
        cbProduk.setValue(null);
    }


    @FXML
    private TableColumn<StokKeluar, Void> colAksi1;
    private void addButtonToTableTambahProduk() {
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnHapus = new Button("Hapus");

            {
                btnHapus.setOnAction(e -> {
                    Produk produk = getTableView().getItems().get(getIndex());
                    boolean confirmed = messageBox.alertConfirm("Apakah Anda yakin ingin menghapus item ini?");
                    if (confirmed) {
                        getTableView().getItems().remove(produk);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnHapus);
            }
        });
    }


    private void addButtonToTableStokKeluar() {
        colAksi1.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetail = new Button();
            private final HBox hBox = new HBox();

            StokKeluar stok = new StokKeluar();
            {
                hBox.setAlignment(Pos.CENTER);
                ImageView iconDetail = new ImageView(getClass().getResource("/Pict/Icons/ikon_gambar.png").toExternalForm());
                iconDetail.setFitHeight(30);
                iconDetail.setFitWidth(30);
                btnDetail.setGraphic(iconDetail);
                btnDetail.setStyle("-fx-background-color: transparent;");
                hBox.getChildren().add(btnDetail);

                btnDetail.setOnAction(e -> {
                    StokKeluar data = getTableView().getItems().get(getIndex());
                    loadDataDetailTransaksiStokKeluar(data.getSk_id());
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }


    private final DBConnect connection = new DBConnect();
    MessageBox messageBox = new  MessageBox();
    int kuantitas;

    List<Produk> stock ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProduk.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("stok"));
        dpTanggal.setText(LocalDate.now().toString());

        loadProdukToComboBox();
        loadCardProduk();

        kuantitas = 1;
        txtKuantitas.setText(String.valueOf(kuantitas));

        StokKeluar stok = new StokKeluar();

        addButtonToTableTambahProduk();   // tombol hapus untuk tabel Produk
        loadDataStokKeluar(null);        // akan load data dan nanti panggil addButtonToTableStokKeluar
        addButtonToTableStokKeluar();
    }


    /*--- View Data Transaksi ---*/
    @FXML private AnchorPane apViewTransaksi;
    @FXML private TableView<StokKeluar> tbStokKeluar;
    @FXML
    private TableColumn<StokKeluar, Integer> colIdTransaksi;

    @FXML
    private TableColumn<StokKeluar, String> colNamaKasir;

    @FXML
    private TableColumn<StokKeluar, Date> colTanggalKeluar;

    @FXML
    private TableColumn<StokKeluar, Integer> colJumlahKeluar;

    @FXML
    private TableColumn<StokKeluar, String> colKeterangan;


    @FXML
    private AnchorPane apInputTransaksi;

    @FXML
    private Button btnRiwayat;

    @FXML
    private ComboBox<String> cbUrutan;

    @FXML
    private TextField txtCariRiwayat;

    @FXML
    private Pagination pgnStokKeluar;

    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<StokKeluar> allStokKeluarList = FXCollections.observableArrayList();

    private void loadDataStokKeluar(String search) {
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("sk_id"));
        colNamaKasir.setCellValueFactory(new PropertyValueFactory<>("kry_nama"));
        colTanggalKeluar.setCellValueFactory(new PropertyValueFactory<>("sk_tanggal_keluar"));
        colJumlahKeluar.setCellValueFactory(new PropertyValueFactory<>("sk_jumlah_keluar"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("sk_keterangan"));

        List<StokKeluar> list = connection.getListStokKeluar(search, null);
            allStokKeluarList.setAll(list);

            int pageCount = (int) Math.ceil(allStokKeluarList.size() * 1.0 / ROWS_PER_PAGE);
            pgnStokKeluar.setPageCount(pageCount);
            pgnStokKeluar.setCurrentPageIndex(0);
            pgnStokKeluar.setPageFactory(this::createPageStokKeluar);

            System.out.println("Total data: " + allStokKeluarList.size());

        colTanggalKeluar.setCellFactory(column -> {
            return new TableCell<StokKeluar, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(new SimpleDateFormat("dd-MM-yyyy").format(item));
                    }
                }
            };
        });
    }



        /*--- Paging ---*/
    private Node createPageStokKeluar(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allStokKeluarList.size());

        ObservableList<StokKeluar> currentPageData = FXCollections.observableArrayList(
                allStokKeluarList.subList(fromIndex, toIndex)
        );

        tbStokKeluar.setItems(currentPageData);
        tbStokKeluar.refresh();
        return new StackPane();
    }


    @FXML
    protected void onClickBtnRiwayat(ActionEvent event) {
        apViewTransaksi.setVisible(true);
        loadDataStokKeluar(null);
    }

    @FXML
    protected void onClickBtnKembali(ActionEvent event) {
        apViewTransaksi.setVisible(false);
    }

    @FXML
    protected void onTypedTxtCariRiwayat() {
        loadDataStokKeluar(txtCariRiwayat.getText());
    }

    @FXML
    private Button btnKembali;
    private void loadProdukToComboBox() {
        cbProduk.getItems().clear();

        for (Produk produk : connection.getListNamaProduk()) {
            cbProduk.getItems().add(produk);
        }
    }

    @FXML
    private void onKeyTypedTxtKeterangan(KeyEvent event) {
        // Cegah input jika panjang teks sudah mencapai 50 karakter
        if (txtKeterangan.getText().length() >= 50) {
            event.consume();
            messageBox.alertError("Keterangan maksimal 50 karakter!");
        }
    }

    private void loadCardProduk() {
        List<Produk> listProduk = connection.getListProduk(txtCari.getText(), 1, "p_nama", "ASC");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(30);
        gridPane.setPadding(new Insets(13));

        int col = 0;
        int row = 0;

        try {
            for (Produk produk : listProduk) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/StokKeluar/CardStockProduk.fxml"));
                Parent item = loader.load();

                // Pastikan kamu sudah buat Card controller untuk stok keluar, misalnya CardStokKeluarController
                CardStokKeluarController controller = loader.getController();
                controller.setData(produk); // atau controller.setData(produk.getNama(), produk.getStok(), ...)

                controller.setParentController(this);

                gridPane.add(item, col, row);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }

            spDataField.setContent(gridPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickBtnSimpan() {
        // Validasi apakah semua field kosong (keterangan kosong DAN tidak ada produk)
        if (txtKeterangan.getText().trim().isEmpty() && tbTambahProduk.getItems().isEmpty()) {
            messageBox.alertError("Data tidak boleh kosong semua!\n");
            return;
        }

        // Validasi apakah ada produk ditambahkan
        if (tbTambahProduk.getItems().isEmpty()) {
            messageBox.alertError("Silakan tambahkan minimal 1 produk terlebih dahulu!");
            return;
        }

        // Validasi keterangan tidak boleh kosong
        String keterangan = txtKeterangan.getText().trim();
        if (keterangan.isEmpty()) {
            messageBox.alertError("Keterangan tidak boleh kosong!");
            return;
        }

        // Validasi panjang keterangan maksimal 50 karakter
        if (keterangan.length() > 50) {
            messageBox.alertError("Keterangan tidak boleh lebih dari 50 karakter!");
            return;
        }

        // Proses penyimpanan jika validasi lolos
        List<DetailStokKeluar> detailStokKeluar = new ArrayList<>();
        for (Produk produk : tbTambahProduk.getItems()) {
            detailStokKeluar.add(new DetailStokKeluar(produk.getId(), produk.getStok()));
        }

        int jumlahJenisProduk = detailStokKeluar.size(); // jumlah produk berbeda

        connection.insertStokKeluar(
                connection.getIdKaryawanByUsername(userName),
                jumlahJenisProduk, // kirim jumlah produk, bukan total kuantitas
                keterangan,
                userAccess,
                detailStokKeluar
        );

        messageBox.alertInfo("Stok keluar berhasil disimpan!");
        clearForm();
    }


    @FXML
    private void onClickBtnBatal() {
        clearForm();
    }

    @FXML
    private void onKeyTypedTxtCari(KeyEvent event) {
        loadCardProduk();
    }

    private String userAccess;

    private String userName;

    public void setUserAccess(String userAccess, String username) {
        this.userAccess = userAccess;
        this.userName = username;
    }

    @FXML
    private void onTypedTxtCariRiwayat(KeyEvent event) {
        loadDataStokKeluar(txtCariRiwayat.getText());
    }


    private void clearForm() {
        cbProduk.setValue(null);
        dpTanggal.setText(LocalDate.now().toString());
        txtKeterangan.clear();
        tbTambahProduk.getItems().clear();
        kuantitas = 1;
        txtKuantitas.setText(String.valueOf(kuantitas));
        isApTambahProduk = false;
        apTambahProduk.setVisible(isApTambahProduk);
    }



    /*--- Detail Transaksi ---*/
    @FXML
    private AnchorPane apDetailTransaksi;

    @FXML
    private TableView<DetailStokKeluar> tbDetailTransaksi;

    @FXML
    private TableColumn colProdukDtl;

    @FXML
    private TableColumn colJumlahDtl;

    @FXML
    private Label lbIDTransaksiDtl;

    @FXML
    private Label lblKeteranganDtl;

    @FXML
    private Label lblTanggalkeluarDtl;

    private void loadDataDetailTransaksiStokKeluar(int idTransaksi) {
        // Atur kolom tabel
        colProdukDtl.setCellValueFactory(new PropertyValueFactory<>("nama_produk"));
        colProdukDtl.setStyle("-fx-alignment: CENTER-LEFT;");

        colJumlahDtl.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        colJumlahDtl.setStyle("-fx-alignment: CENTER-RIGHT;");

        // Ambil data dari database
        List<DetailStokKeluar> list = connection.getDetailStokKeluarById(idTransaksi);
        ObservableList<DetailStokKeluar> observableList = FXCollections.observableArrayList(list);
        tbDetailTransaksi.setItems(observableList);

        // Ambil header transaksi dari objek StokKeluar (optional: buat metode ambil data by id)
        StokKeluar transaksi = connection.getStokKeluarById(idTransaksi);
        if (transaksi != null) {
            lbIDTransaksiDtl.setText(transaksi.getSk_id()+"");
            lblKeteranganDtl.setText(transaksi.getSk_keterangan());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            lblTanggalkeluarDtl.setText(sdf.format(transaksi.getSk_tanggal_keluar()).toString());
        }

        // Tampilkan panel detail
        apDetailTransaksi.setVisible(true);
    }



    @FXML
    protected void onClickApDetailTransaksi() {
        apDetailTransaksi.setVisible(false);
    }
}
