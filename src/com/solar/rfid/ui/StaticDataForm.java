// package com.solar.rfid.ui;

// import javax.swing.*;
// import java.awt.*;

// import com.solar.rfid.model.StaticPanelData;
// import com.solar.rfid.db.StaticDataRepository;

// public class StaticDataForm extends JDialog {

//     private JTextField manufacturer = new JTextField();
//     private JTextField cellManufacturer = new JTextField();
//     private JTextField moduleType = new JTextField();
//     private JTextField moduleCountry = new JTextField();
//     private JTextField cellCountry = new JTextField();
//     private JTextField testLab = new JTextField();
//     private JTextField iecDate = new JTextField();
//     private JTextField factoryCode = new JTextField();
//     private JTextField lineCode = new JTextField();
//     private JTextField cellmanufacturingdate = new JTextField();

//     public StaticDataForm(JFrame parent) {

//         super(parent, "Static Panel Data", true);
//         setSize(450, 420);
//         setLayout(new GridLayout(10, 2, 8, 8));
//         setLocationRelativeTo(parent);

//         add(new JLabel("1.Manufacturer"));
//         add(manufacturer);

//         add(new JLabel("2.Cell Manufacturer"));
//         add(cellManufacturer);

//         add(new JLabel("3.Module Type"));
//         add(moduleType);

//         add(new JLabel("4.Module Country"));
//         add(moduleCountry);

//         add(new JLabel("5.Cell Country"));
//         add(cellCountry);
//         add(new JLabel("6.Cell Manufacturing Date"));
//         add(cellmanufacturingdate);

//         add(new JLabel("7.Test Lab"));
//         add(testLab);

//         add(new JLabel("8.IEC Date"));
//         add(iecDate);

//         add(new JLabel("9.Factory Code"));
//         add(factoryCode);

//         add(new JLabel("10.Line Code"));
//         add(lineCode);

//         JButton save = new JButton("SAVE");
//         add(new JLabel());
//         add(save);

//         save.addActionListener(e -> {

//             StaticPanelData d = new StaticPanelData();
//             d.setManufacturer(manufacturer.getText());
//             d.setCellManufacturer(cellManufacturer.getText());
//             d.setCellManufacturingDate(cellmanufacturingdate.getText());
            
//             d.setModuleType(moduleType.getText());
//             d.setModuleCountry(moduleCountry.getText());
//             d.setCellCountry(cellCountry.getText());
//             d.setTestLab(testLab.getText());
//             d.setIecDate(iecDate.getText());
//             d.setFactoryCode(factoryCode.getText());
//             d.setLineCode(lineCode.getText());

//             StaticDataRepository.save(d);
//             dispose();
//         });
//     }
// }


package com.solar.rfid.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import com.solar.rfid.model.StaticPanelData;
import com.solar.rfid.db.StaticDataRepository;

public class StaticDataForm extends JDialog {

    // ── Colors ──────────────────────────────────────────────────────────────
    private static final Color BG_DARK     = new Color(15,  23,  42);
    private static final Color ACCENT      = new Color(250, 176,  5);
    private static final Color TEXT_LIGHT  = new Color(226, 232, 240);
    private static final Color TEXT_MUTED  = new Color(100, 116, 139);
    private static final Color BORDER_CLR  = new Color(40,  55,  80);
    private static final Color FIELD_BG    = new Color(28,  42,  68);
    private static final Color FIELD_FOCUS = new Color(35,  55,  90);
    private static final Color ERROR_CLR   = new Color(239,  68,  68);
    private static final Color SUCCESS_CLR = new Color(34, 197,  94);

