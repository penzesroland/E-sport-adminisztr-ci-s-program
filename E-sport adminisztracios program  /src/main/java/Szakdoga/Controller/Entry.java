package Szakdoga.Controller;

import Szakdoga.Model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Entry implements Initializable {

    @FXML
    private Label namelbl;

    @FXML
    private Label gamelbl;

    @FXML
    private Label loclbl;

    @FXML
    private Label infolbl;

    @FXML
    private Label linklbl;

    @FXML
    private Label brlinklbl;

    @FXML
    private Label teamlbl;

    @FXML
    private Label datelbl;

    @FXML
    private ListView teamlv;


    String token;

    Szakdoga.Model.Entry chosenEntry;

    Szakdoga.Model.Player player;

    public void initdata(String token, Szakdoga.Model.Entry entry, Player player) throws JSONException {

        this.token = token;
        this.chosenEntry = entry;
        this.player = player;

        namelbl.setText(chosenEntry.getName());
        loclbl.setText(chosenEntry.getLocation());
        infolbl.setText(chosenEntry.getInfo());
        datelbl.setText(chosenEntry.getDate());
        linklbl.setText(chosenEntry.getLink());
        brlinklbl.setText(chosenEntry.getBroadcastLink());
        ObservableList<String> names = FXCollections.observableArrayList();

        for(int i=0;i<chosenEntry.getMembersIds().size();i++){
            String id = chosenEntry.getMembersIds().get(i);
            names.add(getName(id));
        }

        teamlv.setEditable(false);
        teamlv.setItems(names);
        JSONObject jsonObject = new JSONObject();
        jsonObject = getTeamName(chosenEntry.getTeamId());
        teamlbl.setText((String) jsonObject.get("name"));
        gamelbl.setText(getGameName((String) jsonObject.get("gameId")));
    }

    @FXML
    public void backClick(ActionEvent event) throws IOException, JSONException {

        Stage stage = (Stage) teamlbl.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/allentries.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Szakdoga.Controller.AllEntries>getController().initdata(token,player);

        Scene scene = new Scene(root);

        stage.setTitle("Versenyek");
        stage.setScene(scene);
        stage.show();

    }

    public String getGameName(String gameId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String gamename = "";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/admin/games/" + gameId);

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject game = new JSONObject(EntityUtils.toString(entity));
            gamename = (String) game.get("name");

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
        return gamename;
    }

    public JSONObject getTeamName(String teamId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String name = "";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/admin/teams/" + teamId);

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject team = new JSONObject(EntityUtils.toString(entity));
            name = (String) team.get("name");
            json.put("name",name);
            name = (String) team.get("gameId");
            json.put("gameId",name);
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

    public String getName(String playerId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String name = "";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/players/" + playerId);

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);


            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject game = new JSONObject(EntityUtils.toString(entity));
            name = (String) game.get("lastName") + (String) game.get("firstName");

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
        return name;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
