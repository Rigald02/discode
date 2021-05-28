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

        int serverId = serverDao.createServer(servername, userId);
        channelDao.createChannel("Général", serverId);
        channelDao.createChannel("Random", serverId);
        channelDao.createChannel("Music", serverId);
        channelDao.createChannel("Games", serverId);
        channelDao.createChannel("Help", serverId);


        model.put("message", "Serveur " + servername + " créé !");
        return Template.render("server_create.html", model);
    }

    public Object addUser(Request req, Response res) {
        int userId = SessionUtils.getSessionUserId(req);
        int serverId = Integer.parseInt(req.params(":id"));

        Map<String, Object> model = new HashMap<>();
        //model.put("conversations", conversationDao.getAllConversationsForUser(userId));

        if (req.requestMethod().equals("GET")) {
            model.put("message", "");
            return Template.render("invite_user.html", model);
        }

        Map<String, String> query = URLUtils.decodeQuery(req.body());
        String username = query.get("username");

        User newUser = userDao.findUserWithUsername(username);
        logger.info("User = " + newUser);
        if (newUser != null){
            if (serverDao.isAlreadyHere(serverId, newUser.getId())) {
                model.put("message", newUser.getUsername() + " est déja présent !");
            } else {
                serverDao.addUser(serverId, newUser.getId());
                model.put("message", newUser.getUsername() + " a été ajouté dans la conversation !");
            }
        }else {
            model.put("message", "Désolé, " + userDao.findUserWithUsername(username) + " n'existe pas.");
        }

        return Template.render("invite_user.html", model);
    }
}
