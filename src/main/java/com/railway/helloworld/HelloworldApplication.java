package com.railway.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;


@SpringBootApplication
public class HelloworldApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Successfully connected to PostgreSQL!");
            
            // Create table if not exists
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS sales (id SERIAL PRIMARY KEY, name VARCHAR(100))");
                System.out.println("Created 'sales' table if it didn't exist");
                
                // Insert sample record
                stmt.executeUpdate("INSERT INTO sales (name) VALUES ('Sales')");
                System.out.println("Inserted sample record into 'sales' table");
                
                // Verify insertion
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sales")) {
                    if (rs.next()) {
                        System.out.println("Total records in 'sales' table: " + rs.getInt(1));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to connect to PostgreSQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}