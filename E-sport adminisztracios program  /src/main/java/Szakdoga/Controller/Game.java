package Szakdoga.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class Game implements Initializable {

    @FXML
    private Label gamenamelbl;

    @FXML
    private Label mleaderlbl;

    @FXML
    private Label alterlbl;

    public Szakdoga.Model.Game findGame;

    String token;

    public void initdata(String token, Szakdoga.Model.Game game){
        this.token = token;
        this.findGame = game;
       gamenamelbl.setText(findGame.getName());
        alterlbl.setText(findGame.getAltername());
        mleaderlbl.setText(getLeaderName(findGame.getMiddleLeaderId()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getLeaderName(String playerId){
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
            name = (String) game.get("firstName") + (String) game.get("lastName");

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
}
