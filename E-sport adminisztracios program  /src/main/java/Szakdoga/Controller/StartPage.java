package Szakdoga.Controller;

import Szakdoga.Model.Game;
import Szakdoga.Model.Player;
import Szakdoga.Model.PlayerGameDetail;
import Szakdoga.Model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartPage implements Initializable {

    @FXML
    private Button search;

    @FXML
    private Button add;

    @FXML
    private Button entries;

    @FXML
    private Button profil;

    @FXML
    private Button news;

    @FXML
    private Button exit;

    public String token;

    public String username;

    public JSONObject myprofil;

    Player player = new Player();

    public void initdata(String token){

        this.token = token;
       // this.username = username;
        player = myPlayer();
        listallgame();
        listallteam();
        for(int i=0;i<player.getRole().size();i++){
            if(player.getRole().get(i).equals("ROLE_ADMIN") || player.getRole().get(i).equals("ROLE_PRO_LEADER") || player.getRole().get(i).equals("ROLE_MIDDLE_LEADER")){
                add.setVisible(true);
            }
        }
    }

    @FXML
    private void searchingClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) search.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/search.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Searching>getController().initdata(token);

        Scene scene = new Scene(root);

        stage.setTitle("Keresés");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void competitionClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) entries.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/allentries.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<AllEntries>getController().initdata(token,player);

        Scene scene = new Scene(root);

        stage.setTitle("Versenyek");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) add.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/addplayer.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<AddPlayer>getController().initdata(token);

        Scene scene = new Scene(root);

        stage.setTitle("Játékos hozzáadása");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void newsClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) news.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/news.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<News>getController().initdata(token);

        Scene scene = new Scene(root);

        stage.setTitle("Hírfolyam");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void profilClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) profil.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/profile.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Profile>getController().initdata(token,player,myprofil,false);

        Scene scene = new Scene(root);

        stage.setTitle("Saját profil");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void exitClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) exit.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("/View/login.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Bejelentkezés");
        stage.setScene(scene);
        stage.show();
    }


    public Player myPlayer(){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        Player findPlayer = new Player();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet postRequest = new HttpGet("/api/players/me");



            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("token",token);
            postRequest.addHeader("Authorization","Bearer " +token);

         //   StringEntity se = new StringEntity(json.toString());
          //  se.setContentType( "application/json");

           // postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            List<PlayerGameDetail> games = new ArrayList<PlayerGameDetail>();
            List<Team> findteams = new ArrayList<Team>();
            List<Game> findgames = new ArrayList<Game>();
            List<String> findroles = new ArrayList<String>();
            JSONObject player = new JSONObject(EntityUtils.toString(entity));
            findPlayer.setBirthday((String) player.get("birthday"));
            findPlayer.setFirstName((String) player.get("firstName"));
            findPlayer.setLastName((String)player.get("lastName"));
            findPlayer.setEmail((String) player.get("email"));
            findPlayer.setId((String) player.get("id"));
            findPlayer.setUsername((String) player.get("username"));
            findPlayer.setProfilepic((String) player.get("profilePic"));
            JSONArray gameDetails = (JSONArray) player.get("gameDetails");
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
//                g.setId((String)gamedetail.get("id"));
                g.setIgn((String)gamedetail.get("ign"));
                g.setTeamId((String)gamedetail.get("teamId"));
                g.setTeamRole((String)gamedetail.get("teamRole"));
                games.add(g);
            }
            findPlayer.setGameDetails(games);

            JSONArray teams = (JSONArray) player.get("teams");
            for(int i=0;i<teams.length();i++){
                JSONObject team = new JSONObject();
                team = teams.getJSONObject(i);
                Team t = new Team();
                t.setGameId((String)team.get("gameId"));
                t.setId((String)team.get("id"));
                JSONArray jsonArray = team.getJSONArray("membersId");
                List<String> mid = new ArrayList<String>();
                for(int j=0;j<jsonArray.length();j++){
                    mid.add((String)jsonArray.get(j));
                }
                t.setMemberIds(mid);
                t.setLeaderId((String)team.get("leaderId"));
                t.setName((String)team.get("name"));
                findteams.add(t);
            }
            findPlayer.setTeams(findteams);

            myprofil = player;
         JSONArray roles = (JSONArray) player.get("roles");
            for(int i=0;i<roles.length();i++) {
                JSONObject role = new JSONObject();
                role = roles.getJSONObject(i);
                System.out.println(role.toString());
                findroles.add((String)role.get("roleName"));
            }
            findPlayer.setRole(findroles);

            System.out.println(player.toString());


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
        return findPlayer;
    }

    public void listallteam() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com", 80, "http");

            HttpGet getRequest = new HttpGet("/api/teams/listAll");

            getRequest.addHeader("accept", "*/*");
            getRequest.addHeader("Authorization", "Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            System.out.println(EntityUtils.toString(entity));

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


    public void listallgame() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com", 80, "http");

            HttpGet getRequest = new HttpGet("/api/games/listAll");

            getRequest.addHeader("accept", "*/*");
            getRequest.addHeader("Authorization", "Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            System.out.println(EntityUtils.toString(entity));

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

            @Override
            public void initialize(URL location, ResourceBundle resources) {
                add.setVisible(false);
            }
        }
