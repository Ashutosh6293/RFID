

package com.solar.rfid;

import javax.swing.SwingUtilities;
import com.solar.rfid.rfid.RFIDService;
import com.solar.rfid.ui.CombinedFrame;
import com.solar.rfid.service.AutoMappingEngine;

public class MainApp {

    private static boolean started = false;

    public static void main(String[] args) {
        if (started) return;
        started = true;

        System.out.println("Application started");

        boolean ok = RFIDService.initReader("COM4");
        if (!ok) {
            System.out.println("❌ RFID init failed on COM4");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            CombinedFrame ui = new CombinedFrame();
            ui.setVisible(true);

            // Pass the single CombinedFrame to AutoMappingEngine
            AutoMappingEngine engine = new AutoMappingEngine(ui);
            engine.start();

            System.out.println("✅ UI READY – SCANNER USING TEXTFIELD ONLY");
        });
    }
}