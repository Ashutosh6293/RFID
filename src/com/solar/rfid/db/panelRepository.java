package com.solar.rfid.db;

import java.sql.*;
import com.solar.rfid.model.PanelData;

public class PanelRepository {

    public static void updateEPC(String panelId, String epc) {
        try (Connection con = DBUtil.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE panel_data SET epc=? WHERE id=?"
            );
            ps.setString(1, epc);
            ps.setString(2, panelId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PanelData fetchPanel(String panelId) {

        PanelData d = new PanelData();

        try (Connection con = DBUtil.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM panel_data WHERE id=?"
            );
            ps.setString(1, panelId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d.setId(rs.getString("id"));
                d.setPmax(rs.getString("pmax"));
                d.setVoc(rs.getString("voc"));
                d.setIsc(rs.getString("isc"));
                d.setEff(rs.getString("eff"));
                d.setBin(rs.getString("binning"));
                d.setDate(rs.getString("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
