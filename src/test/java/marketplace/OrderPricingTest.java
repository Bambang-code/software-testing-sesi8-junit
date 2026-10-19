package marketplace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class OrderPricingTest {

    private static final double DELTA = 0.001;

    private OrderPricing pricing;

    @BeforeEach
    void setUp() {
        pricing = new OrderPricing();   // Arrange: objek baru untuk setiap test (FIRST: Independent)
    }

    // ---------- hitungTotalSetelahDiskon ----------

    @Test
    @DisplayName("Subtotal 99.999 (tepat di bawah batas) tidak mendapat diskon")
    void tanpaDiskonDiBawahBatas() {
        double hasil = pricing.hitungTotalSetelahDiskon(99_999);
        assertEquals(99_999, hasil, DELTA);
    }

    @Test
    @DisplayName("Subtotal tepat 100.000 mendapat diskon 5%")
    void diskonLimaPersenDiBatasBawah() {
        double hasil = pricing.hitungTotalSetelahDiskon(100_000);
        assertEquals(95_000, hasil, DELTA);
    }

    @Test
    @DisplayName("Subtotal tepat 500.000 mendapat diskon 10%")
    void diskonSepuluhPersenDiBatasAtas() {
        double hasil = pricing.hitungTotalSetelahDiskon(500_000);
        assertEquals(450_000, hasil, DELTA);
    }

    @ParameterizedTest(name = "subtotal {0} -> total {1}")
    @CsvSource({
            "0,       0",
            "99999,   99999",
            "100000,  95000",
            "100001,  95000.95",
            "499999,  474999.05",
            "500000,  450000",
            "500001,  450000.9"
    })
    @DisplayName("Diskon pada nilai-nilai batas (BVA)")
    void diskonNilaiBatas(double subtotal, double diharapkan) {
        assertEquals(diharapkan, pricing.hitungTotalSetelahDiskon(subtotal), DELTA);
    }

    @Test
    @DisplayName("Subtotal negatif ditolak dengan IllegalArgumentException")
    void subtotalNegatifDitolak() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pricing.hitungTotalSetelahDiskon(-1));
        assertEquals("Subtotal negatif", ex.getMessage());
    }

    // ---------- hitungOngkir ----------

    @Test
    @DisplayName("Berat 1 kg dikenai ongkir Rp10.000")
    void ongkirBeratRingan() {
        assertEquals(10_000, pricing.hitungOngkir(1.0, false), DELTA);
    }

    @Test
    @DisplayName("Berat 5 kg dikenai Rp20.000, 5,1 kg naik menjadi Rp25.000")
    void ongkirBatasLimaKilo() {
        assertAll(
                () -> assertEquals(20_000, pricing.hitungOngkir(5.0, false), DELTA),
                () -> assertEquals(25_000, pricing.hitungOngkir(5.1, false), DELTA)
        );
    }

    @Test
    @DisplayName("Pengiriman ekspres dikenai tarif 1,5 kali")
    void ongkirEkspres() {
        assertEquals(15_000, pricing.hitungOngkir(1.0, true), DELTA);
    }

    @Test
    @DisplayName("Berat nol atau negatif ditolak")
    void beratTidakValidDitolak() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> pricing.hitungOngkir(0, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> pricing.hitungOngkir(-2.5, true))
        );
    }

    // ---------- klasifikasiPelanggan ----------

    @ParameterizedTest(name = "{0} pesanan -> {1}")
    @CsvSource({
            "0,  BARU",
            "4,  BARU",
            "5,  REGULER",
            "19, REGULER",
            "20, LOYAL",
            "21, LOYAL"
    })
    @DisplayName("Klasifikasi pelanggan pada nilai-nilai batas (BVA)")
    void klasifikasiNilaiBatas(int jumlahPesanan, String diharapkan) {
        assertEquals(diharapkan, pricing.klasifikasiPelanggan(jumlahPesanan));
    }

    @Test
    @DisplayName("Jumlah pesanan negatif ditolak")
    void jumlahPesananNegatifDitolak() {
        assertThrows(IllegalArgumentException.class,
                () -> pricing.klasifikasiPelanggan(-1));
    }
}
