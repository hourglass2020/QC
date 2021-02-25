package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import CompanyPackage.Company;
import DeviecPackage.Device;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InstallDeviceScene extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private HBox hbButtons = new HBox();
    private HBox hbButtons2 = new HBox();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    private Text new_English_DeviceName;
    private Text new_Persian_DeviceName;
    private Text iso_numberText;

    private JFXTextField new_English_DeviceName_Field;
    private JFXTextField new_Persian_DeviceName_Field;
    private JFXTextField iso_numberField;


    JFXButton btnNext = new JFXButton("Next");
    JFXButton btnClear = new JFXButton("Clear");
    JFXButton btnExit = new JFXButton("Exit");

    private Text nameOfCategory;
    private JFXTextField nameOfCategoryField;
    JFXButton btnAdd = new JFXButton("Add");
    JFXButton btnRemove = new JFXButton("Remove");
    private ObservableList<String> addedCategoryList = FXCollections.observableArrayList();
    JFXListView<String> addedCategoryListView;


    public InstallDeviceScene() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        init();

        mainPane.add(gridPane , 0 , 1);
        mainPane.setBackground(new Background(QCConstantValues.background_fill));

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
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(40);
        gridPane.setHgap(60);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(20);
        mainPane.setMinSize(680 , 650);

        title_text = new Label();
        title_text.setFont(new Font("Anton", 35));
        title_text.setText("Install New Device");
        title_text.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        title_text.setTextFill(Color.WHITE);

        titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[7]));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        title_text.setGraphic(titleImageView);

        mainPane.add(title_text, 0, 0);

        new_English_DeviceName = new Text();
        new_English_DeviceName.setText("Enter Name Of Device in English:");
        new_English_DeviceName.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        new_English_DeviceName.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(new_English_DeviceName, 0, 0);

        new_English_DeviceName_Field = new JFXTextField();
        new_English_DeviceName_Field.setMinWidth(250);
        new_English_DeviceName_Field.setStyle("    -fx-text-fill: white; \n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(new_English_DeviceName_Field, 1, 0);

        new_Persian_DeviceName = new Text();
        new_Persian_DeviceName.setText("Enter Name Of Device in Persian:");
        new_Persian_DeviceName.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        new_Persian_DeviceName.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(new_Persian_DeviceName, 0, 1);

        new_Persian_DeviceName_Field = new JFXTextField();
        new_Persian_DeviceName_Field.setMinWidth(250);
        new_Persian_DeviceName_Field.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(new_Persian_DeviceName_Field, 1, 1);

        iso_numberText = new Text();
        iso_numberText.setText("Enter ISO Of Device :");
        iso_numberText.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        iso_numberText.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(iso_numberText, 0, 2);

        iso_numberField = new JFXTextField();
        iso_numberField.setMinWidth(250);
        iso_numberField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(iso_numberField, 1, 2);

        nameOfCategory = new Text();
        nameOfCategory.setText("Enter Name Of New Category:");
        nameOfCategory.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        nameOfCategory.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(nameOfCategory, 0, 3);

        nameOfCategoryField = new JFXTextField();
        nameOfCategoryField.setMinWidth(250);
        nameOfCategoryField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(nameOfCategoryField, 1, 3);

        //////////////////////////////////////////////////////////////

        addedCategoryListView = new JFXListView<>();
        addedCategoryListView.setMinHeight(100.0);
        addedCategoryListView.setMaxHeight(100.0);
        addedCategoryListView.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        gridPane.add(addedCategoryListView , 1 , 4);

        btnAdd.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnAdd.setOnAction((ActionEvent event) ->
        {
            String potential = nameOfCategoryField.getText();

            if (potential != null) {
                addedCategoryListView.getItems().add(potential);
                nameOfCategoryField.setText("");
            }
        });

        btnRemove.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnRemove.setOnAction((ActionEvent event) -> {
            String selectedItem = addedCategoryListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                addedCategoryListView.getSelectionModel().clearSelection();
                addedCategoryListView.getItems().remove(selectedItem);
            }
        });

        hbButtons2.setSpacing(25.0);
        hbButtons2.getChildren().addAll(btnAdd, btnRemove);
        gridPane.add(hbButtons2 , 0 , 4);


        //////////////////////////////////////////////////////////////

        hbButtons.setSpacing(25.0);

        btnNext.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnNext.setOnMouseClicked(new NextBtnEvent());

//        btnClear.getStylesheets().add(this.getClass().getResource("Button.css").toExternalForm());

        btnExit.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnExit.setOnMouseClicked(new ExitBtnEvent());

        hbButtons.getChildren().addAll(btnNext, /*btnClear,*/ btnExit);

        gridPane.add(hbButtons ,0 , 5);
    }

    private class NextBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {

            if(new_English_DeviceName_Field.getText().equalsIgnoreCase("") ||
                    new_Persian_DeviceName_Field.getText().equalsIgnoreCase("") ||
                    iso_numberField.getText().equalsIgnoreCase(""))
            {
                Utils utils = new Utils();
                try {
                    utils.make_WarningAlert("Please fill all fields.");
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return;
            }

            Device tempDevice = new Device();

            tempDevice.setEngNameOfDevice(new_English_DeviceName_Field.getText());
            tempDevice.setPerNameOfDevice(new_Persian_DeviceName_Field.getText());
            tempDevice.setIsoNo(iso_numberField.getText());
            tempDevice.setCategories(addedCategoryListView.getItems().toArray());
            company.setMainDevice(tempDevice);

            try {
                InstallDeviceScene2 newScene = new InstallDeviceScene2();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Install New Device");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ExitBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {

            company.setMainDevice(null);

            try {
                UI_Scene newScene = new PreferencesScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Preferences");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStop() {
        return;
    }
}
