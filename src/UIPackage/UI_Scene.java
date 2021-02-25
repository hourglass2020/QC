package UIPackage;

import com.jfoenix.controls.JFXDecorator;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class UI_Scene
{
    public Scene current_scene;


    public Scene get_Scene() {

        return (this.current_scene);
    }

    abstract public void onStop();

}
