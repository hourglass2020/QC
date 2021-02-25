package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
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
import javafx.geometry.HPos;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchDeviceScene extends UI_Scene
{
    private Company company = Company.getCompany();
    private Device device = company.getMainDevice();
    private String searchStr = QCMap.getMap().getSearchStr();

    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private HBox hbButtons = new HBox();

    private DirectoryChooser fileChooser;
    private File selectedFile;

    Text deviceENGName;
    Text devicePERName;

    JFXListView<String> attributesName = new JFXListView<>();
    JFXListView<String> attributesDetails = new JFXListView<>();

    JFXButton btnBack = new JFXButton("بازگشت");
    JFXButton btnReport = new JFXButton("گزارش");


    public SearchDeviceScene() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

//        gridPane.setMinSize(500, 425);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(30);
        gridPane.setHgap(80);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(18);
        mainPane.setMinSize(680 , 650);

        mainPane.add(gridPane , 0 , 1);
        mainPane.setBackground(new Background(QCConstantValues.background_fill));

        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER_RIGHT);

        Text title_text = new Text();
        title_text.setFont(new Font("IRANSans", 35));
        title_text.setText("نتیجه جست و جو: " + searchStr);
        title_text.setFill(Color.WHITE);

        Image titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[10]));
        ImageView titleImageView = new ImageView(titleImage);
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
        pane.add(mainPane, 0 ,0);
        pane.setHgap(10);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }


    private void init() throws FileNotFoundException {

/*        deviceENGName = new Text();
        deviceENGName.setText("Device Eng Name: " + device.getEngNameOfDevice());
        deviceENGName.setFont(new Font("Decker" , QCConstantValues.FONT_REGULAR_SIZE));
        deviceENGName.setFill(Color.WHITE);
        gridPane.add(deviceENGName, 0 , 0);*/

        devicePERName = new Text();
        devicePERName.setText("اسم دستگاه: " + device.getPerNameOfDevice());
        devicePERName.setFont(new Font("IRANSans" , 16.5));
        devicePERName.setFill(Color.WHITE);
        GridPane.setHalignment(devicePERName , HPos.RIGHT);

        gridPane.add(devicePERName, 1 , 0);

        Text serial_no = new Text("شماره سریال: " + device.getSerialNo());
        serial_no.setFont(new Font("IRANSans" , 16.5));
        serial_no.setFill(Color.WHITE);
        GridPane.setHalignment(serial_no , HPos.RIGHT);

        gridPane.add(serial_no , 0 , 0);

        Text hc_name = new Text("نام مرکز: " + device.getTempCenter().getName());
        hc_name.setFont(new Font("IRANSans" , 16.5));
        hc_name.setFill(Color.WHITE);
        GridPane.setHalignment(hc_name , HPos.RIGHT);

        gridPane.add(hc_name , 1 , 1);

        Text hc_date = new Text("تاریخ آزمون: " + device.getTempCenter().getDate());
        hc_date.setFont(new Font("IRANSans" , 16.5));
        hc_date.setFill(Color.WHITE);
        GridPane.setHalignment(hc_date , HPos.RIGHT);

        gridPane.add(hc_date , 0 , 1);

        initAttributesNames();
        gridPane.add(attributesName , 0 , 2);
        gridPane.add(attributesDetails , 1 , 2);

        Text result = new Text("نتیجه آزمون: " + device.getResult());
        result.setFont(new Font("IRANSans" , 17));
        result.setFill(Color.WHITE);
        GridPane.setHalignment(result , HPos.RIGHT);

        gridPane.add(result , 1  ,3);

        btnBack.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnBack.setOnMouseClicked(new BackBtnEvent());

        btnReport.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnReport.setOnMouseClicked(new GetComplementaryReportBtnEvent(device));


        hbButtons.setSpacing(25.0);
        hbButtons.getChildren().addAll(btnReport ,  btnBack);

        mainPane.add(hbButtons , 0 , 2);

    }

    private void initAttributesNames()
    {
        for(Attribute attribute : company.getMainDevice().getAllAttributes())
        {
            attributesName.getItems().add(attribute.getName());
        }

        attributesName.setMinHeight(270.0);
        attributesName.setMaxHeight(270.0);
        attributesName.setMinWidth(250.0);
        attributesName.setMaxWidth(250.0);
        attributesDetails.setMinHeight(270.0);
        attributesDetails.setMaxHeight(270.0);
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
        attributesDetails.getItems().add("Result : " + attribute.getMainValue());
        attributesDetails.getItems().add("Category : " + attribute.getCategory());
        attributesDetails.getItems().add("Min : " + attribute.getMin());
        attributesDetails.getItems().add("Max : " + attribute.getMax());
    }

    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {

            try {
                company.setMainHealthCenter(null);
                company.setMainDevice(null);
                UI_Scene newScene = new SearchMainScene(searchStr);
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
        return;
    }
}
