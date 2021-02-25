package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import CompanyPackage.Operator;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainScene extends UI_Scene {
    private GridPane gridPane = new GridPane();
    private GridPane gridPane2 = new GridPane();
    private GridPane gridPane3 = new GridPane();
    private GridPane mainPane = new GridPane();

//    private final TableView<HealthCenter> table = new TableView<>();
//    private final ObservableList<HCUtility> data = FXCollections.observableArrayList();

    private Operator operator = Operator.getOperator();

    public MainScene() throws Exception {
        QCMap.getMap().setCurrentScene(this);
        QCStructure.getStructure().make_map();

        //        gridPane.setMinSize(500 , 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(40);
        gridPane.setHgap(20);
        gridPane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(gridPane, HPos.CENTER);

        gridPane2.setPadding(new Insets(10, 10, 10, 10));
        gridPane2.setVgap(40);
        gridPane2.setHgap(18);
        gridPane2.setAlignment(Pos.CENTER);
        GridPane.setHalignment(gridPane2, HPos.CENTER);


//        gridPane3.setPadding(new Insets(10 , 10 , 10 , 10));
//        gridPane3.setVgap(40);
        gridPane3.setHgap(40);
        gridPane3.setMinWidth(650);
        gridPane3.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setHalignment(gridPane3, HPos.RIGHT);

        mainPane.setMinSize(650, 650);
        mainPane.setVgap(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setBackground(new Background(QCConstantValues.background_fill));


        GridPane userPane = new GridPane();
        userPane.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setHalignment(userPane, HPos.RIGHT);
        userPane.setHgap(5);

        Image userLabelImage = new Image(new FileInputStream("Resources/userPanelIco.png"));
        ImageView userLabelImageView = new ImageView(userLabelImage);
        userLabelImageView.setFitWidth(70);
        userLabelImageView.setFitHeight(50);
        userLabelImageView.setPreserveRatio(true);
        GridPane.setHalignment(userLabelImageView, HPos.RIGHT);

        userPane.add(userLabelImageView, 1, 0);


        Button bgRectangle0 = new Button();
        bgRectangle0.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #50409a;\n" +
                "    -fx-border-radius: 20;\n" +
                "    -fx-background-color: #50409a;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 20;\n" +
                "-fx-min-width: 185.0;" +
                "-fx-min-height: 40");
        GridPane.setHalignment(bgRectangle0, HPos.RIGHT);

        userPane.add(bgRectangle0, 0, 0);

        Text userLabel = new Text();
        userLabel.setText("کاربر: " + operator.getFirst_name() + " " + operator.getLast_name());
        userLabel.setFont(new Font("IRANSans", 18));
        userLabel.setFill(Color.WHITE);
        GridPane.setHalignment(userLabel, HPos.CENTER);
        userPane.add(userLabel, 0, 0);

        gridPane3.add(userPane, 1, 0);

        ///////////////////////////////////////////////////////////


        GridPane searchPane = new GridPane();
        searchPane.setStyle("-fx-min-width: 450;" +
                "-fx-min-height: 40;" +
                "-fx-max-width: 450;" +
                "-fx-max-height: 40;" +
                "-fx-background-color: #050c15;" +
                "-fx-background-radius: 17;" +
                "-fx-border-radius: 17;" +
                "-fx-border-color: #50409a;" +
                "-fx-border-width: 2;");
        searchPane.setHgap(12);
        searchPane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(searchPane, HPos.LEFT);

        JFXTextField searchField = new JFXTextField();
        searchField.setPromptText("شماره سریال، نام بیمارستان یا شهر را جست‌وجو کنید.");
        searchField.setMinSize(370, 30);
        searchField.setAlignment(Pos.CENTER_RIGHT);
        searchField.setFocusColor(Color.TRANSPARENT);
        searchField.setStyle("     -fx-text-fill: white;\n" +
                "    -fx-prompt-text-fill: #afacac;\n" +
                "    -fx-font-family: IRANSans;\n" +
                "    -jfx-focus-color: TRANSPARENT;\n" +
                "    -jfx-unfocus-color: TRANSPARENT;");
        GridPane.setHalignment(searchField, HPos.RIGHT);
        searchPane.add(searchField, 1, 0);

        JFXButton searchBtn = new JFXButton();
        searchBtn.setStyle("-fx-background-color: TRANSPARENT;" +
                "-fx-background-radius: 22");

        Image searchImage = new Image(new FileInputStream("Resources/searchIco.png"));
        ImageView searchImageView = new ImageView(searchImage);
        searchImageView.setFitWidth(40);
        searchImageView.setFitHeight(30);
        searchImageView.setPreserveRatio(true);
        searchBtn.setGraphic(searchImageView);
        searchBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    QCMap.getMap().setSearchStr(searchField.getText());
                    QCStructure.getStructure().setDevices();
                    UI_Scene newScene = new SearchMainScene(searchField.getText());
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Search Result");
                    next_scene.centerOnScreen();
                    next_scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        searchPane.add(searchBtn, 0, 0);


        gridPane3.add(searchPane, 0, 0);

        ///////////////////////////////////////////////////////////

        Image bgRectangle = new Image(new FileInputStream("Resources/back.png"));
        ImageView bgRectangleView = new ImageView(bgRectangle);
        bgRectangleView.setFitWidth(353);
        bgRectangleView.setFitHeight(140);
        bgRectangleView.setPreserveRatio(true);

        gridPane.add(bgRectangleView, 1, 0);

        Text companyName = new Text();
        companyName.setFont(new Font("IRANSans", 30));
        companyName.setFill(Color.WHITE);
        companyName.setText("شرکت: \n" + Company.getCompany().getName());
        companyName.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(companyName, HPos.CENTER);
        gridPane.add(companyName, 1, 0);

        ///////////////////////////////////////////////////////////

        Image bgRectangle1 = new Image(new FileInputStream("Resources/back2.png"));
        ImageView bgRectangle1View = new ImageView(bgRectangle1);
        bgRectangle1View.setFitWidth(353);
        bgRectangle1View.setFitHeight(140);
        bgRectangle1View.setPreserveRatio(true);


        gridPane.add(bgRectangle1View, 0, 0);

        Text operatorStatue = new Text();
        operatorStatue.setText("آخرین ورود: \n" + QCMap.getMap().last_date);
        operatorStatue.setFont(new Font("IRANSans", 30));
        operatorStatue.setFill(Color.WHITE);
        operatorStatue.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(operatorStatue, HPos.CENTER);
        gridPane.add(operatorStatue, 0, 0);

        ///////////////////////////////////////////////////////////

        Button bgRectangle2 = new Button();
        bgRectangle2.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #FFC938;\n" +
                "    -fx-border-radius: 17;\n" +
                "    -fx-background-color: #0a1729;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 17;\n" +
                "-fx-min-width: 230.0;" +
                "-fx-min-height: 110.0");
        gridPane2.add(bgRectangle2, 0, 0);

        Text devicesCount = new Text();
        devicesCount.setText("دستگاه‌های ثبت‌شده: " + getSubmittedDevicesSize());
        devicesCount.setFont(new Font("IRANSans", 19));
        devicesCount.setFill(Color.WHITE);
        GridPane.setHalignment(devicesCount, HPos.CENTER);
        gridPane2.add(devicesCount, 0, 0);


        ///////////////////////////////////////////////////////////

        Button bgRectangle3 = new Button();
        bgRectangle3.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #22c715;\n" +
                "    -fx-border-radius: 17;\n" +
                "    -fx-background-color: #0a1729;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 17;\n" +
                "-fx-min-width: 230.0;" +
                "-fx-min-height: 110");
        gridPane2.add(bgRectangle3, 1, 0);

        Text hcCounts = new Text();
        hcCounts.setText("تعداد مراکز ثبت‌شده: " + Company.getCompany().getHealthCenters().size());
        hcCounts.setFont(new Font("IRANSans", 19));
        hcCounts.setFill(Color.WHITE);
        GridPane.setHalignment(hcCounts, HPos.CENTER);
        gridPane2.add(hcCounts, 1, 0);


        ///////////////////////////////////////////////////////////

        Button bgRectangle4 = new Button();
        bgRectangle4.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #FF5245;\n" +
                "    -fx-border-radius: 17;\n" +
                "    -fx-background-color: #0a1729;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 17;\n" +
                "-fx-min-width: 230.0;" +
                "-fx-min-height: 110");
        gridPane2.add(bgRectangle4, 2, 0);

        Text categoryDevicesCount = new Text();
        categoryDevicesCount.setText("تعداد دستگاه‌های شرکت: " + Company.getCompany().getDeviceNames().size());
        categoryDevicesCount.setFont(new Font("IRANSans", 19));
        categoryDevicesCount.setFill(Color.WHITE);
        GridPane.setHalignment(categoryDevicesCount, HPos.CENTER);
        gridPane2.add(categoryDevicesCount, 2, 0);


        //////////////////////////////////////////////////////////

