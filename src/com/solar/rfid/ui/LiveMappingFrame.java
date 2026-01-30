
package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.KeyboardFocusManager;

import com.solar.rfid.model.PanelData;
import com.solar.rfid.barcode.BarcodeListener;

public class LiveMappingFrame extends JFrame {

    // 🔹 value labels
    private JLabel lblPanelId = new JLabel("-");
    private JLabel lblEpc     = new JLabel("-");
    private JLabel lblPmax    = new JLabel("-");
    private JLabel lblVoc     = new JLabel("-");
    private JLabel lblIsc     = new JLabel("-");
    private JLabel lblEff     = new JLabel("-");
    private JLabel lblBin     = new JLabel("-");
    private JLabel lblDate    = new JLabel("-");

    private JLabel status = new JLabel("READY");

    public LiveMappingFrame() {

        setTitle("GAUTAM SOLAR PVT. LTD. – LIVE RFID MAPPING");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JLabel header = new JLabel("GAUTAM SOLAR PVT. LTD.", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // ================= CENTER (CERTIFICATE STYLE) =================
        JPanel center = new JPanel(new GridLayout(0, 2, 12, 12));
        center.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addRow(center, "PANEL ID", lblPanelId);
        addRow(center, "EPC", lblEpc);
        addRow(center, "PMAX", lblPmax);
        addRow(center, "VOC", lblVoc);
        addRow(center, "ISC", lblIsc);
        addRow(center, "EFF", lblEff);
        addRow(center, "BIN", lblBin);
        addRow(center, "DATE", lblDate);

        add(center, BorderLayout.CENTER);

        // ================= STATUS =================
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(status, BorderLayout.SOUTH);
    }

    // 🔹 helper to add label-value row
    private void addRow(JPanel panel, String title, JLabel value) {
        JLabel lbl = new JLabel(title + " : ");
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        value.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(lbl);
        panel.add(value);
    }

    // ================= BARCODE LISTENER (UNCHANGED LOGIC) =================
    public void attachBarcodeListener(BarcodeListener listener) {

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {

                if (e.getID() == KeyEvent.KEY_TYPED) {
                    listener.keyTyped(e);   // 🔥 auto barcode capture
                }
                return false;
            });
    }

    // ================= DATA DISPLAY =================
    public void showMappedData(PanelData d, String epc) {

        lblPanelId.setText(d.getId());
        lblEpc.setText(epc);
        lblPmax.setText(d.getPmax());
        lblVoc.setText(d.getVoc());
        lblIsc.setText(d.getIsc());
        lblEff.setText(d.getEff());
        lblBin.setText(d.getBin());
        lblDate.setText(d.getDate());
    }

    public void clear() {
        lblPanelId.setText("-");
        lblEpc.setText("-");
        lblPmax.setText("-");
        lblVoc.setText("-");
        lblIsc.setText("-");
        lblEff.setText("-");
        lblBin.setText("-");
        lblDate.setText("-");
    }

    public void showMessage(String msg) {
        status.setText("STATUS : " + msg);
    }
}
