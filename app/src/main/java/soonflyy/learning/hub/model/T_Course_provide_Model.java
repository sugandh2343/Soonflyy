package soonflyy.learning.hub.model;

public class T_Course_provide_Model {
    String title;
    boolean isChecked;

    public T_Course_provide_Model() {
    }

    public T_Course_provide_Model(String title, boolean isChecked) {
        this.title = title;
        this.isChecked = isChecked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
