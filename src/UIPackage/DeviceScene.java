package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.Operator;
import DeviecPackage.Attribute;
import DeviecPackage.Device;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class DeviceScene extends UI_Scene
{
    private Company company = Company.getCompany();

    private Device mainDevice;

    private GridPane mainGrid = new GridPane();
    private GridPane gridPane = new GridPane();
    private GridPane gridPane2 = new GridPane();

    private JFXScrollPane scrollPane = new JFXScrollPane();
    private GridPane hbButtons = new GridPane();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    JFXButton btnAdd = new JFXButton("Add");
    Button btnExit = new JFXButton("Exit");

    int counter = 0;

    public DeviceScene(Device deviceType) throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        mainDevice = deviceType;

        gridPane.setMinSize(610, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(30);
        gridPane.setHgap(40);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setStyle("-fx-background-color: #15151e");

        gridPane2.setPadding(new Insets(10, 10, 10, 10));
        gridPane2.setVgap(30);
        gridPane2.setHgap(40);
        gridPane2.setAlignment(Pos.TOP_CENTER);
        gridPane2.setStyle("-fx-background-color: #15151e");

//        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        mainGrid.setVgap(30);
        mainGrid.setHgap(40);
        mainGrid.setAlignment(Pos.TOP_CENTER);
        mainGrid.setStyle("-fx-background-color: #15151e");

        title_text = new Label();
        title_text.setFont(new Font("Anton", 40));
        title_text.setText( " " + mainDevice.getEngNameOfDevice() + " CheckList");
        title_text.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        title_text.setTextFill(Color.WHITE);

        titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[13]));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        title_text.setGraphic(titleImageView);

        hbButtons.setHgap(25.0);

        btnAdd.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());

        btnExit.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnExit.setOnMouseClicked(new BackBtnEvent());

        hbButtons.add(btnExit , 0 , 0);
        hbButtons.add(btnAdd , 1 , 0);
        hbButtons.setAlignment(Pos.CENTER);
        GridPane.setHalignment(hbButtons , HPos.CENTER);

/*        scrollPane.setMinWidth(800);
        scrollPane.setMaxWidth(800);
        scrollPane.setMaxHeight(550);
        scrollPane.setMinHeight(550);*/

        VBox vBox = new VBox();

        hbButtons.setPrefWidth(0);
        hbButtons.setPrefHeight(0);
        vBox.getChildren().addAll(title_text , hbButtons);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);
        scrollPane.getMidBar().getChildren().add(vBox);
        scrollPane.getMidBar().setAlignment(Pos.BASELINE_CENTER);
        scrollPane.setContent(mainGrid);
        scrollPane.getStylesheets().add(this.getClass().getResource("UI_Styles/ScrollPane.css").toExternalForm());

        initDefaultFields();

        mainGrid.add(gridPane,0 , 0);
        mainGrid.add(gridPane2 , 0 , 1);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(scrollPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        current_scene = new Scene(scrollPane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }

    private void initDefaultFields() throws FileNotFoundException
    {
        Device tempDevice = new Device(mainDevice);
        tempDevice.setAddedAttributes(mainDevice.getAddedAttributes());

        init("" , tempDevice);

        counter = 0;

        //////////////////////////////////////////////////////////

        for(Attribute attribute: tempDevice.getDefaultAttributes())
        {
            if(attribute.getMode().equalsIgnoreCase(Attribute.TEXT_FIELD))
            {
                if(attribute.getName().equalsIgnoreCase("Expert"))
                {
                    attribute.setDefaultValue(Operator.getOperator().getFirst_name() + " " +
                                                Operator.getOperator().getLast_name());
                    attribute.setMainValue(attribute.getDefaultValue());
                }
                initTextField(attribute , "" , gridPane);
            }
        }

        //////////////////////////////////////////////////////////

        Text serialAttText = new Text("Serial NO : ");
        serialAttText.setFill(QCConstantValues.MAIN_FONT_COLOR);
        serialAttText.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        GridPane.setHalignment(serialAttText, HPos.CENTER);
        gridPane.add(serialAttText , 0 , counter);

        TextField serialAttTextField = new JFXTextField();
        serialAttTextField.setMinWidth(250.0);
        serialAttTextField.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");


        serialAttTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            tempDevice.setSerialNo(newValue);
        });
        GridPane.setHalignment(serialAttTextField, HPos.CENTER);

        gridPane.add(serialAttTextField , 1 , counter);

        counter++;

        Text attributeTitle = new Text("Result");
        attributeTitle.setFill(QCConstantValues.MAIN_FONT_COLOR);
        attributeTitle.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        GridPane.setHalignment(attributeTitle, HPos.CENTER);
        gridPane.add(attributeTitle , 0 , counter);


        ComboBox resultBox = new JFXComboBox();
        resultBox.getItems().add("Pass");
        resultBox.getItems().add("Fail");
        resultBox.getItems().add("Conditional");
        gridPane.add(resultBox , 1 , counter);
        resultBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    btnAdd.setOnMouseClicked(new AddBtnEvent(newValue.toString() , tempDevice));
                    init(newValue.toString() , tempDevice);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
