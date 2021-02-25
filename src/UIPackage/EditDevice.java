package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import CompanyPackage.Company;
import DeviecPackage.Attribute;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

public class EditDevice extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane main_pane = new GridPane();
    private HBox hbButtons = new HBox();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    Label deviceENGName;
    Label devicePERName;

    JFXListView<String> attributesName = new JFXListView<>();
    JFXListView<String> attributesDetails = new JFXListView<>();

    JFXButton btnAdd = new JFXButton("Add");
    JFXButton btnBack = new JFXButton("Back");
    JFXButton btnRemove = new JFXButton("Remove");


    public EditDevice() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        gridPane.setMinSize(500, 425);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(30);
        gridPane.setHgap(80);
        gridPane.setAlignment(Pos.CENTER);

        main_pane.setAlignment(Pos.CENTER);
        main_pane.setVgap(28);

        main_pane.add(gridPane , 0 , 1);

        main_pane.setBackground(new Background(QCConstantValues.background_fill));

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

        main_pane.add(title_text, 0, 0);

        init();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(main_pane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        GridPane panel = new UiUtility().setPanel();
        GridPane pane = new GridPane();
        pane.add(panel , 0 ,0);
        pane.add(main_pane , 1 ,0);
        pane.setHgap(40);
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


        btnAdd.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnAdd.setOnMouseClicked(new AddBtnEvent());

        btnBack.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnBack.setOnMouseClicked(new BackBtnEvent());

        btnRemove.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnRemove.setOnMouseClicked(new RemoveBtnEvent());


        hbButtons.setSpacing(25.0);
        hbButtons.getChildren().addAll(btnBack, btnRemove , btnAdd);

        main_pane.add(hbButtons , 0 , 2);

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

    private class AddBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                UI_Scene newScene = new AddAttributeScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Install New Attribute");
                next_scene.centerOnScreen();
                next_scene.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                UI_Scene newScene = new PreferencesScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Preferences");
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


    @Override
    public void onStop() {
        company.setMainDevice(null);
    }
}
