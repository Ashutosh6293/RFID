package com.solar.rfid.sync;

import java.sql.*;
import java.util.concurrent.*;

import com.solar.rfid.db.DBUtil;

public class DataSyncService {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void startAutoSync() {

        scheduler.scheduleAtFixedRate(() -> {

            System.out.println("🔄 AUTO SYNC STARTED...");
            syncStaticData();
            syncPanelData();
            System.out.println("✅ AUTO SYNC FINISHED");

        }, 0, 10, TimeUnit.MINUTES);

        System.out.println("✅ Background Sync Service Running...");
    }

    // ================= STATIC TABLE SYNC =================
    // private static void syncStaticData() {

    //     try (
    //             Connection local = DriverManager.getConnection(
    //                     "jdbc:mysql://localhost:3306/solar_rfid",
    //                     "root", "Ashu@620930");

    //             Connection server = DriverManager.getConnection(
    //                     "jdbc:mysql://93.127.194.235:3306/solar_rfid",
    //                     "rohit", "rohit0101");) {

    //         PreparedStatement ps = local.prepareStatement(
    //                 "SELECT * FROM static_panel_data WHERE synced = 0");

    //         ResultSet rs = ps.executeQuery();

    //         while (rs.next()) {

    //             PreparedStatement insert = server.prepareStatement(
    //                     "INSERT INTO static_panel_data (" +
    //                             "id, manufacturer, cell_manufacturer, cell_manufacturing_date, " +
    //                             "module_type, module_country, cell_country, test_lab, iec_date, " +
    //                             "factory_code, line_code, updated_at" +
    //                             ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?) " +
    //                             "ON DUPLICATE KEY UPDATE " +
    //                             "manufacturer=VALUES(manufacturer), " +
    //                             "cell_manufacturer=VALUES(cell_manufacturer)");

    //             insert.setInt(1, rs.getInt("id"));
    //             insert.setString(2, rs.getString("manufacturer"));
    //             insert.setString(3, rs.getString("cell_manufacturer"));
    //             insert.setDate(4, rs.getDate("cell_manufacturing_date"));
    //             insert.setString(5, rs.getString("module_type"));
    //             insert.setString(6, rs.getString("module_country"));
    //             insert.setString(7, rs.getString("cell_country"));
    //             insert.setString(8, rs.getString("test_lab"));
    //             insert.setString(9, rs.getString("iec_date"));
    //             insert.setString(10, rs.getString("factory_code"));
    //             insert.setString(11, rs.getString("line_code"));
    //             insert.setTimestamp(12, rs.getTimestamp("updated_at"));

    //             insert.executeUpdate();

    //             PreparedStatement mark = local.prepareStatement(
    //                     "UPDATE static_panel_data SET synced = 1 WHERE id = ?");
    //             mark.setInt(1, rs.getInt("id"));
    //             mark.executeUpdate();
    //         }

    //     } catch (Exception e) {
    //         System.out.println("❌ Static Sync Failed");
    //         e.printStackTrace();
    //     }
    // }
    private static void syncStaticData() {

    try (
            Connection local = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/solar_rfid?useSSL=false",
                    "root", "Ashu@620930");

            Connection server = DriverManager.getConnection(
                    "jdbc:mysql://93.127.194.235:3306/solar_rfid?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "rohit", "rohit0101")) {

        server.setAutoCommit(false);

        PreparedStatement ps = local.prepareStatement(
                "SELECT * FROM static_panel_data WHERE synced = 0");

        ResultSet rs = ps.executeQuery();

        PreparedStatement insert = server.prepareStatement(
                "INSERT INTO static_panel_data (" +
                        "id, manufacturer, cell_manufacturer, cell_manufacturing_date, " +
                        "module_type, module_country, cell_country, test_lab, iec_date, " +
                        "factory_code, line_code, updated_at" +
                        ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "manufacturer=VALUES(manufacturer), " +
                        "cell_manufacturer=VALUES(cell_manufacturer)");

        PreparedStatement mark = local.prepareStatement(
                "UPDATE static_panel_data SET synced = 1 WHERE id = ?");

        while (rs.next()) {

            insert.setInt(1, rs.getInt("id"));
            insert.setString(2, rs.getString("manufacturer"));
            insert.setString(3, rs.getString("cell_manufacturer"));
            insert.setDate(4, rs.getDate("cell_manufacturing_date"));
            insert.setString(5, rs.getString("module_type"));
            insert.setString(6, rs.getString("module_country"));
            insert.setString(7, rs.getString("cell_country"));
            insert.setString(8, rs.getString("test_lab"));
            insert.setString(9, rs.getString("iec_date"));
            insert.setString(10, rs.getString("factory_code"));
            insert.setString(11, rs.getString("line_code"));
            insert.setTimestamp(12, rs.getTimestamp("updated_at"));

            insert.addBatch();

            mark.setInt(1, rs.getInt("id"));
            mark.addBatch();
        }

        insert.executeBatch();
        server.commit();
        mark.executeBatch();

    } catch (Exception e) {
        System.out.println("❌ Static Sync Failed");
        e.printStackTrace();
    }
}

    // ================= PANEL TABLE SYNC =================
    // private static void syncPanelData() {

    //     try (
    //             Connection local = DBUtil.getConnection(); // ✅ CORRECT

    //             Connection server = DriverManager.getConnection(
    //                     "jdbc:mysql://93.127.194.235:3306/solar_rfid"
    //                             + "?useSSL=false"
    //                             + "&allowPublicKeyRetrieval=true"
    //                             + "&serverTimezone=UTC",
    //                     "rohit",
    //                     "rohit0101");) {

