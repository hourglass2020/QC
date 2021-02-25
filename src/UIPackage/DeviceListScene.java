package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import DeviecPackage.Device;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class DeviceListScene extends UI_Scene{

    private Company company = Company.getCompany();

    private GridPane mainPane = new GridPane();
    private GridPane hbButtons = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

    Button btnNext = new JFXButton("بعدی");
    Button btnBack = new JFXButton("بازگشت");

    ListView<String> deviceTypesList = new JFXListView<>();

//    int counter = 0;

    public DeviceListScene() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        init();

        mainPane.add(deviceTypesList, 0, 1);

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
        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER_RIGHT);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(40);
        mainPane.setMinSize(680 , 650);

        title_text = new Text();
        title_text.setFont(new Font("IRANSans", 35));
        title_text.setText("لیست دستگاه‌ها");
        title_text.setFill(Color.WHITE);

        titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[10]));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        titlePane.add(title_text , 0 , 0);
        titlePane.add(titleImageView , 1 , 0);

        mainPane.add(titlePane , 0 , 0);

        btnNext.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnNext.setOnMouseClicked(new NextBtnEvent());

        btnBack.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnBack.setOnMouseClicked(new BackBtnEvent());

        hbButtons.setHgap(20.0);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);

        hbButtons.add(btnNext , 0 , 0);
        hbButtons.add(btnBack , 1 , 0);

        mainPane.add(hbButtons, 0, 2);

        ////////////////////////////////////////////////


        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            deviceTypesList.getItems().add(device.getEngNameOfDevice());
        }

        deviceTypesList.setCellFactory(lv -> new ListCell<String>()
        {
            JFXButton button = new JFXButton();

            @Override
            public void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    button.setText(item);
                    setGraphic(button);
                }

                button.setOnMouseClicked(new AddBtnEvent(company.getMainHealthCenter().getDevice(item)));
                button.setStyle("-fx-font-size: 16; -fx-text-fill: white");

                setOnMouseClicked(new AddBtnEvent(company.getMainHealthCenter().getDevice(item)));
            }

        });
        deviceTypesList.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        deviceTypesList.setMinHeight(400);
        deviceTypesList.setMaxHeight(400);
        deviceTypesList.setMinWidth(530.0);
        deviceTypesList.setMaxWidth(530.0);
        GridPane.setHalignment(deviceTypesList , HPos.CENTER);

    }


    private class NextBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                UI_Scene newScene = new ResultListScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Result List");
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
        public void handle(Event event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Back Warning");
            alert.setHeaderText("");
            alert.setContentText("If you back, your new project info is lost.\nDo you continue?");
            Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                company.getMainHealthCenter().setCategory_devices(null);
                try {
                    DeviceChooseScene newScene = new DeviceChooseScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Device Choosing");
                    next_scene.centerOnScreen();
                    next_scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class AddBtnEvent implements EventHandler
    {
        private Device deviceType = new Device();

        public AddBtnEvent(Device deviceType)
        {
            if(deviceType == null)
                return;

            this.deviceType =  deviceType;
        }

        @Override
        public void handle(Event event) {

            try {
                if(deviceType == null)
                    return;

                DeviceScene newScene = new DeviceScene(deviceType);
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Device Choosing");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (Exception e) {
                e.printStackTrace();
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
