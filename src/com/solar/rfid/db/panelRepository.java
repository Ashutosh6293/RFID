// package com.solar.rfid.db;

// import java.sql.Connection;
// import java.sql.PreparedStatement;

// public class PanelRepository {

//     public static void updateEPC(String serial, String epc) {
//         try (Connection con = DBUtil.getConnection()) {
//             PreparedStatement ps =
//                     con.prepareStatement(
//                             "UPDATE panel_data SET epc=? WHERE id=?"
//                     );
//             ps.setString(1, epc);
//             ps.setString(2, serial);
//             ps.executeUpdate();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }





package com.solar.rfid.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PanelRepository {

    public static void updateEPC(String panelId, String epc) {
        String sql = "UPDATE panel_data SET epc=? WHERE id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, epc);
            ps.setString(2, panelId);
            ps.executeUpdate();

            System.out.println("EPC updated for panel: " + panelId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
