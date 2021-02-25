package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import DeviecPackage.Device;
import Utilities.Utils;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FastReportResultScene extends UI_Scene
{
    private DirectoryChooser fileChooser;
    private File selectedFile;

    private GridPane mainPane = new GridPane();
    private GridPane hBox = new GridPane();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    Button btnHome = new JFXButton("Home");
    Button btnReport = new JFXButton("Report");

    FadeTransition transition = new FadeTransition();

    ProgressBar progressBar = new ProgressBar(0);

    public FastReportResultScene() throws IOException {
        QCMap.getMap().setCurrentScene(this);

        mainPane.setMinSize(650 , 650);
        mainPane.setPadding(new Insets(10 , 10 , 10 , 10));
        GridPane.setHalignment(mainPane, HPos.CENTER);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(40);

        title_text = new Label();
        title_text.setFont(new Font("Decker", 37));
        title_text.setText("Please wait to complete the process!");
        title_text.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        title_text.setTextFill(Color.WHITE);
        GridPane.setHalignment(title_text , HPos.CENTER);

        mainPane.add(title_text, 0, 1);

        titleImage = new Image(new FileInputStream(QCConstantValues.TITLES[8]));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(250.0);
        titleImageView.setFitHeight(250.0);
        titleImageView.setPreserveRatio(true);
        GridPane.setHalignment(titleImageView , HPos.CENTER);

        mainPane.add(titleImageView, 0, 0);


        progressBar.setPrefSize(200, 24);
        progressBar.getStylesheets().add(this.getClass().getResource("UI_Styles/progressBar.css").toExternalForm());


        Task copyWorker = createWorker();
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(copyWorker.progressProperty());
        progressBar.progressProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.intValue() == 1)
            {
                title_text.setText("Your project is complete!");
            }
        });

        new Thread(copyWorker).start();


        GridPane.setHalignment(progressBar , HPos.CENTER);
        mainPane.add(progressBar , 0 , 2);


        btnHome.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnHome.setOnMouseClicked(new HomeBtnEvent());

        btnReport.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnReport.setOnMouseClicked(new GetComplementaryReportBtnEvent
                (Company.getCompany().getMainHealthCenter().getInputDevice(0)));

        hBox.setHgap(25.0);
        hBox.setAlignment(Pos.CENTER);
        hBox.add(btnHome , 0 , 0);
        hBox.add(btnReport , 1 , 0);
        GridPane.setHalignment(hBox , HPos.CENTER);

        mainPane.add(hBox , 0 , 3);

        mainPane.setBackground(new Background(QCConstantValues.background_fill));

        transition.setDuration(Duration.millis(1700));
        transition.setFromValue(0.0);
        transition.setToValue(1.0);
        transition.setNode(mainPane);
        transition.play();


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

    private class HomeBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try
            {
                Company.getCompany().setMainHealthCenter(null);
                UI_Scene newScene = new MainScene();
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


    private Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                int total = Company.getCompany().getMainHealthCenter().getInputDevices().size();
                int i = 0;
                for (Device device : Company.getCompany().getMainHealthCenter().getInputDevices()){
                    long elapsedTime = System.currentTimeMillis();

                    elapsedTime = System.currentTimeMillis() - elapsedTime;
                    String status = elapsedTime + " milliseconds";


                    QCStructure.getStructure().setNewDevice(device);
                    QCStructure.getStructure().setAttributes(device);

                    updateMessage(status);
                    updateProgress( 1, total);
                    Thread.sleep(1 * 5000);
                    i++;
                }
                return true;
            }
        };
    }

    @Override
    public void onStop() {
        return;
    }
}
