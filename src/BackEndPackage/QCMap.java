package BackEndPackage;

import CompanyPackage.Company;
import DeviecPackage.Attribute;
import DeviecPackage.Device;
import UIPackage.UI_Scene;
import Utilities.DateConverter;
import com.jfoenix.controls.JFXDecorator;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QCMap
{
    private static QCMap map = new QCMap();
    private QCMap()
    {

    }
    public static QCMap getMap() { return map; }

    private Company company = Company.getCompany();
    private static JSONArray projects = new JSONArray();
    private JSONObject companyInfo = new JSONObject();
    public String last_date = "";
    UI_Scene currentScene;

    JFXDecorator jfxDecorator;

    String searchStr = "";

    public UI_Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(UI_Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void setDecorator(Stage stage , Parent parent) throws FileNotFoundException {
        Image Logo_image = new Image(new FileInputStream
                ("Resources/QCIco.png"));

        ImageView Logo_imageView = new ImageView(Logo_image);
        GridPane.setHalignment(Logo_imageView, HPos.CENTER);
        Logo_imageView.setFitWidth(20);
        Logo_imageView.setFitHeight(20);
        Logo_imageView.setPreserveRatio(true);

        jfxDecorator = new JFXDecorator(stage , parent , false , false , true);
//        jfxDecorator.setCustomMaximize(false);
        jfxDecorator.setMaximized(false);
        jfxDecorator.setMinSize(1000 , 680);
        jfxDecorator.setMaxSize(1000 , 680);
        jfxDecorator.setGraphic(Logo_imageView);
        jfxDecorator.setBackground(new Background(QCConstantValues.background_fill2));
        jfxDecorator.getStylesheets().add(this.getClass().getResource("Decorator.css").toExternalForm());
    }

    public void setDecoratorContent(Scene scene)
    {
        jfxDecorator.setContent(scene.getRoot());
    }

    public JFXDecorator getJfxDecorator()
    {
        return this.jfxDecorator;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public void makeCompanyFile()
    {
        String fileName = "company.json";
        String jsonText;
        File file = new File(fileName);

        if(file.exists())
            return;

        companyInfo.put("date" , DateConverter.getDate(LocalDate.now().toString()));

        jsonText = companyInfo.toString();

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonText);
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveCompany()
    {
        String filename = "company.json";
        String jsonText;

        File file = new File(filename);
        JSONObject object = new JSONObject();

        object.put("date" , DateConverter.getDate(LocalDate.now().toString()));

        jsonText = object.toString();

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonText);
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void initCompany()
    {
        String fileName = "company.json";
        File file = new File(fileName);
        JSONParser parser = new JSONParser();

        if(!file.exists())
        {
            makeCompanyFile();
        }

        try(FileReader reader = new FileReader(fileName))
        {
            Object obj = parser.parse(reader);
            JSONObject companyObject = (JSONObject) obj;
            last_date = companyObject.get("date").toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //////////////////////////////////////////////////////////////////


    public void makeDevicesFile()
    {
        String fileName = "deviceTypes.json";
        String jsonText;
        File file = new File(fileName);

        JSONObject devices = new JSONObject();

        for(Device device : company.getDevices())
        {
            JSONObject deviceOBJ = new JSONObject();

            deviceOBJ.put("english_name" , device.getEngNameOfDevice());
            deviceOBJ.put("persian_name" , device.getPerNameOfDevice());
            deviceOBJ.put("categories" , device.getCategories());
            deviceOBJ.put("iso_number" , device.getIsoNo());
            deviceOBJ.put("attributes" , device.getEngNameOfDevice() + ".json");
            packingAttributes(device);
            devices.put(device.getEngNameOfDevice() , deviceOBJ);
        }

        jsonText = devices.toString();

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonText);
            writer.flush();
        }
        catch (Exception e)
        {}
    }


    public void packingAttributes(Device device)
    {
        JSONArray attributes = new JSONArray();

        String fileName = device.getEngNameOfDevice()+".json";
        String jsonText;
        File file = new File(fileName);

        for (Attribute attribute : device.getAllAttributes())
        {
            JSONObject obj = new JSONObject();

            obj.put("attribute_name" , attribute.getName());
            obj.put("default_val" , attribute.getDefaultValue());
            obj.put("input_mode" , attribute.getStringMode());
            obj.put("min" , attribute.getMin());
            obj.put("max" , attribute.getMax());
            obj.put("category" , attribute.getCategory());
            obj.put("automatic_fill" , attribute.isAutomaticFill());
            obj.put("choices" , attribute.getChoicesNames());
            attributes.add(obj);
        }

        jsonText = attributes.toString();

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonText);
            writer.flush();
        }
        catch (Exception e)
        {
            System.out.println("Salam12");
        }

    }



    //////////////////////////////////////////////////////////////////
}
