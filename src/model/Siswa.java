package model;

public class Siswa {

    private String idSiswa;
    private String namaSiswa;
    private String jenisKelamin;
    private String alamat;

    private int idKelas;
    private String namaKelas;

    private double nilaiTugas;
    private double nilaiUTS;
    private double nilaiUAS;
    private double nilaiAkhir;

    private String grade;
    private int ranking;

    // Constructor kosong
    public Siswa() {
    }

    // Constructor lengkap
    public Siswa(String idSiswa, String namaSiswa, String jenisKelamin,
                  String alamat, int idKelas, String namaKelas,
                  double nilaiTugas, double nilaiUTS, double nilaiUAS,
                  double nilaiAkhir, String grade, int ranking) {

        this.idSiswa = idSiswa;
        this.namaSiswa = namaSiswa;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.nilaiTugas = nilaiTugas;
        this.nilaiUTS = nilaiUTS;
        this.nilaiUAS = nilaiUAS;
        this.nilaiAkhir = nilaiAkhir;
        this.grade = grade;
        this.ranking = ranking;
    }

    // Getter dan Setter

    public String getIdSiswa() {
        return idSiswa;
    }

    public void setIdSiswa(String idSiswa) {
        this.idSiswa = idSiswa;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public double getNilaiTugas() {
        return nilaiTugas;
    }

    public void setNilaiTugas(double nilaiTugas) {
        this.nilaiTugas = nilaiTugas;
    }

    public double getNilaiUTS() {
        return nilaiUTS;
    }

    public void setNilaiUTS(double nilaiUTS) {
        this.nilaiUTS = nilaiUTS;
    }

    public double getNilaiUAS() {
        return nilaiUAS;
    }

    public void setNilaiUAS(double nilaiUAS) {
        this.nilaiUAS = nilaiUAS;
    }   

    public double getNilaiAkhir() {
        return nilaiAkhir;
    }

    public void setNilaiAkhir(double nilaiAkhir) {
        this.nilaiAkhir = nilaiAkhir;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}