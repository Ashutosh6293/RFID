

package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IVCurvePanel extends JPanel {

    private final List<Double> V = new ArrayList<>();
    private final List<Double> I = new ArrayList<>();
    private final List<Double> P = new ArrayList<>();

    private double vocVal, iscVal, pmaxVal;

    private static final int L = 90, R = 90, T = 50, B = 70;

    public void setCurve(double voc, double isc, double pmax) {

        V.clear();
        I.clear();
        P.clear();

        this.vocVal = voc;
        this.iscVal = isc;
        this.pmaxVal = pmax;

        if (voc <= 0 || isc <= 0 || pmax <= 0) {
            repaint();
            return;
        }

        int steps = 300;

        double vmp = voc * 0.80;
        double imp = pmax / vmp;

        for (int k = 0; k <= steps; k++) {

            double v = voc * k / steps;
            double i;

            if (v <= vmp) {
                double ratio = v / vmp;
                i = isc - (isc - imp) * Math.pow(ratio, 1.4);
            } else {
                double ratio = (v - vmp) / (voc - vmp);
                i = imp * (1 - Math.pow(ratio, 1.8));
            }

            if (i < 0) i = 0;

            V.add(v);
            I.add(i);
            P.add(v * i);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int pw = w - L - R;
        int ph = h - T - B;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        g2.setColor(Color.BLACK);
        g2.drawRect(L, T, pw, ph);

        if (V.isEmpty()) return;

        // Dynamic max range
        double maxV = roundUp(vocVal * 1.1, 5);
        double maxI = roundUp(iscVal * 1.2, 5);
        double maxP = roundUp(pmaxVal * 1.2, 100);

        g2.setFont(new Font("Arial", Font.PLAIN, 11));

        // ===== DOTTED GRID =====
        Stroke dotted = new BasicStroke(
                1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0, new float[]{3}, 0);

        g2.setStroke(dotted);
        g2.setColor(new Color(200, 200, 200));

        // Voltage grid + labels
        for (int v = 0; v <= maxV; v += 5) {
            int x = L + (int) (v / maxV * pw);
            g2.drawLine(x, T, x, T + ph);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(v), x - 8, h - 30);
            g2.setColor(new Color(200, 200, 200));
        }

        // Current grid + labels
        for (int i = 0; i <= maxI; i += 5) {
            int y = T + (int) ((1 - i / maxI) * ph);
            g2.drawLine(L, y, L + pw, y);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(i), 50, y + 4);
            g2.setColor(new Color(200, 200, 200));
        }

        // Power labels (right side)
        for (int p = 0; p <= maxP; p += 100) {
            int y = T + (int) ((1 - p / maxP) * ph);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(p), w - 60, y + 4);
        }

        g2.setStroke(new BasicStroke(2));

        int prevX = -1, prevYI = -1, prevYP = -1;

        double maxPower = 0;
        int pmaxX = 0, pmaxY = 0;

        for (int k = 0; k < V.size(); k++) {

            int x = L + (int) (V.get(k) / maxV * pw);
            int yI = T + (int) ((1 - I.get(k) / maxI) * ph);
            int yP = T + (int) ((1 - P.get(k) / maxP) * ph);

            g2.setColor(Color.RED);
            if (prevX != -1) g2.drawLine(prevX, prevYI, x, yI);

            g2.setColor(Color.BLUE);
            if (prevX != -1) g2.drawLine(prevX, prevYP, x, yP);

            if (P.get(k) > maxPower) {
                maxPower = P.get(k);
                pmaxX = x;
                pmaxY = yP;
            }

            prevX = x;
            prevYI = yI;
            prevYP = yP;
        }

        // MPP marker
        g2.setColor(Color.GREEN.darker());
        g2.fillOval(pmaxX - 6, pmaxY - 6, 12, 12);

        // Axis Labels
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString("Voltage (V)", L + pw / 2 - 40, h - 15);

        Graphics2D gLeft = (Graphics2D) g2.create();
        gLeft.rotate(-Math.PI / 2);
        gLeft.drawString("Current (A)", -T - ph / 2 - 40, 25);
        gLeft.dispose();

        Graphics2D gRight = (Graphics2D) g2.create();
        gRight.rotate(Math.PI / 2);
        gRight.drawString("Power (W)", T + ph / 2 - 40, -w + 30);
        gRight.dispose();

        // Legend
        g2.setColor(Color.RED);
        g2.drawString("Current", L + 10, T - 20);
        g2.setColor(Color.BLUE);
        g2.drawString("Power", L + 100, T - 20);
    }

    // Utility for clean rounded axis
    private double roundUp(double value, int multiple) {
        return Math.ceil(value / multiple) * multiple;
    }
}




