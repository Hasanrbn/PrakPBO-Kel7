package dao;

import config.Koneksi;
import model.Statistik;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatistikDAO {

    private Connection conn;

    public StatistikDAO() {
        conn = Koneksi.getConnection();
    }

    // Statistik nilai per kelas
    public List<Statistik> getStatistik() {

        List<Statistik> list = new ArrayList<>();

        String sql =
                "SELECT * FROM statistik_nilai";

        try {

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Statistik s = new Statistik();

                s.setNamaKelas(rs.getString("nama_kelas"));
                s.setJumlahSiswa(rs.getInt("jumlah_siswa"));

                s.setRataRata(rs.getDouble("rata_rata"));
                s.setNilaiTertinggi(rs.getDouble("nilai_tertinggi"));
                s.setNilaiTerendah(rs.getDouble("nilai_terendah"));

                list.add(s);
            }

        } catch (SQLException e) {

            System.out.println("Statistik Gagal : " + e.getMessage());
        }

        return list;
    }
}