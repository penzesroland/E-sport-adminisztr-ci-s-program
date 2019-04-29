package Szakdoga.Model;

public class PlayerGameDetail {

    Game game;
    String ign;
    String teamId;
    String teamRole;

    public PlayerGameDetail(Game game, String ign, String teamId, String teamRole) {
        this.game = game;
        this.ign = ign;
        this.teamId = teamId;
        this.teamRole = teamRole;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }

    public PlayerGameDetail() {
    }
}
