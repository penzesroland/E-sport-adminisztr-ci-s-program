package Szakdoga.Model;

import java.util.List;

public class Player {
    String birthday;
    String email;
    String firstName;
    List<PlayerGameDetail> gameDetails;
    String id;
    String profilepic;
    String lastName;
    List<String> role;
    List<Team> teams;
    String username;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Player(String birthday, String email, String firstName, List<PlayerGameDetail> gameDetails, String id, String profilepic, String lastName, List<String> role, List<Team> teams, String username) {
        this.birthday = birthday;
        this.email = email;
        this.firstName = firstName;
        this.gameDetails = gameDetails;
        this.id = id;
        this.profilepic = profilepic;
        this.lastName = lastName;
        this.role = role;
        this.teams = teams;
        this.username = username;
    }

    public Player() {
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<PlayerGameDetail> getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(List<PlayerGameDetail> gameDetails) {
        this.gameDetails = gameDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