// package com.solar.rfid.ui;

// import javax.swing.*;
// import java.awt.*;
// import java.util.ArrayList;
// import java.util.List;

// public class IVCurvePanel extends JPanel {

//     private final List<Double> V = new ArrayList<>();
//     private final List<Double> I = new ArrayList<>();
//     private final List<Double> P = new ArrayList<>();

//     private double vocVal, iscVal, pmaxVal, vmppVal, imppVal;

//     // ✅ Updated: vmpp aur impp bhi pass karo for realistic curve
//     public void setCurve(double voc, double isc, double pmax, double vmpp, double impp) {

//         V.clear(); I.clear(); P.clear();

//         this.vocVal  = voc;
//         this.iscVal  = isc;
//         this.pmaxVal = pmax;
//         this.vmppVal = vmpp;
//         this.imppVal = impp;

//         if (voc <= 0 || isc <= 0 || pmax <= 0) { repaint(); return; }

//         int steps = 300;
//         for (int k = 0; k <= steps; k++) {
//             double v = voc * k / steps;
//             double iVal;
//             if (v <= vmpp) {
//                 iVal = isc - (isc - impp) * Math.pow(v / vmpp, 2);
//             } else {
//                 double ratio = (v - vmpp) / (voc - vmpp);
//                 iVal = impp * (1 - Math.pow(ratio, 1.8));
//             }
//             if (iVal < 0) iVal = 0;
//             V.add(v);
//             I.add(iVal);
//             P.add(v * iVal);
//         }
//         repaint();
//     }

//     // ── Backward compatible overload (voc*0.78 fallback) ─────────────────────
//     public void setCurve(double voc, double isc, double pmax) {
//         double vmpp = voc * 0.78;
//         double impp = (vmpp > 0) ? pmax / vmpp : 0;
//         setCurve(voc, isc, pmax, vmpp, impp);
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         Graphics2D g2 = (Graphics2D) g;
//         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//         g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

//         int W = getWidth();
//         int H = getHeight();

//         // ── Background ────────────────────────────────────────────────────────
//         g2.setColor(Color.WHITE);
//         g2.fillRect(0, 0, W, H);

//         if (V.isEmpty()) {
//             g2.setColor(Color.GRAY);
//             g2.setFont(new Font("Arial", Font.ITALIC, 12));
//             g2.drawString("Scan a panel to view IV curve", W / 2 - 100, H / 2);
//             return;
//         }

//         // ── Plot area ─────────────────────────────────────────────────────────
//         int padL = 70, padR = 70, padT = 35, padB = 50;
//         int plotX = padL;
//         int plotY = padT;
//         int plotW = W - padL - padR;
//         int plotH = H - padT - padB;

//         // ── Axis ranges (clean round numbers) ─────────────────────────────────
//         double vocMax = Math.ceil(vocVal / 10.0) * 10.0;   // e.g. 48.8 → 50
//         double iscMax = Math.ceil(iscVal / 5.0)  * 5.0;    // e.g. 16.4 → 20
//         double powMax = Math.ceil(pmaxVal / 100.0) * 100.0; // e.g. 632 → 700

//         int xStep  = 10;   // Voltage: 0, 10, 20 ...
//         int yStepI = 5;    // Current: 0, 5, 10 ...
//         int yStepP = 100;  // Power:   0, 100, 200 ...

//         // ── Plot background ───────────────────────────────────────────────────
//         g2.setColor(new Color(248, 250, 255));
//         g2.fillRect(plotX, plotY, plotW, plotH);

//         // ── Dotted grid ───────────────────────────────────────────────────────
//         Stroke dashed = new BasicStroke(0.7f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
//                 1f, new float[]{3f, 3f}, 0f);
//         g2.setStroke(dashed);
//         g2.setColor(new Color(200, 210, 230));

