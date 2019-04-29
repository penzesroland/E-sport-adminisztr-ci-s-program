package Szakdoga.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Team implements Initializable {

    @FXML
    private Label gamenamelbl;

    @FXML
    private Label leaderlbl;

    @FXML
    private Label teamnamelbl;

    @FXML
    private ListView memberslv;

    public Szakdoga.Model.Team findTeam;

    String token;

    public void initdata(String token, Szakdoga.Model.Team team){
        this.token = token;
        this.findTeam = team;
        teamnamelbl.setText(findTeam.getName());
        gamenamelbl.setText(getGameName(findTeam.getGameId()));
        leaderlbl.setText(getLeaderName(findTeam.getLeaderId()));

        ObservableList list = FXCollections.observableArrayList();

        for(int i=0;i<findTeam.getMemberIds().size();i++){
            list.add(getLeaderName(findTeam.getMemberIds().get(i)));
        }
        memberslv.setItems(list);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

    public String getLeaderName(String playerId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String gamename = "";
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
            gamename = (String) game.get("lastName" ) + " " + game.get("firstName");

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
}
