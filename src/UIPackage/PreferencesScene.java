package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import DeviecPackage.Device;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static javafx.scene.paint.Color.WHITE;

public class PreferencesScene extends UI_Scene
{
    private Company company = Company.getCompany();

    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private HBox hbButtons = new HBox();
    private HBox hbButtons2 = new HBox();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    private Text company_name;
    private Text company_username;
    private Text company_password;
    private Text company_devices;

    private JFXTextField company_name_TextField;
    private JFXTextField company_username_TextField;
    private JFXPasswordField company_password_TextField;
    private JFXListView<String> company_devices_ListView;

    JFXButton btnSubmit = new JFXButton("Submit");
    JFXButton btnExit = new JFXButton("Exit");

    JFXButton btnRemove = new JFXButton("Remove");
    JFXButton btnEdit = new JFXButton("Edit");
    JFXButton btnInstall = new JFXButton("Install");

    public PreferencesScene() throws IOException
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
        pane.add(mainPane , 0 ,0);
        pane.setHgap(10);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }


    private void init() throws FileNotFoundException
    {
//        gridPane.setMinSize(500 , 500);
        gridPane.setPadding(new Insets(10 , 10 , 10 , 10));
        gridPane.setVgap(25);
        gridPane.setHgap(30);
        gridPane.setAlignment(Pos.CENTER);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(35);
        mainPane.setMinSize(680 , 650);


        title_text = new Label();
        title_text.setFont(new Font("Anton",40));
        title_text.setText(" Preferences");
        title_text.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        title_text.setTextFill(WHITE);

        titleImage = new Image(new FileInputStream("Resources/preferencesPanelIco2.png"));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        title_text.setGraphic(titleImageView);

        mainPane.add(title_text , 0 , 0);

        company_name = new Text();
        company_name.setText("Company Name :");
        company_name.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        company_name.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(company_name, 0 , 0);

        company_name_TextField = new JFXTextField();
        company_name_TextField.setMinWidth(250);
        company_name_TextField.setText(company.getName());
        company_name_TextField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

        gridPane.add(company_name_TextField,  1, 0);

        company_username = new Text();
        company_username.setText("Company Username:");
        company_username.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        company_username.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(company_username,
                0 , 1);

        company_username_TextField = new JFXTextField();
        company_username_TextField.setText(company.getUsername());
        company_username_TextField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        company_username_TextField.setMinWidth(250);

        gridPane.add(company_username_TextField, 1 , 1);

        company_password = new Text();
        company_password.setText("Password :");
        company_password.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        company_password.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(company_password, 0 , 2);

        company_password_TextField = new JFXPasswordField();
        company_password_TextField.setMinWidth(250);
        company_password_TextField.setText(company.getPassword());
        company_password_TextField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        gridPane.add(company_password_TextField, 1 , 2);

        company_devices = new Text();
        company_devices.setText("Company Devices:");
        company_devices.setFont(new Font("Quicksand" , QCConstantValues.FONT_REGULAR_SIZE));
        company_devices.setFill(QCConstantValues.MAIN_FONT_COLOR);

        gridPane.add(company_devices, 0 , 3);

        company_devices_ListView = new JFXListView<>();
        gridPane.add(company_devices_ListView, 1 , 3);


        company_devices_ListView.setMinHeight(180.0);
        company_devices_ListView.setMaxHeight(180.0);
        company_devices_ListView.setMinWidth(250.0);
        company_devices_ListView.setMaxWidth(250.0);
        company_devices_ListView.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        for(Device device : company.getDevices())
        {
            company_devices_ListView.getItems().add(device.getEngNameOfDevice());
        }

        btnRemove.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnRemove.setOnMouseClicked(new RemoveBtnEvent());
        btnRemove.setDisable(!company.isAccess());

        btnEdit.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnEdit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                String selectedItem = company_devices_ListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null)
                {
                    company.setMainDevice(company.getPassDevice(selectedItem));
                    try {
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
        });

        btnInstall.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnInstall.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                try {
                    UI_Scene newScene = new InstallDeviceScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Edit Page");
                    next_scene.centerOnScreen();
                    next_scene.show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        btnInstall.setDisable(!company.isAccess());

        hbButtons2.setSpacing(25.0);
        hbButtons2.getChildren().addAll(btnRemove , /*btnEdit ,*/ btnInstall);

        gridPane.add(hbButtons2 , 0 , 4);

        //////////////////////////////////////////////////////////////

        hbButtons.setSpacing(25.0);

        btnSubmit.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnSubmit.setOnMouseClicked(new SubmitBtnEvent());

        btnExit.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnExit.setOnMouseClicked(new ExitBtnEvent());

        hbButtons.getChildren().addAll(btnSubmit, btnExit);

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
            company.setName(company_name_TextField.getText());
            company.setPassword(company_password_TextField.getText());
            company.setUsername(company_username_TextField.getText());

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


    private class RemoveBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("");
            alert.setContentText("Do you want to remove this device?");
            Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                String selectedItem = company_devices_ListView.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    removeDevice(company.getMainHealthCenter().getPassDevice(selectedItem));
                    company_devices_ListView.getSelectionModel().clearSelection();
                    company_devices_ListView.getItems().remove(selectedItem);
                    company_devices_ListView.getItems().clear();

                    for (Device device : company.getDevices()) {
                        company_devices_ListView.getItems().add(device.getEngNameOfDevice());
                    }
                }
            }
        }
    }

    private void removeDevice(Device removingDevice)
    {
        try {
            company.removeDevice(removingDevice);
            QCStructure.getStructure().removeDevice(removingDevice);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        return;
    }
}
