package com.solar.rfid.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PrintablePanelReport
 * --------------------
 * Renders the main panel content (header + table + IV curve) as an A4 page.
 * Usage:
 *   PrintablePanelReport report = new PrintablePanelReport(data);
 *   report.print();   // opens print dialog
 */
public class PrintablePanelReport implements Printable {

    // ── Data holder ──────────────────────────────────────────────────────────
    public static class ReportData {
        public String serialNo      = "-";
        public String tid           = "-";
        public String manufacturer  = "";
        public String cellMfr       = "";
        public String moduleType    = "";
        public String moduleMfgDate = "";
        public String cellMfgDate   = "";
        public String moduleCountry = "";
        public String cellCountry   = "";
        public String pmax          = "";
        public String vmpp          = "";
        public String impp          = "";
        public String eff           = "";
        public String ff            = "";
        public String voc           = "";
        public String isc           = "";
        public String testLab       = "";
        public String iecDate       = "";

        // IV curve raw values
        public double vocVal  = 0;
        public double iscVal  = 0;
        public double pmaxVal = 0;
    }

    private final ReportData data;

    public PrintablePanelReport(ReportData data) {
        this.data = data;
    }

    // ── Print entry point ────────────────────────────────────────────────────
    public void print() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Panel Report – " + data.serialNo);

        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        // A4: 595.3 x 841.9 points (1pt = 1/72 inch)
        double w = 595.3, h = 841.9;
        double margin = 36; // 0.5 inch
        paper.setSize(w, h);
        paper.setImageableArea(margin, margin, w - 2 * margin, h - 2 * margin);
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.PORTRAIT);

        job.setPrintable(this, pf);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null,
                    "Print error: " + ex.getMessage(), "Print Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Printable.print ───────────────────────────────────────────────────────
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // translate to imageable area
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        float pageW = (float) pageFormat.getImageableWidth();
        float pageH = (float) pageFormat.getImageableHeight();

        float y = 0;

        // ── Company Header ────────────────────────────────────────────────
        y = drawHeader(g2, pageW, y);

        // ── Serial / TID row ──────────────────────────────────────────────
        y = drawSerialRow(g2, pageW, y);

        y += 10;

        // ── Section title ─────────────────────────────────────────────────
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.setColor(Color.BLACK);
        g2.drawString("Detailed Specification:", 0, y);
        y += 6;

        // ── Table ─────────────────────────────────────────────────────────
        y = drawTable(g2, pageW, y);

        y += 10;

        // ── IV Curve title ────────────────────────────────────────────────
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.setColor(Color.BLACK);
        g2.drawString("IV Characteristics of the Module:", 0, y);
        y += 8;

        // ── IV Curve ──────────────────────────────────────────────────────
        float curveH = Math.min(200f, pageH - y - 30);
        drawIVCurve(g2, 0, y, pageW, curveH);
        y += curveH + 10;

        // ── Print date footer ─────────────────────────────────────────────
        g2.setFont(new Font("Arial", Font.ITALIC, 7));
        g2.setColor(Color.GRAY);
        String ts = "Printed: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        g2.drawString(ts, 0, pageH - 2);

        return PAGE_EXISTS;
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private float drawHeader(Graphics2D g2, float pageW, float y) {
        // Red top bar
        g2.setColor(new Color(200, 16, 46));
        g2.fillRect(0, (int)y, (int)pageW, 3);
        y += 6;

        // Company name
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(200, 16, 46));
        String company = "GAUTAM SOLAR PVT. LTD.";
        int cw = g2.getFontMetrics().stringWidth(company);
        g2.drawString(company, (pageW - cw) / 2, y + 16);
        y += 22;

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.BLACK);
        String sub = "Gautam Solar Private Limited";
        g2.drawString(sub, (pageW - g2.getFontMetrics().stringWidth(sub)) / 2, y + 10);
        y += 14;

        g2.setFont(new Font("Arial", Font.PLAIN, 9));
        g2.setColor(Color.DARK_GRAY);
        String addr = "7 km Milestone, Tosham Road, Dist. Bhiwani, Bawani Khera HR 127032";
        g2.drawString(addr, (pageW - g2.getFontMetrics().stringWidth(addr)) / 2, y + 9);
        y += 12;

        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.setColor(new Color(200, 16, 46));
        String india = "INDIA";
        g2.drawString(india, (pageW - g2.getFontMetrics().stringWidth(india)) / 2, y + 9);
        y += 14;

        // Bottom border
        g2.setColor(new Color(200, 200, 200));
        g2.drawLine(0, (int)y, (int)pageW, (int)y);
        y += 6;

        return y;
    }

    // ── Serial / TID row ──────────────────────────────────────────────────────
    private float drawSerialRow(Graphics2D g2, float pageW, float y) {
        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.setColor(Color.BLACK);
        g2.drawString("Module Serial:", 0, y + 9);

        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.setColor(new Color(33, 150, 243));
        g2.drawString(data.serialNo, 78, y + 9);

        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.setColor(Color.BLACK);
        g2.drawString("TID:", (pageW / 2f), y + 9);

        g2.setFont(new Font("Arial", Font.PLAIN, 8));
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(data.tid, (pageW / 2f) + 22, y + 9);

        return y + 16;
    }

    // ── Specification Table ───────────────────────────────────────────────────
    private float drawTable(Graphics2D g2, float pageW, float y) {
        String[][] rows = {
            {"1",  "Name of the Manufacturer of PV Module",          data.manufacturer},
            {"2",  "Name of the Manufacturer of Solar Cell",          data.cellMfr},
            {"3",  "Module Type",                                     data.moduleType},
            {"4",  "Month & Year of the Manufacture of Module",       data.moduleMfgDate},
            {"5",  "Month & Year of the Manufacture of Solar Cell",   data.cellMfgDate},
            {"6",  "Country of Origin for PV Module",                 data.moduleCountry},
            {"7",  "Country of Origin for Solar Cell",                data.cellCountry},
            {"8",  "Power: P-Max of the Module",                      data.pmax},
            {"9",  "Voltage: V-Max of the Module",                    data.vmpp},
            {"10", "Current: I-Max of the Module",                    data.impp},
            {"11", "Fill Factor (FF) of the Module",                  data.ff},
            {"12", "VOC",                                             data.voc},
            {"13", "ISC",                                             data.isc},
            {"14", "Name of The Test Lab Issuing IEC Certificate",    data.testLab},
            {"15", "Date of Obtaining IEC Certificate",               data.iecDate},
        };

        float rowH     = 16f;
        float col0W    = 30f;
        float col2W    = 120f;
        float col1W    = pageW - col0W - col2W;

        // Header row
        g2.setColor(new Color(255, 255, 200));
        g2.fillRect(0, (int)y, (int)pageW, (int)rowH);
        g2.setColor(new Color(190, 200, 215));
        g2.drawRect(0, (int)y, (int)pageW, (int)rowH);

        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.setColor(Color.BLACK);
        g2.drawString("Sr No.", 4, y + 11);
        g2.drawString("Parameter", col0W + 4, y + 11);
        g2.drawString("Value", col0W + col1W + 4, y + 11);
        y += rowH;

        // Data rows
        for (int i = 0; i < rows.length; i++) {
            Color bg = (i % 2 == 0) ? Color.WHITE : new Color(247, 250, 255);
            g2.setColor(bg);
            g2.fillRect(0, (int)y, (int)pageW, (int)rowH);

            // grid lines
            g2.setColor(new Color(210, 215, 225));
            g2.drawRect(0, (int)y, (int)pageW, (int)rowH);
            g2.drawLine((int)col0W, (int)y, (int)col0W, (int)(y + rowH));
            g2.drawLine((int)(col0W + col1W), (int)y, (int)(col0W + col1W), (int)(y + rowH));

            // Sr No
            g2.setFont(new Font("Arial", Font.PLAIN, 8));
            g2.setColor(Color.BLACK);
            FontMetrics fm0 = g2.getFontMetrics();
            int nw = fm0.stringWidth(rows[i][0]);
            g2.drawString(rows[i][0], (col0W - nw) / 2, y + 11);

            // Parameter
            g2.setFont(new Font("Arial", Font.PLAIN, 8));
            g2.setColor(Color.BLACK);
            g2.drawString(rows[i][1], col0W + 4, y + 11);

            // Value
            g2.setFont(new Font("Arial", Font.BOLD, 8));
            g2.setColor(new Color(20, 100, 190));
            g2.drawString(rows[i][2] != null ? rows[i][2] : "", col0W + col1W + 4, y + 11);

            y += rowH;
        }
        return y;
    }

