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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @FXML
    private CheckBox admincb;

    @FXML
    private CheckBox szakmaicb;

    @FXML
    private CheckBox kozepcb;

    @FXML
    private CheckBox csapatcb;

    @FXML
    private CheckBox playercb;


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
    public void addClick(ActionEvent event) throws IOException, JSONException {
    List<String> rolesname = new ArrayList<String>();
    List<String> rolesid = new ArrayList<String>() ;
    JSONArray json = new JSONArray();
    json = roles();
    System.out.println(json.toString());
    player.setUsername(usernametxt.getText());
    player.setEmail(emailtxt.getText());
    player.setFirstName(firstnametxt.getText());
    player.setLastName(lastnametxt.getText());
    player.setTeams(teams);
    player.setGameDetails(gameDetails);
    LocalDate date = birthday.getValue();
    player.setBirthday(date.toString());
    if(playercb.isSelected()){
       rolesname.add((String)json.getJSONObject(0).get("roleName"));
       rolesid.add((String)json.getJSONObject(0).get("id"));
    }
    if(csapatcb.isSelected()){
        rolesname.add((String)json.getJSONObject(1).get("roleName"));
        rolesid.add((String)json.getJSONObject(1).get("id"));
    }
    if(kozepcb.isSelected()){
        rolesname.add((String)json.getJSONObject(2).get("roleName"));
        rolesid.add((String)json.getJSONObject(2).get("id"));
    }
    if(szakmaicb.isSelected()){
        rolesname.add((String)json.getJSONObject(3).get("roleName"));
        rolesid.add((String)json.getJSONObject(3).get("id"));
    }
    if(admincb.isSelected()){
        rolesname.add((String)json.getJSONObject(4).get("roleName"));
        rolesid.add((String)json.getJSONObject(4).get("id"));
    }
    player.setRole(rolesname);
    player.setGameDetails(gameDetails);
    player.setTeams(teams);

    addPlayer(player,rolesid);



    }

    public void addPlayer(Szakdoga.Model.Player player,List<String> ids ){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpPost postRequest = new HttpPost("/api/admin/players/add");

            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);
            JSONArray teams = new JSONArray();
            JSONArray members = new JSONArray();
            JSONObject team = new JSONObject();
            for(int i=0;i<player.getTeams().size();i++){
                team.put("gameId",player.getTeams().get(i).getGameId());
                team.put("id",player.getTeams().get(i).getId());
                team.put("leaderId",player.getTeams().get(i).getLeaderId());
                team.put("name",player.getTeams().get(i).getName());
                for(int j=0;j<player.getTeams().get(i).getMemberIds().size();j++){
                    members.put(j,player.getTeams().get(i).getMemberIds().get(j));
                }
                members.put(members.length(),player.getId());
                teams.put(i,team);
            }

            JSONArray games = new JSONArray();
            JSONObject game = new JSONObject();
            JSONObject detail = new JSONObject();
            for(int i=0;i<player.getGameDetails().size();i++){
                game.put("alterName",player.getGameDetails().get(i).getGame().getAltername());
                game.put("id",player.getGameDetails().get(i).getGame().getId());
                game.put("middleLeaderId",player.getGameDetails().get(i).getGame().getMiddleLeaderId());
                game.put("name",player.getGameDetails().get(i).getGame().getName());
                detail.put("game",game);
                detail.put("ign",player.getGameDetails().get(i).getIgn());
                detail.put("teamId",player.getGameDetails().get(i).getTeamId());
                detail.put("teamRole",player.getGameDetails().get(i).getTeamRole());
                games.put(i,detail);
            }

            JSONArray roles = new JSONArray();
            JSONObject role = new JSONObject();
            for(int i=0;i<player.getRole().size();i++){
                role.put("roleName",player.getRole().get(i));
                role.put("id",ids.get(i));
                roles.put(i,role);
            }
            jsonObject.put("birthday",player.getBirthday());
            jsonObject.put("email",player.getEmail());
            jsonObject.put("firstName",player.getFirstName());
            jsonObject.put("gameDetails",games);
            jsonObject.put("lastName",player.getLastName());
            jsonObject.put("roles",roles);
            jsonObject.put("teams",teams);
            jsonObject.put("username",player.getUsername());
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

    public JSONArray roles(){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONArray json = null;
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");


            HttpGet postRequest = new HttpGet("/api/admin/roles");
            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            json = new JSONArray(EntityUtils.toString(entity));
            System.out.println(json);
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
        return json;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
