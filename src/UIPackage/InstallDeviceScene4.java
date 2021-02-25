package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;

public class InstallDeviceScene4 extends UI_Scene
{
    private QCMap map = QCMap.getMap();

    private GridPane mainPane = new GridPane();

    Label title_text;
    Image titleImage;
    ImageView titleImageView;

    Button btnHome = new Button("Preferences");

    FadeTransition transition = new FadeTransition();

    ProgressBar progressBar = new ProgressBar(0);

    public InstallDeviceScene4() throws IOException {

        QCMap.getMap().setCurrentScene(this);

        mainPane.setMinSize(650 , 650);
        mainPane.setPadding(new Insets(10 , 10 , 10 , 10));
        GridPane.setHalignment(mainPane, HPos.CENTER);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(40);


        title_text = new Label();
        title_text.setFont(new Font("Decker", 37));
        title_text.setText("Your installation is successful.");
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


        btnHome.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #e2e2e2;\n" +
                "    -fx-border-radius: 25;\n" +
                "    -fx-background-color: #15151e;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 25;\n" +
                "-fx-min-width: 120.0;" +
                "-fx-text-fill: white");

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


    private Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception
            {
                long elapsedTime = System.currentTimeMillis();

                Thread.sleep(1 * 5000);
                elapsedTime = System.currentTimeMillis() - elapsedTime;
                String status = elapsedTime + " milliseconds";


                QCStructure.getStructure().setNewCategoryDevice(Company.getCompany().getMainDevice());
                QCStructure.getStructure().setAttributes(Company.getCompany().getMainDevice());
                Company.getCompany().setMainDevice(null);

                updateMessage(status);
                updateProgress(1, 1);
                Thread.sleep(1 * 8000);

                return true;
            }
        };
    }



    @Override
    public void onStop() {
        return;
    }
}
