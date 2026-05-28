package view;

import controller.KelasController;
import controller.SiswaController;
import model.Kelas;
import model.Siswa;
import utils.GradeHelper;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormSiswa extends JFrame {

    // === KOMPONEN ===
    private JTextField txtId, txtNama, txtAlamat, txtTugas, txtUjian, txtCari;
    private JLabel lblNilaiAkhir, lblGrade;
    private JComboBox<String> cbJK, cbKelas;
    private JButton btnTambah, btnEdit, btnHapus, btnCari, btnRefresh, btnSortNilai;
    private JTable table;
    private DefaultTableModel model;
    private SiswaController controller;
    private List<Kelas> listKelas;
    private boolean isLoading = false;

    // === WARNA ===
    private final Color PRIMARY    = new Color(67, 97, 238);
    private final Color SUCCESS    = new Color(34, 197, 94);
    private final Color WARNING    = new Color(234, 179, 8);
    private final Color DANGER     = new Color(239, 68, 68);
    private final Color INFO       = new Color(14, 165, 233);
    private final Color PURPLE     = new Color(139, 92, 246);
    private final Color NEUTRAL    = new Color(100, 116, 139);
    private final Color BG         = new Color(248, 250, 252);
    private final Color CARD       = Color.WHITE;
    private final Color TEXT_DARK  = new Color(30, 41, 59);
    private final Color TEXT_MUTED = new Color(100, 116, 139);
    private final Color BORDER_CLR = new Color(226, 232, 240);
    private final Color GRADE_A    = new Color(34, 197, 94);
    private final Color GRADE_B    = new Color(14, 165, 233);
    private final Color GRADE_C    = new Color(234, 179, 8);
    private final Color GRADE_D    = new Color(249, 115, 22);
    private final Color GRADE_E    = new Color(239, 68, 68);

    public FormSiswa() {
        controller = new SiswaController();
        setTitle("Data Siswa - Sistem Pengelolaan Nilai");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(BG);
        initComponent();
        loadComboKelas();
        tampilData();
    }

    private void initComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BG);

        // =====================
        // HEADER
        // =====================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel hLeft = new JPanel();
        hLeft.setLayout(new BoxLayout(hLeft, BoxLayout.Y_AXIS));
        hLeft.setOpaque(false);

        JLabel lblTitle = new JLabel("📚  Data Siswa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Kelola data, nilai, dan peringkat siswa secara otomatis");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 210, 255));

        hLeft.add(lblTitle);
        hLeft.add(Box.createVerticalStrut(4));
        hLeft.add(lblSub);
        header.add(hLeft, BorderLayout.WEST);

        // =====================
        // FORM AREA (LEFT SIDEBAR)
        // =====================
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBackground(CARD);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_CLR));

        JPanel formInner = new JPanel();
        formInner.setLayout(new BoxLayout(formInner, BoxLayout.Y_AXIS));
        formInner.setBackground(CARD);
        formInner.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form title
        JLabel formTitle = new JLabel("Input Data Siswa");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formInner.add(formTitle);
        formInner.add(Box.createVerticalStrut(16));

        txtId    = addFormField(formInner, "ID Siswa", "Contoh: S004");
        txtNama  = addFormField(formInner, "Nama Siswa", "Nama lengkap");
        txtAlamat= addFormField(formInner, "Alamat", "Kota/Kabupaten");

        // Jenis Kelamin
        formInner.add(createLabel("Jenis Kelamin"));
        formInner.add(Box.createVerticalStrut(4));
        cbJK = new JComboBox<>(new String[]{"L","P"});
        styleCombo(cbJK);
        formInner.add(cbJK);
        formInner.add(Box.createVerticalStrut(12));

        txtTugas = addFormField(formInner, "Nilai Tugas (0-100)", "0 - 100");
        txtUjian = addFormField(formInner, "Nilai Ujian (0-100)", "0 - 100");

        // Preview nilai akhir & grade
        formInner.add(Box.createVerticalStrut(6));
        JPanel previewPanel = new JPanel(new GridLayout(2,2,8,4));
        previewPanel.setBackground(new Color(241,245,249));
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(8,10,8,10)));
        previewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        previewPanel.add(makeSmallLabel("Nilai Akhir:"));
        lblNilaiAkhir = new JLabel("-");
        lblNilaiAkhir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNilaiAkhir.setForeground(PRIMARY);
        previewPanel.add(lblNilaiAkhir);
        previewPanel.add(makeSmallLabel("Grade:"));
        lblGrade = new JLabel("-");
        lblGrade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGrade.setForeground(SUCCESS);
        previewPanel.add(lblGrade);
        formInner.add(previewPanel);
        formInner.add(Box.createVerticalStrut(16));

        // Buttons
        JPanel btnGrid = new JPanel(new GridLayout(3, 2, 8, 8));
        btnGrid.setOpaque(false);
        btnGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        btnTambah    = makeBtn("＋ Tambah",  SUCCESS);
        btnEdit      = makeBtn("✎ Edit",     WARNING);
        btnHapus     = makeBtn("✕ Hapus",    DANGER);
        btnRefresh   = makeBtn("↺ Refresh",  INFO);
        btnSortNilai = makeBtn("▲ Sort Nilai", PURPLE);
        JButton btnClear = makeBtn("↺ Reset", NEUTRAL);
        btnGrid.add(btnTambah);
        btnGrid.add(btnEdit);
        btnGrid.add(btnHapus);
        btnGrid.add(btnRefresh);
        btnGrid.add(btnSortNilai);
        btnGrid.add(btnClear);
        formInner.add(btnGrid);

        sidebar.add(formInner, BorderLayout.NORTH);

        // =====================
        // TABLE AREA (RIGHT)
        // =====================
        JPanel tableArea = new JPanel(new BorderLayout());
        tableArea.setBackground(BG);

        // Toolbar (filter + search)
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(CARD);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(12, 20, 12, 20)
        ));

        // Kelas filter
        JPanel filterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterLeft.setOpaque(false);
        JLabel lblKelas = new JLabel("Filter Kelas:");
        lblKelas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKelas.setForeground(TEXT_DARK);
        cbKelas = new JComboBox<>();
        cbKelas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbKelas.setPreferredSize(new Dimension(160, 34));
        filterLeft.add(lblKelas);
        filterLeft.add(cbKelas);
        toolbar.add(filterLeft, BorderLayout.WEST);

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);
        txtCari = new JTextField(18);
        txtCari.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(6,10,6,10)));
        txtCari.setToolTipText("Cari nama atau ID siswa...");
        btnCari = makeBtn("🔍 Cari", INFO);
        searchPanel.add(new JLabel("Cari:"));
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);
        toolbar.add(searchPanel, BorderLayout.EAST);

        // Table
        String[] cols = {"#", "ID", "Nama Siswa", "JK", "Alamat", "Kelas",
                         "Tugas", "Ujian", "Nilai Akhir", "Grade"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        tableArea.add(toolbar, BorderLayout.NORTH);
        tableArea.add(scroll, BorderLayout.CENTER);

        // =====================
        // STATUS BAR
        // =====================
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(241, 245, 249));
        statusBar.setBorder(new EmptyBorder(8, 20, 8, 20));
        JLabel statusLabel = new JLabel("Sistem Pengelolaan Nilai Siswa  •  Nilai Akhir = Tugas×40% + Ujian×60%");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_MUTED);
        statusBar.add(statusLabel, BorderLayout.WEST);

        // =====================
        // CONTENT SPLIT
        // =====================
        JPanel content = new JPanel(new BorderLayout());
        content.add(sidebar, BorderLayout.WEST);
        content.add(tableArea, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        add(mainPanel);

        // =====================
        // ACTIONS
        // =====================
        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnCari.addActionListener(e -> cariData());
        btnRefresh.addActionListener(e -> { clearForm(); tampilData(); });
        btnSortNilai.addActionListener(e -> sortData());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> pilihData());
        cbKelas.addActionListener(e -> { if (!isLoading) tampilData(); });

        // Live preview nilai akhir & grade
        java.awt.event.KeyAdapter previewKey = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updatePreview(); }
        };
        txtTugas.addKeyListener(previewKey);
        txtUjian.addKeyListener(previewKey);

        // Enter cari
        txtCari.addActionListener(e -> cariData());
    }

    private void updatePreview() {
        try {
            double t = Double.parseDouble(txtTugas.getText());
            double u = Double.parseDouble(txtUjian.getText());
            if (t < 0 || t > 100 || u < 0 || u > 100) throw new NumberFormatException();
            double na = GradeHelper.hitungNilaiAkhir(t, u);
            String g = GradeHelper.hitungGrade(na);
            lblNilaiAkhir.setText(String.format("%.1f", na));
            lblGrade.setText(g + " - " + GradeHelper.getKeterangan(g));
            lblGrade.setForeground(getGradeColor(g));
        } catch (NumberFormatException ex) {
            lblNilaiAkhir.setText("-");
            lblGrade.setText("-");
            lblGrade.setForeground(TEXT_MUTED);
        }
    }

    private void loadComboKelas() {
        isLoading = true;
        cbKelas.removeAllItems();
        listKelas = new KelasController().getAllKelas();
        for (Kelas k : listKelas) cbKelas.addItem(k.getNamaKelas());
        isLoading = false;
    }

    private void tampilData() {
        model.setRowCount(0);
        if (listKelas == null || listKelas.isEmpty()) return;
        int idx = cbKelas.getSelectedIndex();
        if (idx < 0) return;
        int idKelas = listKelas.get(idx).getIdKelas();
        for (Siswa s : controller.getByKelas(idKelas)) addRow(s);
    }

    private void sortData() {
        model.setRowCount(0);
        for (Siswa s : controller.sortNilai()) addRow(s);
    }

    private void addRow(Siswa s) {
        model.addRow(new Object[]{
            s.getRanking(), s.getIdSiswa(), s.getNamaSiswa(),
            s.getJenisKelamin().equals("L") ? "👦 L" : "👧 P",
            s.getAlamat(), s.getNamaKelas(),
            String.format("%.1f", s.getNilaiTugas()),
            String.format("%.1f", s.getNilaiUjian()),
            String.format("%.1f", s.getNilaiAkhir()),
            s.getGrade()
        });
    }

    private void tambahData() {
        try {
            // Validasi wajib
            if (txtId.getText().trim().isEmpty())   { showWarn("ID Siswa tidak boleh kosong!"); txtId.requestFocus(); return; }
            if (txtNama.getText().trim().isEmpty())  { showWarn("Nama Siswa tidak boleh kosong!"); txtNama.requestFocus(); return; }
            if (txtTugas.getText().trim().isEmpty()) { showWarn("Nilai Tugas tidak boleh kosong!"); txtTugas.requestFocus(); return; }
            if (txtUjian.getText().trim().isEmpty()) { showWarn("Nilai Ujian tidak boleh kosong!"); txtUjian.requestFocus(); return; }

            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double ujian = Double.parseDouble(txtUjian.getText().trim());
            if (tugas < 0 || tugas > 100) { showWarn("Nilai Tugas harus antara 0 - 100!"); return; }
            if (ujian < 0 || ujian > 100) { showWarn("Nilai Ujian harus antara 0 - 100!"); return; }

            int idx = cbKelas.getSelectedIndex();
            Siswa s = new Siswa();
            s.setIdSiswa(txtId.getText().trim());
            s.setNamaSiswa(txtNama.getText().trim());
            s.setJenisKelamin(cbJK.getSelectedItem().toString());
            s.setAlamat(txtAlamat.getText().trim());
            s.setIdKelas(listKelas.get(idx).getIdKelas());
            s.setNilaiTugas(tugas);
            s.setNilaiUjian(ujian);
            s.setRanking(0);

            if (controller.tambahSiswa(s)) {
                showInfo("Data siswa berhasil ditambahkan!");
                tampilData(); clearForm();
            } else {
                showError("Gagal menambahkan data.\nID Siswa mungkin sudah ada.");
            }
        } catch (NumberFormatException e) {
            showError("Nilai Tugas dan Nilai Ujian harus berupa angka!");
        }
    }

    private void editData() {
        if (txtId.getText().trim().isEmpty()) { showWarn("Pilih siswa dari tabel terlebih dahulu!"); return; }
        try {
            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double ujian = Double.parseDouble(txtUjian.getText().trim());
            if (tugas < 0 || tugas > 100) { showWarn("Nilai Tugas harus antara 0 - 100!"); return; }
            if (ujian < 0 || ujian > 100) { showWarn("Nilai Ujian harus antara 0 - 100!"); return; }

            int idx = cbKelas.getSelectedIndex();
            Siswa s = new Siswa();
            s.setIdSiswa(txtId.getText().trim());
            s.setNamaSiswa(txtNama.getText().trim());
            s.setJenisKelamin(cbJK.getSelectedItem().toString().replaceAll(".*?(L|P).*", "$1"));
            s.setAlamat(txtAlamat.getText().trim());
            s.setIdKelas(listKelas.get(idx).getIdKelas());
            s.setNilaiTugas(tugas);
            s.setNilaiUjian(ujian);

            if (controller.updateSiswa(s)) {
                showInfo("Data siswa berhasil diperbarui!");
                tampilData(); clearForm();
            } else {
                showError("Gagal memperbarui data siswa.");
            }
        } catch (NumberFormatException e) {
            showError("Nilai Tugas dan Nilai Ujian harus berupa angka!");
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row < 0) { showWarn("Pilih siswa dari tabel terlebih dahulu!"); return; }
        String nama = model.getValueAt(row, 2).toString();
        int opt = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data siswa:\n" + nama + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;

        String id = model.getValueAt(row, 1).toString();
        int idKelas = listKelas.get(cbKelas.getSelectedIndex()).getIdKelas();
        if (controller.hapusSiswa(id, idKelas)) {
            showInfo("Data siswa berhasil dihapus!");
            tampilData(); clearForm();
        } else {
            showError("Gagal menghapus data siswa.");
        }
    }

    private void cariData() {
        String kw = txtCari.getText().trim();
        if (kw.isEmpty()) { tampilData(); return; }
        model.setRowCount(0);
        for (Siswa s : controller.cariSiswa(kw)) addRow(s);
    }

    private void pilihData() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtId.setText(model.getValueAt(row, 1).toString());
        txtNama.setText(model.getValueAt(row, 2).toString());
        String jk = model.getValueAt(row, 3).toString();
        cbJK.setSelectedItem(jk.contains("L") ? "L" : "P");
        txtAlamat.setText(model.getValueAt(row, 4).toString());
        txtTugas.setText(model.getValueAt(row, 6).toString());
        txtUjian.setText(model.getValueAt(row, 7).toString());
        updatePreview();
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtAlamat.setText("");
        txtTugas.setText(""); txtUjian.setText(""); txtCari.setText("");
        cbJK.setSelectedIndex(0);
        lblNilaiAkhir.setText("-");
        lblGrade.setText("-");
        table.clearSelection();
    }

    // === STYLE HELPERS ===
    private JTextField addFormField(JPanel parent, String label, String tip) {
        JLabel lbl = createLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(5,10,5,10)));
        tf.setToolTipText(tip);
        parent.add(tf);
        parent.add(Box.createVerticalStrut(10));
        return tf;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_MUTED);
        return l;
    }

    private JLabel makeSmallLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(TEXT_MUTED);
        return l;
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    private JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            new EmptyBorder(7, 12, 7, 12)
        ));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(color.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(color); }
        });
        return b;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(38);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setGridColor(new Color(241, 245, 249));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(TEXT_DARK);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY));
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Grade column renderer
        int gradeCol = 9;
        t.getColumnModel().getColumn(gradeCol).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (val != null && !sel) {
                    String g = val.toString();
                    c.setForeground(Color.WHITE);
                    c.setBackground(getGradeColor(g));
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else if (!sel) {
                    c.setForeground(TEXT_DARK);
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        // Center aligns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i : new int[]{0, 3, 6, 7, 8}) {
            t.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Column widths
        int[] widths = {45, 70, 160, 55, 120, 100, 65, 65, 90, 65};
        for (int i = 0; i < widths.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Alternating row color
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(TEXT_DARK);
                }
                // Re-apply grade color
                if (col == gradeCol && val != null && !sel) {
                    c.setBackground(getGradeColor(val.toString()));
                    c.setForeground(Color.WHITE);
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel)c).setFont(getFont().deriveFont(Font.BOLD));
                } else if (col == 0 || col == 6 || col == 7 || col == 8) {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                } else if (col == 3) {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                }
                return c;
            }
        });
    }

    private Color getGradeColor(String g) {
        switch (g) {
            case "A": return GRADE_A;
            case "B": return GRADE_B;
            case "C": return GRADE_C;
            case "D": return GRADE_D;
            default:  return GRADE_E;
        }
    }

    private void showInfo(String msg)  { JOptionPane.showMessageDialog(this, msg, "Sukses", JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn(String msg)  { JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}
