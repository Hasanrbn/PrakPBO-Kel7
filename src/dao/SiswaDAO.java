package dao;

import config.Koneksi;
import model.Siswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiswaDAO {

private Connection conn;

public SiswaDAO() {
    conn = Koneksi.getConnection();
}

// =========================
// INSERT DATA
// =========================
public boolean insert(Siswa siswa) {

    String sql =
            "INSERT INTO siswa VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, siswa.getIdSiswa());
        ps.setString(2, siswa.getNamaSiswa());
        ps.setString(3, siswa.getJenisKelamin());
        ps.setString(4, siswa.getAlamat());
        ps.setInt(5, siswa.getIdKelas());
        ps.setDouble(6, siswa.getNilaiTugas());
        ps.setDouble(7, siswa.getNilaiUTS());
        ps.setDouble(8, siswa.getNilaiUAS());
        ps.setDouble(9, siswa.getNilaiAkhir());
        ps.setString(10, siswa.getGrade());
        ps.setInt(11, siswa.getRanking());
        ps.setTimestamp(12, new Timestamp(System.currentTimeMillis()));

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Insert Gagal : " + e.getMessage());
        return false;
    }
}

// =========================
// TAMPIL SEMUA DATA
// =========================
public List<Siswa> getAll() {

    List<Siswa> list = new ArrayList<>();

    String sql =
            "SELECT s.*, k.nama_kelas " +
            "FROM siswa s " +
            "JOIN kelas k " +
            "ON s.id_kelas = k.id_kelas " +
            "ORDER BY s.id_kelas ASC, " +
            "s.ranking_kelas ASC";

    try (
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)
    ) {
        while (rs.next()) {
            list.add(mapRow(rs));
        }
    } catch (SQLException e) {
        System.out.println("Tampil Data Gagal : " + e.getMessage());
    }

    return list;
}

// =========================
// FILTER BERDASARKAN KELAS
// =========================
public List<Siswa> getByKelas(int idKelas) {

    List<Siswa> list = new ArrayList<>();

    String sql =
            "SELECT s.*, k.nama_kelas " +
            "FROM siswa s " +
            "JOIN kelas k " +
            "ON s.id_kelas = k.id_kelas " +
            "WHERE s.id_kelas=? " +
            "ORDER BY ranking_kelas ASC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idKelas);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

    } catch (SQLException e) {
        System.out.println("Filter Kelas Gagal : " + e.getMessage());
    }

    return list;
}

// =========================
// UPDATE DATA
// =========================
public boolean update(Siswa siswa) {

    String sql =
            "UPDATE siswa SET " +
            "nama_siswa=?, " +
            "jenis_kelamin=?, " +
            "alamat=?, " +
            "id_kelas=?, " +
            "nilai_tugas=?, " +
            "nilai_uts=?, " +
            "nilai_uas=?, " +
            "nilai_akhir=?, " +
            "grade=?, " +
            "ranking_kelas=? " +
            "WHERE id_siswa=?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, siswa.getNamaSiswa());
        ps.setString(2, siswa.getJenisKelamin());
        ps.setString(3, siswa.getAlamat());
        ps.setInt(4, siswa.getIdKelas());
        ps.setDouble(5, siswa.getNilaiTugas());
        ps.setDouble(6, siswa.getNilaiUTS());
        ps.setDouble(7, siswa.getNilaiUAS());
        ps.setDouble(8, siswa.getNilaiAkhir());
        ps.setString(9, siswa.getGrade());
        ps.setInt(10, siswa.getRanking());
        ps.setString(11, siswa.getIdSiswa());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Update Gagal : " + e.getMessage());
        return false;
    }
}

// =========================
// DELETE DATA
// =========================
public boolean delete(String idSiswa) {

    String sql =
            "DELETE FROM siswa " +
            "WHERE id_siswa=?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, idSiswa);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Delete Gagal : " + e.getMessage());
        return false;
    }
}

// =========================
// SEARCH DATA
// =========================
public List<Siswa> search(String keyword) {

    List<Siswa> list = new ArrayList<>();

    String sql =
            "SELECT s.*, k.nama_kelas " +
            "FROM siswa s " +
            "JOIN kelas k " +
            "ON s.id_kelas = k.id_kelas " +
            "WHERE s.nama_siswa LIKE ? " +
            "OR s.id_siswa LIKE ? " +
            "ORDER BY ranking_kelas ASC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

    } catch (SQLException e) {
        System.out.println("Search Gagal : " + e.getMessage());
    }

    return list;
}

// =========================
// SORT BY NILAI AKHIR
// =========================
public List<Siswa> sortByNilai() {

    List<Siswa> list = new ArrayList<>();

    String sql =
            "SELECT s.*, k.nama_kelas " +
            "FROM siswa s " +
            "JOIN kelas k " +
            "ON s.id_kelas = k.id_kelas " +
            "ORDER BY s.nilai_akhir DESC";

    try (
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)
    ) {
        while (rs.next()) {
            list.add(mapRow(rs));
        }
    } catch (SQLException e) {
        System.out.println("Sort Gagal : " + e.getMessage());
    }

    return list;
}

// =========================
// CEK ID SUDAH ADA
// =========================
public boolean isIdExist(String idSiswa) {

    String sql =
            "SELECT id_siswa " +
            "FROM siswa " +
            "WHERE id_siswa=?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, idSiswa);

        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }

    } catch (SQLException e) {
        System.out.println("Cek ID Gagal : " + e.getMessage());
        return false;
    }
}

// =========================
// MAPPING DATA
// =========================
private Siswa mapRow(ResultSet rs) throws SQLException {

    Siswa s = new Siswa();

    s.setIdSiswa(rs.getString("id_siswa"));
    s.setNamaSiswa(rs.getString("nama_siswa"));
    s.setJenisKelamin(rs.getString("jenis_kelamin"));
    s.setAlamat(rs.getString("alamat"));
    s.setIdKelas(rs.getInt("id_kelas"));
    s.setNamaKelas(rs.getString("nama_kelas"));
    s.setNilaiTugas(rs.getDouble("nilai_tugas"));
    s.setNilaiUTS(rs.getDouble("nilai_uts"));
    s.setNilaiUAS(rs.getDouble("nilai_uas"));
    s.setNilaiAkhir(rs.getDouble("nilai_akhir"));
    s.setGrade(rs.getString("grade"));
    s.setRanking(rs.getInt("ranking_kelas"));

    return s;
}


}
