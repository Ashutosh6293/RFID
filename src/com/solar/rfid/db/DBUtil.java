package com.solar.rfid.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String URL =
    "jdbc:mysql://localhost:3306/solar_rfid"
    + "?useSSL=false"
    + "&allowPublicKeyRetrieval=true"
    + "&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASS = "Ashu@620930";
    // private static final String URL = "jdbc:mysql://93.127.194.235:3306/solar_rfid"
    //         + "?useSSL=false"
    //         + "&allowPublicKeyRetrieval=true"
    //         + "&serverTimezone=UTC";

    // private static final String USER = "rohit";
    // private static final String PASS = "rohit0101";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver"); // MySQL 5.1.x
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
