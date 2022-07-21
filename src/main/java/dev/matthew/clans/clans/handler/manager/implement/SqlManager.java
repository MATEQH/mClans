package dev.matthew.clans.clans.handler.manager.implement;

import dev.matthew.clans.clans.handler.clan.Clan;
import dev.matthew.clans.clans.handler.clan.ClanHandler;
import dev.matthew.clans.clans.handler.file.Config;
import dev.matthew.clans.clans.handler.manager.Manager;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.sql.*;

public class SqlManager extends Manager {

    private Connection connection;

    public SqlManager(Plugin plugin) {
        super(plugin);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + Config.DATABASE.AUTH.HOST + ":" + Config.DATABASE.AUTH.PORT +
                    "/" + Config.DATABASE.AUTH.DATABASE + "?autoReconnect=true&connectTimeout=10000&failOverReadOnly=false", Config.DATABASE.AUTH.LOGIN.USER, Config.DATABASE.AUTH.LOGIN.PASSWORD);
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `clans` (id VARCHAR(255) PRIMARY KEY, data LONGTEXT)");
            statement.execute();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Manager save(Clan clan) {
        try {
            boolean exists = false;
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `clans` WHERE `id`=?");
            statement.setString(1, clan.getName());
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                exists = true;
            }
            resultSet.close();
            statement.close();
            if (exists) {
                statement = connection.prepareStatement("UPDATE `clans` SET `data`=? WHERE `id`=?");
                statement.setString(1, gson.toJson(clan.serialize()));
                statement.setString(2, clan.getName().toLowerCase());
            } else {
                statement = connection.prepareStatement("INSERT INTO `clans` (`id`, `data`) VALUES (?, ?)");
                statement.setString(1, clan.getName().toLowerCase());
                statement.setString(2, gson.toJson(clan.serialize()));
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return this;
    }

    // INSERT INTO `clans` (`id`, `data`) VALUES ('Syrix', 'none');
    // SELECT * FROM `clans` WHERE id = 'syrix'
    // UPDATE `clans` SET `data` = 'test string' WHERE `id` = 'syrix'

    @Override
    public Manager remove(Clan clan) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `clans` WHERE `id`=?");
            statement.setString(1, clan.getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return this;
    }

    @Override
    public Manager loadAll() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `clans`");
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                String data = resultSet.getString(2);
                Clan clan = new Clan(Document.parse(data));
                ClanHandler.getClanMap().put(clan.getName().toLowerCase(), clan);
                clan.getMembers().keySet().forEach(uuid -> ClanHandler.getPlayerMap().put(uuid, clan));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return this;
    }

    @Override
    public Manager saveAll() {
        ClanHandler.getClanMap().values().forEach(this::save);
        return this;
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
