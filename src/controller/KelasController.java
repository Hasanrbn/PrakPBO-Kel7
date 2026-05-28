package controller;

import dao.KelasDAO;
import model.Kelas;

import java.util.List;

public class KelasController {

    private KelasDAO kelasDAO;

    public KelasController() {
        kelasDAO = new KelasDAO();
    }

    public boolean tambahKelas(Kelas kelas) {
        return kelasDAO.insert(kelas);
    }

    public boolean updateKelas(Kelas kelas) {
        return kelasDAO.update(kelas);
    }

    public boolean hapusKelas(int idKelas) {
        if (kelasDAO.hasSiswa(idKelas)) return false;
        return kelasDAO.delete(idKelas);
    }

    public List<Kelas> getAllKelas() {
        return kelasDAO.getAll();
    }
}