//        counter++;

    }

    private void init(String result , Device tempDevice) throws FileNotFoundException {

        counter = 0;
        int index = 0;

        gridPane2.getChildren().clear();

        for(String str : tempDevice.getCategories())
        {
            Label categoryLabel = new Label();
            categoryLabel.setText(str);
            categoryLabel.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
            categoryLabel.setTextFill(Color.WHITE);
            gridPane2.add(categoryLabel , 0 , counter);
            counter++;

            for (Attribute attribute : tempDevice.getAddedAttributes()) {

                if(attribute.getCategory().equalsIgnoreCase(str))
                {
                    if (attribute.getMode().equalsIgnoreCase(Attribute.TEXT_FIELD)) {
                        initTextField(attribute, result , gridPane2);
                    } else if (attribute.getMode().equalsIgnoreCase(Attribute.RADIO_BTN_GP)) {
                        initRadioGP(attribute, result , gridPane2);
                    }
                }
            }
        }

        //////////////////////////////////////////////////////////
    }



    private void initRadioGP(Attribute attribute , String result , GridPane gridPane)
    {
        Text attributeTitle = new Text(attribute.getName());
        attributeTitle.setFill(QCConstantValues.MAIN_FONT_COLOR);
        attributeTitle.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        GridPane.setHalignment(attributeTitle, HPos.CENTER);
        gridPane.add(attributeTitle , 0 , counter);

        JFXComboBox attributeBox = new JFXComboBox();
        attributeBox.getItems().addAll(attribute.getChoicesNames().toArray());

        int choice;

        if(fill(attribute , result).equalsIgnoreCase(""))
            choice = 0;

        else
        {
            choice = Integer.parseInt(fill(attribute , result));
        }

        attributeBox.getSelectionModel().select(choice);
        String temp = attributeBox.getSelectionModel().getSelectedItem().toString();
        attribute.setMainValue(temp);
        attributeBox.setMinWidth(250.0);
        attributeBox.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if(attribute.isAutomaticFill())
                attribute.setMainValue(oldValue.toString());
            else
                attribute.setMainValue(newValue.toString());
        });
        GridPane.setHalignment(attributeBox, HPos.CENTER);

        gridPane.add(attributeBox , 1 , counter);

        counter++;
    }


    private void initTextField(Attribute attribute , String result , GridPane gridPane)
    {
        Text attributeTitle = new Text(attribute.getName());
        attributeTitle.setFill(QCConstantValues.MAIN_FONT_COLOR);
        attributeTitle.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        GridPane.setHalignment(attributeTitle, HPos.CENTER);
        gridPane.add(attributeTitle , 0 , counter);

        TextField attributeField = new JFXTextField();
        attributeField.setMinWidth(250.0);
        attributeField.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        String temp = fill(attribute , result);

        attribute.setMainValue(temp);
        attributeField.setText(temp);
        attributeField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(attribute.isAutomaticFill())
                attribute.setMainValue(oldValue);
            else
                attribute.setMainValue(newValue);
        });
        GridPane.setHalignment(attributeField, HPos.CENTER);

        gridPane.add(attributeField , 1 , counter);

        counter++;
    }


    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                DeviceListScene newScene = new DeviceListScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Device List");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddBtnEvent implements EventHandler
    {
        String result;
        Device device;

        public AddBtnEvent(String result , Device device)
        {
            this.result = result;
            this.device = device;
        }

        @Override
        public void handle(Event event)
        {
//            initDefaultFields();

            if(result.equalsIgnoreCase(""))
            {
                alert("result");
                return;
            }

            if(device.getSerialNo() == null ||
                    device.getSerialNo().equalsIgnoreCase(""))
            {
                try {
                    new Utils().make_WarningAlert("Please fill serial no.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }

            device.setResult(result);
            mainDevice.size++;
//            System.out.println(result);

            if (result.equalsIgnoreCase("pass"))
            {
                company.getMainHealthCenter().pass++;
                mainDevice.pass++;
            }
            else if (result.equalsIgnoreCase("fail"))
            {
                company.getMainHealthCenter().fail++;
                mainDevice.fail++;
            }
            else if (result.equalsIgnoreCase("conditional"))
            {
                company.getMainHealthCenter().cond++;
                mainDevice.cond++;
            }

            company.getMainHealthCenter().addInputDevice(this.device);
            try
            {
                gridPane.getChildren().clear();
                initDefaultFields();
//                init("" , this.device);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void alert(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please determine the " + str + ".");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        try {
            stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        alert.showAndWait();
    }

    private class ClearBtnEvent implements EventHandler
    {
        Device device;

        public ClearBtnEvent(Device device) {
            this.device = device;
        }

        @Override
        public void handle(Event event) {
            try
            {
                gridPane.getChildren().clear();
                initDefaultFields();
//                init("" , device);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String fill(Attribute attribute , String result)
    {
        String fillVAL = "";

        if(attribute.isAutomaticFill() && attribute.getMode().equalsIgnoreCase(Attribute.RADIO_BTN_GP))
        {
            if(result.equalsIgnoreCase("pass"))
            {
                fillVAL = "0";
            }
            else if(result.equalsIgnoreCase("fail") ||
                    result.equalsIgnoreCase("conditional"))
            {
                Random random = new Random();
                double temp = random.nextDouble() * (attribute.getChoicesNames().size());
                int choice = (int) temp;

                fillVAL = String.valueOf(choice + (int)random.nextDouble());
            }

        }
        else if(attribute.isAutomaticFill() && attribute.getMode().equalsIgnoreCase(Attribute.TEXT_FIELD))
        {

            if(result.equalsIgnoreCase("pass"))
            {
                Random random = new Random();
                fillVAL = Double.toString((random.nextDouble() * (attribute.getMax() - attribute.getMin()) + attribute.getMin()));
                fillVAL = fillVAL.substring(0 , 4);
            }
            else if(result.equalsIgnoreCase("fail") ||
                    result.equalsIgnoreCase("conditional"))
            {
                Random random = new Random();
                fillVAL = Double.toString((random.nextDouble() * (attribute.getMax() - attribute.getMin() + 1) + attribute.getMin() + (random.nextInt(1))));
                fillVAL = fillVAL.substring(0 , 4);
            }
        }
        else if(!attribute.isAutomaticFill())
        {
            fillVAL = attribute.getDefaultValue();
        }


        return fillVAL;
    }


    @Override
    public void onStop() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back Warning");
        alert.setHeaderText("");
        alert.setContentText("Your new project info is lost.\n\nDo you continue?");
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            try
            {
                QCStructure.getStructure().removeCurrentHC();
                company.setMainHealthCenter(null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
