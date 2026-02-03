
package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import com.solar.rfid.model.PanelData;
import com.solar.rfid.model.StaticPanelData;
import com.solar.rfid.barcode.BarcodeListener;

public class LiveMappingFrame extends JFrame {

    // ===== Dynamic labels =====
    private JLabel lblPanelId = new JLabel("-");
    private JLabel lblEpc = new JLabel("-");
    private JLabel lblPmax = new JLabel("-");
    private JLabel lblVoc = new JLabel("-");
    private JLabel lblIsc = new JLabel("-");
    private JLabel lblEff = new JLabel("-");
    private JLabel lblBin = new JLabel("-");
    private JLabel lblDate = new JLabel("-");

    // ===== Static labels =====
    private JLabel lblManufacturer = new JLabel("-");
    private JLabel lblCellManufacturer = new JLabel("-");
    private JLabel lblModuleType = new JLabel("-");
    private JLabel lblModuleCountry = new JLabel("-");
    private JLabel lblCellCountry = new JLabel("-");
    private JLabel lblTestLab = new JLabel("-");
    private JLabel lblIECDate = new JLabel("-");
    private JLabel lblFactoryCode = new JLabel("-");
    private JLabel lblLineCode = new JLabel("-");

    private JLabel status = new JLabel("READY");

    // ===== IV Curve Panel =====
    private IVCurvePanel ivPanel = new IVCurvePanel();

    public LiveMappingFrame() {

        setTitle("GAUTAM SOLAR PVT. LTD. – LIVE RFID MAPPING");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JLabel header = new JLabel("GAUTAM SOLAR PVT. LTD.", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // ================= CENTER DATA =================
        JPanel center = new JPanel(new GridLayout(0, 4, 12, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Dynamic
        addRow(center, "PANEL ID", lblPanelId);
        addRow(center, "EPC", lblEpc);
        addRow(center, "PMAX", lblPmax);
        addRow(center, "VOC", lblVoc);
        addRow(center, "ISC", lblIsc);
        addRow(center, "EFF", lblEff);
        addRow(center, "BIN", lblBin);
        addRow(center, "DATE", lblDate);

        // Static
        addRow(center, "MANUFACTURER", lblManufacturer);
        addRow(center, "CELL MANUFACTURER", lblCellManufacturer);
        addRow(center, "MODULE TYPE", lblModuleType);
        addRow(center, "MODULE COUNTRY", lblModuleCountry);
        addRow(center, "CELL COUNTRY", lblCellCountry);
        addRow(center, "TEST LAB", lblTestLab);
        addRow(center, "IEC DATE", lblIECDate);
        addRow(center, "FACTORY CODE", lblFactoryCode);
        addRow(center, "LINE CODE", lblLineCode);

        // ================= SPLIT (DATA + IV CURVE) =================
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                center,
                ivPanel
        );
        split.setDividerLocation(440);
        split.setResizeWeight(0.75);

        ivPanel.setPreferredSize(new Dimension(1000, 200));

        add(split, BorderLayout.CENTER);

        // ================= STATUS =================
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(status, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, String title, JLabel value) {
        JLabel lbl = new JLabel(title + " : ");
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        value.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl);
        panel.add(value);
    }

    // ======================================================
    // 🔥 AUTO BARCODE (GLOBAL – NO FOCUS REQUIRED)
    // ======================================================
    public void attachBarcodeListener(BarcodeListener listener) {

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {

                if (e.getID() == KeyEvent.KEY_TYPED) {
                    listener.keyTyped(e);
                }
                return false;
            });
    }

    // ================= DYNAMIC DATA =================
    public void showMappedData(PanelData d, String epc) {

        lblPanelId.setText(d.getId());
        lblEpc.setText(epc);
        lblPmax.setText(d.getPmax());
        lblVoc.setText(d.getVoc());
        lblIsc.setText(d.getIsc());
        lblEff.setText(d.getEff());
        lblBin.setText(d.getBin());
        lblDate.setText(d.getDate());

        // 🔥 IV Curve auto-draw
        try {
            double voc = Double.parseDouble(d.getVoc());
            double isc = Double.parseDouble(d.getIsc());
            double pmax = Double.parseDouble(d.getPmax());
            ivPanel.setCurve(voc, isc, pmax);
        } catch (Exception e) {
            ivPanel.setCurve(0, 0, 0);
        }
    }

    // ================= STATIC DATA =================
    public void showStaticData(StaticPanelData s) {
        if (s == null) return;

        lblManufacturer.setText(s.getManufacturer());
        lblCellManufacturer.setText(s.getCellManufacturer());
        lblModuleType.setText(s.getModuleType());
        lblModuleCountry.setText(s.getModuleCountry());
        lblCellCountry.setText(s.getCellCountry());
        lblTestLab.setText(s.getTestLab());
        lblIECDate.setText(s.getIecDate());
        lblFactoryCode.setText(s.getFactoryCode());
        lblLineCode.setText(s.getLineCode());
    }

    public void showMessage(String msg) {
        status.setText("STATUS : " + msg);
    }

    public void resetForNext() {
        showMessage("READY");
        // ivPanel.setCurve(0, 0, 0);
    }
}
