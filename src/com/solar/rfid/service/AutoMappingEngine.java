

package com.solar.rfid.service;

import com.solar.rfid.barcode.BarcodeListener;
import com.solar.rfid.db.PanelRepository;
import com.solar.rfid.db.StaticDataRepository;
import com.solar.rfid.model.PanelData;
import com.solar.rfid.model.StaticPanelData;
import com.solar.rfid.rfid.RFIDAutoScanner;
import com.solar.rfid.rfid.RFIDService;
import com.solar.rfid.ui.LiveMappingFrame;

public class AutoMappingEngine {

    private static AutoMappingEngine instance;   // 🔥 for manual trigger

    private final LiveMappingFrame ui;
    private volatile boolean busy = false;

    public AutoMappingEngine(LiveMappingFrame ui) {
        this.ui = ui;
        instance = this; // 🔥 register instance
        ui.attachBarcodeListener(new BarcodeListener(this::onBarcode));
    }

    public void start() {
        ui.showMessage("READY");
    }

    // 🔥 MANUAL TRIGGER (MappingFrame se call hoga)
    public static void manualTrigger(String barcode) {
        if (instance != null) {
            instance.onBarcode(barcode);
        }
    }

    // 🔥 COMMON LOGIC (auto + manual dono)
    private void onBarcode(String barcode) {

        if (busy || barcode == null || barcode.isEmpty()) {
            System.out.println("Ignoring barcode: " + barcode);
            return;
        }
        busy = true;

        new Thread(() -> {
            try {
                System.out.println("PROCESSING BARCODE: " + barcode);
                ui.showMessage("BARCODE : " + barcode);

                // ================= RFID READ =================
                String epc = RFIDAutoScanner.readEPC();
                if (epc == null) {
                    ui.showMessage("RFID NOT DETECTED");
                    return;
                }

                System.out.println("EPC READ = " + epc);

                // ================= DB UPDATE =================
                PanelRepository.updateEPC(barcode, epc);

                PanelData data = PanelRepository.fetchPanel(barcode);
                if (data == null || data.getId() == null) {
                    ui.showMessage("DATA NOT FOUND");
                    return;
                }

                // ================= STATIC DATA =================
                StaticPanelData staticData = StaticDataRepository.load();

                // ================= TAG WRITE =================
                boolean ok = RFIDService.writePanelDataToTag(data);
                if (!ok) {
                    ui.showMessage("TAG WRITE FAILED");
                    return;
                }

                // ================= UI UPDATE =================
                ui.showMappedData(data, epc);
                ui.showStaticData(staticData);
                ui.showMessage("TAG MAPPED & DATA WRITTEN");

            } catch (Exception e) {
                e.printStackTrace();
                ui.showMessage("ERROR");
            } finally {
                try { Thread.sleep(400); } catch (Exception ignored) {}
                busy = false;
                ui.resetForNext(); // 🔁 next panel ready
            }
        }).start();
    }
}
