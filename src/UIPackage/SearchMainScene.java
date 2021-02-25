package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import DeviecPackage.Device;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchMainScene extends UI_Scene
{
    Company company = Company.getCompany();

    GridPane mainPane = new GridPane();
    JFXTabPane tabPane = new JFXTabPane();
    Tab deviceTab = new Tab();
    Tab hcTab = new Tab();

//    private final ObservableList<HCUtility> hcData = FXCollections.observableArrayList();
//    private final ObservableList<DeviceUtility> deviceData = FXCollections.observableArrayList();


    String searchStr;


    public SearchMainScene(String searchStr) throws IOException
    {
        QCMap.getMap().setCurrentScene(this);
        this.searchStr = searchStr;

        init();

        mainPane.add(tabPane, 0, 1);

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
        pane.setHgap(20);
        pane.setBackground(new Background(QCConstantValues.background_fill));

        current_scene = new Scene(pane, 1000 , 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);

    }

    private void init() throws IOException
    {
        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER_RIGHT);

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(40);
        mainPane.setMinSize(680 , 650);

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

        ////////////////////////////////////////////////////////

        tabPane.setMinSize(680 , 500);
        tabPane.getStylesheets().add(this.getClass().getResource("UI_Styles/TabPane.css").toExternalForm());

        deviceTab.setText("Devices");
        deviceTab.setContent(initDeviceTableList());

        hcTab.setText("Health Centers");
        hcTab.setContent(initHC_TableList());

        tabPane.getTabs().add(deviceTab);
        tabPane.getTabs().add(hcTab);
    }



