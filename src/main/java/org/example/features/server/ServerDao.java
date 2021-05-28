package org.example.features.server;

import org.example.core.Database;
import org.example.models.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerDao {

    public List<Server> getServersForUserId(int userId) {
        List<Server> servers = new ArrayList<>();
        Connection connection = Database.get().getConnection();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT server.* FROM server LEFT JOIN serverperuser ON server.id = serverperuser.server_id WHERE serverperuser.user_id = ?");

            st.setInt(1, userId);


            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Server server = ServerDao.mapToServer(rs);
                servers.add(server);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servers;
    }

    public static Server mapToServer(ResultSet rs) throws SQLException {
        int i = 1;
        return new Server(
                rs.getInt(i++), // id
                rs.getString(i++), // servername,
                rs.getString(i++) // avatarUrl
        );
    }

    public static int createServer(String servername, Integer userId){
        Connection connection = Database.get().getConnection();
        int newId = 0;
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO discoding.server (serverName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, servername);

            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }
            st = connection.prepareStatement("INSERT INTO discoding.serverperuser (server_id, user_id) VALUES (?, ?)");
            st.setInt(1, newId);
            st.setInt(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }
    public boolean isAlreadyHere(int serverId, int userId) {
        Connection connection = Database.get().getConnection();
        boolean isAlreadyHere = false;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM serverperuser WHERE (server_id = ? AND user_id = ?)");

            st.setInt(1, serverId);
            st.setInt(2, userId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                isAlreadyHere = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAlreadyHere;
    }

    public int addUser(int serverId, int userId) {
        Connection connection = Database.get().getConnection();
        int newId = 0;
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO serverperuser (server_id, user_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, serverId);
            st.setInt(2, userId);

            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }
}
