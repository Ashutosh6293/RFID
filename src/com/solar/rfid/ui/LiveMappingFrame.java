
// package com.solar.rfid.ui;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.KeyEvent;

// import com.solar.rfid.model.PanelData;
// import com.solar.rfid.model.StaticPanelData;
// import com.solar.rfid.barcode.BarcodeListener;

// public class LiveMappingFrame extends JFrame {

//     // ===== Dynamic labels =====
//     private JLabel lblPanelId = new JLabel("-");
//     private JLabel lblEpc = new JLabel("-");
//     private JLabel lblPmax = new JLabel("-");
//     private JLabel lblVoc = new JLabel("-");
//     private JLabel lblIsc = new JLabel("-");
//     private JLabel lblEff = new JLabel("-");
//     private JLabel lblBin = new JLabel("-");
//     private JLabel lblDate = new JLabel("-");

//     // ===== Static labels =====
//     private JLabel lblManufacturer = new JLabel("-");
//     private JLabel lblCellManufacturer = new JLabel("-");
//     private JLabel lblModuleType = new JLabel("-");
//     private JLabel lblModuleCountry = new JLabel("-");
//     private JLabel lblCellCountry = new JLabel("-");
//     private JLabel lblTestLab = new JLabel("-");
//     private JLabel lblIECDate = new JLabel("-");
//     private JLabel lblFactoryCode = new JLabel("-");
//     private JLabel lblLineCode = new JLabel("-");

//     private JLabel status = new JLabel("READY");

//     // ===== IV Curve Panel =====
//     private IVCurvePanel ivPanel = new IVCurvePanel();

//     public LiveMappingFrame() {

//         setTitle("GAUTAM SOLAR PVT. LTD. – LIVE RFID MAPPING");
//         setSize(1100, 700);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLayout(new BorderLayout());

//         // ================= HEADER =================
//         JLabel header = new JLabel("GAUTAM SOLAR PVT. LTD.", SwingConstants.CENTER);
//         header.setFont(new Font("Arial", Font.BOLD, 22));
//         header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//         add(header, BorderLayout.NORTH);

//         // ================= CENTER DATA =================
//         JPanel center = new JPanel(new GridLayout(0, 4, 12, 10));
//         center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

//         // Dynamic
//         addRow(center, "PANEL ID", lblPanelId);
//         addRow(center, "EPC", lblEpc);
//         addRow(center, "PMAX", lblPmax);
//         addRow(center, "VOC", lblVoc);
//         addRow(center, "ISC", lblIsc);
//         addRow(center, "EFF", lblEff);
//         addRow(center, "BIN", lblBin);
//         addRow(center, "DATE", lblDate);

//         // Static
//         addRow(center, "MANUFACTURER", lblManufacturer);
//         addRow(center, "CELL MANUFACTURER", lblCellManufacturer);
//         addRow(center, "MODULE TYPE", lblModuleType);
//         addRow(center, "MODULE COUNTRY", lblModuleCountry);
//         addRow(center, "CELL COUNTRY", lblCellCountry);
//         addRow(center, "TEST LAB", lblTestLab);
//         addRow(center, "IEC DATE", lblIECDate);
//         addRow(center, "FACTORY CODE", lblFactoryCode);
//         addRow(center, "LINE CODE", lblLineCode);

//         // ================= SPLIT (DATA + IV CURVE) =================
//         JSplitPane split = new JSplitPane(
//                 JSplitPane.VERTICAL_SPLIT,
//                 center,
//                 ivPanel
//         );
//         split.setDividerLocation(440);
//         split.setResizeWeight(0.75);

//         ivPanel.setPreferredSize(new Dimension(1000, 200));

//         add(split, BorderLayout.CENTER);

//         // ================= STATUS =================
//         status.setFont(new Font("Arial", Font.BOLD, 14));
//         status.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
//         add(status, BorderLayout.SOUTH);
//     }

//     private void addRow(JPanel panel, String title, JLabel value) {
//         JLabel lbl = new JLabel(title + " : ");
//         lbl.setFont(new Font("Arial", Font.BOLD, 14));
//         value.setFont(new Font("Arial", Font.PLAIN, 14));
//         panel.add(lbl);
//         panel.add(value);
//     }

