package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import DeviecPackage.Device;
import Utilities.OfflineDataSaving;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class SendingDataScene extends UI_Scene
{
    private QCMap map = QCMap.getMap();

    private GridPane mainPane = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

    Button btnHome = new Button("Home");

    FadeTransition transition = new FadeTransition();

    ProgressBar progressBar = new ProgressBar(0);

    public SendingDataScene() throws IOException
    {
        QCMap.getMap().setCurrentScene(this);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(40);
        mainPane.setMinSize(680 , 650);

        title_text = new Text();
        title_text.setFont(new Font("IRANSans", 37));
        title_text.setText("Please wait to complete the process!");
        title_text.setFill(Color.WHITE);
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
        GridPane.setHalignment(btnHome , HPos.CENTER);
        mainPane.add(btnHome , 0 , 3);

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
        pane.add(mainPane , 0 ,0);
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
                OfflineDataSaving dataSaving = new OfflineDataSaving();
                dataSaving.saveProject(Company.getCompany().getMainHealthCenter());

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
                    Thread.sleep(1 * 3000);
                    updateMessage(status);
                    updateProgress(i + 1, total);
                    i++;
                }
                return true;
            }
        };
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
            Company.getCompany().setMainHealthCenter(null);
        }
    }
}
