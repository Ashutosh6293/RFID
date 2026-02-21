
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
                boolean writeOk = RFIDService.writePanelDataToTag(data, epc);

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