//     // ======================================================
//     // 🔥 AUTO BARCODE (GLOBAL – NO FOCUS REQUIRED)
//     // ======================================================
//     public void attachBarcodeListener(BarcodeListener listener) {

//         KeyboardFocusManager.getCurrentKeyboardFocusManager()
//             .addKeyEventDispatcher(e -> {

//                 if (e.getID() == KeyEvent.KEY_TYPED) {
//                     listener.keyTyped(e);
//                 }
//                 return false;
//             });
//     }

//     // ================= DYNAMIC DATA =================
//     public void showMappedData(PanelData d, String epc) {

//         lblPanelId.setText(d.getId());
//         lblEpc.setText(epc);
//         lblPmax.setText(d.getPmax());
//         lblVoc.setText(d.getVoc());
//         lblIsc.setText(d.getIsc());
//         lblEff.setText(d.getEff());
//         lblBin.setText(d.getBin());
//         lblDate.setText(d.getDate());

//         // 🔥 IV Curve auto-draw
//         try {
//             double voc = Double.parseDouble(d.getVoc());
//             double isc = Double.parseDouble(d.getIsc());
//             double pmax = Double.parseDouble(d.getPmax());
//             ivPanel.setCurve(voc, isc, pmax);
//         } catch (Exception e) {
//             ivPanel.setCurve(0, 0, 0);
//         }
//     }

//     // ================= STATIC DATA =================
//     public void showStaticData(StaticPanelData s) {
//         if (s == null) return;

//         lblManufacturer.setText(s.getManufacturer());
//         lblCellManufacturer.setText(s.getCellManufacturer());
//         lblModuleType.setText(s.getModuleType());
//         lblModuleCountry.setText(s.getModuleCountry());
//         lblCellCountry.setText(s.getCellCountry());
//         lblTestLab.setText(s.getTestLab());
//         lblIECDate.setText(s.getIecDate());
//         lblFactoryCode.setText(s.getFactoryCode());
//         lblLineCode.setText(s.getLineCode());
//     }

//     public void showMessage(String msg) {
//         status.setText("STATUS : " + msg);
//     }

//     public void resetForNext() {
//         showMessage("READY");
//         // ivPanel.setCurve(0, 0, 0);
//     }
// }

// package com.solar.rfid.ui;

// import javax.swing.*;
// import javax.swing.border.*;
// import java.awt.*;
// import java.awt.event.KeyEvent;
// import com.solar.rfid.model.PanelData;
// import com.solar.rfid.model.StaticPanelData;
// import com.solar.rfid.barcode.BarcodeListener;

// public class LiveMappingFrame extends JFrame {
//     // ===== Dynamic labels =====
//     private JLabel lblPanelId = new JLabel("-");
//     private JLabel lblTid = new JLabel("-");
//     private JLabel lblEpc = new JLabel("-");
//     private JLabel lblPmax = new JLabel("-");
//     private JLabel lblVmpp = new JLabel("-");
//     private JLabel lblImpp = new JLabel("-");
//     private JLabel lblVoc = new JLabel("-");
//     private JLabel lblIsc = new JLabel("-");
//     private JLabel lblEff = new JLabel("-");
//     private JLabel lblBin = new JLabel("-");
//     private JLabel lblDate = new JLabel("-");
//     private JLabel lblManufactureDate = new JLabel("-");

//     // ===== Static labels =====
//     private JLabel lblManufacturer = new JLabel("-");
//     private JLabel lblCellManufacturer = new JLabel("-");
//     private JLabel lblModuleType = new JLabel("-");
//     private JLabel lblModuleCountry = new JLabel("-");
//     private JLabel lblCellCountry = new JLabel("-");
//     private JLabel lblTestLab = new JLabel("-");
//     private JLabel lblIECDate = new JLabel("-");

//     private JLabel status = new JLabel("READY");

//     // ===== IV Curve Panel =====
//     private IVCurvePanel ivPanel = new IVCurvePanel();

//     public LiveMappingFrame() {
//         setTitle("GAUTAM SOLAR - Module Specification");
//         setSize(900, 1000);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLayout(new BorderLayout(0, 0));
//         getContentPane().setBackground(Color.WHITE);

//         // ================= MAIN PANEL WITH SCROLL =================
//         JPanel mainPanel = new JPanel();
//         mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//         mainPanel.setBackground(Color.WHITE);

