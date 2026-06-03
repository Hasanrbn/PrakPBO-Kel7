package utils;

public class GradeHelper {

    // Hitung nilai akhir
    public static double hitungNilaiAkhir(
        double tugas,
        double uts,
        double uas) {

    return (tugas * 0.3)
         + (uts * 0.3)
         + (uas * 0.4);
    }
    // Hitung grade
    public static String hitungGrade(double nilaiAkhir) {

        if (nilaiAkhir >= 85) {

            return "A";

        } else if (nilaiAkhir >= 75) {

            return "B";

        } else if (nilaiAkhir >= 65) {

            return "C";

        } else if (nilaiAkhir >= 50) {

            return "D";

        } else {

            return "E";
        }
    }

    // Keterangan grade
    public static String getKeterangan(String grade) {

        switch (grade) {

            case "A":
                return "Sangat Baik";

            case "B":
                return "Baik";

            case "C":
                return "Cukup";

            case "D":
                return "Kurang";

            default:
                return "Gagal";
        }
    }
}