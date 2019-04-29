package Szakdoga.Controller;

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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class News implements Initializable {

    @FXML
    private ListView allnewslv;

    @FXML
    private Button choosebtn;

    @FXML
    private ChoiceBox choosecb;

    String token;

    List<Szakdoga.Model.News> myAllNews = new ArrayList<Szakdoga.Model.News>();

    public void initdata(String token){
        this.token = token;
        myAllNews = listAllNews();
        ObservableList<String> titles = FXCollections.observableArrayList();
        for(int i=0; i<myAllNews.size();i++){
            titles.add(myAllNews.get(i).getTitle());
        }
        allnewslv.setItems(titles);
        choosecb.setItems(titles);
    }

    @FXML
    private void searchTitleClick(ActionEvent event) throws IOException {
        Szakdoga.Model.News news = new Szakdoga.Model.News();
        if(!choosecb.getValue().equals(null)) {
            for (int i = 0; i < myAllNews.size(); i++) {
                if (myAllNews.get(i).getTitle().equals(choosecb.getValue())) {
                    news = myAllNews.get(i);
                }
            }

            Stage stage = (Stage) choosebtn.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/newsdelete.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<Szakdoga.Controller.NewsDelete>getController().initdata(token, news);

            Scene scene = new Scene(root);

            stage.setTitle("Hír kezelése");
            stage.setScene(scene);
            stage.show();
        }
    }

    public List<Szakdoga.Model.News> listAllNews(){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        List<Szakdoga.Model.News> allNews = new ArrayList<Szakdoga.Model.News>();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/news/listMyNews");

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONArray news = new JSONArray(EntityUtils.toString(entity));
            System.out.println(news.toString());
            for(int i=0;i<news.length();i++) {
                JSONObject one_news = news.getJSONObject(i);
                Szakdoga.Model.News findnews = new Szakdoga.Model.News();
                findnews.setAuthorID((String) one_news.get("authorId"));
                findnews.setContent((String) one_news.get("content"));
                findnews.setCreated((String) one_news.get("created"));
                if(!one_news.get("gameId").equals(null)) {
                    findnews.setGameId((String) one_news.get("gameId"));
                }
                findnews.setId((String) one_news.get("id"));
                findnews.setType((String) one_news.get("newsType"));
                JSONArray urls = (JSONArray) one_news.get("pictureUrls");
                List<String> purls = new ArrayList<String>();
                for (int j = 0; j < urls.length(); j++) {
                    purls.add((String) urls.get(j));
                }
                findnews.setPictureUrls(purls);
                if(!one_news.get("teamId").equals(null)) {
                    findnews.setTeamId((String) one_news.get("teamId"));
                }
                findnews.setTitle((String) one_news.get("title"));
                allNews.add(findnews);
            }

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
        return allNews;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