    // ── Fields ───────────────────────────────────────────────────────────────
    private final PlaceholderTextField manufacturer          = new PlaceholderTextField("e.g. Tata Power Solar");
    private final PlaceholderTextField cellManufacturer      = new PlaceholderTextField("e.g. LONGi Solar");
    private final PlaceholderTextField moduleType            = new PlaceholderTextField("e.g. Mono PERC 540W");
    private final PlaceholderTextField moduleCountry         = new PlaceholderTextField("e.g. India");
    private final PlaceholderTextField cellCountry           = new PlaceholderTextField("e.g. China");
    private final PlaceholderTextField cellmanufacturingdate = new PlaceholderTextField("DD/MM/YYYY");
    private final PlaceholderTextField testLab               = new PlaceholderTextField("e.g. TUV Rheinland");
    private final PlaceholderTextField iecDate               = new PlaceholderTextField("DD/MM/YYYY");
    private final PlaceholderTextField factoryCode           = new PlaceholderTextField("e.g. FAC-001");
    private final PlaceholderTextField lineCode              = new PlaceholderTextField("e.g. LINE-A");

    // ════════════════════════════════════════════════════════════════════════
    public StaticDataForm(JFrame parent) {
        super(parent, "Static Panel Data", true);
        setUndecorated(true);
        setSize(580, 660);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildFormPanel(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        addDrag(root);
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 45, 75), getWidth(), 0, new Color(20, 30, 55));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(580, 72));
        header.setBorder(new EmptyBorder(14, 22, 14, 18));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel("☀");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        icon.setForeground(ACCENT);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Static Panel Data");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT_LIGHT);

        JLabel subtitle = new JLabel("Solar RFID System  •  All fields are mandatory  *");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(ERROR_CLR);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        left.add(icon);
        left.add(titlePanel);

        JButton close = makeCloseButton();
        close.addActionListener(e -> dispose());

        header.add(left, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);
        return header;
    }

    // ── Form Panel ───────────────────────────────────────────────────────────
    private JScrollPane buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(14, 24, 10, 24));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(4, 0, 4, 0);

        Object[][] rows = {
            {"Manufacturer",            "Name of the panel manufacturer",            manufacturer},
            {"Cell Manufacturer",       "Company that manufactured the solar cells", cellManufacturer},
            {"Module Type",             "Panel model/type with wattage",             moduleType},
            {"Module Country",          "Country where module was assembled",        moduleCountry},
            {"Cell Country",            "Country where cells were made",             cellCountry},
            {"Cell Manufacturing Date", "Cells ki manufacturing date (DD/MM/YYYY)",  cellmanufacturingdate},
            {"Test Lab",                "Lab that performed certification testing",  testLab},
            {"IEC Date",                "IEC certification date (DD/MM/YYYY)",       iecDate},
            {"Factory Code",            "Unique code identifying the factory",       factoryCode},
            {"Line Code",               "Production line identifier code",           lineCode},
        };

        for (int i = 0; i < rows.length; i++) {
            c.gridy = i;
            form.add(buildRow((String) rows[i][0], (String) rows[i][1], i + 1,
                              (PlaceholderTextField) rows[i][2]), c);
        }

        JScrollPane scroll = new JScrollPane(form);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                thumbColor = new Color(60, 80, 120);
                trackColor = BG_DARK;
            }
            protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            private JButton zeroBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
        return scroll;
    }

    private JPanel buildRow(String label, String hint, int num, PlaceholderTextField field) {
        JPanel outer = new JPanel(new BorderLayout(0, 3));
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(3, 0, 3, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        // left: number + label + asterisk
        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelRow.setOpaque(false);

        JLabel numLbl = new JLabel(String.format("%02d  ", num));
        numLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        numLbl.setForeground(ACCENT);

        JLabel nameLbl = new JLabel(label + " ");
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLbl.setForeground(TEXT_LIGHT);

        JLabel star = new JLabel("*");
        star.setFont(new Font("Segoe UI", Font.BOLD, 13));
        star.setForeground(ERROR_CLR);

        labelRow.add(numLbl);
        labelRow.add(nameLbl);
        labelRow.add(star);

        // right: hint text
        JLabel hintLbl = new JLabel(hint);
        hintLbl.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hintLbl.setForeground(new Color(71, 85, 105));

        top.add(labelRow, BorderLayout.WEST);
        top.add(hintLbl, BorderLayout.EAST);

        outer.add(top, BorderLayout.NORTH);
        outer.add(field, BorderLayout.CENTER);
        return outer;
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 14)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BORDER_CLR);
                g.fillRect(0, 0, getWidth(), 1);
                super.paintComponent(g);
            }
        };
        footer.setOpaque(false);

        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancel.setForeground(TEXT_MUTED);
        cancel.setBackground(null);
        cancel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(8, 22, 8, 22)));
        cancel.setFocusPainted(false);
        cancel.setContentAreaFilled(false);
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { cancel.setForeground(TEXT_LIGHT); }
            public void mouseExited(MouseEvent e)  { cancel.setForeground(TEXT_MUTED); }
        });
        cancel.addActionListener(e -> dispose());

        JButton save = new JButton("  Save Data  ") {
            boolean hover;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setForeground(new Color(15, 23, 42));
                setBorder(new EmptyBorder(9, 26, 9, 26));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? new Color(255, 195, 30) : ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        save.addActionListener(e -> handleSave());

        footer.add(cancel);
        footer.add(save);
        return footer;
    }

    // ── Save Logic ────────────────────────────────────────────────────────────
    private void handleSave() {
        PlaceholderTextField[] allFields = {
            manufacturer, cellManufacturer, moduleType, moduleCountry,
            cellCountry, cellmanufacturingdate, testLab, iecDate, factoryCode, lineCode
        };
        String[] allLabels = {
            "Manufacturer", "Cell Manufacturer", "Module Type", "Module Country",
            "Cell Country", "Cell Manufacturing Date", "Test Lab", "IEC Date",
            "Factory Code", "Line Code"
        };

        // ── All fields mandatory ──
        for (int i = 0; i < allFields.length; i++) {
            if (allFields[i].getText().trim().isEmpty()) {
                showError("\"" + allLabels[i] + "\" field khali nahi chhod sakte.\nYeh field mandatory hai.");
                allFields[i].requestFocus();
                return;
            }
        }

        // ── Date format validation ──
        if (!isValidDate(cellmanufacturingdate.getText().trim())) {
            showError("Cell Manufacturing Date ka format galat hai.\nSahi format: DD/MM/YYYY\nExample: 15/08/2023");
            cellmanufacturingdate.requestFocus();
            return;
        }
        if (!isValidDate(iecDate.getText().trim())) {
            showError("IEC Date ka format galat hai.\nSahi format: DD/MM/YYYY\nExample: 01/01/2022");
            iecDate.requestFocus();
            return;
        }

        // ── Save to DB ──
        try {
            StaticPanelData d = new StaticPanelData();
            d.setManufacturer(manufacturer.getText().trim());
            d.setCellManufacturer(cellManufacturer.getText().trim());
            d.setCellManufacturingDate(cellmanufacturingdate.getText().trim());
            d.setModuleType(moduleType.getText().trim());
            d.setModuleCountry(moduleCountry.getText().trim());
            d.setCellCountry(cellCountry.getText().trim());
            d.setTestLab(testLab.getText().trim());
            d.setIecDate(iecDate.getText().trim());
            d.setFactoryCode(factoryCode.getText().trim());
            d.setLineCode(lineCode.getText().trim());
            StaticDataRepository.save(d);
            showSuccess();
        } catch (Exception ex) {
            showError("Data save karne mein error aaya:\n" + ex.getMessage());
        }
    }

    // ── Date Validation DD/MM/YYYY ────────────────────────────────────────────
    private boolean isValidDate(String date) {
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) return false;
        try {
            int day   = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year  = Integer.parseInt(date.substring(6, 10));
            if (month < 1 || month > 12)   return false;
            if (day   < 1 || day   > 31)   return false;
            if (year  < 1900 || year > 2100) return false;
            int[] dim = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) dim[1] = 29;
            return day <= dim[month - 1];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ── Success Toast ─────────────────────────────────────────────────────────
    private void showSuccess() {
        JDialog toast = new JDialog(this, false);
        toast.setUndecorated(true);
        toast.setSize(370, 120);
        toast.setLocationRelativeTo(this);
        toast.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(22, 33, 57));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // green top bar
                g2.setColor(SUCCESS_CLR);
                g2.fillRoundRect(0, 0, getWidth(), 5, 5, 5);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row.setOpaque(false);

        JLabel checkIcon = new JLabel("✔");
        checkIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        checkIcon.setForeground(SUCCESS_CLR);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);

        JLabel msg1 = new JLabel("Data Successfully Saved!");
        msg1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        msg1.setForeground(TEXT_LIGHT);

        JLabel msg2 = new JLabel("Static panel data database mein store ho gaya.");
        msg2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        msg2.setForeground(TEXT_MUTED);

        textPanel.add(msg1);
        textPanel.add(msg2);
        row.add(checkIcon);
        row.add(textPanel);

        panel.add(row, BorderLayout.CENTER);
        toast.setContentPane(panel);
        toast.setVisible(true);

        // Auto close after 2.2s, then dispose main dialog
        Timer t = new Timer(2200, ev -> { toast.dispose(); dispose(); });
        t.setRepeats(false);
        t.start();
    }

    // ── Styled Error Dialog ───────────────────────────────────────────────────
    private void showError(String msg) {
        JDialog err = new JDialog(this, true);
        err.setUndecorated(true);
        err.setSize(370, 150);
        err.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(0, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(22, 33, 57));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(ERROR_CLR);
                g2.fillRoundRect(0, 0, getWidth(), 5, 5, 5);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 22, 16, 22));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);

        JLabel errIcon = new JLabel("⚠");
        errIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        errIcon.setForeground(ERROR_CLR);

        JLabel errTitle = new JLabel("Validation Error");
        errTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        errTitle.setForeground(TEXT_LIGHT);

        top.add(errIcon);
        top.add(errTitle);

        JLabel errMsg = new JLabel("<html><center>" + msg.replace("\n", "<br>") + "</center></html>");
        errMsg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errMsg.setForeground(TEXT_MUTED);
        errMsg.setHorizontalAlignment(SwingConstants.CENTER);

        JButton ok = new JButton("  OK  ") {
            boolean h;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setForeground(new Color(15, 23, 42));
                setBorder(new EmptyBorder(7, 28, 7, 28));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { h = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { h = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(h ? new Color(255, 80, 80) : ERROR_CLR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ok.addActionListener(e -> err.dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.setOpaque(false);
        btnRow.add(ok);

        panel.add(top, BorderLayout.NORTH);
        panel.add(errMsg, BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);

        err.setContentPane(panel);
        err.setVisible(true);
    }

    // ── Placeholder TextField (inner static class) ────────────────────────────
    static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setForeground(new Color(226, 232, 240));
            setCaretColor(ACCENT);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(new EmptyBorder(9, 12, 9, 12));
            setPreferredSize(new Dimension(0, 40));
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e)   { repaint(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background
            g2.setColor(isFocusOwner() ? FIELD_FOCUS : FIELD_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            // gold focus ring
            if (isFocusOwner()) {
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
            }
            g2.dispose();
            super.paintComponent(g);

            // placeholder text when empty & not focused
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D gp = (Graphics2D) g.create();
                gp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gp.setColor(new Color(71, 85, 105));
                gp.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                Insets ins = getInsets();
                FontMetrics fm = gp.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                gp.drawString(placeholder, ins.left + 2, y);
                gp.dispose();
            }
        }
    }

    // ── Utilities ─────────────────────────────────────────────────────────────
    private JButton makeCloseButton() {
        JButton b = new JButton("✕");
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(TEXT_MUTED);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setForeground(ERROR_CLR); }
            public void mouseExited(MouseEvent e)  { b.setForeground(TEXT_MUTED); }
        });
        return b;
    }

    private void addDrag(JPanel panel) {
        final Point[] offset = {null};
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { offset[0] = e.getPoint(); }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (offset[0] != null) {
                    Point loc = getLocation();
                    setLocation(loc.x + e.getX() - offset[0].x,
                                loc.y + e.getY() - offset[0].y);
                }
            }
        });
    }
}