package model;

public class Statistik {

    private String namaKelas;
    private int jumlahSiswa;

    private double rataRata;
    private double nilaiTertinggi;
    private double nilaiTerendah;

    // Constructor kosong
    public Statistik() {
    }

    // Constructor lengkap
    public Statistik(String namaKelas, int jumlahSiswa,
                      double rataRata,
                      double nilaiTertinggi,
                      double nilaiTerendah) {

        this.namaKelas = namaKelas;
        this.jumlahSiswa = jumlahSiswa;
        this.rataRata = rataRata;
        this.nilaiTertinggi = nilaiTertinggi;
        this.nilaiTerendah = nilaiTerendah;
    }

    // Getter Setter

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public int getJumlahSiswa() {
        return jumlahSiswa;
    }

    public void setJumlahSiswa(int jumlahSiswa) {
        this.jumlahSiswa = jumlahSiswa;
    }

    public double getRataRata() {
        return rataRata;
    }

    public void setRataRata(double rataRata) {
        this.rataRata = rataRata;
    }

    public double getNilaiTertinggi() {
        return nilaiTertinggi;
    }

    public void setNilaiTertinggi(double nilaiTertinggi) {
        this.nilaiTertinggi = nilaiTertinggi;
    }

    public double getNilaiTerendah() {
        return nilaiTerendah;
    }

    public void setNilaiTerendah(double nilaiTerendah) {
        this.nilaiTerendah = nilaiTerendah;
    }
}