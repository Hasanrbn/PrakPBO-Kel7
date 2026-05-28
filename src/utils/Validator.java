package utils;

import javax.swing.*;

public class Validator {

    // Validasi field kosong
    public static boolean isEmpty(JTextField textField, String namaField) {

        if (textField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    namaField + " tidak boleh kosong!"
            );

            textField.requestFocus();

            return true;
        }

        return false;
    }

    // Validasi angka
    public static boolean isNumber(String value) {

        try {

            Double.parseDouble(value);

            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    // Validasi nilai 0 - 100
    public static boolean validNilai(double nilai) {

        return nilai >= 0 && nilai <= 100;
    }
}