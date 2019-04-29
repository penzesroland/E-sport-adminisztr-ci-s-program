package Szakdoga.Controller;

import Szakdoga.Model.Entry;
import Szakdoga.Model.Player;
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
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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

public class AllEntries implements Initializable {

    @FXML
    private ListView entrieslv;

    @FXML
    private ChoiceBox entrycb;


    Player me;

    String token;

    List<Szakdoga.Model.Entry> entries = new ArrayList<Entry>();

    public void initdata(String token, Player player){
        this.token = token;
        this.me = player;

        entriesList(me.getId());

        ObservableList<String> entriesname = FXCollections.observableArrayList();

        for(int i=0;i<entries.size();i++){
            entriesname.add(entries.get(i).getName());
        }

        entrieslv.setItems(entriesname);
        entrycb.setItems(entriesname);
    }

    @FXML
    public void entrychooseClick(ActionEvent event) throws IOException, JSONException {
        Entry chosenEntry = new Entry();

        for(int i=0;i<entries.size();i++){
           if( entries.get(i).getName().equals((String)entrycb.getValue())){
               chosenEntry = entries.get(i);
           }
        }

        Stage stage = (Stage) entrycb.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/entry.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Szakdoga.Controller.Entry>getController().initdata(token,chosenEntry,me);

        Scene scene = new Scene(root);

        stage.setTitle("Verseny részletek");
        stage.setScene(scene);
        stage.show();



    }

    public void entriesList(String playerId){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String gamename = "";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            HttpGet getRequest = new HttpGet("/api/entries/" + playerId);

            getRequest.addHeader("accept","*/*");
            getRequest.addHeader("Authorization","Bearer " + token);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            JSONArray entriesList = new JSONArray(EntityUtils.toString(entity));
            for(int i=0;i<entriesList.length();i++){
                Entry e = new Entry();
                JSONObject entry = entriesList.getJSONObject(i);
                e.setBroadcastLink((String) entry.get("broadcastLink"));
                e.setDate((String) entry.get("date"));
                e.setId((String) entry.get("id"));
                e.setInfo((String) entry.get("info"));
                e.setLink((String) entry.get("link"));
                e.setLocation((String) entry.get("location"));
                List<String> members = new ArrayList<String>();
                JSONArray m = new JSONArray(entry.get("memberIds"));
                for(int j=0;j<m.length();j++){
                    members.add((String) m.get(i));
                }
                e.setMembersIds(members);
                e.setName((String) entry.get("name"));
                e.setTeamId((String) entry.get("teamId"));
                entries.add(e);
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
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