//        initTable();
/*        JFXTreeTableView demoDataTable = initTable();
        GridPane.setHalignment(demoDataTable , HPos.CENTER);*/

        JFXListView demoList = initTableList();
        GridPane.setHalignment(demoList, HPos.CENTER);

        mainPane.add(gridPane3, 0, 0);
        mainPane.add(gridPane, 0, 1);
        mainPane.add(gridPane2, 0, 2);
        mainPane.add(demoList, 0, 3);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(mainPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        GridPane panel = new UiUtility().setPanel();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.add(panel, 1, 0);
        pane.add(mainPane, 0, 0);
        pane.setHgap(10);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000, 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }


    public int getSubmittedDevicesSize() throws IOException {
        QCStructure.getStructure().setDevices();
        int size = Company.getCompany().getSubmitedDevices().size();
        Company.getCompany().setSubmittedDevices(null);
        return size;
    }


    private JFXListView<HealthCenter> initTableList()
    {
        JFXListView<HealthCenter> centerJFXListView = new JFXListView<>();
        List<HealthCenter> centers = Company.getCompany().getHealthCenters();
        Collections.reverse(centers);

        centerJFXListView.setMinWidth(723);
        centerJFXListView.setMaxWidth(723);
        centerJFXListView.setMaxHeight(215);
        centerJFXListView.setMinHeight(215);

        if (centers.size() < 3)
            centerJFXListView.getItems().addAll(centers);
        else
            centerJFXListView.getItems().addAll(centers.subList(0,3));

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
                    pane.setHgap(80);
                    pane.setVgap(5);

                    Text nameText = new Text("نام مرکز: " + item.getName());
                    nameText.setFont(new Font("IRANSans" , 17));
                    nameText.setFill(Color.WHITE);
                    GridPane.setHalignment(nameText , HPos.RIGHT);

                    Text dateText = new Text("تاریخ ماموریت: " + item.getDate());
                    dateText.setFont(new Font("IRANSans" , 15));
                    dateText.setFill(Color.WHITE);
                    GridPane.setHalignment(dateText , HPos.RIGHT);

                    Text cityText = new Text("شهر: " + item.getCity());
                    cityText.setFont(new Font("IRANSans" , 15));
                    cityText.setFill(Color.WHITE);
                    GridPane.setHalignment(cityText , HPos.RIGHT);

                    Text addressText = new Text("آدرس:" + item.getAddress());
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

    @Override
    public void onStop() {
        return;
    }

}