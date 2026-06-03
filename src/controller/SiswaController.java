package controller;

import dao.SiswaDAO;
import model.Siswa;
import utils.GradeHelper;
import utils.RankingHelper;

import java.util.List;

public class SiswaController {

private SiswaDAO siswaDAO;

public SiswaController() {
    siswaDAO = new SiswaDAO();
}

// =========================
// TAMBAH SISWA
// =========================
public boolean tambahSiswa(Siswa siswa) {

    if (siswaDAO.isIdExist(siswa.getIdSiswa())) {
        return false;
    }

    hitungNilaiDanGrade(siswa);

    boolean berhasil = siswaDAO.insert(siswa);

    if (berhasil) {
        updateRankingPerKelas(siswa.getIdKelas());
    }

    return berhasil;
}

// =========================
// UPDATE SISWA
// =========================
public boolean updateSiswa(Siswa siswa) {

    hitungNilaiDanGrade(siswa);

    boolean berhasil = siswaDAO.update(siswa);

    if (berhasil) {
        updateRankingPerKelas(siswa.getIdKelas());
    }

    return berhasil;
}

// =========================
// HAPUS SISWA
// =========================
public boolean hapusSiswa(String idSiswa, int idKelas) {

    boolean berhasil = siswaDAO.delete(idSiswa);

    if (berhasil) {
        updateRankingPerKelas(idKelas);
    }

    return berhasil;
}

// =========================
// GET ALL SISWA
// =========================
public List<Siswa> getAllSiswa() {
    return siswaDAO.getAll();
}

// =========================
// GET BY KELAS
// =========================
public List<Siswa> getByKelas(int idKelas) {
    return siswaDAO.getByKelas(idKelas);
}

// =========================
// CARI SISWA
// =========================
public List<Siswa> cariSiswa(String keyword) {
    return siswaDAO.search(keyword);
}

// =========================
// SORT NILAI
// =========================
public List<Siswa> sortNilai() {
    return siswaDAO.sortByNilai();
}

// =========================
// HITUNG NILAI AKHIR & GRADE
// =========================
private void hitungNilaiDanGrade(Siswa siswa) {

    double nilaiAkhir =
            GradeHelper.hitungNilaiAkhir(
                    siswa.getNilaiTugas(),
                    siswa.getNilaiUTS(),
                    siswa.getNilaiUAS()
            );

    siswa.setNilaiAkhir(nilaiAkhir);

    siswa.setGrade(
            GradeHelper.hitungGrade(nilaiAkhir)
    );
}

// =========================
// UPDATE RANKING PER KELAS
// =========================
public void updateRankingPerKelas(int idKelas) {

    List<Siswa> list =
            siswaDAO.getByKelas(idKelas);

    RankingHelper.urutkanRanking(list);

    for (Siswa siswa : list) {
        siswaDAO.update(siswa);
    }
}


}
