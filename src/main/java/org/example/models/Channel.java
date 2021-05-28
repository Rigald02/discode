package org.example.models;

public class Channel {
    private int id;
    private String channelname;
    private int serverid;

    public Channel(int id, int serverid, String channelname) {
        this.id = id;
        this.channelname = channelname;
        this.serverid = serverid;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelname='" + channelname + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

}
