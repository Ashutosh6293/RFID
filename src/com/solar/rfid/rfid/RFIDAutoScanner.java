package com.solar.rfid.rfid;

public class RFIDAutoScanner {

    public static String readEPC() {
        return RFIDService.readSingleEPC(2000);
    }
}
