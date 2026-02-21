
package com.solar.rfid.rfid;

import com.rscja.deviceapi.RFIDWithUHFSerialPortUR4;
import com.rscja.deviceapi.interfaces.IUR4;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.IUHFInventoryCallback;
import com.rscja.deviceapi.entity.AntennaNameEnum;
import com.solar.rfid.model.PanelData;

public class RFIDService {

    private static IUR4 reader;
    private static volatile boolean initialized = false;
    private static volatile String lastEPC = null;

    private static final int MAX_RETRY = 6;

    // ================= INIT =================
    public static synchronized boolean initReader(String comPort) {

        if (initialized)
            return true;

        try {
            reader = new RFIDWithUHFSerialPortUR4();
            boolean ok = reader.init(comPort);

            if (ok) {

                // 🔥 Set antenna power (UR4 compatible)
                reader.setPower(AntennaNameEnum.ANT1, 30);

                initialized = true;
                System.out.println("RFID INIT SUCCESS on " + comPort);

            } else {
                System.out.println("RFID INIT FAILED");
            }

            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= READ SINGLE EPC =================
    public static synchronized String readSingleEPC(int timeoutMs) {

        if (!initialized || reader == null)
            return null;

        lastEPC = null;

        reader.setInventoryCallback(new IUHFInventoryCallback() {
            @Override
            public void callback(UHFTAGInfo info) {

                if (lastEPC != null)
                    return;

                lastEPC = info.getEPC();
                reader.stopInventory();
            }
        });

        if (!reader.startInventoryTag())
            return null;

        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {

            if (lastEPC != null)
                break;

            try {
                Thread.sleep(20);
            } catch (Exception ignored) {
            }
        }

        reader.stopInventory();
        String tid = null;

        if (lastEPC != null) {
            System.out.println("\n====== TAG DETECTED ======");
            System.out.println("EPC : " + lastEPC);
            // 🔥 READ TID IMMEDIATELY
            tid = readTIDDirect();
            System.out.println("TID : " + tid);
            System.out.println("==========================");

        }
        return tid;
    }

    // ================= READ TID =================
    public static synchronized String readTIDDirect() {

        if (!initialized || reader == null)
            return null;

        try {

            reader.stopInventory();
            Thread.sleep(80);

            String tid = reader.readData(
                    "00000000", // access password
                    2, // TID bank
                    0,
                    6 // 6 words
            );

            System.out.println("TID : " + tid);
            return tid;

        } catch (Exception e) {
            return null;
        }
    }

    // ================= WRITE PANEL DATA =================
    public static synchronized boolean writePanelDataToTag(PanelData d) {

        if (!initialized || reader == null)
            return false;

        try {

            reader.stopInventory();
            Thread.sleep(100);

            String text = "ID:" + d.getId() +
                    "|PM:" + d.getPmax() +
                    "|VO:" + d.getVoc() +
                    "|IS:" + d.getIsc();

            String hex = asciiToHex(text);

            int bank = 3; // USER memory
            int start = 0;
            int words = hex.length() / 4;

            if (words > 32) {
                hex = hex.substring(0, 32 * 4);
                words = 32;
            }

            System.out.println("WRITING HEX: " + hex);

            for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {

                System.out.println("WRITE ATTEMPT: " + attempt);

                boolean ok = reader.writeData(
                        "00000000",
                        bank,
                        start,
                        words,
                        hex);

                if (ok) {

                    Thread.sleep(100);

                    // 🔥 VERIFY WRITE
                    String verify = reader.readData(
                            "00000000",
                            bank,
                            start,
                            words);

                    if (verify != null &&
                            verify.equalsIgnoreCase(hex)) {

                        System.out.println("WRITE VERIFIED SUCCESS");
                        return true;
                    }
                }

                Thread.sleep(150);
            }

            System.out.println("WRITE FAILED AFTER RETRIES");
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= WRITE PANEL DATA =================
    // public static synchronized boolean writePanelDataToTag(PanelData d, String
    // tid) {

    // if (!initialized || reader == null || tid == null)
    // return false;

    // try {

    // reader.stopInventory();
    // Thread.sleep(150);

    // // 🔥 SET FILTER BY TID
    // int bank = 2;
    // int ptr = 0;
    // int len = tid.length() * 4;

    // boolean filterSet = reader.setFilter(bank, ptr, len, tid);

    // if (!filterSet) {
    // System.out.println("FILTER FAILED");
    // return false;
    // }

    // Thread.sleep(100);

    // String text = "ID:" + d.getId() +
    // "|PM:" + d.getPmax() +
    // "|VO:" + d.getVoc() +
    // "|IS:" + d.getIsc();

    // String hex = asciiToHex(text);

    // while (hex.length() % 4 != 0) {
    // hex += "00";
    // }

    // int userBank = 3;
    // int start = 0;
    // int words = hex.length() / 4;

    // if (words > 32) {
    // hex = hex.substring(0, 32 * 4);
    // words = 32;
    // }

    // for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {

    // boolean ok = reader.writeData(
    // "00000000",
    // userBank,
    // start,
    // words,
    // hex);

    // if (ok) {

    // Thread.sleep(150);

    // String verify = reader.readData(
    // "00000000",
    // userBank,
    // start,
    // words);

    // if (verify != null && verify.equalsIgnoreCase(hex)) {

    // System.out.println("WRITE SUCCESS & VERIFIED");

    // // 🔥 CLEAR FILTER
    // reader.setFilter(0, 0, 0, null);
    // return true;
    // }
    // }

    // Thread.sleep(200);
    // }

    // reader.setFilter(0, 0, 0, null);
    // System.out.println("WRITE FAILED");
    // return false;

    // } catch (Exception e) {
    // e.printStackTrace();
    // return false;
    // }
    // }

    // ================= ASCII TO HEX =================
    private static String asciiToHex(String txt) {

        StringBuilder sb = new StringBuilder();

        for (char c : txt.toCharArray()) {
            sb.append(String.format("%02X", (int) c));
        }

        return sb.toString();
    }

    // ================= CLOSE =================
    public static synchronized void closeReader() {

        if (reader != null) {
            try {
                reader.stopInventory();
                reader.free();
                initialized = false;
                System.out.println("RFID CLOSED");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
