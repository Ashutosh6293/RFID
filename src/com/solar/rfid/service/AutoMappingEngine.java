

// package com.solar.rfid.service;

// import com.solar.rfid.barcode.BarcodeListener;
// import com.solar.rfid.db.PanelRepository;
// import com.solar.rfid.db.StaticDataRepository;
// import com.solar.rfid.model.PanelData;
// import com.solar.rfid.model.StaticPanelData;
// import com.solar.rfid.rfid.RFIDAutoScanner;
// import com.solar.rfid.rfid.RFIDService;
// import com.solar.rfid.ui.LiveMappingFrame;

// public class AutoMappingEngine {

//     private static AutoMappingEngine instance;   // 🔥 for manual trigger

//     private final LiveMappingFrame ui;
//     private volatile boolean busy = false;

//     public AutoMappingEngine(LiveMappingFrame ui) {
//         this.ui = ui;
//         instance = this; // 🔥 register instance
//         ui.attachBarcodeListener(new BarcodeListener(this::onBarcode));
//     }

//     public void start() {
//         ui.showMessage("READY");
//     }

//     // 🔥 MANUAL TRIGGER (MappingFrame se call hoga)
//     public static void manualTrigger(String barcode) {
//         if (instance != null) {
//             instance.onBarcode(barcode);
//         }
//     }

//     // 🔥 COMMON LOGIC (auto + manual dono)
//     private void onBarcode(String barcode) {

//         if (busy || barcode == null || barcode.isEmpty()) {
//             System.out.println("Ignoring barcode: " + barcode);
//             return;
//         }
//         busy = true;

//         new Thread(() -> {
//             try {
//                 System.out.println("PROCESSING BARCODE: " + barcode);
//                 ui.showMessage("BARCODE : " + barcode);

//                 // ================= RFID READ =================
//                 String epc = RFIDAutoScanner.readEPC();
//                 if (epc == null) {
//                     ui.showMessage("RFID NOT DETECTED");
//                     return;
//                 }

//                 System.out.println("EPC READ = " + epc);

//                 // ================= DB UPDATE =================
//                 PanelRepository.updateEPC(barcode, epc);

//                 PanelData data = PanelRepository.fetchPanel(barcode);
//                 if (data == null || data.getId() == null) {
//                     ui.showMessage("DATA NOT FOUND");
//                     return;
//                 }

//                 // ================= STATIC DATA =================
//                 StaticPanelData staticData = StaticDataRepository.load();

//                 // ================= TAG WRITE =================
//                 boolean ok = RFIDService.writePanelDataToTag(data);
//                 if (!ok) {
//                     ui.showMessage("TAG WRITE FAILED");
//                     return;
//                 }

//                 // ================= UI UPDATE =================
//                 ui.showMappedData(data, epc);
//                 ui.showStaticData(staticData);
//                 ui.showMessage("TAG MAPPED & DATA WRITTEN");

//             } catch (Exception e) {
//                 e.printStackTrace();
//                 ui.showMessage("ERROR");
//             } finally {
//                 try { Thread.sleep(400); } catch (Exception ignored) {}
//                 busy = false;
//                 ui.resetForNext(); // 🔁 next panel ready
//             }
//         }).start();
//     }
// }




//working
// package com.solar.rfid.service;

// import com.solar.rfid.db.PanelRepository;
// import com.solar.rfid.db.StaticDataRepository;
// import com.solar.rfid.model.PanelData;
// import com.solar.rfid.model.StaticPanelData;
// import com.solar.rfid.rfid.RFIDAutoScanner;
// import com.solar.rfid.rfid.RFIDService;
// import com.solar.rfid.ui.CombinedFrame;

// public class AutoMappingEngine {

//     private static AutoMappingEngine instance;
//     private final CombinedFrame ui;          // ✅ CombinedFrame (not LiveMappingFrame)
//     private volatile boolean busy = false;

//     public AutoMappingEngine(CombinedFrame ui) {
//         this.ui = ui;
//         instance = this;
//     }

//     public void start() {
//         ui.showMessage("READY");
//     }

//     // 🔥 Called from MappingFrame / CombinedFrame sidebar
//     public static void manualTrigger(String barcode) {
//         if (instance != null) {
//             instance.processBarcode(barcode);
//         }
//     }

//     private void processBarcode(String barcode) {

//         if (busy || barcode == null || barcode.isEmpty()) {
//             System.out.println("Ignoring barcode: " + barcode);
//             return;
//         }

//         busy = true;

//         new Thread(() -> {
//             try {
//                 System.out.println("PROCESSING BARCODE: " + barcode);
//                 ui.showMessage("BARCODE : " + barcode);

//                 // ================= RFID READ =================
//                 String epc = RFIDAutoScanner.readEPC();
//                 if (epc == null) {
//                     ui.showMessage("RFID NOT DETECTED");
//                     return;
//                 }

//                 System.out.println("EPC READ = " + epc);

