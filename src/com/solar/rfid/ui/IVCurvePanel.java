package com.solar.rfid.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IVCurvePanel extends JPanel {

    private final List<Double> V = new ArrayList<>();
    private final List<Double> I = new ArrayList<>();
    private final List<Double> P = new ArrayList<>();

    private static final int L = 70, R = 70, T = 30, B = 50;

    public void setCurve(double voc, double isc, double pmax) {
        V.clear(); I.clear(); P.clear();

        if (voc <= 0 || isc <= 0 || pmax <= 0) {
            repaint();
            return;
        }

        double vmp = voc * 0.8;
        double imp = pmax / vmp;

        int steps = 60;
        for (int k = 0; k <= steps; k++) {
            double v = voc * k / steps;
            double i;

            if (v <= vmp) {
                i = isc - (isc - imp) * (v / vmp);
            } else {
                i = imp * (1 - (v - vmp) / (voc - vmp));
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

        int w = getWidth(), h = getHeight();
        int pw = w - L - R;
        int ph = h - T - B;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        g2.setColor(Color.BLACK);
        g2.drawRect(L, T, pw, ph);

        if (V.isEmpty()) return;

        double maxV = 70;   // fixed like datasheet
        double maxI = 20;
        double maxP = 700;

        // ===== GRID + TICKS =====
        g2.setFont(new Font("Arial", Font.PLAIN, 11));

        // Voltage X-axis (0–70 step 5)
        for (int v = 0; v <= 70; v += 5) {
            int x = L + (int) (v / maxV * pw);
            g2.setColor(new Color(200,200,200));
            g2.drawLine(x, T, x, T + ph);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(v), x - 8, h - 30);
        }

        // Current Y-axis (0–20 step 5)
        for (int i = 0; i <= 20; i += 5) {
            int y = T + (int) ((1 - i / maxI) * ph);
            g2.setColor(new Color(200,200,200));
            g2.drawLine(L, y, L + pw, y);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(i), 30, y + 4);
        }

        // Power Y-axis (0–700 step 100)
        for (int p = 0; p <= 700; p += 100) {
            int y = T + (int) ((1 - p / maxP) * ph);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(p), w - 50, y + 4);
        }

        // ===== CURVES =====
        int px = -1, pyI = -1, pyP = -1;
        double pMax = 0;
        int pmaxX = 0, pmaxY = 0;

        for (int k = 0; k < V.size(); k++) {
            int x  = L + (int) (V.get(k) / maxV * pw);
            int yI = T + (int) ((1 - I.get(k) / maxI) * ph);
            int yP = T + (int) ((1 - P.get(k) / maxP) * ph);

            g2.setColor(Color.RED);
            if (px != -1) g2.drawLine(px, pyI, x, yI);

            g2.setColor(Color.BLUE);
            if (px != -1) g2.drawLine(px, pyP, x, yP);

            if (P.get(k) > pMax) {
                pMax = P.get(k);
                pmaxX = x;
                pmaxY = yP;
            }

            px = x; pyI = yI; pyP = yP;
        }

        // Pmax point
        g2.setColor(Color.GREEN.darker());
        g2.fillOval(pmaxX - 4, pmaxY - 4, 8, 8);

        // Labels
        g2.setColor(Color.BLACK);
        g2.drawString("Voltage (V)", L + pw / 2 - 30, h - 10);
        g2.drawString("Current (A)", 5, T + ph / 2);
        g2.drawString("Power (W)", w - 60, T + ph / 2);

        // Legend
        g2.setColor(Color.RED);
        g2.drawString("Current", L + 10, T + 15);
        g2.setColor(Color.BLUE);
        g2.drawString("Power", L + 90, T + 15);
    }
}