/*    private JFXTreeTableView init_HC_Table() throws IOException
    {
        JFXTreeTableColumn<HCUtility, String> nameColumn = new JFXTreeTableColumn<>("Health Center");
        nameColumn.setPrefWidth(120);
        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<HCUtility, String> param) -> {

            if (nameColumn.validateValue(param)) {
                return param.getValue().getValue().center_name;
            } else {
                return nameColumn.getComputedValue(param);
            }
        });


        JFXTreeTableColumn<HCUtility, String> dateColumn = new JFXTreeTableColumn<>("Date");
        dateColumn.setPrefWidth(110);
        dateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<HCUtility, String> param) -> {

            if (dateColumn.validateValue(param)) {
                return param.getValue().getValue().date;
            } else {
                return dateColumn.getComputedValue(param);
            }
        });


        JFXTreeTableColumn<HCUtility, String> cityColumn = new JFXTreeTableColumn<>("City");
        cityColumn.setPrefWidth(110);
        cityColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<HCUtility, String> param) -> {

            if (cityColumn.validateValue(param)) {
                return param.getValue().getValue().city;
            } else {
                return cityColumn.getComputedValue(param);
            }
        });


        JFXTreeTableColumn<HCUtility, String> addressColumn = new JFXTreeTableColumn<>("Address");
        addressColumn.setPrefWidth(285);
        addressColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<HCUtility, String> param) -> {

            if (addressColumn.validateValue(param)) {
                return param.getValue().getValue().address;
            } else {
                return addressColumn.getComputedValue(param);
            }
        });


        JFXTreeTableColumn<HCUtility, String> accessColumn = new JFXTreeTableColumn<>("Access");
        accessColumn.setPrefWidth(90);
        Callback<TreeTableColumn<HCUtility, String>, TreeTableCell<HCUtility, String>> cellFactory =  new Callback<TreeTableColumn<HCUtility, String>, TreeTableCell<HCUtility, String>>() {
            @Override
            public TreeTableCell<HCUtility, String> call(TreeTableColumn<HCUtility, String> param) {
                final TreeTableCell<HCUtility, String> cell = new TreeTableCell<HCUtility, String>()
                {
                    private final Button btn = new Button("access");
                    {
                        btn.setStyle("-fx-background-color: #0f233e;" +
                                "-fx-text-fill: white;");
                        btn.setMinWidth(70.0);
                        btn.setMaxWidth(70.0);
                        btn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event)
                            {
                                HealthCenter tempHealthCenter = getTreeTableView().getTreeItem(getIndex()).getValue().center;
                                Company.getCompany().setMainHealthCenter(tempHealthCenter);

                                try {
                                    QCStructure.getStructure().setInputDevices(tempHealthCenter.id);
                                    QCStructure.getStructure().setCategoryDevices(tempHealthCenter.getInputDevices() , tempHealthCenter);
                                    QCStructure.getStructure().processDevices(tempHealthCenter.getInputDevices() , tempHealthCenter);
                                    QCStructure.getStructure().setMainHC_id(tempHealthCenter.id);
                                    UI_Scene newScene = new ProjectReportScene(0);
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

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        accessColumn.setCellFactory(cellFactory);

        final TreeItem<HCUtility> root = new RecursiveTreeItem<>(hcData, RecursiveTreeObject::getChildren);

        JFXTreeTableView<HCUtility> treeView = new JFXTreeTableView<HCUtility>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.editableProperty().setValue(false);
        treeView.getColumns().setAll(nameColumn, dateColumn, cityColumn, addressColumn , accessColumn);
        treeView.setMinHeight(430);
        treeView.setMaxHeight(430);
        treeView.setMinWidth(731);
        treeView.setMaxWidth(731);
        treeView.getStylesheets().add(this.getClass().getResource("TableView.css").toExternalForm());
        fill_HC_TableObservableList();


        return treeView;
    }


    private void fill_HC_TableObservableList() throws IOException
    {
        QCStructure.getStructure().setCenters();
        List<HealthCenter> centers = Company.getCompany().getHealthCenters();

        if (centers.size() == 0)
            return;

        int count = 0;

        for(HealthCenter healthCenter : centers)
        {
            if(healthCenter.getName().equalsIgnoreCase(searchStr) ||
                healthCenter.getCity().equalsIgnoreCase(searchStr))
            {
                hcData.add(new HCUtility(healthCenter));
                count++;
            }
        }
    }


    public class HCUtility extends RecursiveTreeObject<HCUtility>
    {
        final HealthCenter center;
        final StringProperty center_name;
        final StringProperty date;
        final StringProperty city;
        final StringProperty address;
        final int id;

        HCUtility(HealthCenter center)
        {
            this.center = center;
            this.center_name = new SimpleStringProperty(center.getName());
            this.date = new SimpleStringProperty(DateUtilities.getDate(center.getDate().substring(0,11)));
            this.city = new SimpleStringProperty(center.getCity());
            this.address = new SimpleStringProperty(center.getAddress());
            this.id = center.id;
        }
    }*/


    private JFXListView<HealthCenter> initHC_TableList()
    {
        JFXListView<HealthCenter> centerJFXListView = new JFXListView<>();
        List<HealthCenter> centers = new ArrayList<>();

        for(HealthCenter center : Company.getCompany().getHealthCenters())
        {
            if(center.getName().contains(searchStr) ||
                    center.getCity().equalsIgnoreCase(searchStr))
            {
                centers.add(center);
            }
        }

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

    private JFXListView<Device> initDeviceTableList()
    {
        JFXListView<Device> centerJFXListView = new JFXListView<>();
        List<Device> devices = new ArrayList<>();

        for(Device device : Company.getCompany().getSubmitedDevices())
        {
            if(device.getSerialNo().contains(searchStr))
            {
                devices.add(device);
                for (HealthCenter healthCenter : Company.getCompany().getHealthCenters())
                {
                    if (healthCenter.id == device.getOwner()) {
                        device.setTempCenter(healthCenter);
                    }
                }
            }
        }

        centerJFXListView.setMinWidth(731);
        centerJFXListView.setMaxWidth(731);
        centerJFXListView.setMaxHeight(413);
        centerJFXListView.setMinHeight(413);
        centerJFXListView.getItems().addAll(devices);
        centerJFXListView.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        centerJFXListView.setCellFactory(lv -> new ListCell<Device>()
        {
            @Override
            protected void updateItem(Device item, boolean empty) {
                super.updateItem(item, empty);

                if(item != null)
                {
                    GridPane pane = new GridPane();
                    pane.setAlignment(Pos.CENTER_RIGHT);
                    pane.setHgap(40);
                    pane.setVgap(5);
                    pane.setStyle("-fx-background-radius: 10");

                    Text nameText = new Text("اسم دستگاه: " + item.getPerNameOfDevice());
                    nameText.setFont(new Font("IRANSans" , 17));
                    nameText.setFill(Color.WHITE);
                    GridPane.setHalignment(nameText , HPos.RIGHT);

//                    System.out.println(item.getDate());
                    Text serialText = new Text("شماره سریال: " + item.getSerialNo());
                    serialText.setFont(new Font("IRANSans" , 15));
                    serialText.setFill(Color.WHITE);
                    GridPane.setHalignment(serialText , HPos.RIGHT);

                    Text resultText = new Text("نتیجه آزمون: " + item.getResult());
                    resultText.setFont(new Font("IRANSans" , 15));
                    resultText.setFill(Color.WHITE);
                    GridPane.setHalignment(resultText , HPos.RIGHT);

                    Text centerNameText = new Text("مرکز: " + item.getTempCenter().getName());
                    centerNameText.setFont(new Font("IRANSans" , 15));
                    centerNameText.setFill(Color.WHITE);
                    GridPane.setHalignment(centerNameText , HPos.RIGHT);

                    Text dateText = new Text("تاریخ ماموریت: " + item.getTempCenter().getDate());
                    dateText.setFont(new Font("IRANSans" , 14));
                    dateText.setFill(Color.WHITE);
                    GridPane.setHalignment(dateText , HPos.RIGHT);

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
                                company.setMainDevice(item);
                                company.setMainHealthCenter(item.getTempCenter());
                                UI_Scene newScene = null;
                                try {
                                    newScene = new SearchDeviceScene();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                                next_scene.setTitle("Result List");
                                next_scene.centerOnScreen();
                                next_scene.show();
                            }
                        });

                    }

                    GridPane.setHalignment(btn , HPos.RIGHT);

                    pane.add(nameText , 1 , 0);
                    pane.add(serialText , 0 , 0);
                    pane.add(centerNameText , 1 , 1);
                    pane.add(dateText , 0 , 1);
                    pane.add(resultText , 1 , 2);
                    pane.add(btn , 1 , 3);

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
        company.setSubmittedDevices(null);
    }
}