    //         PreparedStatement ps = local.prepareStatement(
    //                 "SELECT * FROM panel_data WHERE synced = 0");

    //         ResultSet rs = ps.executeQuery();

    //         while (rs.next()) {

    //             PreparedStatement insert = server.prepareStatement(
    //                     "INSERT INTO panel_data (" +
    //                             "id, epc, date, pmax, isc, voc, ipm, vpm, ff, rs, rsh, eff, " +
    //                             "t_object, t_target, irr_target, class, sweep_time, " +
    //                             "irr_moncell, isc_moncell, t_moncell, t_ambient, binning, status, static_id" +
    //                             ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
    //                             "ON DUPLICATE KEY UPDATE status=VALUES(status)");

    //             insert.setString(1, rs.getString("id"));
    //             insert.setString(2, rs.getString("epc"));
    //             insert.setTimestamp(3, rs.getTimestamp("date"));
    //             insert.setDouble(4, rs.getDouble("pmax"));
    //             insert.setDouble(5, rs.getDouble("isc"));
    //             insert.setDouble(6, rs.getDouble("voc"));
    //             insert.setDouble(7, rs.getDouble("ipm"));
    //             insert.setDouble(8, rs.getDouble("vpm"));
    //             insert.setDouble(9, rs.getDouble("ff"));
    //             insert.setDouble(10, rs.getDouble("rs"));
    //             insert.setDouble(11, rs.getDouble("rsh"));
    //             insert.setDouble(12, rs.getDouble("eff"));
    //             insert.setDouble(13, rs.getDouble("t_object"));
    //             insert.setDouble(14, rs.getDouble("t_target"));
    //             insert.setInt(15, rs.getInt("irr_target"));
    //             insert.setString(16, rs.getString("class"));
    //             insert.setInt(17, rs.getInt("sweep_time"));
    //             insert.setDouble(18, rs.getDouble("irr_moncell"));
    //             insert.setDouble(19, rs.getDouble("isc_moncell"));
    //             insert.setDouble(20, rs.getDouble("t_moncell"));
    //             insert.setDouble(21, rs.getDouble("t_ambient"));
    //             insert.setString(22, rs.getString("binning"));
    //             insert.setString(23, rs.getString("status"));
    //             insert.setInt(24, rs.getInt("static_id"));

    //             insert.executeUpdate();

    //             PreparedStatement mark = local.prepareStatement(
    //                     "UPDATE panel_data SET synced = 1 WHERE id = ?");
    //             mark.setString(1, rs.getString("id"));
    //             mark.executeUpdate();
    //         }

    //     } catch (Exception e) {
    //         System.out.println("❌ Panel Sync Failed");
    //         e.printStackTrace();
    //     }
    // }
    private static void syncPanelData() {

    try (
            Connection local = DBUtil.getConnection();

            Connection server = DriverManager.getConnection(
                    "jdbc:mysql://93.127.194.235:3306/solar_rfid"
                            + "?useSSL=false"
                            + "&allowPublicKeyRetrieval=true"
                            + "&serverTimezone=UTC",
                    "rohit",
                    "rohit0101")) {

        server.setAutoCommit(false);

        PreparedStatement ps = local.prepareStatement(
                "SELECT * FROM panel_data WHERE synced = 1 AND static_id IS NOT NULL");

        ResultSet rs = ps.executeQuery();

        PreparedStatement insert = server.prepareStatement(
                "INSERT INTO panel_data (" +
                        "id, epc, date, pmax, isc, voc, ipm, vpm, ff, rs, rsh, eff, " +
                        "t_object, t_target, irr_target, class, sweep_time, " +
                        "irr_moncell, isc_moncell, t_moncell, t_ambient, binning, status, static_id" +
                        ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status)");

        PreparedStatement mark = local.prepareStatement(
                "UPDATE panel_data SET synced = 1 WHERE id = ?");

        while (rs.next()) {

            insert.setString(1, rs.getString("id"));
            insert.setString(2, rs.getString("epc"));
            insert.setTimestamp(3, rs.getTimestamp("date"));
            insert.setDouble(4, rs.getDouble("pmax"));
            insert.setDouble(5, rs.getDouble("isc"));
            insert.setDouble(6, rs.getDouble("voc"));
            insert.setDouble(7, rs.getDouble("ipm"));
            insert.setDouble(8, rs.getDouble("vpm"));
            insert.setDouble(9, rs.getDouble("ff"));
            insert.setDouble(10, rs.getDouble("rs"));
            insert.setDouble(11, rs.getDouble("rsh"));
            insert.setDouble(12, rs.getDouble("eff"));
            insert.setDouble(13, rs.getDouble("t_object"));
            insert.setDouble(14, rs.getDouble("t_target"));
            insert.setInt(15, rs.getInt("irr_target"));
            insert.setString(16, rs.getString("class"));
            insert.setInt(17, rs.getInt("sweep_time"));
            insert.setDouble(18, rs.getDouble("irr_moncell"));
            insert.setDouble(19, rs.getDouble("isc_moncell"));
            insert.setDouble(20, rs.getDouble("t_moncell"));
            insert.setDouble(21, rs.getDouble("t_ambient"));
            insert.setString(22, rs.getString("binning"));
            insert.setString(23, rs.getString("status"));
            insert.setInt(24, rs.getInt("static_id"));

            insert.addBatch();

            mark.setString(1, rs.getString("id"));
            mark.addBatch();
        }

        insert.executeBatch();
        server.commit();
        mark.executeBatch();

    } catch (Exception e) {
        System.out.println("❌ Panel Sync Failed");
        e.printStackTrace();
    }
}
}