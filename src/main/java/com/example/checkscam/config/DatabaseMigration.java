package com.example.checkscam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigration implements CommandLineRunner {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            // Check if image_url column exists
            String checkColumnQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_NAME = 'tournaments' AND COLUMN_NAME = 'image_url'";
            
            Integer columnExists = jdbcTemplate.queryForObject(checkColumnQuery, Integer.class);
            
            if (columnExists == 0) {
                jdbcTemplate.execute("ALTER TABLE tournaments ADD COLUMN image_url VARCHAR(500)");
            }
            
        } catch (Exception e) {
            // Silently ignore migration errors in production
        }
    }
}
