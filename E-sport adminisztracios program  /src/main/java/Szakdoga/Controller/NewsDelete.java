package Szakdoga.Controller;

import Szakdoga.Model.News;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewsDelete implements Initializable {

    @FXML
    private TextArea content;

    @FXML
    private Label gamelbl;

    @FXML
    private Label teamlbl;

    @FXML
    private Label authorlbl;

    @FXML
    private Label createdlbl;

    @FXML
    private Label typelbl;

    @FXML
    private Label titlelbl;

    @FXML
    private Button visszabtn;

    @FXML
    private Button torolbtn;

    public News news;

    public String token;

public void initdata(String token, News news){
    this.token = token;
    this.news = news;
    if(!news.getAuthorID().isEmpty()){
        authorlbl.setText(getAuthorName(news.getAuthorID()));
    }else{
        authorlbl.setText("Nincs szerző");
    }
    if(!news.getGameId().isEmpty()){
        gamelbl.setText(getGameName(news.getGameId()));
    }else{
        gamelbl.setText("A hírhez nem kapcsolódik játék!");
    }
    if(!news.getTeamId().isEmpty()){
        teamlbl.setText(getTeamName(news.getTeamId()));
    }else{
        teamlbl.setText("Nincs csapat rendelve a hírhez!");
    }
    createdlbl.setText(news.getCreated());
    typelbl.setText(news.getType());
    titlelbl.setText(news.getTitle());
    content.setText(news.getContent());
}

    @FXML
    private void deleteClick(ActionEvent event) throws IOException {
    deleteNews(news.getId());
        Stage stage = (Stage) torolbtn.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/news.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Szakdoga.Controller.News>getController().initdata(token);

        Scene scene = new Scene(root);

        stage.setTitle("Hírfolyam");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void visszaClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) visszabtn.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/news.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Szakdoga.Controller.News>getController().initdata(token);

        Scene scene = new Scene(root);

        stage.setTitle("Hírfolyam");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteNews(String newsId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String name = "";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/players/" + newsId);

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();

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

    public String getAuthorName(String playerId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
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
            name = (String) game.get("firstName") + " " + (String) game.get("lastName");

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

    public String getTeamName(String teamId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String teamname = "";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/admin/team/" + teamId);

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONObject game = new JSONObject(EntityUtils.toString(entity));
            teamname = (String) game.get("name");

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
        return teamname;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
