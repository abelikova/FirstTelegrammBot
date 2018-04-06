package com.company;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public Connection getPostgresConnection() {
        try {
            DriverManager.registerDriver((Driver)

                    Class.forName("org.postgresql.Driver").newInstance());

            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/botdb","postgres", "postgres");
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
