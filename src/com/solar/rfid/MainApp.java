// package com.solar.rfid;

// import javax.swing.SwingUtilities;

// import com.solar.rfid.rfid.RFIDService;
// import com.solar.rfid.ui.MappingFrame;
// import com.solar.rfid.ui.LiveMappingFrame;
// import com.solar.rfid.service.AutoMappingEngine;

// public class MainApp {

//     private static boolean started = false;

//     public static void main(String[] args) {

//         if (started) return;
//         started = true;

//         System.out.println("Application started");

//         // 🔒 RFID INIT — ONLY HERE, ONLY ONCE
//         boolean ok = RFIDService.initReader("COM4");
//         if (!ok) {
//             System.out.println("❌ RFID init failed on COM4");
//             return;
//         }

//         // ✅ Always start Swing on EDT
//         SwingUtilities.invokeLater(() -> {

//             // ===============================
//             // 1️⃣ Excel Upload UI (Manual)
//             // ===============================
//             MappingFrame excelUI = new MappingFrame();
//             excelUI.setTitle("Excel Upload – Panel Data");
//             excelUI.setLocation(50, 50);
//             excelUI.setVisible(true);

//             // ===============================
//             // 2️⃣ Live Auto Mapping UI
//             // ===============================
//             LiveMappingFrame liveUI = new LiveMappingFrame();
//             liveUI.setTitle("GAUTAM SOLAR PVT. LTD. – LIVE RFID MAPPING");
//             liveUI.setLocation(700, 50);
//             liveUI.setVisible(true);

//             // ===============================
//             // 3️⃣ Auto Mapping Engine
//             // ===============================
//             AutoMappingEngine engine = new AutoMappingEngine(liveUI);
//             engine.start();

//             System.out.println("✅ UI READY – AUTO SCAN ACTIVE");
//         });
//     }
// }






package com.solar.rfid;

import javax.swing.SwingUtilities;

import com.solar.rfid.rfid.RFIDService;
import com.solar.rfid.ui.MappingFrame;
import com.solar.rfid.ui.LiveMappingFrame;
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

            MappingFrame excelUI = new MappingFrame();
            excelUI.setLocation(50, 50);
            excelUI.setVisible(true);

            LiveMappingFrame liveUI = new LiveMappingFrame();
            liveUI.setLocation(700, 50);
            liveUI.setVisible(true);

            AutoMappingEngine engine = new AutoMappingEngine(liveUI);
            engine.start();

            System.out.println("✅ UI READY – SCANNER USING TEXTFIELD ONLY");
        });
    }
}
