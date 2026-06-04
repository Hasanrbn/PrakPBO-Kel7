package controller;

import dao.SiswaDAO;
import model.Siswa;
import utils.GradeHelper;
import utils.RankingHelper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SiswaController {

    private SiswaDAO siswaDAO;

    // Thread pool untuk operasi ranking paralel
    private static final ExecutorService rankingExecutor =
            Executors.newFixedThreadPool(4);

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
    // Menggunakan ExecutorService agar update ranking
    // dapat dijalankan di thread terpisah (non-blocking).
    // Future.get() memastikan operasi selesai sebelum kembali,
    // sehingga data tetap konsisten untuk caller.
    // =========================
    public void updateRankingPerKelas(int idKelas) {

        Future<?> future = rankingExecutor.submit(() -> {

            List<Siswa> list = siswaDAO.getByKelas(idKelas);

            RankingHelper.urutkanRanking(list);

            for (Siswa siswa : list) {
                siswaDAO.update(siswa);
            }

            System.out.println("[Thread: " + Thread.currentThread().getName()
                    + "] Ranking kelas " + idKelas + " selesai diperbarui.");
        });

        // Tunggu sampai selesai agar data konsisten
        try {
            future.get();
        } catch (Exception e) {
            System.err.println("Gagal update ranking: " + e.getMessage());
        }
    }

    // =========================
    // SHUTDOWN EXECUTOR (panggil saat app close)
    // =========================
    public static void shutdownExecutor() {
        rankingExecutor.shutdown();
    }
}
