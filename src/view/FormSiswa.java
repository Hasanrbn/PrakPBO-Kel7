package view;

import controller.KelasController;
import controller.SiswaController;
import model.Kelas;
import model.Siswa;
import utils.DatabaseWorker;
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
    private JLabel lblNilaiAkhir, lblGrade, lblStatus;
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

    // Warna ranking
    private final Color RANK_GOLD   = new Color(255, 193, 7);
    private final Color RANK_SILVER = new Color(176, 190, 197);
    private final Color RANK_BRONZE = new Color(188, 120, 56);
    private final Color RANK_BG     = new Color(241, 245, 249);

    public FormSiswa() {
        controller = new SiswaController();
        setTitle("Data Siswa - Sistem Pengelolaan Nilai");
        setSize(1380, 780);
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

        JLabel formTitle = new JLabel("Input Data Siswa");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formInner.add(formTitle);
        formInner.add(Box.createVerticalStrut(16));

        txtId    = addFormField(formInner, "ID Siswa", "Contoh: S004");
        txtNama  = addFormField(formInner, "Nama Siswa", "Nama lengkap");
        txtAlamat= addFormField(formInner, "Alamat", "Kota/Kabupaten");

        formInner.add(createLabel("Jenis Kelamin"));
        formInner.add(Box.createVerticalStrut(4));
        cbJK = new JComboBox<>(new String[]{"L","P"});
        styleCombo(cbJK);
        formInner.add(cbJK);
        formInner.add(Box.createVerticalStrut(12));

        txtTugas = addFormField(formInner, "Nilai Tugas", "0 - 100");
        txtUTS   = addFormField(formInner, "Nilai UTS", "0 - 100");
        txtUAS   = addFormField(formInner, "Nilai UAS", "0 - 100");

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
        btnTambah    = makeBtn("Tambah", SUCCESS);
        btnEdit      = makeBtn("Edit", WARNING);
        btnHapus     = makeBtn("Hapus", DANGER);
        btnRefresh   = makeBtn("Refresh", INFO);
        btnSortNilai = makeBtn("🏆 Ranking", PURPLE);
        JButton btnClear = makeBtn("Reset", NEUTRAL);
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

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(CARD);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(12, 20, 12, 20)
        ));

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

        // === KOLOM TABEL: "#" = nomor urut, "Ranking" = peringkat nilai ===
        // Kolom index 0  = "#"       (nomor urut baris, 1,2,3,...)
        // Kolom index 11 = "Ranking" (peringkat berdasarkan nilai akhir tertinggi)
        String[] cols = {"#", "ID", "Nama Siswa", "JK", "Alamat", "Kelas",
                         "Tugas", "UTS", "UAS", "Nilai Akhir", "Grade", "Ranking"};
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
        lblStatus = new JLabel("Siap");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(TEXT_MUTED);
        JLabel rumusLabel = new JLabel("Nilai Akhir = (Tugas×30%) + (UTS×30%) + (UAS×40%)");
        rumusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rumusLabel.setForeground(TEXT_MUTED);
        statusBar.add(lblStatus, BorderLayout.WEST);
        statusBar.add(rumusLabel, BorderLayout.EAST);

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

        java.awt.event.KeyAdapter previewKey = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updatePreview(); }
        };
        txtTugas.addKeyListener(previewKey);
        txtUTS.addKeyListener(previewKey);
        txtUAS.addKeyListener(previewKey);
        txtCari.addActionListener(e -> cariData());
    }

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
        }
    }

    // =====================
    // MULTITHREADING: loadComboKelas
    // =====================
    private void loadComboKelas() {
        setStatus("Memuat data kelas...", TEXT_MUTED);

        new DatabaseWorker<List<Kelas>>(
            () -> new KelasController().getAllKelas(),
            kelasList -> {
                isLoading = true;
                cbKelas.removeAllItems();
                listKelas = kelasList;
                for (Kelas k : kelasList) cbKelas.addItem(k.getNamaKelas());
                isLoading = false;
                setStatus("Data kelas dimuat (" + kelasList.size() + " kelas)", SUCCESS);
                tampilData();
            },
            err -> {
                setStatus("Gagal memuat kelas: " + err.getMessage(), DANGER);
            }
        ).execute();
    }

    // =====================
    // MULTITHREADING: tampilData
    // =====================
    private void tampilData() {
        if (listKelas == null || listKelas.isEmpty()) return;
        int idx = cbKelas.getSelectedIndex();
        if (idx < 0) return;
        int idKelas = listKelas.get(idx).getIdKelas();

        setStatus("Memuat data siswa...", TEXT_MUTED);
        setButtonsEnabled(false);

        new DatabaseWorker<List<Siswa>>(
            () -> controller.getByKelas(idKelas),
            siswaList -> {
                model.setRowCount(0);
                // Hitung ranking berdasarkan nilai akhir (descending)
                List<Siswa> ranked = hitungRanking(siswaList);
                int no = 1;
                for (Siswa s : ranked) addRow(s, no++);
                setStatus("Menampilkan " + siswaList.size() + " siswa  [Thread: EDT]", SUCCESS);
                setButtonsEnabled(true);
            },
            err -> {
                setStatus("Gagal memuat data: " + err.getMessage(), DANGER);
                setButtonsEnabled(true);
            }
        ).execute();
    }

    // =====================
    // MULTITHREADING: sortData — sort & tampilkan ranking global
    // =====================
    private void sortData() {
        setStatus("Mengurutkan data berdasarkan nilai...", TEXT_MUTED);
        setButtonsEnabled(false);

        new DatabaseWorker<List<Siswa>>(
            () -> controller.sortNilai(),
            siswaList -> {
                model.setRowCount(0);
                List<Siswa> ranked = hitungRanking(siswaList);
                int no = 1;
                for (Siswa s : ranked) addRow(s, no++);
                setStatus("Data diurutkan berdasarkan nilai akhir (tertinggi ke terendah)  [Thread: EDT]", SUCCESS);
                setButtonsEnabled(true);
            },
            err -> {
                setStatus("Gagal mengurutkan: " + err.getMessage(), DANGER);
                setButtonsEnabled(true);
            }
        ).execute();
    }

    /**
     * Menghitung ranking berdasarkan nilai_akhir (descending).
     * Nilai yang sama mendapat ranking yang sama (dense ranking).
     */
    private List<Siswa> hitungRanking(List<Siswa> list) {
        // Urutkan berdasarkan nilai akhir descending
        list.sort((a, b) -> Double.compare(b.getNilaiAkhir(), a.getNilaiAkhir()));
        int rank = 1;
        for (int i = 0; i < list.size(); i++) {
            if (i > 0 && list.get(i).getNilaiAkhir() == list.get(i-1).getNilaiAkhir()) {
                list.get(i).setRanking(list.get(i-1).getRanking());
            } else {
                list.get(i).setRanking(rank);
            }
            rank++;
        }
        return list;
    }

    // =====================
    // MULTITHREADING: tambahData
    // =====================
    private void tambahData() {
        try {
            if (txtId.getText().trim().isEmpty())   { showWarn("ID Siswa tidak boleh kosong!"); txtId.requestFocus(); return; }
            if (txtNama.getText().trim().isEmpty())  { showWarn("Nama Siswa tidak boleh kosong!"); txtNama.requestFocus(); return; }
            if (txtTugas.getText().trim().isEmpty()) { showWarn("Nilai Tugas tidak boleh kosong!"); txtTugas.requestFocus(); return; }
            if (txtUTS.getText().trim().isEmpty())   { showWarn("Nilai UTS tidak boleh kosong!"); txtUTS.requestFocus(); return; }
            if (txtUAS.getText().trim().isEmpty())   { showWarn("Nilai UAS tidak boleh kosong!"); txtUAS.requestFocus(); return; }

            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double uts   = Double.parseDouble(txtUTS.getText().trim());
            double uas   = Double.parseDouble(txtUAS.getText().trim());
            if (tugas < 0 || tugas > 100) { showWarn("Nilai Tugas harus antara 0 - 100!"); return; }
            if (uts < 0   || uts > 100)   { showWarn("Nilai UTS harus antara 0 - 100!"); return; }
            if (uas < 0   || uas > 100)   { showWarn("Nilai UAS harus antara 0 - 100!"); return; }

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

            setStatus("Menyimpan data siswa...", TEXT_MUTED);
            setButtonsEnabled(false);

            new DatabaseWorker<Boolean>(
                () -> controller.tambahSiswa(s),
                berhasil -> {
                    setButtonsEnabled(true);
                    if (berhasil) {
                        showInfo("Data siswa berhasil ditambahkan!");
                        setStatus("Siswa " + s.getNamaSiswa() + " berhasil ditambahkan", SUCCESS);
                        tampilData(); clearForm();
                    } else {
                        showError("Gagal menambahkan data.\nID Siswa mungkin sudah ada.");
                        setStatus("Gagal tambah siswa", DANGER);
                    }
                },
                err -> {
                    setButtonsEnabled(true);
                    showError("Error: " + err.getMessage());
                    setStatus("Error: " + err.getMessage(), DANGER);
                }
            ).execute();

        } catch (NumberFormatException e) {
            showError("Nilai Tugas, UTS, dan UAS harus berupa angka!");
        }
    }

    // =====================
    // MULTITHREADING: editData
    // =====================
    private void editData() {
        if (txtId.getText().trim().isEmpty()) { showWarn("Pilih siswa dari tabel terlebih dahulu!"); return; }
        try {
            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double uts   = Double.parseDouble(txtUTS.getText().trim());
            double uas   = Double.parseDouble(txtUAS.getText().trim());
            if (tugas < 0 || tugas > 100) { showWarn("Nilai Tugas harus antara 0 - 100!"); return; }
            if (uts < 0   || uts > 100)   { showWarn("Nilai UTS harus antara 0 - 100!"); return; }
            if (uas < 0   || uas > 100)   { showWarn("Nilai UAS harus antara 0 - 100!"); return; }

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

            setStatus("Memperbarui data siswa...", TEXT_MUTED);
            setButtonsEnabled(false);

            new DatabaseWorker<Boolean>(
                () -> controller.updateSiswa(s),
                berhasil -> {
                    setButtonsEnabled(true);
                    if (berhasil) {
                        showInfo("Data siswa berhasil diperbarui!");
                        setStatus("Siswa " + s.getNamaSiswa() + " berhasil diperbarui", SUCCESS);
                        tampilData(); clearForm();
                    } else {
                        showError("Gagal memperbarui data siswa.");
                        setStatus("Gagal update siswa", DANGER);
                    }
                },
                err -> {
                    setButtonsEnabled(true);
                    showError("Error: " + err.getMessage());
                    setStatus("Error: " + err.getMessage(), DANGER);
                }
            ).execute();

        } catch (NumberFormatException e) {
            showError("Nilai harus berupa angka!");
        }
    }

    // =====================
    // MULTITHREADING: hapusData
    // =====================
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

        setStatus("Menghapus data siswa...", TEXT_MUTED);
        setButtonsEnabled(false);

        new DatabaseWorker<Boolean>(
            () -> controller.hapusSiswa(id, idKelas),
            berhasil -> {
                setButtonsEnabled(true);
                if (berhasil) {
                    showInfo("Data siswa berhasil dihapus!");
                    setStatus("Siswa " + nama + " berhasil dihapus", SUCCESS);
                    tampilData(); clearForm();
                } else {
                    showError("Gagal menghapus data siswa.");
                    setStatus("Gagal hapus siswa", DANGER);
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
    // MULTITHREADING: cariData
    // =====================
    private void cariData() {
        String kw = txtCari.getText().trim();
        if (kw.isEmpty()) { tampilData(); return; }

        setStatus("Mencari siswa dengan kata kunci: " + kw + "...", TEXT_MUTED);

        new DatabaseWorker<List<Siswa>>(
            () -> controller.cariSiswa(kw),
            siswaList -> {
                model.setRowCount(0);
                List<Siswa> ranked = hitungRanking(siswaList);
                int no = 1;
                for (Siswa s : ranked) addRow(s, no++);
                setStatus("Ditemukan " + siswaList.size() + " siswa untuk \"" + kw + "\"", SUCCESS);
            },
            err -> setStatus("Gagal mencari: " + err.getMessage(), DANGER)
        ).execute();
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

    /**
     * Tambah baris ke tabel.
     * @param s   objek Siswa (sudah memiliki ranking yang dihitung di hitungRanking)
     * @param no  nomor urut baris (1, 2, 3, ...)
     */
    private void addRow(Siswa s, int no) {
        String rankLabel = getRankLabel(s.getRanking());
        model.addRow(new Object[]{
            no,                                        // col 0  = "#" (nomor urut)
            s.getIdSiswa(),                            // col 1  = ID
            s.getNamaSiswa(),                          // col 2  = Nama
            s.getJenisKelamin().equals("L") ? "👦 L" : "👧 P", // col 3 = JK
            s.getAlamat(),                             // col 4  = Alamat
            s.getNamaKelas(),                          // col 5  = Kelas
            String.format("%.1f", s.getNilaiTugas()),  // col 6  = Tugas
            String.format("%.1f", s.getNilaiUTS()),    // col 7  = UTS
            String.format("%.1f", s.getNilaiUAS()),    // col 8  = UAS
            String.format("%.1f", s.getNilaiAkhir()),  // col 9  = Nilai Akhir
            s.getGrade(),                              // col 10 = Grade
            rankLabel                                  // col 11 = Ranking
        });
    }

    /**
     * Konversi angka ranking ke label dengan emoji medali.
     */
    private String getRankLabel(int rank) {
        switch (rank) {
            case 1: return "🥇 #1";
            case 2: return "🥈 #2";
            case 3: return "🥉 #3";
            default: return "#" + rank;
        }
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtAlamat.setText("");
        txtTugas.setText(""); txtUTS.setText(""); txtUAS.setText("");
        txtCari.setText("");
        cbJK.setSelectedIndex(0);
        lblNilaiAkhir.setText("-");
        lblGrade.setText("-");
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
        btnRefresh.setEnabled(enabled);
        btnSortNilai.setEnabled(enabled);
        btnCari.setEnabled(enabled);
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
        b.setForeground(Color.BLACK);
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

        final int COL_GRADE  = 10;
        final int COL_RANK   = 11;

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(TEXT_DARK);
                }

                // Kolom Grade — background warna sesuai grade
                if (col == COL_GRADE && val != null && !sel) {
                    c.setBackground(getGradeColor(val.toString()));
                    c.setForeground(Color.WHITE);
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel)c).setFont(getFont().deriveFont(Font.BOLD));
                }
                // Kolom Ranking — warna medali untuk top 3
                else if (col == COL_RANK && val != null && !sel) {
                    String rankStr = val.toString();
                    if (rankStr.startsWith("🥇")) {
                        c.setBackground(new Color(255, 248, 220)); // gold tint
                        c.setForeground(new Color(180, 120, 0));
                        ((JLabel)c).setFont(getFont().deriveFont(Font.BOLD));
                    } else if (rankStr.startsWith("🥈")) {
                        c.setBackground(new Color(240, 242, 244)); // silver tint
                        c.setForeground(new Color(100, 116, 139));
                        ((JLabel)c).setFont(getFont().deriveFont(Font.BOLD));
                    } else if (rankStr.startsWith("🥉")) {
                        c.setBackground(new Color(251, 236, 221)); // bronze tint
                        c.setForeground(new Color(140, 80, 30));
                        ((JLabel)c).setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                        c.setForeground(TEXT_MUTED);
                    }
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                }
                // Kolom yang center-aligned
                else if (col == 0 || col == 6 || col == 7 || col == 8 || col == 9 || col == 3) {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });

        // Lebar kolom: #, ID, Nama, JK, Alamat, Kelas, Tugas, UTS, UAS, NilaiAkhir, Grade, Ranking
        int[] widths = {40, 65, 155, 55, 115, 95, 60, 60, 60, 85, 60, 80};
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

    private void showInfo(String msg)  { JOptionPane.showMessageDialog(this, msg, "Sukses", JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn(String msg)  { JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}
