package Szakdoga.Model;

import java.util.List;

public class Entry {
    String broadcastLink;
    String date;
    String id;
    String info;
    String link;
    String location;
    List<String> membersIds;
    String name;
    String teamId;


    public Entry(String broadcastLink, String date, String id, String info, String link, String location, List<String> membersIds, String name, String teamId) {
        this.broadcastLink = broadcastLink;
        this.date = date;
        this.id = id;
        this.info = info;
        this.link = link;
        this.location = location;
        this.membersIds = membersIds;
        this.name = name;
        this.teamId = teamId;
    }

    public Entry() {
    }

    public String getBroadcastLink() {
        return broadcastLink;
    }

    public void setBroadcastLink(String broadcastLink) {
        this.broadcastLink = broadcastLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<String> membersIds) {
        this.membersIds = membersIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
