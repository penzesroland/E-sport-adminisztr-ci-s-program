package Szakdoga.Model;

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

import java.util.List;

public class Game {
    String altername;
    String id;
    String middleLeaderId;
    String name;

    public Game(String altername, String id, String middleLeaderId, String name) {
        this.altername = altername;
        this.id = id;
        this.middleLeaderId = middleLeaderId;
        this.name = name;
    }

    public Game() {
    }

    public String getAltername() {
        return altername;
    }

    public void setAltername(String altername) {
        this.altername = altername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMiddleLeaderId() {
        return middleLeaderId;
    }

    public void setMiddleLeaderId(String middleLeaderId) {
        this.middleLeaderId = middleLeaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
