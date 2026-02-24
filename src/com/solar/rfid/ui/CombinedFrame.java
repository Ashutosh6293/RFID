package com.solar.rfid.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import com.solar.rfid.excel.ExcelImporter;
import com.solar.rfid.model.PanelData;
import com.solar.rfid.model.StaticPanelData;
import com.solar.rfid.service.AutoMappingEngine;
import com.solar.rfid.db.PanelRepository;
import com.solar.rfid.db.StaticDataRepository;

public class CombinedFrame extends JFrame {

    // ===== Live data labels =====
    private JLabel lblPanelId = new JLabel("-");
    private JLabel lblTid     = new JLabel("-");

    // ===== Status bar =====
    private JLabel status    = new JLabel("READY");
    private JPanel statusBar;

    // ===== IV Curve =====
    private IVCurvePanel ivPanel = new IVCurvePanel();

    // ===== Table =====
    private DefaultTableModel tableModel;
    private JTable dataTable;

    // ===== Sidebar fields =====
    private JTextField txtBarcode = new JTextField();
    private JTextField txtSearch  = new JTextField();

    // ===== Current data for printing =====
    private PrintablePanelReport.ReportData currentReportData =
            new PrintablePanelReport.ReportData();

    // =========================================================
    public CombinedFrame() {
        setTitle("GAUTAM SOLAR – RFID SYSTEM");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.LIGHT_GRAY);

        JPanel sidebar = buildSidebar();
        sidebar.setPreferredSize(new Dimension(300, 0));
        add(sidebar, BorderLayout.WEST);

        add(buildMainPanel(), BorderLayout.CENTER);

        statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(33, 150, 243));
        statusBar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setForeground(Color.WHITE);
        statusBar.add(status, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
    }

    // =========================================================
    // SIDEBAR
    // =========================================================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(22, 30, 46));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        // ── Logo ──────────────────────────────────────────────
        JLabel logo1 = new JLabel("GAUTAM SOLAR");
        logo1.setFont(new Font("Arial", Font.BOLD, 24));
        logo1.setForeground(new Color(220, 40, 50));
        logo1.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo1);
        sidebar.add(Box.createVerticalStrut(4));

        JLabel tagline = new JLabel("Panel Management System");
        tagline.setFont(new Font("Arial", Font.PLAIN, 12));
        tagline.setForeground(new Color(120, 150, 190));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(tagline);

        sidebar.add(divider());

        // ── Barcode Scanner ───────────────────────────────────
        sectionLabel(sidebar, "BARCODE / SCANNER");
        sidebar.add(Box.createVerticalStrut(6));

        styleTextField(txtBarcode, new Color(60, 100, 160));
        txtBarcode.setToolTipText("Scan barcode or type manually");

        final boolean[] isProcessing = {false};

        txtBarcode.addActionListener(e -> {
            if (!isProcessing[0] && !txtBarcode.getText().trim().isEmpty()) {
                isProcessing[0] = true;
                startMapping();
                txtBarcode.setText("");
                isProcessing[0] = false;
            }
        });

        Timer scanTimer = new Timer(200, e -> {
            if (!isProcessing[0] && !txtBarcode.getText().trim().isEmpty()) {
                isProcessing[0] = true;
                startMapping();
                txtBarcode.setText("");
                isProcessing[0] = false;
            }
        });
        scanTimer.setRepeats(false);

        txtBarcode.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void restartTimer() { scanTimer.restart(); }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { restartTimer(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  {}
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        sidebar.add(txtBarcode);
        sidebar.add(Box.createVerticalStrut(8));

        JButton btnScan = sidebarBtn("▶  Scan RFID & Map", new Color(33, 150, 243));
        btnScan.addActionListener(e -> startMapping());
        sidebar.add(btnScan);

        sidebar.add(divider());

        // ── Search by Serial No ───────────────────────────────
        sectionLabel(sidebar, "SEARCH PANEL BY SERIAL NO.");
        sidebar.add(Box.createVerticalStrut(6));

        styleTextField(txtSearch, new Color(250, 140, 0)); // gold border
        txtSearch.setToolTipText("Serial No. dalo aur Enter dabao");
        txtSearch.addActionListener(e -> doSearch());
        sidebar.add(txtSearch);
        sidebar.add(Box.createVerticalStrut(6));

        JPanel searchBtnRow = new JPanel(new GridLayout(1, 2, 6, 0));
        searchBtnRow.setOpaque(false);
        searchBtnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        searchBtnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSearch = sidebarBtn("🔍  Search", new Color(250, 140, 0));
        btnSearch.addActionListener(e -> doSearch());

        JButton btnClear = sidebarBtn("✕  Clear", new Color(90, 100, 115));
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            clearMainPanel();
            showMessage("READY – Scan or search a panel");
            SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
        });

        searchBtnRow.add(btnSearch);
        searchBtnRow.add(btnClear);
        sidebar.add(searchBtnRow);

        sidebar.add(divider());

        // ── Data Import ───────────────────────────────────────
        sectionLabel(sidebar, "DATA IMPORT");
        sidebar.add(Box.createVerticalStrut(8));

        JButton btnExcel = sidebarBtn("📂  Upload Excel", new Color(40, 167, 69));
        btnExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select Excel File");
            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();

                JDialog loader = new JDialog(this, "Importing Excel...", true);
                loader.setLayout(new BorderLayout(10, 10));
                JLabel label = new JLabel("Please wait... Importing data");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                JProgressBar bar = new JProgressBar();
                bar.setIndeterminate(true);
                loader.add(label, BorderLayout.CENTER);
                loader.add(bar, BorderLayout.SOUTH);
                loader.setSize(300, 110);
                loader.setLocationRelativeTo(this);
                loader.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override protected Void doInBackground() {
                        ExcelImporter.importExcel(selectedFile);
                        return null;
                    }
                    @Override protected void done() {
                        loader.dispose();
                        JOptionPane.showMessageDialog(CombinedFrame.this,
                            "✅ Excel Imported:\n" + selectedFile.getName(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
                    }
                };
                worker.execute();
                loader.setVisible(true);
            }
        });
        sidebar.add(btnExcel);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnStatic = sidebarBtn("⚙  Static Panel Data", new Color(108, 117, 125));
        btnStatic.addActionListener(e -> {
            new StaticDataForm(this).setVisible(true);
            SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
        });
        sidebar.add(btnStatic);

        sidebar.add(divider());

        // ── Print ─────────────────────────────────────────────
        sectionLabel(sidebar, "PRINT");
        sidebar.add(Box.createVerticalStrut(8));

        JButton btnPrint = sidebarBtn("🖨  Print A4 Report", new Color(155, 50, 220));
        btnPrint.addActionListener(e -> doPrint());
        sidebar.add(btnPrint);

        sidebar.add(divider());

        // ── Current Module Info ───────────────────────────────
        sectionLabel(sidebar, "CURRENT MODULE");
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(infoRow("Serial No:", lblPanelId));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(infoRow("TID:", lblTid));

        sidebar.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 65, 90));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(8));

        JLabel footer = new JLabel("Gautam Solar Pvt. Ltd. v1.0");
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(new Color(70, 90, 120));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        return sidebar;
    }

    // ── TextField styling helper ──────────────────────────────
    private void styleTextField(JTextField tf, Color borderColor) {
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setFont(new Font("Arial", Font.PLAIN, 12));
        tf.setBackground(new Color(35, 45, 65));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 5)));
    }

    // =========================================================
    // SEARCH LOGIC
    // =========================================================
    private void doSearch() {
        String serial = txtSearch.getText().trim();
        if (serial.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Serial Number khali hai. Kuch type karo.",
                "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        showMessage("Searching: " + serial + " ...");

        SwingWorker<PanelData, Void> worker = new SwingWorker<PanelData, Void>() {
            @Override
            protected PanelData doInBackground() {
                return PanelRepository.findBySerialNo(serial);
            }

            @Override
            protected void done() {
                try {
                    PanelData found = get();
                    if (found == null) {
                        showError("Panel nahi mila: " + serial);
                    } else {
                        // Static data load karo (same as live scan)
                        // StaticPanelData sd = StaticDataRepository.load();
                        StaticPanelData sd =
        StaticDataRepository.findById(found.getStaticId());
                        showMappedData(found, found.getTid());
                        if (sd != null) showStaticData(sd);
                        showSuccess("Panel mila: " + serial);
                    }
                } catch (Exception ex) {
                    showError("Search error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    // =========================================================
    // PRINT LOGIC
    // =========================================================
    private void doPrint() {
        if (currentReportData.serialNo == null ||
            currentReportData.serialNo.isEmpty() ||
            currentReportData.serialNo.equals("-")) {
            JOptionPane.showMessageDialog(this,
                "Koi panel data load nahi hai.\nPehle scan karo ya search karo.",
                "Print", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new PrintablePanelReport(currentReportData).print();
        SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
    }

    // =========================================================
    // CLEAR PANEL
    // =========================================================
    private void clearMainPanel() {
        lblPanelId.setText("-");
        lblTid.setText("-");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, 2);
        }
        ivPanel.setCurve(0, 0, 0);
        currentReportData = new PrintablePanelReport.ReportData();
    }

    // =========================================================
    // SIDEBAR HELPERS
    // =========================================================
    private void sectionLabel(JPanel p, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 10));
        lbl.setForeground(new Color(90, 130, 200));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
    }

    private JButton sidebarBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        Color brighter = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(brighter); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private Component divider() {
        JPanel d = new JPanel(new BorderLayout());
        d.setBackground(new Color(22, 30, 46));
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 65, 90));
        d.add(sep, BorderLayout.CENTER);
        d.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return d;
    }

    private JPanel infoRow(String key, JLabel valueLabel) {
        JPanel row = new JPanel(new GridLayout(2, 1, 0, 2));
        row.setBackground(new Color(22, 30, 46));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        JLabel k = new JLabel(key);
        k.setFont(new Font("Arial", Font.PLAIN, 10));
        k.setForeground(new Color(120, 150, 190));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 11));
        valueLabel.setForeground(new Color(33, 150, 243));
        row.add(k);
        row.add(valueLabel);
        return row;
    }

    // =========================================================
    // MAIN PANEL
    // =========================================================
    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(Color.WHITE);
        main.add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 0, 0));
        center.setBackground(Color.WHITE);
        center.add(buildTablePanel());
        center.add(buildCurvePanel());
        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 6, 20));

        addCenter(panel, "GAUTAM SOLAR PVT. LTD.",   Font.BOLD, 28, new Color(200, 16, 46));
        addCenter(panel, "Gautam Solar Private Limited", Font.BOLD, 18, Color.BLACK);
        addCenter(panel, "7 km Milestone, Tosham Road, Dist. Bhiwani, Bawani Khera HR 127032",
                Font.BOLD, 14, Color.DARK_GRAY);
        addCenter(panel, "INDIA", Font.BOLD, 14, new Color(200, 16, 46));

        panel.add(Box.createVerticalStrut(6));

        JPanel subRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        subRow.setBackground(Color.WHITE);

        JLabel k1 = new JLabel("Module Serial:");
        k1.setFont(new Font("Arial", Font.BOLD, 14));
        lblPanelId.setFont(new Font("Arial", Font.BOLD, 11));
        lblPanelId.setForeground(new Color(33, 150, 243));

        JLabel k2 = new JLabel("TID:");
        k2.setFont(new Font("Arial", Font.BOLD, 14));
        lblTid.setFont(new Font("Arial", Font.BOLD, 11));
        lblTid.setForeground(Color.DARK_GRAY);

        subRow.add(k1); subRow.add(lblPanelId);
        subRow.add(Box.createHorizontalStrut(30));
        subRow.add(k2); subRow.add(lblTid);
        panel.add(subRow);

        panel.add(Box.createVerticalStrut(6));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(200, 200, 200));
        panel.add(sep);

        return panel;
    }

    private void addCenter(JPanel p, String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", style, size));
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(l);
    }

    private JPanel buildTablePanel() {
        JPanel container = new JPanel(new BorderLayout(0, 4));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(6, 20, 4, 20));

        JLabel title = new JLabel("Detailed Specification:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        container.add(title, BorderLayout.NORTH);

        String[] columns = {"Sr No.", "Parameter", "Value"};
        Object[][] data = {
            {"1",  "Name of the Manufacturer of PV Module",         ""},
            {"2",  "Name of the Manufacturer of Solar Cell",         ""},
            {"3",  "Module Type",                                    ""},
            {"4",  "Month & Year of the Manufacture of Module",      ""},
            {"5",  "Month & Year of the Manufacture of Solar Cell",  ""},
            {"6",  "Country of Origin for PV Module",                ""},
            {"7",  "Country of Origin for Solar Cell",               ""},
            {"8",  "Power: P-Max of the Module",                     ""},
            {"9",  "Voltage: V-Max of the Module",                   ""},
            {"10", "Current: I-Max of the Module",                   ""},
            {"11", "Fill Factor (FF) of the Module",                 ""},
            {"12", "VOC",                                            ""},
            {"13", "ISC",                                            ""},
            {"14", "Name of The Test Lab Issuing IEC Certificate",   ""},
            {"15", "Date of Obtaining IEC Certificate",              ""},
        };

        tableModel = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.BOLD, 14));
        dataTable.setRowHeight(24);
        dataTable.setGridColor(new Color(210, 215, 225));
        dataTable.setShowGrid(true);
        dataTable.setSelectionBackground(new Color(220, 235, 255));

        dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 250, 255));
                if (col == 2) {
                    setFont(new Font("Arial", Font.BOLD, 11));
                    setForeground(new Color(20, 100, 190));
                } else {
                    setFont(new Font("Arial", Font.PLAIN, 11));
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        JTableHeader header = dataTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(255, 255, 200));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);

        TableColumnModel cm = dataTable.getColumnModel();
        cm.getColumn(0).setPreferredWidth(55);
        cm.getColumn(1).setPreferredWidth(400);
        cm.getColumn(2).setPreferredWidth(250);

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(JLabel.CENTER);
        cm.getColumn(0).setCellRenderer(centerR);

        JScrollPane sp = new JScrollPane(dataTable);
        sp.setBorder(BorderFactory.createLineBorder(new Color(190, 200, 215), 1));
        container.add(sp, BorderLayout.CENTER);

        return container;
    }

    private JPanel buildCurvePanel() {
        JPanel container = new JPanel(new BorderLayout(0, 4));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(4, 20, 10, 20));

        JLabel title = new JLabel("IV Characteristics of the Module:");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        container.add(title, BorderLayout.NORTH);
        container.add(ivPanel, BorderLayout.CENTER);

        return container;
    }

    // =========================================================
    // MAPPING LOGIC
    // =========================================================
    private void startMapping() {
        String barcode = txtBarcode.getText().trim();
        if (barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please scan or enter Panel Barcode");
            return;
        }
        System.out.println("BARCODE RECEIVED: " + barcode);
        AutoMappingEngine.manualTrigger(barcode);
        txtBarcode.setText("");
        SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
    }

    public void triggerAutoMapping(String barcode) {
        txtBarcode.setText(barcode);
        startMapping();
    }

    // =========================================================
    // PUBLIC API – called by AutoMappingEngine
    // =========================================================
    public void showMappedData(PanelData d, String epc) {
        SwingUtilities.invokeLater(() -> {
            lblPanelId.setText(d.getId());
            lblTid.setText(epc != null && !epc.isEmpty() ? epc : "-");

            // ── Sync report data ──────────────────────────────
            currentReportData.serialNo      = d.getId();
            currentReportData.tid           = epc != null ? epc : "-";
            currentReportData.moduleMfgDate = d.getDate();
            currentReportData.cellMfgDate   = d.getCellManufacturingDate();
            // currentReportData.eff           = d.getEff();
            currentReportData.ff           = d.getFf();
            currentReportData.voc           = d.getVoc();
            currentReportData.isc           = d.getIsc();
            currentReportData.pmax          = d.getPmax();

            try {
                double voc  = Double.parseDouble(d.getVoc());
                double isc  = Double.parseDouble(d.getIsc());
                double pmax = Double.parseDouble(d.getPmax());
                double vmpp = voc * 0.78;
                double impp = pmax / vmpp;

                currentReportData.vocVal  = voc;
                currentReportData.iscVal  = isc;
                currentReportData.pmaxVal = pmax;
                currentReportData.vmpp    = String.format("%.5f", vmpp);
                currentReportData.impp    = String.format("%.2f", impp);

                tableModel.setValueAt(d.getPmax(),                    7, 2);
                tableModel.setValueAt(String.format("%.5f", vmpp),   8, 2);
                tableModel.setValueAt(String.format("%.2f", impp),   9, 2);
                tableModel.setValueAt(d.getFf(),                    10, 2);
                tableModel.setValueAt(d.getVoc(),                    11, 2);
                tableModel.setValueAt(d.getIsc(),                    12, 2);
                tableModel.setValueAt(d.getDate(),                    3, 2);
                // tableModel.setValueAt(d.getCellManufacturingDate(),   4, 2);

                ivPanel.setCurve(voc, isc, pmax);
            } catch (Exception ex) {
                ivPanel.setCurve(0, 0, 0);
            }
        });
    }

    public void showStaticData(StaticPanelData s) {
        if (s == null) return;
        SwingUtilities.invokeLater(() -> {
            tableModel.setValueAt(s.getManufacturer(),    0, 2);
            tableModel.setValueAt(s.getCellManufacturer(),1, 2);
            tableModel.setValueAt(s.getCellManufacturingDate(),   4, 2);
            tableModel.setValueAt(s.getModuleType(),      2, 2);
            tableModel.setValueAt(s.getModuleCountry(),   5, 2);
            tableModel.setValueAt(s.getCellCountry(),     6, 2);
            tableModel.setValueAt(s.getTestLab(),        13, 2);
            tableModel.setValueAt(s.getIecDate(),        14, 2);

            // ── Sync report static fields ─────────────────────
            currentReportData.manufacturer  = s.getManufacturer();
            currentReportData.cellMfr       = s.getCellManufacturer();
            currentReportData.cellMfgDate   = s.getCellManufacturingDate();
            currentReportData.moduleType    = s.getModuleType();
            currentReportData.moduleCountry = s.getModuleCountry();
            currentReportData.cellCountry   = s.getCellCountry();
            currentReportData.testLab       = s.getTestLab();
            currentReportData.iecDate       = s.getIecDate();
        });
    }

    public void showMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            status.setText("  " + msg);
            statusBar.setBackground(new Color(33, 150, 243));
        });
    }

    public void showSuccess(String msg) {
        SwingUtilities.invokeLater(() -> {
            status.setText("  ✅  " + msg);
            statusBar.setBackground(new Color(40, 167, 69));
        });
    }

    public void showError(String msg) {
        SwingUtilities.invokeLater(() -> {
            status.setText("  ❌  " + msg);
            statusBar.setBackground(new Color(200, 35, 51));
        });
    }

    public void resetForNext() {
        SwingUtilities.invokeLater(() -> {
            showMessage("READY – Scan next panel");
            txtBarcode.requestFocusInWindow();
        });
    }
}




