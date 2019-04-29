package Szakdoga.Controller;

import Szakdoga.Model.Game;
import Szakdoga.Model.Player;
import Szakdoga.Model.PlayerGameDetail;
import Szakdoga.Model.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
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
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Searching implements Initializable {
    @FXML
    private ChoiceBox type;

    @FXML
    private ListView result;

    @FXML
    private TextField search;

    public String token;

    public  Game game = new Game();

    public  Player player = new Player();

    public  Team team = new Team();

    public void initdata(String token){
        this.token = token;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items = FXCollections.observableArrayList("Játékos","Csapat","Játék");
        type.setItems(items);
    }

    @FXML
    public void searchClick(ActionEvent actionEvent) throws IOException {
        if(type.getValue().equals("Játékos")){




            player = searchPlayer(search.getText());

            ObservableList list = FXCollections.observableArrayList(player.getUsername());

            result.setItems(list);

            /*

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/profile.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<Profile>getController().initdata(token,player,myJson);

            Scene scene = new Scene(root);

            stage.setTitle("Játékos");
            stage.setScene(scene);
            stage.show();*/
        }else if(type.getValue().equals("Csapat")){

            team = searchTeam(search.getText());

            ObservableList list = FXCollections.observableArrayList(team.getName());

            result.setItems(list);

            /*
            Stage stage = (Stage) type.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/team.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<Szakdoga.Controller.Team>getController().initdata(token,team);

            Scene scene = new Scene(root);

            stage.setTitle("Csapat");
            stage.setScene(scene);
            stage.show();*/
        }else if(type.getValue().equals("Játék")){
            game = searchGame(search.getText());

            ObservableList list = FXCollections.observableArrayList(game.getName());

            result.setItems(list);

            /*
            Stage stage = (Stage) type.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/game.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<Szakdoga.Controller.Game>getController().initdata(token,game);

            Scene scene = new Scene(root);

            stage.setTitle("Játék");
            stage.setScene(scene);
            stage.show();*/
        }
    }

    @FXML
    private void detailsClick(ActionEvent event) throws IOException {
        String selected = result.getSelectionModel().getSelectedItem().toString();

       System.out.println(selected);
       JSONObject myJson = new JSONObject();
       if(type.getValue().equals("Játékos") && selected.equals(player.getUsername())){
           Stage stage = (Stage) type.getScene().getWindow();

           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/profile.fxml"));

           Parent root = fxmlLoader.load();

           fxmlLoader.<Profile>getController().initdata(token,player,myJson,true);

           Scene scene = new Scene(root);

           stage.setTitle("Játékos");
           stage.setScene(scene);
           stage.show();

       }else if(type.getValue().equals("Csapat") && selected.equals(team.getName())){
           Stage stage = (Stage) type.getScene().getWindow();

           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/team.fxml"));

           Parent root = fxmlLoader.load();

           fxmlLoader.<Szakdoga.Controller.Team>getController().initdata(token,team);

           Scene scene = new Scene(root);

           stage.setTitle("Csapat");
           stage.setScene(scene);
           stage.show();
       }else if(type.getValue().equals("Játék") && selected.equals(game.getName())){
           Stage stage = (Stage) type.getScene().getWindow();

           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/game.fxml"));

           Parent root = fxmlLoader.load();

           fxmlLoader.<Szakdoga.Controller.Game>getController().initdata(token,game);

           Scene scene = new Scene(root);

           stage.setTitle("Játék");
           stage.setScene(scene);
           stage.show();
       }


    }

    @FXML
    private void backClick(ActionEvent event) throws IOException{

        Stage stage = (Stage) type.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/startpage.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<StartPage>getController().initdata(token);

        Scene scene = new Scene(root);

        stage.setTitle("Kezdőlap");
        stage.setScene(scene);
        stage.show();
    }

    public Game searchGame(String name){
        Game findgame = new Game();
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

    public Player searchPlayer(String name){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        Szakdoga.Model.Player findPlayer = new Szakdoga.Model.Player();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpPost postRequest = new HttpPost("/api/search/players");

            json.put("username",name);

            postRequest.addHeader("accept","*/*");
            postRequest.addHeader("Authorization","Bearer " + token);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType( "application/json");

            postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            List<PlayerGameDetail> games = new ArrayList<PlayerGameDetail>();
            List<Szakdoga.Model.Team> findteams = new ArrayList<Szakdoga.Model.Team>();
            List<Szakdoga.Model.Game> findgames = new ArrayList<Game>();
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
             //   g.setId((String)gamedetail.get("id"));
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
                Szakdoga.Model.Team t = new Szakdoga.Model.Team();
                t.setGameId((String)team.get("gameId"));
                t.setId((String)team.get("id"));
                t.setLeaderId((String)team.get("leaderId"));
                t.setName((String)team.get("name"));
                findteams.add(t);
            }
            findPlayer.setTeams(findteams);

            //myJson = player;


            JSONArray roles = (JSONArray) player.get("roles");
            for(int i=0;i<roles.length();i++) {
                JSONObject role = new JSONObject();
                role = roles.getJSONObject(i);
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

}
