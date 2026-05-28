package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class About extends JFrame {

    private final Color PRIMARY    = new Color(67, 97, 238);
    private final Color BG         = new Color(248, 250, 252);
    private final Color CARD       = Color.WHITE;
    private final Color TEXT_DARK  = new Color(30, 41, 59);
    private final Color TEXT_MUTED = new Color(100, 116, 139);
    private final Color BORDER_CLR = new Color(226, 232, 240);

    public About() {
        setTitle("Tentang Aplikasi");
        setSize(460, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        initComponent();
    }

    private void initComponent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header dengan gradient
        JPanel header = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY, getWidth(), getHeight(), new Color(139, 92, 246));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 30, 30, 30));
        header.setPreferredSize(new Dimension(0, 160));

        JLabel icon = new JLabel("🎓", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("SISTEM PENGELOLAAN NILAI SISWA");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Versi 2.0 — 2026");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        version.setForeground(new Color(200, 210, 255));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(icon);
        header.add(Box.createVerticalStrut(10));
        header.add(appName);
        header.add(Box.createVerticalStrut(4));
        header.add(version);

        // Info cards
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(20, 25, 20, 25));

        body.add(infoCard("🛠  Teknologi", "Java Swing  •  MVC Pattern  •  DAO Pattern\nMySQL Database  •  OOP Principles"));
        body.add(Box.createVerticalStrut(12));
        body.add(infoCard("✨  Fitur Utama", "CRUD Siswa & Kelas\nHitung Nilai Akhir & Grade Otomatis\nRanking Per Kelas\nStatistik Nilai Per Kelas\nSearch & Sort Data"));
        body.add(Box.createVerticalStrut(12));
        body.add(infoCard("👤  Developer", "Ajrun Ramadhan\nSistem Informasi — 2026"));

        // Close button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(0, 0, 15, 0));
        JButton btnClose = new JButton("Tutup");
        btnClose.setBackground(PRIMARY);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY.darker()), new EmptyBorder(9, 30, 9, 30)));
        btnClose.addActionListener(e -> dispose());
        footer.add(btnClose);

        main.add(header, BorderLayout.NORTH);
        main.add(body, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);
        add(main);
    }

    private JPanel infoCard(String title, String content) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            new EmptyBorder(12, 16, 12, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lTitle.setForeground(PRIMARY);

        JTextArea lContent = new JTextArea(content);
        lContent.setEditable(false);
        lContent.setOpaque(false);
        lContent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lContent.setForeground(TEXT_DARK);
        lContent.setLineWrap(false);

        card.add(lTitle, BorderLayout.NORTH);
        card.add(lContent, BorderLayout.CENTER);
        return card;
    }
}