//         // Header
//         JPanel headerPanel = createHeaderPanel();
//         mainPanel.add(headerPanel);

//         // Module Serial and TID
//         JPanel serialPanel = createSerialPanel();
//         mainPanel.add(serialPanel);

//         // Data Table
//         JPanel tablePanel = createDataTablePanel();
//         mainPanel.add(tablePanel);

//         // IV Curve Section Title
//         JPanel curveTitlePanel = createCurveTitlePanel();
//         mainPanel.add(curveTitlePanel);

//         // IV Curve Graph
//         ivPanel.setPreferredSize(new Dimension(850, 400));
//         ivPanel.setMaximumSize(new Dimension(2000, 400));
//         JPanel curveContainer = new JPanel(new BorderLayout());
//         curveContainer.setBackground(Color.WHITE);
//         curveContainer.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
//         curveContainer.add(ivPanel, BorderLayout.CENTER);
//         mainPanel.add(curveContainer);

//         // Scroll pane for main content
//         JScrollPane scrollPane = new JScrollPane(mainPanel);
//         scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//         scrollPane.setBorder(null);
//         add(scrollPane, BorderLayout.CENTER);

//         // ================= STATUS BAR =================
//         JPanel statusPanel = new JPanel(new BorderLayout());
//         statusPanel.setBackground(new Color(33, 150, 243));
//         statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

//         status.setFont(new Font("Arial", Font.BOLD, 14));
//         status.setForeground(Color.WHITE);
//         statusPanel.add(status, BorderLayout.WEST);

//         add(statusPanel, BorderLayout.SOUTH);
//     }

//     private JPanel createHeaderPanel() {
//         JPanel headerPanel = new JPanel(new BorderLayout());
//         headerPanel.setBackground(Color.WHITE);
//         headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
//         headerPanel.setMaximumSize(new Dimension(2000, 120));

//         // Logo and Company Name (Center)
//         JPanel centerPanel = new JPanel();
//         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
//         centerPanel.setBackground(Color.WHITE);

//         JLabel logoLabel = new JLabel("GAUTAM");
//         logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
//         logoLabel.setForeground(new Color(200, 16, 46));
//         logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

//         JLabel solarLabel = new JLabel("SOLAR");
//         solarLabel.setFont(new Font("Arial", Font.BOLD, 28));
//         solarLabel.setForeground(new Color(200, 16, 46));
//         solarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

//         centerPanel.add(logoLabel);
//         centerPanel.add(solarLabel);

//         headerPanel.add(centerPanel, BorderLayout.CENTER);

//         // Company details below logo
//         JPanel detailsPanel = new JPanel();
//         detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
//         detailsPanel.setBackground(Color.WHITE);
//         detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

//         JLabel company = new JLabel("Gautam Solar Private Limited");
//         JLabel addr1 = new JLabel("7 km Milestone, Tosham Road");
//         JLabel addr2 = new JLabel("Dist. Bhiwani");
//         JLabel addr3 = new JLabel("Bawani Khera HR 127032");
//         JLabel country = new JLabel("India");

//         company.setFont(new Font("Arial", Font.BOLD, 12));
//         addr1.setFont(new Font("Arial", Font.PLAIN, 11));
//         addr2.setFont(new Font("Arial", Font.PLAIN, 11));
//         addr3.setFont(new Font("Arial", Font.PLAIN, 11));
//         country.setFont(new Font("Arial", Font.BOLD, 11));
//         country.setForeground(new Color(200, 16, 46));

//         company.setAlignmentX(Component.CENTER_ALIGNMENT);
//         addr1.setAlignmentX(Component.CENTER_ALIGNMENT);
//         addr2.setAlignmentX(Component.CENTER_ALIGNMENT);
//         addr3.setAlignmentX(Component.CENTER_ALIGNMENT);
//         country.setAlignmentX(Component.CENTER_ALIGNMENT);

//         detailsPanel.add(company);
//         detailsPanel.add(addr1);
//         detailsPanel.add(addr2);
//         detailsPanel.add(addr3);
//         detailsPanel.add(country);

//         JPanel wrapper = new JPanel(new BorderLayout());
//         wrapper.setBackground(Color.WHITE);
//         wrapper.add(headerPanel, BorderLayout.NORTH);
//         wrapper.add(detailsPanel, BorderLayout.CENTER);
//         wrapper.setMaximumSize(new Dimension(2000, 180));

