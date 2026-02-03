package com.solar.rfid.db;

import java.sql.*;
import com.solar.rfid.model.StaticPanelData;

public class StaticDataRepository {

    public static StaticPanelData load() {

        StaticPanelData d = new StaticPanelData();

        try (Connection con = DBUtil.getConnection()) {

            ResultSet rs = con.createStatement()
                .executeQuery("SELECT * FROM static_panel_data WHERE id = 1");

            if (rs.next()) {
                d.setManufacturer(rs.getString("manufacturer"));
                d.setCellManufacturer(rs.getString("cell_manufacturer"));
                d.setModuleType(rs.getString("module_type"));
                d.setModuleCountry(rs.getString("module_country"));
                d.setCellCountry(rs.getString("cell_country"));
                d.setTestLab(rs.getString("test_lab"));
                d.setIecDate(rs.getString("iec_date"));
                d.setFactoryCode(rs.getString("factory_code"));
                d.setLineCode(rs.getString("line_code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    public static void save(StaticPanelData d) {

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "UPDATE static_panel_data SET " +
                "manufacturer=?, cell_manufacturer=?, module_type=?, " +
                "module_country=?, cell_country=?, test_lab=?, iec_date=?, " +
                "factory_code=?, line_code=? " +
                "WHERE id = 1"
            );

            ps.setString(1, d.getManufacturer());
            ps.setString(2, d.getCellManufacturer());
            ps.setString(3, d.getModuleType());
            ps.setString(4, d.getModuleCountry());
            ps.setString(5, d.getCellCountry());
            ps.setString(6, d.getTestLab());
            ps.setString(7, d.getIecDate());
            ps.setString(8, d.getFactoryCode());
            ps.setString(9, d.getLineCode());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
