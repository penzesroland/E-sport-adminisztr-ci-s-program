package Szakdoga.Controller;

import Szakdoga.Model.Entry;
import Szakdoga.Model.Game;
import Szakdoga.Model.Player;
import Szakdoga.Model.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import org.json.JSONException;
import org.json.JSONObject;
import sun.awt.image.URLImageSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class Profile implements Initializable {
    @FXML
    private Label nevlbl;

    @FXML
    private Label szullbl;

    @FXML
    private Label nicknamelbl;

    @FXML
    private Label emaillbl;

    @FXML
    private ListView game;

    @FXML
    private ListView ign;

    @FXML
    private ListView team;

    @FXML
    private Button updatebtn;

    @FXML
    private Button back;

    @FXML
    private ImageView profilpic;

    public String token;

    public List<Team> teams = new ArrayList<Team>();

    public Player me = new Player();

    public JSONObject myJson;

    public boolean keres;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updatebtn.setVisible(true);
    }

    @FXML
    private void updateClick(ActionEvent event) throws IOException, JSONException {
        Stage stage = (Stage) updatebtn.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/updateProfile.fxml"));

        Parent root = fxmlLoader.load();

        fxmlLoader.<Szakdoga.Controller.UpdateProfile>getController().initdata(token,myJson);

        Scene scene = new Scene(root);

        stage.setTitle("Profil módosítása");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void backClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        if(keres) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/search.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<Szakdoga.Controller.Searching>getController().initdata(token);

            Scene scene = new Scene(root);

            stage.setTitle("Keresés");
            stage.setScene(scene);
            stage.show();
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/startpage.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<StartPage>getController().initdata(token);

            Scene scene = new Scene(root);

            stage.setTitle("Kezdőlap");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void initdata(String token,Player player, JSONObject myprofil,boolean keres){

     this.token = token;
     this.me = player;
     this.myJson = myprofil;
     this.keres = keres;

     if(keres){
         updatebtn.setVisible(false);
     }

     String imageSource = me.getProfilepic();
     profilpic.setImage(new Image(imageSource));
        nevlbl.setText(me.getFirstName() + " " + me.getLastName());
        szullbl.setText(me.getBirthday());
        nicknamelbl.setText(me.getUsername());
        emaillbl.setText(me.getEmail());
        teams = me.getTeams();
        ObservableList<String> gamenames = FXCollections.observableArrayList();
        ObservableList<String> ignnames = FXCollections.observableArrayList();
        ObservableList<String> teamnames = FXCollections.observableArrayList();
        for(int i=0 ; i < me.getGameDetails().size();i++){
            gamenames.add(me.getGameDetails().get(i).getGame().getName());
            ignnames.add(me.getGameDetails().get(i).getIgn());
            teamnames.add(teamname(me.getGameDetails().get(i).getTeamId()));
        }
        game.setItems(gamenames);
        ign.setItems(ignnames);
        team.setItems(teamnames);
    }


    public String teamname(String teamId){
        String teamname = null;

        for(int i=0;i<teams.size();i++){
            if(teams.get(i).getId().equals(teamId)){
                teamname = teams.get(i).getName();
            }
        }
        return teamname;
    }

}
