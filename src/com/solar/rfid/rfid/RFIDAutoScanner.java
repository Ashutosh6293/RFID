package com.solar.rfid.rfid;

public class RFIDAutoScanner {

    // timeout in ms
    private static final int TIMEOUT = 1500;

    // Auto EPC reader (used by AutoMappingEngine)
    public static String readEPC() {
        return RFIDService.readSingleEPC(TIMEOUT);
    }
}
