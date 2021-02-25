package BackEndPackage;

import java.sql.PreparedStatement;

public class Preferences
{
    private static Preferences preferences = new Preferences();

    private Preferences(){}

    public static Preferences getPreferences(){ return preferences; }

    public void changeTheme(int choice)
    {
        if (choice == 1)
        {
            QCConstantValues.MAIN_FONT_COLOR = QCConstantValues.FONT_COLOR_LIGHT;
            QCConstantValues.MAIN_SCREEN_COLOR = QCConstantValues.SCREEN_COLOR_LIGHT;
        }

        if (choice == 2)
        {
            QCConstantValues.MAIN_FONT_COLOR = QCConstantValues.FONT_COLOR_DARK;
            QCConstantValues.MAIN_SCREEN_COLOR = QCConstantValues.SCREEN_COLOR_DARK;
        }
    }
}
