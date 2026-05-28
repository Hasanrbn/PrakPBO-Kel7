package controller;

import dao.StatistikDAO;
import model.Statistik;

import java.util.List;

public class StatistikController {

    private StatistikDAO statistikDAO;

    public StatistikController() {

        statistikDAO = new StatistikDAO();
    }

    // =========================
    // AMBIL DATA STATISTIK
    // =========================
    public List<Statistik> getStatistik() {

        return statistikDAO.getStatistik();
    }
}