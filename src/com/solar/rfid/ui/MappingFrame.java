

package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import com.solar.rfid.excel.ExcelImporter;
import com.solar.rfid.service.AutoMappingEngine;

public class MappingFrame extends JFrame {

    private JTextField txtBarcode = new JTextField(20);
    private JButton btnScanRFID;

    public MappingFrame() {

        setTitle("Excel Upload – Panel Data");
        setSize(420, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= TOP =================
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Panel Barcode : "));
        top.add(txtBarcode);
        add(top, BorderLayout.NORTH);

        // 🔥 IMPORTANT: Scanner Auto Trigger
        txtBarcode.addActionListener(e -> {
            startMapping();   // Enter press hote hi mapping start
        });

        // Always keep focus ready for scanner
        SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());

        // ================= CENTER BUTTONS =================
        JPanel center = new JPanel(new GridLayout(3, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnUploadExcel = new JButton("Upload Excel");
        btnScanRFID = new JButton("Scan RFID & Map EPC");
        JButton btnStaticData = new JButton("Fill Static Panel Data");

        center.add(btnUploadExcel);
        center.add(btnScanRFID);
        center.add(btnStaticData);

        add(center, BorderLayout.CENTER);

        // ================= ACTIONS =================

        btnUploadExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                ExcelImporter.importExcel(file);
                JOptionPane.showMessageDialog(this,
                        "Excel Imported Successfully");
            }
        });

        btnStaticData.addActionListener(e -> {
            new StaticDataForm(this).setVisible(true);
        });

        // Manual button (optional)
        btnScanRFID.addActionListener(e -> startMapping());
    }

    // 🔥 MAIN MAPPING METHOD
    private void startMapping() {

        String barcode = txtBarcode.getText().trim();

        if (barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Panel Barcode");
            return;
        }

        System.out.println("BARCODE RECEIVED: " + barcode);

        AutoMappingEngine.manualTrigger(barcode);

        // 🔥 AUTO CLEAR FOR NEXT SCAN
        txtBarcode.setText("");

        // 🔥 Keep focus ready
        SwingUtilities.invokeLater(() -> txtBarcode.requestFocusInWindow());
    }

    // 🔥 AUTO TRIGGER (If called externally)
    public void triggerAutoMapping(String barcode) {

        txtBarcode.setText(barcode);
        startMapping();
    }
}
