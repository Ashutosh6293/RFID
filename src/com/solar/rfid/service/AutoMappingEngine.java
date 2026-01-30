

package com.solar.rfid.service;

import com.solar.rfid.barcode.BarcodeListener;
import com.solar.rfid.rfid.RFIDAutoScanner;
import com.solar.rfid.rfid.RFIDService;
import com.solar.rfid.db.PanelRepository;
import com.solar.rfid.model.PanelData;
import com.solar.rfid.ui.LiveMappingFrame;

public class AutoMappingEngine {

    private final LiveMappingFrame ui;
    private volatile boolean busy = false;

    public AutoMappingEngine(LiveMappingFrame ui) {
        this.ui = ui;
        ui.attachBarcodeListener(new BarcodeListener(this::onBarcode));
    }

    public void start() {
        System.out.println("AUTO MAPPING ENGINE STARTED");
        ui.showMessage("READY");
    }

    private void onBarcode(String barcode) {

        if (busy) return;
        busy = true;

        System.out.println("PROCESSING BARCODE: " + barcode);
        ui.clear();
        ui.showMessage("BARCODE : " + barcode);

        new Thread(() -> {
            try {
                String epc = RFIDAutoScanner.readEPC();
                if (epc == null) {
                    ui.showMessage("RFID NOT DETECTED");
                    return;
                }

                PanelRepository.updateEPC(barcode, epc);
                PanelData data = PanelRepository.fetchPanel(barcode);

                if (data == null || data.getId() == null) {
                    ui.showMessage("DATA NOT FOUND");
                    return;
                }

                boolean writeOk = RFIDService.writePanelDataToTag(data);
                if (!writeOk) {
                    ui.showMessage("TAG WRITE FAILED");
                    return;
                }

                ui.showMappedData(data, epc);
                ui.showMessage("TAG MAPPED & DATA WRITTEN");

            } finally {
                busy = false;
            }
        }).start();
    }
}