//         return wrapper;
//     }

//     private JPanel createSerialPanel() {
//         JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
//         panel.setBackground(Color.WHITE);
//         panel.setMaximumSize(new Dimension(2000, 60));

//         JLabel serialLabel = new JLabel("Module Serial Number:");
//         serialLabel.setFont(new Font("Arial", Font.BOLD, 12));
//         lblPanelId.setFont(new Font("Arial", Font.PLAIN, 12));
//         lblPanelId.setForeground(new Color(33, 150, 243));

//         JLabel tidLabel = new JLabel("TID:");
//         tidLabel.setFont(new Font("Arial", Font.BOLD, 12));
//         lblTid.setFont(new Font("Arial", Font.PLAIN, 12));

//         panel.add(serialLabel);
//         panel.add(lblPanelId);
//         panel.add(Box.createHorizontalStrut(20));
//         panel.add(tidLabel);
//         panel.add(lblTid);

//         return panel;
//     }

//     private JPanel createDataTablePanel() {
//         JPanel container = new JPanel(new BorderLayout());
//         container.setBackground(Color.WHITE);
//         container.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));
//         container.setMaximumSize(new Dimension(2000, 450));

//         JLabel title = new JLabel("Detailed Specification:");
//         title.setFont(new Font("Arial", Font.BOLD, 13));
//         title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
//         container.add(title, BorderLayout.NORTH);

//         // Create table
//         String[] columnNames = {"Sr No.", "Parameter", "Value"};
//         Object[][] data = new Object[15][3];

//         // Initialize data
//         data[0] = new Object[]{"1", "Name of the Manufacturer of PV Module", lblManufacturer};
//         data[1] = new Object[]{"2", "Name of the Manufacturer of Solar Cell", lblCellManufacturer};
//         data[2] = new Object[]{"3", "Module Type", lblModuleType};
//         data[3] = new Object[]{"4", "Month & Year of the Manufacture of Module", lblManufactureDate};
//         data[4] = new Object[]{"5", "Month & Year of the Manufacture of Solar Cell", lblDate};
//         data[5] = new Object[]{"6", "Country of Origin for PV Module", lblModuleCountry};
//         data[6] = new Object[]{"7", "Country of Origin for Solar Cell", lblCellCountry};
//         data[7] = new Object[]{"8", "Power: P-Max of the Module", lblPmax};
//         data[8] = new Object[]{"9", "Voltage: V-Max of the Module", lblVmpp};
//         data[9] = new Object[]{"10", "Current: I-Max of the Module", lblImpp};
//         data[10] = new Object[]{"11", "Fill Factor (FF) of the Module", lblEff};
//         data[11] = new Object[]{"12", "VOC", lblVoc};
//         data[12] = new Object[]{"13", "ISC", lblIsc};
//         data[13] = new Object[]{"14", "Name of The Test Lab Issuing IEC Certificate", lblTestLab};
//         data[14] = new Object[]{"15", "Date of Obtaining IEC Certificate", lblIECDate};

//         JPanel tablePanel = new JPanel();
//         tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
//         tablePanel.setBackground(Color.WHITE);

//         // Add header row
//         JPanel headerRow = createTableRow("Sr No.", "Parameter", "Value", true);
//         tablePanel.add(headerRow);

//         // Add data rows
//         for (int i = 0; i < 15; i++) {
//             JPanel row = createTableRow(
//                 data[i][0].toString(),
//                 data[i][1].toString(),
//                 (JLabel) data[i][2],
//                 false
//             );
//             tablePanel.add(row);
//         }

//         container.add(tablePanel, BorderLayout.CENTER);

//         return container;
//     }

//     private JPanel createTableRow(String col1, String col2, Object col3, boolean isHeader) {
//         JPanel row = new JPanel(new GridBagLayout());
//         row.setBackground(Color.WHITE);
//         row.setMaximumSize(new Dimension(2000, 35));

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.fill = GridBagConstraints.BOTH;
//         gbc.gridy = 0;
//         gbc.ipady = 8;

