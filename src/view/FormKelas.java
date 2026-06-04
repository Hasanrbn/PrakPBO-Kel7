package view;

import controller.KelasController;
import model.Kelas;
import utils.DatabaseWorker;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class FormKelas extends JFrame {

    private JTextField txtNamaKelas;
    private JTextField txtWaliKelas;
    private JTextField txtIdKelas;

    private JButton btnTambah;
    private JButton btnEdit;
    private JButton btnHapus;
    private JButton btnBersihkan;

    private JTable table;
    private DefaultTableModel model;
    private KelasController controller;
    private JLabel lblStatus;

    // === WARNA ===
    private final Color PRIMARY    = new Color(67, 97, 238);
    private final Color SUCCESS    = new Color(34, 197, 94);
    private final Color WARNING    = new Color(234, 179, 8);
    private final Color DANGER     = new Color(239, 68, 68);
    private final Color NEUTRAL    = new Color(100, 116, 139);
    private final Color BG         = new Color(248, 250, 252);
    private final Color CARD       = Color.WHITE;
    private final Color TEXT_DARK  = new Color(30, 41, 59);
    private final Color TEXT_MUTED = new Color(100, 116, 139);
    private final Color BORDER_CLR = new Color(226, 232, 240);

    public FormKelas() {
        controller = new KelasController();
        setTitle("Manajemen Kelas");
        setSize(750, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(BG);
        initComponent();
        tampilData();
    }

    private void initComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BG);

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("🏫  Manajemen Kelas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Tambah, edit, dan hapus data kelas sekolah");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 210, 255));

        JPanel hText = new JPanel();
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        hText.setOpaque(false);
        hText.add(lblTitle);
        hText.add(Box.createVerticalStrut(4));
        hText.add(lblSub);
        header.add(hText, BorderLayout.WEST);

        // === FORM CARD ===
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(CARD);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdKelas = new JTextField();
        txtIdKelas.setVisible(false);

        txtNamaKelas = createStyledField("Contoh: X RPL 1");
        txtWaliKelas  = createStyledField("Contoh: Budi Santoso");

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0;
        formCard.add(createLabel("Nama Kelas"), gbc);
        gbc.gridx=1; gbc.weightx=1;
        formCard.add(txtNamaKelas, gbc);

        gbc.gridx=2; gbc.weightx=0;
        formCard.add(createLabel("Wali Kelas"), gbc);
        gbc.gridx=3; gbc.weightx=1;
        formCard.add(txtWaliKelas, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=4; gbc.weightx=1;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setOpaque(false);

        btnTambah    = makeBtn("＋ Tambah",   SUCCESS);
        btnEdit      = makeBtn("✎ Edit",      WARNING);
        btnHapus     = makeBtn("✕ Hapus",     DANGER);
        btnBersihkan = makeBtn("↺ Reset",     NEUTRAL);

        btnPanel.add(btnTambah);
        btnPanel.add(btnEdit);
        btnPanel.add(btnHapus);
        btnPanel.add(btnBersihkan);
        formCard.add(btnPanel, gbc);

        // === TABLE ===
        model = new DefaultTableModel(new String[]{"ID", "Nama Kelas", "Wali Kelas"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(CARD);

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(new Color(241, 245, 249));
        tableHeader.setBorder(new EmptyBorder(10, 25, 10, 25));
        JLabel tblTitle = new JLabel("Daftar Kelas");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblTitle.setForeground(TEXT_DARK);
        tableHeader.add(tblTitle);
        tableWrap.add(tableHeader, BorderLayout.NORTH);
        tableWrap.add(scroll, BorderLayout.CENTER);

        // === STATUS BAR ===
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(241, 245, 249));
        statusBar.setBorder(new EmptyBorder(6, 20, 6, 20));
        lblStatus = new JLabel("Siap");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(TEXT_MUTED);
        statusBar.add(lblStatus, BorderLayout.WEST);

        // === ASSEMBLE ===
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(header, BorderLayout.NORTH);
        topSection.add(formCard, BorderLayout.CENTER);

        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(tableWrap, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        add(mainPanel);

        // === ACTIONS ===
        btnTambah.addActionListener(e -> tambahKelas());
        btnEdit.addActionListener(e -> editKelas());
        btnHapus.addActionListener(e -> hapusKelas());
        btnBersihkan.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> pilihData());
    }

    // =====================
    // MULTITHREADING: tambahKelas
    // =====================
    private void tambahKelas() {
        String nama = txtNamaKelas.getText().trim();
        String wali = txtWaliKelas.getText().trim();
        if (nama.isEmpty()) { showWarn("Nama Kelas tidak boleh kosong!"); txtNamaKelas.requestFocus(); return; }
        if (wali.isEmpty()) { showWarn("Wali Kelas tidak boleh kosong!"); txtWaliKelas.requestFocus(); return; }

        Kelas k = new Kelas(0, nama, wali);
        setStatus("Menyimpan kelas...", TEXT_MUTED);
        setButtonsEnabled(false);

        new DatabaseWorker<Boolean>(
            () -> controller.tambahKelas(k),
            berhasil -> {
                setButtonsEnabled(true);
                if (berhasil) {
                    showInfo("Kelas berhasil ditambahkan!");
                    setStatus("Kelas " + nama + " berhasil ditambahkan", SUCCESS);
                    tampilData(); clearForm();
                } else {
                    showError("Gagal menambahkan kelas.");
                    setStatus("Gagal tambah kelas", DANGER);
                }
            },
            err -> {
                setButtonsEnabled(true);
                showError("Error: " + err.getMessage());
                setStatus("Error: " + err.getMessage(), DANGER);
            }
        ).execute();
    }

    // =====================
    // MULTITHREADING: editKelas
    // =====================
    private void editKelas() {
        if (txtIdKelas.getText().isEmpty()) { showWarn("Pilih kelas dari tabel terlebih dahulu!"); return; }
        String nama = txtNamaKelas.getText().trim();
        String wali = txtWaliKelas.getText().trim();
        if (nama.isEmpty()) { showWarn("Nama Kelas tidak boleh kosong!"); return; }
        if (wali.isEmpty()) { showWarn("Wali Kelas tidak boleh kosong!"); return; }

        Kelas k = new Kelas(Integer.parseInt(txtIdKelas.getText()), nama, wali);
        setStatus("Memperbarui kelas...", TEXT_MUTED);
        setButtonsEnabled(false);

        new DatabaseWorker<Boolean>(
            () -> controller.updateKelas(k),
            berhasil -> {
                setButtonsEnabled(true);
                if (berhasil) {
                    showInfo("Kelas berhasil diperbarui!");
                    setStatus("Kelas " + nama + " berhasil diperbarui", SUCCESS);
                    tampilData(); clearForm();
                } else {
                    showError("Gagal memperbarui kelas.");
                    setStatus("Gagal update kelas", DANGER);
                }
            },
            err -> {
                setButtonsEnabled(true);
                showError("Error: " + err.getMessage());
                setStatus("Error: " + err.getMessage(), DANGER);
            }
        ).execute();
    }

    // =====================
    // MULTITHREADING: hapusKelas
    // =====================
    private void hapusKelas() {
        if (txtIdKelas.getText().isEmpty()) { showWarn("Pilih kelas dari tabel terlebih dahulu!"); return; }
        int id = Integer.parseInt(txtIdKelas.getText());
        int opt = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus kelas ini?\nPastikan tidak ada siswa di kelas ini.",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;

        setStatus("Menghapus kelas...", TEXT_MUTED);
        setButtonsEnabled(false);

        new DatabaseWorker<Boolean>(
            () -> controller.hapusKelas(id),
            berhasil -> {
                setButtonsEnabled(true);
                if (berhasil) {
                    showInfo("Kelas berhasil dihapus!");
                    setStatus("Kelas berhasil dihapus", SUCCESS);
                    tampilData(); clearForm();
                } else {
                    showError("Gagal menghapus kelas.\nPastikan tidak ada siswa yang terdaftar di kelas ini.");
                    setStatus("Gagal hapus kelas (mungkin masih ada siswa)", DANGER);
                }
            },
            err -> {
                setButtonsEnabled(true);
                showError("Error: " + err.getMessage());
                setStatus("Error: " + err.getMessage(), DANGER);
            }
        ).execute();
    }

    // =====================
    // MULTITHREADING: tampilData
    // =====================
    private void tampilData() {
        setStatus("Memuat data kelas...", TEXT_MUTED);

        new DatabaseWorker<List<Kelas>>(
            () -> controller.getAllKelas(),
            kelasList -> {
                model.setRowCount(0);
                for (Kelas k : kelasList)
                    model.addRow(new Object[]{k.getIdKelas(), k.getNamaKelas(), k.getWaliKelas()});
                setStatus("Menampilkan " + kelasList.size() + " kelas", SUCCESS);
            },
            err -> setStatus("Gagal memuat data: " + err.getMessage(), DANGER)
        ).execute();
    }

    private void pilihData() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtIdKelas.setText(model.getValueAt(row, 0).toString());
            txtNamaKelas.setText(model.getValueAt(row, 1).toString());
            txtWaliKelas.setText(model.getValueAt(row, 2).toString());
        }
    }

    private void clearForm() {
        txtIdKelas.setText("");
        txtNamaKelas.setText("");
        txtWaliKelas.setText("");
        table.clearSelection();
    }

    private void setStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }

    private void setButtonsEnabled(boolean enabled) {
        btnTambah.setEnabled(enabled);
        btnEdit.setEnabled(enabled);
        btnHapus.setEnabled(enabled);
    }

    // === HELPERS ===
    private JTextField createStyledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(200, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR, 1),
            new EmptyBorder(4, 10, 4, 10)
        ));
        f.setToolTipText(placeholder);
        return f;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT_DARK);
        return l;
    }

    private JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            new EmptyBorder(8, 18, 8, 18)
        ));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(color.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(color); }
        });
        return b;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(36);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setGridColor(new Color(241, 245, 249));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(TEXT_DARK);
        t.setShowVerticalLines(false);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));
        t.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        t.getColumnModel().getColumn(0).setCellRenderer(center);
        t.getColumnModel().getColumn(0).setMaxWidth(60);
    }

    private void showInfo(String msg)  { JOptionPane.showMessageDialog(this, msg, "Sukses", JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn(String msg)  { JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}
