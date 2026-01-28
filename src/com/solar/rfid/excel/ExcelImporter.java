package com.solar.rfid.excel;

import com.solar.rfid.db.DBUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelImporter {

    public static void importExcel(File file) {

        String sql =
            "INSERT INTO panel_data (" +
            "date, id, pmax, isc, voc, ipm, vpm, ff, rs, rsh, eff, " +
            "t_object, t_target, irr_target, class, sweep_time, " +
            "irr_moncell, isc_moncell, t_moncell, t_ambient, binning" +
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE " +
            "date=VALUES(date), pmax=VALUES(pmax), isc=VALUES(isc), voc=VALUES(voc), " +
            "ipm=VALUES(ipm), vpm=VALUES(vpm), ff=VALUES(ff), rs=VALUES(rs), " +
            "rsh=VALUES(rsh), eff=VALUES(eff), t_object=VALUES(t_object), " +
            "t_target=VALUES(t_target), irr_target=VALUES(irr_target), " +
            "class=VALUES(class), sweep_time=VALUES(sweep_time), " +
            "irr_moncell=VALUES(irr_moncell), isc_moncell=VALUES(isc_moncell), " +
            "t_moncell=VALUES(t_moncell), t_ambient=VALUES(t_ambient), " +
            "binning=VALUES(binning)";

        try (
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            FileInputStream fis = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(fis)
        ) {

            Sheet sh = wb.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();
            SimpleDateFormat mysqlFmt =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat excelTextFmt =
                    new SimpleDateFormat("dd-MM-yyyy HH:mm");

            for (int i = 1; i <= sh.getLastRowNum(); i++) {
                Row r = sh.getRow(i);
                if (r == null) continue;

                /* ---------------- DATE (SAFE) ---------------- */
                String mysqlDate = null;
                Cell dateCell = r.getCell(0);

                if (dateCell != null) {
                    try {
                        if (dateCell.getCellType() == CellType.NUMERIC
                                && DateUtil.isCellDateFormatted(dateCell)) {

                            Date d = dateCell.getDateCellValue();
                            if (d != null) {
                                mysqlDate = mysqlFmt.format(d);
                            }

                        } else {
                            String raw = fmt.formatCellValue(dateCell).trim();
                            if (!raw.isEmpty()) {
                                Date d = excelTextFmt.parse(raw);
                                mysqlDate = mysqlFmt.format(d);
                            }
                        }
                    } catch (Exception ignore) {
                        mysqlDate = null;
                    }
                }
                ps.setString(1, mysqlDate);

                /* ---------------- ID (BARCODE STRING) ---------------- */
                ps.setString(2, fmt.formatCellValue(r.getCell(1)).trim());

                /* ---------------- NUMERIC ---------------- */
                ps.setDouble(3, getDouble(fmt, r, 2));   // Pmax
                ps.setDouble(4, getDouble(fmt, r, 3));   // Isc
                ps.setDouble(5, getDouble(fmt, r, 4));   // Voc
                ps.setDouble(6, getDouble(fmt, r, 5));   // Ipm
                ps.setDouble(7, getDouble(fmt, r, 6));   // Vpm
                ps.setDouble(8, getDouble(fmt, r, 7));   // FF
                ps.setDouble(9, getDouble(fmt, r, 8));   // Rs
                ps.setDouble(10, getDouble(fmt, r, 9));  // Rsh
                ps.setDouble(11, getDouble(fmt, r, 10)); // Eff
                ps.setDouble(12, getDouble(fmt, r, 11)); // T_Object
                ps.setDouble(13, getDouble(fmt, r, 12)); // T_Target
                ps.setDouble(14, getDouble(fmt, r, 13)); // Irr_Target

                /* ---------------- STRING ---------------- */
                ps.setString(15, fmt.formatCellValue(r.getCell(14))); // Class

                /* ---------------- MORE NUMERIC ---------------- */
                ps.setInt(16, (int) getDouble(fmt, r, 15)); // Sweep_Time
                ps.setDouble(17, getDouble(fmt, r, 16));    // Irr_MonCell
                ps.setDouble(18, getDouble(fmt, r, 17));    // Isc_MonCell
                ps.setDouble(19, getDouble(fmt, r, 18));    // T_MonCell
                ps.setDouble(20, getDouble(fmt, r, 19));    // T_Ambient

                /* ---------------- BINNING ---------------- */
                ps.setString(21, fmt.formatCellValue(r.getCell(20))); // Binning

                ps.executeUpdate();
            }

            System.out.println("Excel import completed successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ---------------- SAFE DOUBLE PARSER ---------------- */
    private static double getDouble(DataFormatter fmt, Row r, int idx) {
        try {
            String v = fmt.formatCellValue(r.getCell(idx));
            if (v == null || v.trim().isEmpty() || v.equalsIgnoreCase("Ok"))
                return 0.0;
            return Double.parseDouble(v.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
