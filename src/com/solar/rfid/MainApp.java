
package com.solar.rfid;

import com.solar.rfid.ui.MappingFrame;
import com.solar.rfid.ui.LiveMappingFrame;
import com.solar.rfid.rfid.RFIDInitializer;
import com.solar.rfid.service.AutoMappingEngine;

public class MainApp {

    public static void main(String[] args) {

        // RFID SDK init (port same, no change)
        RFIDInitializer.init();

        // OLD Excel + Manual UI (unchanged)
        MappingFrame excelFrame = new MappingFrame();
        excelFrame.setVisible(true);

        // NEW Live Auto UI
        LiveMappingFrame liveUI = new LiveMappingFrame();
        liveUI.setVisible(true);

        // Auto engine
        AutoMappingEngine engine = new AutoMappingEngine(liveUI);
        engine.start();
    }
}


