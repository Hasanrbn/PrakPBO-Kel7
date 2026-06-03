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
    private JTextField txtId, txtNama, txtAlamat, txtTugas, txtUTS, txtUAS, txtCari;
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
        setMinimumSize(new Dimension(900, 600));
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
        header.setBorder(new EmptyBorder(18, 30, 18, 30));

        JPanel hLeft = new JPanel();
        hLeft.setLayout(new BoxLayout(hLeft, BoxLayout.Y_AXIS));
        hLeft.setOpaque(false);

        JLabel lblTitle = new JLabel("Data Siswa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Kelola data, nilai, dan peringkat siswa secara otomatis");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 210, 255));

        hLeft.add(lblTitle);
        hLeft.add(Box.createVerticalStrut(3));
        hLeft.add(lblSub);
        header.add(hLeft, BorderLayout.WEST);

        // =====================
        // SIDEBAR (kiri) — dibungkus JScrollPane
        // =====================
        JPanel formInner = new JPanel();
        formInner.setLayout(new BoxLayout(formInner, BoxLayout.Y_AXIS));
        formInner.setBackground(CARD);
        formInner.setBorder(new EmptyBorder(20, 16, 20, 16));

        // Form title
        JLabel formTitle = new JLabel("Input Data Siswa");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formInner.add(formTitle);
        formInner.add(makeSeparator());

        // --- Data Diri ---
        addSectionLabel(formInner, "DATA DIRI");
        txtId     = addFormField(formInner, "ID Siswa *", "Contoh: S001");
        txtNama   = addFormField(formInner, "Nama Siswa *", "Nama lengkap siswa");
        txtAlamat = addFormField(formInner, "Alamat", "Kota / Kabupaten");

        formInner.add(createLabel("Jenis Kelamin"));
        formInner.add(Box.createVerticalStrut(4));
        cbJK = new JComboBox<>(new String[]{"L", "P"});
        styleCombo(cbJK);
        cbJK.setAlignmentX(Component.LEFT_ALIGNMENT);
        formInner.add(cbJK);
        formInner.add(Box.createVerticalStrut(14));

        // --- Nilai ---
        addSectionLabel(formInner, "NILAI");
        txtTugas = addFormField(formInner, "Nilai Tugas *", "0 – 100");
        txtUTS   = addFormField(formInner, "Nilai UTS *",   "0 – 100");
        txtUAS   = addFormField(formInner, "Nilai UAS *",   "0 – 100");

        // Preview nilai akhir & grade
        JPanel previewPanel = new JPanel(new GridLayout(2, 2, 8, 6));
        previewPanel.setBackground(new Color(241, 245, 249));
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            new EmptyBorder(10, 12, 10, 12)));
        previewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        previewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        previewPanel.add(makeSmallLabel("Nilai Akhir:"));
        lblNilaiAkhir = new JLabel("-");
        lblNilaiAkhir.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNilaiAkhir.setForeground(PRIMARY);
        previewPanel.add(lblNilaiAkhir);

        previewPanel.add(makeSmallLabel("Grade:"));
        lblGrade = new JLabel("-");
        lblGrade.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGrade.setForeground(SUCCESS);
        previewPanel.add(lblGrade);

        formInner.add(previewPanel);
        formInner.add(Box.createVerticalStrut(18));

        // --- Tombol Aksi ---
        addSectionLabel(formInner, "AKSI");
        JPanel btnGrid = new JPanel(new GridLayout(3, 2, 8, 8));
        btnGrid.setOpaque(false);
        btnGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 132));
        btnGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnTambah    = makeBtn("Tambah", SUCCESS);
        btnEdit      = makeBtn("Edit", WARNING);
        btnHapus     = makeBtn("Hapus", DANGER);
        btnRefresh   = makeBtn("Refresh", INFO);
        btnSortNilai = makeBtn("Sort Nilai", PURPLE);
        JButton btnClear = makeBtn("Reset", NEUTRAL);

        btnGrid.add(btnTambah);
        btnGrid.add(btnEdit);
        btnGrid.add(btnHapus);
        btnGrid.add(btnRefresh);
        btnGrid.add(btnSortNilai);
        btnGrid.add(btnClear);
        formInner.add(btnGrid);
        formInner.add(Box.createVerticalStrut(8));

        // Sidebar dengan scroll
        JScrollPane sidebarScroll = new JScrollPane(formInner,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setPreferredSize(new Dimension(270, 0));
        sidebarScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_CLR));
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(12);
        sidebarScroll.getViewport().setBackground(CARD);

        // =====================
        // TABLE AREA (kanan)
        // =====================
        JPanel tableArea = new JPanel(new BorderLayout());
        tableArea.setBackground(BG);

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(CARD);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(10, 16, 10, 16)));

        // Filter kelas
        JPanel filterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterLeft.setOpaque(false);
        JLabel lblKelasFilter = new JLabel("Filter Kelas:");
        lblKelasFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKelasFilter.setForeground(TEXT_DARK);
        cbKelas = new JComboBox<>();
        cbKelas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbKelas.setPreferredSize(new Dimension(150, 32));
        filterLeft.add(lblKelasFilter);
        filterLeft.add(cbKelas);
        toolbar.add(filterLeft, BorderLayout.WEST);

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchPanel.setOpaque(false);
        JLabel lblCari = new JLabel("Cari:");
        lblCari.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCari.setForeground(TEXT_DARK);
        txtCari = new JTextField(16);
        txtCari.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            new EmptyBorder(5, 9, 5, 9)));
        txtCari.setToolTipText("Cari nama atau ID siswa...");
        btnCari = makeBtn("Cari", INFO);
        searchPanel.add(lblCari);
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);
        toolbar.add(searchPanel, BorderLayout.EAST);

        // Table
        String[] cols = {"#", "ID", "Nama Siswa", "JK", "Alamat", "Kelas",
                         "Tugas", "UTS", "UAS", "Nilai Akhir", "Grade"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane tableScroll = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.getVerticalScrollBar().setUnitIncrement(16);

        tableArea.add(toolbar, BorderLayout.NORTH);
        tableArea.add(tableScroll, BorderLayout.CENTER);

        // =====================
        // STATUS BAR
        // =====================
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(241, 245, 249));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(7, 20, 7, 20)));
        JLabel statusLabel = new JLabel(
            "Sistem Pengelolaan Nilai Siswa  •  Nilai Akhir = (Tugas × 30%) + (UTS × 30%) + (UAS × 40%)");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_MUTED);
        statusBar.add(statusLabel, BorderLayout.WEST);

        // =====================
        // LAYOUT UTAMA
        // =====================
        JPanel content = new JPanel(new BorderLayout());
        content.add(sidebarScroll, BorderLayout.WEST);
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

        java.awt.event.KeyAdapter previewKey = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updatePreview(); }
        };
        txtTugas.addKeyListener(previewKey);
        txtUTS.addKeyListener(previewKey);
        txtUAS.addKeyListener(previewKey);

        txtCari.addActionListener(e -> cariData());
    }

    // =====================
    // PREVIEW NILAI AKHIR
    // =====================
    private void updatePreview() {
        try {
            double tugas = Double.parseDouble(txtTugas.getText());
            double uts   = Double.parseDouble(txtUTS.getText());
            double uas   = Double.parseDouble(txtUAS.getText());
            double na    = GradeHelper.hitungNilaiAkhir(tugas, uts, uas);
            String grade = GradeHelper.hitungGrade(na);
            lblNilaiAkhir.setText(String.format("%.2f", na));
            lblGrade.setText(grade);
            lblGrade.setForeground(getGradeColor(grade));
        } catch (Exception e) {
            lblNilaiAkhir.setText("-");
            lblGrade.setText("-");
            lblGrade.setForeground(SUCCESS);
        }
    }

    // =====================
    // LOAD COMBO KELAS
    // =====================
    private void loadComboKelas() {
        isLoading = true;
        cbKelas.removeAllItems();
        listKelas = new KelasController().getAllKelas();
        for (Kelas k : listKelas) {
            cbKelas.addItem(k.getNamaKelas());
        }
        isLoading = false;
    }

    // =====================
    // CRUD & DATA
    // =====================
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
            s.getJenisKelamin().equals("L") ? "L" : "P",
            s.getAlamat(), s.getNamaKelas(),
            String.format("%.1f", s.getNilaiTugas()),
            String.format("%.1f", s.getNilaiUTS()),
            String.format("%.1f", s.getNilaiUAS()),
            String.format("%.1f", s.getNilaiAkhir()),
            s.getGrade()
        });
    }

    private void tambahData() {
        try {
            if (txtId.getText().trim().isEmpty())    { showWarn("ID Siswa tidak boleh kosong!");    txtId.requestFocus();    return; }
            if (txtNama.getText().trim().isEmpty())   { showWarn("Nama Siswa tidak boleh kosong!"); txtNama.requestFocus();  return; }
            if (txtTugas.getText().trim().isEmpty())  { showWarn("Nilai Tugas tidak boleh kosong!"); txtTugas.requestFocus(); return; }
            if (txtUTS.getText().trim().isEmpty())    { showWarn("Nilai UTS tidak boleh kosong!");   txtUTS.requestFocus();   return; }
            if (txtUAS.getText().trim().isEmpty())    { showWarn("Nilai UAS tidak boleh kosong!");   txtUAS.requestFocus();   return; }

            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double uts   = Double.parseDouble(txtUTS.getText().trim());
            double uas   = Double.parseDouble(txtUAS.getText().trim());

            if (tugas < 0 || tugas > 100) { showWarn("Nilai Tugas harus antara 0 - 100!"); return; }
            if (uts   < 0 || uts   > 100) { showWarn("Nilai UTS harus antara 0 - 100!");   return; }
            if (uas   < 0 || uas   > 100) { showWarn("Nilai UAS harus antara 0 - 100!");   return; }

            if (listKelas == null || listKelas.isEmpty()) { showWarn("Data kelas belum tersedia!"); return; }

            int idx = cbKelas.getSelectedIndex();
            if (idx < 0) { showWarn("Pilih kelas terlebih dahulu!"); return; }

            Siswa s = new Siswa();
            s.setIdSiswa(txtId.getText().trim());
            s.setNamaSiswa(txtNama.getText().trim());
            s.setJenisKelamin(cbJK.getSelectedItem().toString());
            s.setAlamat(txtAlamat.getText().trim());
            s.setIdKelas(listKelas.get(idx).getIdKelas());
            s.setNilaiTugas(tugas);
            s.setNilaiUTS(uts);
            s.setNilaiUAS(uas);
            s.setRanking(0);

            if (controller.tambahSiswa(s)) {
                showInfo("Data siswa berhasil ditambahkan!");
                tampilData(); clearForm();
            } else {
                showError("Gagal menambahkan data.\nID Siswa mungkin sudah ada.");
            }
        } catch (NumberFormatException e) {
            showError("Nilai harus berupa angka!");
        }
    }

    private void editData() {
        if (txtId.getText().trim().isEmpty()) { showWarn("Pilih siswa dari tabel terlebih dahulu!"); return; }
        try {
            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double uts   = Double.parseDouble(txtUTS.getText().trim());
            double uas   = Double.parseDouble(txtUAS.getText().trim());

            if (tugas < 0 || tugas > 100) { showWarn("Nilai Tugas harus antara 0 - 100!"); return; }
            if (uts   < 0 || uts   > 100) { showWarn("Nilai UTS harus antara 0 - 100!");   return; }
            if (uas   < 0 || uas   > 100) { showWarn("Nilai UAS harus antara 0 - 100!");   return; }

            int idx = cbKelas.getSelectedIndex();
            Siswa s = new Siswa();
            s.setIdSiswa(txtId.getText().trim());
            s.setNamaSiswa(txtNama.getText().trim());
            s.setJenisKelamin(cbJK.getSelectedItem().toString().replaceAll(".*?(L|P).*", "$1"));
            s.setAlamat(txtAlamat.getText().trim());
            s.setIdKelas(listKelas.get(idx).getIdKelas());
            s.setNilaiTugas(tugas);
            s.setNilaiUTS(uts);
            s.setNilaiUAS(uas);

            if (controller.updateSiswa(s)) {
                showInfo("Data siswa berhasil diperbarui!");
                tampilData(); clearForm();
            } else {
                showError("Gagal memperbarui data siswa.");
            }
        } catch (NumberFormatException e) {
            showError("Nilai harus berupa angka!");
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

        String id      = model.getValueAt(row, 1).toString();
        int    idKelas = listKelas.get(cbKelas.getSelectedIndex()).getIdKelas();
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
        txtUTS.setText(model.getValueAt(row, 7).toString());
        txtUAS.setText(model.getValueAt(row, 8).toString());
        updatePreview();
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtAlamat.setText("");
        txtTugas.setText(""); txtUTS.setText(""); txtUAS.setText("");
        txtCari.setText("");
        cbJK.setSelectedIndex(0);
        lblNilaiAkhir.setText("-");
        lblGrade.setText("-");
        lblGrade.setForeground(SUCCESS);
        table.clearSelection();
    }

    // =====================
    // STYLE HELPERS
    // =====================
    private JTextField addFormField(JPanel parent, String label, String tip) {
        JLabel lbl = createLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            new EmptyBorder(5, 10, 5, 10)));
        tf.setToolTipText(tip);
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(tf);
        parent.add(Box.createVerticalStrut(12));
        return tf;
    }

    private void addSectionLabel(JPanel parent, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(8));
    }

    private JSeparator makeSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_CLR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        // wrap in panel so BoxLayout respects the max height
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.add(Box.createVerticalStrut(8));
        wrap.add(sep);
        wrap.add(Box.createVerticalStrut(12));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
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
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
    }

    private JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            new EmptyBorder(6, 10, 6, 10)));
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
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY));
        t.getTableHeader().setPreferredSize(new Dimension(0, 38));

        final int gradeCol = 10;

        // Alternating row + grade color renderer
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(TEXT_DARK);
                }
                if (col == gradeCol && val != null && !sel) {
                    c.setBackground(getGradeColor(val.toString()));
                    c.setForeground(Color.WHITE);
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel) c).setFont(getFont().deriveFont(Font.BOLD));
                } else if (col == gradeCol && sel) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else if (col == 0 || col == 3 || col == 6 || col == 7 || col == 8 || col == 9) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });

        // Column widths
        int[] widths = {40, 70, 160, 50, 120, 90, 60, 60, 60, 80, 60};
        for (int i = 0; i < widths.length && i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
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

    private void showInfo(String msg)  { JOptionPane.showMessageDialog(this, msg, "Sukses",     JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn(String msg)  { JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);     }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error",      JOptionPane.ERROR_MESSAGE);       }
}
