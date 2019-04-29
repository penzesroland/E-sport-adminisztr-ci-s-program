package Szakdoga.Controller;

import Szakdoga.Model.Game;
import Szakdoga.Model.Player;
import Szakdoga.Model.PlayerGameDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateProfile implements Initializable {

    String token;

    JSONObject myProfil = new JSONObject();

    JSONArray gameDetails = new JSONArray();

    JSONArray teams = new JSONArray();

    @FXML
    private Button emailbtn;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField teamtxt;

    @FXML
    private TextField nicktxt;

    @FXML
    private TextField gametxt;

    @FXML
    private TextField gameroletxt;

    @FXML
    private TextField gameigntxt;

    @FXML
    private ChoiceBox gameteamcb;

    @FXML
    private ChoiceBox nickcb;

    @FXML
    private ChoiceBox teamcb;

    @FXML
    private ChoiceBox gamecb;

    @FXML
    private Button nickbtn;

    public ObservableList<String> game = FXCollections.observableArrayList();
    public ObservableList<String> team = FXCollections.observableArrayList();

    @FXML
    private void updateClick(ActionEvent event) throws IOException, JSONException {
    update(myProfil);
        Player findPlayer = new Player();
        List<PlayerGameDetail> games = new ArrayList<PlayerGameDetail>();
        List<Szakdoga.Model.Team> findteams = new ArrayList<Szakdoga.Model.Team>();
        List<Szakdoga.Model.Game> findgames = new ArrayList<Game>();
        findPlayer.setBirthday((String) myProfil.get("birthday"));
        findPlayer.setFirstName((String) myProfil.get("firstName"));
        findPlayer.setLastName((String) myProfil.get("lastName"));
        findPlayer.setEmail((String) myProfil.get("email"));
        findPlayer.setId((String) myProfil.get("id"));
        findPlayer.setUsername((String) myProfil.get("username"));
        findPlayer.setProfilepic((String) myProfil.get("profilePic"));
        JSONArray gameDetails = (JSONArray) myProfil.get("gameDetails");
        for(int i=0;i<gameDetails.length();i++){
            JSONObject gamedetail = new JSONObject();
            gamedetail = gameDetails.getJSONObject(i);
            PlayerGameDetail g = new PlayerGameDetail();
            Game gamedata = new Game();
            JSONObject game = gamedetail.getJSONObject("game");
            gamedata.setAltername((String)game.get("alterName"));
            gamedata.setId((String)game.get("id"));
            gamedata.setMiddleLeaderId((String)game.get("middleLeaderId"));
            gamedata.setName((String)game.get("name"));
            g.setGame(gamedata);
           // g.setId((String)gamedetail.get("id"));
            g.setIgn((String)gamedetail.get("ign"));
            g.setTeamId((String)gamedetail.get("teamId"));
            g.setTeamRole((String)gamedetail.get("teamRole"));
            games.add(g);
        }
        findPlayer.setGameDetails(games);
        JSONArray teams = (JSONArray) myProfil.get("teams");
        for(int i=0;i<teams.length();i++){
            JSONObject team = new JSONObject();
            team = teams.getJSONObject(i);
            Szakdoga.Model.Team t = new Szakdoga.Model.Team();
            t.setGameId((String)team.get("gameId"));
            t.setId((String)team.get("id"));
            t.setLeaderId((String)team.get("leaderId"));
            t.setName((String)team.get("name"));
            findteams.add(t);
        }
        findPlayer.setTeams(findteams);

        Stage stage = (Stage) gamecb.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/profile.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Profile>getController().initdata(token,findPlayer,myProfil,false);

        Scene scene = new Scene(root);

        stage.setTitle("Saját profil");
        stage.setScene(scene);
        stage.show();

    }

    public void update(JSONObject jsonObject){

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpPost postRequest = new HttpPost("/api/players/update");

            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);


            StringEntity se = new StringEntity(jsonObject.toString(),"UTF8");
            se.setContentType( "application/json");

            postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject json = new JSONObject(EntityUtils.toString(entity));

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }

    }

    @FXML
    private void backClick(ActionEvent event) throws IOException, JSONException{
        Player findPlayer = new Player();
        List<PlayerGameDetail> games = new ArrayList<PlayerGameDetail>();
        List<Szakdoga.Model.Team> findteams = new ArrayList<Szakdoga.Model.Team>();
        List<Szakdoga.Model.Game> findgames = new ArrayList<Game>();
        findPlayer.setBirthday((String) myProfil.get("birthday"));
        findPlayer.setFirstName((String) myProfil.get("firstName"));
        findPlayer.setLastName((String) myProfil.get("lastName"));
        findPlayer.setEmail((String) myProfil.get("email"));
        findPlayer.setId((String) myProfil.get("id"));
        findPlayer.setUsername((String) myProfil.get("username"));
        findPlayer.setProfilepic((String) myProfil.get("profilePic"));
        JSONArray gameDetails = (JSONArray) myProfil.get("gameDetails");
        for(int i=0;i<gameDetails.length();i++){
            JSONObject gamedetail = new JSONObject();
            gamedetail = gameDetails.getJSONObject(i);
            PlayerGameDetail g = new PlayerGameDetail();
            Game gamedata = new Game();
            JSONObject game = gamedetail.getJSONObject("game");
            gamedata.setAltername((String)game.get("alterName"));
            gamedata.setId((String)game.get("id"));
            gamedata.setMiddleLeaderId((String)game.get("middleLeaderId"));
            gamedata.setName((String)game.get("name"));
            g.setGame(gamedata);
            // g.setId((String)gamedetail.get("id"));
            g.setIgn((String)gamedetail.get("ign"));
            g.setTeamId((String)gamedetail.get("teamId"));
            g.setTeamRole((String)gamedetail.get("teamRole"));
            games.add(g);
        }
        findPlayer.setGameDetails(games);
        JSONArray teams = (JSONArray) myProfil.get("teams");
        for(int i=0;i<teams.length();i++){
            JSONObject team = new JSONObject();
            team = teams.getJSONObject(i);
            Szakdoga.Model.Team t = new Szakdoga.Model.Team();
            t.setGameId((String)team.get("gameId"));
            t.setId((String)team.get("id"));
            t.setLeaderId((String)team.get("leaderId"));
            t.setName((String)team.get("name"));
            findteams.add(t);
        }
        findPlayer.setTeams(findteams);

        Stage stage = (Stage) gamecb.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/profile.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Profile>getController().initdata(token,findPlayer,myProfil,false);

        Scene scene = new Scene(root);

        stage.setTitle("Saját profil");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void nickClick(ActionEvent event) throws IOException, JSONException {
        if(!nickcb.getValue().equals(null)) {
            for (int i = 0; i < gameDetails.length(); i++) {
                if (gameDetails.getJSONObject(i).getJSONObject("game").get("name").equals(nickcb.getValue())) {
                    gameDetails.getJSONObject(i).remove("ign");
                    gameDetails.getJSONObject(i).put("ign", nicktxt.getText());
                }
            }
        }
        nickcb.setValue(null);
    }

    @FXML
    private void delteamClick(ActionEvent event) throws IOException, JSONException {
        if(!teamcb.getValue().equals(null)) {
            for (int i = 0; i < teams.length(); i++) {
                if (teams.getJSONObject(i).get("name").equals((String) teamcb.getValue())) {
                    teams.remove(i);
                    team.remove(teamcb.getValue());
                }
            }
        }
        teamcb.setValue(null);
    }

    @FXML
    private void addteamClick(ActionEvent event) throws IOException, JSONException {
    JSONObject team = new JSONObject();
    if(!teamtxt.getText().isEmpty()) {
        team = searchTeam(teamtxt.getText());
        JSONArray array = team.getJSONArray("membersId");
        array.put(myProfil.get("id"));
        team.remove("membersId");
        team.put("membersId", array);
        teams.put(team);
        updateTeam(team);
    }
    }

    public void updateTeam(JSONObject team){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpPost postRequest = new HttpPost("/api/teams/update");

            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);


            StringEntity se = new StringEntity(team.toString(),"UTF8");
            se.setContentType( "application/json");

            postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject json = new JSONObject(EntityUtils.toString(entity));

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }


    }

    public JSONObject searchTeam(String name){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        JSONObject team = new JSONObject();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpPost postRequest = new HttpPost("/api/search/teams");

            json.put("teamName",name);

            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType( "application/json");

            postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject json1 = new JSONObject(EntityUtils.toString(entity));
            team = json1;
            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return team;
    }

    @FXML
    private void addgameClick(ActionEvent event) throws IOException, JSONException {
    JSONObject game = new JSONObject();
    JSONObject detail = new JSONObject();
    String team ="";
    if(!gameteamcb.getValue().equals(null) && !gameigntxt.getText().isEmpty() && !gametxt.getText().isEmpty()) {
        game = searchGame(gametxt.getText());
        detail.put("game", game);
        detail.put("ign", gameigntxt.getText());
        for (int i = 0; i < teams.length(); i++) {
            if (teams.getJSONObject(i).get("name").equals(gameteamcb.getValue())) {
                team = (String) teams.getJSONObject(i).get("id");
            }
        }
        detail.put("teamId", team);
        detail.put("teamRole", gameroletxt.getText());
        gameDetails.put(detail);
    }
    }

    public JSONObject searchGame(String name){

        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        JSONObject game = new JSONObject();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");


            HttpPost postRequest = new HttpPost("/api/search/games");

            json.put("gameName",name);

            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);


            StringEntity se = new StringEntity(json.toString());
            se.setContentType( "application/json");

            postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject findgame = new JSONObject(EntityUtils.toString(entity));
            game = findgame;
            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return game;
    }

    @FXML
    private void delgameClick(ActionEvent event) throws IOException, JSONException{
       if(!gamecb.getValue().equals(null)) {
           for (int i = 0; i < gameDetails.length(); i++) {
               if (gameDetails.getJSONObject(i).getJSONObject("game").get("name").equals(gamecb.getValue())) {
                   gameDetails.remove(i);
                   game.remove(gamecb.getValue());
               }
           }
       }
        gamecb.setValue(null);
    }

    @FXML
    private void emailClick(ActionEvent event) throws IOException {
        try {
            myProfil.remove("email");
            myProfil.put("email",emailtxt.getText());
            System.out.println(myProfil.toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initdata(String token, JSONObject myJson) throws JSONException {

        this.token = token;
        this.myProfil = myJson;

        gameDetails = (JSONArray) myProfil.get("gameDetails");
        for(int i=0;i<gameDetails.length();i++){
            game.add((String) gameDetails.getJSONObject(i).getJSONObject("game").get("name"));
        }
        teams = (JSONArray) myProfil.get("teams");

        for(int i=0;i<teams.length();i++){
            team.add((String) teams.getJSONObject(i).get("name"));
        }

        nickcb.setItems(game);
        gamecb.setItems(game);
        teamcb.setItems(team);
        gameteamcb.setItems(team);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
