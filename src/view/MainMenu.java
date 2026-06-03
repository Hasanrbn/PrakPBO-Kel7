package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private JButton btnSiswa, btnKelas, btnStatistik, btnAbout, btnExit;

    private final Color BG       = new Color(248, 250, 252);
    private final Color CARD     = Color.WHITE;
    private final Color PRIMARY  = new Color(67, 97, 238);
    private final Color PURPLE   = new Color(139, 92, 246);
    private final Color EMERALD  = new Color(16, 185, 129);
    private final Color AMBER    = new Color(245, 158, 11);
    private final Color DANGER   = new Color(239, 68, 68);
    private final Color TXT      = new Color(30, 41, 59);
    private final Color MUTED    = new Color(100, 116, 139);
    private final Color BORDER   = new Color(226, 232, 240);

    public MainMenu() {
        setTitle("Sistem Pengelolaan Nilai Siswa");
        setSize(920, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(BG);
        initComponent();
    }

    private void initComponent() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(BG);

        // =====================
        // HEADER GRADIENT
        // =====================
        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY, getWidth(), getHeight(), PURPLE);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setBorder(new EmptyBorder(28, 35, 28, 35));
        header.setPreferredSize(new Dimension(0, 120));

        JPanel hText = new JPanel();
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        hText.setOpaque(false);

        JLabel lblTitle = new JLabel("Sistem Pengelolaan Nilai Siswa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Java OOP  •  MVC Pattern  •  DAO  •  MySQL  •  Desktop Application");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(210, 220, 255));

        hText.add(lblTitle);
        hText.add(Box.createVerticalStrut(6));
        hText.add(lblSub);

        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 58));

        header.add(hText, BorderLayout.WEST);
        header.add(iconLabel, BorderLayout.EAST);

        // =====================
        // CARDS GRID
        // =====================
        JPanel grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setBackground(BG);
        grid.setBorder(new EmptyBorder(22, 25, 22, 25));

        btnSiswa    = menuCard("📚","Data Siswa", "Kelola data, nilai & peringkat siswa", PRIMARY);
        btnKelas    = menuCard("🏫", "Data Kelas", "Tambah, edit & hapus data kelas", PURPLE);
        btnStatistik= menuCard("📊", "Statistik Nilai", "Lihat ringkasan statistik nilai per kelas", EMERALD);
        btnAbout    = menuCard("ℹ", "Tentang Aplikasi", "Informasi aplikasi dan developer", AMBER);

        grid.add(btnSiswa);
        grid.add(btnKelas);
        grid.add(btnStatistik);
        grid.add(btnAbout);

        // =====================
        // FOOTER
        // =====================
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
            new EmptyBorder(12, 25, 12, 25)
        ));

        JLabel copy = new JLabel("© 2026 Sistem Akademik Sekolah");
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copy.setForeground(MUTED);

        btnExit = new JButton("⏻  Keluar");
        btnExit.setBackground(DANGER);
        btnExit.setForeground(Color.BLACK);
        btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnExit.setFocusPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DANGER.darker()), new EmptyBorder(9, 22, 9, 22)));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnExit.setBackground(DANGER.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnExit.setBackground(DANGER); }
        });

        footer.add(copy, BorderLayout.WEST);
        footer.add(btnExit, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);
        main.add(grid, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);
        add(main);

        // Actions
        btnSiswa.addActionListener(e     -> new FormSiswa().setVisible(true));
        btnKelas.addActionListener(e     -> new FormKelas().setVisible(true));
        btnStatistik.addActionListener(e -> new FormStatistik().setVisible(true));
        btnAbout.addActionListener(e     -> new About().setVisible(true));
        btnExit.addActionListener(e      -> {
            int opt = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    private JButton menuCard(String icon, String title, String desc, Color color) {
        JButton btn = new JButton() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                    ? new Color(color.getRed(), color.getGreen(), color.getBlue(), 15)
                    : CARD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                super.paintComponent(g);
            }
        };
        btn.setLayout(new BorderLayout());
        btn.setBackground(CARD);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(22, 22, 22, 22)
        ));

        // Left accent bar
        JPanel accent = new JPanel();
        accent.setBackground(color);
        accent.setPreferredSize(new Dimension(5, 0));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 16, 0, 0));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TXT);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(MUTED);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 18));
        arrow.setForeground(color);
        arrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(lblIcon);
        content.add(Box.createVerticalStrut(10));
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(4));
        content.add(lblDesc);
        content.add(Box.createVerticalStrut(14));
        content.add(arrow);

        btn.add(accent, BorderLayout.WEST);
        btn.add(content, BorderLayout.CENTER);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    new EmptyBorder(21, 21, 21, 21)));
                btn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    new EmptyBorder(22, 22, 22, 22)));
                btn.repaint();
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