//     // ── IV Curve ──────────────────────────────────────────────────────────────
    private void drawIVCurve(Graphics2D g2, float x, float y, float w, float h) {
        // border
        g2.setColor(new Color(190, 200, 215));
        g2.drawRect((int)x, (int)y, (int)w, (int)h);

        if (data.vocVal <= 0 || data.iscVal <= 0 || data.pmaxVal <= 0) {
            g2.setFont(new Font("Arial", Font.ITALIC, 9));
            g2.setColor(Color.GRAY);
            g2.drawString("IV data not available", x + w/2 - 50, y + h/2);
            return;
        }

        float pad = 30f;
        float plotX = x + pad;
        float plotY = y + 10;
        float plotW  = w - pad - 10;
        float plotH  = h - 20;

        double voc  = data.vocVal;
        double isc  = data.iscVal;
        double pmax = data.pmaxVal;
        double vmpp = voc * 0.78;
        double impp = pmax / vmpp;

        // Axes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1f));
        // X axis
        g2.drawLine((int)plotX, (int)(plotY + plotH), (int)(plotX + plotW), (int)(plotY + plotH));
        // Y axis
        g2.drawLine((int)plotX, (int)plotY, (int)plotX, (int)(plotY + plotH));

        // Axis labels
        g2.setFont(new Font("Arial", Font.PLAIN, 7));
        g2.drawString("Voltage (V)", (int)(plotX + plotW / 2 - 20), (int)(plotY + plotH + 12));
        // rotated Y label
        Graphics2D g2r = (Graphics2D) g2.create();
        g2r.translate(x + 8, plotY + plotH / 2 + 20);
        g2r.rotate(-Math.PI / 2);
        g2r.setFont(new Font("Arial", Font.PLAIN, 7));
        g2r.drawString("Current (A)", 0, 0);
        g2r.dispose();

        // Tick marks + values on axes
        int xTicks = 5;
        for (int i = 0; i <= xTicks; i++) {
            double v = voc * i / xTicks;
            float px = plotX + (float)(v / voc) * plotW;
            g2.drawLine((int)px, (int)(plotY + plotH), (int)px, (int)(plotY + plotH + 3));
            g2.setFont(new Font("Arial", Font.PLAIN, 6));
            g2.drawString(String.format("%.1f", v), (int)(px - 5), (int)(plotY + plotH + 10));
        }
        int yTicks = 4;
        for (int i = 0; i <= yTicks; i++) {
            double curr = isc * i / yTicks;
            float py = plotY + plotH - (float)(curr / isc) * plotH;
            g2.drawLine((int)plotX - 3, (int)py, (int)plotX, (int)py);
            g2.setFont(new Font("Arial", Font.PLAIN, 6));
            g2.drawString(String.format("%.1f", curr), (int)(plotX - 20), (int)(py + 3));
        }

        // IV Curve (smooth with many points)
        g2.setColor(new Color(33, 150, 243));
        g2.setStroke(new BasicStroke(1.5f));
        int pts = 120;
        int[] xs = new int[pts];
        int[] ys = new int[pts];
        for (int i = 0; i < pts; i++) {
            double v  = voc * i / (pts - 1);
            // Simplified diode model
            double ii = isc * (1 - Math.exp((v - voc) / (voc * 0.07)));
            if (ii < 0) ii = 0;
            xs[i] = (int)(plotX + (v / voc) * plotW);
            ys[i] = (int)(plotY + plotH - (ii / isc) * plotH);
        }
        g2.drawPolyline(xs, ys, pts);

        // PV (Power) Curve
        g2.setColor(new Color(220, 50, 50));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
            1f, new float[]{3, 2}, 0));
        int[] pxs = new int[pts];
        int[] pys = new int[pts];
        double maxP = 0;
        for (int i = 0; i < pts; i++) {
            double v  = voc * i / (pts - 1);
            double ii = isc * (1 - Math.exp((v - voc) / (voc * 0.07)));
            if (ii < 0) ii = 0;
            double p = v * ii;
            if (p > maxP) maxP = p;
        }
        for (int i = 0; i < pts; i++) {
            double v  = voc * i / (pts - 1);
            double ii = isc * (1 - Math.exp((v - voc) / (voc * 0.07)));
            if (ii < 0) ii = 0;
            double p  = v * ii;
            pxs[i] = (int)(plotX + (v / voc) * plotW);
            pys[i] = (int)(plotY + plotH - (p / maxP) * plotH);
        }
        g2.drawPolyline(pxs, pys, pts);
        g2.setStroke(new BasicStroke(1f));

        // MPP dot
        float mppX = plotX + (float)(vmpp / voc) * plotW;
        float mppY = plotY + plotH - (float)(impp / isc) * plotH;
        g2.setColor(new Color(255, 140, 0));
        g2.fillOval((int)(mppX - 3), (int)(mppY - 3), 6, 6);
        g2.setFont(new Font("Arial", Font.BOLD, 6));
        g2.setColor(new Color(255, 140, 0));
        g2.drawString(String.format("MPP(%.1fV,%.2fA)", vmpp, impp),
            (int)(mppX + 4), (int)(mppY - 2));

        // Legend
        g2.setFont(new Font("Arial", Font.PLAIN, 7));
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(33, 150, 243));
        g2.drawLine((int)(plotX + plotW - 80), (int)(plotY + 8),
                    (int)(plotX + plotW - 65), (int)(plotY + 8));
        g2.setColor(Color.BLACK);
        g2.drawString("I-V", (int)(plotX + plotW - 62), (int)(plotY + 11));

        g2.setColor(new Color(220, 50, 50));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
            1f, new float[]{3, 2}, 0));
        g2.drawLine((int)(plotX + plotW - 45), (int)(plotY + 8),
                    (int)(plotX + plotW - 30), (int)(plotY + 8));
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Color.BLACK);
        g2.drawString("P-V", (int)(plotX + plotW - 27), (int)(plotY + 11));
    }
}




