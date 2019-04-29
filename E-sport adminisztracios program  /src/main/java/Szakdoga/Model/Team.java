package Szakdoga.Model;

import java.util.List;

public class Team {
    String gameId;
    String id;
    String leaderId;
    List<String> memberIds;
    String name;

    public Team(String gameId, String id, String leaderId, List<String> memberIds, String name) {
        this.gameId = gameId;
        this.id = id;
        this.leaderId = leaderId;
        this.memberIds = memberIds;
        this.name = name;
    }

    public Team() {
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

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