//                 // ================= DUPLICATE CHECK =================
//                 if (PanelRepository.isEpcAlreadyMapped(epc)) {
//                     ui.showMessage("EPC ALREADY MAPPED");
//                     PanelRepository.updateEPC(barcode, epc, "NOT_SUCCESS");
//                     return;
//                 }

//                 // ================= FETCH PANEL =================
//                 PanelData data = PanelRepository.fetchPanel(barcode);
//                 if (data == null || data.getId() == null) {
//                     ui.showMessage("DATA NOT FOUND");
//                     return;
//                 }

//                 // ================= STATIC DATA =================
//                 StaticPanelData staticData = StaticDataRepository.load();

//                 // ================= TAG WRITE =================
//                 boolean writeOk = RFIDService.writePanelDataToTag(data);

//                 if (!writeOk) {
//                     PanelRepository.updateEPC(barcode, epc, "NOT_SUCCESS");
//                     ui.showMessage("TAG WRITE FAILED");
//                     return;
//                 }

//                 // ================= SUCCESS =================
//                 PanelRepository.updateEPC(barcode, epc, "SUCCESS");

//                 ui.showMappedData(data, epc);
//                 ui.showStaticData(staticData);
//                 ui.showMessage("TAG MAPPED & DATA WRITTEN");

//             } catch (Exception e) {
//                 e.printStackTrace();
//                 ui.showMessage("ERROR");
//             } finally {
//                 try { Thread.sleep(400); } catch (Exception ignored) {}
//                 busy = false;
//                 ui.resetForNext();
//             }

//         }).start();
//     }
// }











package com.solar.rfid.service;

import com.solar.rfid.db.PanelRepository;
import com.solar.rfid.db.StaticDataRepository;
import com.solar.rfid.model.PanelData;
import com.solar.rfid.model.StaticPanelData;
import com.solar.rfid.rfid.RFIDAutoScanner;
import com.solar.rfid.rfid.RFIDService;
import com.solar.rfid.ui.CombinedFrame;

public class AutoMappingEngine {

    private static AutoMappingEngine instance;
    private final CombinedFrame ui;
    private volatile boolean busy = false;

    public AutoMappingEngine(CombinedFrame ui) {
        this.ui = ui;
        instance = this;
    }

    public void start() {
        ui.showMessage("READY – Scan next panel");
    }

    /** Called from CombinedFrame sidebar on barcode scan */
    public static void manualTrigger(String barcode) {
        if (instance != null) {
            instance.processBarcode(barcode);
        }
    }

    private void processBarcode(String barcode) {

        if (busy || barcode == null || barcode.isEmpty()) {
            System.out.println("Ignoring barcode: " + barcode);
            return;
        }

        busy = true;

        new Thread(() -> {
            try {
                System.out.println("PROCESSING BARCODE: " + barcode);
                ui.showMessage("Processing: " + barcode);

                // ── RFID READ ──────────────────────────────────────────────
                String epc = RFIDAutoScanner.readEPC();
                if (epc == null) {
                    ui.showError("RFID NOT DETECTED – Tag missing or out of range");
                    return;
                }
                System.out.println("EPC READ = " + epc);

                // ── DUPLICATE CHECK ────────────────────────────────────────
                if (PanelRepository.isEpcAlreadyMapped(epc)) {
                    ui.showError("EPC ALREADY MAPPED – Tag was previously written");
                    PanelRepository.updateEPC(barcode, epc, "NOT_SUCCESS");
                    return;
                }

                // ── FETCH PANEL DATA FROM DB ───────────────────────────────
                PanelData data = PanelRepository.fetchPanel(barcode);
                if (data == null || data.getId() == null) {
                    ui.showError("DATA NOT FOUND – Barcode not in database: " + barcode);
                    return;
                }

                // ── STATIC DATA ────────────────────────────────────────────
                StaticPanelData staticData = StaticDataRepository.load();

                // ── ALWAYS SHOW DATA ON UI (before write attempt) ──────────
                ui.showMappedData(data, epc);
                ui.showStaticData(staticData);

                // ── TAG WRITE ──────────────────────────────────────────────
                boolean writeOk = RFIDService.writePanelDataToTag(data);

                if (!writeOk) {
                    // Write failed — data already shown, just mark red
                    PanelRepository.updateEPC(barcode, epc, "NOT_SUCCESS");
                    ui.showError("TAG WRITE FAILED – Data shown but not written to tag");
                    return;
                }

                // ── SUCCESS ────────────────────────────────────────────────
                PanelRepository.updateEPC(barcode, epc, "SUCCESS");
                ui.showSuccess("TAG MAPPED & DATA WRITTEN SUCCESSFULLY ✓  |  Panel: " + barcode);

            } catch (Exception e) {
                e.printStackTrace();
                ui.showError("ERROR – " + e.getMessage());
            } finally {
                try { Thread.sleep(3000); } catch (Exception ignored) {}  // show result for 3 sec
                busy = false;
                ui.resetForNext();
            }
        }).start();
    }
}