//         // Column 1 - Sr No (10%)
//         gbc.gridx = 0;
//         gbc.weightx = 0.08;
//         JLabel label1 = new JLabel(col1, SwingConstants.CENTER);
//         styleTableCell(label1, isHeader);
//         row.add(label1, gbc);

//         // Column 2 - Parameter (55%)
//         gbc.gridx = 1;
//         gbc.weightx = 0.55;
//         JLabel label2 = new JLabel(" " + col2);
//         styleTableCell(label2, isHeader);
//         row.add(label2, gbc);

//         // Column 3 - Value (35%)
//         gbc.gridx = 2;
//         gbc.weightx = 0.37;
//         if (col3 instanceof JLabel) {
//             JLabel valueLabel = (JLabel) col3;
//             valueLabel.setText(" " + valueLabel.getText());
//             styleTableCell(valueLabel, isHeader);
//             row.add(valueLabel, gbc);
//         } else {
//             JLabel label3 = new JLabel(" " + col3.toString());
//             styleTableCell(label3, isHeader);
//             row.add(label3, gbc);
//         }

//         return row;
//     }

//     private void styleTableCell(JLabel label, boolean isHeader) {
//         if (isHeader) {
//             label.setFont(new Font("Arial", Font.BOLD, 11));
//             label.setBackground(new Color(240, 240, 240));
//         } else {
//             label.setFont(new Font("Arial", Font.PLAIN, 11));
//             label.setBackground(Color.WHITE);
//         }
//         label.setOpaque(true);
//         label.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
//     }

//     private JPanel createCurveTitlePanel() {
//         JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
//         panel.setBackground(Color.WHITE);
//         panel.setMaximumSize(new Dimension(2000, 40));

//         JLabel title = new JLabel("IV Characteristics of the Module:");
//         title.setFont(new Font("Arial", Font.BOLD, 13));
//         panel.add(title);

//         return panel;
//     }

//     // ====================================================== 
//     // 🔥 AUTO BARCODE (GLOBAL – NO FOCUS REQUIRED) 
//     // ====================================================== 
//     public void attachBarcodeListener(BarcodeListener listener) {
//         KeyboardFocusManager.getCurrentKeyboardFocusManager()
//             .addKeyEventDispatcher(e -> {
//                 if (e.getID() == KeyEvent.KEY_TYPED) {
//                     listener.keyTyped(e);
//                 }
//                 return false;
//             });
//     }

//     // ================= DYNAMIC DATA ================= 
//     public void showMappedData(PanelData d, String epc) {
//         lblPanelId.setText(d.getId());
//         lblTid.setText(epc);
//         lblPmax.setText(d.getPmax());
//         lblVoc.setText(d.getVoc());
//         lblIsc.setText(d.getIsc());
//         lblEff.setText(d.getEff());
//         lblBin.setText(d.getBin());
//         lblDate.setText(d.getDate());
//         lblManufactureDate.setText(d.getDate());

//         // Calculate Vmpp and Impp from Pmax
//         try {
//             double voc = Double.parseDouble(d.getVoc());
//             double isc = Double.parseDouble(d.getIsc());
//             double pmax = Double.parseDouble(d.getPmax());

//             // Typical MPP is around 0.78 * Voc
//             double vmpp = voc * 0.78;
//             double impp = pmax / vmpp;

//             lblVmpp.setText(String.format("%.5f", vmpp));
//             lblImpp.setText(String.format("%.2f", impp));

//             // Update IV curve
//             ivPanel.setCurve(voc, isc, pmax);
//         } catch (Exception e) {
//             lblVmpp.setText("-");
//             lblImpp.setText("-");
//             ivPanel.setCurve(0, 0, 0);
//         }
//     }

//     // ================= STATIC DATA ================= 
//     public void showStaticData(StaticPanelData s) {
//         if (s == null) return;

//         lblManufacturer.setText(s.getManufacturer());
//         lblCellManufacturer.setText(s.getCellManufacturer());
//         lblModuleType.setText(s.getModuleType());
//         lblModuleCountry.setText(s.getModuleCountry());
//         lblCellCountry.setText(s.getCellCountry());
//         lblTestLab.setText(s.getTestLab());
//         lblIECDate.setText(s.getIecDate());
//     }

//     public void showMessage(String msg) {
//         status.setText("STATUS: " + msg);
//     }

//     public void resetForNext() {
//         showMessage("READY");
//     }
// }

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