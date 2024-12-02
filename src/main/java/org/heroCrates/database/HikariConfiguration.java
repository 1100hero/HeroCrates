package org.heroCrates.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.heroCrates.HeroCrates;

import java.sql.Connection;

public class HikariConfiguration {

    private final FileConfiguration config;
    @Getter
    private HikariDataSource dataSource;

    public HikariConfiguration(HeroCrates plugin) {
        this.config = plugin.getConfig();
        //startConnection();
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
}
