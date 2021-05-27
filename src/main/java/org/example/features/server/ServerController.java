package org.example.features.server;

import org.example.core.Database;
import org.example.core.Template;
import org.example.features.channel.ChannelDao;
import org.example.features.conversation.ConversationDao;
import org.example.features.friends.FriendController;
import org.example.features.friends.FriendDao;
import org.example.features.user.UserDao;
import org.example.models.User;
import org.example.utils.SessionUtils;
import org.example.utils.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ServerController {
    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    ChannelDao channelDao = new ChannelDao();
    ConversationDao conversationDao = new ConversationDao();
    ServerDao serverDao = new ServerDao();
    UserDao userDao = new UserDao();

    public String serverList(Request request, Response response) {
        int userId = SessionUtils.getSessionUserId(request);
        if (userId == 0) {
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("channelId", 0);
        model.put("servers", serverDao.getServersForUserId(userId));
        return Template.render("server_list.html", model);
    }

    public String createServer(Request request, Response response) {
        int userId = SessionUtils.getSessionUserId(request);

        Map<String, Object> model = new HashMap<>();
        model.put("conversations", conversationDao.getAllConversationsForUser(userId));

        if (request.requestMethod().equals("GET")) {
            model.put("message", "");
            return Template.render("server_create.html", model);
        }

        Map<String, String> query = URLUtils.decodeQuery(request.body());
        String servername = query.get("servername");


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
            model.put("message", "Serveur " + servername + " créé !");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Template.render("server_create.html", model);
    }
}
