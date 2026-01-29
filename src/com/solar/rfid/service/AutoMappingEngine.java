package com.solar.rfid.service;

import com.solar.rfid.barcode.BarcodeListener;
import com.solar.rfid.rfid.RFIDAutoScanner;
import com.solar.rfid.db.PanelRepository;
import com.solar.rfid.model.PanelData;
import com.solar.rfid.ui.LiveMappingFrame;

public class AutoMappingEngine {

    private LiveMappingFrame ui;
    private String currentBarcode;

    public AutoMappingEngine(LiveMappingFrame ui) {
        this.ui = ui;
        ui.attachBarcodeListener(new BarcodeListener(this::onBarcode));
    }

    public void start() {
        System.out.println("AUTO MAPPING ENGINE STARTED");
    }

    private void onBarcode(String barcode) {

        ui.clear();
        ui.showMessage("Barcode detected");

        currentBarcode = barcode;

        new Thread(() -> {
            String epc = RFIDAutoScanner.readEPC();
            if (epc == null) {
                ui.showMessage("RFID not detected");
                return;
            }

            PanelRepository.updateEPC(barcode, epc);

            PanelData data = PanelRepository.fetchPanel(barcode);
            ui.showMappedData(data, epc);

            ui.showMessage("TAG MAPPED SUCCESSFULLY");
        }).start();
    }
}
