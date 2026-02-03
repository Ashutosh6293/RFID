package com.solar.rfid.rfid;

import com.rscja.deviceapi.RFIDWithUHFSerialPortUR4;
import com.rscja.deviceapi.interfaces.IUR4;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.IUHFInventoryCallback;

import com.solar.rfid.model.PanelData;
import com.solar.rfid.model.StaticPanelData;

public class RFIDService {

    private static IUR4 reader;
    private static volatile boolean initialized = false;
    private static volatile String lastEPC = null;

    // ================= INIT =================
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

        if (!initialized || reader == null)
            return null;

        lastEPC = null;

        reader.setInventoryCallback(info -> {
            if (lastEPC == null) {
                lastEPC = info.getEPC();
                System.out.println("EPC READ = " + lastEPC);
            }
        });

        if (!reader.startInventoryTag())
            return null;

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (lastEPC != null)
                break;
            try {
                Thread.sleep(30);
            } catch (Exception ignored) {
            }
        }

        reader.stopInventory();
        return lastEPC;
    }

    // ================= WRITE SAFE DATA TO TAG =================
    public static boolean writePanelDataToTag(PanelData d) {

        if (!initialized || reader == null) {
            System.out.println("Reader not initialized");
            return false;
        }

        try {
            // 🔹 EXACTLY SAME 5 DATA (LIKE BEFORE)
            String text = "ID:" + d.getId() + "|" +
                    "PM:" + d.getPmax() + "|" +
                    "VO:" + d.getVoc() + "|" +
                    "IS:" + d.getIsc() + "|" +
                    "BN:" + d.getBin();

            String hexPayload = asciiToHex(text);

            // 🔒 USER memory (VERY SAFE SETTINGS)
            int bank = 3; // USER
            int startAddr = 0; // START FROM 0 (important)
            int maxWords = 16; // 🔥 LOWER LIMIT (most tags support)

            int words = hexPayload.length() / 4;
            if (words > maxWords) {
                hexPayload = hexPayload.substring(0, maxWords * 4);
                words = maxWords;
            }

            System.out.println("WRITE WORDS = " + words);
            System.out.println("HEX PAYLOAD = " + hexPayload);

            boolean ok = reader.writeData(
                    "00000000", // default access password
                    bank,
                    startAddr,
                    words,
                    hexPayload);

            System.out.println("TAG WRITE OK = " + ok);
            System.out.println("WRITTEN TEXT = " + text);

            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= ASCII → HEX =================
    private static String asciiToHex(String txt) {
        StringBuilder sb = new StringBuilder();
        for (char c : txt.toCharArray()) {
            sb.append(String.format("%02X", (int) c));
        }
        return sb.toString();
    }
}
