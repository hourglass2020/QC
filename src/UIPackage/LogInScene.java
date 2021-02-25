package UIPackage;

/************************************************/
/*** This class is for Welcome & Log in Window***/
/************************************************/

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.jna.platform.win32.LMAccess;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogInScene extends UI_Scene
{
    private final QCStructure structure = QCStructure.getStructure();

    private GridPane main_pane = new GridPane();
    private GridPane gridPane = new GridPane();

    private Image logo_image;
    private ImageView logo_imageView;
    private FadeTransition logo_image_transition;

    private Text error_loading_image;
    private Text describe_text;

    private Button signing_in_bt;

    private JFXTextField username_textField;
    private JFXPasswordField password_field;

    private Text username;
    private Text password;


    private Alert alert;


    public LogInScene()
    {
        gridPane.setMinSize(150, 150);
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefHeight(0);

        main_pane.setAlignment(Pos.CENTER);
        main_pane.setVgap(30);
        main_pane.setBackground(new Background(QCConstantValues.background_fill2));

        describe_text = new Text();
        describe_text.setFont(new Font("REVOLUTION",30));
        describe_text.setText("Quality Control Program");
        describe_text.setFill(QCConstantValues.MAIN_TITLE_COLOR);
        describe_text.setStrokeWidth(1);
        describe_text.setStrokeLineJoin(StrokeLineJoin.MITER);
        GridPane.setHalignment(describe_text, HPos.CENTER);
        main_pane.add(describe_text , 0 , 1);

        username = new Text("Username");
        username.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        username.setFill(QCConstantValues.MAIN_FONT_COLOR);
        gridPane.add(username , 0 , 0);

        password = new Text("Password");
        password.setFont(new Font(QCConstantValues.FONT_REGULAR_SIZE));
        password.setFill(QCConstantValues.MAIN_FONT_COLOR);
        gridPane.add(password , 0 , 1);

        username_textField = new JFXTextField();
        username_textField.setPromptText("UserName");
        username_textField.setMaxSize(200, 30);
        username_textField.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        GridPane.setHalignment(username_textField, HPos.CENTER);
        gridPane.add(username_textField , 1 , 0);


        password_field = new JFXPasswordField();
        password_field.setPromptText("Password");
        password_field.setMaxSize(200, 30);
        password_field.setStyle("    -fx-text-fill: white;\n" +
                "    -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        GridPane.setHalignment(password_field, HPos.CENTER);
        gridPane.add(password_field , 1 , 1);

        main_pane.add(gridPane , 0 , 2);


        signing_in_bt = new JFXButton("Sign in");
        GridPane.setHalignment(signing_in_bt , HPos.CENTER);
        signing_in_bt.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        signing_in_bt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                logInActivity(event);
            }
        });

/*        hBox.add(signing_in_bt , 0 , 0);
        hBox.add(exit_btn , 1 , 0);*/
        main_pane.add(signing_in_bt , 0 , 3);

        alert = new Alert(Alert.AlertType.ERROR, "Username or password is wrong.");
        alert.setTitle("Error");
        alert.setHeaderText("");

        try
        {
            logo_image = new Image(new FileInputStream
                    ("Resources/QC.png"));

            logo_imageView = new ImageView(logo_image);

            GridPane.setHalignment(logo_imageView, HPos.CENTER);
            main_pane.add(logo_imageView, 0 , 0);
            logo_imageView.setFitWidth(275);
            logo_imageView.setFitHeight(275);
            logo_imageView.setPreserveRatio(true);


            logo_image_transition = new FadeTransition(Duration.millis(3500));
            logo_image_transition.setNode(logo_imageView);
            logo_image_transition.setFromValue(0);
            logo_image_transition.setToValue(1);
            logo_image_transition.setCycleCount(1);
            logo_image_transition.play();

        }

        catch(FileNotFoundException e)
        {
            error_loading_image = new Text();
            error_loading_image.setFont(new Font("Arial",30));
            GridPane.setHalignment(error_loading_image, HPos.CENTER);
            main_pane.add(error_loading_image , 0 , 0);
            error_loading_image.setText("Error to load Logo image!");
            error_loading_image.setFill(QCConstantValues.MAIN_FONT_COLOR);

        }

        current_scene = new Scene(main_pane , 1000 , 650);
        current_scene.setFill(QCConstantValues.SECOND_BACK);
    }

    private void logInActivity(Event event)
    {
        try {
            String username = username_textField.getText().trim();
            String password = password_field.getText().trim();

            if(structure.setToken(username , password))
            {
                UI_Scene mainScene = new MainScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(mainScene.get_Scene());
                next_scene.setTitle("Home Page");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            else {
                alert.show();
                password_field.setText("");
                username_textField.setText("");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Result4: false");
        }
    }

    @Override
    public void onStop() {
        return;
    }
}

