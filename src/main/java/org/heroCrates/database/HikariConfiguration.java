package org.heroCrates.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.heroCrates.HeroCrates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HikariConfiguration {

    private final FileConfiguration config;
    private final HeroCrates plugin;
    @Getter
    private HikariDataSource dataSource;

    public HikariConfiguration(HeroCrates plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        startConnection();
        createTable();
    }

    private void startConnection() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getString("database.host") + ":" + config.getInt("database.port") + "/" + config.getString("database.database"));
        hikariConfig.setUsername(config.getString("database.username"));
        hikariConfig.setPassword(config.getString("database.password"));

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    @SneakyThrows
    public Connection getConnection() {
        return dataSource.getConnection();
    }

    private void createTable() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {

                try (PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `key_data` (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "type VARCHAR(255) NOT NULL, " +
                                "is_virtual BOOLEAN NOT NULL)")) {
                    statement.execute();
                }

                try (PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `player_stats` (" +
                                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                                "uuid CHAR(36) NOT NULL, " +
                                "key_id INT NOT NULL, " +
                                "used BOOLEAN NOT NULL, " +
                                "CONSTRAINT fk_stats_keys FOREIGN KEY (key_id) REFERENCES key_data (id))")) {
                    statement.execute();
                }
            } catch (SQLException e) {
                plugin.getLogger().info("Problema nella creazione delle tabelle.");
            }
        });
    }
}
