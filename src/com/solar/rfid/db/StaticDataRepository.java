

// package com.solar.rfid.db;

// import java.sql.*;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

// import com.solar.rfid.model.StaticPanelData;

// public class StaticDataRepository {

//     // UI format
//     private static final DateTimeFormatter UI_FORMAT =
//             DateTimeFormatter.ofPattern("dd/MM/yyyy");

//     public static StaticPanelData load() {

//         StaticPanelData d = new StaticPanelData();

//         try (Connection con = DBUtil.getConnection()) {

//             ResultSet rs = con.createStatement()
//                     .executeQuery("SELECT * FROM static_panel_data WHERE id = 1");

//             if (rs.next()) {

//                 d.setManufacturer(rs.getString("manufacturer"));
//                 d.setCellManufacturer(rs.getString("cell_manufacturer"));

//                 // 🔥 Convert DB DATE → UI FORMAT (dd/MM/yyyy)
//                 Date dbDate = rs.getDate("cell_manufacturing_date");
//                 if (dbDate != null) {
//                     LocalDate localDate = dbDate.toLocalDate();
//                     d.setCellManufacturingDate(localDate.format(UI_FORMAT));
//                 } else {
//                     d.setCellManufacturingDate("");
//                 }

//                 d.setModuleType(rs.getString("module_type"));
//                 d.setModuleCountry(rs.getString("module_country"));
//                 d.setCellCountry(rs.getString("cell_country"));
//                 d.setTestLab(rs.getString("test_lab"));
//                 d.setIecDate(rs.getString("iec_date"));
//                 d.setFactoryCode(rs.getString("factory_code"));
//                 d.setLineCode(rs.getString("line_code"));
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         return d;
//     }

//     public static void save(StaticPanelData d) {

//         try (Connection con = DBUtil.getConnection()) {

//             PreparedStatement ps = con.prepareStatement(
//                     "UPDATE static_panel_data SET " +
//                             "manufacturer=?, cell_manufacturer=?, cell_manufacturing_date=?, module_type=?, " +
//                             "module_country=?, cell_country=?, test_lab=?, iec_date=?, " +
//                             "factory_code=?, line_code=? " +
//                             "WHERE id = 1"
//             );

//             ps.setString(1, d.getManufacturer());
//             ps.setString(2, d.getCellManufacturer());

//             // 🔥 Convert UI DATE → MySQL DATE
//             String uiDate = d.getCellManufacturingDate();

//             if (uiDate != null && !uiDate.trim().isEmpty()) {
//                 LocalDate localDate = LocalDate.parse(uiDate, UI_FORMAT);
//                 ps.setDate(3, java.sql.Date.valueOf(localDate));
//             } else {
//                 ps.setDate(3, null);
//             }

//             ps.setString(4, d.getModuleType());
//             ps.setString(5, d.getModuleCountry());
//             ps.setString(6, d.getCellCountry());
//             ps.setString(7, d.getTestLab());
//             ps.setString(8, d.getIecDate());
//             ps.setString(9, d.getFactoryCode());
//             ps.setString(10, d.getLineCode());

//             ps.executeUpdate();

//             System.out.println("Static data updated successfully ✅");

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }





package com.solar.rfid.db;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.solar.rfid.model.StaticPanelData;

public class StaticDataRepository {

    private static final DateTimeFormatter UI_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ================= LOAD LATEST STATIC =================
    public static StaticPanelData load() {

        StaticPanelData d = new StaticPanelData();

        try (Connection con = DBUtil.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM static_panel_data ORDER BY id DESC LIMIT 1");

            if (rs.next()) {

                d.setId(rs.getInt("id")); // 🔥 Added

                d.setManufacturer(rs.getString("manufacturer"));
                d.setCellManufacturer(rs.getString("cell_manufacturer"));

                Date dbDate = rs.getDate("cell_manufacturing_date");
                if (dbDate != null) {
                    LocalDate localDate = dbDate.toLocalDate();
                    d.setCellManufacturingDate(localDate.format(UI_FORMAT));
                }

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

    // ================= INSERT NEW STATIC (NO UPDATE) =================
    public static void save(StaticPanelData d) {

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO static_panel_data (" +
                            "manufacturer, cell_manufacturer, cell_manufacturing_date, module_type, " +
                            "module_country, cell_country, test_lab, iec_date, factory_code, line_code" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, d.getManufacturer());
            ps.setString(2, d.getCellManufacturer());

            if (d.getCellManufacturingDate() != null && !d.getCellManufacturingDate().isEmpty()) {
                LocalDate localDate = LocalDate.parse(d.getCellManufacturingDate(), UI_FORMAT);
                ps.setDate(3, java.sql.Date.valueOf(localDate));
            } else {
                ps.setDate(3, null);
            }

            ps.setString(4, d.getModuleType());
            ps.setString(5, d.getModuleCountry());
            ps.setString(6, d.getCellCountry());
            ps.setString(7, d.getTestLab());
            ps.setString(8, d.getIecDate());
            ps.setString(9, d.getFactoryCode());
            ps.setString(10, d.getLineCode());

            ps.executeUpdate();

            System.out.println("New Static configuration saved ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET LATEST STATIC ID =================
    public static int getLatestStaticId() {

        try (Connection con = DBUtil.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT id FROM static_panel_data ORDER BY id DESC LIMIT 1");

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    // ================= GET STATIC BY ID =================
public static StaticPanelData findById(int id) {

    StaticPanelData d = null;

    try (Connection con = DBUtil.getConnection()) {

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM static_panel_data WHERE id = ?"
        );

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            d = new StaticPanelData();

            d.setId(rs.getInt("id"));
            d.setManufacturer(rs.getString("manufacturer"));
            d.setCellManufacturer(rs.getString("cell_manufacturer"));

            Date dbDate = rs.getDate("cell_manufacturing_date");
            if (dbDate != null) {
                LocalDate localDate = dbDate.toLocalDate();
                d.setCellManufacturingDate(localDate.format(UI_FORMAT));
            }

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
}


