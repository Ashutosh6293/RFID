package com.solar.rfid.rfid;

import com.uhf.UHFMainForm;
import com.rscja.deviceapi.RFIDWithUHFSerialPortUR4;

public class RFIDInitializer {

    public static void init() {
        try {
            UHFMainForm.ur4 = new RFIDWithUHFSerialPortUR4();
            System.out.println("RFID initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
