package org.example.features.channel;

import org.example.core.Template;
import org.example.features.conversation.ConvForUser;
import org.example.features.conversation.ConversationController;
import org.example.features.conversation.ConversationDao;
import org.example.features.messages.MessageDao;
import org.example.features.user.UserDao;
import org.example.models.Channel;
import org.example.models.Message;
import org.example.utils.SessionUtils;
import org.example.utils.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    final ChannelDao channelDao = new ChannelDao();
    final MessageDao messageDao = new MessageDao();
    final UserDao userDao = new UserDao();

    public String addMessage(Request request, Response response) {
        int channelId = Integer.parseInt(request.params(":id"));
        int userId = SessionUtils.getSessionUserId(request);

        Map<String, String> query = URLUtils.decodeQuery(request.body());
        String content = query.get("content");

        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        Message message = new Message(0, channelId, userId, content, now.toString());
        messageDao.createMessageChannel(message);


        response.redirect("/channel/" + channelId);
        return null;
    }

    public String detail(Request request, Response response) {
        int channelId = Integer.parseInt(request.params(":id"));
        List<Message> messages = messageDao.getMessagesForChannelId(channelId);
        Channel channel = channelDao.getChannelById(channelId);
        Map<String, Object> model = new HashMap<>();
        model.put("channelId", channelId);
        model.put("channels", channelDao.getChannelsForServerId(channel.getServerid()));
        model.put("channel", channel);
        model.put("messages", messages);
        return Template.render("channel_detail.html", model);
    }
}
