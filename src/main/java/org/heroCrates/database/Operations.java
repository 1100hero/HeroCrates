package org.heroCrates.database;

import org.bukkit.Bukkit;
import org.heroCrates.HeroCrates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Operations {

    private final HeroCrates plugin;

    public Operations(HeroCrates plugin) {
        this.plugin = plugin;
    }

    public void insertPhysicalKey(UUID playerUUID, String keyType) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = plugin.getHikari().getConnection()) {

                int keyId;
                try (PreparedStatement insertKeyStmt = connection.prepareStatement(
                        "INSERT INTO `key_data` (type, is_virtual) VALUES (?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

                    insertKeyStmt.setString(1, keyType);
                    insertKeyStmt.setBoolean(2, false);
                    insertKeyStmt.execute();

                    try (ResultSet generatedKeys = insertKeyStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            keyId = generatedKeys.getInt(1);
                        } else {
                            plugin.getLogger().severe("Error while inserting physical key data for player: " + playerUUID);
                            return;
                        }
                    }
                }

                try (PreparedStatement insertPlayerStatsStmt = connection.prepareStatement(
                        "INSERT INTO `player_stats` (uuid, key_id, used) VALUES (?, ?, ?)")) {

                    insertPlayerStatsStmt.setString(1, playerUUID.toString());
                    insertPlayerStatsStmt.setInt(2, keyId);
                    insertPlayerStatsStmt.setBoolean(3, true);
                    insertPlayerStatsStmt.execute();
                }

            } catch (SQLException e) {
                plugin.getLogger().severe("Error while inserting physical key data for player: " + playerUUID);
            }
        });
    }

    public void insertVirtualKey(UUID playerUUID, String keyType, int amount) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (int i = 0; i < amount; i++) {
                try (Connection connection = plugin.getHikari().getConnection()) {

                    int keyId;
                    try (PreparedStatement insertKeyStmt = connection.prepareStatement(
                            "INSERT INTO `key_data` (type, is_virtual) VALUES (?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS)) {

                        insertKeyStmt.setString(1, keyType);
                        insertKeyStmt.setBoolean(2, true);
                        insertKeyStmt.execute();

                        try (ResultSet generatedKeys = insertKeyStmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                keyId = generatedKeys.getInt(1);
                            } else {
                                plugin.getLogger().severe("Error while inserting virtual key data for player: " + playerUUID);
                                return;
                            }
                        }
                    }

                    try (PreparedStatement insertPlayerStatsStmt = connection.prepareStatement(
                            "INSERT INTO `player_stats` (uuid, key_id, used) VALUES (?, ?, ?)")) {

                        insertPlayerStatsStmt.setString(1, playerUUID.toString());
                        insertPlayerStatsStmt.setInt(2, keyId);
                        insertPlayerStatsStmt.setBoolean(3, false);
                        insertPlayerStatsStmt.execute();
                    }
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error while inserting virtual key data for player: " + playerUUID);
                }
            }
        });
    }

    public int countUnusedVirtualKeys(UUID playerUUID, String keyType) {
        String query =
                "SELECT COUNT(*) AS unused_keys " +
                        "FROM `player_stats` ps " +
                        "JOIN `key_data` kd ON ps.key_id = kd.id " +
                        "WHERE ps.uuid = ? AND kd.is_virtual = ? AND ps.used = ? AND kd.type = ?";

        try (Connection connection = plugin.getHikari().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, playerUUID.toString());
            stmt.setBoolean(2, true);
            stmt.setBoolean(3, false);
            stmt.setString(4, keyType);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("unused_keys");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while counting virtual keys for player: " + playerUUID + " and keyType: " + keyType);
        }
        return 0;
    }

    public void removeOneVirtualKey(UUID playerUUID, String keyType) {
        String query =
                "SELECT ps.id " +
                        "FROM `player_stats` ps " +
                        "JOIN `key_data` kd ON ps.key_id = kd.id " +
                        "WHERE ps.uuid = ? AND kd.is_virtual = ? AND ps.used = ? AND kd.type = ? " +
                        "LIMIT 1";

        try (Connection connection = plugin.getHikari().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, playerUUID.toString());
            stmt.setBoolean(2, true);
            stmt.setBoolean(3, false);
            stmt.setString(4, keyType);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int playerStatsId = rs.getInt("id");
                    try (PreparedStatement updateStmt = connection.prepareStatement(
                            "UPDATE `player_stats` SET used = ? WHERE id = ?")) {

                        updateStmt.setBoolean(1, true);
                        updateStmt.setInt(2, playerStatsId);
                        updateStmt.execute();
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while removing virtual key for player: " + playerUUID + " and keyType: " + keyType);
        }
    }
}
