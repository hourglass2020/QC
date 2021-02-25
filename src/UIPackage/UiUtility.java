package UIPackage;

import BackEndPackage.NewsItem;
import BackEndPackage.QCConstantValues;
import BackEndPackage.QCMap;
import BackEndPackage.QCStructure;
import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import DeviecPackage.Attribute;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static javafx.scene.paint.Color.WHITE;

public class UiUtility
{

    public GridPane setPanel() throws FileNotFoundException
    {
        GridPane pane = new GridPane();
//        pane.setVgap(25.0);
        pane.setMinWidth(230);
        pane.setMaxWidth(230);
        pane.setMinHeight(650);
        pane.setMaxHeight(650);

        pane.setBackground(new Background(QCConstantValues.background_fill2));
        pane.setAlignment(Pos.TOP_CENTER);
        GridPane.setHalignment(pane , HPos.CENTER);
        Double fontSize = 18.0;
        Double WIDTH = 50.0;
        Double HEIGHT = 35.0;
        String fontName = "Segoe Print";


        //////////////////////////////////////////////////

        GridPane temp_pane = new GridPane();
        temp_pane.setVgap(15.0);
        temp_pane.setMinWidth(230);
        temp_pane.setMaxWidth(230);
        temp_pane.setMinHeight(250);
        temp_pane.setBackground(new Background(QCConstantValues.background_fill2));
        temp_pane.setAlignment(Pos.CENTER);

        Image Logo_image = new Image(new FileInputStream
                ("Resources/QCPanel.png"));

        ImageView Logo_imageView = new ImageView(Logo_image);
        GridPane.setHalignment(Logo_imageView, HPos.CENTER);
        Logo_imageView.setFitWidth(110);
        Logo_imageView.setFitHeight(110);
        Logo_imageView.setPreserveRatio(true);
        temp_pane.add(Logo_imageView , 0 , 0);


        //////////////////////////////////////////////////


        Text companyLabel = new Text();
        companyLabel.setFont(new Font("REVOLUTION",fontSize + 8));
        companyLabel.setText("QC Program");
        companyLabel.setFill(Color.WHITE);
        GridPane.setHalignment(companyLabel , HPos.CENTER);
        temp_pane.add(companyLabel , 0 , 1);

        Text newsNotify = new Text();
        newsNotify.setFont(new Font("IRANSans",fontSize));
        newsNotify.setText("صندوق پیام‌ها");
        newsNotify.setFill(QCConstantValues.TITLE_COLOR_DEFAULT);
        newsNotify.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    formAlert();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GridPane.setHalignment(newsNotify , HPos.CENTER);
        temp_pane.add(newsNotify , 0 , 2);

        pane.add(temp_pane , 0 , 0);

        //////////////////////////////////////////////////


        Button homeLabel = new JFXButton();
        homeLabel.setFont(new Font(fontName,fontSize ));
        homeLabel.setText(" صفحه اصلی ");
        homeLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        homeLabel.setTextFill(Color.WHITE);
        homeLabel.setContentDisplay(ContentDisplay.RIGHT);
        homeLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    QCMap.getMap().getCurrentScene().onStop();
                    UI_Scene newScene = new MainScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Home Page");
                    next_scene.centerOnScreen();
                    next_scene.show();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        homeLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        GridPane.setHalignment(homeLabel , HPos.CENTER);

        Image homeLabelImage = new Image(new FileInputStream("Resources/Home.png"));
        ImageView homeLabelImageView = new ImageView(homeLabelImage);
        homeLabelImageView.setFitWidth(WIDTH);
        homeLabelImageView.setFitHeight(HEIGHT);
        homeLabelImageView.setPreserveRatio(true);

        homeLabel.setGraphic(homeLabelImageView);

        pane.add(homeLabel , 0 , 1);


        //////////////////////////////////////////////////


        Button newProjectLabel = new JFXButton();
        newProjectLabel.setFont(new Font(fontName,fontSize ));
        newProjectLabel.setText(" پروژه جدید ");
        newProjectLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        newProjectLabel.setTextFill(WHITE);
        newProjectLabel.setContentDisplay(ContentDisplay.RIGHT);
        newProjectLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    QCMap.getMap().getCurrentScene().onStop();
                    UI_Scene newScene = new NewProjectScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("New Project");
                    next_scene.centerOnScreen();
                    next_scene.show();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        newProjectLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        GridPane.setHalignment(newProjectLabel , HPos.CENTER);

        Image newProjectLabelImage = new Image(new FileInputStream("Resources/newProjectPanelIco.png"));
        ImageView newProjectLabelImageView = new ImageView(newProjectLabelImage);
        newProjectLabelImageView.setFitWidth(WIDTH);
        newProjectLabelImageView.setFitHeight(HEIGHT);
        newProjectLabelImageView.setPreserveRatio(true);

        newProjectLabel.setGraphic(newProjectLabelImageView);

        pane.add(newProjectLabel , 0 , 2);


        //////////////////////////////////////////////////


        Button openProjectLabel = new JFXButton();
        openProjectLabel.setFont(new Font(fontName,fontSize ));
        openProjectLabel.setText(" دسترسی به پروژه‌ها ");
        openProjectLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        openProjectLabel.setTextFill(Color.WHITE);
        openProjectLabel.setContentDisplay(ContentDisplay.RIGHT);
        openProjectLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    QCMap.getMap().getCurrentScene().onStop();
                    OpenProjectScene newScene = new OpenProjectScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Open Project");
                    next_scene.centerOnScreen();
                    next_scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        openProjectLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        GridPane.setHalignment(openProjectLabel , HPos.CENTER);

        Image openProjectLabelImage = new Image(new FileInputStream("Resources/openProjectPanelIco.png"));
        ImageView openProjectLabelImageView = new ImageView(openProjectLabelImage);
        openProjectLabelImageView.setFitWidth(WIDTH);
        openProjectLabelImageView.setFitHeight(HEIGHT);
        openProjectLabelImageView.setPreserveRatio(true);

        openProjectLabel.setGraphic(openProjectLabelImageView);

        pane.add(openProjectLabel , 0 , 3);


        //////////////////////////////////////////////////


        Button fastReportLabel = new JFXButton();
        fastReportLabel.setFont(new Font(fontName,fontSize));
        fastReportLabel.setText(" گزارش‌دهی سریع ");
        fastReportLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        fastReportLabel.setTextFill(WHITE);
        fastReportLabel.setContentDisplay(ContentDisplay.RIGHT);
        fastReportLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        fastReportLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    QCMap.getMap().getCurrentScene().onStop();
                    UI_Scene newScene = new FastReportScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Fast Report Page");
                    next_scene.centerOnScreen();
                    next_scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        GridPane.setHalignment(fastReportLabel , HPos.CENTER);

        Image fastReportLabelImage = new Image(new FileInputStream("Resources/fastReportPanelIco.png"));
        ImageView fastReportLabelImageView = new ImageView(fastReportLabelImage);
        fastReportLabelImageView.setFitWidth(WIDTH);
        fastReportLabelImageView.setFitHeight(HEIGHT);
        fastReportLabelImageView.setPreserveRatio(true);

        fastReportLabel.setGraphic(fastReportLabelImageView);

        pane.add(fastReportLabel , 0 , 4);


        //////////////////////////////////////////////////


        Button preferencesLabel = new JFXButton();
        preferencesLabel.setFont(new Font(fontName,fontSize));
        preferencesLabel.setText(" تنظیمات ");
        preferencesLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        preferencesLabel.setTextFill(WHITE);
        preferencesLabel.setContentDisplay(ContentDisplay.RIGHT);
        preferencesLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    QCMap.getMap().getCurrentScene().onStop();
                    UI_Scene newScene = new PreferencesScene();
                    Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                    next_scene.setTitle("Preferences");
                    next_scene.centerOnScreen();
                    next_scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        preferencesLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        GridPane.setHalignment(preferencesLabel , HPos.CENTER);


        Image preferencesLabelImage = new Image(new FileInputStream("Resources/preferencesPanelIco.png"));
        ImageView preferencesLabelImageView = new ImageView(preferencesLabelImage);
        preferencesLabelImageView.setFitWidth(WIDTH);
        preferencesLabelImageView.setFitHeight(HEIGHT);
        preferencesLabelImageView.setPreserveRatio(true);

        preferencesLabel.setGraphic(preferencesLabelImageView);

        pane.add(preferencesLabel , 0 , 5);

        //////////////////////////////////////////////////

        Button logOutLabel = new JFXButton();
        logOutLabel.setFont(new Font(fontName,fontSize));
        logOutLabel.setText(" خروج از حساب ");
        logOutLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        logOutLabel.setTextFill(WHITE);
        logOutLabel.setContentDisplay(ContentDisplay.RIGHT);
        logOutLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                QCMap.getMap().getCurrentScene().onStop();

                UI_Scene newScene = new LogInScene();
                Stage next_scene = (Stage) ((Node) event.getSource()).getScene().getWindow();
                QCMap.getMap().setDecoratorContent(newScene.get_Scene());
                next_scene.setTitle("Log In");
                next_scene.centerOnScreen();
                next_scene.show();
            }
        });
        logOutLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        GridPane.setHalignment(logOutLabel , HPos.CENTER);


        Image logOutLabelImage = new Image(new FileInputStream("Resources/logOutIco.png"));
        ImageView logOutLabelImageView = new ImageView(logOutLabelImage);
        logOutLabelImageView.setFitWidth(WIDTH);
        logOutLabelImageView.setFitHeight(HEIGHT);
        logOutLabelImageView.setPreserveRatio(true);

        logOutLabel.setGraphic(logOutLabelImageView);

        pane.add(logOutLabel , 0 , 6);


        //////////////////////////////////////////////////

        Button exitLabel = new JFXButton();
        exitLabel.setFont(new Font(fontName,fontSize));
        exitLabel.setText(" خروج ");
        exitLabel.setGraphicTextGap(QCConstantValues.MAIN_SCREEN_COLOR.getSaturation());
        exitLabel.setTextFill(WHITE);
        exitLabel.setContentDisplay(ContentDisplay.RIGHT);
        exitLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("");
                alert.setContentText("Do you want to exit?");
                Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
                try {
                    stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK)
                {
                    QCMap.getMap().getCurrentScene().onStop();
                    Platform.exit();
                }

            }
        });
        exitLabel.getStylesheets().add(this.getClass().getResource("UI_Styles/ButtonPanel.css").toExternalForm());
        GridPane.setHalignment(exitLabel , HPos.CENTER);


        Image exitLabelImage = new Image(new FileInputStream("Resources/Exit.png"));
        ImageView exitLabelImageView = new ImageView(exitLabelImage);
        exitLabelImageView.setFitWidth(WIDTH);
        exitLabelImageView.setFitHeight(HEIGHT);
        exitLabelImageView.setPreserveRatio(true);

        exitLabel.setGraphic(exitLabelImageView);

        pane.add(exitLabel , 0 , 7);

        return pane;
    }


    private void formAlert() throws Exception {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("News");
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
        grid.setMinSize(500 , 200);
        grid.setHgap(10);
        grid.setVgap(10);

        Text titleText = new Text("پیام‌های اخیر");
        titleText.setFont(new Font("IRANSans",18));
        titleText.setFill(WHITE);
        GridPane.setHalignment(titleText , HPos.RIGHT);
        grid.add(titleText , 0 , 0);

        GridPane tempGrid = new GridPane();
        tempGrid.setHgap(10);
        tempGrid.setVgap(10);
        tempGrid.setMinSize(500, 200);
        tempGrid.setAlignment(Pos.CENTER);

        JFXListView<NewsItem> centerJFXListView = new JFXListView<>();
        List<NewsItem> news = QCStructure.getStructure().getNews();

        if(news.isEmpty() || (news.size() == 0))
        {
            news = new ArrayList<>();
        }

        centerJFXListView.getItems().addAll(news);

        centerJFXListView.setMinWidth(450);
        centerJFXListView.setMaxWidth(450);
        centerJFXListView.setMaxHeight(180);
        centerJFXListView.setMinHeight(180);

        centerJFXListView.getStylesheets().add(this.getClass().getResource("UI_Styles/ListView.css").toExternalForm());
        centerJFXListView.setCellFactory(lv -> new ListCell<NewsItem>()
        {
            @Override
            protected void updateItem(NewsItem item, boolean empty) {
                super.updateItem(item, empty);

                if(item != null)
                {
                    GridPane pane = new GridPane();
                    pane.setAlignment(Pos.CENTER_RIGHT);
                    pane.setHgap(80);
                    pane.setVgap(5);

                    Text nameText = new Text(" " + item.getTitle());
                    nameText.setFont(new Font("IRANSans" , 17));
                    nameText.setFill(Color.WHITE);
                    GridPane.setHalignment(nameText , HPos.RIGHT);

                    Text dateText = new Text("تاریخ پیام: " + item.getDate());
                    dateText.setFont(new Font("IRANSans" , 15));
                    dateText.setFill(Color.WHITE);
                    GridPane.setHalignment(dateText , HPos.RIGHT);

                    Text cityText = new Text(" " + item.getContent());
                    cityText.setFont(new Font("IRANSans" , 15));
                    cityText.setFill(Color.WHITE);
                    GridPane.setHalignment(cityText , HPos.RIGHT);


                    pane.add(nameText , 0 , 0);
                    pane.add(dateText , 0 , 1);
                    pane.add(cityText , 0 , 2);

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

        tempGrid.add(centerJFXListView , 0 , 0);

        grid.add(tempGrid , 0 , 1);
        grid.setAlignment(Pos.CENTER);

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

}
