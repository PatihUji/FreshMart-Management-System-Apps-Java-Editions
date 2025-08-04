package Transaksi.Penjualan;

public class DetailPenjualan {
    private int p_id;
    private int dp_kuantitas;
    private Double dp_subTotal;
    private String p_nama;
    private Double p_harga;

    public DetailPenjualan(int p_id, int dp_kuantitas, Double dp_subTotal) {
        this.p_id = p_id;
        this.dp_kuantitas = dp_kuantitas;
        this.dp_subTotal = dp_subTotal;
    }

    public DetailPenjualan(String p_nama, int dp_kuantitas, Double p_harga) {
        this.p_nama = p_nama;
        this.dp_kuantitas = dp_kuantitas;
        this.p_harga = p_harga;
    }

    public String getP_nama() {
        return p_nama;
    }

    public void setP_nama(String p_nama) {
        this.p_nama = p_nama;
    }

    public Double getP_harga() {
        return p_harga;
    }

    public void setP_harga(Double p_harga) {
        this.p_harga = p_harga;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getDp_kuantitas() {
        return dp_kuantitas;
    }

    public void setDp_kuantitas(int dp_kuantitas) {
        this.dp_kuantitas = dp_kuantitas;
    }

    public Double getDp_subTotal() {
        return dp_subTotal;
    }

    public void setDp_subTotal(Double dp_subTotal) {
        this.dp_subTotal = dp_subTotal;
    }
}
