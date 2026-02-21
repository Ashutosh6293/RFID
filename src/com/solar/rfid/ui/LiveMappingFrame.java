

package com.solar.rfid.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import com.solar.rfid.model.PanelData;
import com.solar.rfid.model.StaticPanelData;
// import com.solar.rfid.barcode.BarcodeListener;

public class LiveMappingFrame extends JFrame {
    // ===== Dynamic labels =====
    private JLabel lblPanelId = new JLabel("-");
    private JLabel lblTid = new JLabel("-");
    private JLabel lblPmax = new JLabel("-");
    private JLabel lblVmpp = new JLabel("-");
    private JLabel lblImpp = new JLabel("-");
    private JLabel lblVoc = new JLabel("-");
    private JLabel lblIsc = new JLabel("-");
    private JLabel lblEff = new JLabel("-");
    private JLabel lblDate = new JLabel("-");

    // ===== Static labels =====
    private JLabel lblManufacturer = new JLabel("-");
    private JLabel lblCellManufacturer = new JLabel("-");
    private JLabel lblModuleType = new JLabel("-");
    private JLabel lblModuleCountry = new JLabel("-");
    private JLabel lblCellCountry = new JLabel("-");
    private JLabel lblTestLab = new JLabel("-");
    private JLabel lblIECDate = new JLabel("-");

    private JLabel status = new JLabel("READY");

    // ===== IV Curve Panel =====
    private IVCurvePanel ivPanel = new IVCurvePanel();

    // Table model
    private DefaultTableModel tableModel;
    private JTable dataTable;

    public LiveMappingFrame() {
        setTitle("GAUTAM SOLAR - Module Specification");
        setSize(900, 1100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        // ================= MAIN PANEL =================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Header with logo
        mainPanel.add(createHeaderPanel());

        // Module Serial and TID
        mainPanel.add(createSerialPanel());

        // "Detailed Specification:" label
        mainPanel.add(createSpecTitlePanel());

        // Data Table using JTable
        mainPanel.add(createDataTable());

        // IV Curve Title
        mainPanel.add(createCurveTitlePanel());

        // IV Curve Graph
        ivPanel.setPreferredSize(new Dimension(850, 450));
        ivPanel.setMaximumSize(new Dimension(2000, 450));
        JPanel curveContainer = new JPanel(new BorderLayout());
        curveContainer.setBackground(Color.WHITE);
        curveContainer.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        curveContainer.add(ivPanel, BorderLayout.CENTER);
        mainPanel.add(curveContainer);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // ================= STATUS BAR =================
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(33, 150, 243));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setForeground(Color.WHITE);
        statusPanel.add(status, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
        panel.setMaximumSize(new Dimension(2000, 160));

        // Logo (G with red square)
        JLabel logo1 = new JLabel("GAUTAM");
        logo1.setFont(new Font("Arial", Font.BOLD, 32));
        logo1.setForeground(new Color(200, 16, 46));
        logo1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo2 = new JLabel("SOLAR");
        logo2.setFont(new Font("Arial", Font.BOLD, 28));
        logo2.setForeground(new Color(200, 16, 46));
        logo2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(logo1);
        panel.add(logo2);
        panel.add(Box.createVerticalStrut(8));

        // Company details
        addCenteredLabel(panel, "Gautam Solar Private Limited", Font.BOLD, 11, Color.BLACK);
        addCenteredLabel(panel, "7 km Milestone, Tosham Road", Font.PLAIN, 10, Color.BLACK);
        addCenteredLabel(panel, "Dist. Bhiwani", Font.PLAIN, 10, Color.BLACK);
        addCenteredLabel(panel, "Bawani Khera HR 127032", Font.PLAIN, 10, Color.BLACK);
        addCenteredLabel(panel, "India", Font.BOLD, 10, new Color(200, 16, 46));

        return panel;
    }

    private void addCenteredLabel(JPanel panel, String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", style, size));
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
    }

    private JPanel createSerialPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(2000, 40));

        JLabel serialLabel = new JLabel("Module Serial Number:");
        serialLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPanelId.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPanelId.setForeground(new Color(33, 150, 243));

        JLabel tidLabel = new JLabel("  TID:");
        tidLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTid.setFont(new Font("Arial", Font.PLAIN, 11));

        panel.add(serialLabel);
        panel.add(lblPanelId);
        panel.add(tidLabel);
        panel.add(lblTid);

