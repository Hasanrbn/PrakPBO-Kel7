package utils;

import model.Siswa;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingHelper {

    // Sorting ranking berdasarkan nilai akhir
    public static void urutkanRanking(List<Siswa> list) {

        Collections.sort(list, new Comparator<Siswa>() {

            @Override
            public int compare(Siswa s1, Siswa s2) {

                return Double.compare(
                        s2.getNilaiAkhir(),
                        s1.getNilaiAkhir()
                );
            }
        });

        // Set ranking otomatis
        for (int i = 0; i < list.size(); i++) {

            list.get(i).setRanking(i + 1);
        }
    }
}