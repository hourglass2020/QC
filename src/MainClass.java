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
//        map.makeDevicesFile();
       map.saveCompany();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
