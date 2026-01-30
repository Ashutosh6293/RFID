

package com.solar.rfid.rfid;

import com.rscja.deviceapi.RFIDWithUHFSerialPortUR4;
import com.rscja.deviceapi.interfaces.IUR4;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.IUHFInventoryCallback;
import com.solar.rfid.model.PanelData;

public class RFIDService {

    private static IUR4 reader;
    private static volatile boolean initialized = false;
    private static volatile String lastEPC = null;

    // ================= INIT (ONLY ONCE) =================
    public static synchronized boolean initReader(String comPort) {

        if (initialized) {
            System.out.println("RFID already initialized");
            return true;
        }

        try {
            reader = new RFIDWithUHFSerialPortUR4();
            boolean ok = reader.init(comPort);

            if (ok) {
                initialized = true;
                System.out.println("UHF init on " + comPort + " = true");
            }
            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= EPC READ =================
    public static String readSingleEPC(int timeoutMs) {

        if (!initialized || reader == null) {
            System.out.println("Reader not initialized");
            return null;
        }

        lastEPC = null;

        reader.setInventoryCallback(new IUHFInventoryCallback() {
            @Override
            public void callback(UHFTAGInfo info) {
                if (lastEPC == null) {
                    lastEPC = info.getEPC();
                    System.out.println("EPC READ = " + lastEPC);
                }
            }
        });

        if (!reader.startInventoryTag()) {
            System.out.println("Failed to start inventory");
            return null;
        }

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (lastEPC != null) break;
            try { Thread.sleep(30); } catch (Exception ignored) {}
        }

        reader.stopInventory();
        return lastEPC;
    }

    // ================= WRITE 5 DATA TO TAG (ASCII → HEX) =================
    public static boolean writePanelDataToTag(PanelData d) {

        if (!initialized || reader == null) {
            System.out.println("Reader not initialized");
            return false;
        }

        try {
            // 🔹 Human-readable payload
            String text =
                "ID:" + d.getId() + "|" +
                "PM:" + d.getPmax() + "|" +
                "VO:" + d.getVoc() + "|" +
                "IS:" + d.getIsc() + "|" +
                "BN:" + d.getBin();

            // 🔹 Convert ASCII → HEX
            String hexPayload = asciiToHex(text);

            System.out.println("HEX PAYLOAD = " + hexPayload);

            // USER memory
            int bank = 3;        // USER
            int startAddr = 0;
            int wordLen = hexPayload.length() / 4; // 1 word = 4 hex chars

            boolean ok = reader.writeData(
                "00000000",   // access password
                bank,
                startAddr,
                wordLen,
                hexPayload
            );

            System.out.println("TAG WRITE OK = " + ok);
            System.out.println("WRITTEN TEXT = " + text);

            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= ASCII → HEX (single helper) =================
    private static String asciiToHex(String txt) {
        StringBuilder sb = new StringBuilder();
        for (char c : txt.toCharArray()) {
            sb.append(String.format("%02X", (int) c));
        }
        return sb.toString();
    }
}
