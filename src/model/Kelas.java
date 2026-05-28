package model;

public class Kelas {

    private int idKelas;
    private String namaKelas;
    private String waliKelas;

    // Constructor kosong
    public Kelas() {
    }

    // Constructor lengkap
    public Kelas(int idKelas, String namaKelas, String waliKelas) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.waliKelas = waliKelas;
    }

    // Getter Setter

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

    public String getWaliKelas() {
        return waliKelas;
    }

    public void setWaliKelas(String waliKelas) {
        this.waliKelas = waliKelas;
    }
}