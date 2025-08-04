package Transaksi.Penjualan;

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
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import master.MetodePembayaran.MetodePembayaran;
import master.Promo.Promo;
import master.karyawan.karyawan;
import master.produk.Produk;
import master.setting.Setting;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PenjualanProdukController implements Initializable {

    @FXML
    private Button btnBatal;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnTambahPromo;

    @FXML
    private ComboBox<Setting> cbJenisPembelian;

    @FXML
    private ComboBox<MetodePembayaran> cbMetodePembayaran;

    @FXML
    private Label lblNamaKasir;

    @FXML
    private Label lblReqPassword;

    @FXML
    private Label lblReqUsername;

    @FXML
    private ScrollPane spKeranjang;

    @FXML
    private ScrollPane spDataField;

    @FXML
    private TextField txtCari;

    @FXML
    private TextField txtTotalHarga;

    @FXML
    private TextField txtUangDibayar;

    @FXML
    private TextField txtUangKembalian;

    @FXML
    private ListView<Promo> lvPromo;

    boolean isApViewTransaksi = false;

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

    @FXML
    void onKeyTypedTxtCari(KeyEvent event) {
        loadDataItems(txtCari.getText(), "p_nama", "ASC");
    }

    @FXML
    void onClickBtnBatal(){
        cbMetodePembayaran.setValue(null);
        cbJenisPembelian.setValue(null);
        for (Promo promo : lvPromo.getItems()) {
            promo.setSelected(false);
        }

        isListViewTrue = false;
        lvPromo.setVisible(isListViewTrue);
        txtUangDibayar.clear();
        txtUangKembalian.clear();
        txtTotalHarga.clear();

        Node content = spKeranjang.getContent();
        if (content instanceof GridPane gridPane) {
            gridPane.getChildren().clear();
        }
    }

    @FXML
    void onClickBtnSimpan() {
        boolean isKeranjangEmpty = false;
        Node content = spKeranjang.getContent();

        if (content instanceof GridPane gridPane) {
            if(gridPane.getChildren().isEmpty()){
                isKeranjangEmpty = true;
            }
        }

        if(txtTotalHarga.getText().isEmpty() || cbMetodePembayaran.getSelectionModel().getSelectedItem() == null || cbJenisPembelian.getSelectionModel().getSelectedItem() == null || txtUangDibayar.getText().isEmpty() || isKeranjangEmpty) {
            messageBox.alertError("Data yang wajib diisi belum terisi semua!");
        }
        else if(Double.parseDouble(txtUangDibayar.getText().replace(".", "").replaceAll(",.*", "")) < Double.parseDouble(txtTotalHarga.getText().replace("Rp", "").replace(".", "").replaceAll(",.*", ""))) {
            messageBox.alertWarning("Total yang dibayar tidak mencukupi!");
        }
        else {
            List<DetailPenjualan> listDetailPenjualan = new ArrayList<>();
            List<DetailPromo> listDetailPromo = new ArrayList<>();

            if (content instanceof GridPane gridPane) {
                for (Node node : gridPane.getChildren()) {
                    if (node.getUserData() instanceof CardProdukInCartController controller) {
                        int idProduk = controller.getIdProduk();
                        int qty = controller.getKuantitas();
                        double subTotal = controller.getSubTotalHarga();

                        DetailPenjualan item = new DetailPenjualan(idProduk, qty, subTotal);
                        listDetailPenjualan.add(item);
                    }
                }
            }

            for (Promo promo : lvPromo.getItems()) {
                if (promo.isSelected()) {
                    DetailPromo selected = new DetailPromo(promo.getPr_id());
                    listDetailPromo.add(selected);
                }
            }

            String totalHargaText = txtTotalHarga.getText().replace("Rp", "").replace(".", "").replaceAll(",.*", "").trim();

            if (cbJenisPembelian.getValue().getS_nama().equals("Bawa Pulang")) {
                connection.insertPenjualan(cbJenisPembelian.getValue().getS_id(), cbMetodePembayaran.getValue().getMpb_id(), connection.getIdKaryawanByUsername(userName), Double.parseDouble(totalHargaText), userAccess, 1, listDetailPenjualan, listDetailPromo);
            } else if (cbJenisPembelian.getValue().getS_nama().equals("Pengiriman")) {
                connection.insertPenjualan(cbJenisPembelian.getValue().getS_id(), cbMetodePembayaran.getValue().getMpb_id(), connection.getIdKaryawanByUsername(userName), Double.parseDouble(totalHargaText), userAccess, 0, listDetailPenjualan, listDetailPromo);
            }
            onClickBtnBatal();
        }
    }

    @FXML
    void onKeyPressTxtUangDibayar() {
        txtUangDibayar.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }

            if (txtUangDibayar.getText() != null && txtUangDibayar.getText().length() > 20) {
                event.consume();
            }

            DecimalFormat currencyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("in", "ID"));
            txtUangDibayar.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) return;

                String cleaned = newValue.replaceAll("\\.", "");
                try {
                    long value = Long.parseLong(cleaned);
                    txtUangDibayar.setText(currencyFormat.format(value));
                } catch (NumberFormatException e) {
                    txtUangDibayar.setText(oldValue);
                }
            });

            try {
                // Ambil total harga dan uang dibayar, bersihkan formatnya
                String totalHargaText = txtTotalHarga.getText().replace("Rp", "").replace(".", "").replaceAll(",.*", "").trim();
                String uangDibayarText = txtUangDibayar.getText().replace(".", "").replaceAll(",.*", "").trim();

                if (totalHargaText.isEmpty() || uangDibayarText.isEmpty()) {
                    txtUangKembalian.clear();
                    return;
                }

                double totalHarga = Double.parseDouble(totalHargaText);
                double uangDibayar = Double.parseDouble(uangDibayarText);

                if (uangDibayar >= totalHarga) {
                    double kembalian = uangDibayar - totalHarga;
                    txtUangKembalian.setText(RupiahFormat(kembalian));
                } else {
                    txtUangKembalian.clear();
                }

            } catch (NumberFormatException e) {
                txtUangKembalian.clear();
            }
        });
    }

    @FXML
    void onClickBtnTambahPromo() {
        if(isListViewTrue){
            isListViewTrue = false;
        }
        else {
            isListViewTrue = true;
        }

        lvPromo.setVisible(isListViewTrue);
    }

    MessageBox messageBox = new MessageBox();

    private boolean isListViewTrue =  false;

    DBConnect connection = new DBConnect();

    private GridPane keranjangGrid = new GridPane();

    private String userAccess;

    private String userName;

    public void setUserAccess(String userAccess, String username) {
        this.userAccess = userAccess;
        lblNamaKasir.setText(userAccess);
        this.userName = username;
    }

    public GridPane getKeranjangGrid() {
        return keranjangGrid;
    }

    public void setTotalHarga() {
        double totalHarga = 0.0;

        Node content = spKeranjang.getContent();
        if (content instanceof GridPane gridPane) {
            for (Node node : gridPane.getChildren()) {
                if (node.getUserData() instanceof CardProdukInCartController controller) {
                    totalHarga += controller.getSubTotalHarga();
                }
            }
        }

        // Hitung semua promo yang terpilih
        for (Promo promo : lvPromo.getItems()) {
            if (promo.isSelected()) {
                if (promo.getPr_persentase() > 0) {
                    totalHarga -= totalHarga * (promo.getPr_persentase() / 100.0);
                }
            }
        }

        // Pastikan harga tidak minus
        if (totalHarga < 0) totalHarga = 0;

        txtTotalHarga.setText(RupiahFormat(totalHarga));
    }

    private void loadDataItems (String search, String sortColumn, String sortOrder) {
        if (search.isEmpty()){
            search = null;
        }
        if (sortColumn.isEmpty()){
            sortColumn = "p_nama";
        }
        if (sortOrder.isEmpty()){
            sortOrder = "ASC";
        }
        List<Produk> listData = connection.getListProduk(search, 1, sortColumn, sortOrder);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(30);
        gridPane.setPadding(new Insets(13));

        int column = 0;
        int row = 0;

        try {
            for (Produk data : listData) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/Penjualan/CardProductItem.fxml"));
                Parent item = loader.load();

                CardProductItemController controller = loader.getController();
                controller.setData(data.getId(), data.getGambar(), data.getNama(), data.getHarga(), data.getSatuan(), data.getJpnama(), data.getStok());

                controller.setParentController(this);

                gridPane.add(item, column, row);

                column++;
                if (column == 4) {
                    column = 0;
                    row++;
                }
            }

            spDataField.setContent(gridPane); // Masukkan ke ScrollPane

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String RupiahFormat(Double uang){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(uang);
    }

    private void loadComboBoxMetodePembayaran() {
        cbMetodePembayaran.getItems().clear();

        for (MetodePembayaran metodePembayaran : connection.getListNamaMetodePembayaran()) {
            cbMetodePembayaran.getItems().add(metodePembayaran);
        }
    }

    private void loadComboBoxJenisPembelian(){
        cbJenisPembelian.getItems().clear();

        for (Setting jenisPembelian : connection.getListSettingByKategori("jenis pembelian")) {
            cbJenisPembelian.getItems().add(jenisPembelian);
        }
    }

    private void loadListViewPromo(){
        List<Promo> list = connection.getListNamaPromo();

        for (Promo promo : list) {
            promo.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                setTotalHarga(); // Update total setiap promo diubah
            });
        }

        ObservableList<Promo> observableList = FXCollections.observableArrayList(list);
        lvPromo.setItems(observableList);
        lvPromo.setCellFactory(CheckBoxListCell.forListView(
                Promo::selectedProperty,  // Property binding untuk checkbox
                new StringConverter<>() {
                    @Override
                    public String toString(Promo object) {
                        return object.getPr_nama();  // Teks yang tampil di ListView
                    }

                    @Override
                    public Promo fromString(String string) {
                        return null; // Tidak digunakan
                    }
                }
        ));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        keranjangGrid.setVgap(20);
        spKeranjang.setContent(keranjangGrid);

        loadDataItems(txtCari.getText(), "p_nama", "ASC");
        loadComboBoxMetodePembayaran();
        loadComboBoxJenisPembelian();
        loadListViewPromo();

        /*--- view transaksi ---*/
        loadDataPenjualan(null);
        addButtonToTable();
        lockTableViewBehavior(tbPenjualan);
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
    private TableColumn<Penjualan, Void> colAksi;

    @FXML
    private TableColumn colIdTransaksi;

    @FXML
    private TableColumn colJenisPembelian;

    @FXML
    private TableColumn colMetodePembayaran;

    @FXML
    private TableColumn colNamaKasir;

    @FXML
    private TableColumn colTotalHarga;

    @FXML
    private TableColumn colTglPembelian;

    @FXML
    private TableView<Penjualan> tbPenjualan;

    @FXML
    private TextField txtCariRiwayat;

    @FXML
    private Pagination pgnPenjualan;

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
        loadDataPenjualan(txtCariRiwayat.getText());
    }

    /*--- Membatasi jumlah baris yang muncul dalam 1 page ---*/
    private static final int ROWS_PER_PAGE = 8;

    /*--- Menampilkan data pada tabel ---*/
    private ObservableList<Penjualan> allPenjualanList = FXCollections.observableArrayList();

    private void loadDataPenjualan(String search) {
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("pnj_id"));
        colIdTransaksi.setStyle("-fx-alignment: CENTER-LEFT;");
        colTglPembelian.setCellValueFactory(new PropertyValueFactory<>("pnj_created_date"));
        colTglPembelian.setStyle("-fx-alignment: CENTER-LEFT;");
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

        List<Penjualan> list = connection.getListPenjualan(search, 1);
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
        colAksi.setCellFactory(param -> new TableCell<>() {
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

                    // Event handler
                    btnDetail.setOnAction(e -> {
                        Penjualan data = getTableView().getItems().get(getIndex());
                        lblNamaKasirDtl.setText(data.getKry_nama());
                        lblJenisPembelianDtl.setText(data.getS_nama());
                        lblTotalPembelianDtl.setText( RupiahFormat(data.getPnj_total_harga()));
                        loadDataDetailTransaksi(data.getPnj_id());

                        Double totalDiskon = 0.0;
                        if(tbDetailTransaksi.getItems() != null){
                            for(DetailPenjualan detail : tbDetailTransaksi.getItems()){
                                totalDiskon += detail.getP_harga();
                            }
                            totalDiskon = totalDiskon - data.getPnj_total_harga();
                        }

                        if(totalDiskon > 0){
                            lblDiskonDtl.setText(RupiahFormat(totalDiskon));
                        }
                        else {
                            lblDiskonDtl.setText("-");
                        }
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

    private void lockTableViewBehavior(TableView<?> tableView) {
        // Nonaktifkan seleksi baris
        tableView.setSelectionModel(null);

        // Nonaktifkan fokus keyboard
        tableView.setFocusTraversable(false);

    }

    /*--- Detail Transaksi ---*/
    @FXML
    private AnchorPane apDetailTransaksi;

    @FXML
    private TableView<DetailPenjualan> tbDetailTransaksi;

    @FXML
    private TableColumn colHargaSatuanDtl;

    @FXML
    private TableColumn colJumlahDtl;

    @FXML
    private TableColumn colProdukDtl;

    @FXML
    private Label lblJenisPembelianDtl;

    @FXML
    private Label lblNamaKasirDtl;

    @FXML
    private Label lblTotalPembelianDtl;

    @FXML
    private Label lblDiskonDtl;

    private void loadDataDetailTransaksi(int idTransaksi){
        colProdukDtl.setCellValueFactory(new PropertyValueFactory<>("p_nama"));
        colProdukDtl.setStyle("-fx-alignment: CENTER-LEFT;");
        colJumlahDtl.setCellValueFactory(new PropertyValueFactory<>("dp_kuantitas"));
        colJumlahDtl.setStyle("-fx-alignment: CENTER-RIGHT;");
        colHargaSatuanDtl.setCellValueFactory(new PropertyValueFactory<>("p_harga"));
        colHargaSatuanDtl.setStyle("-fx-alignment: CENTER-RIGHT;");
        colHargaSatuanDtl.setCellFactory(column -> new TableCell<DetailPenjualan, Number>() {
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
