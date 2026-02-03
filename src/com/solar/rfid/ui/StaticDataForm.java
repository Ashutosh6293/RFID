package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;

import com.solar.rfid.model.StaticPanelData;
import com.solar.rfid.db.StaticDataRepository;

public class StaticDataForm extends JDialog {

    private JTextField manufacturer = new JTextField();
    private JTextField cellManufacturer = new JTextField();
    private JTextField moduleType = new JTextField();
    private JTextField moduleCountry = new JTextField();
    private JTextField cellCountry = new JTextField();
    private JTextField testLab = new JTextField();
    private JTextField iecDate = new JTextField();
    private JTextField factoryCode = new JTextField();
    private JTextField lineCode = new JTextField();

    public StaticDataForm(JFrame parent) {

        super(parent, "Static Panel Data", true);
        setSize(450, 420);
        setLayout(new GridLayout(10, 2, 8, 8));
        setLocationRelativeTo(parent);

        add(new JLabel("Manufacturer"));
        add(manufacturer);

        add(new JLabel("Cell Manufacturer"));
        add(cellManufacturer);

        add(new JLabel("Module Type"));
        add(moduleType);

        add(new JLabel("Module Country"));
        add(moduleCountry);

        add(new JLabel("Cell Country"));
        add(cellCountry);

        add(new JLabel("Test Lab"));
        add(testLab);

        add(new JLabel("IEC Date"));
        add(iecDate);

        add(new JLabel("Factory Code"));
        add(factoryCode);

        add(new JLabel("Line Code"));
        add(lineCode);

        JButton save = new JButton("SAVE");
        add(new JLabel());
        add(save);

        save.addActionListener(e -> {

            StaticPanelData d = new StaticPanelData();
            d.setManufacturer(manufacturer.getText());
            d.setCellManufacturer(cellManufacturer.getText());
            d.setModuleType(moduleType.getText());
            d.setModuleCountry(moduleCountry.getText());
            d.setCellCountry(cellCountry.getText());
            d.setTestLab(testLab.getText());
            d.setIecDate(iecDate.getText());
            d.setFactoryCode(factoryCode.getText());
            d.setLineCode(lineCode.getText());

            StaticDataRepository.save(d);
            dispose();
        });
    }
}
