package view;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.oracle.tools.packager.IOUtils;
import controller.ProbSetNav;
import controller.SolutionButton;
import controller.SolutionHeader;
import controller.ProbHeader;
import editor.BoardView;
import editor.ProblemSet;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.AsyncPost;
import utility.Constant;
import utility.Toast;


import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static utility.Constant.MY_BUCKET;

public class NewProblem {
    private ProblemSet problemSet;
    public NewProblem() throws IOException, IOException {
        //JSONObject data=new JSONObject("{\"prob_schema\":{\"bgColor\":\"0xffffffff\",\"transX\":-2252,\"transY\":-2252,\"indicatorColor\":\"0x32cd32ff\",\"elements\":[{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":23,\"indY\":25,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":24,\"indY\":27,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":26,\"indY\":23,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":27,\"indY\":26,\"type\":\"coin\"}],\"isIndicator\":true,\"lineColor\":\"0x00ff00ff\",\"zoom\":800,\"defaultMatchStick\":{\"fillColor\":\"0x0090ffff\",\"isMust\":true},\"defaultCoin\":{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\"},\"lineOpacity\":0.25},\"sol_schema\":[{\"bgColor\":\"0xffffffff\",\"transX\":-2250.1425625842203,\"transY\":-2252,\"indicatorColor\":\"0x32cd32ff\",\"elements\":[{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":23,\"indY\":25,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":24,\"indY\":27,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":26,\"indY\":23,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":27,\"indY\":26,\"type\":\"coin\"}],\"isIndicator\":true,\"lineColor\":\"0x00ff00ff\",\"zoom\":800,\"defaultMatchStick\":{\"fillColor\":\"0x0090ffff\",\"isMust\":true},\"defaultCoin\":{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\"},\"lineOpacity\":0.25},{\"bgColor\":\"0x003333ff\",\"transX\":-2131.2665679743263,\"transY\":-2133.124005390106,\"indicatorColor\":\"0x32cd32ff\",\"elements\":[{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":23,\"indY\":25,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":24,\"indY\":27,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":26,\"indY\":23,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":27,\"indY\":26,\"type\":\"coin\"},{\"indTailY\":23,\"fillColor\":\"0x0090ffff\",\"indTailX\":21,\"isMust\":true,\"indHeadY\":22,\"indHeadX\":20,\"type\":\"matchStick\"}],\"isIndicator\":true,\"lineColor\":\"0x00ff00ff\",\"zoom\":800,\"defaultMatchStick\":{\"fillColor\":\"0x0090ffff\",\"isMust\":true},\"defaultCoin\":{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\"},\"lineOpacity\":0.25},{\"bgColor\":\"0xffffffff\",\"transX\":-2252,\"transY\":-2252,\"indicatorColor\":\"0x32cd32ff\",\"elements\":[{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":23,\"indY\":25,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":24,\"indY\":27,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":26,\"indY\":23,\"type\":\"coin\"},{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\",\"indX\":27,\"indY\":26,\"type\":\"coin\"}],\"isIndicator\":true,\"lineColor\":\"0x00ff00ff\",\"zoom\":800,\"defaultMatchStick\":{\"fillColor\":\"0x0090ffff\",\"isMust\":true},\"defaultCoin\":{\"outerColor\":\"0x0090ffff\",\"isMust\":true,\"innerColor\":\"0x004588ff\"},\"lineOpacity\":0.25}]}\n");
        Stage stage=new Stage();
        stage.setTitle("Create a problem");
        stage.setResizable(false);
        stage.initOwner(Main.primaryStage);
        Group group=new Group();
        problemSet=new ProblemSet();
        Rectangle dividerRect=new Rectangle(10,600);
        dividerRect.setFill(Constant.COLOR_SKYBLUE);
        dividerRect.setLayoutX(1010);
        FXMLLoader navLoader = new FXMLLoader(getClass().getResource("/fxml/probSetNav.fxml"));
        Parent navLayout = navLoader.load();
        navLayout.setLayoutX(1020);
        ProbSetNav probSetNav = navLoader.getController();

        LoaderView loaderView=new LoaderView(1330,600);
        Group loaderGroup=loaderView.getLayout();
        loaderGroup.setVisible(false);

        probSetNav.save.setOnMouseClicked(event -> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setInitialFileName("ProblemSet");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Problem file", "*.prob")
            );
            fileChooser.setTitle("Save ProblemSet");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    FileOutputStream outStream = new FileOutputStream(file);
                    outStream.write(problemSet.getData().toString().getBytes());
                }catch (Exception e){

                }
            }
        });

        probSetNav.open.setOnMouseClicked(event -> {
            FileChooser fileChooser=new FileChooser();
            FileChooser.ExtensionFilter extFilterJSON = new FileChooser.ExtensionFilter("Problem files","*.prob");
            fileChooser.getExtensionFilters().add(extFilterJSON);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));
                    JSONObject data=new JSONObject(reader.readLine());
                    problemSet=new ProblemSet(data);
                    group.getChildren().set(0,problemSet.getLayout());

                }catch (Exception e){

                }
            }
        });

        probSetNav.submit.setOnMouseClicked(event -> {
            JSONObject data=new JSONObject(problemSet.getData().toString());
            if(data.getInt("ans_type") == 0 && !data.has("sol_schema")){
                Toast.makeText(stage,"draw solution(s) figure as your answer type is not text or MCQ based",1500,500,500);
            }
            else if(!data.has("title")){
                Toast.makeText(stage,"Enter title",1500,500,500);
            }
            else if(!data.has("author")){
                Toast.makeText(stage,"Enter author's name",1500,500,500);
            }
            else if(!data.has("series")){
                Toast.makeText(stage,"Enter series name",1500,500,500);
            }
            else if(!data.has("category")){
                Toast.makeText(stage,"Select category",1500,500,500);
            }
            else if(!data.has("difficulty")){
                Toast.makeText(stage,"Select difficulty",1500,500,500);
            }
            else if(!data.has("description")){
                Toast.makeText(stage,"Enter description",1500,500,500);
            }
            else if(!data.has("statement")){
                Toast.makeText(stage,"Enter problem statement",1500,500,500);
            }
            else if(data.getInt("ans_type")==1 && !data.has("answer")){
                Toast.makeText(stage,"Enter answer text",1500,500,500);
            }
            else if(data.getInt("ans_type")==2 && !data.has("answer")){
                Toast.makeText(stage,"Select answer option",1500,500,500);
            }
            else if(data.getInt("ans_type")==2 && data.getJSONArray("options").length()==1){
                Toast.makeText(stage,"You must set more than 1 option for MCQ based answer",1500,500,500);
            }
            else if(!data.has("explanation")){
                Toast.makeText(stage,"Enter explanation",1500,500,500);
            }
            else{

                loaderGroup.setVisible(true);

                String key=System.currentTimeMillis()+"";
                int nTotal=0;
                final int[] nUploaded = { 0 };
                final boolean[] isError = {false};
                if(data.has("des_images"))nTotal+=data.getJSONArray("des_images").length();
                if(data.has("ans_images"))nTotal+=data.getJSONArray("ans_images").length();

                int finalNTotal = nTotal;
                JSONArray ans_images_url=new JSONArray();
                JSONArray des_images_url=new JSONArray();

                AnimationTimer animationTimer=new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        if (isError[0]) {

                            Toast.makeText(stage, "Error occured, please check your internet connection or existence of images added.", 1500, 500, 500);
                            this.stop();
                            loaderGroup.setVisible(false);
                        } else if (nUploaded[0] == finalNTotal) {
                            if (data.has("des_images")) {
                                data.remove("des_images");
                                data.put("des_images", des_images_url);
                            }
                            if (data.has("ans_images")) {
                                data.remove("ans_images");
                                data.put("ans_images", ans_images_url);
                            }

                            try {
                                AsyncPost postRequest = new AsyncPost(Constant.FIREBASE_ENDPOINT, data);
                                Observer observer = new Observer() {
                                    @Override
                                    public void update(Observable o, Object arg) {
                                        if (postRequest.isError()) {
                                            Toast.makeText(stage, "Error occured, please check your internet connection or existence of images added.", 1500, 500, 500);
                                            loaderGroup.setVisible(false);

                                        } else {
                                            Toast.makeText(stage, "Doneeee", 1500, 500, 500);
                                            loaderGroup.setVisible(false);
                                        }
                                        postRequest.reset();
                                    }
                                };
                                postRequest.addObserver(observer);
                                postRequest.send();
                                this.stop();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                                Toast.makeText(stage, "Error occured, please check your internet connection or existence of images added.", 1500, 500, 500);
                                this.stop();
                                loaderGroup.setVisible(false);
                            }
                        }
                    }
                };
                animationTimer.start();
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            if(data.has("des_images")){
                                JSONArray images=data.getJSONArray("des_images");
                                for(int i=0;i<images.length();i++){
                                    String file_loc = images.getString(i);
                                    String OBJECT_KEY = "problem_images/" + key + "_d_" + i + ".jpg";
                                    AWSCredentials credentials = new BasicAWSCredentials(Constant.ACCESS_KEY, Constant.SECRET_KEY);
                                    AmazonS3 s3 = new AmazonS3Client(credentials);
                                    java.security.Security.setProperty("networkaddress.cache.ttl", "60");
                                    s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                                    s3.setEndpoint("https://s3-ap-south-1.amazonaws.com/");

                                    InputStream inputStream = new FileInputStream(file_loc);
                                    byte[] contents = inputStream.readAllBytes();
                                    ObjectMetadata meta = new ObjectMetadata();
                                    meta.setContentLength(contents.length);
                                    meta.setContentType("image/jpeg");
                                    InputStream stream = new ByteArrayInputStream(contents);
                                    PutObjectRequest putObjectRequest = new PutObjectRequest(
                                            MY_BUCKET, OBJECT_KEY, stream,
                                            meta).withCannedAcl(CannedAccessControlList.PublicRead);


                                    putObjectRequest.setProgressListener(new ProgressListener() {
                                        @Override
                                        public void progressChanged(ProgressEvent progressEvent) {
                                            if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                                                des_images_url.put(Constant.IMAGE_URL+OBJECT_KEY);
                                                nUploaded[0]++;
                                            }
                                        }
                                    });
                                    s3.putObject(putObjectRequest);
                                }
                            }

                            if(data.has("ans_images")){
                                JSONArray images=data.getJSONArray("ans_images");
                                for(int i=0;i<images.length();i++){
                                    String file_loc = images.getString(i);
                                    String OBJECT_KEY = "problem_images/" + key + "_a_" + i + ".jpg";
                                    AWSCredentials credentials = new BasicAWSCredentials(Constant.ACCESS_KEY, Constant.SECRET_KEY);
                                    AmazonS3 s3 = new AmazonS3Client(credentials);
                                    java.security.Security.setProperty("networkaddress.cache.ttl", "60");
                                    s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                                    s3.setEndpoint("https://s3-ap-south-1.amazonaws.com/");

                                    InputStream inputStream = new FileInputStream(file_loc);
                                    byte[] contents = inputStream.readAllBytes();
                                    ObjectMetadata meta = new ObjectMetadata();
                                    meta.setContentLength(contents.length);
                                    meta.setContentType("image/jpeg");
                                    InputStream stream = new ByteArrayInputStream(contents);
                                    PutObjectRequest putObjectRequest = new PutObjectRequest(
                                            MY_BUCKET, OBJECT_KEY, stream,
                                            meta).withCannedAcl(CannedAccessControlList.PublicRead);


                                    putObjectRequest.setProgressListener(new ProgressListener() {
                                        @Override
                                        public void progressChanged(ProgressEvent progressEvent) {
                                            if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                                                ans_images_url.put(Constant.IMAGE_URL+OBJECT_KEY);
                                                nUploaded[0]++;
                                            }
                                        }
                                    });
                                    s3.putObject(putObjectRequest);
                                }
                            }
                        } catch(Exception v) {
                            isError[0] =true;
                            this.stop();
                        }
                    }
                };
                thread.start();
            }
        });


        group.getChildren().addAll(problemSet.getLayout(),dividerRect,navLayout,loaderGroup);
        Scene scene=new Scene(group);
        stage.setScene(scene);
        stage.show();
    }
}