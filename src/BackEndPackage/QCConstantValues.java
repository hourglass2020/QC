package BackEndPackage;

import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class QCConstantValues
{
    public static String DEFAULT_THEME = "TextField.css";
    public static String DARK_THEME = "UI_Styles/Button.css";
    public static String LIGHT_THEME ;

    public static String MAIN_THEME = "TextField.css";

    public static Color TITLE_COLOR_DEFAULT = Color.valueOf("#009999");
    public static Color FONT_COLOR_DEFAULT = Color.WHITE;
    public static Color SCREEN_COLOR_DEFAULT = Color.valueOf("#0f233e");

    public static Color TITLE_COLOR_LIGHT = Color.valueOf("#004d4d");
    public static Color FONT_COLOR_LIGHT = Color.WHITE;
    public static Color SCREEN_COLOR_LIGHT = Color.valueOf("#0e4c62");

    public static Color TITLE_COLOR_DARK = Color.valueOf("#ff3300");
    public static Color FONT_COLOR_DARK = Color.LIGHTSLATEGRAY;
    public static Color SCREEN_COLOR_DARK = Color.valueOf("#333333");

    public static Color MAIN_TITLE_COLOR = TITLE_COLOR_DEFAULT;
    public static Color MAIN_FONT_COLOR = FONT_COLOR_DEFAULT;
    public static Color MAIN_SCREEN_COLOR = SCREEN_COLOR_DEFAULT;

    public static double FONT_REGULAR_SIZE = 16;

    public static BackgroundFill background_fill = new BackgroundFill(MAIN_SCREEN_COLOR,
            CornerRadii.EMPTY, Insets.EMPTY);

    public static Color SECOND_BACK = Color.valueOf("#050c15");

    public static BackgroundFill background_fill2 = new BackgroundFill(SECOND_BACK,
            CornerRadii.EMPTY, Insets.EMPTY);

    public static Color THIRD_BACK = Color.valueOf("#12043e");

    public static BackgroundFill background_fill3 = new BackgroundFill(THIRD_BACK,
            CornerRadii.EMPTY, Insets.EMPTY);

    public static final String[] TITLES = {"InstallDevice.png" , "OpenProject.png"
                                         , "NewProject.png"  , "Preferences.png" , "Resources/Home.png"
                                         , "NewProjectIco.png" , "Resources/OpenProjectIco.png"
                                         , "Resources/InstallDeviceIco.png" , "Resources/Successful.png" ,
                                            "PixelProgrammingGP.png" , "Resources/DeviceListIco.png" ,
                                            "AddIco.png" , "itemBG.png" , "Resources/DeviceSceneIco.png",
                                            "EditIco.png" , "Resources/ReportIco.png" ,
                                             "Resources/WarningIco.png" , "Centers.png" , "PreferencesIco.png",
                                                "Resources/Result.png" , "FastReportIco.png"};
}
