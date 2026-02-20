
// package com.solar.rfid.barcode;

// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;

// public class BarcodeListener extends KeyAdapter {

//     private StringBuilder buffer = new StringBuilder();
//     private BarcodeCallback callback;

//     public BarcodeListener(BarcodeCallback cb) {
//         this.callback = cb;
//     }

//     @Override
//     public void keyTyped(KeyEvent e) {
//         if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
//             callback.onBarcode(buffer.toString().trim());
//             buffer.setLength(0);
//         } else {
//             buffer.append(e.getKeyChar());
//         }
//     }

//     public interface BarcodeCallback {
//         void onBarcode(String barcode);
//     }
// }





package com.solar.rfid.barcode;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BarcodeListener extends KeyAdapter {

    private StringBuilder buffer = new StringBuilder();
    private BarcodeCallback callback;

    public BarcodeListener(BarcodeCallback cb) {
        this.callback = cb;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // ENTER = Barcode complete
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            String barcode = buffer.toString().trim();

            if (!barcode.isEmpty()) {
                callback.onBarcode(barcode);
            }

            buffer.setLength(0); // reset buffer
        }
        else {
            char ch = e.getKeyChar();

            if (!Character.isISOControl(ch)) {
                buffer.append(ch);
            }
        }
    }

    public interface BarcodeCallback {
        void onBarcode(String barcode);
    }
}
