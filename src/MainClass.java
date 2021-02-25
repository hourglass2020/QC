import BackEndPackage.QCMap;
import UIPackage.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.FileInputStream;

public class MainClass extends Application
{
    QCMap map = QCMap.getMap();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        UI_Scene scene = new LogInScene();
        map.initCompany();
        Image icon = new Image(new FileInputStream("Resources/QC.png"));

        map.setDecorator(primaryStage , scene.get_Scene().getRoot());
        primaryStage.setScene(new Scene(map.getJfxDecorator()));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("QC");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        map.makeDevicesFile();
        map.saveCompany();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


/*
    Circle circle = new Circle();

        circle.setCenterX(300.0f);
                circle.setCenterY(135.0f);
                circle.setRadius(20.0f);
                circle.setFill(Color.valueOf("#0a4969"));
                circle.setStrokeWidth(20);
                GridPane.setHalignment(circle , HPos.CENTER);
                Path path = new Path();

                MoveTo moveTo = new MoveTo(100, 150);

                CubicCurveTo cubicCurveTo = new CubicCurveTo(400, 40, 175, 250, 500, 150);

                path.getElements().add(moveTo);
                path.getElements().add(cubicCurveTo);

                FillTransition fillTransition = new FillTransition(Duration.millis(1000));
                fillTransition.setShape(circle);
                fillTransition.setFromValue(Color.valueOf("#42dad9"));
                fillTransition.setToValue(Color.valueOf("#146886"));
                fillTransition.setCycleCount(7);
                fillTransition.setAutoReverse(true);

                PathTransition pathTransition = new PathTransition();
                pathTransition.setDuration(Duration.millis(1000));
                pathTransition.setNode(circle);
                pathTransition.setPath(path);
                pathTransition.setOrientation(PathTransition.OrientationType.
                ORTHOGONAL_TO_TANGENT);
                pathTransition.setCycleCount(7);
                pathTransition.setAutoReverse(true);

                //Playing the animation
                pathTransition.play();
                fillTransition.play();


                Text describe_text = new Text();
                describe_text.setFont(new Font("REVOLUTION",28));
                describe_text.setText("Quality Control Program");
                describe_text.setFill(Color.WHITE);
                describe_text.setStrokeWidth(1);
                describe_text.setStrokeLineJoin(StrokeLineJoin.MITER);

                GridPane pane = new GridPane();
                pane.setAlignment(Pos.CENTER);
                pane.setVgap(40);
                pane.add(describe_text , 0 , 0);
                pane.add(circle , 0 , 1);

                Scene scene = new Scene(pane, 500, 210);
                scene.setFill(QCConstantValues.SCREEN_COLOR_DEFAULT);

                pathTransition.statusProperty().addListener(new ChangeListener<Animation.Status>() {
@Override
public void changed(ObservableValue<? extends Animation.Status> observable, Animation.Status oldValue, Animation.Status newValue) {
        if(newValue == Animation.Status.STOPPED) {
        UI_Scene mainScene = new LogInScene();

//                    primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(mainScene.get_Scene());
        primaryStage.setTitle("Log In");
        primaryStage.centerOnScreen();
        primaryStage.show();
        }
        }
        });
*/

