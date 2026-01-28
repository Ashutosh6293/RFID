package com.solar.rfid;

import com.solar.rfid.ui.MappingFrame;
import com.uhf.UHFMainForm;

public class MainApp {

    public static UHFMainForm UHF_WINDOW; // keep reference

    public static void main(String[] args) {

        // 🔴 MUST stay alive for SDK to work
        UHF_WINDOW = new UHFMainForm();
        UHF_WINDOW.setVisible(true);   // DO NOT hide immediately

        // Your application UI
        new MappingFrame().setVisible(true);
    }
}





