package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import CompanyPackage.Company;
import DeviecPackage.Attribute;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class AddAttributeScene extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane MainPane = new GridPane();
    private HBox hbButtons = new HBox();
    private HBox hbButtons2 = new HBox();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    private Text name_new_attribute;
    private JFXTextField name_new_attribute_Field;

    private Text category;
    private JFXComboBox categoryBox;

    private Text default_Value;
    private JFXTextField default_Value_Field;
    private JFXCheckBox automaticField_CheckBox = new JFXCheckBox("Automatic Filling");

    JFXCheckBox textField_checkBox = new JFXCheckBox("Qualitatively");
    private Text min_Value;
    private JFXTextField min_Value_Field = new JFXTextField();;
    private Text max_Value;
    private JFXTextField max_Value_Field = new JFXTextField();;

    JFXCheckBox radioGP_checkBox = new JFXCheckBox("Quantitative");
    private Text choices;
    private JFXTextField choices_Field;
    private Text test;
    private JFXComboBox testBox;
    JFXButton btnAdd = new JFXButton("Add");
    JFXButton btnRemove = new JFXButton("Remove");

    JFXButton btnClear = new JFXButton("Clear");
    JFXButton btnMainAdd = new JFXButton("Add");
    JFXButton btnBack = new JFXButton("Back");

    public AddAttributeScene() throws IOException
    {
        init();

        MainPane.add(gridPane, 0, 1);
        MainPane.setBackground(new Background(QCConstantValues.background_fill));

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(MainPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        GridPane panel = new UiUtility().setPanel();
        GridPane pane = new GridPane();
        pane.add(panel , 0 ,0);
        pane.add(MainPane , 1 ,0);
        pane.setHgap(40);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);

    }

    private void init() throws FileNotFoundException {

        gridPane.setMinSize(500, 425);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(35);
        gridPane.setHgap(135);
        gridPane.prefHeight(0);
        gridPane.setAlignment(Pos.TOP_CENTER);

        MainPane.setAlignment(Pos.CENTER);
        MainPane.setVgap(28);

        title_text = new Label();
        title_text.setFont(new Font("Anton", 35));
        title_text.setText("Install New Attribute");
        title_text.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        title_text.setTextFill(Color.WHITE);

        titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[7]));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        title_text.setGraphic(titleImageView);

        MainPane.add(title_text, 0, 0);

        name_new_attribute = new Text("Name of attribute: ");
        name_new_attribute.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        name_new_attribute.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(name_new_attribute, 0, 0);

        name_new_attribute_Field = new JFXTextField();
        name_new_attribute_Field.setMinWidth(250);
        name_new_attribute_Field.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(name_new_attribute_Field, 1, 0);

        category = new Text("Choose Category: ");
        category.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        category.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(category, 0, 1);

        categoryBox = new JFXComboBox();
        categoryBox.setMinWidth(70.0);
        categoryBox.getItems().addAll(company.getMainDevice().getCategories().toArray());

        gridPane.add(categoryBox, 1, 1);

        default_Value = new Text("Default Value: ");
        default_Value.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        default_Value.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(default_Value, 0, 2);

        default_Value_Field = new JFXTextField();
        default_Value_Field.setMinWidth(250);
        default_Value_Field.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(default_Value_Field, 1, 2);

        textField_checkBox.setSelected(false);

        textField_checkBox.setFont(new Font("REVOLUTION", QCConstantValues.FONT_REGULAR_SIZE));
        textField_checkBox.setTextFill(Color.WHITE);
        textField_checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            int size = gridPane.getChildren().size();

            if(newValue)
            {
                gridPane.getChildren().remove(8, size);
                textFieldForm();
                min_Value_Field.setDisable(!newValue);
                max_Value_Field.setDisable(!newValue);
            }

            radioGP_checkBox.setSelected(!newValue);
        });
        gridPane.add(textField_checkBox, 0, 3);


        automaticField_CheckBox.setSelected(true);
        automaticField_CheckBox.setTextFill(QCConstantValues.MAIN_FONT_COLOR);
        automaticField_CheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(textField_checkBox.isSelected()) {
                min_Value_Field.setDisable(!newValue);
                max_Value_Field.setDisable(!newValue);
            }
        });



        radioGP_checkBox.setSelected(false);
        radioGP_checkBox.setFont(new Font("REVOLUTION", QCConstantValues.FONT_REGULAR_SIZE));
        radioGP_checkBox.setTextFill(Color.WHITE);
        radioGP_checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            int size = gridPane.getChildren().size();

            if(newValue)
            {
                gridPane.getChildren().remove(8, size);

                radioGroupForm();

                choices_Field.setDisable(!newValue);
                testBox.setDisable(!newValue);
                btnAdd.setDisable(!newValue);
                btnRemove.setDisable(!newValue);
            }

            textField_checkBox.setSelected(!newValue);

        });
        gridPane.add(radioGP_checkBox, 1, 3);

        min_Value_Field.setDisable(true);
        max_Value_Field.setDisable(true);

        //////////////////////////////////////////////////////////////

        hbButtons.setSpacing(25.0);

        btnClear.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnClear.setOnMouseClicked(new ClearBtnEvent());

        btnMainAdd.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnMainAdd.setOnMouseClicked(new AddBtnEventHandler());

        btnBack.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnBack.setOnMouseClicked(new BackBtnEvent());

        hbButtons.getChildren().addAll(btnBack, btnClear, btnMainAdd );
        GridPane.setHalignment(hbButtons , HPos.CENTER);
        MainPane.add(hbButtons, 0, 2);
    }

    private void textFieldForm() {

        gridPane.setHgap(135);

        min_Value = new Text("Min Value: ");
        min_Value.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        min_Value.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(min_Value, 0, 4);

        min_Value_Field.setMinWidth(250);
        min_Value_Field.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(min_Value_Field, 1, 4);

        max_Value = new Text("Max Value: ");
        max_Value.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        max_Value.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(max_Value, 0, 5);

        max_Value_Field.setMinWidth(250);
        max_Value_Field.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(max_Value_Field, 1, 5);

        gridPane.add(automaticField_CheckBox, 0, 6);

    }

    private void radioGroupForm() {

        gridPane.setHgap(60);

        choices = new Text("Add a Choice: ");
        choices.setDisable(true);
        choices.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        choices.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(choices, 0, 4);

        choices_Field = new JFXTextField();
        choices_Field.setDisable(true);
        choices_Field.setMinWidth(250);
        choices_Field.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(choices_Field, 1, 4);

        test = new Text("Test ChoiceBox: ");
        test.setDisable(true);
        test.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        test.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(test, 0, 5);

        testBox = new JFXComboBox();
        testBox.setMinWidth(70.0);
        testBox.setEditable(true);
        testBox.setDisable(true);

        gridPane.add(testBox, 1, 5);

        btnAdd.setDisable(true);
        btnAdd.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnAdd.setStyle("-fx-font-size: 10.3;");
        btnAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                testBox.getItems().add(choices_Field.getText());
                choices_Field.clear();
            }
        });

        btnRemove.setDisable(true);
        btnRemove.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnRemove.setStyle("-fx-font-size: 10.3;");
        btnRemove.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                testBox.getItems().remove(choices_Field.getText());
            }
        });

        Text warText = new Text(" * The first Item will be chosen for the PASS RESULT");
        warText.setFont(new Font(12.0));
        warText.setFill(QCConstantValues.MAIN_FONT_COLOR);

        hbButtons2.getChildren().clear();
        hbButtons2.setSpacing(10.0);
        hbButtons2.setPrefWidth(0);
        hbButtons2.getChildren().addAll(btnAdd, btnRemove);

        gridPane.add(hbButtons2, 0, 6);
        gridPane.add(warText , 1 , 6);

        gridPane.add(automaticField_CheckBox, 0, 7);

    }

    private class AddBtnEventHandler implements EventHandler
    {
        @Override
        public void handle(Event event) {

            Attribute attribute = new Attribute();

            if (name_new_attribute_Field.getText() == null ||
                    name_new_attribute_Field.getText().equalsIgnoreCase(""))
            {
                alert("the name field");
                return;
            }

            attribute.setName(name_new_attribute_Field.getText());
            attribute.setDefaultValue(default_Value_Field.getText());

            if(!categoryBox.getSelectionModel().isEmpty())
                attribute.setCategory(categoryBox.getSelectionModel().getSelectedItem().toString());

            else
                attribute.setCategory("No Title");

            attribute.setAutomaticFill(automaticField_CheckBox.isSelected());


            if (textField_checkBox.isSelected())
            {
                attribute.setMode(Attribute.TEXT_FIELD);

                if(automaticField_CheckBox.isSelected())
                {
                    if ((min_Value_Field.getText() == null) || (max_Value_Field.getText() == null)
                            || min_Value_Field.getText().equalsIgnoreCase("") ||
                            max_Value_Field.getText().equalsIgnoreCase("")) {
                        alert("the min & max");
                        return;
                    } else {
                        attribute.setMin(Double.parseDouble(min_Value_Field.getText()));
                        attribute.setMax(Double.parseDouble(max_Value_Field.getText()));
                    }
                }
            }
            else if (radioGP_checkBox.isSelected()) {
                attribute.setMode(Attribute.RADIO_BTN_GP);
                ObservableList attributesName = testBox.getItems();
                attribute.setMin(0);
                attribute.setMax(attributesName.size() - 1);
                attribute.setDefaultValue("0");

                System.out.println(attributesName);

                attribute.setChoices(attributesName.toArray());
            }

            company.getMainDevice().addAttribute(attribute);

            if (attribute.getMode().equalsIgnoreCase("")) {
                alert("type of attribute");
                return;
            }

            clearFields(attribute.getMode());
        }
    }

    private void alert(String fieldName)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please fill " + fieldName + " values.");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        try {
            stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ridi!");
        }
        alert.showAndWait();
    }

    private class ClearBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event)
        {
            String mode = null;

            if(textField_checkBox.isSelected())
                mode = Attribute.TEXT_FIELD;

            else if(radioGP_checkBox.isSelected())
                mode = Attribute.RADIO_BTN_GP;

            else if (mode.equalsIgnoreCase("")) {
                alert("type of attribute");
                return;
            }

            clearFields(mode);
        }
    }

    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event)
        {
            try
            {
                UI_Scene newScene = new EditDevice();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Edit Page");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields(String mode) {
        name_new_attribute_Field.clear();
        default_Value_Field.clear();

        if (mode.equalsIgnoreCase(Attribute.TEXT_FIELD))
        {
            min_Value_Field.clear();
            max_Value_Field.clear();
        }

        else if(mode.equalsIgnoreCase(Attribute.RADIO_BTN_GP))
        {
            choices_Field.clear();
            testBox.getItems().clear();
        }
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
//            QCStructure.getStructure().removeNewHC(healthCenter);
            company.setMainDevice(null);
        }
    }
}
