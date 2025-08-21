package Dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_edu?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; // உங்கள் MySQL username
    private static final String PASS = "12345"; // உங்கள் MySQL password

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
