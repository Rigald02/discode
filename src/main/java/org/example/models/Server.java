package org.example.models;

public class Server {
    private int id;
    private String servername;
    private String avatarUrl;

    public Server(int id, String servername, String avatarUrl) {
        this.id = id;
        this.servername = servername;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", servername='" + servername + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
