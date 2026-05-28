package dao;

import config.Koneksi;
import model.Kelas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KelasDAO {

    private Connection conn;

    public KelasDAO() {
        conn = Koneksi.getConnection();
    }

    // =========================
    // INSERT KELAS
    // =========================
    public boolean insert(Kelas kelas) {
        String sql = "INSERT INTO kelas (nama_kelas, wali_kelas) VALUES (?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kelas.getNamaKelas());
            ps.setString(2, kelas.getWaliKelas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // =========================
    // UPDATE KELAS
    // =========================
    public boolean update(Kelas kelas) {
        String sql = "UPDATE kelas SET nama_kelas=?, wali_kelas=? WHERE id_kelas=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kelas.getNamaKelas());
            ps.setString(2, kelas.getWaliKelas());
            ps.setInt(3, kelas.getIdKelas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Kelas Gagal : " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DELETE KELAS
    // =========================
    public boolean delete(int idKelas) {
        String sql = "DELETE FROM kelas WHERE id_kelas=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idKelas);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete Kelas Gagal : " + e.getMessage());
            return false;
        }
    }

    // =========================
    // GET ALL KELAS
    // =========================
    public List<Kelas> getAll() {
        List<Kelas> list = new ArrayList<>();
        String sql = "SELECT * FROM kelas ORDER BY id_kelas ASC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Kelas k = new Kelas();
                k.setIdKelas(rs.getInt("id_kelas"));
                k.setNamaKelas(rs.getString("nama_kelas"));
                k.setWaliKelas(rs.getString("wali_kelas"));
                list.add(k);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    // =========================
    // CEK ADA SISWA DI KELAS
    // =========================
    public boolean hasSiswa(int idKelas) {
        String sql = "SELECT COUNT(*) FROM siswa WHERE id_kelas=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idKelas);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
