package soonflyy.learning.hub.Common_Package.Common_Package.Models;

public class Video_Quality_Speed {
    String text;
    boolean checked;

    public Video_Quality_Speed(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