        return panel;
    }

    private JPanel createSpecTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(2000, 30));

        JLabel title = new JLabel("Detailed Specification:");
        title.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(title);

        return panel;
    }

    private JPanel createDataTable() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));
        container.setMaximumSize(new Dimension(2000, 420));

        // Column names
        String[] columns = { "Sr No.", "Parameter", "Value" };

        // Data
        Object[][] data = {
                { "1", "Name of the Manufacturer of PV Module", "" },
                { "2", "Name of the Manufacturer of Solar Cell", "" },
                { "3", "Module Type", "" },
                { "4", "Month & Year of the Manufacture of Module", "" },
                { "5", "Month & Year of the Manufacture of Solar Cell", "" },
                { "6", "Country of Origin for PV Module", "" },
                { "7", "Country of Origin for Solar Cell", "" },
                { "8", "Power: P-Max of the Module", "" },
                { "9", "Voltage: V-Max of the Module", "" },
                { "10", "Current: I-Max of the Module", "" },
                { "11", "Fill Factor (FF) of the Module", "" },
                { "12", "VOC", "" },
                { "13", "ISC", "" },
                { "14", "Name of The Test Lab Issuing IEC Certificate", "" },
                { "15", "Date of Obtaining IEC Certificate", "" }
        };

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dataTable = new JTable(tableModel);

        // Table styling
        dataTable.setFont(new Font("Arial", Font.PLAIN, 10));
        dataTable.setRowHeight(25);
        dataTable.setGridColor(Color.BLACK);
        dataTable.setShowGrid(true);
        dataTable.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = dataTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setBackground(new Color(255, 255, 200));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Column widths
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60); // Sr No
        columnModel.getColumn(1).setPreferredWidth(420); // Parameter
        columnModel.getColumn(2).setPreferredWidth(280); // Value

        // Center align Sr No column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPane.setPreferredSize(new Dimension(850, 400));

        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createCurveTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(2000, 40));

        JLabel title = new JLabel("IV Characteristics of the Module:");
        title.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(title);

        return panel;
    }

    // ======================================================
    // 🔥 AUTO BARCODE (GLOBAL – NO FOCUS REQUIRED)
    // ======================================================
    // public void attachBarcodeListener(BarcodeListener listener) {
    // KeyboardFocusManager.getCurrentKeyboardFocusManager()
    // .addKeyEventDispatcher(e -> {
    // if (e.getID() == KeyEvent.KEY_TYPED) {
    // listener.keyTyped(e);
    // }
    // return false;
    // });
    // }
    // ======================================================
    // 🔥 AUTO BARCODE (FRAME LEVEL – NO BUTTON REQUIRED)
    // ======================================================
    // ================= UPDATE DATA =================
    public void showMappedData(PanelData d, String epc) {
        lblPanelId.setText(d.getId());
        lblTid.setText(epc);

        // Update table
        try {
            double voc = Double.parseDouble(d.getVoc());
            double isc = Double.parseDouble(d.getIsc());
            double pmax = Double.parseDouble(d.getPmax());

            // Calculate Vmpp and Impp
            double vmpp = voc * 0.78;
            double impp = pmax / vmpp;

            tableModel.setValueAt(d.getPmax(), 7, 2);
            tableModel.setValueAt(String.format("%.5f", vmpp), 8, 2);
            tableModel.setValueAt(String.format("%.2f", impp), 9, 2);
            tableModel.setValueAt(d.getEff(), 10, 2);
            tableModel.setValueAt(d.getVoc(), 11, 2);
            tableModel.setValueAt(d.getIsc(), 12, 2);

            // Dates
            tableModel.setValueAt(d.getDate(), 3, 2);
            tableModel.setValueAt(d.getDate(), 4, 2);

            // Update IV curve
            ivPanel.setCurve(voc, isc, pmax);

        } catch (Exception e) {
            ivPanel.setCurve(0, 0, 0);
        }
    }

    public void showStaticData(StaticPanelData s) {
        if (s == null)
            return;

        tableModel.setValueAt(s.getManufacturer(), 0, 2);
        tableModel.setValueAt(s.getCellManufacturer(), 1, 2);
        tableModel.setValueAt(s.getModuleType(), 2, 2);
        tableModel.setValueAt(s.getModuleCountry(), 5, 2);
        tableModel.setValueAt(s.getCellCountry(), 6, 2);
        tableModel.setValueAt(s.getTestLab(), 13, 2);
        tableModel.setValueAt(s.getIecDate(), 14, 2);
    }

    public void showMessage(String msg) {
        status.setText("STATUS: " + msg);
    }

    public void resetForNext() {
        showMessage("READY");
    }
}