//         for (double v = 0; v <= vocMax + 0.1; v += xStep) {
//             int gx = plotX + (int) (v / vocMax * plotW);
//             g2.drawLine(gx, plotY, gx, plotY + plotH);
//         }
//         for (double ic = 0; ic <= iscMax + 0.1; ic += yStepI) {
//             int gy = plotY + (int) ((1 - ic / iscMax) * plotH);
//             g2.drawLine(plotX, gy, plotX + plotW, gy);
//         }
//         g2.setStroke(new BasicStroke(1f));

//         // ── Axes ──────────────────────────────────────────────────────────────
//         g2.setColor(Color.BLACK);
//         g2.setStroke(new BasicStroke(1.5f));
//         // X bottom
//         g2.drawLine(plotX, plotY + plotH, plotX + plotW, plotY + plotH);
//         // Left Y
//         g2.drawLine(plotX, plotY, plotX, plotY + plotH);
//         // Right Y
//         g2.drawLine(plotX + plotW, plotY, plotX + plotW, plotY + plotH);
//         g2.setStroke(new BasicStroke(1f));

//         // ── X ticks & labels: Voltage 0, 10, 20 ... ──────────────────────────
//         g2.setFont(new Font("Arial", Font.PLAIN, 11));
//         g2.setColor(Color.BLACK);
//         for (double v = 0; v <= vocMax + 0.1; v += xStep) {
//             int tx = plotX + (int) (v / vocMax * plotW);
//             g2.drawLine(tx, plotY + plotH, tx, plotY + plotH + 4);
//             String lbl = String.valueOf((int) v);
//             int lw = g2.getFontMetrics().stringWidth(lbl);
//             g2.drawString(lbl, tx - lw / 2, plotY + plotH + 16);
//         }

//         // ── Left Y ticks & labels: Current 0, 5, 10 ... RED ──────────────────
//         g2.setFont(new Font("Arial", Font.PLAIN, 11));
//         g2.setColor(new Color(200, 0, 0));
//         for (double ic = 0; ic <= iscMax + 0.1; ic += yStepI) {
//             int ty = plotY + (int) ((1 - ic / iscMax) * plotH);
//             g2.drawLine(plotX - 4, ty, plotX, ty);
//             String lbl = String.valueOf((int) ic);
//             int lw = g2.getFontMetrics().stringWidth(lbl);
//             g2.drawString(lbl, plotX - lw - 6, ty + 4);
//         }

//         // ── Right Y ticks & labels: Power 0, 100, 200 ... BLUE ───────────────
//         g2.setFont(new Font("Arial", Font.PLAIN, 11));
//         g2.setColor(new Color(0, 80, 200));
//         for (double pw = 0; pw <= powMax + 0.1; pw += yStepP) {
//             int ty = plotY + (int) ((1 - pw / powMax) * plotH);
//             g2.drawLine(plotX + plotW, ty, plotX + plotW + 4, ty);
//             String lbl = String.valueOf((int) pw);
//             g2.drawString(lbl, plotX + plotW + 7, ty + 4);
//         }

//         // ── Axis Titles ───────────────────────────────────────────────────────
//         g2.setFont(new Font("Arial", Font.BOLD, 12));

//         // X: Voltage (V)
//         g2.setColor(Color.BLACK);
//         String xTitle = "Voltage (V)";
//         g2.drawString(xTitle, plotX + (plotW - g2.getFontMetrics().stringWidth(xTitle)) / 2,
//                 plotY + plotH + 35);

//         // Left: Current (A) rotated — RED
//         Graphics2D gL = (Graphics2D) g2.create();
//         gL.setFont(new Font("Arial", Font.BOLD, 12));
//         gL.setColor(new Color(200, 0, 0));
//         gL.translate(18, plotY + plotH / 2);
//         gL.rotate(-Math.PI / 2);
//         String yL = "Current (A)";
//         gL.drawString(yL, -gL.getFontMetrics().stringWidth(yL) / 2, 0);
//         gL.dispose();

//         // Right: Power (W) rotated — BLUE
//         Graphics2D gR = (Graphics2D) g2.create();
//         gR.setFont(new Font("Arial", Font.BOLD, 12));
//         gR.setColor(new Color(0, 80, 200));
//         gR.translate(W - 18, plotY + plotH / 2);
//         gR.rotate(Math.PI / 2);
//         String yR = "Power (W)";
//         gR.drawString(yR, -gR.getFontMetrics().stringWidth(yR) / 2, 0);
//         gR.dispose();

