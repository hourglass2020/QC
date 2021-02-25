package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import DeviecPackage.Device;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class DeviceChooseScene extends UI_Scene
{
    private Company company = Company.getCompany();
    private HealthCenter healthCenter = company.getMainHealthCenter();

    private ScrollPane scrollPane = new ScrollPane();

//    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private GridPane hbButtons = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

    Button btnNext = new JFXButton("بعدی");
    Button btnBack = new JFXButton("بازگشت");

    ObservableList<String> selectedDevicesList = FXCollections.observableArrayList();

    JFXListView<String> deviceTypesList = new JFXListView<>();


    public DeviceChooseScene() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        init();

        mainPane.add(deviceTypesList , 0 , 1);
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
        pane.add(mainPane , 0 ,0);
        pane.setHgap(10);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }


    private void init() throws FileNotFoundException
    {

        try {
            GridPane titlePane = new GridPane();
            titlePane.setHgap(10);
            titlePane.setAlignment(Pos.CENTER_RIGHT);

            mainPane.setAlignment(Pos.CENTER);
            mainPane.setVgap(40);
            mainPane.setMinSize(680 , 650);

            title_text = new Text();
            title_text.setFont(new Font("IRANSans", 35));
            title_text.setText("دستگاه‌های داخل مرکز را انتخاب کنید.");
            title_text.setFill(Color.WHITE);

            titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[10]));
            titleImageView = new ImageView(titleImage);
            titleImageView.setFitWidth(120.0);
            titleImageView.setFitHeight(80.0);
            titleImageView.setPreserveRatio(true);

            titlePane.add(title_text , 0 , 0);
            titlePane.add(titleImageView , 1 , 0);

            mainPane.add(titlePane , 0 , 0);

            btnNext.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
            btnNext.setOnMouseClicked(new NextBtnEvent());
            btnBack.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
            btnBack.setOnMouseClicked(new BackBtnEvent());
            //btnMainAdd.getStylesheets().add(this.getClass().getResource(QCConstantValues.MAIN_THEME).toExternalForm());

            hbButtons.setHgap(20.0);
            hbButtons.setAlignment(Pos.CENTER_RIGHT);

            hbButtons.add(btnNext , 0 , 0);
            hbButtons.add(btnBack , 1 , 0);

            mainPane.add(hbButtons, 0, 2);

            /////////////////////////////////////////////////////////

            for (Device device : company.getDevices()) {
                deviceTypesList.getItems().add(device.getEngNameOfDevice());
            }

            deviceTypesList.setCellFactory(lv -> new ListCell<String>() {
                JFXCheckBox checkBox = new JFXCheckBox();

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        checkBox.setText(item);
                        setGraphic(checkBox);
                    }

                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                    {
                        if (newValue)
                            healthCenter.addCategoryDevice(healthCenter.getDevice(checkBox.getText()));
                        else
                            healthCenter.getCategory_devices().remove(healthCenter.getDevice(item));
                    });

                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            checkBox.setSelected(!checkBox.isSelected());
                        }
                    });
                }
            });
            deviceTypesList.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
            deviceTypesList.setMinHeight(400);
            deviceTypesList.setMaxHeight(400);
            deviceTypesList.setMinWidth(530.0);
            deviceTypesList.setMaxWidth(530.0);
            GridPane.setHalignment(deviceTypesList , HPos.CENTER);
        }
        catch (Exception e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert(e.getMessage());
            return;
        }

    }

    private class NextBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            for(int index = 0; index < healthCenter.getCategory_devices().size() ; index+=2)
            {
                healthCenter.getCategory_devices().remove(index);
            }

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

    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Back Warning");
            alert.setHeaderText("");
            alert.setContentText("If you back, your new project info is lost.\n\nDo you continue?");
            Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                company.getMainHealthCenter().getCategory_devices().clear();
                selectedDevicesList.clear();

                try
                {
                    QCStructure.getStructure().removeCurrentHC();
                    NewProjectScene newScene = new NewProjectScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("New Project");
                    next_scene.centerOnScreen();
                    next_scene.show();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
            try{
                QCStructure.getStructure().removeCurrentHC();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            company.setMainHealthCenter(null);
        }
    }

}
