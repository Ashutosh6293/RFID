package com.solar.rfid.ui;

import javax.swing.*;
import com.solar.rfid.rfid.RFIDService;
import com.solar.rfid.db.PanelRepository;
import com.solar.rfid.excel.ExcelImporter;
import java.io.File;

public class MappingFrame extends JFrame {

    public MappingFrame() {

        setTitle("Solar Panel RFID Mapping");

        JTextField txtSerial = new JTextField(25);
        JButton btnExcel = new JButton("Upload Excel");
        JButton btnRFID = new JButton("Scan RFID & Map EPC");

        // 🔹 INIT RFID once (CHANGE COM if needed)
        RFIDService.initReader("COM4");

        // Excel upload (UNCHANGED)
        btnExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                ExcelImporter.importExcel(f);
                JOptionPane.showMessageDialog(this, "Excel uploaded to DB");
            }
        });

        // RFID mapping
        btnRFID.addActionListener(e -> {
            String serial = txtSerial.getText().trim();
            if (serial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Scan panel barcode first");
                return;
            }

            String epc = RFIDService.readSingleEPC(3000);
            if (epc != null) {
                PanelRepository.updateEPC(serial, epc);
                JOptionPane.showMessageDialog(this,
                        "EPC mapped successfully:\n" + epc);
            } else {
                JOptionPane.showMessageDialog(this,
                        "RFID tag not detected");
            }
        });

        setLayout(new java.awt.FlowLayout());
        add(new JLabel("Panel Barcode:"));
        add(txtSerial);
        add(btnExcel);
        add(btnRFID);

        setSize(500, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
