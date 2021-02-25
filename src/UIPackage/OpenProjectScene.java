package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OpenProjectScene extends UI_Scene
{
    private GridPane gridPane = new GridPane();
    private GridPane mainPane = new GridPane();
    private GridPane hbButtons = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

/*    private final TableView<HealthCenter> table = new TableView<>();
    private final ObservableList<HCUtility> data = FXCollections.observableArrayList();*/

    Button btnExit = new JFXButton("بازگشت");

    public OpenProjectScene() throws IOException
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

    private void init() throws IOException
    {
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(5);
        mainPane.setMinSize(680 , 650);

        gridPane.setMinSize(500 , 500);
        gridPane.setPadding(new Insets(10 , 10 , 10 , 10));
        gridPane.setVgap(20);
        gridPane.setHgap(40);
        gridPane.setAlignment(Pos.CENTER);

        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER_RIGHT);

        title_text = new Text();
        title_text.setFont(new Font("IRANSans",37));
        title_text.setText("لیست پروژه‌ها");
        title_text.setFill(Color.WHITE);

        titleImage = new Image(new FileInputStream("Resources/openProjectPanelIco2.png"));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        titlePane.add(title_text , 0 , 0);
        titlePane.add(titleImageView , 1 , 0);

        mainPane.add(titlePane , 0 , 0);

        gridPane.add(initTableList() , 0 , 0);

        btnExit.getStylesheets().add(this.getClass().getResource("UI_Styles/Button.css").toExternalForm());
        btnExit.setOnMouseClicked(new ExitBtnEvent());

        hbButtons.setHgap(20.0);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);

        hbButtons.add(btnExit , 0 , 0);

        mainPane.add(hbButtons, 0, 2);
    }


    private JFXListView<HealthCenter> initTableList()
    {
        JFXListView<HealthCenter> centerJFXListView = new JFXListView<>();
        List<HealthCenter> centers = Company.getCompany().getHealthCenters();

        centerJFXListView.setMinWidth(731);
        centerJFXListView.setMaxWidth(731);
        centerJFXListView.setMaxHeight(413);
        centerJFXListView.setMinHeight(413);
        centerJFXListView.getItems().addAll(centers);
        centerJFXListView.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        centerJFXListView.setCellFactory(lv -> new ListCell<HealthCenter>()
        {
            @Override
            protected void updateItem(HealthCenter item, boolean empty) {
                super.updateItem(item, empty);

                if(item != null)
                {
                    GridPane pane = new GridPane();
                    pane.setAlignment(Pos.CENTER_RIGHT);
                    pane.setHgap(40);
                    pane.setVgap(5);
                    pane.setStyle("-fx-background-radius: 10");

                    Text nameText = new Text("نام مرکز: " + item.getName());
                    nameText.setFont(new Font("IRANSans" , 17));
                    nameText.setFill(Color.WHITE);
                    GridPane.setHalignment(nameText , HPos.RIGHT);

//                    System.out.println(item.getDate());
                    Text dateText = new Text("تاریخ ماموریت: " + item.getDate());
                    dateText.setFont(new Font("IRANSans" , 15));
                    dateText.setFill(Color.WHITE);
                    GridPane.setHalignment(dateText , HPos.RIGHT);

                    Text cityText = new Text("شهر: " + item.getCity());
                    cityText.setFont(new Font("IRANSans" , 15));
                    cityText.setFill(Color.WHITE);
                    GridPane.setHalignment(cityText , HPos.RIGHT);

                    Text addressText = new Text("آدرس: " + item.getAddress());
                    addressText.setFont(new Font("IRANSans" , 14));
                    addressText.setFill(Color.WHITE);
                    GridPane.setHalignment(addressText , HPos.RIGHT);

                    final Button btn = new JFXButton("اطلاعات بیشتر...");
                    {
                        btn.setStyle("-fx-background-color: transparent;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: IRANSans;" +
                                "-fx-font-size: 13;" +
                                "-fx-min-width: 130");
                        btn.setMinWidth(70.0);
                        btn.setMaxWidth(70.0);
                        btn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event)
                            {
                                HealthCenter tempHealthCenter = item;
                                Company.getCompany().setMainHealthCenter(tempHealthCenter);

                                try {
                                    QCStructure.getStructure().setInputDevices(tempHealthCenter.id);
                                    QCStructure.getStructure().setCategoryDevices(tempHealthCenter.getInputDevices() , tempHealthCenter);
                                    QCStructure.getStructure().processDevices(tempHealthCenter.getInputDevices() , tempHealthCenter);
                                    QCStructure.getStructure().setMainHC_id(tempHealthCenter.id);
                                    UI_Scene newScene = new ProjectReportScene(1);
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
                        });

                    }

                    GridPane.setHalignment(btn , HPos.RIGHT);

                    pane.add(nameText , 1 , 0);
                    pane.add(dateText , 0 , 0);
                    pane.add(cityText , 1 , 1);
                    pane.add(addressText , 0 , 1);
                    pane.add(btn , 1 , 2);

                    if(empty)
                    {
                        setGraphic(null);
                    }
                    else
                    {
                        setGraphic(pane);
                    }
                }

            }


        });

        return centerJFXListView;
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

    @Override
    public void onStop() {
        return;
    }
}