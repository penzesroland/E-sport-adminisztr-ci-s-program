package Szakdoga.Controller;

import Szakdoga.Model.Game;
import Szakdoga.Model.Player;
import Szakdoga.Model.PlayerGameDetail;
import Szakdoga.Model.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class AddPlayer implements Initializable {

    @FXML
    private TextField firstnametxt;

    @FXML
    private TextField lastnametxt;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField usernametxt;

    @FXML
    private TextField gamenametxt;

    @FXML
    private TextField igntxt;

    @FXML
    private TextField teamroletxt;

    @FXML
    private TextField teamtxt;

    @FXML
    private ListView gameslv;

    @FXML
    private DatePicker birthday;

    ObservableList gamelist = FXCollections.observableArrayList();

    List<PlayerGameDetail> gameDetails = new ArrayList<PlayerGameDetail>();

    Player player = new Player();

    List<Team> teams = new ArrayList<Team>();

    String token;

    public void initdata(String token){
        this.token = token;

    }

    @FXML
    public void addGameClick(ActionEvent event) throws IOException {

        PlayerGameDetail playerGameDetail = new PlayerGameDetail();
        Game game = new Game();
        Team team = new Team();

        playerGameDetail.setTeamRole(teamroletxt.getText());
        playerGameDetail.setIgn(igntxt.getText());

        game = searchGame(gamenametxt.getText());
        team = searchTeam(teamtxt.getText());

        playerGameDetail.setTeamId(team.getId());
        playerGameDetail.setGame(game);

        teams.add(team);
        gameDetails.add(playerGameDetail);

        gamelist.add(game.getName());
        gameslv.setItems(gamelist);
    }

    @FXML
    public void addClick(ActionEvent event) throws IOException {
    player.setUsername(usernametxt.getText());
    player.setEmail(emailtxt.getText());
    player.setFirstName(firstnametxt.getText());
    player.setLastName(lastnametxt.getText());
    player.setTeams(teams);
    player.setGameDetails(gameDetails);
   // player.setBirthday((String) birthday.);
    }

    public Team searchTeam(String name){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        Szakdoga.Model.Team findteam = new Szakdoga.Model.Team();
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
            JSONObject team = new JSONObject(EntityUtils.toString(entity));
            findteam.setGameId((String)team.get("gameId"));
            findteam.setId((String)team.get("id"));
            JSONArray jsonArray = team.getJSONArray("membersId");
            List<String> mid = new ArrayList<String>();
            for(int i=0;i<jsonArray.length();i++){
                mid.add((String)jsonArray.get(i));
            }
            findteam.setMemberIds(mid);
            findteam.setLeaderId((String)team.get("leaderId"));
            findteam.setName((String)team.get("name"));

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
        return findteam;
    }

    public Szakdoga.Model.Game searchGame(String name){
        Szakdoga.Model.Game findgame = new Game();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
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
            JSONObject game = new JSONObject(EntityUtils.toString(entity));
            findgame.setAltername((String)game.get("alterName"));
            findgame.setId((String)game.get("id"));
            findgame.setMiddleLeaderId((String)game.get("middleLeaderId"));
            findgame.setName((String)game.get("name"));
            System.out.println(game.toString());

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
        return findgame;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