//         // ── IV Curve (RED) ────────────────────────────────────────────────────
//         g2.setColor(new Color(220, 30, 30));
//         g2.setStroke(new BasicStroke(2.2f));
//         for (int k = 0; k < V.size() - 1; k++) {
//             int x1 = plotX + (int) (V.get(k)     / vocMax * plotW);
//             int y1 = plotY + (int) ((1 - I.get(k)     / iscMax) * plotH);
//             int x2 = plotX + (int) (V.get(k + 1) / vocMax * plotW);
//             int y2 = plotY + (int) ((1 - I.get(k + 1) / iscMax) * plotH);
//             g2.drawLine(x1, y1, x2, y2);
//         }

//         // ── Power Curve (BLUE) ────────────────────────────────────────────────
//         g2.setColor(new Color(0, 100, 220));
//         g2.setStroke(new BasicStroke(2.2f));
//         for (int k = 0; k < V.size() - 1; k++) {
//             int x1 = plotX + (int) (V.get(k)     / vocMax * plotW);
//             int y1 = plotY + (int) ((1 - P.get(k)     / powMax) * plotH);
//             int x2 = plotX + (int) (V.get(k + 1) / vocMax * plotW);
//             int y2 = plotY + (int) ((1 - P.get(k + 1) / powMax) * plotH);
//             g2.drawLine(x1, y1, x2, y2);
//         }
//         g2.setStroke(new BasicStroke(1f));

//         // ── MPP dot (GREEN) ───────────────────────────────────────────────────
//         double maxP = 0; int mppK = 0;
//         for (int k = 0; k < P.size(); k++) {
//             if (P.get(k) > maxP) { maxP = P.get(k); mppK = k; }
//         }
//         int mppPx = plotX + (int) (V.get(mppK) / vocMax * plotW);
//         int mppPy = plotY + (int) ((1 - I.get(mppK) / iscMax) * plotH);

//         g2.setColor(new Color(0, 170, 0));
//         g2.fillOval(mppPx - 6, mppPy - 6, 12, 12);
//         g2.setColor(Color.WHITE);
//         g2.setStroke(new BasicStroke(1.2f));
//         g2.drawOval(mppPx - 6, mppPy - 6, 12, 12);
//         g2.setStroke(new BasicStroke(1f));

//         // MPP label
//         g2.setFont(new Font("Arial", Font.BOLD, 10));
//         g2.setColor(new Color(0, 120, 0));
//         String mppLbl = String.format("MPP: %.1fV, %.2fA, %.1fW",
//                 V.get(mppK), I.get(mppK), P.get(mppK));
//         int mlx = mppPx + 10;
//         if (mlx + g2.getFontMetrics().stringWidth(mppLbl) > plotX + plotW) {
//             mlx = mppPx - g2.getFontMetrics().stringWidth(mppLbl) - 10;
//         }
//         g2.drawString(mppLbl, mlx, mppPy - 8);

//         // ── Legend ────────────────────────────────────────────────────────────
//         int legX = plotX + 10, legY = plotY + 10;
//         g2.setColor(new Color(255, 255, 255, 210));
//         g2.fillRoundRect(legX, legY, 130, 42, 8, 8);
//         g2.setColor(new Color(180, 190, 210));
//         g2.drawRoundRect(legX, legY, 130, 42, 8, 8);

//         g2.setColor(new Color(220, 30, 30));
//         g2.setStroke(new BasicStroke(2.2f));
//         g2.drawLine(legX + 6, legY + 13, legX + 24, legY + 13);
//         g2.setFont(new Font("Arial", Font.PLAIN, 10));
//         g2.setColor(Color.BLACK);
//         g2.drawString("Current (I-V)", legX + 28, legY + 17);

//         g2.setColor(new Color(0, 100, 220));
//         g2.setStroke(new BasicStroke(2.2f));
//         g2.drawLine(legX + 6, legY + 29, legX + 24, legY + 29);
//         g2.setStroke(new BasicStroke(1f));
//         g2.setColor(Color.BLACK);
//         g2.drawString("Power (P-V)", legX + 28, legY + 33);

//         // ── Border ────────────────────────────────────────────────────────────
//         g2.setColor(new Color(160, 175, 200));
//         g2.drawRect(plotX, plotY, plotW, plotH);
//     }
// }