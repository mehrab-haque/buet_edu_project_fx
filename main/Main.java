package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.NewProblem;
import java.io.*;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage=stage;
        primaryStage.setResizable(false);
        new NewProblem();

        //pdf experiment
        /*FileChooser fileChooser=new FileChooser();
        FileChooser.ExtensionFilter extFilterJSON = new FileChooser.ExtensionFilter("Problem files","*.prob");
        fileChooser.getExtensionFilters().add(extFilterJSON);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));
                JSONObject data=new JSONObject(reader.readLine());
                BoardView boardView=new BoardView(data.getJSONObject("prob_schema"),MODE_PROBLEM_IMAGE);
                BoardView boardView1=new BoardView(data.getJSONArray("sol_schema").getJSONObject(0),MODE_PROBLEM_IMAGE);
                Stage tmpStage=new Stage();
                Group tmpGroup=new Group();
                tmpGroup.getChildren().addAll(boardView.getLayout(),boardView1.getLayout());
                tmpStage.setScene(new Scene(tmpGroup));
                WritableImage snapshot = boardView.getLayout().snapshot(new SnapshotParameters(), null);
                WritableImage snapshot1 = boardView1.getLayout().snapshot(new SnapshotParameters(), null);
                String dest = "F:/sample.pdf";
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);



                Document document = new Document(pdf, PageSize.A4);
                document.setMargins(20,20,20,20);
                ImageData data1 = ImageDataFactory.create(SwingFXUtils.fromFXImage(snapshot, null), null);
                ImageData data2 = ImageDataFactory.create(SwingFXUtils.fromFXImage(snapshot1, null), null);
                com.itextpdf.layout.element.Image image1=new com.itextpdf.layout.element.Image(data1);
                com.itextpdf.layout.element.Image image2=new com.itextpdf.layout.element.Image(data2);
                image1.setHeight(300);
                image2.setHeight(300);
                image1.setWidth(300);
                image2.setWidth(300);

                image1.setBorder(new SolidBorder(new DeviceRgb(0,145,255),1));



                Label label=new Label("Problem - 1 : "+data.getString("title"));
                label.setStyle("-fx-padding: 5;-fx-font-size: 1.5em;-fx-text-fill: #0090ff;-fx-border-radius: 100;-fx-border-color: #0090ff;-fx-border-width: 2");
                tmpGroup.getChildren().add(label);
                WritableImage labelImage=label.snapshot(new SnapshotParameters(),null);
                ImageData data3=ImageDataFactory.create(SwingFXUtils.fromFXImage(labelImage,null),null);
                com.itextpdf.layout.element.Image image3=new com.itextpdf.layout.element.Image(data3);







                Paragraph header=new Paragraph();
                header.add(image3);
                header.setTextAlignment(TextAlignment.CENTER);
                document.add(header);


                Text text=new Text("Series : "+data.getString("series")+"     |     Category : "+ Constant.CATEGORIES[data.getInt("category")]+"     |     Difficulty : "+data.getInt("difficulty"));
                text.setFontSize(12);

                text.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
                Color myColor = new DeviceRgb(68,68,68);
                text.setFontColor(myColor);
                Paragraph paragraph1=new Paragraph(text);
                paragraph1.setBorder(new SolidBorder(new DeviceRgb(68,68,68),1));
                paragraph1.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
                paragraph1.setMarginTop(10);
                paragraph1.setPadding(5);
                document.add(paragraph1);

                Text text1=new Text("Description : \n");
                text1.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));

                text1.setFontSize(12);
                Color myColor1 = new DeviceRgb(68,68,68);
                text1.setFontColor(myColor1);

                Text text2=new Text(data.getString("description"));
                text2.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
                text2.setFontSize(12);
                Color myColor2 = new DeviceRgb(68,68,68);
                text2.setFontColor(myColor2);
                Paragraph paragraph3=new Paragraph(text1);
                paragraph3.add(text2);
                paragraph3.setPadding(5);
                paragraph3.setMarginTop(10);
                paragraph3.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph3);

                Text text3=new Text("Problem Statement : \n");
                text3.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
                text3.setFontSize(12);
                Color myColor3 = new DeviceRgb(68,68,68);
                text3.setFontColor(myColor3);

                Text text4=new Text(data.getString("statement"));
                text4.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
                text4.setFontSize(12);
                Color myColor4 = new DeviceRgb(68,68,68);
                text4.setFontColor(myColor4);
                Paragraph paragraph4=new Paragraph(text3);
                paragraph4.add(text4);
                paragraph4.setPadding(5);
                paragraph4.setMarginTop(10);
                paragraph4.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph4);

                Text text5=new Text("Problem Figure : \n");
                text5.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
                text5.setFontSize(12);
                Color myColor5 = new DeviceRgb(68,68,68);
                text5.setFontColor(myColor5);
                Paragraph paragraph5=new Paragraph(text5);
                paragraph5.setPadding(5);
                paragraph5.setMarginTop(10);
                paragraph5.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph5);

                Paragraph paragraph6=new Paragraph();
                paragraph6.setTextAlignment(TextAlignment.CENTER);
                paragraph6.setMarginBottom(10);
                paragraph6.add(image1);
                document.add(paragraph6);

                document.add(new AreaBreak());

                Text text6=new Text("Solution Figure(s) : \n");
                text6.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
                text6.setFontSize(12);
                Color myColor6 = new DeviceRgb(68,68,68);
                text6.setFontColor(myColor6);
                Paragraph paragraph7=new Paragraph(text6);
                paragraph7.setPadding(5);
                paragraph7.setMarginTop(10);
                paragraph7.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph7);

                Paragraph paragraph8=new Paragraph();
                paragraph8.setTextAlignment(TextAlignment.CENTER);
                paragraph8.add(image2);
                document.add(paragraph8);

                Text text9=new Text("Explanation : \n");
                text9.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
                text9.setFontSize(12);
                Color myColor9 = new DeviceRgb(68,68,68);
                text9.setFontColor(myColor9);

                Text text10=new Text(data.getString("explanation"));
                text10.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
                text10.setFontSize(12);
                Color myColor10 = new DeviceRgb(68,68,68);
                text10.setFontColor(myColor10);
                Paragraph paragraph10=new Paragraph(text9);
                paragraph10.add(text10);
                paragraph10.setPadding(5);
                paragraph10.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph10);

                document.close();

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}

