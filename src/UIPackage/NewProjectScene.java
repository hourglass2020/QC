package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import Utilities.DateConverter;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;


public class NewProjectScene extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private GridPane hbButtons = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

    private Text name_new_HC_Text;
    private Text date_new_HC_Text;
    private Text city_new_HC_Text;
    private Text address_new_HC_Text;
    private Text serial_text;

    private JFXTextField name_new_HC_TextField;
    private JFXDatePicker date_new_HC_DatePicker;
    private JFXTextField city_new_HC_TextField;
    private JFXTextArea address_new_HC_TextArea;
    private JFXTextField serial_TextField;

    JFXButton btnSubmit = new JFXButton("ثبت");
    JFXButton btnClear = new JFXButton("دوباره");
    JFXButton btnExit = new JFXButton("بازگشت");

    public NewProjectScene() throws IOException
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


    private void init() throws FileNotFoundException
    {
//        gridPane.setMinSize(500 , 450);
        gridPane.setPadding(new Insets(5 , 10 , 5 , 10));
        gridPane.setVgap(40);
        gridPane.setHgap(150);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER_RIGHT);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(50);
        mainPane.setMinSize(680 , 650);

        title_text = new Text();
        title_text.setFont(new Font("IRANSans",40));
        title_text.setText("پروژه جدید");
        title_text.setFill(Color.WHITE);

        titleImage = new Image(new FileInputStream("Resources/newProjectPanelIco2.png"));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        titlePane.add(title_text , 0 , 0);
        titlePane.add(titleImageView , 1 , 0);

        mainPane.add(titlePane , 0 , 0);

        name_new_HC_Text = new Text();
        name_new_HC_Text.setText("نام مرکز جدید: ");
        name_new_HC_Text.setFont(new Font("IRANSans" , QCConstantValues.FONT_REGULAR_SIZE));
        name_new_HC_Text.setFill(QCConstantValues.MAIN_FONT_COLOR);
        GridPane.setHalignment(name_new_HC_Text , HPos.RIGHT);

        gridPane.add(name_new_HC_Text , 1 , 0);

        name_new_HC_TextField = new JFXTextField();
        name_new_HC_TextField.setMinWidth(250);
        name_new_HC_TextField.setPromptText("Name of center");
        name_new_HC_TextField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(name_new_HC_TextField ,  0, 0);

        date_new_HC_Text = new Text();
        date_new_HC_Text.setText("تاریخ انجام پروژه: ");
        date_new_HC_Text.setFont(new Font("IRANSans" , QCConstantValues.FONT_REGULAR_SIZE));
        date_new_HC_Text.setFill(QCConstantValues.MAIN_FONT_COLOR);
        GridPane.setHalignment(date_new_HC_Text , HPos.RIGHT);
        gridPane.add(date_new_HC_Text , 1 , 1);

        date_new_HC_DatePicker = new JFXDatePicker();
        date_new_HC_DatePicker.getStylesheets().add(this.getClass().getResource("UI_Styles/DatePicker.css").toExternalForm());
        date_new_HC_DatePicker.valueProperty().set(LocalDate.now());
        date_new_HC_DatePicker.setMinWidth(250);

        gridPane.add(date_new_HC_DatePicker, 0 , 1);

        city_new_HC_Text = new Text();
        city_new_HC_Text.setText("شهر: ");
        city_new_HC_Text.setFont(new Font("IRANSans" , QCConstantValues.FONT_REGULAR_SIZE));
        city_new_HC_Text.setFill(QCConstantValues.MAIN_FONT_COLOR);
        GridPane.setHalignment(city_new_HC_Text , HPos.RIGHT);
        gridPane.add(city_new_HC_Text , 1 , 2);

        city_new_HC_TextField = new JFXTextField();
        city_new_HC_TextField.setMinWidth(250);
        city_new_HC_TextField.setPromptText("City");
        city_new_HC_TextField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(city_new_HC_TextField , 0 , 2);

        address_new_HC_Text = new Text();
        address_new_HC_Text.setText("آدرس:");
        address_new_HC_Text.setFont(new Font("IRANSans" , QCConstantValues.FONT_REGULAR_SIZE));
        address_new_HC_Text.setFill(QCConstantValues.MAIN_FONT_COLOR);
        GridPane.setHalignment(address_new_HC_Text , HPos.RIGHT);
        gridPane.add(address_new_HC_Text , 1 , 3);

        address_new_HC_TextArea = new JFXTextArea();
        address_new_HC_TextArea.setMaxWidth(300);
        address_new_HC_TextArea.setMaxHeight(110);
        address_new_HC_TextArea.setPromptText("Address");
        address_new_HC_TextArea.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(address_new_HC_TextArea , 0 , 3);


        serial_text = new Text();
        serial_text.setText("شماره سریال مرکز: ");
        serial_text.setFont(new Font("IRANSans" , QCConstantValues.FONT_REGULAR_SIZE));
        serial_text.setFill(QCConstantValues.MAIN_FONT_COLOR);
        GridPane.setHalignment(serial_text , HPos.RIGHT);
        gridPane.add(serial_text , 1 , 4);

        serial_TextField = new JFXTextField();
        serial_TextField.setMinWidth(250);
        serial_TextField.setPromptText("serial of center");
        serial_TextField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(serial_TextField ,  0, 4);
        //////////////////////////////////////////////////////////////

        hbButtons.setHgap(20.0);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);

        btnSubmit.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnSubmit.setOnMouseClicked(new SubmitBtnEvent());

        btnClear.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnClear.setOnMouseClicked(new ClearBtnEvent());

        btnExit.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnExit.setOnMouseClicked(new ExitBtnEvent());

        hbButtons.add(btnSubmit , 0 , 0);
        hbButtons.add(btnClear , 1 , 0);
        hbButtons.add(btnExit , 2 , 0);
        GridPane.setHalignment(hbButtons , HPos.LEFT);

        mainPane.add(hbButtons ,0 , 2);
    }

    private class ExitBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                MainScene newScene = new MainScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Home Page");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SubmitBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {

            HealthCenter healthCenter = new HealthCenter();
            healthCenter.setName(name_new_HC_TextField.getText());
            healthCenter.setCity(city_new_HC_TextField.getText());
            healthCenter.setAddress(address_new_HC_TextArea.getText());
            // serial of project is not set!
            healthCenter.setSerial(serial_TextField.getText());
            healthCenter.setDate(DateConverter.getDate(date_new_HC_DatePicker.getValue().toString()));

            System.out.println(healthCenter.getDate());

            company.setMainHealthCenter(healthCenter);

            if (!QCStructure.getStructure().setNewHC(healthCenter))
            {
                try {
                    new Utils().make_WarningAlert("There is a problem");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }

            try {
                DeviceChooseScene newScene = new DeviceChooseScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Device Choose");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClearBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            address_new_HC_TextArea.clear();
            city_new_HC_TextField.clear();
            name_new_HC_TextField.clear();
            serial_TextField.clear();
            date_new_HC_DatePicker.valueProperty().set(LocalDate.now());
        }
    }

    @Override
    public void onStop() {
        return;
    }
}
