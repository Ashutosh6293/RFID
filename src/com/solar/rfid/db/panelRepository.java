
package com.solar.rfid.db;

import java.sql.*;
import java.text.SimpleDateFormat;
import com.solar.rfid.model.PanelData;

public class PanelRepository {

    // ================= CHECK DUPLICATE EPC =================
    public static boolean isEpcAlreadyMapped(String epc) {

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT id FROM panel_data WHERE epc=?");

            ps.setString(1, epc);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    // ================= SAFE UPDATE =================
    public static boolean updateEPC(String panelId, String epc, String status) {

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE panel_data SET epc=?, status=? WHERE id=?");

            ps.setString(1, epc);
            ps.setString(2, status);
            ps.setString(3, panelId);

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {

            System.out.println("DUPLICATE EPC ERROR");
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= FETCH PANEL =================
    public static PanelData fetchPanel(String panelId) {

        PanelData d = new PanelData();

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM panel_data WHERE id=?");

            ps.setString(1, panelId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                d.setId(rs.getString("id"));
                d.setPmax(rs.getString("pmax"));
                d.setVoc(rs.getString("voc"));
                d.setIsc(rs.getString("isc"));
                d.setEff(rs.getString("eff"));
                d.setFf(rs.getString("ff"));

                d.setBin(rs.getString("binning"));
                d.setDate(rs.getString("date"));
                d.setIpm(rs.getDouble("ipm"));
                d.setVpm(rs.getDouble("vpm"));
                d.setStatus(rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    // ================= SEARCH BY SERIAL NO ✅ =================
    public static PanelData findBySerialNo(String serialNo) {

        PanelData d = null;

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM panel_data WHERE id = ?");

            ps.setString(1, serialNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                d = new PanelData();

                d.setId(rs.getString("id"));
                d.setTid(rs.getString("epc"));          // epc column = TID
                d.setPmax(rs.getString("pmax"));
                d.setVoc(rs.getString("voc"));
                d.setIsc(rs.getString("isc"));
                d.setFf(rs.getString("ff"));
                d.setBin(rs.getString("binning"));
                d.setIpm(rs.getDouble("ipm"));
                d.setVpm(rs.getDouble("vpm"));
                d.setStatus(rs.getString("status"));

                // ✅ date column: DATETIME → "MM/yyyy" format
                Timestamp ts = rs.getTimestamp("date");
                if (ts != null) {
                    String formatted = new SimpleDateFormat("MM/yyyy").format(ts);
                    d.setDate(formatted);
                } else {
                    d.setDate("");
                }

                // cell_manufacturing_date — agar column exist karta hai
                try {
                    Date cellDate = rs.getDate("cell_manufacturing_date");
                    if (cellDate != null) {
                        String formatted = new SimpleDateFormat("MM/yyyy").format(cellDate);
                        d.setCellManufacturingDate(formatted);
                    }
                } catch (Exception ignored) {
                    // column nahi hai toh ignore karo
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d; // null = not found
    }
}