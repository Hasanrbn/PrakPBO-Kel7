package view;

import controller.StatistikController;
import model.Statistik;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormStatistik extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private StatistikController controller;

    private final Color PRIMARY    = new Color(67, 97, 238);
    private final Color SUCCESS    = new Color(34, 197, 94);
    private final Color BG         = new Color(248, 250, 252);
    private final Color CARD       = Color.WHITE;
    private final Color TEXT_DARK  = new Color(30, 41, 59);
    private final Color TEXT_MUTED = new Color(100, 116, 139);
    private final Color BORDER_CLR = new Color(226, 232, 240);

    public FormStatistik() {
        controller = new StatistikController();
        setTitle("Statistik Nilai Per Kelas");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponent();
        tampilData();
    }

    private void initComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(16, 185, 129)); // emerald
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel hText = new JPanel();
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        hText.setOpaque(false);

        JLabel lblTitle = new JLabel("📊  Statistik Nilai Siswa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Ringkasan statistik nilai per kelas secara otomatis");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 250, 230));

        hText.add(lblTitle);
        hText.add(Box.createVerticalStrut(4));
        hText.add(lblSub);
        header.add(hText, BorderLayout.WEST);

        JButton btnRefresh = makeBtn("↺ Refresh", PRIMARY);
        btnRefresh.addActionListener(e -> { model.setRowCount(0); tampilData(); });
        header.add(btnRefresh, BorderLayout.EAST);

        // Table
        model = new DefaultTableModel(
            new String[]{"Kelas", "Jumlah Siswa", "Rata-rata", "Nilai Tertinggi", "Nilai Terendah"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CARD);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD);
        tableCard.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel tableTitle = new JLabel("Data Statistik Per Kelas");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(TEXT_DARK);
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        // Footer note
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(241, 245, 249));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel note = new JLabel("ℹ  Nilai Akhir = (Nilai Tugas × 40%) + (Nilai Ujian × 60%)  |  Grade A≥85  B≥75  C≥65  D≥50  E<50");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(TEXT_MUTED);
        footer.add(note);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tableCard, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void tampilData() {
        List<Statistik> list = controller.getStatistik();
        for (Statistik s : list) {
            model.addRow(new Object[]{
                s.getNamaKelas(),
                s.getJumlahSiswa() + " siswa",
                String.format("%.2f", s.getRataRata()),
                String.format("%.2f", s.getNilaiTertinggi()),
                String.format("%.2f", s.getNilaiTerendah())
            });
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(42);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(241, 245, 249));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(16,185,129)));
        t.getTableHeader().setPreferredSize(new Dimension(0, 44));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < 5; i++) t.getColumnModel().getColumn(i).setCellRenderer(center);

        // Highlight rata-rata column
        t.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    c.setForeground(new Color(16, 185, 129));
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? CARD : new Color(248, 250, 252));
                    c.setForeground(col == 2 ? new Color(16,185,129) : TEXT_DARK);
                    if (col == 2) setFont(getFont().deriveFont(Font.BOLD));
                }
                if (col > 0) ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    }

    private JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker()), new EmptyBorder(8, 18, 8, 18)));
        return b;
    }
}
