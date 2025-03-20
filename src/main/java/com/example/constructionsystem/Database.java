package com.example.constructionsystem;

import java.sql.*;

public class Database {

    public Connection connection;
    public ResultSet resultSet;
    public PreparedStatement preparedStatement;
    //wykorzystanie zmiennych środowiskowych w systemie do połaczenia z baza w celu zwiekszenia bezpieczenstwa aplikacji
    private static final String URL = System.getenv("DB_URL_CONSTRUCTION");
    private static final String USER = System.getenv("DB_USER_CONSTRUCTION");
    private static final String PASSWORD = System.getenv("DB_PASSWORD_CONSTRUCTION");
    public static Connection connectDB() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd połączenia!");
        }
        return connection;
    }
}
