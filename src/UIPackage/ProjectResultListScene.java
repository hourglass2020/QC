package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import DeviecPackage.Attribute;
import DeviecPackage.Device;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectResultListScene extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private GridPane hbButtons = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

    Device tempDevice;

    JFXListView<String> devicesName = new JFXListView<>();
    JFXListView<String> devicesDetails = new JFXListView<>();

    JFXButton btnReport = new JFXButton("گزارش");
    JFXButton btnBack = new JFXButton("بازگشت");
    JFXButton btnRemove = new JFXButton("حذف");
    JFXButton btnAdd = new JFXButton("افزودن");

    private DirectoryChooser fileChooser;
    private File selectedFile;

    int counter = 1;

    public ProjectResultListScene() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(40);
        mainPane.setMinSize(680 , 650);
        mainPane.setBackground(new Background(QCConstantValues.background_fill));

        gridPane.setMinSize(500, 400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(30);
        gridPane.setHgap(80);
        gridPane.setAlignment(Pos.CENTER);
        mainPane.add(gridPane , 0 , 1);

        mainPane.setBackground(new Background(QCConstantValues.background_fill));

        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER_RIGHT);

        title_text = new Text();
        title_text.setFont(new Font("IRANSans", 35));
        title_text.setText("لیست دستگاه‌های ثبت شده");
        title_text.setFill(Color.WHITE);

        titleImage = new Image(new FileInputStream("Resources/ReportIco.png"));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        titlePane.add(title_text , 0 , 0);
        titlePane.add(titleImageView , 1 , 0);

        mainPane.add(titlePane , 0 , 0);
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
        pane.add(mainPane , 0 ,0);
        pane.setHgap(10);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }


    private void init() throws FileNotFoundException {

        initDevicesNames();
        gridPane.add(devicesName, 0 , 0);
        gridPane.add(devicesDetails, 1 , 0);


        btnReport.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());

        btnBack.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnBack.setOnMouseClicked(new BackBtnEvent());

        btnRemove.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnRemove.setOnMouseClicked(new RemoveBtnEvent());

        btnAdd.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                try {
                    UI_Scene newScene = new ProjectDeviceListScene();
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
        });

        btnAdd.setDisable(false);

        hbButtons.setHgap(20.0);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);

        hbButtons.add(btnReport , 0 , 0);
        hbButtons.add(btnBack , 1 , 0);
        hbButtons.add(btnRemove , 2 , 0);
        hbButtons.add(btnAdd , 3 , 0);

        mainPane.add(hbButtons , 0 , 2);
    }

    private void initDevicesNames()
    {
        for(Device device : company.getMainHealthCenter().getInputDevices())
        {
            devicesName.getItems().add(counter + ". " + device.getEngNameOfDevice() +
                    "   " + device.getSerialNo());
            counter++;
        }

        devicesName.setMinHeight(350);
        devicesName.setMaxHeight(350);
        devicesName.setMinWidth(250.0);
        devicesName.setMaxWidth(250.0);
        devicesDetails.setMinHeight(350);
        devicesDetails.setMaxHeight(350);
        devicesDetails.setMinWidth(230.0);
        devicesDetails.setMaxWidth(230.0);
        devicesName.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        devicesDetails.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());

        devicesName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if(newValue == null)
                    return;

                int temp = newValue.lastIndexOf(" ");
                newValue = newValue.substring(temp +1, newValue.length());
                tempDevice = company.getMainHealthCenter().getPassDevice(newValue);
                btnReport.setOnMouseClicked(new GetComplementaryReportBtnEvent(tempDevice));
                initAttributesDetails(tempDevice);
            }
        });
    }

    private void initAttributesDetails(Device device) {

        devicesDetails.getItems().clear();

        devicesDetails.getItems().add("Result : " + device.getResult());
        devicesDetails.getItems().add("Serial NO. : " + device.getSerialNo());

        for (Attribute attribute : device.getAllAttributes())
        {
            devicesDetails.getItems().add(attribute.getName() + " : " + attribute.getMainValue());
        }
    }


    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                int choice = 0;
                if(QCMap.getMap().getSearchStr().equalsIgnoreCase("")
                        ||QCMap.getMap().getSearchStr() == null)
                {
                    choice = 1;
                }
                UI_Scene newScene = new ProjectReportScene(choice);
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Open Project");
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
        public void handle(Event event)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("");
            alert.setContentText("Do you want to remove?");
            Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                String selectedItem = devicesName.getSelectionModel().getSelectedItem();

                if (selectedItem != null)
                {
                    counter = 1;
                    int temp = selectedItem.lastIndexOf(" ");
                    selectedItem = selectedItem.substring(temp +1, selectedItem.length());
                    removeDevice(company.getMainHealthCenter().getPassDevice(selectedItem));

                    devicesName.getSelectionModel().clearSelection();
                    devicesName.getItems().remove(selectedItem);
                    devicesName.getItems().clear();
                    devicesDetails.getItems().clear();
                    initDevicesNames();
                }
            }
        }
    }

    private void removeDevice(Device removingDevice)
    {
        for (Device device : company.getMainHealthCenter().getCategory_devices())
        {
            if (device.getEngNameOfDevice().equalsIgnoreCase(removingDevice.getEngNameOfDevice()))
            {
                device.size--;
                String result = removingDevice.getResult();

                if (result.equalsIgnoreCase("pass"))
                {
                    company.getMainHealthCenter().pass--;
                    device.pass--;
                }
                else if (result.equalsIgnoreCase("fail"))
                {
                    company.getMainHealthCenter().fail--;
                    device.fail--;
                }
                else if (result.equalsIgnoreCase("conditional"))
                {
                    company.getMainHealthCenter().cond--;
                    device.cond--;
                }

            }
        }

        try {
            company.getMainHealthCenter().removeInputDevice(removingDevice);
            QCStructure.getStructure().removeDevice(removingDevice);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class GetComplementaryReportBtnEvent implements EventHandler
    {
        Device device;
        public GetComplementaryReportBtnEvent(Device device)
        {
            this.device = device;
        }

        @Override
        public void handle(Event event)
        {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Utils utils = new Utils();

            fileChooser = new DirectoryChooser();
            selectedFile = fileChooser.showDialog(primaryStage);

            if(selectedFile == null)
                return;

            try {
                utils.get_SingleDocumetation(device , selectedFile.getAbsoluteFile());
            } catch (Exception ex) {
                Logger.getLogger(FastReportResultScene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onStop() {
        company.setMainHealthCenter(null);
    }
}
