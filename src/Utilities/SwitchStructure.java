package Utilities;

public class SwitchStructure
{
    private String title;
    private String caption;

    public SwitchStructure()
    {
        this.title = null;
        this.caption = null;
    }

    public SwitchStructure(String title, String caption)
    {
        this.caption = caption;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
