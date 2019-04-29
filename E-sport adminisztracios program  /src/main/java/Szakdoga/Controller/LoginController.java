package Szakdoga.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import static javax.print.attribute.standard.ReferenceUriSchemesSupported.HTTP;

public class LoginController implements Initializable {
    @FXML
    private Button login;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label hiba;

    public String token;

    @FXML
    private void loginClick(ActionEvent event) throws IOException{
        token = login(username.getText(),password.getText());
        if(token != null){
            hiba.setText("");
            Stage stage = (Stage) login.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/startpage.fxml"));

            Parent root = fxmlLoader.load();

            fxmlLoader.<StartPage>getController().initdata(token);

            Scene scene = new Scene(root);

            stage.setTitle("Kezdőlap");
            stage.setScene(scene);
            stage.show();
        }else{
        hiba.setText("Hibás bejelentkezési adatok");
        username.setText("");
        password.setText("");
        }
    }
    public String login(String username,String password){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String token ="";
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("deac-hackers-rest.herokuapp.com",80 ,"http");

            // specify the get request
           // HttpGet getRequest = new HttpGet("/api/login");

            HttpPost postRequest = new HttpPost("/api/login");

            json.put("password",password);
            json.put("usernameOrEmail",username);

            postRequest.addHeader("accept","application/json");

            StringEntity se = new StringEntity(json.toString());
            se.setContentType( "application/json");

            postRequest.setEntity(se);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
                JSONObject tokenJson = new JSONObject(EntityUtils.toString(entity));
                try{
                    token = (String) tokenJson.get("token");}
                    catch(Exception e){
                    return null;
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
        return token;
    }


    public void initialize(URL location, ResourceBundle resources) {
        hiba.setText("");
    }
}
