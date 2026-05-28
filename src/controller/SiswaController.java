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

    public boolean tambahSiswa(Siswa siswa) {
        // Cek ID duplikat
        if (siswaDAO.isIdExist(siswa.getIdSiswa())) return false;
        double nilaiAkhir = GradeHelper.hitungNilaiAkhir(siswa.getNilaiTugas(), siswa.getNilaiUjian());
        siswa.setNilaiAkhir(nilaiAkhir);
        siswa.setGrade(GradeHelper.hitungGrade(nilaiAkhir));
        boolean berhasil = siswaDAO.insert(siswa);
        if (berhasil) updateRankingPerKelas(siswa.getIdKelas());
        return berhasil;
    }

    public boolean updateSiswa(Siswa siswa) {
        double nilaiAkhir = GradeHelper.hitungNilaiAkhir(siswa.getNilaiTugas(), siswa.getNilaiUjian());
        siswa.setNilaiAkhir(nilaiAkhir);
        siswa.setGrade(GradeHelper.hitungGrade(nilaiAkhir));
        boolean berhasil = siswaDAO.update(siswa);
        if (berhasil) updateRankingPerKelas(siswa.getIdKelas());
        return berhasil;
    }

    public boolean hapusSiswa(String idSiswa, int idKelas) {
        boolean berhasil = siswaDAO.delete(idSiswa);
        if (berhasil) updateRankingPerKelas(idKelas);
        return berhasil;
    }

    public List<Siswa> getAllSiswa() {
        return siswaDAO.getAll();
    }

    public List<Siswa> getByKelas(int idKelas) {
        return siswaDAO.getByKelas(idKelas);
    }

    public List<Siswa> cariSiswa(String keyword) {
        return siswaDAO.search(keyword);
    }

    public List<Siswa> sortNilai() {
        return siswaDAO.sortByNilai();
    }

    public void updateRankingPerKelas(int idKelas) {
        List<Siswa> list = siswaDAO.getByKelas(idKelas);
        RankingHelper.urutkanRanking(list);
        for (Siswa s : list) {
            siswaDAO.update(s);
        }
    }
}
