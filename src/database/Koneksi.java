package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {

    private static Connection koneksi;

    public static Connection getConnection() {

        if (koneksi == null) {

            try {

                String url = "jdbc:mysql://localhost:3306/db_manajemen_siswa";
                String user = "root";
                String password = "";

                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

                koneksi = DriverManager.getConnection(url, user, password);

                System.out.println("Koneksi Database Berhasil!");

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        null,
                        "Koneksi Database Gagal : " + e.getMessage()
                );

                System.exit(0);
            }
        }

        return koneksi;
    }
}