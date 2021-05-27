package org.example.features.server;

import org.example.core.Database;
import org.example.core.Template;
import org.example.features.conversation.ConvForUser;
import org.example.features.conversation.ConversationDao;
import org.example.models.Server;
import org.example.models.User;
import org.example.utils.SessionUtils;
import spark.Request;
import spark.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
