package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import DeviecPackage.Attribute;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class InstallDeviceScene3 extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private HBox hbButtons = new HBox();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    Label deviceENGName;
    Label devicePERName;

    JFXListView<String> attributesName = new JFXListView<>();
    JFXListView<String> attributesDetails = new JFXListView<>();

    JFXButton btnNext = new JFXButton("Next");
    JFXButton btnBack = new JFXButton("Back");
    JFXButton btnRemove = new JFXButton("Remove");


    public InstallDeviceScene3() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        gridPane.setMinSize(500, 425);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(30);
        gridPane.setHgap(80);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(28);
        mainPane.setMinSize(680 , 650);

        mainPane.add(gridPane , 0 , 1);

        mainPane.setBackground(new Background(QCConstantValues.background_fill));

        title_text = new Label();
        title_text.setFont(new Font("Anton", 35));
        title_text.setText(" Result of Installation");
        title_text.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        title_text.setTextFill(Color.WHITE);

        titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[7]));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        title_text.setGraphic(titleImageView);

        mainPane.add(title_text, 0, 0);

        init();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(mainPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        GridPane panel = new UiUtility().setPanel();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.add(panel , 1 ,0);
        pane.add(mainPane, 0 ,0);
        pane.setHgap(10);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }


    private void init() throws FileNotFoundException {

        deviceENGName = new Label();
        deviceENGName.setText("Device Eng Name: " + company.getMainDevice().getEngNameOfDevice());
        deviceENGName.setFont(new Font("Decker" , QCConstantValues.FONT_REGULAR_SIZE + 2.0));
        deviceENGName.setTextFill(Color.WHITE);
        gridPane.add(deviceENGName, 0 , 0);

        devicePERName = new Label();
        devicePERName.setText("Device Per Name: " + company.getMainDevice().getPerNameOfDevice());
        devicePERName.setFont(new Font("Decker" , QCConstantValues.FONT_REGULAR_SIZE + 2.0));
        devicePERName.setTextFill(Color.WHITE);
        gridPane.add(devicePERName, 0 , 1);

        initAttributesNames();
        gridPane.add(attributesName , 0 , 2);
        gridPane.add(attributesDetails , 1 , 2);


        btnNext.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnNext.setOnMouseClicked(new NextBtnEvent());

        btnBack.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnBack.setOnMouseClicked(new ClearBtnEvent());

        btnRemove.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnRemove.setOnMouseClicked(new RemoveBtnEvent());


        hbButtons.setSpacing(25.0);
        hbButtons.getChildren().addAll(btnBack, btnRemove , btnNext);

        mainPane.add(hbButtons , 0 , 2);

        formAlert(company.getMainDevice().getAttribute("دما"));
        formAlert(company.getMainDevice().getAttribute("فشار"));
        formAlert(company.getMainDevice().getAttribute("رطوبت"));

    }

    private void initAttributesNames()
    {
        for(Attribute attribute : company.getMainDevice().getAllAttributes())
        {
            attributesName.getItems().add(attribute.getName());
        }

        attributesName.setMinHeight(250.0);
        attributesName.setMaxHeight(250.0);
        attributesName.setMinWidth(250.0);
        attributesName.setMaxWidth(250.0);
        attributesDetails.setMinHeight(250.0);
        attributesDetails.setMaxHeight(250.0);
        attributesDetails.setMinWidth(230.0);
        attributesDetails.setMaxWidth(230.0);
        attributesName.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        attributesDetails.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());

        attributesName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || newValue.equalsIgnoreCase(""))
                    return;

                initAttributesDetails(company.getMainDevice().getAttribute(newValue));
            }
        });
    }

    private void initAttributesDetails(Attribute attribute) {

        attributesDetails.getItems().clear();
        attributesDetails.getItems().add("Name : " + attribute.getName());
        attributesDetails.getItems().add("Category : " + attribute.getCategory());
        attributesDetails.getItems().add("Type Input : " + attribute.getMode());
        attributesDetails.getItems().add("Default Value : " + attribute.getDefaultValue());
        attributesDetails.getItems().add("Min : " + attribute.getMin());
        attributesDetails.getItems().add("Max : " + attribute.getMax());
    }

    private class NextBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {

            company.addDevice(company.getMainDevice());
            try {
                InstallDeviceScene4 newScene = new InstallDeviceScene4();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Install New Device");
                next_scene.centerOnScreen();
                next_scene.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClearBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                UI_Scene newScene = new InstallDeviceScene2();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Install New Device");
                next_scene.centerOnScreen();
                next_scene.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class RemoveBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            String selectedItem = attributesName.getSelectionModel().getSelectedItem();
            if (selectedItem != null)
            {
                Attribute attribute = company.getMainDevice().getAttribute(selectedItem);

                if(attribute.getCategory().equalsIgnoreCase("default"))
                {
                    alert("This is a default attribute and you can not remove it.");
                    return;
                }

                attributesName.getSelectionModel().clearSelection();
                attributesName.getItems().remove(selectedItem);
                company.getMainDevice().removeAttribute(attribute);

                attributesDetails.getItems().clear();
                attributesName.getItems().clear();

                initAttributesNames();

            }
        }
    }

    private void alert(String fieldName)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(fieldName);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        try {
            stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ridi!");
        }
        alert.showAndWait();
    }

    private void formAlert(Attribute attribute)
    {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Setting up attributes");
        dialog.setHeaderText("");
        dialog.getDialogPane().setBackground(new Background(QCConstantValues.background_fill));

// Set the button types.
        ButtonType setButtonType = new ButtonType("Set", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(setButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));

        Text titleText = new Text("Please set min & max values of attribute " + attribute.getName());
        titleText.setFont(new Font("Anton",18));
        titleText.setFill(QCConstantValues.MAIN_TITLE_COLOR);


        TextField minValue = new JFXTextField();
        minValue.setPromptText("Min");
        minValue.setMinWidth(250);
        minValue.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");


        Text minText = new Text("Min Value:");
        minText.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        minText.setFill(QCConstantValues.MAIN_FONT_COLOR);

        JFXTextField maxValue = new JFXTextField();
        maxValue.setPromptText("Max");
        maxValue.setMinWidth(250);
        maxValue.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");


        Text maxText = new Text("Max Value:");
        maxText.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        maxText.setFill(QCConstantValues.MAIN_FONT_COLOR);


        JFXCheckBox automaticField_CheckBox = new JFXCheckBox("Automatic Filling");
        automaticField_CheckBox.setTextFill(QCConstantValues.MAIN_FONT_COLOR);


        grid.add(titleText , 0 , 0);

        GridPane tempGrid = new GridPane();
        tempGrid.setHgap(10);
        tempGrid.setVgap(10);
        tempGrid.setPadding(new Insets(20, 150, 10, 10));

        tempGrid.add(minText, 0, 0);
        tempGrid.add(minValue, 1, 0);
        tempGrid.add(maxText, 0, 1);
        tempGrid.add(maxValue, 1, 1);
        tempGrid.add(automaticField_CheckBox , 0 , 2);

        grid.add(tempGrid , 0 , 1);

        Node setButton = dialog.getDialogPane().lookupButton(setButtonType);
        setButton.setDisable(false);

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> minValue.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == setButtonType) {
                if(minValue == null || minValue.getText().equalsIgnoreCase("") ||
                        maxValue == null || maxValue.getText().equalsIgnoreCase(""))
                {

                }
                else
                {
                    attribute.setMin(Double.parseDouble(minValue.getText()));
                    attribute.setMax(Double.parseDouble(maxValue.getText()));
                    attribute.setAutomaticFill(automaticField_CheckBox.isSelected());
                }
                return new Pair<>(minValue.getText(), maxValue.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            if(minValue == null || minValue.getText().equalsIgnoreCase("") ||
                    maxValue == null || maxValue.getText().equalsIgnoreCase(""))
            {
            }
            else
            {
                attribute.setMin(Double.parseDouble(minValue.getText()));
                attribute.setMax(Double.parseDouble(maxValue.getText()));
                attribute.setAutomaticFill(automaticField_CheckBox.isSelected());
            }
        });
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
                QCStructure.getStructure().removeDevice(company.getMainDevice());
                company.setMainDevice(null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
