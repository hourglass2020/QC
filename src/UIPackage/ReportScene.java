package UIPackage;

import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import DeviecPackage.Device;
import Utilities.DoughnutChart;
import Utilities.Utils;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class ReportScene extends UI_Scene
{
    private Company company = Company.getCompany();
    private HealthCenter mainHealthCenter = company.getMainHealthCenter();

    private GridPane mainPane = new GridPane();
    private GridPane hbButtons = new GridPane();

    private GridPane gridPane = new GridPane();
    private JFXScrollPane scrollPane = new JFXScrollPane();

    private GridPane gridPane2 = new GridPane();
    private GridPane gridPane3 = new GridPane();
    private GridPane gridPane4 = new GridPane();

    Text title_text;
    Image titleImage;
    ImageView titleImageView;

    Button btnHome = new JFXButton("اتمام");
    Button btnBack = new JFXButton("بازگشت");
    Button btnReport = new JFXButton("گزارش");

    ///////////////////////////////////////////////////////////////

    private PieChart Result_Chart;

    private DirectoryChooser fileChooser;
    private File selectedFile;

    private ObservableList<String> chartDevicesData;
    private ObservableList<PieChart.Data> pieChartResultData;

    public ReportScene() throws IOException
    {
//        scrollPane.setMinWidth(320);
//        scrollPane.setMaxWidth(540);
//        scrollPane.setMaxHeight(450);
        scrollPane.setContent(mainPane);
        scrollPane.getStylesheets().add(this.getClass().getResource("UI_Styles/ScrollPane.css").toExternalForm());

        mainPane.setBackground(new Background(QCConstantValues.background_fill));

        init();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(scrollPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        current_scene = new Scene(scrollPane, 1000, 650);
        current_scene.setFill(QCConstantValues.MAIN_SCREEN_COLOR);
    }

    private void init() throws FileNotFoundException
    {
        gridPane.setHgap(50);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #0f233e");

        gridPane2.setMinWidth(500);
        gridPane2.setHgap(50);
        gridPane2.setAlignment(Pos.CENTER);
        gridPane2.setStyle("-fx-background-color: #0f233e");

        gridPane3.setHgap(50);
        gridPane3.setAlignment(Pos.CENTER);
        gridPane3.setStyle("-fx-background-color: #0f233e");

        gridPane4.setHgap(50);
        gridPane4.setAlignment(Pos.CENTER);
        gridPane4.setStyle("-fx-background-color: #0f233e");

        mainPane.setAlignment(Pos.CENTER);
        mainPane.setVgap(30);

        GridPane titlePane = new GridPane();
        titlePane.setHgap(10);
        titlePane.setAlignment(Pos.CENTER);

        title_text = new Text();
        title_text.setFont(new Font("IRANSans", 35));
        title_text.setText("گزارش نهایی پروژه");
        title_text.setFill(Color.WHITE);

        titleImage = new Image(new FileInputStream("Resources/ReportIco.png"));
        titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(120.0);
        titleImageView.setFitHeight(80.0);
        titleImageView.setPreserveRatio(true);

        titlePane.add(title_text , 0 , 0);
        titlePane.add(titleImageView , 1 , 0);


        initResultTable();
        initResultChart();
        initDevicesChart();

        VBox vBox = new VBox();

        btnBack.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnBack.setOnMouseClicked(new BackBtnEvent());

        btnHome.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());
        btnHome.setOnMouseClicked(new SendingDataBtnEvent());

        btnReport.setOnMouseClicked(new GetReportBtnEvent());
        btnReport.getStylesheets().add(this.getClass().getResource(QCConstantValues.DARK_THEME).toExternalForm());


        hbButtons.setHgap(25.0);
        hbButtons.add(btnBack, 0 , 0);
        hbButtons.add(btnReport, 1 , 0);
        hbButtons.add(btnHome, 2 , 0);
        hbButtons.setAlignment(Pos.CENTER);

        hbButtons.setPrefWidth(0);
        hbButtons.setPrefHeight(0);
        vBox.getChildren().addAll(titlePane , hbButtons);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);
        scrollPane.getMidBar().getChildren().add(vBox);
        scrollPane.getMidBar().setAlignment(Pos.BASELINE_CENTER);

        ////////////////////////////////////////////////

    }

    private void initResultTable()
    {
        Button bgRectangle = new Button();
        bgRectangle.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #e2e2e2;\n" +
                "    -fx-border-radius: 25;\n" +
                "    -fx-background-color: #0f233e;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 25;\n" +
                "-fx-min-width: 230.0;");

        gridPane.add(bgRectangle , 0 , 0);

        Label projectName = new Label();
        projectName.setText("    Name: ");
        projectName.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 12;");
        GridPane.setHalignment(projectName, HPos.LEFT);
        gridPane.add(projectName , 0 , 0);

        Label projectNameVal = new Label();
        projectNameVal.setText(mainHealthCenter.getName());
        projectNameVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 13;");

        GridPane.setHalignment(projectNameVal, HPos.CENTER);
        gridPane.add(projectNameVal , 0 , 0);

        ///////////////////////////////////////////////////////////

        Button bgRectangle1 = new Button();
        bgRectangle1.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #e2e2e2;\n" +
                "    -fx-border-radius: 25;\n" +
                "    -fx-background-color: #0f233e;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 25;\n" +
                "-fx-min-width: 230.0;");
        gridPane.add(bgRectangle1 , 1 , 0);

        Label projectDate = new Label();
        projectDate.setText("    Date: ");
        projectDate.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 12;");
        GridPane.setHalignment(projectDate, HPos.LEFT);
        gridPane.add(projectDate , 1 , 0);

        Label projectDateVal = new Label();
        projectDateVal.setText(mainHealthCenter.getDate());
        projectDateVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 13;");

        GridPane.setHalignment(projectDateVal, HPos.CENTER);
        gridPane.add(projectDateVal , 1 , 0);

        ///////////////////////////////////////////////////////////

        Button bgRectangle13 = new Button();
        bgRectangle13.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #e2e2e2;\n" +
                "    -fx-border-radius: 25;\n" +
                "    -fx-background-color: #0f233e;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 25;\n" +
                "-fx-min-width: 230.0;");
        gridPane.add(bgRectangle13 , 2 , 0);

        Label projectCity = new Label();
        projectCity.setText("    City: ");
        projectCity.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 12;");
        GridPane.setHalignment(projectCity, HPos.LEFT);
        gridPane.add(projectCity , 2 , 0);

        Label projectCityVal = new Label();
        projectCityVal.setText(mainHealthCenter.getCity());
        projectCityVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 13;");

        GridPane.setHalignment(projectCityVal, HPos.CENTER);
        gridPane.add(projectCityVal , 2 , 0);

        ///////////////////////////////////////////////////////////
        mainPane.add(gridPane, 0 , 1);
        ///////////////////////////////////////////////////////////

        Button bgRectangle2 = new Button();
        bgRectangle2.setStyle("    -fx-padding: 5 22 5 22;\n" +
            "    -fx-border-color: #e2e2e2;\n" +
            "    -fx-border-radius: 25;\n" +
            "    -fx-background-color: #0f233e;\n" +
            "    -fx-border-width: 1.5;\n" +
            "    -fx-background-radius: 25;\n" +
            "    -fx-min-width: 790.0;");
        gridPane2.add(bgRectangle2 , 0 , 0);

        Label projectAddress = new Label();
        projectAddress.setText("   Address: ");
        projectAddress.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 12;");
        GridPane.setHalignment(projectAddress, HPos.LEFT);
        gridPane2.add(projectAddress , 0 , 0);

        Label projectAddressVal = new Label();
        projectAddressVal.setText(mainHealthCenter.getAddress());
        projectAddressVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 13; -fx-max-height: 50.0;");

        GridPane.setHalignment(projectAddressVal, HPos.CENTER);
        gridPane2.add(projectAddressVal , 0 , 0);

        ///////////////////////////////////////////////////////////
        mainPane.add(gridPane2, 0 , 2);
        ///////////////////////////////////////////////////////////

        Button bgRectangle3 = new Button();
        bgRectangle3.setStyle("    -fx-padding: 5 22 5 22;\n" +
            "    -fx-border-color: #e2e2e2;\n" +
            "    -fx-border-radius: 25;\n" +
            "    -fx-background-color: #229918;\n" +
            "    -fx-border-width: 1.5;\n" +
            "    -fx-background-radius: 25;\n" +
            "-fx-min-width: 230.0;");
        gridPane3.add(bgRectangle3 , 0 , 0);

        Label projectPass = new Label();
        projectPass.setText("    Pass: ");
        projectPass.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 12;");
        GridPane.setHalignment(projectPass, HPos.LEFT);
        gridPane3.add(projectPass , 0 , 0);

        Label projectPassVal = new Label();
        projectPassVal.setText(String.valueOf(mainHealthCenter.pass));
        projectPassVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 13;");

        GridPane.setHalignment(projectPassVal, HPos.CENTER);
        gridPane3.add(projectPassVal , 0 , 0);

        ///////////////////////////////////////////////////////////

        Button bgRectangle4 = new Button();
        bgRectangle4.setStyle("    -fx-padding: 5 22 5 22;\n" +
            "    -fx-border-color: #e2e2e2;\n" +
            "    -fx-border-radius: 25;\n" +
            "    -fx-background-color: #c61414;\n" +
            "    -fx-border-width: 1.5;\n" +
            "    -fx-background-radius: 25;\n" +
            "-fx-min-width: 230.0;");
        gridPane3.add(bgRectangle4 , 1 , 0);

        Label projectFail = new Label();
        projectFail.setText("    Fail: ");
        projectFail.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 12;");
        GridPane.setHalignment(projectFail, HPos.LEFT);
        gridPane3.add(projectFail , 1 , 0);

        Label projectFailVal = new Label();
        projectFailVal.setText(String.valueOf(mainHealthCenter.fail));
        projectFailVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: white ; -fx-font-size: 13;");

        GridPane.setHalignment(projectFailVal, HPos.CENTER);
        gridPane3.add(projectFailVal , 1 , 0);

        ///////////////////////////////////////////////////////////

        Button bgRectangle5 = new Button();
        bgRectangle5.setStyle("    -fx-padding: 5 22 5 22;\n" +
                "    -fx-border-color: #e2e2e2;\n" +
                "    -fx-border-radius: 25;\n" +
                "    -fx-background-color: #fdc600;\n" +
                "    -fx-border-width: 1.5;\n" +
                "    -fx-background-radius: 25;\n" +
                "-fx-min-width: 230.0;");
        gridPane3.add(bgRectangle5 , 2 , 0);

        Label projectCond = new Label();
        projectCond.setText("    Conditional: ");
        projectCond.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: black ; -fx-font-size: 12;");
        GridPane.setHalignment(projectCond, HPos.LEFT);
        gridPane3.add(projectCond , 2 , 0);

        Label projectCondVal = new Label();
        projectCondVal.setText(String.valueOf(mainHealthCenter.cond));
        projectCondVal.setStyle("-fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;" +
                " -fx-text-fill: black ; -fx-font-size: 13;");

        GridPane.setHalignment(projectCondVal, HPos.CENTER);
        gridPane3.add(projectCondVal , 2 , 0);

        ///////////////////////////////////////////////////////////
        mainPane.add(gridPane3, 0 , 3);
        ///////////////////////////////////////////////////////////
    }

    private void initResultChart()
    {
        QCMap.getMap().setCurrentScene(this);

        pieChartResultData = FXCollections.observableArrayList();

        pieChartResultData.add(new PieChart.Data("Fail" , mainHealthCenter.fail));
        pieChartResultData.add(new PieChart.Data("Cond" , mainHealthCenter.cond));
        pieChartResultData.add(new PieChart.Data("Pass" , mainHealthCenter.pass));

        Result_Chart = new DoughnutChart(pieChartResultData);
//        Result_Chart.setTitle("Result");
        Result_Chart.setClockwise(true);
        Result_Chart.setLabelLineLength(50);
        Result_Chart.setLabelsVisible(true);
        Result_Chart.setStartAngle(57);
        Result_Chart.setMinSize(250, 380);
        Result_Chart.prefWidth(0);
        Result_Chart.setLegendSide(Side.LEFT);
        Result_Chart.getStylesheets().add(this.getClass().getResource("UI_Styles/PieChart.css").toExternalForm());
        GridPane.setHalignment(Result_Chart , HPos.CENTER);

        mainPane.add(Result_Chart , 0 , 4);

    }

    private void initDevicesChart()
    {
        chartDevicesData = FXCollections.observableArrayList();

        for(Device device : mainHealthCenter.getCategory_devices())
        {
            chartDevicesData.add(device.getEngNameOfDevice());
        }

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Devices");
        xAxis.setCategories(chartDevicesData);
        yAxis.setLabel("Value");

        final StackedBarChart<String,Number> stackedBarChart = new StackedBarChart<String,Number>(xAxis,yAxis);
        stackedBarChart.setTitle("");
        stackedBarChart.setMinHeight(570);
        stackedBarChart.setMaxHeight(570);

        XYChart.Series<String,Number> series1 = new XYChart.Series();
        series1.setName("Fail");

        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            series1.getData().add(new XYChart.Data(device.getEngNameOfDevice() , device.fail));
        }

        XYChart.Series<String,Number> series2 = new XYChart.Series();
        series2.setName("Cond");

        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            series2.getData().add(new XYChart.Data(device.getEngNameOfDevice() , device.cond));
        }

        XYChart.Series<String,Number> series3 = new XYChart.Series();
        series3.setName("Pass");

        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            series3.getData().add(new XYChart.Data(device.getEngNameOfDevice() , device.pass));
        }

        stackedBarChart.getData().addAll(series1, series2 , series3);
        stackedBarChart.getStylesheets().add(this.getClass().getResource("UI_Styles/BarChart.css").toExternalForm());
        mainPane.add(stackedBarChart , 0 , 5);
    }

    private class BackBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                ResultListScene newScene = new ResultListScene();
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
    }

    private class SendingDataBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event) {
            try {
                UI_Scene newScene = new SendingDataScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Finishing Process");
                next_scene.centerOnScreen();
                next_scene.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetReportBtnEvent implements EventHandler
    {
        @Override
        public void handle(Event event)
        {
            formAlert();
        }
    }

    private class GetSheetReportBtnEvent implements EventHandler
    {
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
                utils.get_excel(selectedFile.getAbsoluteFile());
            } catch (IOException ex) {
                return;
//                Logger.getLogger(ReportScene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class GetComplementaryReportBtnEvent implements EventHandler
    {
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
                utils.get_AllDocumentations(selectedFile.getAbsoluteFile());
            } catch (Exception ex) {
                return;
//                Logger.getLogger(ReportScene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class GetIdentificationDocBtnEvent implements EventHandler
    {
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
                utils.get_DevicesIdentification(selectedFile.getAbsoluteFile());
            } catch (Exception ex) {
                return;
//                Logger.getLogger(ReportScene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void formAlert()
    {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Report");
        dialog.setHeaderText("");
        dialog.getDialogPane().setBackground(new Background(QCConstantValues.background_fill));
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

        try {
            stage.getIcons().add(new Image(new FileInputStream("Resources/QC.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ButtonType setButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(setButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Text titleText = new Text("نوع گزارشدهی خود را انتخاب کنید: ");
        titleText.setFont(new Font("IRANSans",18));
        titleText.setFill(Color.WHITE);
        GridPane.setHalignment(titleText , HPos.RIGHT);

        Text btnComplementaryReport = new Text("گزارش‌های تکمیلی");
        GridPane.setHalignment(btnComplementaryReport , HPos.RIGHT);
        btnComplementaryReport.setFill(Color.WHITE);
        btnComplementaryReport.setFont(new Font("IRANSans",18));
        btnComplementaryReport.setOnMouseClicked(new GetComplementaryReportBtnEvent());

        Text btnGetSheet = new Text("گزارش اجمالی");
        GridPane.setHalignment(btnGetSheet , HPos.RIGHT);
        btnGetSheet.setFill(Color.WHITE);
        btnGetSheet.setFont(new Font("IRANSans",18));
        btnGetSheet.setOnMouseClicked(new GetSheetReportBtnEvent());

        Text btnGetId = new Text("شناسنامه‌های دستگاه‌های ثبت شده");
        GridPane.setHalignment(btnGetId , HPos.RIGHT);
        btnGetId.setFill(Color.WHITE);
        btnGetId.setFont(new Font("IRANSans",18));
        btnGetId.setOnMouseClicked(new GetIdentificationDocBtnEvent());

        grid.add(titleText , 0 , 0);

        GridPane tempGrid = new GridPane();
        tempGrid.setHgap(10);
        tempGrid.setVgap(10);
        tempGrid.setPadding(new Insets(20, 15, 10, 10));
        tempGrid.setMinSize(300, 100);
        tempGrid.setAlignment(Pos.CENTER);

        tempGrid.add(btnComplementaryReport, 0, 0);
        tempGrid.add(btnGetSheet, 0, 1);
        tempGrid.add(btnGetId, 0, 2);

        grid.add(tempGrid , 0 , 1);
        grid.setAlignment(Pos.CENTER_RIGHT);

        Node setButton = dialog.getDialogPane().lookupButton(setButtonType);
        setButton.setDisable(false);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == setButtonType) {
                return new Pair<>(null , null);
            }
            return null;
        });

        dialog.showAndWait();

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
            try{
                QCStructure.getStructure().removeCurrentHC();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            company.setMainHealthCenter(null);
        }
    }


}
