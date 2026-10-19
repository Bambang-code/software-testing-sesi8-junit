package marketplace;

/**
 * Logika harga pesanan pada marketplace UMKM lokal.
 * Berisi tiga method berlogika bersyarat: diskon, ongkir, dan klasifikasi pelanggan.
 */
public class OrderPricing {

    public static final double BATAS_DISKON_5 = 100_000;
    public static final double BATAS_DISKON_10 = 500_000;

    /**
     * Menghitung total setelah diskon.
     * Aturan: subtotal < 100.000 tanpa diskon, 100.000 s.d. < 500.000 diskon 5%,
     * >= 500.000 diskon 10%.
     *
     * @throws IllegalArgumentException jika subtotal negatif
     */
    public double hitungTotalSetelahDiskon(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal negatif");
        }
        if (subtotal >= BATAS_DISKON_10) {
            return subtotal * 0.90;
        }
        if (subtotal >= BATAS_DISKON_5) {
            return subtotal * 0.95;
        }
        return subtotal;
    }

    /**
     * Menghitung ongkos kirim berdasarkan berat (kg).
     * Aturan: <= 1 kg Rp10.000, > 1 s.d. 5 kg Rp20.000,
     * di atas 5 kg Rp20.000 + Rp5.000 per kg tambahan (dibulatkan ke atas).
     * Pengiriman ekspres dikenai tarif 1,5 kali.
     *
     * @throws IllegalArgumentException jika berat nol atau negatif
     */
    public double hitungOngkir(double beratKg, boolean ekspres) {
        if (beratKg <= 0) {
            throw new IllegalArgumentException("Berat harus lebih dari 0");
        }
        double ongkir;
        if (beratKg <= 1) {
            ongkir = 10_000;
        } else if (beratKg <= 5) {
            ongkir = 20_000;
        } else {
            ongkir = 20_000 + Math.ceil(beratKg - 5) * 5_000;
        }
        return ekspres ? ongkir * 1.5 : ongkir;
    }

    /**
     * Mengklasifikasikan pelanggan berdasarkan jumlah pesanan.
     * Aturan: 0-4 BARU, 5-19 REGULER, >= 20 LOYAL.
     *
     * @throws IllegalArgumentException jika jumlah pesanan negatif
     */
    public String klasifikasiPelanggan(int jumlahPesanan) {
        if (jumlahPesanan < 0) {
            throw new IllegalArgumentException("Jumlah pesanan negatif");
        }
        if (jumlahPesanan >= 20) {
            return "LOYAL";
        }
        if (jumlahPesanan >= 5) {
            return "REGULER";
        }
        return "BARU";
    }
}
