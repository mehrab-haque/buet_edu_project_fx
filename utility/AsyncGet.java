package utility;

import javafx.animation.AnimationTimer;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/*
   Author : Md. Mehrab Haque
 */

public class AsyncGet extends Observable {
    private JSONObject response;
    private URL url;
    private boolean isError;
    private String errorMessage;
    private AnimationTimer animationTimer;
    public AsyncGet(String string) throws MalformedURLException {
        url=new URL(string);
        isError=false;
        errorMessage="";
        response=new JSONObject();
        animationTimer=new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(response.length()>0 || isError){
                    setChanged();
                    notifyObservers();
                    animationTimer.stop();
                }
            }
        };
    }
    public void send(){
        animationTimer.start();
        Thread thread = new Thread() {
            public void run() {
                try {
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    int res=con.getResponseCode();
                    String data="";
                    if(res==200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String input;
                        while ((input = br.readLine()) != null) {
                            data+=input;
                        }
                        response=new JSONObject(data);
                    }
                } catch(Exception v) {
                    isError=true;
                    errorMessage=v.getMessage();
                }
            }
        };
        thread.start();
    }

    public void reset(){
        isError=false;
        errorMessage="";
        response=new JSONObject();
    }

    public JSONObject getResponse() {
        return response;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

