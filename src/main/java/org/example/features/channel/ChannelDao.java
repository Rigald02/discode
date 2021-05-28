package org.example.features.channel;

import org.example.core.Database;
import org.example.features.conversation.ConvForUser;
import org.example.features.server.ServerDao;
import org.example.models.Channel;
import org.example.models.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChannelDao {
    public static void createChannel(String channelName, Integer serverId){
        Connection connection = Database.get().getConnection();
        int newId = 0;
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO discoding.channel (channelname, idserver) VALUES (?, ?)");

            st.setString(1, channelName);
            st.setInt(2, serverId);

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Channel mapToChannel(ResultSet rs) throws SQLException {
        int i = 1;
        return new Channel(
                rs.getInt(i++), // id
                rs.getInt(i++), //serverid
                rs.getString(i++) // channelname,
        );
    }
    public List<Channel> getChannelsForServerId(int serverId) {
        List<Channel> channels = new ArrayList<>();
        Connection connection = Database.get().getConnection();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT channel.* FROM channel WHERE channel.idserver = ?");

            st.setInt(1, serverId);


            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Channel channel = ChannelDao.mapToChannel(rs);
                channels.add(channel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    public Channel getChannelById(int channelId) {
        Channel chan = null;
        Connection connection = Database.get().getConnection();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT channel.* FROM channel WHERE id = ?");

            st.setInt(1, channelId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                chan = mapToChannel(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chan;
    }

}
