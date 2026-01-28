
package com.solar.rfid.rfid;

import com.rscja.deviceapi.RFIDWithUHFSerialPortUR4;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.IUHFInventoryCallback;
import com.rscja.deviceapi.interfaces.IUR4;

public class RFIDService {

    private static IUR4 reader;
    private static volatile String lastEPC = null;

    // 🔹 INIT reader (NO DEMO UI)
    public static boolean initReader(String comPort) {
        try {
            reader = new RFIDWithUHFSerialPortUR4();
            boolean ok = reader.init(comPort);
            System.out.println("UHF init on " + comPort + " = " + ok);
            return ok;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Read single EPC
    public static String readSingleEPC(int timeoutMs) {

        if (reader == null) {
            System.out.println("Reader not initialized");
            return null;
        }

        lastEPC = null;

        reader.setInventoryCallback(new IUHFInventoryCallback() {
            @Override
            public void callback(UHFTAGInfo info) {
                if (lastEPC == null) {
                    lastEPC = info.getEPC();
                    System.out.println("EPC detected: " + lastEPC);
                }
            }
        });

        boolean started = reader.startInventoryTag();
        if (!started) {
            System.out.println("Failed to start inventory");
            return null;
        }

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (lastEPC != null) break;
            try { Thread.sleep(50); } catch (Exception ignored) {}
        }

        reader.stopInventory();
        return lastEPC;
    }
}
