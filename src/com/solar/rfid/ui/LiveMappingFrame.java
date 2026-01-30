package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import com.solar.rfid.model.PanelData;
import com.solar.rfid.barcode.BarcodeListener;

public class LiveMappingFrame extends JFrame {

    private JTextArea area = new JTextArea();
    private JLabel status = new JLabel("READY");

    public LiveMappingFrame() {

        setTitle("GAUTAM SOLAR PVT. LTD.");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    public void attachBarcodeListener(BarcodeListener listener) {

    KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(e -> {

            if (e.getID() == KeyEvent.KEY_TYPED) {
                listener.keyTyped(e);   // ⬅️ direct pass
            }
            return false;
        });
    }


    public void showMappedData(PanelData d, String epc) {

        area.setText(
            "PANEL ID : " + d.getId() + "\n" +
            "EPC      : " + epc + "\n" +
            "PMAX     : " + d.getPmax() + "\n" +
            "VOC      : " + d.getVoc() + "\n" +
            "ISC      : " + d.getIsc() + "\n" +
            "EFF      : " + d.getEff() + "\n" +
            "BIN      : " + d.getBin() + "\n" +
            "DATE     : " + d.getDate()
        );
    }

    public void clear() {
        area.setText("");
    }

    public void showMessage(String msg) {
        status.setText(msg);
    }
}
