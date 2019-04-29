package Szakdoga.Model;

import org.apache.commons.codec.binary.StringUtils;

import java.util.List;

public class News {
    String authorID;
    String content;
    String created;
    String gameId;
    String id;
   // public enum types {GLOBAL, GAME, TEAM, TEAM_LEADERS, MIDDLE_LEADERS, PRO_LEADERS };
    String type;
    List<String> pictureUrls;
    String teamId;
    String title;

    public News(String authorID, String content, String created, String gameId, String id, String type, List<String> pictureUrls, String teamId, String title) {
        this.authorID = authorID;
        this.content = content;
        this.created = created;
        this.gameId = gameId;
        this.id = id;
        this.type = type;
        this.pictureUrls = pictureUrls;
        this.teamId = teamId;
        this.title = title;
    }

    public News() {
        this.authorID = "";
        this.gameId = "";
        this.teamId = "";
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
