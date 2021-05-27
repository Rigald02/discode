package org.example.features.server;

import org.example.core.Template;
import org.example.features.conversation.ConversationDao;
import org.example.features.friends.FriendController;
import org.example.features.friends.FriendDao;
import org.example.features.user.UserDao;
import org.example.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ServerController {
    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    ConversationDao conversationDao = new ConversationDao();
    FriendDao friendDao = new FriendDao();
    UserDao userDao = new UserDao();

    public String serverList(Request request, Response response) {
        int userId = SessionUtils.getSessionUserId(request);
        if (userId == 0) {
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("conversationId", 0);
        model.put("conversations", conversationDao.getAllConversationsForUser(userId));
        model.put("servers", friendDao.getFriendsForUserId(userId));
        return Template.render("server_list.html", model);
    }
}
